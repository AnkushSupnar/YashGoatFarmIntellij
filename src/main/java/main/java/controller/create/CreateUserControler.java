package main.java.main.java.controller.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.Login;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.service.LoginService;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.LoginServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserControler implements Initializable {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private ComboBox<String> cmbEmployee;
	@FXML
	private TextField txtUserName;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private PasswordField txtRepassword;
	@FXML
	private Button btnCreate;

	@FXML
	private Button btnCancel;

	private LoginService service;
	private EmployeeService employeeService;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		service = new LoginServiceImpl();
		employeeService = new EmployeeServiceImpl();
		cmbEmployee.getItems().addAll(employeeService.getAllEmployeeNames());

	}
	
    
    @FXML
	void cancel(ActionEvent event) {
    	//mainPane.setVisible(false);
    	txtPassword.setText("");
    	txtRepassword.setText("");
    	txtUserName.setText("");
    	cmbEmployee.getSelectionModel().clearSelection();
	}

	@FXML
	void saveLogin(ActionEvent event) {
		if(validateData()!=1)
		{
			return;
		}
		Login login = new Login();
		login.setEmployee(new EmployeeServiceImpl().getEmployeeByName(cmbEmployee.getValue()));
		login.setPassword(txtPassword.getText().trim());
		login.setStatus("logout");
		login.setUsername(txtUserName.getText().trim());
		int flag=service.saveLogin(login);
		if(flag==1)
		{
			new Alert(Alert.AlertType.INFORMATION,"Login User Save Success").showAndWait();
			clear();
		}
	}
	private void clear()
	{
		txtUserName.setText("");
		cmbEmployee.getSelectionModel().clearSelection();
		txtPassword.setText("");
		
		txtRepassword.setText("");
		
	}
	private int validateData()
	{
		try {
			if(cmbEmployee.getValue()==null|| cmbEmployee.getSelectionModel().getSelectedItem().equals(""))
			{
				new Alert(Alert.AlertType.ERROR,"Select Employee").showAndWait();
				cmbEmployee.requestFocus();
				return 0;
			}
			if(txtUserName.getText().equals(""))
			{
				new Alert(Alert.AlertType.ERROR,"Select User Name for Login").showAndWait();
				txtUserName.requestFocus();
				return 0;
			}
			if(service.getLoginByName(txtUserName.getText())!=null)
			{
				new Alert(Alert.AlertType.ERROR,"User Name Already Exist Choose Another").showAndWait();
				txtUserName.requestFocus();
				return 0;
			}
			if(txtPassword.getText().equals(""))
			{
				new Alert(Alert.AlertType.ERROR,"Enter Password").showAndWait();
				txtPassword.requestFocus();
				txtPassword.setVisible(true);
				return 0;
			}

			if(txtRepassword.getText().equals(""))
			{
				new Alert(Alert.AlertType.ERROR,"Enter Re-Password").showAndWait();
				txtRepassword.requestFocus();
				txtRepassword.setVisible(true);
				return 0;
			}
			if(!txtPassword.getText().equals(txtRepassword.getText()))
			{
				new Alert(Alert.AlertType.ERROR,"Repassword Not Match With Password!!!").showAndWait();
				txtRepassword.requestFocus();
				txtRepassword.setVisible(true);
				return 0;
			}
			return 1;
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR,"Error").showAndWait();
			e.printStackTrace();
			return 0;
		}
	}
	

}
