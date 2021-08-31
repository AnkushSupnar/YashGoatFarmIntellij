package main.java.main.java.controller.report;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;


public class ReportMenuControler implements Initializable {

	 	@FXML private AnchorPane reportMenuPanel;
		@FXML private Button btnCommisionReport;
	    @FXML private Button btnSalesReport;
	    @FXML private Button btnDailySalesReport;
	    @FXML private Button btnWeeklySalesReport;
	    @FXML private Button btnMonthlySalesReport;
	    @FXML private Button btnPeriodSalesReport;	 
	    @FXML private Button brnYearSalesReport;
	    @FXML private Button btnLabourCharges;
	    

	    @FXML private Button btnCounterStock;

	    
	    @FXML private Button btnCustomerBills;
	    @FXML private Button btnUnpaidBills;
	    @FXML private Button btnViewAllStock;
	    //Bank Report Menu
	    @FXML private Button btnBankStatement;
	    @FXML private Button btnAllUnpaidBills;
	    
	    private BorderPane pane;
		private ViewUtil viewUtil;
		private Pane centerPane;
	    @Override
		public void initialize(URL location, ResourceBundle resources) {
	    	viewUtil = new ViewUtil();
	    	//pane = (BorderPane) reportMenuPanel.getParent();
	    	
		}
	    @FXML
	    void btnCommisionReportAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/CommisionFrame");
	    	pane = (BorderPane) reportMenuPanel.getParent();
			pane.setCenter(centerPane);
			centerPane.setVisible(true);
	    }

	    @FXML
	    void btnSalesReportAction(ActionEvent event) {
			centerPane = viewUtil.getPage("report/SalesmanSalesReport");
	    	pane = (BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    	
	    }

	    @FXML
	    void btnDailySalesReportAction(ActionEvent event) {
			centerPane = viewUtil.getPage("report/DailySaleReport");
	    	pane = (BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }

	    @FXML
	    void btnMonthlySalesReport(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
	    	System.out.println("monthly Sales Report");
			centerPane = viewUtil.getPage("report/MonthlySalesReport");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }

	    @FXML
	    void btnPeriodSalesReportAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/PeriodItemSalesReport");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }

	    @FXML
	    void btnCustomerBillsAction(ActionEvent event) {
			centerPane = viewUtil.getPage("report/CustomerBills");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }


	    @FXML
	    void btnWeeklySalesReportAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/WeeklySalesReport");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void brnYearSalesReportAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/YearItemSalesReport");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnUnpaidBillsAction(ActionEvent event) {
			centerPane = viewUtil.getPage("report/CustomerUnpaidBills");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnViewAllStockAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/ItemStockReport");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void allUnpaidBillAction(ActionEvent event) {
			centerPane = viewUtil.getPage("report/ShowAllUnpaidBills");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnBankStatementAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/BankStatement");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnLabourChargesAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/LabourCommision");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnSalesmanLabourChargesAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane =  viewUtil.getPage("report/SalesmanLabourChargesReport");
	    	pane = (BorderPane)reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnStickReportAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/StickReport");
	    	pane =(BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnDailyItemSalesReportAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/DailyItemSalesReport");
	    	pane =(BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void weeklyItemSalesReportAction(ActionEvent event) {
	    	if(ViewUtil.login.getId()!=1)
	    	{
	    		new Alert(AlertType.ERROR,"You are not authorised to view this report!!!").showAndWait();
	    		return;
	    	}
			centerPane = viewUtil.getPage("report/WeeklyItemSalesReport");
	    	pane =(BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }

	    @FXML
	    void btnCounterStockAction(ActionEvent event) {
	    	
	    	centerPane = viewUtil.getPage("report/viewcounterstock");
	    	pane =(BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnPartyStatementAction(ActionEvent event) {	    	
	    	centerPane = viewUtil.getPage("report/PurchaseStatement");
	    	pane =(BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnCashReceiptreportAction(ActionEvent event) {
	    	centerPane = viewUtil.getPage("report/paymentrecieptreport");
	    	pane =(BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	    @FXML
	    void btnCustomerStatementAction(ActionEvent event) {
	    	centerPane = viewUtil.getPage("report/customerstatement");
	    	pane =(BorderPane) reportMenuPanel.getParent();
	    	pane.setCenter(centerPane);
	    }
	@FXML
	void btnSalesmanItemReportAction(ActionEvent event) {
		centerPane = viewUtil.getPage("report/salesmanitemsalesreport");
		pane =(BorderPane) reportMenuPanel.getParent();
		pane.setCenter(centerPane);
	}
	@FXML
	void btnMonthlyItemSalesReportAction(ActionEvent event) {
		centerPane = viewUtil.getPage("report/MonthlyItemSalesReport");
		pane =(BorderPane) reportMenuPanel.getParent();
		pane.setCenter(centerPane);
	}
	@FXML
	void btnWeeklyLabourChargesAction(ActionEvent event) {
		centerPane = viewUtil.getPage("report/labourcharges/WeeklyLabourChargesReport");
		pane =(BorderPane) reportMenuPanel.getParent();
		pane.setCenter(centerPane);
	}
	@FXML
	void btnMonthlyLabouChargesAction(ActionEvent event) {
		centerPane = viewUtil.getPage("report/labourcharges/MonthlyLabourChargesReport");
		pane =(BorderPane) reportMenuPanel.getParent();
		pane.setCenter(centerPane);
	}

	@FXML
	void btnPeriodLabourChargesAction(ActionEvent event) {
		centerPane = viewUtil.getPage("report/labourcharges/PeriodLabourChargesReport");
		pane =(BorderPane) reportMenuPanel.getParent();
		pane.setCenter(centerPane);

	}
	@FXML
	void btnYearlyLabourChargesAction(ActionEvent event) {
		centerPane = viewUtil.getPage("report/labourcharges/YearlyLabourChargesReport");
		pane =(BorderPane) reportMenuPanel.getParent();
		pane.setCenter(centerPane);
	}

}
