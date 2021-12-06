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
import main.java.main.java.print.DailySalesReportPrint;
import main.java.main.java.print.PrintFile;

import java.net.URL;
import java.time.LocalDate;
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


	private ObservableList<Bill>billList =FXCollections.observableArrayList();
	  private BillService billService;
	  boolean cash;
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

	    	//billList.addAll(billService.getDateWiseBill(date.getValue()));
				if(!cash)
				{
					System.out.println("Without bCash Bills");
					for(Bill bill:billService.getDateWiseBill(date.getValue()))
					{
						if(bill.getBank().getId()!=1 && bill.getBank().getId()!=5 )
						{
							billList.add(bill);
						}
					}
				}
				else
					billList.addAll(billService.getDateWiseBill(date.getValue()));
	    	int sr=0;
	    	double totalBill=0,totalPaid=0,totalUnpaid=0;
	    	for(int i=0;i<billList.size();i++)
	    	{
					billList.get(i).setNettotal(billList.get(i).getNettotal()+
							billList.get(i).getTransportingchrges()+
							billList.get(i).getOtherchargs());
					totalBill = totalBill+billList.get(i).getNettotal();
					totalPaid = totalPaid+billList.get(i).getRecivedamount();
					totalUnpaid = totalUnpaid+(billList.get(i).getNettotal()-billList.get(i).getRecivedamount());

					billList.get(i).setOtherchargs(++sr);
					billList.get(i).setRecievedby(billList.get(i).getBank().getBankname());
					billList.get(i).setRecievedreff(billList.get(i).getEmployee().getFname()+" "+
							billList.get(i).getEmployee().getMname()+" "+
							billList.get(i).getEmployee().getLname());

				}

			txtBillAmount.setText(""+totalBill);
			txtTotalPaid.setText(""+totalPaid);
			txtUnpaid.setText(""+totalUnpaid);
	    	}

	    }

