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
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.reportEntity.ReportTable;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.StickReportPrint;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StickReportController implements Initializable {

	  	@FXML private AnchorPane mainPane;
	    @FXML private DatePicker dateStart;
	    @FXML private DatePicker dateEnd;

	    @FXML private TableView<ReportTable> table;
	    @FXML private TableColumn<ReportTable,Integer> colSrNo;
	    @FXML private TableColumn<ReportTable,LocalDate> colDate;
	    @FXML private TableColumn<ReportTable,String> colSalesman;
	    @FXML private TableColumn<ReportTable,String> colItem;
	    @FXML private TableColumn<ReportTable,Double> colQty;
	    @FXML private TableColumn<ReportTable,Double> colRate;
	    @FXML private TableColumn<ReportTable,Double> colAmount;
	    @FXML private TableColumn<?, ?> colCommision;
	    @FXML private TableColumn<ReportTable,Double> colLabour;
	    @FXML private TableColumn<ReportTable,Double> colRemain;
	    
	    @FXML private TextField txtQty;
	    @FXML private TextField txtAmount;
	    @FXML private TextField txtCommision;
	    @FXML private TextField txtLabour;
	    @FXML private TextField txtRemain;
	    
	    private BillService billService;
	    private ItemService itemService;
	    private ObservableList<ReportTable>reportList = FXCollections.observableArrayList();
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			billService = new BillServiceImpl();
			itemService = new ItemServiceImpl();
			
			 colSrNo.setCellValueFactory(new PropertyValueFactory<>("srNo"));
			 colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
			 colSalesman.setCellValueFactory(new PropertyValueFactory<>("salesman"));
			 colItem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
			 colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
			 colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
			 colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
			 colCommision.setCellValueFactory(new PropertyValueFactory<>("commision"));
			 colLabour.setCellValueFactory(new PropertyValueFactory<>("labour"));
			 colRemain.setCellValueFactory(new PropertyValueFactory<>("remain"));
			 
			 table.setItems(reportList);
		}

		@FXML
		void btnShowAction(ActionEvent event) {
			if(dateStart.getValue()==null)
			{
				new Alert(AlertType.ERROR,"Select Start Date").showAndWait();
				dateStart.requestFocus();
				return;
			}
			if(dateEnd.getValue()==null)
			{
				new Alert(AlertType.ERROR,"Enter End Date").showAndWait();
				dateEnd.requestFocus();
				return;
			}
			if(dateStart.getValue().compareTo(dateEnd.getValue())==1)
			{
				new Alert(AlertType.ERROR,"Start Date Must be Less Than End Date").showAndWait();
				return;
			}
			List<Bill>billList =billService.getPeriodWiseBills(dateStart.getValue(),dateEnd.getValue());
			List<Bill> labourBills = new ArrayList<Bill>();
			int flag=0;
			double qty=0,amount=0,commision=0,labour=0,remain=0;
			for(Bill bill: billList)
			{
				for(Transaction tr:bill.getTransaction())
				{
					if(itemService.getItemByName(tr.getItemname()).getLabourCharges()>0)
					{
						flag=1;
					}
				}
				if(flag==1)
				{
				labourBills.add(bill);
				}
				flag=0;
			}
			reportList.clear();
			ReportTable rt=null;
			int sr=0;
			for(Bill bill:labourBills)
			{
				for(Transaction tr:bill.getTransaction())
				{
					if(itemService.getItemByName(tr.getItemname()).getLabourCharges()>0) {
					rt = new ReportTable(
							++sr,
							bill.getDate(),
							bill.getEmployee().getFullName(),
							tr.getItemname(),
							tr.getQuantity(),
							tr.getRate(),
							tr.getAmount(),
							tr.getCommision(),
							itemService.getItemByName(tr.getItemname()).getLabourCharges()*tr.getQuantity(), 
							tr.getAmount()-tr.getCommision()-(itemService.getItemByName(tr.getItemname()).getLabourCharges()*tr.getQuantity())
							);
					reportList.add(rt);
					qty=qty+rt.getQty();
					amount=amount+rt.getAmount();
					commision=commision+rt.getCommision();
					labour=labour+rt.getLabour();
					remain=remain+rt.getRemain();
					
					rt=null;
					}
				}
			}
			txtAmount.setText(""+amount);
			txtCommision.setText(""+commision);
			txtLabour.setText(""+labour);
			txtQty.setText(""+qty);
			txtRemain.setText(""+remain);
			
		}
		  @FXML
		    void btnPrintAction(ActionEvent event) {
			  if(reportList.size()<=0 ||dateStart.getValue()==null||dateEnd.getValue()==null)
			  {
				  return;
			  }
			  new StickReportPrint(reportList, dateStart.getValue(), dateEnd.getValue());
			  new PrintFile().openFile("D:\\Software\\Prints\\StickReport.pdf");
		    }


		  @FXML
		    void btnClearAction(ActionEvent event) {
			  txtAmount.setText("");
			  txtCommision.setText("");
			  txtLabour.setText("");
			  txtQty.setText("");
			  txtRemain.setText("");
			  reportList.clear();
			  
		    }
}

