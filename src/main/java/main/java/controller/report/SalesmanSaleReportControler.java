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
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class SalesmanSaleReportControler implements Initializable {

	 	@FXML private AnchorPane mainFrame;
	    @FXML private ComboBox<String> cmbSalesman;
	    @FXML private DatePicker fromDate;
	    @FXML private DatePicker toDate;
	    @FXML private Button btnShow;
	    @FXML private Button btnReset;
	    @FXML private Button btnBack;
	    @FXML private Button btnPreview;
	    @FXML private TableView<Bill> table;
	    @FXML private TableColumn<Bill, String> colSrNo;//recievedreff
	    @FXML private TableColumn<Bill,LocalDate> colDate;
	    @FXML private TableColumn<Bill,Long> colBillNo;
	    @FXML private TableColumn<Bill,Float> colBillAmount;
	    @FXML private TableColumn<Bill,Float> colCommistion;
	    @FXML private TableColumn<Bill,Float> colPaidCommision;
	    @FXML private TableColumn<Bill,String>colCustomerName;
	    @FXML private TextField txtTotalBillAmount;
	    @FXML private TextField txtTotalCommision;
	    @FXML private TextField txtPaidCommition;
	    private EmployeeService empService;
	    private BillService billService;
	    private ObservableList<Bill> billList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		empService = new EmployeeServiceImpl();
		billService = new BillServiceImpl();
		cmbSalesman.getItems().addAll(empService.getAllSalesmanNames());
		
		colSrNo.setCellValueFactory(new PropertyValueFactory<Bill, String>("recievedreff"));
		colDate.setCellValueFactory(new PropertyValueFactory<Bill, LocalDate>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<Bill, Long>("billno"));
		colBillAmount.setCellValueFactory(new PropertyValueFactory<Bill, Float>("nettotal"));
		colCommistion.setCellValueFactory(new PropertyValueFactory<Bill, Float>("otherchargs"));
		colPaidCommision.setCellValueFactory(new PropertyValueFactory<Bill, Float>("paidcommision"));
		colCustomerName.setCellValueFactory(new PropertyValueFactory<Bill, String>("recievedby"));
		table.setItems(billList);
		

	}
	 @FXML
	    void btnBackAction(ActionEvent event) {
		 mainFrame.setVisible(false);
	    }

	    @FXML
	    void btnResetAction(ActionEvent event) {
	    	billList.clear();
	    	txtPaidCommition.setText("");
	    	txtTotalBillAmount.setText("");
	    	txtTotalCommision.setText("");
	    }

	    @FXML
	    void btnShowAction(ActionEvent event) {
	    	loadData();
	    }
	    private void loadData()
	    {
	    	billList.clear();
	    	txtPaidCommition.setText("");
	    	txtTotalBillAmount.setText("");
	    	txtTotalCommision.setText("");
	    	try {
				if(cmbSalesman.getSelectionModel().getSelectedItem().equals("")||cmbSalesman.getSelectionModel().getSelectedItem().equals(null))
				{
					new Alert(AlertType.ERROR,"Please Select Salesman Name!!!").showAndWait();
					cmbSalesman.requestFocus();
					return;
				}
				if(fromDate.getValue()==null)
				{
					new Alert(AlertType.ERROR,"Please Select From Date!!!").showAndWait();
					fromDate.requestFocus();
					return;
				}
				if(toDate.getValue()==null)
				{
					new Alert(AlertType.ERROR,"Please Select To Date!!!").showAndWait();
					toDate.requestFocus();
					return;
				}
				if(fromDate.getValue().compareTo(toDate.getValue())==1)
				{
					new Alert(AlertType.ERROR,"From Date Must be less than To Date!!!").showAndWait();
					toDate.requestFocus();
					return;
				}
				List<Bill>list = billService.getPeriodWiseBills(fromDate.getValue(), toDate.getValue());
				int empid = empService.getEmployeeByName(cmbSalesman.getSelectionModel().getSelectedItem()).getId();
				for(Bill b:list)
				{
					if(b.getEmployee().getId()==empid)
					{
						billList.add(b);
					}
				}
				int sr=0;
				float totalBill=0,totalCommision=0,totalpaid=0;
				for(int i=0;i<billList.size();i++)
				{
					billList.get(i).setRecievedreff(""+(++sr));
					billList.get(i).setNettotal(
							billList.get(i).getNettotal()+
							billList.get(i).getOtherchargs()+
							billList.get(i).getTransportingchrges());
					totalBill = totalBill+billList.get(i).getNettotal();
					
					billList.get(i).setRecievedby(
							billList.get(i).getCustomer().getFname()+" "+
							billList.get(i).getCustomer().getMname()+ " "+
							billList.get(i).getCustomer().getLname());
					billList.get(i).setOtherchargs(getCommision(billList.get(i)));
					totalCommision = totalCommision+billList.get(i).getOtherchargs();
					totalpaid = totalpaid+billList.get(i).getPaidcommision();
				}
				txtTotalBillAmount.setText(""+totalBill);
				txtTotalCommision.setText(""+totalCommision);
				txtPaidCommition.setText(""+totalpaid);
			} catch (Exception e) {
				new Alert(AlertType.ERROR,e.getMessage()).showAndWait();
			}
	    }
	    @FXML
	    void btnPreviewAction(ActionEvent event) {
	    	if(table.getSelectionModel().getSelectedItem()==null)
	    	{return;}
	    	Bill bill = billService.getBillByBillno(table.getSelectionModel().getSelectedItem().getBillno());
	    	if(bill==null)
	    	{
	    		new Alert(AlertType.ERROR,"Select Bill from Above List To Preview!!!").showAndWait();
	    		return;
	    	}
	    	CommonData.previewBillNo = bill.getBillno();
	    	new ViewUtil().showBillPreview(event);
	    	
	    }

	    private float getCommision(Bill bill)
	    {
	    	float commision=0;
	    	for(Transaction tr:bill.getTransaction())
	    	{
	    		commision = commision+tr.getCommision();
	    	}
	    	return commision;
	    }

}
