package main.java.main.java.controller.masterReport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class MasterReportController implements Initializable {

    @FXML private AnchorPane mainPane;
    @FXML private HBox menuDailySales;
    @FXML private HBox menuWeeklySales;
    @FXML private HBox menuMonthlySales;
    @FXML private HBox menuPeriodSales;
    @FXML private HBox menuPurchase;
    @FXML private HBox menuSalesman;

    @FXML private HBox menuWeeklyLabour;
    @FXML private HBox menuMonthlyLabour;
    @FXML private HBox menuPeriodLabour;

    private BorderPane pane;
    private Pane centerPane;
    private ViewUtil viewUtil;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewUtil = new ViewUtil();
        menuDailySales.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/DailySaleReport");
            pane = (BorderPane) mainPane.getParent();
            pane.setCenter(centerPane);
        });
        menuWeeklySales.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/WeeklySalesReport");
            pane = (BorderPane)mainPane.getParent();
            pane.setCenter(centerPane);
        });
        menuMonthlySales.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/MonthlySalesReport");
            pane = (BorderPane)mainPane.getParent();
            pane.setCenter(centerPane);
        });
        menuPeriodSales.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/PeriodSalesReport");
            pane = (BorderPane)mainPane.getParent();
            pane.setCenter(centerPane);
        });
        menuPurchase.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/purchasereport/PurchasePartyReport");
            pane = (BorderPane) mainPane.getParent();
            pane.setCenter(centerPane);
            centerPane.setVisible(true);
        });
        menuWeeklyLabour.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/labourcharges/WeeklyLabourChargesReport");
            pane =(BorderPane) mainPane.getParent();
            pane.setCenter(centerPane);
        });
        menuMonthlyLabour.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/labourcharges/MonthlyLabourChargesReport");
            pane =(BorderPane) mainPane.getParent();
            pane.setCenter(centerPane);
        });
        menuPeriodLabour.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/labourcharges/PeriodLabourChargesReport");
            pane =(BorderPane) mainPane.getParent();
            pane.setCenter(centerPane);
        });
        menuSalesman.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("masterreport/SalesmanCommisionReport");
            pane =(BorderPane) mainPane.getParent();
            pane.setCenter(centerPane);
        });
    }
}
