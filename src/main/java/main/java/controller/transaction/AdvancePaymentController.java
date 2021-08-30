package main.java.main.java.controller.transaction;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import  main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.AdvancePayment;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.service.service.AdvancePaymentService;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.PurchasePartyService;
import main.java.main.java.hibernate.service.serviceImpl.AdvancePaymentServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchasePartyServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdvancePaymentController implements Initializable {

		@FXML private AnchorPane mainPane;

	  	@FXML private DatePicker date;
	    @FXML private ComboBox<String> cmbPartyName;
 	    @FXML private ComboBox<String> cmbBankName;
	    @FXML private TextField txtAmount;
	    @FXML private TextField txtReff;
 	    @FXML private Button btnSave;
	    @FXML private Button btnClear;
	    @FXML private Button btnUpdate;
	    @FXML private Button btnExit;
	    
	    @FXML private TableView<AdvancePayment> table;
	    @FXML private TableColumn<AdvancePayment,Long> colSrNo;
	    @FXML private TableColumn<AdvancePayment,LocalDate> colDate;
	    @FXML private TableColumn<AdvancePayment,String> colPartyName;
	    @FXML private TableColumn<AdvancePayment,Float> colAmount;
	    @FXML private TableColumn<AdvancePayment,String> colBankName;
	    @FXML private TableColumn<AdvancePayment,String> colReff;

	    private AdvancePaymentService service;
	    private BankService bankService;
	    private PurchasePartyService partyService;
	    private BankTransactionService bankTrService;
	    private ObservableList<AdvancePayment>paymentList = FXCollections.observableArrayList();
	    long id;
	    private AlertNotification notify;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		service = new AdvancePaymentServiceImpl();
		bankService = new BankServiceImpl();
		partyService = new PurchasePartyServiceImpl();
		bankTrService = new BankTransactionServiceImpl();
		notify = new AlertNotification();
		id=0;
		date.setValue(LocalDate.now());
		cmbPartyName.getItems().addAll(partyService.getAllPurchasePartyNames());
		cmbBankName.getItems().addAll(bankService.getAllBankNames());
        
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colPartyName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getParty().getName()));
//colBank.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBank().getBankname()));
		colBankName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBank().getBankname()));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));		
		colReff.setCellValueFactory(new PropertyValueFactory<>("refference"));
		paymentList.addAll(service.getAllAdvancePayment());
		table.setItems(paymentList);
		
		btnSave.setOnAction(e->save());
		btnClear.setOnAction(e->clear());
		btnUpdate.setOnAction(e->update());
		btnExit.setOnAction(e->mainPane.setVisible(false));
	}
	 private void update() {
		if(table.getSelectionModel().getSelectedItem()==null)
			return;
		AdvancePayment payment = table.getSelectionModel().getSelectedItem();
		date.setValue(payment.getDate());
		cmbPartyName.setValue(payment.getParty().getName());
		cmbBankName.setValue(payment.getBank().getBankname());
		txtAmount.setText(""+payment.getAmount());
		txtReff.setText(payment.getRefference());
		id=payment.getId();
	}
	@FXML
	    void txtAmountKeyRelease(KeyEvent event) {		
			if (!isNumber(txtAmount.getText())) {
				txtAmount.setText("");
			}
	    }
	private void save() {
		if(validateData()!=1)
			return;
		AdvancePayment pay = new AdvancePayment();
		pay.setAmount(Float.parseFloat(txtAmount.getText()));
		pay.setParty(partyService.getPurchasePartyByName(cmbPartyName.getValue()));
		AdvancePayment payment = new AdvancePayment(
				partyService.getPurchasePartyByName(cmbPartyName.getSelectionModel().getSelectedItem()),
				date.getValue(),
				bankService.getBankByName(cmbBankName.getSelectionModel().getSelectedItem()),
				Float.parseFloat(txtAmount.getText()),
				txtReff.getText());
		AdvancePayment oldPayment = null;
		if(id!=0)
		{
			oldPayment = service.getAdvancePaymentById(id);
			payment.setId(id);
		}
		int flag = service.saveAdvancePayment(payment);
		if(flag==1)
		{
			clear();
			addInList(payment);
			reducePaymentFromBank(payment);
			notify.showSuccessMessage("Record save Success");			
		}
		else {
			clear();
			addInList(payment);
			addPaymentInBank(oldPayment);
			reducePaymentFromBank(payment);
			notify.showSuccessMessage("Record Update Success");
			
		}
	}
	private void addInList(AdvancePayment payment) {
		int flag=-1;
		for(int i=0;i<paymentList.size();i++)
		{
			if(paymentList.get(i).getId()==payment.getId())
			{
				flag=i;
				break;
			}
		}		
		if(flag==-1)
			paymentList.add(payment);
		else
		{
			paymentList.remove(flag);
			paymentList.add(flag,payment);
		}
		table.refresh();
	}
	private void clear() {
		date.setValue(LocalDate.now());
		cmbPartyName.getSelectionModel().clearSelection();
		cmbBankName.getSelectionModel().clearSelection();
		txtAmount.setText("");
		txtReff.setText("");
		id=0;
		
	}
	private int validateData()
	{
		if(date.getValue()==null)
		{
			notify.showErrorMessage("Select Date");
			date.requestFocus();
			return 0;
		}
		if(cmbPartyName.getSelectionModel().getSelectedItem()==null)
		{
			notify.showErrorMessage("Select Party Name");
			cmbPartyName.requestFocus();
			return 0;
		}
		if(cmbBankName.getSelectionModel().getSelectedItem()==null)
		{
			notify.showErrorMessage("Select Bank Name");
			cmbBankName.requestFocus();
			return 0;
		}
		if(txtAmount.getText().isEmpty())
		{
			notify.showErrorMessage("Enter Amount in Digit");
			txtAmount.requestFocus();
			return 0;
		}
		if(!isNumber(txtAmount.getText()))
		{
			notify.showErrorMessage("Enter Amount in Digit");
			txtAmount.requestFocus();
			return 0;
		}
		if(txtReff.getText().isEmpty())
		{
			txtReff.setText("-");
		}
		
		
		
		return 1;
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

	private void addPaymentInBank(AdvancePayment payment)
	{
		BankTransaction bankTr = new BankTransaction(
				"Edit Advance Payment id "+payment.getId(), 
				payment.getId(),
				payment.getAmount(), 
				0,
				payment.getBank().getId(),
				payment.getDate());
		bankService.addBankBalance(payment.getBank().getId(),payment.getAmount());
		bankTrService.saveBankTransaction(bankTr);
	}
	private void reducePaymentFromBank(AdvancePayment payment)
	{
		BankTransaction bankTr = new BankTransaction(
				"Advance Payment id "+payment.getId(), 
				payment.getId(),
				0, 
				payment.getAmount(),
				payment.getBank().getId(),
				payment.getDate());
		bankService.reduceBankBalance(payment.getBank().getId(),payment.getAmount());
		bankTrService.saveBankTransaction(bankTr);
	}
}
