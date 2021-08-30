package main.java.main.java.controller.transaction;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.entities.BankTransfer;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.BankTransferService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransferServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BankMoneyTransferController implements Initializable {

	 	@FXML private AnchorPane mainPane;
	 	@FXML private DatePicker date;
	    @FXML private ComboBox<String> cmbFromBank;
	    @FXML private ComboBox<String> cmbToBank;
	    @FXML private TextField txtAmount;
	    @FXML private Button btnTransafer;
	    @FXML private Button btnClear;
	    @FXML private TableView<BankTransfer> table;
	    @FXML private TableColumn<BankTransfer,Integer> coSrNo;
	    @FXML private TableColumn<BankTransfer,LocalDate> colDate;
	    @FXML private TableColumn<BankTransfer,String> colFrom;
	    @FXML private TableColumn<BankTransfer,String> colTo;
	    @FXML private TableColumn<BankTransfer,Float> colAmount;
	    @FXML private Label lblBalance;

	    private BankTransferService service;
	    private BankService bankService;
	    private BankTransactionService bankTrService;
	    private AlertNotification notify;
	    private ObservableList<BankTransfer>list = FXCollections.observableArrayList();
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		service = new BankTransferServiceImpl();
		bankService = new BankServiceImpl();
		bankTrService = new BankTransactionServiceImpl();
		notify= new AlertNotification();
		lblBalance.setText("");
		date.setValue(LocalDate.now());
		cmbFromBank.getItems().addAll(bankService.getAllBankNames());
		cmbToBank.getItems().addAll(bankService.getAllBankNames());
		        
		coSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colFrom.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getFromBank().getBankname()));
		colTo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getToBank().getBankname()));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		list.addAll(service.getBankTransferByDate(LocalDate.now()));
		table.setItems(list);
		
		btnClear.setOnAction(e->clear());
		btnTransafer.setOnAction(e->transfer());
		cmbFromBank.setOnAction(e->{
			if(cmbFromBank.getValue()!=null)
			{
				//notify.showSuccessMessage(cmbFromBank.getValue());
				lblBalance.setText(""+
				bankService.getBankByName(cmbFromBank.getValue()).getBalance()
						);
			}
		});

	}

	private void transfer() {
		try {
			if(!validate())
			{
				return;
			}
			BankTransfer transfer = new BankTransfer(date.getValue(), bankService.getBankByName(cmbFromBank.getSelectionModel().getSelectedItem()), bankService.getBankByName(cmbToBank.getSelectionModel().getSelectedItem()), Float.parseFloat(txtAmount.getText()));
			int flag = service.saveBankTransfer(transfer);
			if(flag==1)
			{
				//1 add money in bank
				//Transfer to bankTransaction
				bankTrService.saveBankTransaction(
						new BankTransaction(
								"Transafer To bank id "+transfer.getId(), 
								transfer.getId(),
								transfer.getAmount(),
								0,
								transfer.getToBank().getId(), 
								transfer.getDate()));
				//2 transfer from bank
				bankTrService.saveBankTransaction(
						new BankTransaction(
								"Transafer from bank id "+transfer.getId(), 
								transfer.getId(),
								0,
								transfer.getAmount(),
								transfer.getFromBank().getId(), 
								transfer.getDate()));
				bankService.addBankBalance(transfer.getToBank().getId(), transfer.getAmount());
				bankService.reduceBankBalance(transfer.getFromBank().getId(), transfer.getAmount());
				list.add(transfer);
				clear();
				notify.showSuccessMessage("Bank Transfer Success");
			}
		} catch (Exception e) {
			notify.showErrorMessage("Errro in transafering "+e.getMessage());
			e.printStackTrace();
		}
	}

	private boolean validate() {
		if(date.getValue()==null)
		{
			notify.showErrorMessage("Select Date");
			date.requestFocus();
			return false;
		}
		if(cmbFromBank.getSelectionModel().getSelectedItem()==null)
		{
			notify.showErrorMessage("Select From Bank");
			cmbFromBank.requestFocus();
			return false;
		}
		if(cmbToBank.getSelectionModel().getSelectedItem()==null)
		{
			notify.showErrorMessage("Select To Bank");
			cmbToBank.requestFocus();
			return false;
		}
		if(cmbFromBank.getSelectionModel().getSelectedItem().equals(cmbToBank.getSelectionModel().getSelectedItem()))
		{
			notify.showErrorMessage("From Bank And To Bank Must Be Different");
			cmbToBank.requestFocus();
			return false;
		}
		if(!isNumber(txtAmount.getText()))
		{
			notify.showErrorMessage("Enter Amount in Digit");
			txtAmount.requestFocus();
			return false;
		}
		if(lblBalance.getText().equals("")) {
			notify.showErrorMessage("Select from Bank Agein");
			cmbFromBank.requestFocus();
			return false;			
		}
		if(Float.parseFloat(lblBalance.getText())<Float.parseFloat(txtAmount.getText()))
		{
			notify.showErrorMessage("Insufficient Balance in From Bank");
			cmbFromBank.requestFocus();
			return false;
		}
		return true;
	}

	private boolean isNumber(String text) {
		try {
			Float.parseFloat(text);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void clear() {
		cmbFromBank.getSelectionModel().clearSelection();
		cmbToBank.getSelectionModel().clearSelection();
		txtAmount.setText("");
		date.setValue(LocalDate.now());
		
	}

}
