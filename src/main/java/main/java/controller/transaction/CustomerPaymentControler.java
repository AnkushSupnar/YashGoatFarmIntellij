package main.java.main.java.controller.transaction;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
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
import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.PaymentReciept;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.CustomerService;
import main.java.main.java.hibernate.service.service.PaymentRecieptService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CustomerServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PaymentRecieptServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PrintPaymentReceipt;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerPaymentControler implements Initializable {
	@FXML private AnchorPane mainFrame;
	@FXML private TextField txtCustomerName;
	@FXML private Button btnShow;
	@FXML private Button btnPreview;
	@FXML private TextField txtBillNo;
	@FXML private TableView<Bill> table;
	@FXML private TableColumn<Bill,String> colSrNo;//recievedby
	@FXML private TableColumn<Bill,LocalDate> colDate;
	@FXML private TableColumn<Bill,Long> colBillNo;
	@FXML private TableColumn<Bill,Float> colBillAmount;//nettotal
	@FXML private TableColumn<Bill,Float> colRecieved;
	@FXML private TableColumn<Bill,Float> colRemaining;//otherchargs
	@FXML private TableColumn<Bill,Float> colToday;//transportingchrges
	@FXML private TableColumn<Bill,String> colSalesman;//recievedreff
	@FXML private TextField txtTotalBillAmount;
	@FXML private TextField txtTotalPaid;
	@FXML private TextField txtTotalRemainig;
	@FXML private TextField txtTodays;
	@FXML private TextField txtRemaining;
	@FXML private ComboBox<String> cmbBankName;
	
	@FXML private Button btnPay;
	@FXML private Button btnCalculate;
	@FXML private Button btnReset;
	@FXML private TextField txtRefNo;
	private ObservableList<Bill>billList = FXCollections.observableArrayList();
	private BillService billService;
	private CustomerService customerService;
	private BankService bankService;
	private BankTransactionService bankTransactionService;
	private PaymentRecieptService paymentRecieptService;
	private SuggestionProvider<String> customerNameProvider;
    private ObservableList<String> customerNameList = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customerService = new CustomerServiceImpl();
		bankService = new BankServiceImpl();
		billService = new BillServiceImpl();
		bankTransactionService = new BankTransactionServiceImpl();
		paymentRecieptService = new PaymentRecieptServiceImpl();
		
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("recievedby"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
		colBillAmount.setCellValueFactory(new PropertyValueFactory<>("nettotal"));
		colRecieved.setCellValueFactory(new PropertyValueFactory<>("recivedamount"));
		colRemaining.setCellValueFactory(new PropertyValueFactory<>("otherchargs"));
		colToday.setCellValueFactory(new PropertyValueFactory<>("transportingchrges"));
		colSalesman.setCellValueFactory(new PropertyValueFactory<>("recievedreff"));
		table.setItems(billList);
		cmbBankName.getItems().addAll(bankService.getAllBankNames());
		//cmbCustomer.getItems().addAll(customerService.getAllCustomerNames());
		
		customerNameList.addAll(customerService.getAllCustomerNames());
		customerNameProvider = SuggestionProvider.create(customerService.getAllCustomerNames());
		new AutoCompletionTextFieldBinding<>(txtCustomerName, customerNameProvider);

		if (CommonData.paymentCustomerName != null && !CommonData.paymentCustomerName.isEmpty()) {
			txtCustomerName.setText(CommonData.paymentCustomerName);
			CommonData.paymentCustomerName = null;
			javafx.application.Platform.runLater(() -> btnShow.fire());
		}
	}
	@FXML
	void btnShowAction(ActionEvent event) {
		billList.clear();
		if(txtCustomerName.getText().isEmpty() && !txtBillNo.getText().isEmpty() && isNumber(txtBillNo.getText()))
		{

			Bill bill = billService.getBillByBillno(Long.parseLong(txtBillNo.getText()));
			if(bill!=null)
			{
				if(bill.getRecivedamount()<(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs()))
				{
					billList.add(bill);
					loadBill();
				}
				else
					new Alert(AlertType.ERROR,"this bill is already Paid").showAndWait();
			}
				return;
		}
		//btnReset.fire();
		if(txtCustomerName.getText().equals("") )
		{
			new Alert(AlertType.ERROR,"Select Customer Name!!!").showAndWait();
			txtCustomerName.requestFocus();
			return;
		}
		if(customerService.getCustomerByName(txtCustomerName.getText())==null)
		{
			new Alert(AlertType.ERROR,"Select Customer Again!!!").showAndWait();
			txtCustomerName.requestFocus();
			return;
		}
		txtBillNo.setText("");
		billList.addAll(billService.getUnpaidBills(customerService.getCustomerByName(txtCustomerName.getText()).getId()));

		loadBill();
		
	}
	void loadBill(){
		int sr=0;
		float total=0,paid=0;
		for(int i=0;i<billList.size();i++)
		{
			billList.get(i).setNettotal(
					billList.get(i).getNettotal()+
							billList.get(i).getOtherchargs()+
							billList.get(i).getTransportingchrges());
			billList.get(i).setOtherchargs(billList.get(i).getNettotal()-billList.get(i).getRecivedamount());
			billList.get(i).setTransportingchrges(0);
			billList.get(i).setRecievedby(""+(++sr));
			billList.get(i).setRecievedreff(billList.get(i).getEmployee().getFname()+" "+
					billList.get(i).getEmployee().getMname()+" "+
					billList.get(i).getEmployee().getLname());
			total=total+billList.get(i).getNettotal();
			paid = paid+billList.get(i).getRecivedamount();
		}
		txtTotalBillAmount.setText(""+total);
		txtTotalPaid.setText(""+paid);
		txtTotalRemainig.setText(""+(total-paid));
	}

	@FXML
	void btnCalculateAction(ActionEvent event) {
		btnShow.fire();
		
		if(txtTodays.getText().equals(""))
		{
			new Alert(AlertType.ERROR,"Enter Todays Recieved Amount!!!").showAndWait();
			txtTodays.requestFocus();
			return;
		}
		if(billList.size()==0)
		{
			new Alert(AlertType.ERROR,"No Pending Bills For This Customer!!!").showAndWait();
			txtCustomerName.requestFocus();
			return;
		}
		if(Float.parseFloat(txtTotalRemainig.getText())<=0)
		{
			new Alert(AlertType.ERROR,"No Pending Bills For This Customer!!!").showAndWait();
			txtCustomerName.requestFocus();
			return;
		}
		if(!isNumber(txtTodays.getText()))
		{
			new Alert(AlertType.ERROR,"Enter Todays Recieved in Digit");
			txtTodays.requestFocus();
			return;
		}
		if(Float.parseFloat(txtTotalRemainig.getText())<Float.parseFloat(txtTodays.getText()))
		{
			new Alert(AlertType.ERROR,"Recieved Amount Must Less or Equal to Remaining Amount!!!").showAndWait();
			txtTodays.requestFocus();
			return;
		}
		float today = Float.parseFloat(txtTodays.getText());
		//float remain=0;
		//Bill bill = null;
		List<Bill>list = billList;
		for(int i=0;i<list.size();i++)
		{
			if(today>0) 
			{
				if(today>list.get(i).getOtherchargs())
				{
					list.get(i).setTransportingchrges(list.get(i).getOtherchargs());
					today=today-list.get(i).getOtherchargs();
				}
				else
				{
					list.get(i).setTransportingchrges(today);
					today=0;
				}
			}
		}
		Bill bill=null;
		
		for(int i=0;i<list.size();i++)
		{
			bill = billList.get(i);
			billList.set(i, bill);
		}
		txtRemaining.setText(""+(
		Float.parseFloat(txtTotalRemainig.getText())-
		Float.parseFloat(txtTodays.getText())));
	}

	@FXML
	void btnPayAction(ActionEvent event) {
		btnCalculate.fire();
		if(cmbBankName.getValue()==null)
		{
			new Alert(AlertType.ERROR,"Select Bank Name!!!").showAndWait();
			cmbBankName.requestFocus();
			return;
		}
		Bank bank = bankService.getBankByName(cmbBankName.getValue());
		int bankid = bank.getId();
		String refNo = (txtRefNo != null && txtRefNo.getText() != null) ? txtRefNo.getText().trim() : "";
		LocalDate today = LocalDate.now();

		List<Long> settledBillNos = new ArrayList<>();
		float totalSettled = 0f;

		for(Bill bill:billList)
		{
			float allocated = bill.getTransportingchrges();
			if (allocated <= 0.001f) continue;
			if (billService.addPaymentToBill(bill.getBillno(), bank, allocated, refNo, today) == 1) {
				BankTransaction banktr = new BankTransaction();
				banktr.setBankid(bankid);
				banktr.setCredit(0);
				banktr.setDebit(allocated);
				banktr.setParticulars("Payment received against BillNo " + bill.getBillno()
						+ (refNo.isEmpty() ? "" : " Ref:" + refNo));
				banktr.setReffid(bill.getBillno());
				banktr.setDate(today);
				bankTransactionService.saveBankTransaction(banktr);
				settledBillNos.add(bill.getBillno());
				totalSettled += allocated;
			}
		}

		if (totalSettled <= 0f) {
			new Alert(AlertType.ERROR, "Nothing to pay. Enter Today's Received Amount first.").showAndWait();
			return;
		}

		bankService.addBankBalance(bankid, totalSettled);

		// Create a stand-alone PaymentReciept row for the printed receipt + history.
		String billRefs = "Bills: " + settledBillNos.toString().replaceAll("[\\[\\]]", "");
		String note = billRefs + (refNo.isEmpty() ? "" : " | Ref " + refNo);
		PaymentReciept pr = new PaymentReciept(today, txtCustomerName.getText(), note, totalSettled, bank);
		paymentRecieptService.savePaymentReciept(pr);

		new Alert(AlertType.INFORMATION, "Payment Saved. Total Settled: " + totalSettled).showAndWait();

		Alert printAsk = new Alert(AlertType.CONFIRMATION, "Print receipt?");
		printAsk.setHeaderText("Payment Received");
		printAsk.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> ans = printAsk.showAndWait();
		if (ans.isPresent() && ans.get() == ButtonType.YES) {
			try {
				new PrintPaymentReceipt(pr.getId());
				new PrintFile().openFile(PrintPaymentReceipt.filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		btnReset.fire();
	}

	@FXML
	void btnPreviewAction(ActionEvent event) {
		if(table.getSelectionModel().getSelectedItem()==null)
		{
			return;
		}
		Bill bill = table.getSelectionModel().getSelectedItem();
		if(bill==null)
		{
			return;
		}
		CommonData.previewBillNo = bill.getBillno();
		new ViewUtil().showBillPreview(event);
	}

	@FXML
	void btnResetAction(ActionEvent event) {

		txtRemaining.setText("");
		txtTodays.setText("");
		txtTotalBillAmount.setText("");
		txtTotalPaid.setText("");
		txtTotalRemainig.setText("");
		if (txtRefNo != null) txtRefNo.setText("");
		txtCustomerName.setText("");
		billList.clear();
		cmbBankName.getSelectionModel().clearSelection();
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
}
