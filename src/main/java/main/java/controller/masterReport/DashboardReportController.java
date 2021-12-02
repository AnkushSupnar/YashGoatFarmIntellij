package main.java.main.java.controller.masterReport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML private StackPane titlePane;
    @FXML private Button btnToday;
    @FXML private Button btnMonth;
    @FXML private Button btnYear;
    @FXML private Button btnAll;
    private ViewUtil viewUtil;
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewUtil = new ViewUtil();
        btnToday.setOnAction(e->{
            pane = viewUtil.getPage("masterreport/TodayDashboard");
            pane.setLayoutY(35);

            mainPane.getChildren().add(1,pane);
        });
    }
}
