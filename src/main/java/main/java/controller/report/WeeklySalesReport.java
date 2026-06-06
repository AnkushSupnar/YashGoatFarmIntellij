package main.java.main.java.controller.report;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
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
import main.java.main.java.hibernate.entities.BillPayment;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.WeeklySalesReportPrint;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class WeeklySalesReport implements Initializable {

 @FXML private AnchorPane MainFrame;
 @FXML private Button btnLoad;
 @FXML private Button btnPreview;
 @FXML private Button btnReset;
 @FXML private Button btnExit;
 @FXML private Button btnPrint;

	@FXML private DatePicker date;
 @FXML private TableView<Bill> table;
 @FXML private TableColumn<Bill,Double> colSrNo;//otherchargs
 @FXML private TableColumn<Bill,LocalDate> colDate;
 @FXML private TableColumn<Bill,Long> colBillNo;
 @FXML private TableColumn<Bill,Double> colBillAmount;
 @FXML private TableColumn<Bill,Double> colPaidAmount;
 @FXML private TableColumn<Bill,String> colBankName;
 @FXML private TableColumn<Bill,String> colSalesmanName; @FXML private TextField txtBillAmount;
 @FXML private TextField txtTotalPaid;
 @FXML private TextField txtUnpaid;
 @FXML private TableView<ModeTotal> tableModeBreakdown;
 @FXML private TableColumn<ModeTotal,String> colModeName;
 @FXML private TableColumn<ModeTotal,Number> colModeAmount;
 private ObservableList<Bill>billList =FXCollections.observableArrayList();
 private ObservableList<ModeTotal> modeBreakdownList = FXCollections.observableArrayList();
 private BillService billService;

	public static class ModeTotal {
		private final String mode;
		private final double amount;
		public ModeTotal(String mode, double amount) { this.mode = mode; this.amount = amount; }
		public String getMode() { return mode; }
		public double getAmount() { return amount; }
	}
 @Override
 public void initialize(URL location, ResourceBundle resources) {
	 date.setValue(LocalDate.now());
		billService = new BillServiceImpl();
		colSrNo.setCellValueFactory(new PropertyValueFactory<Bill,Double>("otherchargs"));
		colDate.setCellValueFactory(new PropertyValueFactory<Bill,LocalDate>("date"));
		colBillNo.setCellValueFactory(new PropertyValueFactory<Bill,Long>("billno"));
		colBillAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("nettotal"));
		colPaidAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("recivedamount"));
		colBankName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedby"));
		colSalesmanName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedreff"));
		table.setItems(billList);

		colModeName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMode()));
		colModeAmount.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().getAmount()));
		tableModeBreakdown.setItems(modeBreakdownList);

		btnPrint.setOnAction(e->{
			if(billList.size()==0) return;
			new WeeklySalesReportPrint(billList,date.getValue().with(DayOfWeek.MONDAY),date.getValue().with(DayOfWeek.SUNDAY));
			new PrintFile().openFile( "D:\\Software\\Prints\\WeeklySalesReport.pdf");
		});
	}
@FXML
void btnExitAction(ActionEvent event) {
	MainFrame.setVisible(false);
}
@FXML
void btnLoadAction(ActionEvent event) {
	if(date.getValue()==null)
	{
		new Alert(AlertType.ERROR,"Select Any Date from week").showAndWait();
		return;
	}
	billList.clear();
	modeBreakdownList.clear();
	int sr=0;
	double totalAmount=0,totalPaid=0,totalUnpaid=0;
	Map<String, Double> modeMap = new LinkedHashMap<>();
	billList.addAll(billService.getPeriodWiseBills(date.getValue().with(DayOfWeek.MONDAY),date.getValue().with(DayOfWeek.SUNDAY)));

	for(int i=0;i<billList.size();i++)
	{
		Bill b = billList.get(i);
		accumulateModes(b, modeMap);
		b.setNettotal(b.getNettotal()+b.getOtherchargs()+b.getTransportingchrges());
		b.setOtherchargs((++sr));
		b.setRecievedby(buildPaymentModesString(b));
		b.setRecievedreff(b.getEmployee().getFname()+" "+b.getEmployee().getMname()+" "+b.getEmployee().getLname());
		totalAmount = totalAmount+b.getNettotal();
		totalPaid = totalPaid+b.getRecivedamount();
	}
	for (Map.Entry<String, Double> e : modeMap.entrySet()) {
		modeBreakdownList.add(new ModeTotal(e.getKey(), e.getValue()));
	}
	totalUnpaid = totalAmount- totalPaid;
	txtBillAmount.setText(""+totalAmount);
	txtTotalPaid.setText(""+totalPaid);
	txtUnpaid.setText(""+totalUnpaid);
 }

	private String buildPaymentModesString(Bill bill) {
		if (bill.getPayments() != null && !bill.getPayments().isEmpty()) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (BillPayment p : bill.getPayments()) {
				if (!first) sb.append(", ");
				first = false;
				String bn = p.getBank() != null ? p.getBank().getBankname() : "-";
				sb.append(bn).append(":").append(p.getAmount());
			}
			return sb.toString();
		}
		if (bill.getBank() != null && bill.getRecivedamount() > 0) {
			return bill.getBank().getBankname() + ":" + bill.getRecivedamount();
		}
		return bill.getBank() != null ? bill.getBank().getBankname() : "";
	}

	private void accumulateModes(Bill bill, Map<String, Double> modeMap) {
		if (bill.getPayments() != null && !bill.getPayments().isEmpty()) {
			for (BillPayment p : bill.getPayments()) {
				if (p.getBank() == null) continue;
				modeMap.merge(p.getBank().getBankname(), (double) p.getAmount(), Double::sum);
			}
		} else if (bill.getBank() != null && bill.getRecivedamount() > 0) {
			modeMap.merge(bill.getBank().getBankname(), (double) bill.getRecivedamount(), Double::sum);
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

@FXML
void btnResetAction(ActionEvent event) {
	txtBillAmount.setText("");
	txtTotalPaid.setText("");
	txtUnpaid.setText("");
	billList.clear();
	modeBreakdownList.clear();
	date.setValue(LocalDate.now());
    }
}
