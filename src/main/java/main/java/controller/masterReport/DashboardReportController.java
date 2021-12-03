package main.java.main.java.controller.masterReport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML private StackPane titlePane;

    @FXML private AnchorPane paneToday;
    @FXML private AnchorPane paneMonth;
    @FXML private AnchorPane paneYear;
    @FXML private AnchorPane paneAll;
    private ViewUtil viewUtil;
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewUtil = new ViewUtil();
        paneToday.getChildren().add(viewUtil.getPage("masterreport/TodayDashboard"));
//        btnToday.setOnAction(e->{
//            pane = viewUtil.getPage("masterreport/TodayDashboard");
//            pane.setLayoutY(35);
//
//            mainPane.getChildren().add(1,pane);
//        });
    }
}
