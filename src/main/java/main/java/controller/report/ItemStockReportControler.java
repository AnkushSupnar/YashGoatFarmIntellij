package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.ItemStock;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.reportEntity.ItemStockReport;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemStockService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemStockServiceImpl;
import main.java.main.java.print.ItemStockReport2;
import main.java.main.java.print.PrintFile;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ItemStockReportControler implements Initializable {
	@FXML private TableView<ItemStockReport> table;
    @FXML private TableColumn<ItemStockReport,Integer> colSrNo;
    @FXML private TableColumn<ItemStockReport,String> colItemName;
    @FXML private TableColumn<ItemStockReport, Float> colSoldQty;
    @FXML private TableColumn<ItemStockReport,Float> colQty;
    @FXML private TableColumn<ItemStockReport,String> colUni;
    @FXML private Button btnPrint;
    
    @FXML private DatePicker dateStart;
    @FXML private DatePicker dateEnd;
    @FXML private Button btnShow;

    
    private ObservableList<ItemStock>list = FXCollections.observableArrayList();
    private ItemStockService service;
    private BillService billService;
    List<Bill>billList;
    private ObservableList<ItemStockReport> stockList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		service = new ItemStockServiceImpl();
		billService = new BillServiceImpl();
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName")); 
		colSoldQty.setCellValueFactory(new PropertyValueFactory<>("soldQty"));
		colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
		colUni.setCellValueFactory(new PropertyValueFactory<>("unit"));
		//list.addAll(service.getAllItemStock());
		
		table.setItems(stockList);
		

	}
	@FXML
    void btnPrintAction(ActionEvent event) {
		//new ItemStockReportPrint();
		if(stockList.size()!=0) {
		new ItemStockReport2(dateStart.getValue(), dateEnd.getValue(), stockList);
		new PrintFile().openFile("D:\\Software\\Prints\\ItemStock.pdf");
		}
    }
	private float getSoldQty(String item)
	{
		try {
			float qty=0;
			for(Bill bill:billList)
			{
				for(Transaction tr:bill.getTransaction())
				{
					if(tr.getItemname().equals(item))
					{
						qty=qty+tr.getQuantity();
					}
				}
			}
			return qty;
		} catch (Exception e) {
			new Alert(AlertType.ERROR,"Error in Loading Sold Stock").show();
			return 0;
		}
	}
	 @FXML
	    void btnShowAction(ActionEvent event) {
		 if(dateStart.getValue()==null)
		 {
			 new Alert(AlertType.ERROR,"Enter Start Date").showAndWait();
			 dateStart.requestFocus();
			 return;
		 }
		 if(dateEnd.getValue()==null)
		 {
			 new Alert(AlertType.ERROR,"Enter Enter Date").showAndWait();
			 dateEnd.requestFocus();
			 return;
		 }
		 if(dateStart.getValue().compareTo(dateEnd.getValue())>0)
		 {
			 new Alert(AlertType.ERROR,"Start Date Must be smaller than End Date").showAndWait();
			 dateEnd.requestFocus();
			 return;
		 }
		 list.clear();
		 stockList.clear();
		 list.addAll(service.getAllItemStock());
		 billList = billService.getPeriodWiseBills(dateStart.getValue(),dateEnd.getValue());
		 for(ItemStock stock:list)
		 {
			 ItemStockReport rt = new ItemStockReport(
					 stock.getItemname(),
					 getSoldQty(stock.getItemname()),
					 stock.getQuantity(),
					 stock.getUnit());
			 rt.setId(stock.getId());
			 stockList.add(rt);
		 }
		 
	 }

}
