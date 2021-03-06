package main.java.main.java.controller.report.itemsalereport;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.main.java.Main;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Item;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.reportEntity.ItemSaleReportPojo;
import main.java.main.java.hibernate.reportEntity.WeeklyItemSales;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PrintMonthlyItemSaleReport;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

public class WeeklyItemSalesReport implements Initializable {
	@FXML private AnchorPane mainPane;
	@FXML private DatePicker date;
    @FXML private TextField txtItemName;
    @FXML private Button btnShow;
    @FXML private Button btnShowAll;
    @FXML private Button btnShowChart;
    @FXML private Button btnClear;
	@FXML private Button btnPrint;
	@FXML private Button btnExit;
	@FXML private TableView<WeeklyItemSales> table;
    @FXML private TableColumn<WeeklyItemSales,Integer> colSrNo;
    @FXML private TableColumn<WeeklyItemSales,Long>    colDate;
    @FXML private TableColumn<WeeklyItemSales,String>  colBillNo;
    @FXML private TableColumn<WeeklyItemSales,Float>  colItemName;
    @FXML private TableColumn<WeeklyItemSales,String>  colQty;
    @FXML private TableColumn<?, ?> colUnit;
    @FXML private TableColumn<WeeklyItemSales,Float>  colRate;
    @FXML private TableColumn<WeeklyItemSales,Float>  colAmount;
	@FXML private TextField txtQyt;
	@FXML private TextField txtAmount;
	@FXML private TextField txtKG;
	@FXML private TextField txtNos;
	private BillService billService;
    private ItemService itemService;
    private ObservableList<WeeklyItemSales>list = FXCollections.observableArrayList();
    private List<Bill>billList = new ArrayList<Bill>();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date.setValue(LocalDate.now());
		billService = new BillServiceImpl();
		itemService = new ItemServiceImpl();
		new AutoCompletionTextFieldBinding<String>(txtItemName, SuggestionProvider.create(itemService.getAllItemNames()));
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("srno"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<>("billNo"));
		colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
		colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
		colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		table.setItems(list);
		btnPrint.setOnAction(e->{
			if(list.isEmpty())
			{
				new Alert(AlertType.ERROR,"No Data to Print").showAndWait();
				return;
			}
			List<ItemSaleReportPojo>itemList = new ArrayList<>();
			for(WeeklyItemSales sale:list)
			{
				itemList.add(new ItemSaleReportPojo(
						sale.getSrno(),
						sale.getBillNo(),
						sale.getDate(),
						sale.getItemName(),
						sale.getUnit(),
						sale.getQty(),
						sale.getRate(),
						sale.getAmount()
						));
			}
			new PrintMonthlyItemSaleReport(itemList,
					date.getValue().with(previousOrSame(MONDAY)),
					date.getValue().with(nextOrSame(SUNDAY)));
			new PrintFile().openFile("D:\\Software\\Prints\\ItemSalesReport.pdf");
		});
	}
	 @FXML
	    void btnClearAction(ActionEvent event) {
		 date.setValue(null);
		 txtItemName.setText("");
		 list.clear();
		 billList.clear();
	    }

	    @FXML
	    void btnShowAction(ActionEvent event) {
	    	if(date.getValue()==null)
	    	{
	    		new Alert(AlertType.ERROR,"Select Date").showAndWait();
	    		date.requestFocus();
	    		return;
	    	}
	    	if(txtItemName.getText().equals(""))
	    	{
	    		new Alert(AlertType.ERROR,"Enter Item Name").showAndWait();
	    		txtItemName.requestFocus();
	    		return;
	    	}
	    	list.clear();
	    	billList.clear();
			txtKG.setText(""+0.0f);
			txtAmount.setText(""+0.0f);
			txtNos.setText(""+0.0f);
			txtQyt.setText(""+0.0f);
	    	CommonData.weeklyItemSaleStickList.clear();
	    	billList.addAll(billService.getPeriodWiseBills(date.getValue().with(DayOfWeek.MONDAY),date.getValue().with(DayOfWeek.SUNDAY)));
	    	int sr=0;
	    	float qty=0,amount=0;
	    	if(billList.isEmpty())
	    	{
	    		new Alert(AlertType.ERROR,"No Data to show").showAndWait();
	    		return;
	    	}
	    	for(Bill bill:billList)
	    	{
	    		for(Transaction tr:bill.getTransaction())
	    		{
	    			if(tr.getItemname().equals(txtItemName.getText()))
	    			{
	    				list.add(new WeeklyItemSales(++sr, bill.getDate(),bill.getBillno(), tr.getItemname(), tr.getQuantity(), tr.getUnit(),tr.getRate(), tr.getAmount()));
	    				qty+=tr.getQuantity();
	    				amount+=tr.getAmount();
	    			}
	    		}
	    	}
	    	txtQyt.setText(""+qty);
			txtAmount.setText(""+amount);
	    }

	    @FXML
	    void btnShowAllAction(ActionEvent event) {
	    	if(date.getValue()==null)
	    	{
	    		new Alert(AlertType.ERROR,"Enter Date").showAndWait();
	    		date.requestFocus();
	    		return;
	    	}
			txtAmount.setText(""+0.0f);
			txtKG.setText(""+0.0f);
			txtNos.setText(""+0);
			txtQyt.setText(""+0.0f);
	    	list.clear();
	    	billList.clear();
	    	CommonData.weeklyItemSaleStickList.clear();
	    	billList.addAll(billService.getPeriodWiseBills(date.getValue().with(DayOfWeek.MONDAY),date.getValue().with(DayOfWeek.SUNDAY)));
	    	if(billList.isEmpty())
	    	{
	    		new Alert(AlertType.ERROR,"No Data to show").showAndWait();
	    		return;
	    	}
	    	int sr=0;
	    	WeeklyItemSales week =null;


			for(Bill bill:billList)
			{
				for(Transaction tr:bill.getTransaction())
				{
					addInList(new WeeklyItemSales(
							++sr,
							date.getValue(),
							0,
							tr.getItemname(),
							tr.getQuantity(),
							tr.getUnit(),
							tr.getRate(),
							tr.getAmount()
					));
				}
			}
			/*
	    	for(Item item:itemService.getAllItems())
	    	{
				float qty = getItemAllSale(item.getItemname());
	    		week = new WeeklyItemSales
						(++sr,
								date.getValue() ,
								0,
								item.getItemname(),
								qty,
								item.getUnit(),
								item.getRate(),
								item.getRate()*qty);
	    		amount+=week.getAmount();

				if(item.getUnit().equals("KG")) kg+=qty;
				else nos+=qty;
	    		list.add(week);

	    		if(item.getLabourCharges()>0)
	    		{
	    			CommonData.weeklyItemSaleStickList.add(week);
	    		}
	    		else
	    		{
	    			CommonData.weeklyItemSaleList.add(week);
	    		}
	    	}
			txtKG.setText(String.valueOf(kg));
			txtNos.setText(String.valueOf(nos));
			txtAmount.setText(String.valueOf(amount));

			 */
	    }

	private void addInList(WeeklyItemSales sale) {
		int index=-1;
		for(WeeklyItemSales tr:list)
		{
			if(tr.getItemName().equalsIgnoreCase(sale.getItemName())&& tr.getRate()==sale.getRate())
			{
				index=list.indexOf(tr);
				break;
			}
		}
		if(index==-1)
		{
			sale.setSrno(list.size()+1);
			list.add(sale);
			table.refresh();
		}
		else{
			list.get(index).setQty(list.get(index).getQty()+sale.getQty());
			list.get(index).setAmount(list.get(index).getAmount()+sale.getAmount());
		}

		if(sale.getUnit().equalsIgnoreCase("KG"))
			txtKG.setText(String.valueOf(Float.parseFloat(txtKG.getText())+sale.getQty()));
		else
			txtNos.setText(String.valueOf(Float.parseFloat(txtNos.getText())+sale.getQty()));

		txtAmount.setText(String.valueOf(Float.parseFloat(txtAmount.getText())+sale.getAmount()));

		//list.add(week);

		Item item = itemService.getItemByName(sale.getItemName());
		if(item.getLabourCharges()>0)
		{
			CommonData.weeklyItemSaleStickList.add(sale);
		}
		else
		{
			CommonData.weeklyItemSaleList.add(sale);
		}




	}

	@FXML
	    void btnShowChartAction(ActionEvent event) {
	    	if(CommonData.weeklyItemSaleStickList.isEmpty())
	    	{
	    		new Alert(AlertType.ERROR,"NO Data to Show in Chart").showAndWait();
	    		return;
	    	}
	    	try {
	    	Stage stage = new Stage();
			Parent root = FXMLLoader.load(Main.class.getResource("/view/report/charts/WeeklyItemSalesReport.fxml"));
			stage.setScene(new Scene(root));
			stage.setTitle("Weekly Items Sales Report Charts");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(((Node) event.getSource()).getScene().getWindow());
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent e) {
					
				}
			});
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }

	    private float getItemAllSale(String itemName)
	    {
	    	try {
	    		float qty=0;
				for(Bill bill:billList)
				{
					for(Transaction tr:bill.getTransaction())
					{
						if(tr.getItemname().equals(itemName))
						{
							qty+=tr.getQuantity();
						}
					}
				}
				return qty;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
	    }

}
