package main.java.main.java.controller.report;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Item;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PrintSalemanItemSalesReport;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.*;

public class SalesmanItemSalesReportController implements Initializable {

	@FXML private AnchorPane mainPane;
    @FXML private ComboBox<String> cmbSalesman;
    @FXML private TextField txtItem;
    @FXML private DatePicker dateStart;
    @FXML private DatePicker dateEnd;
    @FXML private Button btnShow;
    @FXML private Button btnWeek;
    @FXML private Button btnMonth;
    @FXML private Button btnYear;
    @FXML private Button btnAll;
    @FXML private Button btnPrint;
    @FXML private Button btnReset;
    @FXML private Button btnExit;
    @FXML private TableView<Transaction> table;
    @FXML private TableColumn<Transaction,Integer> colSrNo;
    @FXML private TableColumn<Transaction,String> colDate;
    @FXML private TableColumn<Transaction,String> collBillNo;
    @FXML private TableColumn<Transaction,String> colItem;
    @FXML private TableColumn<Transaction,Float> colQuantity;
    @FXML private TableColumn<Transaction,Float> colRate;
    @FXML private TableColumn<Transaction,Float> colAmount;
	@FXML private TextField txtTotalKg;
	@FXML private TextField txtTotalNos;
    @FXML private TextField txtQty;
    @FXML private TextField txtAmount;

    private BillService billService;
    private EmployeeService employeeService;
    private AlertNotification notify;
    private ItemService itemService;
    private ObservableList<Transaction>list = FXCollections.observableArrayList();
    private List<Bill>billList;
    int empid;
   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		empid=0;
		billService = new BillServiceImpl();
		itemService = new ItemServiceImpl();
		notify = new AlertNotification();
		employeeService = new EmployeeServiceImpl();
		billList = new ArrayList<Bill>();
        cmbSalesman.getItems().addAll(employeeService.getAllSalesmanNames());
        TextFields.bindAutoCompletion(txtItem,itemService.getAllItemNames());
	
        colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(cellData->new SimpleStringProperty(""+cellData.getValue().getBill().getDate().toString()));
		collBillNo.setCellValueFactory(cellData->new SimpleStringProperty(Long.toString(cellData.getValue().getBill().getBillno())));
		colItem.setCellValueFactory(new PropertyValueFactory<>("itemname"));
		colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		table.setItems(list);
		
