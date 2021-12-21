package main.java.main.java.controller.masterReport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML private StackPane titlePane;

    @FXML private ProgressBar progressBar;
    @FXML private AnchorPane paneToday;
    @FXML private AnchorPane paneMonth;
    @FXML private AnchorPane paneYear;
    @FXML private AnchorPane paneAll;
    @FXML private AnchorPane paneWeek;


    @FXML private Tab tabAll;
    @FXML private Tab tabMonth;
    @FXML private Tab tabToday;
    @FXML private Tab tabWeek;
    @FXML private Tab tabYear;


    private ViewUtil viewUtil;
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewUtil = new ViewUtil();
        progressBar.setVisible(false);

        paneToday.getChildren().setAll(getNode("masterreport/TodayDashboard"));
        //pane.setSi);
        tabWeek.setOnSelectionChanged(e->{
            System.out.println("Select Week");
            paneWeek.getChildren().clear();
            paneWeek.getChildren().setAll(getNode("masterreport/WeekDashboard"));
        });
        tabToday.setOnSelectionChanged(e->{
            System.out.println("Select Week");
            paneToday.getChildren().clear();
            paneToday.getChildren().setAll(getNode("masterreport/TodayDashboard"));
        });




        //paneToday.getChildren().add(pane);

    }
    private Node getNode(String filePath)
    {
        //Node node = viewUtil.getPage("masterreport/TodayDashboard");
        Node node = viewUtil.getPage(filePath);
        AnchorPane.setTopAnchor(node,0.0);
        AnchorPane.setLeftAnchor(node,0.0);
        AnchorPane.setBottomAnchor(node,0.0);
        AnchorPane.setRightAnchor(node,0.0);
        return node;
    }
}
