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

public class YearItemSalesReport implements Initializable {
	@FXML private AnchorPane MainFrame;
	@FXML private TextField txtItemName;
	@FXML private DatePicker date;
	@FXML private Button btnLoad;
	@FXML private Button btnShowAll;
	@FXML private Button btnReset;
	@FXML private Button btnPrint;
	@FXML private Button btnExit;
	@FXML private TableView<ItemSaleReportPojo> table;
	@FXML private TableColumn<ItemSaleReportPojo,Integer> colSrNo;
	@FXML private TableColumn<ItemSaleReportPojo,LocalDate> colDate;
	@FXML private TableColumn<ItemSaleReportPojo,Long> colBillNo;
	@FXML private TableColumn<ItemSaleReportPojo,String> colItemName;
	@FXML private TableColumn<ItemSaleReportPojo,String> colUnit;
	@FXML private TableColumn<ItemSaleReportPojo,Float> colRate;
	@FXML private TableColumn<ItemSaleReportPojo,Float> colQty;
	@FXML private TableColumn<ItemSaleReportPojo,Float> colAmount;
	@FXML private TextField txtQuantity;
	@FXML private TextField txtAmount;
	private BillService billService;
	private ItemService itemService;
	private AlertNotification notify;
	private ObservableList<ItemSaleReportPojo>list= FXCollections.observableArrayList();
 @Override
 public void initialize(URL location, ResourceBundle resources) {
	 date.setValue(LocalDate.now());
		billService = new BillServiceImpl();
		itemService = new ItemServiceImpl();
		notify = new AlertNotification();
	TextFields.bindAutoCompletion(txtItemName, itemService.getAllItemNames());
	colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
	colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
	colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
	colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
	colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
	colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
	colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
	colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
	table.setItems(list);

	btnLoad.setOnAction(e->load());
	btnShowAll.setOnAction(e->showAll());
	btnReset.setOnAction(e->{
		txtItemName.requestFocus();
		txtAmount.setText("");
		txtQuantity.setText("");
		date.setValue(LocalDate.now());
		list.clear();
	});


	}

	private void showAll() {
		if(date.getValue()==null)
		{
			notify.showErrorMessage("Enter Date");
			date.requestFocus();
			return;
		}
		list.clear();
		txtQuantity.setText("");
		txtAmount.setText("");
		int sr=0;
		List<Bill> billList = billService.getYearWiseBills(date.getValue().getYear());
		for(String name:itemService.getAllItemNames())
		{
			float qty=0,amount=0,rate=0;
			qty=getItemSale(billList,name);
			rate=itemService.getItemByName(name).getRate();

			list.add(new ItemSaleReportPojo(
					++sr,0,date.getValue(),name,itemService.getItemByName(name).getUnit(),qty,rate,qty*rate));
		}
	}
	private float getItemSale(List<Bill>billList,String name)
	{
		float qty=0;
		for(Bill bill:billList)
		{
			for(Transaction tr:bill.getTransaction())
			{
				if(tr.getItemname().equalsIgnoreCase(name))
				{
					qty+=tr.getQuantity();
				}
			}
		}
		return qty;
	}
	private void load() {
	 if(txtItemName.getText().isEmpty())
	 {
		 notify.showErrorMessage("Select Item Name");
		 txtItemName.requestFocus();
		 return;
	 }
	 if(date.getValue()==null)
	 {
		notify.showErrorMessage("Select Date");
		date.requestFocus();
		return;
	 }
	 List<Bill> billList = billService.getYearWiseBills(date.getValue().getYear());
	 loadData(billList,txtItemName.getText());
	}

	private void loadData(List<Bill> billList, String name) {
	 try {
		 int sr=0;
		 float qty=0,amt=0;
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
					 amt +=tr.getAmount();
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
