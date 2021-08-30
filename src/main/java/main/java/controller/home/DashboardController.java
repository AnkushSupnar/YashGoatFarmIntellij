package main.java.main.java.controller.home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

	
	@FXML
    private BorderPane mainPane;
	@FXML
	private Label txtTitle;
	@FXML
	public Label txtWindowTitle;
    @FXML
    private Button btnTransaction;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnReport;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnLogout;
    

    private ViewUtil viewUtil;
    private AlertNotification notify;
    private Pane createPane,transactionPane,reportPane,centerPane;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewUtil = new ViewUtil();
        notify = new AlertNotification();
		txtWindowTitle.setText("Home Page");

		//createPane = viewUtil.getPage("create/CreateMenuFrame");

		txtTitle.setText(ViewUtil.login.getUsername());
		if(ViewUtil.login.getId()!=1)
		{
			//btnCreate.setVisible(false);
			//btnReport.setVisible(false);
		//	btnSettings.setVisible(false);
			
		}
	}
    @FXML
    void logout(ActionEvent event) {
    }
    @FXML
    void openCreate(ActionEvent event) {
        if(ViewUtil.login.getId()!=1)
        {
           notify.showErrorMessage("YOu Are Not Authorised To See This Page");
           return;

        }
        createPane = viewUtil.getPage("create/CreateMenuFrame");
    	txtWindowTitle.setText("Create Master");
    	//createPane = viewUtil.getPage("home/CreateMenuFrame");
    	mainPane.setCenter(createPane);
    }
    @FXML
    void openReport(ActionEvent event) {
        reportPane = viewUtil.getPage("report/ReportMenu");
    	txtWindowTitle.setText("Report");
    	mainPane.setCenter(reportPane);
    }
    @FXML
    void openSettings(ActionEvent event) {
    }
    @FXML
    void openTransaction(ActionEvent event) {
        transactionPane = viewUtil.getPage("transaction/TransactionMenu");
    	txtWindowTitle.setText("Transaction");
    	mainPane.setCenter(transactionPane);
    }
}
