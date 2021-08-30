package main.java.main.java.controller.report;

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
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.CustomerService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CustomerServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class CustomerUnpaidBillControler implements Initializable {

		@FXML private AnchorPane mainFrame;
	  	//@FXML private ComboBox<String> cmbCustomerName;
	    @FXML private TextField txtCustomerName;

	    @FXML private Button btnShow;
	    @FXML private Button btnPreview;
	    @FXML private Button btnReset;
	    @FXML private Button btnBack;

	    @FXML
	    private TableView<Bill> table;

	    @FXML
	    private TableColumn<Bill,String> colSrNo;//RecivedReff

	    @FXML
	    private TableColumn<Bill,LocalDate> colDate;

	    @FXML
	    private TableColumn<Bill,Long> colBillNo;

	    @FXML
	    private TableColumn<Bill,Double> colBillAmount;//nettotal

	    @FXML
	    private TableColumn<Bill,Double> colPaidAmount;

	    @FXML
	    private TableColumn<Bill,Double> colRemaining;//otherchargs

	    @FXML
	    private TableColumn<Bill,String> colSalesmanName;//received by

	    @FXML
	    private TextField txtTotalBillAmount;

	    @FXML
	    private TextField txtTotalPaidAmount;

	    @FXML
	    private TextField txtTotalRemainigAmount;

	    private CustomerService customerService;
	    private BillService billService;
	    private ObservableList<Bill>billList = FXCollections.observableArrayList();
	    private ObservableList<String> customerNameList = FXCollections.observableArrayList();
	    private SuggestionProvider<String> customerNameProvider;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		customerService = new CustomerServiceImpl();
		billService = new BillServiceImpl();
		//cmbCustomerName.getItems().addAll(customerService.getAllCustomerNames());
		customerNameList.addAll(customerService.getAllCustomerNames());
		customerNameProvider = SuggestionProvider.create(customerService.getAllCustomerNames());
		new AutoCompletionTextFieldBinding<>(txtCustomerName, customerNameProvider);
		
		colSrNo.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedreff")); 
		colDate.setCellValueFactory(new PropertyValueFactory<Bill,LocalDate>("date")); 
		colBillNo.setCellValueFactory(new PropertyValueFactory<Bill,Long>("billno"));
		colBillAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("nettotal"));
		colPaidAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("recivedamount"));
		colRemaining.setCellValueFactory(new PropertyValueFactory<Bill,Double>("otherchargs"));
		colSalesmanName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedby"));
		table.setItems(billList);
	} 
	 @FXML
	    void btnBackAction(ActionEvent event) {
		 mainFrame.setVisible(false);
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
	    	billList.clear();
	    	txtTotalBillAmount.setText("");
	    	txtTotalPaidAmount.setText("");
	    	txtTotalRemainigAmount.setText("");
	    }

	    @FXML
	    void btnShowAction(ActionEvent event) {
	    	btnReset.fire();
	    	if(txtCustomerName.getText().equals(""))
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
	    	billList.addAll(billService.getUnpaidBills(customerService.getCustomerByName(txtCustomerName.getText()).getId()));
	    	int srno=0;
	    	double totalAmount=0,paid=0,unpaid=0;
	    	for(int i=0;i<billList.size();i++)
	    	{
	    		billList.get(i).setNettotal(
	    				billList.get(i).getNettotal()+
	    				billList.get(i).getOtherchargs()+
	    				billList.get(i).getTransportingchrges());
	    		billList.get(i).setOtherchargs(
	    				billList.get(i).getNettotal()-
	    				billList.get(i).getRecivedamount());
	    		billList.get(i).setRecievedby(txtCustomerName.getText());
	    		billList.get(i).setRecievedreff(""+(++srno));
	    		totalAmount = totalAmount+billList.get(i).getNettotal();
	    		paid = paid+billList.get(i).getRecivedamount();
	    		unpaid = unpaid+billList.get(i).getOtherchargs();
	    	}
	    	txtTotalBillAmount.setText(""+totalAmount);
	    	txtTotalPaidAmount.setText(""+paid);
	    	txtTotalRemainigAmount.setText(""+unpaid);
	    	
	    }

}
