package main.java.main.java.controller.masterReport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML private StackPane titlePane;

    @FXML private DatePicker dateReport;
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


    private ProgressBar progress;
    private ViewUtil viewUtil;
    private Pane pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewUtil = new ViewUtil();
        progress = new ProgressBar();
        //progress.setLayoutX(progressBar.getLayoutX());
       // progress.setLayoutY(progressBar.getLayoutY());
        progress.setMaxSize(400,20);
        titlePane.getChildren().add(progress);
        progress.setVisible(false);
        progressBar.setVisible(false);
        dateReport.setValue(LocalDate.now());
        CommonData.dashboardDate = dateReport.getValue();
        dateReport.setOnAction(e->{
            if(dateReport.getValue()!=null)
            {
                CommonData.dashboardDate = dateReport.getValue();
            }
        });
        paneToday.getChildren().setAll(getNode("masterreport/DayWiseDashboard"));
        //pane.setSi);
        tabWeek.setOnSelectionChanged(e->{
          //  progress.setVisible(true);
            System.out.println("Select Week");
            paneWeek.getChildren().clear();
            paneWeek.getChildren().setAll(getNode("masterreport/WeekDashboard"));
        });
        tabToday.setOnSelectionChanged(e->{
            System.out.println("Select Week");
            paneToday.getChildren().clear();
            paneToday.getChildren().setAll(getNode("masterreport/DayWiseDashboard"));
        });
        tabMonth.setOnSelectionChanged(e->{
            System.out.println("Select Month");
            paneMonth.getChildren().clear();
            paneMonth.getChildren().setAll(getNode("masterreport/MonthDashboard"));
        });
        tabYear.setOnSelectionChanged(e->{
            System.out.println("Select Month");
            paneYear.getChildren().clear();
            paneYear.getChildren().setAll(getNode("masterreport/YearDashboard"));
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
    public void showProgress()
    {
        System.out.println("Show Progress");
        progress.setVisible(true);
    }
    public void hideProgress()
    {
        progress.setVisible(false);
    }
}
