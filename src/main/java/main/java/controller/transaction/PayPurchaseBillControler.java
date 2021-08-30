package main.java.main.java.controller.transaction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.service.PurchasePartyService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchasePartyServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PayPurchaseBillControler implements Initializable {
	@FXML private AnchorPane mainPane;
	@FXML private ComboBox<String> cmbPartyName;
	@FXML private Button btnShow;
	@FXML private Button btnPreview;
	@FXML private TableView<PurchaseInvoice> table;
	@FXML private TableColumn<PurchaseInvoice,String> colSrNo;
	@FXML private TableColumn<PurchaseInvoice,LocalDate> colDate;
	@FXML private TableColumn<PurchaseInvoice,Long> colBillNo;
	@FXML private TableColumn<PurchaseInvoice,String> colInvoiceNo;
	@FXML private TableColumn<PurchaseInvoice,Float> colAmount;//grandtotal
	@FXML private TableColumn<PurchaseInvoice,Float> colPaid;
	@FXML private TableColumn<PurchaseInvoice,Float> colRemaining;//otherchargs
	@FXML private TableColumn<PurchaseInvoice,Float> colTodayPaid;
	@FXML private TextField txtBillAmount;  
	@FXML private TextField txtPaid;   
	@FXML private TextField txtToday;
	@FXML private TextField txtRemaining;
	@FXML private TextField txtTotalRemaining;

	@FXML private ComboBox<String> cmbBank;
	@FXML private Button btnCalculate;
	@FXML private Button btnPay;
	@FXML private Button btnReset;
	@FXML private Button btnClose;
	private ObservableList<PurchaseInvoice> purchaseList = FXCollections.observableArrayList();
	private PurchaseInvoiceService purchaseSevice;
	private PurchasePartyService partyService;
	private BankService bankService;
	private BankTransactionService bankTrService;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		purchaseSevice = new PurchaseInvoiceServiceImpl();
		partyService = new PurchasePartyServiceImpl();
		bankService = new BankServiceImpl();
		bankTrService = new BankTransactionServiceImpl();
		cmbPartyName.getItems().addAll(partyService.getAllPurchasePartyNames());
		cmbBank.getItems().addAll(bankService.getAllBankNames());
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("bankreffno"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
		colInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("grandtotal"));
		colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
		colRemaining.setCellValueFactory(new PropertyValueFactory<>("othercharges"));
		colTodayPaid.setCellValueFactory(new PropertyValueFactory<>("transportcharges"));
		table.setItems(purchaseList);
	}
	@FXML
	void btnShowAction(ActionEvent event) {
		if(cmbPartyName.getValue()==null)
		{
			new Alert(AlertType.ERROR,"Select Party Name!!!").showAndWait();
			cmbPartyName.requestFocus();
			return;
		}
		purchaseList.addAll(purchaseSevice.getPartyWiseUnpaidPurchaseInvoice(partyService.getPurchasePartyByName(cmbPartyName.getValue()).getId()));
		int srno=0;
		float total=0,paid=0;
		for(int i=0;i<purchaseList.size();i++)
		{
			purchaseList.get(i).setBankreffno(""+(++srno));;
			purchaseList.get(i).setOthercharges(purchaseList.get(i).getGrandtotal()-purchaseList.get(i).getPaid());
			total = total+purchaseList.get(i).getGrandtotal();
			paid = paid+purchaseList.get(i).getPaid();
		}
		txtBillAmount.setText(""+total);
		txtPaid.setText(""+paid);
		txtTotalRemaining.setText(""+(total-paid));
		

	}

	@FXML
	void btnCalculateAction(ActionEvent event) {
		if(purchaseList.size()==0)
		{
			new Alert(AlertType.ERROR,"No Data To Calculate!!!").showAndWait();
			return;
		}
		if(txtTotalRemaining.getText().equals("")||Float.parseFloat(txtTotalRemaining.getText())<=0)
		{
			new Alert(AlertType.ERROR,"No Amount Remaining To Save!!!").showAndWait();
			return;
		}
		if(txtToday.getText().equals("")||Float.parseFloat(txtToday.getText())<=0)
		{
			new Alert(AlertType.ERROR,"Todays Amount must be greater than 0 !!!").showAndWait();
			txtToday.requestFocus();
			return;
		}
		PurchaseInvoice invoice=null;
		float today = Float.parseFloat(txtToday.getText());
		List<PurchaseInvoice> list = purchaseList;
		for(int i=0;i<list.size();i++)
		{
			if(today>0)
			{
				if(today>list.get(i).getOthercharges())
				{
					
					list.get(i).setTransportcharges(list.get(i).getOthercharges());
					today=today-list.get(i).getOthercharges();
					invoice=null;
					invoice=list.get(i);
					purchaseList.set(i, invoice);
				}
				else
				{
					list.get(i).setTransportcharges(today);
					today=0;
					invoice=null;
					invoice=list.get(i);
					purchaseList.set(i, invoice);
				}	
			}
		}
		txtRemaining.setText(""+(
				Float.parseFloat(txtTotalRemaining.getText())-
				Float.parseFloat(txtToday.getText())));
	}

	@FXML
	void btnCloseAction(ActionEvent event) {
		mainPane.setVisible(false);
	}

	@FXML
	void btnPayAction(ActionEvent event) {
		if(txtRemaining.getText().equals(""))
		{
			btnCalculate.fire();
		}
		if(cmbBank.getValue()==null)
		{
			new Alert(AlertType.ERROR,"Select Bank Name!!!").showAndWait();
			cmbBank.requestFocus();
			return;
		}
		BankTransaction btr=null;
		for(PurchaseInvoice invoice:purchaseList)
		{
			if(purchaseSevice.updatePaidAmount(invoice.getBillno(), invoice.getTransportcharges())==1)
			{
				btr = new BankTransaction();
				btr.setBankid(bankService.getBankByName(cmbBank.getValue()).getId());
				btr.setCredit(invoice.getTransportcharges());
				btr.setDebit(0);
				btr.setDate(LocalDate.now());
				btr.setParticulars("Reduce Invoice Amount BillNo "+invoice.getBillno());
				btr.setReffid(invoice.getBillno());
				bankTrService.saveBankTransaction(btr);				
			}
		}
		bankService.reduceBankBalance(bankService.getBankByName(cmbBank.getValue()).getId(), Float.parseFloat(txtToday.getText()));
		new Alert(AlertType.INFORMATION,"Record Save Success").showAndWait();
		btnReset.fire();
	}

	@FXML
	void btnPreview(ActionEvent event) {
		if(table.getSelectionModel().getSelectedItem()==null)
		{
			return;
		}
		PurchaseInvoice invoice = table.getSelectionModel().getSelectedItem();
		if(invoice==null)
		{
			return;
		}
		CommonData.previewInvoiceno = invoice.getBillno();
		new ViewUtil().showInvoicePreview(event);
		//table.getSelectionModel().clearSelection();
	}

	@FXML
	void btnResetAction(ActionEvent event) {
		txtBillAmount.setText("");
		txtPaid.setText("");
		txtRemaining.setText("");
		txtToday.setText("");
		txtTotalRemaining.setText("");
		cmbPartyName.getSelectionModel().clearSelection();
		cmbBank.getSelectionModel().clearSelection();
		purchaseList.clear();
	}

	
}
