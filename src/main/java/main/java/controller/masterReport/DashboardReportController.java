package main.java.main.java.controller.masterReport;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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


        //pane.setSi);

        paneToday.getChildren().setAll(getNode("masterreport/TodayDashboard"));
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
