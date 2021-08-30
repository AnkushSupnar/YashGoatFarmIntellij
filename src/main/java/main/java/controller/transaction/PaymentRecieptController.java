package main.java.main.java.controller.transaction;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.entities.PaymentReciept;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.PaymentRecieptService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PaymentRecieptServiceImpl;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PrintPaymentReceipt;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentRecieptController implements Initializable {

	@FXML private AnchorPane mainPanel;
     @FXML private DatePicker date;
     @FXML private TextField txtName;
     @FXML private TextField txtReference;
	 @FXML private TextField txtAmount;
     @FXML private ComboBox<String> cmbBankName;
	 @FXML private Button btnPay;
     @FXML private Button btnClear;
     @FXML private Button btnUpdate;
     @FXML private Button btnHome;
     @FXML private Button btnPrint;
     @FXML private TableView<PaymentReciept> table;
     @FXML private TableColumn<PaymentReciept,Long> colSrNo;
     @FXML private TableColumn<PaymentReciept,LocalDate> colDate;
     @FXML private TableColumn<PaymentReciept,String> colName;
     @FXML private TableColumn<PaymentReciept,Double> colAmount;
     @FXML private TableColumn<PaymentReciept,String> colNote;
     @FXML private TableColumn<PaymentReciept,String> colBank;
     
     private AlertNotification notify;
     private BankService bankService;
     private BankTransactionService bankTrService;
     private PaymentRecieptService paymentService;
     private ObservableList<PaymentReciept>paymentList = FXCollections.observableArrayList();
     private long id;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		id=0;
		notify = new AlertNotification();
		bankService = new BankServiceImpl();
		paymentService = new PaymentRecieptServiceImpl();
		bankTrService = new BankTransactionServiceImpl();
		date.setValue(LocalDate.now());
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));      
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
		//colBank.setCellValueFactory(new PropertyValueFactory<>("bankname"));
		colBank.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBank().getBankname()));  
		paymentList.addAll(paymentService.getAllPaymentReciept());
		table.setItems(paymentList);
		cmbBankName.getItems().addAll(bankService.getAllBankNames());
		btnPay.setOnAction(e->pay());
		btnUpdate.setOnAction(e->update());
		btnClear.setOnAction(e->clear());
		btnPrint.setOnAction(e->{
			if(table.getSelectionModel().getSelectedItem()!=null)
			{
				new PrintPaymentReceipt(table.getSelectionModel().getSelectedItem().getId());
				new PrintFile().openFile("D:\\Software\\Prints\\receipt.pdf");
			}
		});
	}

	private void print(long rid) {
	try {
		Stage stage = (Stage) mainPanel.getScene().getWindow();
		Alert.AlertType type = Alert.AlertType.CONFIRMATION;
		Alert alert = new Alert(type, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText("Do You Want Print Receipt?");
		alert.getDialogPane().setHeaderText("Confirmation");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// new BillPrint(billno);
			try {
				new PrintPaymentReceipt(rid);
				new PrintFile().openFile	("D:\\Software\\Prints\\receipt.pdf");
			} catch (Exception e) {
				notify.showErrorMessage( e.getMessage());
			}
		} else if (result.get() == ButtonType.CANCEL) {

		}

	} catch (Exception e) {
		
	}
	}

	private void update() {
		try {
			if(table.getSelectionModel().getSelectedItem()==null)
			return;
			PaymentReciept pr = table.getSelectionModel().getSelectedItem();
			if(pr==null)
				return;
			date.setValue(pr.getDate());
			txtName.setText(pr.getName());
			txtAmount.setText(""+pr.getAmount());
			txtReference.setText(pr.getNote());
			cmbBankName.setValue(pr.getBank().getBankname());
			id=pr.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void pay() {
		try {
			if(txtName.getText().isEmpty())
			{
				notify.showErrorMessage("Enter Name");
				txtName.requestFocus();
				return;
			}
			if(txtReference.getText().isEmpty())
			{
				notify.showErrorMessage("Enter Reference");
				txtReference.requestFocus();
				return;
			}
			if(txtAmount.getText().isEmpty())
			{
				notify.showErrorMessage("Enter Amount");
				txtAmount.requestFocus();
				return;
			}
			if(!isNumber(txtAmount.getText()))
			{
				notify.showErrorMessage("Enter Amount in Digit");
				txtAmount.requestFocus();
				return;
			}
			if(cmbBankName.getValue()==null)
			{
				notify.showErrorMessage("Select Bank Name");
				cmbBankName.requestFocus();
				return;
			}
			PaymentReciept reciept = new PaymentReciept(date.getValue(),txtName.getText(),txtReference.getText(),Float.parseFloat(txtAmount.getText()), bankService.getBankByName(cmbBankName.getSelectionModel().getSelectedItem()));
			if(id==0)
			{
			//	System.out.println("Adding");
				int flag = paymentService.savePaymentReciept(reciept);
				if(flag==1)
				{
					//reduce amount from bbank
					BankTransaction btr = new BankTransaction("Pay id"+reciept.getId(),reciept.getId(), 0,reciept.getAmount(), reciept.getBank().getId(), reciept.getDate());
					if(new BankTransactionServiceImpl().saveBankTransaction(btr)==1)
					{
						bankService.reduceBankBalance(reciept.getBank().getId(),reciept.getAmount());
					}
					addInList(reciept);
					notify.showSuccessMessage("Record Save Success");
					print(reciept.getId());
					clear();
				//	System.out.println(reciept);
				}
			}
			else
			{
				//System.out.println("update id="+id);
				//getOld Reciept
				PaymentReciept old = paymentService.getPaymentRecieptById(id);
				reciept.setId(id);
				int flag = paymentService.savePaymentReciept(reciept);
				if(flag==2)
				{
					//Add old payment in bank
					BankTransaction btr = new BankTransaction("Edit Pay id "+old.getId(),old.getId(),old.getAmount(),0,old.getBank().getId(), date.getValue());
					int f = bankTrService.saveBankTransaction(btr);
					if(f==1)
					{
					//	System.out.println("money added "+old.getAmount());
						bankService.addBankBalance(old.getBank().getId(),old.getAmount());
					}
					//update payment reduce new amount from new bank
					BankTransaction bt = new BankTransaction("Pay id"+reciept.getId(),reciept.getId(), 0,reciept.getAmount(), reciept.getBank().getId(), reciept.getDate());
					f=0;
					f = bankTrService.saveBankTransaction(bt);
					if(f==1)
					{
						//System.out.println("moneyreduce "+reciept.getAmount());
						bankService.reduceBankBalance(reciept.getBank().getId(),reciept.getAmount());
					}
					notify.showSuccessMessage("Reciept Update Success");
					print(reciept.getId());
					addInList(reciept);
					clear();
				}
			}
		} catch (Exception e) {
			notify.showErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}
	private void clear()
	{
		txtAmount.setText("");
		txtName.setText("");
		cmbBankName.getSelectionModel().clearSelection();
		txtReference.setText("");
		date.setValue(LocalDate.now());
		id=0;
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
	private void addInList(PaymentReciept reciept)
	{
		int flag=-1;
		for(int i=0;i<paymentList.size();i++)
		{
			if(reciept.getId()==paymentList.get(i).getId())
			{
				flag=i;
			}
		}
		if(flag==-1)
		{
			System.out.println("Not Found");
			paymentList.add(reciept);
		}
		else
		{
			System.out.println("Found");
			paymentList.remove(flag);
			paymentList.add(flag,reciept);
			table.refresh();
			
		}		
	}
	

}
