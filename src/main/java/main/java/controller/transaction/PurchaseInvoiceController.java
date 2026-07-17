package main.java.main.java.controller.transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.*;
import main.java.main.java.hibernate.service.service.*;
import main.java.main.java.hibernate.service.serviceImpl.*;
import main.java.main.java.hibernate.util.CommonData;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;

public class PurchaseInvoiceController implements Initializable {
	  	@FXML private TextField txtBillNo;
	  	@FXML private TextField txtInvoiceNo;
	  	@FXML private DatePicker date;
	    @FXML private ComboBox<String> cmbPartyName;
	    @FXML private Button btnSearch;
	    @FXML private Button btnAddParty;
	    @FXML private TextArea txtPartyInfo;
	    @FXML private TextField txtItemName;
	    @FXML private TextField txtUnit;
	    @FXML private TextField txtQuantity;
	    @FXML private TextField txtRate;
	    @FXML private TextField txtAmount;
	    @FXML private Button btnAdd;
	    @FXML private Button btnClear;
	    @FXML private Button btnEdit;
	    @FXML private Button btnRemove;
	    @FXML private TableView<PurchaseTransaction> table;
	    @FXML private TableColumn<PurchaseTransaction, Long> colSrNo;
	    @FXML private TableColumn<PurchaseTransaction,String> colItemName;
	    @FXML private TableColumn<PurchaseTransaction,String> colUnit;
	    @FXML private TableColumn<PurchaseTransaction,Float> colQty;
	    @FXML private TableColumn<PurchaseTransaction,Float> colRate;
	    @FXML private TableColumn<PurchaseTransaction,Float> colIgstPct;
	    @FXML private TableColumn<PurchaseTransaction,Float> colIgstAmt;
	    @FXML private TableColumn<PurchaseTransaction,Float> colAmount;
	    @FXML private Button btnsave;
	    @FXML private Button btnClear2;
	    @FXML private Button btnBack;
	    @FXML private Button btnPrint;
	    @FXML private AnchorPane rootPane;
	    @FXML private TextField txtNetTotal;
	    @FXML private TextField txtIgstPct;
	    @FXML private TextField txtGst;
	    @FXML private TextField txtOtherChrgs;
	    @FXML private TextField txtTransportChrgs;
	    @FXML private TextField txtGrandTotal;
	    @FXML private ComboBox<String> cmbpaymentFrom;
	    @FXML private TextField txtTransactionReff;
	    @FXML private TextField txtPaidAmount;
	    @FXML private TextField txtAdvancePaid;


	    private ObservableList<String>partyNameList = FXCollections.observableArrayList();
	    //private ObservableList<String>itemNameList = FXCollections.observableArrayList();
	   // private ObservableList<Item>itemList = FXCollections.observableArrayList();
	    private ObservableList<PurchaseTransaction>transactionList = FXCollections.observableArrayList();

	    private PurchasePartyService partyService;
	    private PurchaseInvoiceService purchaseInvoiceService;
	    private ItemService itemService;
	    private BankService bankService;
	   // private SuggestionProvider<String>itemNameProvider;
	    
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			partyService = new PurchasePartyServiceImpl();
			purchaseInvoiceService = new PurchaseInvoiceServiceImpl();
			itemService = new ItemServiceImpl();
			bankService = new BankServiceImpl();

			partyNameList.addAll(partyService.getAllPurchasePartyNames());
			cmbPartyName.getItems().addAll(partyNameList);

			txtBillNo.setText("" + purchaseInvoiceService.getNewInvoiceNo());

			
			CommonData.setItemNames();
			//itemNameProvider = SuggestionProvider.create(CommonData.itemNames);
			//new AutoCompletionTextFieldBinding<>(txtItemName, itemNameProvider);
			TextFields.bindAutoCompletion(txtItemName, CommonData.itemNames);

