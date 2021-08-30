package main.java.main.java.controller.report;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.List;
import java.util.ResourceBundle;

public class CustomerBillControler implements Initializable {
		@FXML
		private AnchorPane mainFrame;
	 	//@FXML private ComboBox<String> cmbCustomerName;
	 	@FXML private TextField txtCustomerName;
	    @FXML private DatePicker dateFrom;
	    @FXML private DatePicker dateTo;
	    @FXML private Button btnShow;
	    @FXML private Button btnReset;
	    @FXML private Button btnPreview;
	    @FXML private Button btnBack;
	    @FXML private TableView<Bill> table;
	    @FXML private TableColumn<Bill,String> colSrNo;
	    @FXML private TableColumn<Bill, LocalDate> colDate;
	    @FXML private TableColumn<Bill,Long> colBillNo;
	    @FXML private TableColumn<Bill,Double> colBillAmount;
	    @FXML private TableColumn<Bill,Double> colPaidAmount;
	    @FXML private TableColumn<Bill,Double> colRemaining;
	    @FXML private TableColumn<Bill,String> colSalesmanName;
	    @FXML private TextField txtTotalAmount;
	    @FXML private TextField txtTotalPaid;
	    @FXML private TextField txtRemaining;
	    //private EmployeeService empService;
	    private CustomerService customerService;
	    private BillService billService;
	    private ObservableList<Bill> billList = FXCollections.observableArrayList();
	    private ObservableList<String> customerNameList = FXCollections.observableArrayList();
	    private SuggestionProvider<String> customerNameProvider;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//empService = new EmployeeServiceImpl();
		customerService = new CustomerServiceImpl();
		billService = new BillServiceImpl();
		//cmbCustomerName.getItems().addAll(customerService.getAllCustomerNames());
		
		customerNameList.addAll(customerService.getAllCustomerNames());
		customerNameProvider = SuggestionProvider.create(customerService.getAllCustomerNames());
		new AutoCompletionTextFieldBinding<>(txtCustomerName, customerNameProvider);
		
		
		colSrNo.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedby"));
		colDate.setCellValueFactory(new PropertyValueFactory<Bill,LocalDate>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<Bill,Long>("billno"));
		colBillAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("nettotal"));
		colPaidAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("recivedamount"));
		colRemaining.setCellValueFactory(new PropertyValueFactory<Bill,Double>("otherchargs"));
		colSalesmanName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedreff"));
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
	    	txtTotalAmount.setText("");
	    	txtRemaining.setText("");
	    	txtTotalPaid.setText("");
	    	billList.clear();
	    	table.getSelectionModel().clearSelection();
	    }

	    @FXML
	    void btnShowAction(ActionEvent event) {
	    	loadData();
	    }
	    private void loadData()
	    {
	    	try {
				if(txtCustomerName.getText().equals(""))
				{
					new Alert(Alert.AlertType.ERROR,"Select Customer Name!!!").showAndWait();
					txtCustomerName.requestFocus();
					return;
				}
				if(customerService.getCustomerByName(txtCustomerName.getText())==null)
				{
					new Alert(Alert.AlertType.ERROR,"Select Customer Again!!!").showAndWait();
					txtCustomerName.requestFocus();
					return;
				}
				if(dateFrom.getValue()==null)
				{
					new Alert(Alert.AlertType.ERROR,"Select From Date!!!").showAndWait();
					dateFrom.requestFocus();
					return;
				}
				if(dateTo.getValue()==null)
				{
					new Alert(Alert.AlertType.ERROR,"Select To Date!!!").showAndWait();
					dateTo.requestFocus();
					return;
				}
				billList.clear();
				int cid = customerService.getCustomerByName(txtCustomerName.getText()).getId();
				List<Bill> list = billService.getPeriodWiseBills(dateFrom.getValue(), dateTo.getValue());
				for(Bill b:list)
				{
					if(b.getCustomer().getId()==cid)
					{
						billList.add(b);
					}
				}
				int sr=0;
				double totalAmount=0,totalPaid=0,totalunpaid=0;
				for(int i=0;i<billList.size();i++)
				{
					billList.get(i).setNettotal(
							billList.get(i).getNettotal()+
							billList.get(i).getOtherchargs()+
							billList.get(i).getTransportingchrges());
					billList.get(i).setRecievedreff(
							billList.get(i).getEmployee().getFname()+" "+
									billList.get(i).getEmployee().getMname()+" "+
									billList.get(i).getEmployee().getLname()+" ");
					billList.get(i).setOtherchargs(billList.get(i).getNettotal()-billList.get(i).getRecivedamount());
					billList.get(i).setRecievedby(""+(++sr));
					totalAmount = totalAmount+billList.get(i).getNettotal();
					totalPaid = totalPaid+billList.get(i).getRecivedamount();
					totalunpaid = totalunpaid+billList.get(i).getOtherchargs();
				}
				txtTotalAmount.setText(""+totalAmount);
				txtTotalPaid.setText(""+totalPaid);
				txtRemaining.setText(""+totalunpaid);
				
	    	} catch (Exception e) {
				new Alert(Alert.AlertType.ERROR,e.getMessage()).showAndWait();
			}
	    }

}
