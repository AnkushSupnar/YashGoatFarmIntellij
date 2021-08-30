package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.print.PrintBankStatement;
import main.java.main.java.print.PrintFile;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.*;

public class BankStatementControler implements Initializable {
		@FXML
		private AnchorPane mainFrame;
		@FXML private ComboBox<String> cmbBankName;
	    @FXML private DatePicker dateFrom;
	    @FXML private DatePicker dateTo;
	    @FXML private Button btnShow;
	    @FXML private Button btnReset;
	    @FXML private Button btnClose;
	    @FXML private Button btnWeekly;
	    @FXML private Button btnMonthly;
	    @FXML private Button btnYearly;
	    @FXML private Button btnAll;
	    @FXML private Button btnPrint;

	    @FXML private TableView<BankTransaction> table;
	    @FXML private TableColumn<BankTransaction, Long> colSrNo;
	    @FXML private TableColumn<BankTransaction,String> colDescription;
	    @FXML private TableColumn<BankTransaction, LocalDate> colDate;
	    @FXML private TableColumn<BankTransaction,Double> colDebit;
	    @FXML private TableColumn<BankTransaction,Double> colCredit;
	    @FXML private TextField txtDebit;
	    @FXML private TextField txtCredit;
	    @FXML private TextField txtBalance;

	    
	    private BankService bankService;
	    private BankTransactionService bankTrService;
	    private ObservableList<BankTransaction> list = FXCollections.observableArrayList();
	    private AlertNotification notify;
	    private LocalDate start,end;
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bankService = new BankServiceImpl();
		bankTrService = new BankTransactionServiceImpl();
		notify = new AlertNotification();
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDescription.setCellValueFactory(new PropertyValueFactory<>("particulars"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colDebit.setCellValueFactory(new PropertyValueFactory<>("credit"));
		colCredit.setCellValueFactory(new PropertyValueFactory<>("debit"));
		table.setItems(list);
		cmbBankName.getItems().addAll(bankService.getAllBankNames());
		btnWeekly.setOnAction(e->loadWeekly());
		btnMonthly.setOnAction(e->loadMonthly());
		btnYearly.setOnAction(e->loadYearly());
		btnAll.setOnAction(e->loadAll());
		btnPrint.setOnAction(e->print());
	}

	private void print() {
		if(list.size()==0)
			return;
		new PrintBankStatement(list,start,end);
		new PrintFile().openFile("D:\\Software\\Prints\\BankStatement.pdf");
		
	}

	@FXML
	    void btnCloseAction(ActionEvent event) {
		 mainFrame.setVisible(false);
	    }

	    @FXML
	    void btnResetAction(ActionEvent event) {
	    	list.clear();
	    	txtDebit.setText(""+0.0);
	    	txtCredit.setText(""+0.0);
	    	txtBalance.setText(""+0.0);
	    	cmbBankName.getSelectionModel().clearSelection();
	    	dateFrom.setValue(null);
	    	dateTo.setValue(null);
	    }

	    @FXML
		void btnShowAction(ActionEvent event) {
			if (validateData() != 1) {
				return;
			}
			loadData(dateFrom.getValue(),dateTo.getValue());
		}
	    private int validateData()
	    {
	    	try {
				if(cmbBankName.getValue()==null)
				{
					notify.showErrorMessage("Select Bank Name!!!");
					cmbBankName.requestFocus();
					return 0;
				}
				if(dateFrom.getValue()==null)
				{
					notify.showErrorMessage("Select Fram Date!!!");
					dateFrom.requestFocus();
					return 0;
				}
				if(dateTo.getValue()==null)
				{
					notify.showErrorMessage("Select To Date!!!");
					dateTo.requestFocus();
					return 0;
				}
				return 1;
			} catch (Exception e) {
				notify.showErrorMessage(e.getMessage());
				return 0;
			}
	    }

	    void loadData(LocalDate start,LocalDate end)
	    {
	    	this.start=start;
	    	this.end=end;
	    	list.clear();
			txtDebit.setText(""+0.0);
			txtCredit.setText(""+0.0);
			txtBalance.setText(""+0.0);
			Bank bank =bankService.getBankByName( cmbBankName.getValue());
			list.addAll(bankTrService.getPeriodWiseBankTransaction(start, end,bankService.getBankByName(cmbBankName.getValue()).getId()));
			txtBalance.setText(""+bank.getBalance());
			int sr=0;
			for(int i=0;i<list.size();i++)
			{
				list.get(i).setId(++sr);
				txtDebit.setText(""+
			(Float.parseFloat(txtDebit.getText())+list.get(i).getDebit()));
				txtCredit.setText(""+
						(Float.parseFloat(txtCredit.getText())+list.get(i).getCredit()));
			}

	    }
		 private void loadAll() {
			 if(cmbBankName.getValue()==null)
			 {
				 notify.showErrorMessage("select Bank Name");
				 cmbBankName.requestFocus();
				 return;
			 }
			 list.clear();
				txtDebit.setText(""+0.0);
				txtCredit.setText(""+0.0);
				txtBalance.setText(""+0.0);
				Bank bank =bankService.getBankByName( cmbBankName.getValue());
				list.addAll(bankTrService.getBankTransaction(bankService.getBankByName(cmbBankName.getValue()).getId()));
				this.start=null;
				this.end=null;
				txtBalance.setText(""+bank.getBalance());
				int sr=0;
				for(int i=0;i<list.size();i++)
				{
					list.get(i).setId(++sr);
					txtDebit.setText(""+
				(Float.parseFloat(txtDebit.getText())+list.get(i).getDebit()));
					txtCredit.setText(""+
							(Float.parseFloat(txtCredit.getText())+list.get(i).getCredit()));
				}

		}
		private void loadYearly() {
			 if(cmbBankName.getValue()==null)
				{
					notify.showErrorMessage("Select Bank Name");
					cmbBankName.requestFocus();
					return;
				}
				if(dateFrom.getValue()==null)
				{
					notify.showErrorMessage("Select Date in From Date");
					dateFrom.requestFocus();
					return;
				}
				loadData(dateFrom.getValue().with(firstDayOfYear()),dateFrom.getValue().with(lastDayOfYear()));
		}
		private void loadMonthly() {
			 if(cmbBankName.getValue()==null)
				{
					notify.showErrorMessage("Select Bank Name");
					cmbBankName.requestFocus();
					return;
				}
				if(dateFrom.getValue()==null)
				{
					notify.showErrorMessage("Select Date in From Date");
					dateFrom.requestFocus();
					return;
				}
				loadData(dateFrom.getValue().with(firstDayOfMonth()),dateFrom.getValue().with(lastDayOfMonth()));			
		}
		private void loadWeekly() {
			if(cmbBankName.getValue()==null)
			{
				notify.showErrorMessage("Select Bank Name");
				cmbBankName.requestFocus();
				return;
			}
			if(dateFrom.getValue()==null)
			{
				notify.showErrorMessage("Select Date in From Date");
				dateFrom.requestFocus();
				return;
			}		
			loadData(dateFrom.getValue().with(previousOrSame(MONDAY)),dateFrom.getValue().with(nextOrSame(SUNDAY)));		
		}
}
