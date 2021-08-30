package main.java.main.java.controller.report;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.PaymentReciept;
import main.java.main.java.hibernate.service.service.PaymentRecieptService;
import main.java.main.java.hibernate.service.serviceImpl.PaymentRecieptServiceImpl;
import main.java.main.java.print.PaymentReceiptReportPrint;
import main.java.main.java.print.PrintFile;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.*;

public class PaymentReceipReportController implements Initializable {

	 	@FXML private AnchorPane mainPane;
	 	@FXML private DatePicker date;
	    @FXML private Button btnShow;
	    @FXML private Button btnWeekly;
 	    @FXML private Button btnMonthly;
	    @FXML private Button btnYear;
	    @FXML private Button btnAll;
	    @FXML private Button btnPrint;
	    @FXML private Button btnHome;
	    @FXML private TableView<PaymentReciept> table;
	    @FXML private TableColumn<PaymentReciept,Long> colSrno;
	    @FXML private TableColumn<PaymentReciept,LocalDate> colDate;	    
	    @FXML private TableColumn<PaymentReciept,String> colName;
	    @FXML private TableColumn<PaymentReciept,Float> colAmount;
	    @FXML private TableColumn<PaymentReciept,String> colNote;
	    @FXML private TableColumn<PaymentReciept,String> colBankName;
	    @FXML private TextField txtTotal;
	    
	    private ObservableList<PaymentReciept>list = FXCollections.observableArrayList();
	    private PaymentRecieptService service;
	    private AlertNotification notify;
	    private LocalDate start,end; 
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date.setValue(LocalDate.now());
		notify = new AlertNotification();
		service = new PaymentRecieptServiceImpl();
		colSrno.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
		colBankName.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBank().getBankname()));
		list.addAll(service.getDateWisePaymentReciept(date.getValue()));
		table.setItems(list);
		txtTotal.setText(""+(
				list.stream().mapToDouble(item->item.getAmount()).sum()
				));
		btnShow.setOnAction(e->show(e));
		btnWeekly.setOnAction(e->show(e));
		btnMonthly.setOnAction(e->show(e));
		btnYear.setOnAction(e->show(e));
		btnAll.setOnAction(e->show(e));
		btnPrint.setOnAction(e->{
			if(list.isEmpty())
				return;
			new PaymentReceiptReportPrint(list, start,end);
			new PrintFile().openFile("D:\\Software\\Prints\\PaymentReceiptReport.pdf");
		});
	}
	
	private void show(Event event)
	{
		try {
			if(date==null)
			{
				notify.showErrorMessage("Delect date");
				this.date.requestFocus();
				return;
			}
			list.clear();
			Button button = (Button) event.getSource();			
			if(button.getId().equals("btnShow"))
			{
				list.addAll(service.getDateWisePaymentReciept(date.getValue()));
				start = date.getValue();
				end = date.getValue();
			}
			else if(button.getId().equals("btnWeekly"))
			{
				list.addAll(service.getDatePeriodPaymentReciept(date.getValue().with(previousOrSame(MONDAY)), date.getValue().with(nextOrSame(SUNDAY))));
				start = date.getValue().with(previousOrSame(MONDAY));
				end = date.getValue().with(nextOrSame(SUNDAY));
			}
			else if(button.getId().equals("btnMonthly"))
			{
				list.addAll(service.getDatePeriodPaymentReciept(date.getValue().with(firstDayOfMonth()),date.getValue().with(lastDayOfMonth())));
				start=date.getValue().with(firstDayOfMonth());
				end=date.getValue().with(lastDayOfMonth());
			}
			else if(button.getId().equals("btnYear"))
			{
				list.addAll(service.getDatePeriodPaymentReciept(date.getValue().with(firstDayOfYear()),date.getValue().with(lastDayOfYear())));
				start =date.getValue().with(firstDayOfYear()); 
				end=date.getValue().with(lastDayOfYear());
			}
			else if(button.getId().equals("btnAll"))
			{
				list.addAll(service.getAllPaymentReciept());
				start = null;
				end=null;
			}
			txtTotal.setText(""+(
					list.stream().mapToDouble(item->item.getAmount()).sum()
					));
		} catch (Exception e) {
			notify.showErrorMessage("Error in Showing Records "+e.getMessage());
		}
	}
	
	
	
	
	private void show() {
		try {
			if(date.getValue()==null)
			{
				notify.showErrorMessage("Delect date");
				date.requestFocus();
				return;
			}
			list.clear();
			list.addAll(service.getDateWisePaymentReciept(date.getValue()));
			txtTotal.setText(""+(
					list.stream().mapToDouble(item->item.getAmount()).sum()
					));
		} catch (Exception e) {
			notify.showErrorMessage("Error in Showing Records "+e.getMessage());
		}
	}

}
