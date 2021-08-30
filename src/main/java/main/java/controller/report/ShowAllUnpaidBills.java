package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class ShowAllUnpaidBills implements Initializable {

	 	@FXML private AnchorPane mainPane;
	    @FXML private TableView<Bill> table;
	    @FXML private TableColumn<Bill,Integer> colSrNo;
	    @FXML private TableColumn<Bill,LocalDate> colDate;
	    @FXML private TableColumn<Bill,Long> colBillNo;
	    @FXML private TableColumn<Bill,String> colCustomerName;
	    @FXML private TableColumn<Bill,Double> colBillAmount;
	    @FXML private TableColumn<Bill,Double> colPaidAmount;
	    @FXML private TableColumn<Bill,Double> colRemainingAmount;
	    @FXML private TableColumn<Bill,String> colSalesmanName;
	    
	    @FXML private TextField txtBill;
	    @FXML private TextField txtRecieved;
	    @FXML private TextField txtRemaining;
	    private BillService service;
	    private ObservableList<Bill>list = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		service = new BillServiceImpl();
		list.addAll(service.getAllUnpaidBills());
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("transportingchrges")); 
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
		colCustomerName.setCellValueFactory(new PropertyValueFactory<>("recievedby"));
		colBillAmount.setCellValueFactory(new PropertyValueFactory<>("nettotal"));
		colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("recivedamount"));
		colRemainingAmount.setCellValueFactory(new PropertyValueFactory<>("otherchargs"));
		colSalesmanName.setCellValueFactory(new PropertyValueFactory<>("recievedreff"));
		int sr=0;
		double bill=0,paid=0,remaining=0;
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setNettotal(
					list.get(i).getNettotal()+
					list.get(i).getOtherchargs()+
					list.get(i).getTransportingchrges());
			bill = bill+list.get(i).getNettotal();
			paid = paid+list.get(i).getRecivedamount();
			
			list.get(i).setOtherchargs(list.get(i).getNettotal()-list.get(i).getRecivedamount());
			list.get(i).setRecievedby(
					list.get(i).getCustomer().getFname()+" "+
					list.get(i).getCustomer().getMname()+" "+
					list.get(i).getCustomer().getLname());
			list.get(i).setRecievedreff(
					list.get(i).getEmployee().getFname()+" "+
							list.get(i).getEmployee().getMname()+" "+
							list.get(i).getEmployee().getLname());
			list.get(i).setTransportingchrges(++sr);
		}
		
		remaining = bill-paid;
		
		table.setItems(list);
		txtBill.setText(""+bill);
		txtRecieved.setText(""+paid);
		txtRemaining.setText(""+remaining);
	}

}
