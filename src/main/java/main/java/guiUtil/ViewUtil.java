package main.java.main.java.guiUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.main.java.Main;
import main.java.main.java.hibernate.entities.Login;

import java.io.IOException;
import java.net.URL;

public class ViewUtil
{
    private Pane view;
    public static Login login;

    public void changeWindow(ActionEvent event, String filename) throws IOException {
        URL fileUrl = Main.class.getResource("/view/" + filename + ".fxml");
        Parent parent = FXMLLoader.load(fileUrl);
       // Parent parent = FXMLLoader.load(getClass().getResource("/resources/view/" + filename + ".fxml"));
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(parent));
        window.setMaximized(true);
    }

    public Pane getPage(String fileName)
    {
        System.out.println("got to open "+fileName);
        view = null;
        try {
            URL fileUrl = Main.class.getResource("/view/" + fileName + ".fxml");
            if(fileUrl==null)
            {
                throw new java.io.FileNotFoundException("FXML not found on classpath: /view/" + fileName + ".fxml");
            }
            view = FXMLLoader.load(fileUrl);
            System.out.println("loaded OK: "+fileName);
        } catch (Throwable e) {
            System.out.println("ERROR loading page "+fileName+": "+e.getMessage());
            new Alert(Alert.AlertType.ERROR,"No Page "+fileName+" please Check fxmlLoader\n"+e.getMessage()).showAndWait();
            e.printStackTrace();
        }
        return view;
    }

    public void showBillPreview(ActionEvent event)
    {
        Stage stage = new Stage();
        Parent root;
        try {
            root = FXMLLoader.load(Main.class.getResource("/view/report/BillPreview.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Bill Preview");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {

                }
            });
        } catch (IOException e1) {
            new Alert(Alert.AlertType.ERROR,"Error in Loading data").showAndWait();
            e1.printStackTrace();
        }
    }
    public void showInvoicePreview(ActionEvent event)
    {
        Stage stage = new Stage();
        Parent root;
        try {
            root = FXMLLoader.load(Main.class.getResource("/view/report/PurchaseBillPreview.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Purchase Invoice Preview");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(
                    ((Node)event.getSource()).getScene().getWindow() );
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {

                }
            });
        } catch (IOException e1) {
            new Alert(Alert.AlertType.ERROR,"Error in Loading data").showAndWait();
            e1.printStackTrace();
        }
    }
}