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
import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.BillPayment;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.DailySalesReportPrint;
import main.java.main.java.print.PrintFile;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class DailySalesReport implements Initializable {

	  @FXML private AnchorPane mainFrame;
	  @FXML private DatePicker date;
	  @FXML private CheckBox checkCash;
	  @FXML private TableView<Bill> table;
	  @FXML private TableColumn<Bill,Double> colSrNo;//otherchargs
	  @FXML private TableColumn<Bill,Long> colBillNo;
	  @FXML private TableColumn<Bill,Double> colBillAmount;//netTotal
	  @FXML private TableColumn<Bill,Double> colPaidAmount;
	  @FXML private TableColumn<Bill,String> colBankName;//Recieved By
	  @FXML private TableColumn<Bill,String> colSalesmanName;//recievedReff
	  @FXML private Button btnLoad;
	  @FXML private TextField txtBillAmount;
	  @FXML private TextField txtTotalPaid;
	  @FXML private TextField txtUnpaid;
	  @FXML private Button btnPreview;
	  @FXML private Button btnReset;
	  @FXML private Button btnExit;
	  @FXML private Button btnPrint;

	  @FXML private TableView<ModeTotal> tableModeBreakdown;
	  @FXML private TableColumn<ModeTotal,String> colModeName;
	  @FXML private TableColumn<ModeTotal,Number> colModeAmount;

	private ObservableList<Bill>billList =FXCollections.observableArrayList();
	private ObservableList<ModeTotal> modeBreakdownList = FXCollections.observableArrayList();
	  private BillService billService;
	  boolean cash;

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
			colBillNo.setCellValueFactory(new PropertyValueFactory<Bill,Long>("billno"));
			colBillAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("nettotal"));
			colPaidAmount.setCellValueFactory(new PropertyValueFactory<Bill,Double>("recivedamount"));
			colBankName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedby"));
			colSalesmanName.setCellValueFactory(new PropertyValueFactory<Bill,String>("recievedreff"));
			table.setItems(billList);

			colModeName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getMode()));
			colModeAmount.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().getAmount()));
			tableModeBreakdown.setItems(modeBreakdownList);

			loadData();
			checkCash.setOnAction(e->{
				if(checkCash.isSelected())
				{
				cash=true;
					}
				else
				{
					cash=false;
				}
			});

			btnPrint.setOnAction(e->{
				if(billList.size()==0) return;
				new DailySalesReportPrint(billList);
				new PrintFile().openFile("D:\\Software\\Prints\\DailySalesReport.pdf");
			});
		}
	  @FXML void btnExitAction(ActionEvent event) {

		  mainFrame.setVisible(false);
	    }

	    @FXML
	    void btnLoadAction(ActionEvent event) {
	    	loadData();
	    	
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

	    private void loadData()
	    {
	    	if(date.getValue()==null)
	    	{
	    		new Alert(AlertType.ERROR,"Enter Date").showAndWait();
	    		date.requestFocus();
	    		return;
	    	}
	    	billList.clear();
	    	modeBreakdownList.clear();

			for (Bill bill : billService.getDateWiseBill(date.getValue())) {
				if (cash ? hasCashPayment(bill) : hasNonCashPayment(bill)) {
					billList.add(bill);
				}
			}
	    	int sr=0;
	    	double totalBill=0,totalPaid=0,totalUnpaid=0;
			Map<String, Double> modeMap = new LinkedHashMap<>();
	    	for(int i=0;i<billList.size();i++)
	    	{
					Bill b = billList.get(i);
					accumulateModes(b, modeMap);

					b.setNettotal(b.getNettotal()+b.getTransportingchrges()+b.getOtherchargs());
					totalBill = totalBill+b.getNettotal();
					totalPaid = totalPaid+b.getRecivedamount();
					totalUnpaid = totalUnpaid+(b.getNettotal()-b.getRecivedamount());

					b.setOtherchargs(++sr);
					b.setRecievedby(buildPaymentModesString(b));
					b.setRecievedreff(b.getEmployee().getFname()+" "+
							b.getEmployee().getMname()+" "+
							b.getEmployee().getLname());

				}

			for (Map.Entry<String, Double> e : modeMap.entrySet()) {
				modeBreakdownList.add(new ModeTotal(e.getKey(), e.getValue()));
			}

			txtBillAmount.setText(""+totalBill);
			txtTotalPaid.setText(""+totalPaid);
			txtUnpaid.setText(""+totalUnpaid);
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

		private boolean hasCashPayment(Bill bill) {
			if (bill.getPayments() != null && !bill.getPayments().isEmpty()) {
				for (BillPayment p : bill.getPayments()) {
					if (isCashBank(p.getBank())) return true;
				}
				return false;
			}
			return isCashBank(bill.getBank());
		}

		private boolean hasNonCashPayment(Bill bill) {
			if (bill.getPayments() != null && !bill.getPayments().isEmpty()) {
				for (BillPayment p : bill.getPayments()) {
					if (p.getBank() != null && !isCashBank(p.getBank())) return true;
				}
				return false;
			}
			return bill.getBank() != null && !isCashBank(bill.getBank());
		}

		private boolean isCashBank(Bank bank) {
			return bank != null && (bank.getId() == 1 || bank.getId() == 5);
		}

	    }