		btnShow.setOnAction(e->show(e));
		btnYear.setOnAction(e->show(e));
		btnWeek.setOnAction(e->show(e));
		btnMonth.setOnAction(e->show(e));
		btnAll.setOnAction(e->show(e));
		btnReset.setOnAction(e->{
			cmbSalesman.getSelectionModel().clearSelection();
			txtItem.setText("");
			dateStart.setValue(null);
			dateEnd.setValue(null);
			list.clear();
			txtAmount.setText("");
			txtQty.setText("");
		});
		btnExit.setOnAction(e->mainPane.setVisible(false));
        btnPrint.setOnAction(e->print());
		
	}

    private void print() {
        if(list.size()==0)
        {notify.showErrorMessage("No data to print"); return;}
        if(list.get(0).getBill().getBillno()==0)
        {
            //for all items
            new PrintSalemanItemSalesReport(list,cmbSalesman.getSelectionModel().getSelectedItem(),dateStart.getValue(),dateEnd.getValue());
			new PrintFile().openFile("D:\\Software\\Prints\\SalesmanItemSalesReport.pdf");
			System.out.println("Printing done");
        }
        else{
            //for individual item
            new PrintSalemanItemSalesReport(list,cmbSalesman.getSelectionModel().getSelectedItem(),dateStart.getValue(),dateEnd.getValue());
			new PrintFile().openFile("D:\\Software\\Prints\\SalesmanItemSalesReport.pdf");
			System.out.println("Printing done");
        }
    }

    private void show(ActionEvent e) {
		Button button = (Button) e.getSource();
		txtQty.setText(""+0.0f);
		if(button.getId().equals("btnShow"))
		{
			if(!validate())
			{
				return;
			}
			if(dateStart.getValue()==null)
			{
				notify.showErrorMessage("Select Start date");
				dateStart.requestFocus();
				calculateNosKg();
				return;
			}
			if(dateStart.getValue()!=null && dateEnd.getValue()==null)
			{
				//date wise bills
				billList.clear();
				list.clear();
				billList.addAll(
						billService.getDateWiseSalesmanBills(empid,dateStart.getValue()));
				if(billList.size()==0)
				{
					notify.showErrorMessage("No data to show");
					return;
				}
				if(txtItem.getText().isEmpty())
				{
					showAllItem(billList);
				}else {
					showTable(billList);
				}
				calculateNosKg();
				return;
			}
			if(dateStart.getValue()!=null && dateEnd.getValue()!=null)
			{
				//period bills
				billList.clear();
				list.clear();
				billList.addAll(billService.getPeriodWiseSalesmanBills(empid, dateStart.getValue(), dateEnd.getValue()));
                if(txtItem.getText().isEmpty())
                {
                    showAllItem(billList);
                }else {
                    showTable(billList);
                }
				calculateNosKg();
				return;
			}

		}
		if(button.getId().equals("btnWeek"))
		{
			if(!validate())
			{
				return;
			}
			if(dateStart.getValue()==null)
			{
				notify.showErrorMessage("Select Starting Date");
				dateStart.requestFocus();
				return;
			}
			billList.clear();
			list.clear();
			billList.addAll(billService.getPeriodWiseSalesmanBills(empid, dateStart.getValue().with(previousOrSame(MONDAY)), dateStart.getValue().with(nextOrSame(SUNDAY))));
			if(txtItem.getText().isEmpty())
			{
				showAllItem(billList);
			}else {
				showTable(billList);
			}
			calculateNosKg();
			return;
		}
		if(button.getId().equals("btnMonth"))
		{
			if(!validate())
			{
				return;
			}
			if(dateStart.getValue()==null)
			{
				notify.showErrorMessage("Select Starting Date");
				dateStart.requestFocus();
				return;
			}
			list.clear();
			billList.clear();
			billList.addAll(billService.getPeriodWiseSalesmanBills(empid, dateStart.getValue().with(firstDayOfMonth()),dateStart.getValue().with(lastDayOfMonth())));
			if(txtItem.getText().isEmpty())
			{
				showAllItem(billList);
			}else {
				showTable(billList);
			}
			calculateNosKg();
			return;
		}
		if(button.getId().equals("btnYear"))
		{
			if(!validate())
			{
				return;
			}
			if(dateStart.getValue()==null)
			{
				notify.showErrorMessage("Select Starting Date");
				dateStart.requestFocus();
				return;
			}
			list.clear();
			billList.clear();
			billList.addAll(billService.getPeriodWiseSalesmanBills(empid, dateStart.getValue().with(firstDayOfYear()),dateStart.getValue().with(lastDayOfYear())));
			if(txtItem.getText().isEmpty())
			{
				showAllItem(billList);
			}else {
				showTable(billList);
			}
			calculateNosKg();
			return;
		}
		if(button.getId().equals("btnAll"))
		{
			if(!validate())
			{
				return;
			}
			billList.clear();
			list.clear();
			dateStart.setValue(LocalDate.now());
			billList.addAll(billService.getSalesmanAllBills(empid));

			if(txtItem.getText().isEmpty())
			{
				showAllItem(billList);
			}else {
				showTable(billList);
			}
			calculateNosKg();
			return;
		}
		
	}

	private void showAllItem(List<Bill> billList) {
	try {
		txtAmount.setText("");
		for(String name:itemService.getAllItemNames())
		{
			list.add(getItemWise(billList,name));
		}

	}catch(Exception e)
	{
		notify.showErrorMessage("Error in loading Item List"+e.getMessage());
		e.printStackTrace();
	}
	}

	private Transaction getItemWise(List<Bill> billList, String name) {
		Transaction t = new Transaction();
		float qty=0;
		float amt=0;

        Item item = itemService.getItemByName(name);
		for(Bill bill :billList)
		{
			for(Transaction tr:bill.getTransaction())
			{
				if(tr.getItemname().equals(name))
				{
					qty+=tr.getQuantity();
					amt+=tr.getAmount();
				}

			}
		}
		Bill b = new Bill();
		b.setDate(dateStart.getValue());
		b.setBillno(0);
        t.setId(list.size()+1);
        t.setAmount(amt);
        t.setBill(b);
        t.setItemname(name);
        t.setCommision(0);
        t.setQuantity(qty);
        t.setRate(item.getRate());
        t.setUnit(item.getUnit());
		if(txtAmount.getText().isEmpty()) txtAmount.setText(""+0.0);
		txtAmount.setText(String.valueOf(Float.parseFloat(txtAmount.getText())+t.getAmount()));

        return t;
	}

	private void showTable(List<Bill> billList2) {
		try {
			int sr=0;
			list.clear();
			float qty=0,amount=0;
			for(Bill bill:billList)
			{
				for(Transaction tr:bill.getTransaction())
				{
					if(tr.getItemname().equals(txtItem.getText().trim())) {
						tr.setId(++sr);
						qty+=tr.getQuantity();
						amount+=tr.getAmount();
					list.add(tr);
					}
				}
			}
			txtQty.setText(""+qty);
			txtAmount.setText(""+amount);
		} catch (Exception e) {
			e.printStackTrace();
			notify.showErrorMessage("Error in Loading Data "+e.getMessage());
		}
		
	}
	private boolean validate() {
		if(cmbSalesman.getSelectionModel().getSelectedItem()==null)
		{
			notify.showErrorMessage("Select Salesman!");
			cmbSalesman.requestFocus();
			return false;
		}
//		if(txtItem.getText().isEmpty())
//		{
//			notify.showErrorMessage("Enter Item Name");
//			txtItem.requestFocus();
//			return false;
//		}
		empid = employeeService.getEmployeeByName(cmbSalesman.getSelectionModel().getSelectedItem()).getId();
		return true;
	}

	void calculateNosKg()
	{
		txtTotalNos.setText(""+0.0f);
		txtTotalKg.setText(""+0.0f);
		if(!list.isEmpty()){
			for(Transaction tr:list)
			{
				if(tr.getUnit().equalsIgnoreCase("kg"))
				 txtTotalKg.setText(
						 String.valueOf(Float.parseFloat(txtTotalKg.getText())+tr.getQuantity())
				 );
				else
					txtTotalNos.setText(
							String.valueOf(Float.parseFloat(txtTotalNos.getText())+tr.getQuantity())
					);
			}
		}

	}
}
