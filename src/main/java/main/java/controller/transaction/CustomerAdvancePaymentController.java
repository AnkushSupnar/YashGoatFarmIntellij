package main.java.main.java.controller.transaction;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.entities.CustomerAdvancePayment;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.CustomerAdvancePaymentService;
import main.java.main.java.hibernate.service.service.CustomerService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CustomerAdvancePaymentServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CustomerServiceImpl;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CustomerAdvancePaymentController implements Initializable {

	@FXML private DatePicker date;
 	@FXML private TextField txtCustomer;
 	@FXML private ComboBox<String> cmbBankName;
	@FXML private TextField txtAmount;
	@FXML private TextField txtReff;
	@FXML private Button btnSave;
 	@FXML private Button btnClear;
	@FXML private Button btnUpdate;
	@FXML private Button btnExit;
	@FXML private TableView<CustomerAdvancePayment> table;
	@FXML private TableColumn<CustomerAdvancePayment,Long> colSrNo;
	@FXML private TableColumn<CustomerAdvancePayment,LocalDate> colDate;
	@FXML private TableColumn<CustomerAdvancePayment,String> colCustomer;
	@FXML private TableColumn<CustomerAdvancePayment,Float> colAmount;
	@FXML private TableColumn<CustomerAdvancePayment,String> colBankName;
	@FXML private TableColumn<CustomerAdvancePayment,String> colReff;

	private CustomerService customerService;
	private CustomerAdvancePaymentService paymentService;
	private AlertNotification notify;
	private BankService bankService;
	private BankTransactionService bankTrService;
	private ObservableList<CustomerAdvancePayment> list = FXCollections.observableArrayList();
	private long id;
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		id=0;
		customerService = new CustomerServiceImpl();
		paymentService = new CustomerAdvancePaymentServiceImpl();
		notify = new AlertNotification();
		bankService = new BankServiceImpl();
		bankTrService = new BankTransactionServiceImpl();
		TextFields.bindAutoCompletion(txtCustomer,customerService.getAllCustomerNames());
		cmbBankName.getItems().addAll(bankService.getAllBankNames());
		date.setValue(LocalDate.now());
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colCustomer.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getCustomer().getFname()+" "+cellData.getValue().getCustomer().getMname()+" "+cellData.getValue().getCustomer().getLname())); 
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		colBankName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBank().getBankname()));
		colReff.setCellValueFactory(new PropertyValueFactory<>("refference"));
		list.addAll(paymentService.getCustomerAdvanceByDate(LocalDate.now()));
		table.setItems(list);
		btnSave.setOnAction(e->save());
		btnClear.setOnAction(e->clear());
		btnUpdate.setOnAction(e->update());
		
	}	
	private void update() {
		try {
			if(table.getSelectionModel().getSelectedItem()==null)
			{
				return;
			}
			CustomerAdvancePayment ap = table.getSelectionModel().getSelectedItem();
			if(ap==null)
				return;
			date.setValue(ap.getDate());
			txtCustomer.setText(ap.getCustomer().getFname()+" "+ap.getCustomer().getMname()+" "+ap.getCustomer().getLname());
			cmbBankName.setValue(ap.getBank().getBankname());
			txtAmount.setText(""+ap.getAmount());
			txtReff.setText(ap.getRefference());
			id=ap.getId();
						
		} catch (Exception e) {
			notify.showErrorMessage("Error in update"+e.getMessage());
		}
	}
	private void clear() {
		date.setValue(LocalDate.now());
		txtAmount.setText("");
		txtCustomer.setText("");
		txtReff.setText("");
		cmbBankName.getSelectionModel().clearSelection();
		id=0;
		
	}
	private void save() {
		try {
			if(!validate())
			{
				return;
			}
			CustomerAdvancePayment oldPay = null;		
			CustomerAdvancePayment payment = new CustomerAdvancePayment(
					customerService.getCustomerByName(txtCustomer.getText()),
					date.getValue(),
					bankService.getBankByName(cmbBankName.getSelectionModel().getSelectedItem()),
					Float.parseFloat(txtAmount.getText()),
					txtReff.getText());
			if(id!=0) {
				oldPay= paymentService.getCustomerAdvanceById(id);
			payment.setId(id);
			}
			int flag = paymentService.saveCustomerAdvance(payment);
			if(flag==1)
			{
				//add money in bqnk
				addPaymentInBank(payment);				
				clear();
				list.add(payment);
				addInList(payment);
				notify.showSuccessMessage("Record save Success");
			}
			else
			{
				reducePaymentFromBank(oldPay);
				addPaymentInBank(payment);;				
				addInList(payment);
				clear();
				notify.showSuccessMessage("Record Update Success");			
			}			
		} catch (Exception e) {
			notify.showErrorMessage("Error in saving data "+e.getMessage());
			e.printStackTrace();
		}
	}
	private boolean validate() {
		if(date.getValue()==null)
		{
			notify.showErrorMessage("Enter Date");
			date.requestFocus();
			return false;
		}
		if(txtCustomer.getText().isEmpty())
		{
			notify.showErrorMessage("Enter Customer Name");
			txtCustomer.requestFocus();
			return false;
		}
		if(customerService.getCustomerByName(txtCustomer.getText().trim())==null)
		{
			notify.showErrorMessage("Enter Correct Customer Name\n Customer not found!");
			txtCustomer.requestFocus();
			return false;
		}
		if(cmbBankName.getSelectionModel().getSelectedItem()==null)
		{
			notify.showErrorMessage("Select Bank Name");
			cmbBankName.requestFocus();
			return false;
		}
		if(txtAmount.getText().isEmpty())
		{
			notify.showErrorMessage("Enter Amount");
			txtAmount.requestFocus();
			return false;
		}
		if(!isNumber(txtAmount.getText()))
		{
			notify.showErrorMessage("Enter Amount in Digit");
			txtAmount.selectAll();
			txtAmount.requestFocus();
			return false;
		}
		if(txtReff.getText().isEmpty())
		{
			notify.showErrorMessage("Enter Refference");
			txtReff.requestFocus();
			return false;
		}
		return true;
	}
	@FXML
	void txtAmountKeyRelease(KeyEvent event) {
		if (!isNumber(txtAmount.getText())) {
			txtAmount.setText("");
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

	private void addInList(CustomerAdvancePayment payment) {
		int flag=-1;
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).getId()==payment.getId())
			{
				flag=i;
				break;
			}
		}		
		if(flag==-1)
			list.add(payment);
		else
		{
			list.remove(flag);
			list.add(flag,payment);
		}
		table.refresh();
	}
	private void addPaymentInBank(CustomerAdvancePayment payment)
	{
		BankTransaction bankTr = new BankTransaction(
				"Advance Payment from Customer "+payment.getId(), 
				payment.getId(),
				payment.getAmount(), 
				payment.getBank().getId(),
				0,
				payment.getDate());
		bankService.addBankBalance(payment.getBank().getId(),payment.getAmount());
		bankTrService.saveBankTransaction(bankTr);
	}
	private void reducePaymentFromBank(CustomerAdvancePayment payment)
	{
		BankTransaction bankTr = new BankTransaction(
				"Edit Advance Payment from Customer"+payment.getId(), 
				payment.getId(),
				0,
				payment.getAmount(),
				payment.getBank().getId(),
				payment.getDate());
		bankService.reduceBankBalance(payment.getBank().getId(),payment.getAmount());
		bankTrService.saveBankTransaction(bankTr);
	}


}
