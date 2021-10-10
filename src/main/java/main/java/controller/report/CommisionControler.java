package main.java.main.java.controller.report;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.main.java.Main;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.*;
import main.java.main.java.hibernate.service.service.*;
import main.java.main.java.hibernate.service.serviceImpl.*;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.CommisionReport;
import main.java.main.java.print.PrintFile;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.*;

public class CommisionControler implements Initializable {
		@FXML
		private AnchorPane mainFrame;
		@FXML private ComboBox<String> cmbSalesmanName;
	    @FXML private DatePicker dateStart;
	    @FXML private DatePicker dateEnd;
	    @FXML private Button btnShow;
	    @FXML private Button btnShowAll;
	    @FXML private Button btnClear;

	    @FXML private TableView<Bill> table;
	    @FXML private TableColumn<Bill, Long> colBillNo;
	    @FXML private TableColumn<Bill, LocalDate> colDate;
	    @FXML private TableColumn<Bill,Float> CollBillAmount;
	    @FXML private TableColumn<Bill,Float> colCommision;
	    @FXML private TableColumn<Bill, Float> colTransporting;
	    @FXML private TableColumn<Bill,Float> ColPaidCommision;
	    
	    @FXML private TextField txtTotalCommision;
	    @FXML private ComboBox<String> cmbBankName; 
	    @FXML private TextField txtRefferenceNo;
	    @FXML private Button btnPay;
	    @FXML private Button btnClear2;
	    @FXML private Button btnEdit;
	    @FXML private Button btnPrint;
	    @FXML private Button btnExit;	   
	    @FXML private Button btnPreview;

	    @FXML private TableView<Commision> tableCommision;
	    @FXML private TableColumn<Commision, Long> colSrNo;
	    @FXML private TableColumn<Commision, String> colEmpName;
	    @FXML private TableColumn<Commision,LocalDate> colDate2;
	    @FXML private TableColumn<Commision, Long> colBill;
	    @FXML private TableColumn<Commision, Float> colBank;
	    @FXML private TableColumn<Commision, Float> colCash;
	    @FXML private TableColumn<Commision, Float> colCommision2;
	    @FXML private TableColumn<Commision, Float> colTransaporting;
	    
	    
	    
	    @FXML private TextField txtBillAmount;
	    @FXML private TextField txtCash;
	    @FXML private TextField txtBank;
	    @FXML private TextField txtOtherCharges;
	    @FXML private TextField txtUnpaid;

	@FXML private DatePicker dateLoad;
	@FXML private Button btnLoad;
	@FXML private Button btnMonth;
	@FXML private Button btnYear;
	@FXML private Button btnAll;

	    private EmployeeService employeeService;
	    private BillService billService;
	    private BankService bankService;
	    private CommisionService commisionService;
	    private BankTransactionService bankTrService;
	    long id;
	    private ObservableList<Bill> billList = FXCollections.observableArrayList();
	    private ObservableList<Commision> commisionList = FXCollections.observableArrayList();
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			employeeService = new EmployeeServiceImpl();
			billService = new BillServiceImpl();
			bankService = new BankServiceImpl();
			bankTrService = new BankTransactionServiceImpl();
			commisionService = new CommisionServiceImpl();
			cmbSalesmanName.getItems().addAll(employeeService.getAllSalesmanNames());
			colBillNo.setCellValueFactory(new PropertyValueFactory<Bill,Long>("billno"));
			colDate.setCellValueFactory(new PropertyValueFactory<Bill,LocalDate>("date"));
			CollBillAmount.setCellValueFactory(new PropertyValueFactory<Bill,Float>("nettotal"));
			colCommision.setCellValueFactory(new PropertyValueFactory<Bill,Float>("otherchargs"));//otherchargs column is use to Commision
			ColPaidCommision.setCellValueFactory(new PropertyValueFactory<Bill,Float>("paidcommision"));
			colTransporting.setCellValueFactory(new PropertyValueFactory<>("transportingchrges"));
			table.setItems(billList);
			cmbBankName.getItems().addAll(bankService.getAllBankNames());
			id=commisionService.getNewCommisionId();
			
			
			colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
			colEmpName.setCellValueFactory(new PropertyValueFactory<>("bankReffNo"));
			colDate2.setCellValueFactory(new PropertyValueFactory<>("date"));
			colBill.setCellValueFactory(new PropertyValueFactory<>("totalBill"));
			colBank.setCellValueFactory(new PropertyValueFactory<>("bankAmount"));
			colCash.setCellValueFactory(new PropertyValueFactory<>("cashamount"));
			colCommision2.setCellValueFactory(new PropertyValueFactory<>("paidCommision"));
			colTransaporting.setCellValueFactory(new PropertyValueFactory<>("transaportingCharges"));
			dateLoad.setValue(LocalDate.now());
			commisionList.addAll(commisionService.getDatePeriodCommision(
					dateLoad.getValue().with(previousOrSame(MONDAY)),
					dateLoad.getValue().with(nextOrSame(SUNDAY))));
			//commisionList.addAll(commisionService.getAllCommision());

