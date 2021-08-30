package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.util.ResourceBundle;


public class BillPreviewControler implements Initializable {

	  	@FXML private TextField txtBillNo;
	    @FXML private DatePicker date;
	    @FXML private TextField txtCustomerName;
	    @FXML private TextField txtSalesmanName;
	    
	    @FXML private TableView<Transaction> table;
	    @FXML private TableColumn<Transaction, Long> colSrNo;
	    @FXML private TableColumn<Transaction, String> colItemName;
	    @FXML private TableColumn<Transaction, String> colUnit;
	    @FXML private TableColumn<Transaction, Double> colQty;
	    @FXML private TableColumn<Transaction, Double> colRate;
	    @FXML private TableColumn<Transaction, Double>colAmount;
	    @FXML private TableColumn<Transaction, Double> colCommision;
	    
	    @FXML private TextField txtNetTotal;
	    @FXML private TextField txtOtherChrgs;
	    @FXML private TextField txtTransaportingChrgs;
	    @FXML private TextField txtGrandTotal;
	   
	    @FXML private TextField txtPaid;
	    
	    private BillService billService;
	   // private CustomerService customerService;
	    private ObservableList<Transaction> trList = FXCollections.observableArrayList();
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	billService = new BillServiceImpl();
	    	//customerService = new CustomerServiceImpl();
	    	colSrNo.setCellValueFactory(new PropertyValueFactory<Transaction, Long>("id")) ;
	    	colItemName.setCellValueFactory(new PropertyValueFactory<Transaction, String>("itemname"));
	    	colUnit.setCellValueFactory(new PropertyValueFactory<Transaction, String>("unit")) ;
	    	colQty.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("quantity")) ;
	    	colRate.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("rate")) ;
	    	colAmount.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("amount"));
	    	colCommision.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("commision")) ;
	    	if(CommonData.previewBillNo==0 || billService.getBillByBillno(CommonData.previewBillNo)==null)
	    	{
	    		new Alert(AlertType.ERROR,"No Bill To Preview").showAndWait();
	    		return;
	    	}
	    	Bill bill = billService.getBillByBillno(CommonData.previewBillNo);
	    	trList.addAll(bill.getTransaction());
	    	int srno=1;
	    	for(int i=0;i<trList.size();i++)
	    	{
	    		trList.get(i).setId(srno++);
	    	}
	    	table.setItems(trList);
	    	txtBillNo.setText(""+bill.getBillno());
	    	date.setValue(bill.getDate());
	    	txtCustomerName.setText(bill.getCustomer().getFname()+" "+bill.getCustomer().getMname()+" "+bill.getCustomer().getLname());
	    	txtSalesmanName.setText(bill.getEmployee().getFname()+" "+bill.getEmployee().getMname()+" "+bill.getEmployee().getLname());
	    	txtNetTotal.setText(""+bill.getNettotal());
	    	txtOtherChrgs.setText(""+bill.getOtherchargs());
	    	txtTransaportingChrgs.setText(""+bill.getTransportingchrges());
	    	txtGrandTotal.setText(""+(bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges()));
	    	txtPaid.setText(""+bill.getRecivedamount());
	    	
		}
	   

}
