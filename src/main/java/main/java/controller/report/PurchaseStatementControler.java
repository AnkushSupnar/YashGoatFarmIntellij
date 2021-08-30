package main.java.main.java.controller.report;

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
import main.java.main.java.hibernate.entities.AdvancePayment;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.reportEntity.PurchaseStatementPojo;
import main.java.main.java.hibernate.service.service.AdvancePaymentService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.service.PurchasePartyService;
import main.java.main.java.hibernate.service.serviceImpl.AdvancePaymentServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchasePartyServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PurchaseStatementPrint;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class PurchaseStatementControler implements Initializable {

	    @FXML private AnchorPane mainPain;
	    @FXML private ComboBox<String> cmbPartyName;
	    @FXML private DatePicker dateFrom;
	    @FXML private DatePicker dateTo;
	    @FXML private Button btnShow;
	    @FXML private Button btnReset;
	    @FXML private Button btnPreview;
	    @FXML private Button btnPrint;
	    @FXML private Button btnClose;

	    @FXML private TableView<PurchaseStatementPojo> table;
	    @FXML private TableColumn<PurchaseStatementPojo, Integer> colSrNo;
	    @FXML private TableColumn<PurchaseStatementPojo,String> colParticulars;
	    @FXML private TableColumn<PurchaseStatementPojo,LocalDate> colDate;
	    @FXML private TableColumn<PurchaseStatementPojo,Float> colDebit;
	    @FXML private TableColumn<PurchaseStatementPojo,Float> colCredit;
	    @FXML private TableColumn<PurchaseStatementPojo,Float> colBalance;
	    
	    @FXML private TextField txtDebit;
	    @FXML private TextField txtCredit;
	    @FXML private TextField txtBalance;
	    @FXML private ProgressIndicator progress;

	    private PurchasePartyService partyService;
	    private PurchaseInvoiceService invoiceService;
	    private BankTransactionService bankTransactionservice;
	    private AdvancePaymentService advanceService;
	    private List<PurchaseInvoice> invoiceList;
	    
	    private ObservableList<PurchaseStatementPojo> purchaseList = FXCollections.observableArrayList();
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		partyService = new PurchasePartyServiceImpl();
		invoiceService = new PurchaseInvoiceServiceImpl();
		bankTransactionservice = new BankTransactionServiceImpl();
		advanceService = new AdvancePaymentServiceImpl();
		cmbPartyName.getItems().addAll(partyService.getAllPurchasePartyNames());
		
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id")); 
		colParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colDebit.setCellValueFactory(new PropertyValueFactory<>("debit"));
		colCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
		colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
		
		table.setItems(purchaseList);
		
		table.setVisible(true);
		progress.setVisible(false);
	}
	@FXML
	void btnShowAction(ActionEvent event) {
		try {
			if (validateData() != 1) {
				return;
			}
			//table.setVisible(false);
			//progress.setVisible(true);
			int partyid = partyService.getPurchasePartyByName(cmbPartyName.getValue()).getId();
			System.out.println("Party id = " + partyid);
			purchaseList.clear();
			int srno = 0;
			float bal=0,debit=0,credit=0;
			
			for (LocalDate date = dateFrom.getValue(); date
					.isBefore(dateTo.getValue().plusDays(1)); date = date.plusDays(1)) {
				invoiceList = invoiceService.getPurchaseInvoicePartyWise(date, partyid);
				BankTransaction bankTr;
				for(AdvancePayment pay:advanceService.getDatePartyWiseAdvancePayment(date, partyid))
				{
					purchaseList.add(new PurchaseStatementPojo((++srno),"Advance Payment id"+pay.getId(),pay.getAmount(), 0, 0, pay.getDate(), pay.getId()));
				//	progress.setProgress(progress.getProgress()+0.1);
				}
				for (PurchaseInvoice in : invoiceList) {
					purchaseList.add(new PurchaseStatementPojo(++srno, "Purchase BillNo" + in.getBillno(), 0,
							in.getGrandtotal(), 0, in.getDate(), in.getBillno()));
					//progress.setProgress(progress.getProgress()+0.1);
					bankTr = bankTransactionservice
							.getBankTransactionByPartucular("Reduce Invoice Amount BillNo " + in.getBillno());
					if (bankTr != null) {
						purchaseList.add(new PurchaseStatementPojo(++srno, "Pay Purchase Bill " + in.getBillno(),
								bankTr.getCredit(), 0, 0, bankTr.getDate(), in.getBillno()));
						//progress.setProgress(progress.getProgress()+0.1);
					}
				}
			}
			for(int i=0;i<purchaseList.size();i++)
			{
				bal = bal-purchaseList.get(i).getCredit();
				bal = bal+purchaseList.get(i).getDebit();
				purchaseList.get(i).setBalance(bal);
				debit = debit+purchaseList.get(i).getDebit();
				credit = credit+purchaseList.get(i).getCredit();
			}
			txtCredit.setText(""+credit);
			txtDebit.setText(""+debit);
			txtBalance.setText(""+(debit-credit));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void btnCloseAction(ActionEvent event) {
		mainPain.setVisible(false);
	}

	@FXML
	void btnPreviewAction(ActionEvent event) {
		try {
			if (table.getSelectionModel().getSelectedItem() == null)
				return;
			PurchaseStatementPojo pj = table.getSelectionModel().getSelectedItem();
			if (pj == null)
				return;
			System.out.println(pj);
			CommonData.previewInvoiceno = pj.getBillno();
			new ViewUtil().showInvoicePreview(event);

		} catch (Exception e) {
			new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
		}
	}

	@FXML
	void btnPrintAction(ActionEvent event) {
		if(purchaseList.isEmpty())
			return;
		new PurchaseStatementPrint(purchaseList,dateFrom.getValue(),dateTo.getValue(),partyService.getPurchasePartyByName(cmbPartyName.getValue()));
		new PrintFile().openFile("D:\\Software\\Prints\\PurchaseStatement.pdf");
	}

	@FXML
	void btnResetAction(ActionEvent event) {
		cmbPartyName.getSelectionModel().clearSelection();
		dateFrom.setValue(null);
		dateTo.setValue(null);
		purchaseList.clear();
		txtBalance.setText("");
		txtCredit.setText("");
		txtDebit.setText("");
	}

	private int validateData() {
		try {
			if (cmbPartyName.getValue() == null) {
				new Alert(AlertType.ERROR, "Select Party Name").showAndWait();
				cmbPartyName.requestFocus();
				return 0;
			}
			if (dateFrom.getValue() == null) {
				new Alert(AlertType.ERROR, "Select From Date").showAndWait();
				dateFrom.requestFocus();
				return 0;
			}
			if (dateTo.getValue() == null) {
				new Alert(AlertType.ERROR, "Select To Date").showAndWait();
				dateTo.requestFocus();
				return 0;
			}
			if (dateFrom.getValue().compareTo(dateTo.getValue()) == 1) {
				new Alert(AlertType.ERROR, "From Date Must be Smaller than To Date").showAndWait();
				dateTo.requestFocus();
				return 0;
			}
			return 1;
		} catch (Exception e) {
			new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
			return 0;
		}
	}

}