			for(int i=0;i<commisionList.size();i++)
			{
				commisionList.get(i).setBankReffNo(
						commisionList.get(i).getEmployee().getFname()+" "+
								commisionList.get(i).getEmployee().getLname()+" "+
								commisionList.get(i).getEmployee().getLname()
						);
			}
			tableCommision.setItems(commisionList);
			btnLoad.setOnAction(e->{
				if(dateLoad.getValue()!=null)
				{
					commisionList.clear();
					commisionList.addAll(commisionService.getDatePeriodCommision(
							dateLoad.getValue().with(previousOrSame(MONDAY)),
							dateLoad.getValue().with(nextOrSame(SUNDAY))));
					for(int i=0;i<commisionList.size();i++)
					{
						commisionList.get(i).setBankReffNo(
								commisionList.get(i).getEmployee().getFname()+" "+
										commisionList.get(i).getEmployee().getLname()+" "+
										commisionList.get(i).getEmployee().getLname()
						);
					}
				}
				});
			btnMonth.setOnAction(e->{
				if(dateLoad.getValue()!=null)
				{
					commisionList.clear();
					commisionList.addAll(commisionService.getDatePeriodCommision(
							dateLoad.getValue().with(firstDayOfMonth()),
							dateLoad.getValue().with(lastDayOfMonth())));
					for(int i=0;i<commisionList.size();i++)
					{
						commisionList.get(i).setBankReffNo(
								commisionList.get(i).getEmployee().getFname()+" "+
										commisionList.get(i).getEmployee().getLname()+" "+
										commisionList.get(i).getEmployee().getLname()
						);
					}
				}
			});
			btnYear.setOnAction(e->{
				if(dateLoad.getValue()!=null)
				{
					commisionList.clear();
					commisionList.addAll(commisionService.getDatePeriodCommision(
							dateLoad.getValue().with(firstDayOfYear()),
							dateLoad.getValue().with(lastDayOfYear())));
					for(int i=0;i<commisionList.size();i++)
					{
						commisionList.get(i).setBankReffNo(
								commisionList.get(i).getEmployee().getFname()+" "+
										commisionList.get(i).getEmployee().getLname()+" "+
										commisionList.get(i).getEmployee().getLname()
						);
					}
				}
			});
			btnAll.setOnAction(e->{

				commisionList.addAll(commisionService.getAllCommision());
				//commisionList.addAll(commisionService.getAllCommision());
				for(int i=0;i<commisionList.size();i++)
				{
					commisionList.get(i).setBankReffNo(
							commisionList.get(i).getEmployee().getFname()+" "+
									commisionList.get(i).getEmployee().getLname()+" "+
									commisionList.get(i).getEmployee().getLname()
					);
				}
			});
		}

	    @FXML
	    void btnShowAction(ActionEvent event) {
	    	if(cmbSalesmanName.getValue()==null)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"Select Salesman Name").showAndWait();
	    		cmbSalesmanName.requestFocus();
	    		return;
	    	}
	    	if(dateStart.getValue()==null)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"Select Starting Date").showAndWait();
	    		dateStart.requestFocus();
	    		return;
	    	}
	    	if(dateEnd.getValue()==null)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"Select End Date").showAndWait();
	    		dateEnd.requestFocus();
	    		return;
	    	}
	    	if(dateStart.getValue().compareTo(dateEnd.getValue())==1)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"Start date must be smaller than end Date").showAndWait();
	    		dateEnd.requestFocus();
	    		return;
	    	}
	    	
	    	if(id!=commisionService.getNewCommisionId())
	    	{
	    		new Alert(Alert.AlertType.ERROR,"You Can not revert paid Commision").showAndWait();
	    		//dateEnd.requestFocus();
	    		return;
	    	}
	    	billList.clear();
	    	
	    	billList.addAll(billService.getUnpaidCommisionBillsPeriodWise(employeeService.getEmployeeByName(cmbSalesmanName.getValue()).getId(),dateStart.getValue(), dateEnd.getValue()));
	    	if(billList.size()==0)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"NO Commision Pending For this Date").showAndWait();
	    		return;
	    	}
	    	validateBillList();
	    	
	    }

	    @FXML
	    void btnShowAllAction(ActionEvent event) {
	    	if(cmbSalesmanName.getValue()==null)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"Select SSalesman Name").showAndWait();
	    		cmbSalesmanName.requestFocus();
	    		return;
	    	}
	    	if(id!=commisionService.getNewCommisionId())
	    	{
	    		new Alert(Alert.AlertType.ERROR,"You Can not revert paid Commision").showAndWait();
	    		//dateEnd.requestFocus();
	    		return;
	    	}
	    	
	    	billList.clear();
	    	
	    	billList.addAll(billService.getUnpaidCommisionBills(employeeService.getEmployeeByName(cmbSalesmanName.getValue()).getId()));
	    	//float commisoin = 0.0f,bill=0,cash=0,bank=0,gtotal=0,tranp=0;
	    	if(billList.size()==0)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"NO Commision Pending For this Date").showAndWait();
	    		return;
	    	}
	    	validateBillList();
	    }
	    @FXML
	    void btnClearAction(ActionEvent event) {
	    	billList.clear();
	    	cmbSalesmanName.getSelectionModel().clearSelection();
	    	dateStart.setValue(null);
	    	dateEnd.setValue(null);
	    	txtOtherCharges.setText("");
	    	
	    }
	    @FXML
	    void btnClear2Action(ActionEvent event) {
	    	btnClear.fire();
	    	id=0;
	    	txtRefferenceNo.setText("");
	    	cmbBankName.getSelectionModel().clearSelection();
	    	txtTotalCommision.setText(""+0.0f);
			txtBank.setText("");
			txtBillAmount.setText(""+0.0f);
			txtCash.setText(""+0.0f);
			txtTotalCommision.setText(""+0.0f);
			txtOtherCharges.setText(""+0.0f);
			txtRefferenceNo.setText(""+0.0f);
			txtUnpaid.setText(""+0.0f);
	    	
	    	
	    }
	    @FXML
	    void btnEditAction(ActionEvent event) {
	    	if(tableCommision.getSelectionModel().getSelectedItem()==null)
	    	{
	    		return;
	    	}
	    	Commision commision = tableCommision.getSelectionModel().getSelectedItem();
	    	if(commision==null)
	    	{
	    		return;
	    	}
	    	id= commision.getId();
	    	billList.clear();
	    	commision = commisionService.getCommisionById(id);
	    	
	    	for(CommisionTransaction tr:commision.getTransaction())
	    	{
	    		billList.add(billService.getBillByBillno(tr.getBill().getBillno()));
	    	}	    	
	    	validateBillList();
	    	txtBillAmount.setText(""+commision.getTotalBill());
	    	txtCash.setText(""+commision.getCashamount());
	    	txtBank.setText(""+commision.getBankAmount());
	    	txtOtherCharges.setText(""+commision.getTransaportingCharges());
	    	cmbBankName.setValue(commision.getBank().getBankname());
	    	cmbSalesmanName.setValue(commision.getEmployee().getFname()+" "+commision.getEmployee().getMname()+" "+commision.getEmployee().getLname());
	    	txtRefferenceNo.setText(commision.getBankReffNo());

	    }
	    @FXML
	    void btnExitAction(ActionEvent event) {
	    		
	    	mainFrame.setVisible(false);

	    }

	    @FXML
	    void btnPayAction(ActionEvent event) {
	    	if(validateData()!=1)
	    	{
	    		return;
	    	}
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"You are not authorised to pay commision !!!").showAndWait();
	    		return;
	    	}
	    	Commision commision = new Commision(
	    			LocalDate.now(),
	    			Float.parseFloat(txtTotalCommision.getText()),
	    			Float.parseFloat(txtTotalCommision.getText()),
	    			Float.parseFloat(txtBillAmount.getText()),
	    			Float.parseFloat(txtBank.getText()),
	    			Float.parseFloat(txtCash.getText()),
	    			Float.parseFloat(txtOtherCharges.getText()),
	    			txtRefferenceNo.getText(),
	    			employeeService.getEmployeeByName(cmbSalesmanName.getValue()),
	    			bankService.getBankByName(cmbBankName.getValue()),
	    			null);
	    	System.out.println(commision.getEmployee());
	    	commision.setId(id);
	    	Commision oldCommision=null;
	    	if(id!=0) oldCommision = commisionService.getCommisionById(commision.getId());
	    	System.out.println("got id=="+id);
	    	
	    	List<CommisionTransaction> trList = new ArrayList();
	    	for(Bill b:billList)
	    	{
	    		trList.add(
	    				new CommisionTransaction(
	    						b.getDate(), 
	    						b.getOtherchargs(), 
	    						b.getOtherchargs(),
	    						commision,
	    						billService.getBillByBillno(b.getBillno())
	    						)
	    				);
	    	}
	    	//System.out.println(commision);
	    	commision.setTransaction(trList);
	    	for(CommisionTransaction tr:trList)
	    	{
	    		System.out.println("id "+tr.getId()+" Commision "+tr.getTotalCommision()+" paid-"+tr.getPaidCommision()
	    		+" Date "+tr.getDate());
	    	}
	    	
	    	BankTransaction bkTr = new BankTransaction(
	    			"Reduce Pay Commision id "+commision.getId(),
	    			commision.getId(),
	    			0,
	    			commision.getPaidCommision(),
	    			commision.getBank().getId(),
	    			commision.getDate());
	    	
	    	int flag=commisionService.saveCommision(commision);
	    	
	    	
	    	if(flag==1)
	    	{
	    		//add bank transaction
	    		bankTrService.saveBankTransaction(bkTr);
	    		//update bank
	    		bankService.reduceBankBalance(commision.getBank().getId(), commision.getPaidCommision());
	    		//update bill set paid commision
	    		billService.updatePaidCommision(billList);
	    		printCommisionReport(id);
	    		new Alert(Alert.AlertType.INFORMATION,"Record Saved!!!").showAndWait();
	    		billList.clear();
	    		btnClear.fire();
	    		btnClear2.fire();
	    		
	    		commisionList.add(commision);
	    		for(int i=0;i<commisionList.size();i++)
				{
					commisionList.get(i).setBankReffNo(
							commisionList.get(i).getEmployee().getFname()+" "+
									commisionList.get(i).getEmployee().getLname()+" "+
									commisionList.get(i).getEmployee().getLname()
							);
				}
	    		id=0;
	    		return;
	    	}
	    	if(flag==2)
	    	{
	    		//edit Commision
	    		//add amount in bank and transaction
	    		BankTransaction btr = new BankTransaction(
	    				"Add Edit Commision id "+oldCommision.getId(),
	    				oldCommision.getId(),
	    				oldCommision.getPaidCommision(),
	    				0,
	    				oldCommision.getBank().getId(),
	    				LocalDate.now());
	    		bankTrService.saveBankTransaction(btr);
	    		bankService.addBankBalance(btr.getBankid(), btr.getDebit());
	    		
	    		//reduce bank Balance
	    		bankTrService.saveBankTransaction(bkTr);
	    		//update bank
	    		bankService.reduceBankBalance(commision.getBank().getId(), commision.getPaidCommision());
	    		//update bill set paid commision
	    		billService.updatePaidCommision(billList);
	    		printCommisionReport(id);
	    		new Alert(Alert.AlertType.INFORMATION,"Record Updated!!!").showAndWait();
	    		billList.clear();
	    		btnClear.fire();
	    		btnClear2.fire();
	    		int f=0;
	    		for(int i=0;i<commisionList.size();i++)
	    		{
	    			if(commisionList.get(i).getId()==commision.getId())
	    			{
	    				f=i;
	    				return;
	    			}
	    		}
	    		if(f!=0) {
	    		commisionList.remove(f);
	    		commisionList.add(f, commision);
	    		}
	    		id=0;
	    		return;
	    		
	    	}
	    	

	    }

	    private void printCommisionReport(long i) {
	    	Stage stage = (Stage) mainFrame.getScene().getWindow();
			Alert.AlertType type = Alert.AlertType.CONFIRMATION;
			Alert alert = new Alert(type,"");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(stage);
			alert.getDialogPane().setContentText("Do You Want Print Commision Report?");
			alert.getDialogPane().setHeaderText("Confirmation");
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get()==ButtonType.OK)
			{
				new CommisionReport(i);
				//new PrintFile("D:\\Software\\Prints\\commision.pdf");
				new PrintFile().openFile("D:\\Software\\Prints\\commision.pdf");
			}
			else if(result.get()==ButtonType.CANCEL)
			{
				
			}
			
		}

		@FXML
	    void btnPrintAction(ActionEvent event) {
			try {
				if(tableCommision.getSelectionModel().getSelectedItem()==null)
				{
					return;
				}
				printCommisionReport(tableCommision.getSelectionModel().getSelectedItem().getId());
			} catch (Exception e) {
				e.printStackTrace();
				new Alert(Alert.AlertType.ERROR,"Error in Printing Commission"+e.getMessage()).showAndWait();
			}
	    }
	    @FXML
	    void btnPreviewAction(ActionEvent event) {
	    //	Bill bill = table.getSelectionModel().getSelectedItem();
	    	Bill bill = billService.getBillByBillno(table.getSelectionModel().getSelectedItem().getBillno());
	    	if(bill == null)
	    	{
	    		new Alert(Alert.AlertType.ERROR,"Select Bill from Above List To Preview!!!").showAndWait();
	    		return;
	    	}
	    	CommonData.previewBillNo=bill.getBillno();
	    	 Stage stage = new Stage();
			 Parent root;
			 try {
				root = FXMLLoader.load(Main.class.getResource("/view/report/BillPreview.fxml"));
			    stage.setScene(new Scene(root));
			    stage.setTitle("Bill Preview");
			    stage.initModality(Modality.WINDOW_MODAL);
			    stage.initOwner(
			        ((Node)event.getSource()).getScene().getWindow() );
			    stage.show();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent e) {
						
					}
				});
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    }
	   
	    private float getBillCommision(Bill bill)
	    {
	    	System.out.println("Got To Check Commision "+bill.getBillno());
	    	float commision=0.0f;
	    	if(bill!=null)
	    	{
	    		for(Transaction tr:bill.getTransaction())
	    		{
	    			System.out.println(tr.getCommision());
	    			commision = commision+tr.getCommision();
	    		}
	    		
	    	}
	    	return commision;
	    }

	    private void validateBillList()
	    {
	    	float commisoin = 0.0f,bill=0,cash=0,bank=0,gtotal=0,tranp=0,paid=0;
	    	for(int i=0;i<billList.size();i++)
	    	{
	    		
	    		
	    		if(billList.get(i).getBank().getAccountno().equalsIgnoreCase("cash".trim())) {	 
//	    			cash = cash+billList.get(i).getNettotal()+
//	    					billList.get(i).getOtherchargs() +
//	    					billList.get(i).getTransportingchrges() ;
	    			cash = cash+billList.get(i).getRecivedamount();
	    		}
	    		else {
//	    			bank = bank+billList.get(i).getNettotal()+
//	    					billList.get(i).getOtherchargs() +
//	    					billList.get(i).getTransportingchrges() ;
	    			bank = bank+billList.get(i).getRecivedamount();
	    		}
	    		
	    		
	    		tranp = tranp+billList.get(i).getTransportingchrges();
	    		gtotal = billList.get(i).getNettotal()+
    					billList.get(i).getOtherchargs()+
    					billList.get(i).getTransportingchrges();
	    		
	    		bill=bill+gtotal;
	    		paid = paid+billList.get(i).getRecivedamount();
	    		
	    		billList.get(i).setNettotal(
	    				billList.get(i).getNettotal()+
	    				billList.get(i).getOtherchargs()+
	    				billList.get(i).getTransportingchrges()
	    				);
	    		
	    		billList.get(i).setOtherchargs(getBillCommision(billList.get(i)));
	    		commisoin = commisoin+billList.get(i).getOtherchargs();
	    		
	    		
	    	}
	    	
	    	txtTotalCommision.setText(""+commisoin);
	    	txtBank.setText(""+bank);
	    	txtCash.setText(""+cash);
	    	txtBillAmount.setText(""+bill);
	    	txtOtherCharges.setText(""+tranp);
	    	txtUnpaid.setText(""+(bill-paid));
	    }
	    private int validateData()
	    {
	    	try {
				if(txtTotalCommision.getText().equals("0.0")||txtTotalCommision.getText().equals(""))
				{
					new Alert(Alert.AlertType.ERROR,"No Data To Save!!!").showAndWait();
					return 0;
				}
				if(cmbBankName.getSelectionModel().getSelectedItem()==null)
				{
					new Alert(Alert.AlertType.ERROR,"Select Bank Name !!!").showAndWait();
					return 0;
				}
				if(txtRefferenceNo.getText().equals(""))
				{
					new Alert(Alert.AlertType.ERROR,"Enter Bank Transaction No !!!").showAndWait();
					txtRefferenceNo.requestFocus();
					return 0;
				}
				return 1;
			} catch (Exception e) {
				e.printStackTrace();
				new Alert(Alert.AlertType.ERROR,"Error in Pay \n"+e.getMessage()).showAndWait();
				return 0;
			}
	    }
}
