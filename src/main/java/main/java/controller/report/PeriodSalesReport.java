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
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.WeeklySalesReportPrint;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class PeriodSalesReport implements Initializable {
 @FXML private AnchorPane mainFrame;
 @FXML private Button btnLoad;
 @FXML private Button btnPreview;
 @FXML private Button btnReset;
 @FXML private Button btnExit;
 @FXML private Button btnPrint;
 @FXML private DatePicker startDate;
 @FXML private DatePicker endDate;
 @FXML private TableView<Bill> table;
 @FXML private TableColumn<Bill,Double> colSrNo;//otherchargs
 @FXML private TableColumn<Bill,LocalDate> colDate;
 @FXML private TableColumn<Bill,Long> colBillNo;
 @FXML private TableColumn<Bill,Double> colBillAmount;
 @FXML private TableColumn<Bill,Double> colPaidAmount;
 @FXML private TableColumn<Bill,String> colBankName;
 @FXML private TableColumn<Bill,String> colSalesmanName;
 @FXML private TextField txtBillAmount;
 @FXML private TextField txtTotalPaid;
 @FXML private TextField txtUnpaid;
 private ObservableList<Bill>billList =FXCollections.observableArrayList(); 
 private BillService billService;
 @Override
 public void initialize(URL location, ResourceBundle resources) {
	 startDate.setValue(LocalDate.now());
	 endDate.setValue(LocalDate.now());
		billService = new BillServiceImpl();
		colSrNo.setCellValueFactory(new PropertyValueFactory<Bill,Double>("otherchargs"));
		colDate.setCellValueFactory(new PropertyValueFactory<Bill,LocalDate>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<Bill,Long>("billno"));
		colBillAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("nettotal"));
		colPaidAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("recivedamount"));
		colBankName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedby"));
		colSalesmanName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedreff"));
		table.setItems(billList);

		btnPrint.setOnAction(e->{
			new WeeklySalesReportPrint(billList,startDate.getValue(),endDate.getValue());
			new PrintFile().openFile("D:\\Software\\Prints\\WeeklySalesReport.pdf");
		});
	}
@FXML
void btnExitAction(ActionEvent event) {
	mainFrame.setVisible(false);
}
@FXML
void btnLoadAction(ActionEvent event) {
	
	if(startDate.getValue()==null)
	{
		new Alert(AlertType.ERROR,"Enter Start Date ").showAndWait();
		startDate.requestFocus();
		return;
	}
	if(endDate.getValue()==null)
	{
		new Alert(AlertType.ERROR,"Enter End Date ").showAndWait();
		endDate.requestFocus();
		return;
	}
	if(startDate.getValue().compareTo(endDate.getValue())==1)
	{
		new Alert(AlertType.ERROR,"Start Date Must be Less Than End Date").showAndWait();
		return;
	}
	billList.clear();
	int sr=0;
	double totalAmount=0,totalPaid=0,totalUnpaid=0;
	//billList.addAll(billService.getPeriodWiseBills(date.getValue().with(DayOfWeek.MONDAY),date.getValue().with(DayOfWeek.SUNDAY)));
	billList.addAll(billService.getPeriodWiseBills(startDate.getValue(),endDate.getValue()));
	for(int i=0;i<billList.size();i++)
	{
		
		billList.get(i).setNettotal(
				billList.get(i).getNettotal()+
				billList.get(i).getOtherchargs()+
				billList.get(i).getTransportingchrges());
		billList.get(i).setOtherchargs((++sr));
		billList.get(i).setRecievedby(billList.get(i).getBank().getBankname());
		billList.get(i).setRecievedreff(billList.get(i).getEmployee().getFname()+" "+billList.get(i).getEmployee().getMname()+" "+billList.get(i).getEmployee().getLname());
		totalAmount = totalAmount+billList.get(i).getNettotal();
		totalPaid = totalPaid+billList.get(i).getRecivedamount();
		
	}
	totalUnpaid = totalAmount- totalPaid;
	txtBillAmount.setText(""+totalAmount);
	txtTotalPaid.setText(""+totalPaid);
	txtUnpaid.setText(""+totalUnpaid);
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

@FXML
void btnResetAction(ActionEvent event) {
	txtBillAmount.setText("");
	txtTotalPaid.setText("");
	txtUnpaid.setText("");
	billList.clear();
	startDate.setValue(LocalDate.now());
	endDate.setValue(LocalDate.now());
    }
}
