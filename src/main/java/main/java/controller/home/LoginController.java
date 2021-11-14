package main.java.main.java.controller.home;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.service.service.LoginService;
import main.java.main.java.hibernate.service.serviceImpl.LoginServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ProgressIndicator loding;

    @FXML
    private ComboBox<String> cmbUserName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnCancel;

    @FXML
    private Hyperlink linkAddEmployee;

    private LoginService service;
    private ObservableList<String> userNameList;
    private ViewUtil viewUtil;
    private AlertNotification notification;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        service = new LoginServiceImpl();
        notification = new AlertNotification();
        userNameList = FXCollections.observableArrayList(service.getAllUserNames());

        System.out.println("List Size= " + userNameList.size());
        if (userNameList.size() == 0) {
            new Alert(Alert.AlertType.ERROR, "No User Found Click on Add User !!!").showAndWait();
            // Login l = new Login("Admin", "", employee, status)
            linkAddEmployee.setDisable(false);
            linkAddEmployee.setVisible(true);

            // linkAddEmployee.fire();


        }
        cmbUserName.setItems(userNameList);
        viewUtil = new ViewUtil();
    }

    @FXML
    void addEmployee(ActionEvent event) {
        try {
            new ViewUtil().changeWindow(event, "create/CreateUserFrame");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void login(ActionEvent event) {
        try {
            if (validateData() != 1) {
                return;
            }
            int flag = service.validateLogin(cmbUserName.getValue(), txtPassword.getText());
            if (flag == 1) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loding.setVisible(true);
                        loding.progressProperty();
                    }
                });
                //new Alert(Alert.AlertType.INFORMATION, "Login Success").show();
                notification.showSuccessMessage("Login Success");
                ViewUtil.login = service.getLoginByName(cmbUserName.getValue());
                //viewUtil.changeWindow(event, "home/Dashboard");
                viewUtil.changeWindow(event, "home/Home");


            } else {
                notification.showErrorMessage("Login Faild!");
                //new Alert(Alert.AlertType.ERROR, "Login Faild!").showAndWait();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "HomePage Not found").show();
            e.printStackTrace();
        }

    }
    @FXML
    void txtPasswordAction(ActionEvent event) {
        login(event);
    }
    public int validateData() {
        if (cmbUserName.getValue() == null || cmbUserName.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Select User Name").showAndWait();
            return 0;
        }
        if (txtPassword.getText().equals("") || txtPassword.getText() == null) {
            new Alert(Alert.AlertType.ERROR, "Enter Password ").showAndWait();
            return 0;
        }
        return 1;
    }

}