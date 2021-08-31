package main.java.main.java.controller.report.itemsalereport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.reportEntity.ItemSaleReportPojo;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class PeriodItemSalesReport implements Initializable {
	@FXML private AnchorPane mainFrame;
	@FXML private TextField txtItemName;
	@FXML private DatePicker startDate;
	@FXML private DatePicker endDate;
	@FXML private Button btnLoad;
	@FXML private Button btnShowAll;
	@FXML private Button btnPrint;
	@FXML private Button btnReset;
	@FXML private Button btnExit;
	@FXML private TableView<ItemSaleReportPojo> table;
	@FXML private TableColumn<ItemSaleReportPojo,Integer> colSrNo;
	@FXML private TableColumn<ItemSaleReportPojo,LocalDate> colDate;
	@FXML private TableColumn<ItemSaleReportPojo,Long> colBillNo;
	@FXML private TableColumn<ItemSaleReportPojo,String> colItemName;
	@FXML private TableColumn<ItemSaleReportPojo,String> colUnit;
	@FXML private TableColumn<ItemSaleReportPojo,Float> colRate;
	@FXML private TableColumn<ItemSaleReportPojo,Float> colQuantity;
	@FXML private TableColumn<ItemSaleReportPojo,Float> colAmount;
	@FXML private TextField txtQuantity;
	@FXML private TextField txtAmount;
 private BillService billService;
 private ItemService itemService;
 private AlertNotification notify;
 private ObservableList<ItemSaleReportPojo>list=FXCollections.observableArrayList();
 @Override
 public void initialize(URL location, ResourceBundle resources) {
	 itemService = new ItemServiceImpl();
	 billService = new BillServiceImpl();
	 notify = new AlertNotification();
	 startDate.setValue(LocalDate.now());
	 endDate.setValue(LocalDate.now());
	 TextFields.bindAutoCompletion(txtItemName,itemService.getAllItemNames());
	 colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
	 colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
	 colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
	 colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
	 colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
	 colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
	 colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
	 colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
	table.setItems(list);
	 btnLoad.setOnAction(e->load());
	 btnShowAll.setOnAction(e->showAll());
	 btnReset.setOnAction(e->{
		 txtItemName.setText("");
		 startDate.setValue(LocalDate.now());
		 endDate.setValue(LocalDate.now());
		 list.clear();
		 txtQuantity.setText("");
		 txtAmount.setText("");
	 });
	 btnExit.setOnAction(e->mainFrame.setVisible(false));
	}

	private void showAll() {
		if(startDate.getValue()==null)
		{
			notify.showErrorMessage("Enter Starting date");
			startDate.requestFocus();
			return;
		}
		if(endDate.getValue()==null)
		{
			notify.showErrorMessage("Enter ending date");
			endDate.requestFocus();
			return;
		}
		if(startDate.getValue().compareTo(endDate.getValue())>0)
		{
			notify.showErrorMessage("Starting date must be smaller than end date");
			startDate.requestFocus();
			return;
		}
		list.clear();
		txtQuantity.setText("");
		txtAmount.setText("");
		List<Bill>billList = billService.getPeriodWiseBills(startDate.getValue(),endDate.getValue());
		int sr=0;
		for(String name:itemService.getAllItemNames())
		{
			float qty = getItemQty(billList,name);
			float rate = itemService.getItemByName(name).getRate();
			list.add(new ItemSaleReportPojo(
					++sr,
					0,
					startDate.getValue(),
					name,
					itemService.getItemByName(name).getUnit(),
					qty,
					rate,
					qty*rate));
		}
	}

	private float getItemQty(List<Bill> billList, String name) {
	 try {

		 float qty=0;
		 for(Bill bill:billList)
		 {
			 for(Transaction tr:bill.getTransaction())
			 {
				 if(tr.getItemname().equals(name))
				 {
					 qty+=tr.getQuantity();
				 }
			 }
		 }
		 return qty;
	 }catch (Exception e)
	 {
		 notify.showErrorMessage("Error in loading data "+e.getMessage());
		 return 0;
	 }
	}

	private void load() {
	 if(txtItemName.getText().isEmpty())
	 {
		txtItemName.requestFocus();
		notify.showErrorMessage("Select Item Name");
		return;
	 }
	 if(startDate.getValue()==null)
	 {
		 notify.showErrorMessage("Enter Starting date");
		 startDate.requestFocus();
		 return;
	 }
		if(endDate.getValue()==null)
		{
			notify.showErrorMessage("Enter ending date");
			endDate.requestFocus();
			return;
		}
		if(startDate.getValue().compareTo(endDate.getValue())>0)
		{
			notify.showErrorMessage("Starting date must be smaller than end date");
			startDate.requestFocus();
			return;
		}
		list.clear();
		List<Bill>billList = billService.getPeriodWiseBills(startDate.getValue(),endDate.getValue());
		getData(billList,txtItemName.getText());
	}

	private void getData(List<Bill> billList, String name) {
	 try {
		 int sr=0;
		 float amt=0,qty=0;
		for(Bill bill:billList)
		{
			for(Transaction tr:bill.getTransaction())
			{
				if(tr.getItemname().equals(name))
				{
					list.add(new ItemSaleReportPojo(
							++sr,
							bill.getBillno(),
							bill.getDate(),
							tr.getItemname(),
							tr.getUnit(),
							tr.getQuantity(),
							tr.getRate(),
							tr.getAmount()));
					amt+=tr.getAmount();
					qty+=tr.getQuantity();
				}
			}
		}
		txtAmount.setText(""+amt);
		txtQuantity.setText(""+qty);
	 }catch (Exception e)
	 {
		 notify.showErrorMessage("Error in loading data "+e.getMessage());
	 }
	}

}
