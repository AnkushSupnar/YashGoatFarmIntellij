package main.java.main.java.controller.home;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML private BorderPane mainPane;
    @FXML private HBox menuTransaction;
    @FXML private HBox menuDashboard;
    @FXML private HBox menuCreate;
    @FXML private HBox menuInventary;
    @FXML private HBox menuMaster;
    @FXML private HBox menuReport;
    @FXML private HBox menuExit;
    @FXML private Text txtUserName;
    @FXML private Text txtTitle;


    private Pane centerPane;
    private ViewUtil viewUtil;
    private AlertNotification notify;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewUtil = new ViewUtil();
        notify = new AlertNotification();
        txtUserName.setText(ViewUtil.login.getUsername());
        menuTransaction.setOnMouseClicked(e->{
           centerPane = viewUtil.getPage("transaction/TransactionMenu");
            txtTitle.setText("Transaction");
            mainPane.setCenter(centerPane);
        });
        menuCreate.setOnMouseClicked(e->{
            if(ViewUtil.login.getId()!=1){
                notify.showErrorMessage("You Are Not Authorised To See This Page");
                return;
            }
            centerPane = viewUtil.getPage("create/CreateMenuFrame");
            txtTitle.setText("Create Master");
            mainPane.setCenter(centerPane);
        });
        menuReport.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("report/ReportMenu");
            txtTitle.setText("Report");
            mainPane.setCenter(centerPane);
        });

        menuMaster.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("masterreport/MasterReportMenu");
            txtTitle.setText("Master Report");
            mainPane.setCenter(centerPane);
        });
        menuDashboard.setOnMouseClicked(e->{
            centerPane = viewUtil.getPage("masterreport/DashboardReport");
            txtTitle.setText("Dashboard");
            mainPane.setCenter(centerPane);
        });
    }
}
