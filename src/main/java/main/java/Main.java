package main.java.main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {

    String pagename;
    static Logger log = Logger.getLogger(Main.class.getName());
    @Override
    public void start(Stage primaryStage) throws Exception{
       // pagename="home/LoginFrame";
       // pagename="report/YearItemSalesReport";
       // pagename="report/YearItemSalesReport";
       // pagename = "report/labourcharges/WeeklyLabourChargesReport";
        //pagename = "report/labourcharges/MonthlyLabourChargesReport";
       // pagename = "report/labourcharges/YearlyLabourChargesReport";
       // pagename = "report/labourcharges/PeriodLabourChargesReport";
        //pagename = "report/CommisionFrame";

        pagename = "report/labourcharges/WeeklyLabourChargesReport";
      try {
//          Parent root = FXMLLoader.load(getClass().getResource("/view/" +pagename+".fxml"));
          Parent root = FXMLLoader.load(getClass().getResource("/view/"+pagename+".fxml"));
        // Parent root = FXMLLoader.load(Main.class.getResource("fxml"));
          Scene scene = new Scene(root);
          //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
          //	Image icon = new Image("D:\\Software\\Images\\350.jpg");
          //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/YashLogo.jpg")));
          primaryStage.setScene(scene);
          primaryStage.setTitle("Yash Goat Farm Management System");
          primaryStage.show();
      }catch (Exception e)
      {
          e.printStackTrace();
      }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