			colSrNo.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, Long>("id"));
			colItemName.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, String>("itemname"));
			colUnit.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, String>("unit"));
			colQty.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, Float>("quantity"));
			colRate.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, Float>("rate"));
			colIgstPct.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, Float>("igstPercent"));
			colIgstAmt.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, Float>("igstAmount"));
			colAmount.setCellValueFactory(new PropertyValueFactory<PurchaseTransaction, Float>("amount"));
			table.setItems(transactionList);

			cmbpaymentFrom.getItems().addAll(bankService.getAllBankNames());
			cmbpaymentFrom.getSelectionModel().selectFirst();
			

			if (CommonData.editInvoiceNo != 0) {
				loadInvoiceForEdit(CommonData.editInvoiceNo);
				CommonData.editInvoiceNo = 0;
			}
		}

		@FXML
		void searchPurchaseParty(ActionEvent event) {
			if (cmbPartyName.getSelectionModel().getSelectedItem().equals("")) {
				cmbPartyName.requestFocus();
				return;
			}
			PurchaseParty party = partyService
					.getPurchasePartyByName(cmbPartyName.getSelectionModel().getSelectedItem());
			txtPartyInfo.setText(party.toString());
			txtAdvancePaid.setText(""+(
					new AdvancePaymentServiceImpl().getPartyAdvancePayment(party.getId())-
					purchaseInvoiceService.getAllPaidAmountByparty(party.getId())-
					purchaseInvoiceService.getAllPaidAmountByparty(party.getId())
					));
		}

		@FXML
		void txtItemNameAction(ActionEvent event) {
			if (txtItemName.getText().equals("") || txtItemName.getText().equals(null)) {
				return;
			}
			Item item = itemService.getItemByName(txtItemName.getText());
			if (item == null) {
				return;
			}
			txtUnit.setText(item.getUnit());
			txtIgstPct.setText("" + item.getIgst());
			txtQuantity.requestFocus();
		}

		@FXML
		void txtQtyAction(ActionEvent event) {
			if (txtQuantity.getText().equals("") || txtQuantity.getText() == null) {
				return;
			}
			if (txtItemName.getText().equals("") || txtUnit.getText().equals("")) {
				new Alert(AlertType.ERROR, "Select Item Again!!!").showAndWait();
				txtItemName.requestFocus();
				return;
			}
			if (!isNumber(txtQuantity.getText())) {
				new Alert(AlertType.ERROR, "Enter Quantity in Digit!!!").showAndWait();
				txtQuantity.requestFocus();
				txtQuantity.selectAll();
				return;
			}
			if (!txtRate.getText().equals("") && isNumber(txtRate.getText())) {
				txtAmount.setText("" + (Float.parseFloat(txtRate.getText()) * Float.parseFloat(txtQuantity.getText())));
				txtRate.requestFocus();
				return;
			}
			txtRate.requestFocus();
		}

		@FXML
		void txtQtyKeyEvent(KeyEvent event) {
			if (txtQuantity.getText().equals("-")) {
				return;
			}
			if (!isNumber(txtQuantity.getText())) {
				txtQuantity.setText("");
			}
		}

		@FXML
		void txtRateAction(ActionEvent event) {
			if (txtRate.getText().equals("") || txtRate.getText() == null) {
				return;
			}
			if (txtItemName.getText().equals("") || txtUnit.getText().equals("")) {
				new Alert(AlertType.ERROR, "Select Item Again!!!").showAndWait();
				txtItemName.requestFocus();
				return;
			}
			if (!isNumber(txtRate.getText())) {
				new Alert(AlertType.ERROR, "Enter Rate in Digit!!!").showAndWait();
				txtRate.requestFocus();
				txtRate.selectAll();
				return;
			}
			if (!txtQuantity.getText().equals("") && isNumber(txtQuantity.getText())) {
				txtAmount.setText("" + (Float.parseFloat(txtRate.getText()) * Float.parseFloat(txtQuantity.getText())));
				btnAdd.requestFocus();
				return;
			}
		}

		@FXML
		void txtRateKeyEvent(KeyEvent event) {
			if (txtRate.getText().equals("-")) {
				return;
			}
			if (!isNumber(txtRate.getText())) {
				txtRate.setText("");
			}
		}

		@FXML
		void btnAddAction(ActionEvent event) {

			if (validateData() != 1) {
				return;
			}
			float igstPct = isNumber(txtIgstPct.getText()) ? Float.parseFloat(txtIgstPct.getText()) : 0f;
			float baseAmt = Float.parseFloat(txtAmount.getText());
			float igstAmt = Math.round(baseAmt * igstPct / 100f * 100f) / 100f;

			PurchaseTransaction transaction = new PurchaseTransaction(txtItemName.getText(), txtUnit.getText(),
					Float.parseFloat(txtRate.getText()), Float.parseFloat(txtQuantity.getText()),
					baseAmt, null);
			transaction.setIgstPercent(igstPct);
			transaction.setIgstAmount(igstAmt);

			addPurchaseTransaction(transaction);
			recalculateIgstTotal();
			calculateGrandTotal();
			clear();
		}

		@FXML
		void txtGstAction(ActionEvent event) {
			calculateGrandTotal();
		}

		@FXML
		void txtGstKeyEvent(KeyEvent event) {
			numberField(txtGst);
			calculateGrandTotal();
		}

		@FXML
		void txtOtherChrgsAction(ActionEvent event) {
			calculateGrandTotal();
		}

		@FXML
		void txtOtherChrgsKeyEvent(KeyEvent event) {
			numberField(txtOtherChrgs);
			calculateGrandTotal();
		}

		@FXML
		void txtTransportChargsAction(ActionEvent event) {
			calculateGrandTotal();
		}

		@FXML
		void txtTransportChargsKeyEvent(KeyEvent event) {
			numberField(txtTransportChrgs);
			calculateGrandTotal();
		}

		@FXML
		void txtPaidAmountKeyEvent(KeyEvent event) {
			numberField(txtPaidAmount);
		}

		@FXML
		void btnSaveAction(ActionEvent event) {
			if (validateFormData() != 1) {
				return;
			}
			BankTransactionService btService = new BankTransactionServiceImpl();
			boolean advance=false;
			if(Float.parseFloat(txtAdvancePaid.getText())>=Float.parseFloat(txtGrandTotal.getText()))
			{
				
				txtPaidAmount.setText(""+(
						Float.parseFloat(txtGrandTotal.getText())
						));
				advance=true;
				
			}
			PurchaseInvoice invoice = new PurchaseInvoice();			
			invoice.setBillno(Long.parseLong(txtBillNo.getText()));
			PurchaseInvoice oldInvoice = purchaseInvoiceService.getPurchaseInvoice(invoice.getBillno());
			invoice.setInvoiceNo(txtInvoiceNo.getText());
			invoice.setDate(date.getValue());
			invoice.setParty(partyService.getPurchasePartyByName(cmbPartyName.getSelectionModel().getSelectedItem()));
			invoice.setGrandtotal(Float.parseFloat(txtGrandTotal.getText()));
			invoice.setGst(Float.parseFloat(txtGst.getText()));
			invoice.setNettotal(Float.parseFloat(txtNetTotal.getText()));
			invoice.setOthercharges(Float.parseFloat(txtOtherChrgs.getText()));
			invoice.setBankreffno(txtTransactionReff.getText());
			invoice.setTransportcharges(Float.parseFloat(txtTransportChrgs.getText()));
			invoice.setPaid(Float.parseFloat(txtPaidAmount.getText()));
			
			if (txtPaidAmount.getText().equals("" + 0.0)) {
				invoice.setBank(bankService.getCashAccount());
			} else {
				invoice.setBank(bankService.getBankByName(cmbpaymentFrom.getSelectionModel().getSelectedItem()));
				float oldpaid = 0.0f;
				if(oldInvoice!=null)
				{
					if(btService.getBankTransactionByPartucular("Reduce Invoice Amount BillNo " + oldInvoice.getBillno())!=null)
					oldpaid=oldInvoice.getPaid();
				}
				if (oldpaid!=0 && (oldpaid+ bankService.getBankBalance(invoice.getBank().getId())) < invoice.getPaid()) {
					new Alert(AlertType.ERROR, "Insufficient Amount in Bank Account\n Please Choose Another Bank")
							.showAndWait();
					return;
				}
			}
			if(oldInvoice!=null && checkItemStock(oldInvoice.getTransaction())==0)
			{
				new Alert(AlertType.ERROR,"Cant Edit This Bill Items Already sold!!!").showAndWait();
				return;
			}
			for (int i = 0; i < transactionList.size(); i++) {
				transactionList.get(i).setInvoiceno(invoice);
				transactionList.get(i).setId(0);
			}
			invoice.setTransaction(transactionList);
			
			
			int flag = purchaseInvoiceService.savePurchaseInvoice(invoice);
			
			if (flag == 1) {
				if (invoice.getPaid() > 0) {
					if(advance==false) {
						BankTransaction bt = new BankTransaction("Reduce Invoice Amount BillNo " + invoice.getBillno(),
							invoice.getBillno(), 0, invoice.getPaid(), invoice.getBank().getId(),date.getValue());
					
						int f = btService.saveBankTransaction(bt);
						if (f == 1) {
							bankService.reduceBankBalance(invoice.getBank().getId(), invoice.getPaid());
						}
					}
				}
				addStock(invoice.getTransaction());
				new Alert(AlertType.INFORMATION, "Bill Save Success").showAndWait();
				btnClear2.fire();
			}
			else if(flag==2)
			{
				BankTransaction bt = new BankTransaction();
				if (oldInvoice.getPaid() > 0.0 && !advance) {
					bt = new BankTransaction();
					bt.setBankid(oldInvoice.getBank().getId());
					bt.setCredit(0.0f);
					bt.setDebit(oldInvoice.getPaid());
					bt.setParticulars("Edit Invoice BillNo " + oldInvoice.getBillno());
					bt.setReffid(oldInvoice.getBillno());
					bt.setDate(date.getValue());
					int f = btService.saveBankTransaction(bt);
					if (f == 1) {
						bankService.addBankBalance(oldInvoice.getBank().getId(), oldInvoice.getPaid());
					}
				}
				reduceStock(oldInvoice.getTransaction());
				if(invoice.getPaid()>0.0)
				{
					bt = new BankTransaction();
					bt.setBankid(invoice.getBank().getId());
					bt.setCredit(invoice.getPaid());
					bt.setDebit(0.0f);
					bt.setParticulars("Reduce Invoice Amount BillNo " + invoice.getBillno());
					bt.setReffid(invoice.getBillno());
					bt.setDate(date.getValue());
					int f = btService.saveBankTransaction(bt);
					if (f == 1) {
						bankService.reduceBankBalance(invoice.getBank().getId(), invoice.getPaid());
					}
				}
				addStock(invoice.getTransaction());
				new Alert(AlertType.INFORMATION,"Invoice Edit Success").showAndWait();
				btnClear2.fire();
			}
			CommonData.setStockItemNames();
			CommonData.setItemNames();
			
		}

		@FXML
		void btnClearAction(ActionEvent event) {
			clear();
		}

		@FXML
		void btnEditAction(ActionEvent event) {
			PurchaseTransaction tr = table.getSelectionModel().getSelectedItem();
			if (tr == null) {
				return;
			}
			txtItemName.setText(tr.getItemname());
			txtUnit.setText(tr.getUnit());
			txtRate.setText("" + tr.getRate());
			txtQuantity.setText("" + tr.getQuantity());
			txtIgstPct.setText("" + tr.getIgstPercent());
			txtRate.fireEvent(event);
		}

		@FXML
		void btnRemoveAction(ActionEvent event) {

			txtNetTotal.setText("" + (Float.parseFloat(txtNetTotal.getText())
					- table.getSelectionModel().getSelectedItem().getAmount()));
			transactionList.remove(table.getSelectionModel().getSelectedIndex());
			int sr = 1;
			for (int i = 0; i < transactionList.size(); i++) {
				transactionList.get(i).setId(sr++);
			}
			recalculateIgstTotal();
			calculateGrandTotal();

		}

		@FXML
		void btnClear2Action(ActionEvent event) {
			txtInvoiceNo.setText("");
			date.setValue(null);
			cmbPartyName.getSelectionModel().clearSelection();
			txtPartyInfo.setText("");
			clear();
			transactionList.clear();
			txtNetTotal.setText("" + 0.0f);
			txtGst.setText("" + 0.0f);
			txtOtherChrgs.setText("" + 0.0f);
			txtTransportChrgs.setText("" + 0.0f);
			txtGrandTotal.setText("" + 0.0f);
			txtTransactionReff.setText("" + 0.0f);
			cmbpaymentFrom.getSelectionModel().clearSelection();
			txtBillNo.setText("" + purchaseInvoiceService.getNewInvoiceNo());
			txtInvoiceNo.requestFocus();
			txtPaidAmount.setText(""+0.0f);
			txtAdvancePaid.setText(""+0.0f);
		}

		@FXML
		void backAction(ActionEvent event) {
			BorderPane bp = (BorderPane) rootPane.getParent();
			if (bp == null) return;
			Pane dashboard = new ViewUtil().getPage("transaction/PurchaseInvoiceDashboard");
			if (dashboard != null) bp.setCenter(dashboard);
		}

		private void loadInvoiceForEdit(long billno) {
			PurchaseInvoice invoice = purchaseInvoiceService.getPurchaseInvoice(billno);
			if (invoice == null) return;
			txtBillNo.setText("" + invoice.getBillno());
			txtInvoiceNo.setText(invoice.getInvoiceNo());
			date.setValue(invoice.getDate());
			cmbPartyName.getSelectionModel().select(invoice.getParty().getName());
			btnSearch.fire();
			long sr = 0;
			transactionList.clear();
			for (PurchaseTransaction tr : invoice.getTransaction()) {
				tr.setId(++sr);
				transactionList.add(tr);
			}
			txtNetTotal.setText("" + invoice.getNettotal());
			txtGst.setText("" + invoice.getGst());
			txtOtherChrgs.setText("" + invoice.getOthercharges());
			txtTransportChrgs.setText("" + invoice.getTransportcharges());
			txtGrandTotal.setText("" + invoice.getGrandtotal());
			if (invoice.getPaid() != 0) {
				cmbpaymentFrom.getSelectionModel().select(invoice.getBank().getBankname());
			}
			txtTransactionReff.setText(invoice.getBankreffno());
			txtPaidAmount.setText("" + invoice.getPaid());
		}

		private void clear() {
			txtItemName.setText("");
			txtUnit.setText("");
			txtRate.setText("");
			txtAmount.setText("");
			txtQuantity.setText("");
			txtIgstPct.setText("");
			txtItemName.requestFocus();
		}

		private void numberField(TextField text) {
			if (!isNumber(text.getText())) {
				text.setText("");
			}
		}

		private int validateData() {
			try {
				if (txtItemName.getText().equals("")) {
					new Alert(AlertType.ERROR, "Enter Item Name").showAndWait();
					return 0;
				}
				if (txtUnit.getText().equals("")) {
					new Alert(AlertType.ERROR, "select Item Again").showAndWait();
					return 0;
				}
				if (txtQuantity.getText().equals("")) {
					new Alert(AlertType.ERROR, "Enter Item Quantity").showAndWait();
					return 0;
				}
				if (txtRate.getText().equals("")) {
					new Alert(AlertType.ERROR, "Enter Rate").showAndWait();
					return 0;
				}
				if (txtAmount.getText().equals("")) {
					new Alert(AlertType.ERROR, "select Item Again").showAndWait();
					return 0;
				}
				return 1;
			} catch (Exception e) {
				new Alert(AlertType.ERROR, "Error \n" + e.getMessage()).showAndWait();
				return 0;
			}
		}

		private void addPurchaseTransaction(PurchaseTransaction tr) {
			try {
				if (transactionList.size() == 0) {
					tr.setId(1);
					transactionList.add(tr);
					txtNetTotal.setText("" + (Float.parseFloat(txtNetTotal.getText()) + tr.getAmount()));
					calculateGrandTotal();
				} else {
					int index = -1;
					for (int i = 0; i < transactionList.size(); i++) {
						if (transactionList.get(i).getItemname().equals(tr.getItemname())
								&& transactionList.get(i).getUnit().equals(tr.getUnit())
								&& transactionList.get(i).getRate() == tr.getRate()) {
							index = i;
							break;
						}
					}
					if (index != -1) {

						txtNetTotal.setText("" + (Float.parseFloat(txtNetTotal.getText()) + tr.getAmount()));
						tr.setQuantity(transactionList.get(index).getQuantity() + tr.getQuantity());
						tr.setAmount(tr.getRate() * tr.getQuantity());
						tr.setIgstAmount(Math.round(tr.getAmount() * tr.getIgstPercent() / 100f * 100f) / 100f);
						tr.setId(transactionList.get(index).getId());
						transactionList.remove(index);
						transactionList.add(index, tr);

					} else {
						System.out.println("Item Not Found");
						txtNetTotal.setText("" + (Float.parseFloat(txtNetTotal.getText()) + tr.getAmount()));
						calculateGrandTotal();
						tr.setId(transactionList.size() + 1);
						transactionList.add(tr);

					}
				}
			} catch (Exception e) {
				new Alert(AlertType.ERROR, "Error " + e.getMessage()).showAndWait();
				e.printStackTrace();
			}
		}

		private boolean isNumber(String num) {
			if (num == null) {
				return false;
			}
			try {
				Float.parseFloat(num);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		private void recalculateIgstTotal() {
			float total = 0f;
			for (PurchaseTransaction t : transactionList) {
				total += t.getIgstAmount();
			}
			txtGst.setText("" + total);
		}

		private void calculateGrandTotal() {
			if (txtGst.getText().equals("")) {
				txtGst.setText("" + 0.0f);
			}
			if (txtTransportChrgs.getText().equals("")) {
				txtTransportChrgs.setText("" + 0.0f);
			}
			if (txtOtherChrgs.getText().equals("")) {
				txtOtherChrgs.setText("" + 0.0f);
			}
			if (isNumber(txtOtherChrgs.getText()) == false) {
				new Alert(AlertType.ERROR, "Enter Other Charges Amount in Digit!!!").showAndWait();
				txtOtherChrgs.requestFocus();
				return;
			}
			if (isNumber(txtTransportChrgs.getText()) == false) {
				new Alert(AlertType.ERROR, "Enter Transport Charges Amount in Digit!!!").showAndWait();
				txtTransportChrgs.requestFocus();
				return;
			}
			txtGrandTotal.setText("" + (Float.parseFloat(txtNetTotal.getText())
					+ Float.parseFloat(txtGst.getText())
					+ Float.parseFloat(txtOtherChrgs.getText())
					+ Float.parseFloat(txtTransportChrgs.getText())));

		}

		private int validateFormData() {
			try {
				if (txtInvoiceNo.getText().equals("") || txtInvoiceNo.getText().equals(null)) {
					new Alert(AlertType.ERROR, "Enter Party Invoice No!!!").showAndWait();
					txtInvoiceNo.requestFocus();
					return 0;
				}
				if (date.getValue() == null) {
					new Alert(AlertType.ERROR, "Enter Invoice Date!!!").showAndWait();
					date.requestFocus();
					return 0;
				}
				if (txtPartyInfo.getText().equals("") || txtPartyInfo.getText().equals(null)) {
					new Alert(AlertType.ERROR, "Select Party Again No!!!").showAndWait();
					cmbPartyName.requestFocus();
					return 0;
				}
				if (txtGrandTotal.getText().equals("") || txtGrandTotal.getText().equals("" + 0.0f)) {
					new Alert(AlertType.ERROR, "No Data to Save!!!\n Add Items from Invoice").showAndWait();
					txtItemName.requestFocus();
					return 0;
				}

				if (txtTransactionReff.getText().equals("")) {
					txtTransactionReff.setText("0");
				}
				if (txtPaidAmount.getText().equals("")) {
					txtPaidAmount.setText("" + 0.0f);
				}
				if (Float.parseFloat(txtPaidAmount.getText()) > 0.0f && cmbpaymentFrom.getValue() == null) {
					new Alert(AlertType.ERROR, "Select Payment From Bank Name").showAndWait();
					cmbpaymentFrom.requestFocus();
					return 0;
				}
				
				if (Float.parseFloat(txtPaidAmount.getText()) > Float.parseFloat(txtGrandTotal.getText())) {
					new Alert(AlertType.ERROR, "Paying Amount Should Be equal Or Less Than Grand Total").showAndWait();
					txtPaidAmount.requestFocus();
					txtPaidAmount.selectAll();
					return 0;
				}
				return 1;
			} catch (Exception e) {
				new Alert(AlertType.ERROR, "Error\n" + e.getMessage()).showAndWait();
				e.printStackTrace();
				return 0;
			}
		}
		private void addStock(List<PurchaseTransaction> list)
		{
			try {
				ItemStockService stockService = new ItemStockServiceImpl();
				ItemStock stock;
				for(PurchaseTransaction tr:list)
				{
					System.out.println("Got Qty to Add"+tr.getQuantity());
					stock = stockService.getItemStockByItemName(tr.getItemname());
					if(stock!=null)
					stock.setQuantity(tr.getQuantity());
					else
						stock = new ItemStock(tr.getItemname(),tr.getUnit(),tr.getQuantity());
					stockService.saveItemStock(stock);
					stock=null;
				}
			} catch (Exception e) {
				new Alert(AlertType.ERROR,"Error in Adding Stock").showAndWait();
			}
		}
		private void reduceStock(List<PurchaseTransaction> list)
		{
			try {
				for(int i=0;i<list.size();i++)
				{
					list.get(i).setQuantity(list.get(i).getQuantity()-(list.get(i).getQuantity()*2));
				}
				addStock(list);
				
			} catch (Exception e) {
				new Alert(AlertType.ERROR,"Unable To Reduce Item From Stock").showAndWait();
			}
		}
		private int checkItemStock(List<PurchaseTransaction>list)
		{
			try {
				ItemStockService stockService = new ItemStockServiceImpl();
				int flag=1;
				for(PurchaseTransaction tr:list)
				{
					if(tr.getQuantity()>stockService.getItemStock(tr.getItemname()))
					{
						flag=0;
						break;
					}
				}
				return flag;
			} catch (Exception e) {
				new Alert(AlertType.ERROR,"Error in Checking Item Stock").showAndWait();
				return 0;
			}
		}
}
