package main.java.main.java.controller.transaction;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.main.java.Main;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.*;
import main.java.main.java.hibernate.service.service.*;
import main.java.main.java.hibernate.service.serviceImpl.*;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.hibernate.util.GetBackup;
import main.java.main.java.print.CouriorReceipt;
import main.java.main.java.print.GenerateBill;
import main.java.main.java.print.PrintFile;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BillControler implements Initializable{

	@FXML private BorderPane mainPanel;
	@FXML private TextField txtBillNo;
    @FXML private DatePicker date;
    @FXML private TextField txtCustomerName;
    @FXML private Button btnSearch;
    @FXML private Button btnNew;
    @FXML private TextArea txtCustomerInfo;
    @FXML private ComboBox<String> cmbSalesman;
    @FXML private TextField txtItemName;
    @FXML private TextField txtUnit;
    @FXML private TextField txtQty;
    @FXML private TextField txtRate;
    @FXML private TextField txtAmount;
    @FXML private Button btnAdd;
    @FXML private Button btnClear;
    @FXML private Button btnRemove;
    @FXML private Button btnUpdate;
    @FXML private TableView<Transaction> table;
    @FXML private TableColumn<Transaction, Long> colSrNo;
    @FXML private TableColumn<Transaction, String> colItemName;
    @FXML private TableColumn<Transaction, String> colUnit;
    @FXML private TableColumn<Transaction, Float> colQty;
    @FXML private TableColumn<Transaction, Float> colRate;
    @FXML private TableColumn<Transaction, Float> colAmount;
    @FXML private Label lblMode;
    @FXML private Button btnSave;
    @FXML private Button btnClearBill;
    @FXML private Button btnExit;
    @FXML private Button btnPrint;
    @FXML private ComboBox<String> cmbRecievedBy;
    @FXML private ComboBox<String> cmbBankName;
    @FXML private TextField txtReffNo;
    @FXML private TextField txtNetTotal;
    @FXML private TextField txtTransoChrgs;
    @FXML private TextField txtOtherChargs;
    @FXML private TextField txtGrandTotal;
    @FXML private TextField txtReivedAmount;

    @FXML private Button btnAddPayment;
    @FXML private Button btnRemovePayment;
    @FXML private TableView<BillPayment> tablePayments;
    @FXML private TableColumn<BillPayment, String> colPayBank;
    @FXML private TableColumn<BillPayment, String> colPayRef;
    @FXML private TableColumn<BillPayment, Float> colPayAmount;
    @FXML private TextField txtTotalRecieved;
    @FXML private TextField txtBalanceDue;

    private ObservableList<BillPayment> paymentSplits = FXCollections.observableArrayList();

    private ObservableList<Transaction>trList = FXCollections.observableArrayList();
    private BillService billService;
    private CustomerService customerService;
    private ItemService itemService;
    private EmployeeService employeeService;
    private BankService bankService;
    private BankTransactionService bankTrService;
   // private ItemStockService itemStockService;
    //private ObservableList<String>itemNameList = FXCollections.observableArrayList();
    private SuggestionProvider<String> customerNameProvider;
    private ObservableList<String> customerNameList = FXCollections.observableArrayList();
    private CounterStockDataService counterStockDataService;
    private AlertNotification notification;
	private Login login;
	private CustomerAdvancePaymentService advanceService;
	private QuotationService quotationService;
	private long sourceQuotationId = 0;
	// private long billNo;

	@Override
 	public void initialize(URL arg0, ResourceBundle arg1) {
		billService = new BillServiceImpl();
		customerService = new CustomerServiceImpl();
		itemService = new ItemServiceImpl();
		employeeService = new EmployeeServiceImpl();
		bankService = new BankServiceImpl();
		bankTrService = new BankTransactionServiceImpl();
		//itemStockService = new ItemStockServiceImpl();
		counterStockDataService = new CounterStockDataServiceImpl();
		advanceService = new CustomerAdvancePaymentServiceImpl();
		quotationService = new QuotationServiceImpl();
		notification = new AlertNotification();
		// billNo = 0;
		date.setValue(LocalDate.now());
		txtBillNo.setText("" + billService.getNewBNillNo());
		colSrNo.setCellValueFactory(new PropertyValueFactory<Transaction, Long>("id"));
		colItemName.setCellValueFactory(new PropertyValueFactory<Transaction, String>("itemname"));
		colUnit.setCellValueFactory(new PropertyValueFactory<Transaction, String>("unit"));
		colQty.setCellValueFactory(new PropertyValueFactory<Transaction, Float>("quantity"));
		colRate.setCellValueFactory(new PropertyValueFactory<Transaction, Float>("rate"));
		colAmount.setCellValueFactory(new PropertyValueFactory<Transaction, Float>("amount"));
		table.setItems(trList);

		List<String> allCustomerNames = customerService.getAllCustomerNames();
		if (allCustomerNames != null) customerNameList.addAll(allCustomerNames);
		customerNameProvider = SuggestionProvider.create(allCustomerNames != null ? allCustomerNames : java.util.Collections.emptyList());
		new AutoCompletionTextFieldBinding<>(txtCustomerName, customerNameProvider);
		CommonData.setStockItemNames();
		// itemNameList.addAll(CommonData.itemNames);
		// TextFields.bindAutoCompletion(txtCustomerName, customerNameList);
		TextFields.bindAutoCompletion(txtItemName, CommonData.stockItemNames);

		// cmbSalesman.getItems().addAll(employeeService.getAllSalesmanNames());
		login = ViewUtil.login;
		if (login.getId() == 1) {
			List<String> salesmanNames = employeeService.getAllSalesmanNames();
			if (salesmanNames != null) cmbSalesman.getItems().addAll(salesmanNames);
		} else {
			cmbSalesman.getItems().add(login.getEmployee().getFname() + " " + login.getEmployee().getMname() + " "
					+ login.getEmployee().getLname());
			// cmbSalesman.getSelectionModel().select(1);
			cmbSalesman.setValue(login.getEmployee().getFname() + " " + login.getEmployee().getMname() + " "
					+ login.getEmployee().getLname());
		}
		cmbRecievedBy.getItems().add("By Hand");
		cmbRecievedBy.getItems().add("By Courier");
		cmbRecievedBy.getItems().add("By Vehicle");

		List<String> bankNames = bankService.getAllBankNames();
		if (bankNames != null) cmbBankName.getItems().addAll(bankNames);

		colPayBank.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
				p.getValue().getBank() != null ? p.getValue().getBank().getBankname() : ""));
		colPayRef.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(
				p.getValue().getRefNo() != null ? p.getValue().getRefNo() : ""));
		colPayAmount.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<Float>(p.getValue().getAmount()));
		tablePayments.setItems(paymentSplits);
		paymentSplits.addListener((javafx.collections.ListChangeListener<BillPayment>) c -> refreshTotalReceived());

		if (CommonData.editBillNo != 0) {
			long editId = CommonData.editBillNo;
			CommonData.editBillNo = 0;
			lblMode.setText("EDITING BILL #" + editId);
			loadBillForEdit(editId);
		} else {
			lblMode.setText("NEW BILL");
			if (CommonData.billFromQuotationId != 0) {
				long qid = CommonData.billFromQuotationId;
				CommonData.billFromQuotationId = 0;
				prefillFromQuotation(qid);
			}
		}
	}

	private void prefillFromQuotation(long quotationId) {
		Quotation q = quotationService.getQuotationById(quotationId);
		if (q == null) {
			notification.showErrorMessage("Quotation not found: " + quotationId);
			return;
		}
		if (q.isBilled()) {
			notification.showErrorMessage("Bill is already generated for this quotation");
			return;
		}
		sourceQuotationId = q.getId();
		if (q.getCustomer() != null) {
			String cname = (safeStr(q.getCustomer().getFname()) + " " + safeStr(q.getCustomer().getMname()) + " "
					+ safeStr(q.getCustomer().getLname())).replaceAll(" +", " ").trim();
			txtCustomerName.setText(cname);
			searchCustomer(null);
		}
		if (q.getEmployee() != null) {
			String ename = (safeStr(q.getEmployee().getFname()) + " " + safeStr(q.getEmployee().getMname()) + " "
					+ safeStr(q.getEmployee().getLname())).replaceAll(" +", " ").trim();
			if (cmbSalesman.getItems().contains(ename)) {
				cmbSalesman.setValue(ename);
			}
		}
		trList.clear();
		float netSum = 0f;
		if (q.getTransaction() != null) {
			int sr = 1;
			for (QuotationTransaction qt : q.getTransaction()) {
				float com = 0f;
				Item flagItem = itemService.getItemByName(qt.getItemname());
				if (flagItem != null) {
					if ("Percentage".equals(flagItem.getCommisionrate())) {
						float comrate = (flagItem.getCommision() * 100 / flagItem.getRate());
						com = (comrate / 100) * qt.getRate();
					} else {
						com = flagItem.getCommision();
					}
				}
				Transaction tr = new Transaction(qt.getItemname(), qt.getUnit(), qt.getRate(),
						qt.getQuantity(), qt.getAmount(), null, com * qt.getQuantity());
				tr.setId(sr++);
				trList.add(tr);
				netSum += qt.getAmount();
			}
		}
		txtNetTotal.setText("" + netSum);
		txtTransoChrgs.setText("" + q.getTransportingchrges());
		txtOtherChargs.setText("" + q.getOtherchargs());
		txtGrandTotal.setText("" + (netSum + q.getTransportingchrges() + q.getOtherchargs()));
	}

	private static String safeStr(String s) { return s == null ? "" : s; }

	@FXML
	void customerNameAction(ActionEvent event) {
		if (!txtCustomerName.getText().equals("") || txtCustomerName.getText() != null) {
			btnSearch.requestFocus();
		}
	}

	@FXML
	void searchCustomer(ActionEvent event) {
		try {
			if (txtCustomerName.getText().equals("") || txtCustomerName.getText() == null) {
				txtCustomerName.requestFocus();
				return;
			}
			Customer customer = customerService.getCustomerByName(txtCustomerName.getText());
			if (customer != null) {
				double advance = advanceService.getCustomerTotalAdvance(customer.getId())-billService.getWholeSaleBillAmount(customer.getId());
				txtCustomerInfo.setText(customer.getMobileno() + "\n" + customer.getAddress() + " City-"
						+ customer.getCity() + "\nTaluka-" + customer.getTaluka() + " District-"
						+ customer.getDistrict() + " Pin-" + customer.getPin()+"\n"+
						"Total Advance="+advance);
				if(advanceService.getCustomerTotalAdvance(customer.getId())!=0)
				{
					cmbBankName.setValue(bankService.getBankById(2).getBankname());
				}
				
				cmbSalesman.requestFocus();
			}
			else
			{
				notification.showErrorMessage("No Customer Found Select Again !!!");
				txtCustomerName.requestFocus();
				txtCustomerInfo.setText("");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			notification.showErrorMessage( e.getMessage());
		}
	}

	@FXML
	void searchItem(ActionEvent event) {
		if (txtItemName.getText().equals("") || txtItemName.getText() == null) {
			txtItemName.requestFocus();
			return;
		}
		if (!CommonData.stockItemNames.contains(txtItemName.getText())) {
			return;
		}
		Item item = itemService.getItemByName(txtItemName.getText());
		if (item != null) {
			txtUnit.setText(item.getUnit());
			txtRate.setText("" + item.getRate());
			txtQty.requestFocus();

		}
	}

	@FXML
	void txtQtyAction(ActionEvent event) {
		try {
			if (txtQty.getText().equals("") || txtQty.getText() == null) {
				return;
			}
			if (txtItemName.getText().equals("") || txtUnit.getText().equals("")) {
				notification.showErrorMessage("Select Item Again!!!");
				txtItemName.requestFocus();
				return;
			}
			if (!isNumber(txtRate.getText())) {
				notification.showErrorMessage("Enter Rate in Digit!!!");
				txtRate.requestFocus();
				return;
			}
			if (!isNumber(txtQty.getText())) {
				notification.showErrorMessage("Enter Quantity in Digit!!!");
				txtQty.requestFocus();
				txtQty.selectAll();
				return;
			}
			txtAmount.setText("" + (Float.parseFloat(txtRate.getText()) * Float.parseFloat(txtQty.getText())));
			btnAdd.requestFocus();
		} catch (Exception e) {
			notification.showErrorMessage("Enter Quantity in Digits!!!");
			txtQty.setText("");
			txtQty.requestFocus();
			return;
		}
	}

	@FXML
	void txtQtyKeyEvent(KeyEvent event) {
		if (txtQty.getText().equals("-")) {
			return;
		}
		if (!isNumber(txtQty.getText())) {
			txtQty.setText("");
		}
	}

	@FXML
	void btnAddAction(ActionEvent event) {
		if (txtNetTotal.getText().equals("") || txtNetTotal.getText() == null) {
			txtNetTotal.setText("" + 0.0);
		}
		if (txtAmount.getText().equals("") || txtItemName.getText().equals("") || txtUnit.getText().equals("")) {
			notification.showErrorMessage("Select Item Again");
			txtItemName.requestFocus();
			return;
		}
		Bill bill = new Bill();
		bill = billService.getBillByBillno(Long.parseLong(txtBillNo.getText()));
		if (bill == null) {
			bill = new Bill();
			bill.setBillno(Long.parseLong(txtBillNo.getText()));
		}
		// bill.setBillno(billNo);
		int index = -1;
		float com=0;//not used
		if(itemService.getItemByName(txtItemName.getText()).getLabourCharges()>0)
		{
			if (txtItemName.getText().equals("Indonesian Baahubali"))
				com =  0.75f;
			else if(txtItemName.getText().equals("Bangladesh Hape Rad Napear"))
				com=0.50f;
			else
				com = Float.parseFloat(txtRate.getText()) * 0.25f;
		}
		
		else
		{
			com = itemService.getItemByName(txtItemName.getText()).getCommision();
		}
		com=0;
		Item flagItem =itemService.getItemByName(txtItemName.getText());
		if(flagItem.getCommisionrate().equals("Percentage"))
		{
			float comrate = (flagItem.getCommision()*100/flagItem.getRate());
			com = (comrate/100)*Float.parseFloat(txtRate.getText());
		}
		else
			com = flagItem.getCommision();
		Transaction transaction = new Transaction(
				txtItemName.getText(), 
				txtUnit.getText(),
				Float.parseFloat(txtRate.getText()),
				Float.parseFloat(txtQty.getText()),
				Float.parseFloat(txtAmount.getText()), 
				bill,
                //itemService.getItemByName(txtItemName.getText()).getCommision()*Float.parseFloat(txtQty.getText())
				com*Float.parseFloat(txtQty.getText())
        );
		for (int i = 0; i < trList.size(); i++) {
			if (trList.get(i).getItemname().equals(transaction.getItemname())
					&& trList.get(i).getRate() == transaction.getRate()) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			// check Stock
//			if (transaction.getQuantity() > itemStockService.getItemStock(transaction.getItemname())) {
//				new Alert(AlertType.ERROR, "Quantity Not Available In Stock\n Please Check Stock\nAvailable Quantity="
//						+ itemStockService.getItemStock(transaction.getItemname())).showAndWait();
//				return;
//			}
			if(transaction.getQuantity()>counterStockDataService.getCounterItemStock(transaction.getItemname()))
			{
				notification.showErrorMessage("Quantity Not Available In Stock\n Please Check Stock\nAvailable Quantity="
						+ counterStockDataService.getCounterItemStock(transaction.getItemname()));
				return;
			}
			transaction.setId(trList.size() + 1);
			trList.add(transaction);
			txtNetTotal.setText("" + (Float.parseFloat(txtNetTotal.getText()) + transaction.getAmount()));
			calculateGrandTotal();

		} else {
			if (transaction.getQuantity() + trList.get(index).getQuantity() >
			counterStockDataService.getCounterItemStock(transaction.getItemname())) {
//				new Alert(AlertType.ERROR, "Quantity Not Available In Stock\n Please Check Stock\nAvailable Quantity="
//						+ itemStockService.getItemStock(transaction.getItemname())).showAndWait();
				notification.showErrorMessage("Quantity Not Available In Stock\n Please Check Stock\nAvailable Quantity="+ 
						counterStockDataService.getCounterItemStock(transaction.getItemname()));
				return;
			}
			txtNetTotal.setText("" + (Float.parseFloat(txtNetTotal.getText()) + transaction.getAmount()));
			transaction.setQuantity(transaction.getQuantity() + trList.get(index).getQuantity());
			transaction.setAmount(transaction.getQuantity() * transaction.getRate());
			//transaction.setCommision(transaction.getQuantity() * itemService.getCommision(txtItemName.getText()));
			transaction.setCommision(transaction.getQuantity() * com);
			trList.remove(index);
			transaction.setId(index + 1);
			trList.add(index, transaction);
			calculateGrandTotal();
		}
		clear();
		txtItemName.requestFocus();
	}

	@FXML
	void btnClearAction(ActionEvent event) {
		clear();
	}

	@FXML
	void btnremoveAction(ActionEvent event) {
		remove(table.getSelectionModel().getSelectedIndex());
	}

	@FXML
	void btnUpdateAction(ActionEvent event) {
		Transaction tr = table.getSelectionModel().getSelectedItem();
		if (tr != null) {
			txtItemName.setText(tr.getItemname());
			txtUnit.setText(tr.getUnit());
			txtRate.setText("" + tr.getRate());
			txtQty.setText("" + tr.getQuantity());
			txtAmount.setText("" + tr.getAmount());
		}
	}

	@FXML
	void cmbPaymentModeAction(ActionEvent event) {

	}

	@FXML
	void txtTranspChrgsAction(ActionEvent event) {
		try {
			calculateGrandTotal();
		} catch (Exception e) {
			return;
		}
	}

	@FXML
	void txtOtherChargesAction(ActionEvent event) {
		calculateGrandTotal();
	}

	@FXML
	void btnAddPaymentAction(ActionEvent event) {
		if (cmbBankName.getValue() == null) {
			notification.showErrorMessage("Select Bank/Mode for this payment!!!");
			cmbBankName.requestFocus();
			return;
		}
		if (!isNumber(txtReivedAmount.getText()) || Float.parseFloat(txtReivedAmount.getText()) <= 0) {
			notification.showErrorMessage("Enter a valid Amount for this payment!!!");
			txtReivedAmount.requestFocus();
			return;
		}
		Bank bank = bankService.getBankByName(cmbBankName.getValue());
		if (bank == null) {
			notification.showErrorMessage("Bank not found!!!");
			return;
		}
		float amount = Float.parseFloat(txtReivedAmount.getText());
		float grandTotal = isNumber(txtGrandTotal.getText()) ? Float.parseFloat(txtGrandTotal.getText()) : 0f;
		if (currentSplitsTotal() + amount > grandTotal + 0.001f) {
			notification.showErrorMessage("Sum of payments cannot exceed Grand Total!!!");
			return;
		}
		paymentSplits.add(new BillPayment(null, bank, amount, txtReffNo.getText(), date.getValue()));
		cmbBankName.getSelectionModel().clearSelection();
		txtReffNo.setText("");
		txtReivedAmount.setText("");
		cmbBankName.requestFocus();
	}

	@FXML
	void btnRemovePaymentAction(ActionEvent event) {
		BillPayment sel = tablePayments.getSelectionModel().getSelectedItem();
		if (sel != null) paymentSplits.remove(sel);
	}

	private float currentSplitsTotal() {
		float sum = 0f;
		for (BillPayment p : paymentSplits) sum += p.getAmount();
		return sum;
	}

	private void refreshTotalReceived() {
		float total = currentSplitsTotal();
		txtTotalRecieved.setText("" + total);
		float grand = isNumber(txtGrandTotal.getText()) ? Float.parseFloat(txtGrandTotal.getText()) : 0f;
		float balance = grand - total;
		if (balance > 0.001f && grand > 0.001f) {
			txtBalanceDue.setText("CREDIT " + balance);
		} else {
			txtBalanceDue.setText("" + balance);
		}
	}

	private void autoAddPendingSplit() {
		if (paymentSplits.isEmpty()
				&& cmbBankName.getValue() != null
				&& isNumber(txtReivedAmount.getText())
				&& Float.parseFloat(txtReivedAmount.getText()) > 0) {
			Bank bank = bankService.getBankByName(cmbBankName.getValue());
			if (bank != null) {
				paymentSplits.add(new BillPayment(null, bank, Float.parseFloat(txtReivedAmount.getText()),
						txtReffNo.getText(), date.getValue()));
			}
		}
	}

	private void reverseOldPayments(Bill oldBill) {
		java.util.Set<BillPayment> oldSplits = oldBill.getPayments();
		if (oldSplits != null && !oldSplits.isEmpty()) {
			for (BillPayment op : oldSplits) {
				if (op.getBank() == null || op.getAmount() <= 0) continue;
				BankTransaction bt = new BankTransaction();
				bt.setCredit(op.getAmount());
				bt.setBankid(op.getBank().getId());
				bt.setDebit(0.0f);
				bt.setParticulars("Edit Bill No " + oldBill.getBillno() + " (reverse split)");
				bt.setReffid(oldBill.getBillno());
				bt.setDate(date.getValue());
				if (bankTrService.saveBankTransaction(bt) == 1) {
					bankService.reduceBankBalance(op.getBank().getId(), op.getAmount());
				}
			}
		} else if (oldBill.getRecivedamount() > 0 && oldBill.getBank() != null) {
			BankTransaction bt = new BankTransaction();
			bt.setCredit(oldBill.getRecivedamount());
			bt.setBankid(oldBill.getBank().getId());
			bt.setDebit(0.0f);
			bt.setParticulars("Edit Bill No " + oldBill.getBillno());
			bt.setReffid(oldBill.getBillno());
			bt.setDate(date.getValue());
			if (bankTrService.saveBankTransaction(bt) == 1) {
				bankService.reduceBankBalance(oldBill.getBank().getId(), oldBill.getRecivedamount());
			}
		}
	}

	private void applyNewPayments(Bill bill) {
		if (bill.getPayments() == null) return;
		for (BillPayment p : bill.getPayments()) {
			if (p.getBank() == null || p.getAmount() <= 0) continue;
			BankTransaction bt = new BankTransaction(
					"Bill " + bill.getBillno() + " " + p.getBank().getBankname()
							+ (p.getRefNo() != null && !p.getRefNo().isEmpty() ? " Ref:" + p.getRefNo() : ""),
					bill.getBillno(), 0.0f, p.getAmount(), p.getBank().getId(), date.getValue());
			if (bankTrService.saveBankTransaction(bt) == 1) {
				bankService.addBankBalance(p.getBank().getId(), p.getAmount());
			}
		}
	}

	@FXML
	void btnSaveAction(ActionEvent event) {
		autoAddPendingSplit();
		if (validateData() != 1) {
			return;
		}
		float totalReceived = currentSplitsTotal();
		Bank primaryBank = paymentSplits.isEmpty() ? null : paymentSplits.get(0).getBank();
		String primaryRef = paymentSplits.isEmpty() ? "" : paymentSplits.get(0).getRefNo();

		Bill bill = new Bill(customerService.getCustomerByName(txtCustomerName.getText()), date.getValue(),
				Float.parseFloat(txtNetTotal.getText()), Float.parseFloat(txtTransoChrgs.getText()),
				Float.parseFloat(txtOtherChargs.getText()), primaryBank,
				cmbRecievedBy.getValue(), primaryRef,
				employeeService.getEmployeeByName(cmbSalesman.getValue()), null,
				totalReceived, 0.0f);
		bill.setBillno(Long.parseLong(txtBillNo.getText()));
		for (Transaction tr : trList) {
			tr.setBill(bill);
			tr.setId(0);
		}
		bill.setTransaction(trList);

		java.util.Set<BillPayment> splitsCopy = new java.util.LinkedHashSet<>();
		for (BillPayment p : paymentSplits) {
			splitsCopy.add(new BillPayment(bill, p.getBank(), p.getAmount(), p.getRefNo(), date.getValue()));
		}
		bill.setPayments(splitsCopy);

		Bill oldBill = billService.getBillByBillno(bill.getBillno());
		if (oldBill != null) {
			for (Transaction tr : oldBill.getTransaction()) {
				counterStockDataService.saveCounterStockdata(new CounterStockData(tr.getItemname(),tr.getQuantity(),tr.getUnit()));
			}
			reverseOldPayments(oldBill);
		}

		int flag = billService.saveBill(bill);
		if (flag == 1 || flag == 2) {
			applyNewPayments(bill);
			reduceStock(bill.getTransaction());
			if (sourceQuotationId != 0) {
				quotationService.markBilled(sourceQuotationId);
				sourceQuotationId = 0;
			}
			showPrintBillConfirmation(bill.getBillno());
			showPrintCouriorConfirmation(bill);

			if (flag == 1) {
				notification.showSuccessMessage("Bill saved Success");
			} else {
				notification.showSuccessMessage("Bill Update Success");
			}
			lblMode.setText("NEW BILL");
			new GetBackup("D:\\Software\\Backup\\");
			clearBill();
		}
	}

	private void reduceStock(List<Transaction> list) {
		try {
			for(Transaction tr:list)
			{
				float qty=tr.getQuantity();
				qty*=-1;
				counterStockDataService.saveCounterStockdata(new CounterStockData(tr.getItemname(),qty,tr.getUnit()));
			}
//			ItemStock stock;
//			for (Transaction tr : list) {
//				stock = itemStockService.getItemStockByItemName(tr.getItemname());
//				stock.setQuantity(tr.getQuantity() - (tr.getQuantity() * 2));
//				itemStockService.saveItemStock(stock);

//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void btnClearBillAction(ActionEvent event) {
		clearBill();
	}

	@FXML
	void btnExitAction(ActionEvent event) {
		if (mainPanel.getParent() instanceof BorderPane) {
			BorderPane homePane = (BorderPane) mainPanel.getParent();
			Pane dashboard = new ViewUtil().getPage("transaction/BillsDashboard");
			if (dashboard != null) {
				homePane.setCenter(dashboard);
				return;
			}
		}
		mainPanel.setVisible(false);
	}

	@FXML
	void btnNewAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(Main.class.getResource("/view/create/AddCustomerFrame.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("My modal window");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				customerNameList.clear();
				customerNameList.addAll(customerService.getAllCustomerNames());
				customerNameProvider.clearSuggestions();
				customerNameProvider.addPossibleSuggestions(customerNameList);
			}
		});

	}

	@FXML
	void btnPrintAction(ActionEvent event) {
		String billNoText = txtBillNo.getText();
		if (billNoText == null || billNoText.isEmpty()) return;
		try {
			long billNo = Long.parseLong(billNoText);
			if (billService.getBillByBillno(billNo) != null) {
				new GenerateBill(billNo);
				new PrintFile().openFile("D:\\Software\\Prints\\bill.pdf");
			} else {
				notification.showErrorMessage("Save the bill before printing");
			}
		} catch (NumberFormatException e) {
			notification.showErrorMessage("Invalid bill number");
		}
	}

	private void loadBillForEdit(long billNo) {
		Bill bill = billService.getBillByBillno(billNo);
		if (bill == null) {
			notification.showErrorMessage("Bill not found: " + billNo);
			lblMode.setText("NEW BILL");
			return;
		}
		if (login.getId() != 1) {
			if (bill.getEmployee() == null || bill.getEmployee().getId() != login.getEmployee().getId()) {
				notification.showErrorMessage("You are not Authorized to Edit This Bill !!!");
				lblMode.setText("NEW BILL");
				return;
			}
		}
		txtBillNo.setText("" + bill.getBillno());
		txtCustomerName.setText(bill.getCustomer().getFname() + " " + bill.getCustomer().getMname() + " "
				+ bill.getCustomer().getLname());
		btnSearch.fire();
		cmbSalesman.setValue(bill.getEmployee().getFname() + " " + bill.getEmployee().getMname() + " "
				+ bill.getEmployee().getLname());
		trList.clear();
		trList.addAll(bill.getTransaction());
		txtNetTotal.setText("" + bill.getNettotal());
		txtTransoChrgs.setText("" + bill.getTransportingchrges());
		txtOtherChargs.setText("" + bill.getOtherchargs());
		txtGrandTotal.setText("" + (bill.getNettotal() + bill.getOtherchargs() + bill.getTransportingchrges()));
		cmbRecievedBy.setValue(bill.getRecievedby());
		txtReffNo.setText("");
		cmbBankName.getSelectionModel().clearSelection();
		txtReivedAmount.setText("");
		paymentSplits.clear();
		if (bill.getPayments() != null && !bill.getPayments().isEmpty()) {
			for (BillPayment p : bill.getPayments()) {
				paymentSplits.add(new BillPayment(null, p.getBank(), p.getAmount(), p.getRefNo(), p.getDate()));
			}
		} else if (bill.getRecivedamount() > 0 && bill.getBank() != null) {
			paymentSplits.add(new BillPayment(null, bill.getBank(), bill.getRecivedamount(),
					bill.getRecievedreff(), bill.getDate()));
		}
		refreshTotalReceived();
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

	private void calculateGrandTotal() {
		try {
			if (!isNumber(txtTransoChrgs.getText())) {
				txtTransoChrgs.setText("" + 0.0);
			}
			if (!isNumber(txtOtherChargs.getText())) {
				txtOtherChargs.setText("" + 0.0);
			}
			txtGrandTotal.setText("" + (Float.parseFloat(txtNetTotal.getText())
					+ Float.parseFloat(txtTransoChrgs.getText()) + Float.parseFloat(txtOtherChargs.getText())));
		} catch (Exception e) {
			notification.showErrorMessage("Error" + e.getMessage());
		}
	}

	private void remove(int selected) {
		Transaction tr = trList.get(selected);
		if (tr != null) {
			int index = -1;
			for (int i = 0; i < trList.size(); i++) {
				if (trList.get(i).getItemname().equals(tr.getItemname()) && trList.get(i).getRate() == tr.getRate()) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				txtNetTotal.setText("" + (Float.parseFloat(txtNetTotal.getText()) - tr.getAmount()));
				trList.remove(index);
				int sr = index;
				for (int i = index; i < trList.size(); i++) {
					trList.get(i).setId(++sr);
				}
			}

		}
	}

	private void clear() {
		txtItemName.setText("");
		txtUnit.setText("");
		txtRate.setText("");
		txtAmount.setText("");
		txtQty.setText("");
	}

	private int validateData() {
		try {
			if (date.getValue() == null) {
				notification.showErrorMessage("Select Billing Date!!!");
				date.requestFocus();
				return 0;
			}
			if(customerService.getCustomerByName(txtCustomerName.getText())==null)
			{
				notification.showErrorMessage("Select Customer!!!");
				txtCustomerName.requestFocus();
				return 0;
			}
			if (txtCustomerInfo.getText().equals("")) {
				notification.showErrorMessage("Select Customer!!!");
				txtCustomerName.requestFocus();
				return 0;
			}
			if (cmbSalesman.getValue() == null) {
				notification.showErrorMessage("Select Salesman!!!");
				cmbSalesman.requestFocus();
				return 0;
			}
			if (trList.size() == 0) {
				notification.showErrorMessage("No Data to save add items!!!");
				txtItemName.requestFocus();
				return 0;
			}
			if (txtGrandTotal.getText().equals("" + 0.0)) {
				notification.showErrorMessage("No Data to save add items!!!");
				txtItemName.requestFocus();
				return 0;
			}
			if (cmbRecievedBy.getValue() == null) {
				notification.showErrorMessage("select Recived By!!!");
				cmbRecievedBy.requestFocus();
				return 0;
			}
			// Credit bill: zero payments is allowed. The user will collect later via Payment Received.
			if (currentSplitsTotal() > Float.parseFloat(txtGrandTotal.getText()) + 0.001f) {
				notification.showErrorMessage("Total Received cannot exceed Grand Total!!!");
				return 0;
			}

			return 1;

		} catch (Exception e) {
			notification.showErrorMessage("Error");
			e.printStackTrace();
			return 0;
		}
	}

	private void clearBill() {
		lblMode.setText("NEW BILL");
		txtBillNo.setText("" + billService.getNewBNillNo());
		date.setValue(LocalDate.now());
		txtCustomerName.setText("");
		txtCustomerInfo.setText("");
		cmbSalesman.getSelectionModel().clearSelection();
		txtItemName.setText("");
		txtQty.setText("");
		txtUnit.setText("");
		txtRate.setText("");
		txtAmount.setText("");
		trList.clear();
		txtNetTotal.setText("" + 0.0f);
		txtTransoChrgs.setText("" + 0.0f);
		txtOtherChargs.setText("" + 0.0f);
		txtGrandTotal.setText("" + 0.0f);
		txtReffNo.setText("");
		txtReivedAmount.setText("");
		cmbRecievedBy.getSelectionModel().clearSelection();
		cmbBankName.getSelectionModel().clearSelection();
		paymentSplits.clear();
		txtTotalRecieved.setText("0.0");
		txtBalanceDue.setText("0.0");
		sourceQuotationId = 0;
	}

	private void showPrintBillConfirmation(long billno) {
		Stage stage = (Stage) mainPanel.getScene().getWindow();
		Alert.AlertType type = Alert.AlertType.CONFIRMATION;
		Alert alert = new Alert(type, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText("Do You Want Print Bill");
		alert.getDialogPane().setHeaderText("Confirmation");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// new BillPrint(billno);
			try {
				new GenerateBill(billno);
				new PrintFile().openFile("D:\\Software\\Prints\\bill.pdf");
			} catch (Exception e) {
				notification.showErrorMessage( e.getMessage());
			}
		} else if (result.get() == ButtonType.CANCEL) {

		}
	}

	private void showPrintCouriorConfirmation(Bill bill) {
		Stage stage = (Stage) mainPanel.getScene().getWindow();
		Alert.AlertType type = Alert.AlertType.CONFIRMATION;
		Alert alert = new Alert(type, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText("Do You Want Print Courior Sticker?");
		alert.getDialogPane().setHeaderText("Confirmation");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			new CouriorReceipt(bill);
			new PrintFile("D:\\Software\\Prints\\courior.pdf");
		} else if (result.get() == ButtonType.CANCEL) {
			return;
		}
	}

}
