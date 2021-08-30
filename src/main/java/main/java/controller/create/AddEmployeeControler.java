package main.java.main.java.controller.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddEmployeeControler implements Initializable {

	@FXML
    private AnchorPane mainPage;
	@FXML
	private TextField txtFirstName;
	@FXML
	private TextField txtMiddleName;
	@FXML
	private TextField txtLastName;
	@FXML
	private TextField txtMobileNo;
	@FXML
	private TextField txtAlternateMobileNo;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtAddress;
	@FXML
	private TextField txtCity;
	@FXML
	private TextField txtTaluka;
	@FXML
	private TextField txtDistrict;
	@FXML
	private TextField txtPin;
	@FXML
	private ComboBox<String> cmbJobPost;
	@FXML
	private ComboBox<String> cmbSalaryDuration;
	@FXML
	private ComboBox<String> cmbSalaryType;
	@FXML
	private TextField txtSalary;
	@FXML
	private DatePicker dateOfJoin;
	@FXML
	private DatePicker dateOfExit;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnClear;
	@FXML
	private Button btnEdit;
	@FXML
	private Button btnExit;
	@FXML
	private TableView<Employee> table;

	@FXML
	private TableColumn<Employee, Integer> colSrNo;

	@FXML
	private TableColumn<Employee, String> colName;

	@FXML
	private TableColumn<Employee, String> colAddress;

	@FXML
	private TableColumn<Employee, String> colMobileNo;

	@FXML
	private TableColumn<Employee, String> colEmail;

	@FXML
	private TableColumn<Employee, String> colPost;

	@FXML
	private TableColumn<Employee, String> colSalaryType;

	@FXML
	private TableColumn<Employee, String> colSalaryDuration;

	@FXML
	private TableColumn<Employee, Float> colSalary;

	@FXML
	private TableColumn<Employee, LocalDate> colDoj;

	@FXML
	private TableColumn<Employee, LocalDate> colDoe;

	private EmployeeService service;
	private ObservableList<Employee> employeeList = FXCollections.observableArrayList();
	private int id;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colSrNo.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("id"));
		colName.setCellValueFactory(new PropertyValueFactory<Employee, String>("fname"));
		colMobileNo.setCellValueFactory(new PropertyValueFactory<Employee, String>("mobileno"));
		colEmail.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
		colAddress.setCellValueFactory(new PropertyValueFactory<Employee, String>("address"));
		colDoj.setCellValueFactory(new PropertyValueFactory<Employee, LocalDate>("joiningdate"));
		colDoe.setCellValueFactory(new PropertyValueFactory<Employee, LocalDate>("exitdate"));
		colPost.setCellValueFactory(new PropertyValueFactory<Employee, String>("jobpost"));
		colSalaryType.setCellValueFactory(new PropertyValueFactory<Employee, String>("salarytype"));
		colSalaryDuration.setCellValueFactory(new PropertyValueFactory<Employee, String>("salarytime"));
		colSalary.setCellValueFactory(new PropertyValueFactory<Employee, Float>("salary"));

		cmbJobPost.getItems().add("Manager");
		cmbJobPost.getItems().add("Salesman");
		cmbJobPost.getItems().add("Accountant");
		cmbJobPost.getItems().add("Labour");

		cmbSalaryDuration.getItems().add("Monthly");
		cmbSalaryDuration.getItems().add("Weekly");

		cmbSalaryType.getItems().add("Fix Salary");
		cmbSalaryType.getItems().add("Commision");

		service = new EmployeeServiceImpl();
		if (service.getAllEmployee() != null) {
			employeeList.addAll(service.getAllEmployee());
			for (Employee e : employeeList) {
				e.setFname(e.getFname() + " " + e.getMname() + " " + e.getLname());
				e.setAddress(e.getAddress() + "," + e.getCity() + "," + e.getTaluka() + "," + e.getDistrict() + ","
						+ e.getPin());
				e.setMobileno(e.getMobileno() + " / " + e.getAltermobileno());
			}
		}
		table.setItems(employeeList);
		id = 0;
	}

	@FXML
	void selectSalaryType(ActionEvent event) {
		if (cmbSalaryType.getValue()!=null && cmbSalaryType.getValue().equals("Fix Salary")) {
			txtSalary.setDisable(false);
		} else {
			txtSalary.setText("" + 0);
			txtSalary.setDisable(true);
		}

	}

	@FXML
	void clear(ActionEvent event) {
		clear();
	}

	@FXML
	void edit(ActionEvent event) {
		
		Employee e = service.getEmployeeById(table.getSelectionModel().getSelectedItem().getId());
		System.out.println(e);
		txtAddress.setText(e.getAddress());
		txtAlternateMobileNo.setText(e.getAltermobileno());
		txtCity.setText(e.getCity());
		txtDistrict.setText(e.getDistrict());
		txtEmail.setText(e.getEmail());
		txtFirstName.setText(e.getFname());
		txtLastName.setText(e.getLname());
		txtMiddleName.setText(e.getMname());
		txtMobileNo.setText(e.getMobileno());
		txtPin.setText(""+e.getPin());
		txtSalary.setText(""+e.getSalary());
		txtSalary.setDisable(true);
		txtTaluka.setText(e.getTaluka());
		cmbJobPost.getSelectionModel().clearSelection();
		cmbJobPost.setValue(e.getJobpost());
		cmbSalaryDuration.getSelectionModel().clearSelection();
		cmbSalaryDuration.setValue(e.getSalarytime());
		cmbSalaryType.getSelectionModel().clearSelection();
		cmbSalaryType.setValue(e.getSalarytype());
		dateOfJoin.setValue(e.getJoiningdate());
		dateOfExit.setValue(e.getExitdate());
		id = e.getId();
	}

	@FXML
	void exit(ActionEvent event) {
		
		mainPage.setVisible(false);
		
	}

	@FXML
	void save(ActionEvent event) {
		try {
			if (validateData() == 0) {
				return;
			}
			Employee e = new Employee();
			e.setId(id);
			e.setAddress(txtAddress.getText().trim());
			e.setAltermobileno(txtAlternateMobileNo.getText().trim());
			e.setCity(txtCity.getText().trim());
			e.setDistrict(txtDistrict.getText().trim());
			e.setEmail(txtEmail.getText().trim());
			e.setExitdate(dateOfExit.getValue());
			e.setFname(txtFirstName.getText().trim());
			e.setJobpost(cmbJobPost.getValue().trim());
			e.setJoiningdate(dateOfJoin.getValue());
			e.setLname(txtLastName.getText().trim());
			e.setMname(txtMiddleName.getText().trim());
			e.setMobileno(txtMobileNo.getText().trim());
			e.setPin(Integer.parseInt(txtPin.getText().trim()));
			e.setPost(cmbJobPost.getValue());
			e.setSalary(Float.parseFloat(txtSalary.getText().trim()));
			e.setSalarytime(cmbSalaryDuration.getValue());
			e.setSalarytype(cmbSalaryType.getValue());
			e.setTaluka(txtTaluka.getText().trim());
			//System.out.println(e);

			int flag = service.saveEmployee(e);
			e.setFname(e.getFname() + " " + e.getMname() + " " + e.getLname());
			e.setAddress(e.getAddress() + "," + e.getCity() + "," + e.getTaluka() + "," + e.getDistrict() + ","
					+ e.getPin());
			e.setMobileno(e.getMobileno() + " / " + e.getAltermobileno());
			if (flag == 1) {
				new Alert(Alert.AlertType.INFORMATION, "Employee Saved").showAndWait();
				employeeList.add(e);
				clear();
			}
			if(flag==2)
			{
				new Alert(Alert.AlertType.INFORMATION, "Employee Updated Succes").showAndWait();
				int index=-1;
				for(int i=0;i<employeeList.size();i++)
				{
					if(employeeList.get(i).getId()==e.getId())
					{
						index=i;
						break;
					}
				}
				if(index!=-1)
				{
					employeeList.remove(index);
					employeeList.add(index, e);
				}
				clear();
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int validateData() {
		try {
			if (txtFirstName.getText().equals("") || txtFirstName.getText().equals(null)) {
				new Alert(Alert.AlertType.ERROR, "Enter First Name").showAndWait();
				txtFirstName.requestFocus();
				return 0;
			}
			if (txtMiddleName.getText().equals("") || txtMiddleName.getText().equals(null)) {
				new Alert(Alert.AlertType.ERROR, "Enter Middle Name").showAndWait();
				txtMiddleName.requestFocus();
				return 0;
			}
			if (txtLastName.getText().equals("") || txtLastName.getText().equals(null)) {
				new Alert(Alert.AlertType.ERROR, "Enter Last Name").showAndWait();
				txtLastName.requestFocus();
				return 0;
			}
			if (txtMobileNo.getText().equals("") || txtMobileNo.getText().equals(null)) {
				new Alert(Alert.AlertType.ERROR, "Enter Mobile No").showAndWait();
				txtMobileNo.requestFocus();
				return 0;
			}
			if (!txtMobileNo.getText().matches("\\d{8}||\\d{10}||\\d{11}||\\d{12}")) {
				new Alert(Alert.AlertType.ERROR, "Enter Correct Mobile No").showAndWait();
				txtMobileNo.requestFocus();
				return 0;
			}
			if (txtAlternateMobileNo.getText().equals("") || txtAlternateMobileNo.getText().equals(null)) {
				txtAlternateMobileNo.setText("-");
			}
			if (txtEmail.getText().equals("") || txtEmail.getText().equals(null)) {
				txtEmail.setText("-");
			}
			if (txtAddress.getText().equals("") || txtAddress.getText().equals(null)) {
				txtAddress.setText("-");
			}
			if (txtCity.getText().equals("") || txtCity.getText().equals(null)) {
				new Alert(Alert.AlertType.ERROR, "Enter City / Village Name").showAndWait();
				txtCity.requestFocus();
				return 0;
			}
			if (txtTaluka.getText().equals("") || txtTaluka.getText().equals(null)) {
				new Alert(Alert.AlertType.ERROR, "Enter Taluka Name").showAndWait();
				txtTaluka.requestFocus();
				return 0;
			}
			if (txtDistrict.getText().equals("") || txtDistrict.getText().equals(null)) {
				new Alert(Alert.AlertType.ERROR, "Enter District Name").showAndWait();
				txtDistrict.requestFocus();
				return 0;
			}
			if (txtPin.getText().equals("") || txtPin.getText().equals(null)) {
				txtPin.setText("" + 0);
			}
			if (dateOfJoin.getValue() == null) {
				new Alert(Alert.AlertType.ERROR, "Enter Joining Date").showAndWait();
				dateOfJoin.requestFocus();
				return 0;
			}
			if (cmbJobPost.getValue() == null) {
				new Alert(Alert.AlertType.ERROR, "Select Job Post").showAndWait();
				cmbJobPost.requestFocus();
				return 0;
			}
			if (cmbSalaryDuration.getValue() == null) {
				new Alert(Alert.AlertType.ERROR, "Select cmbSalary Duration").showAndWait();
				cmbSalaryDuration.requestFocus();
				return 0;
			}
			if (cmbSalaryDuration.getValue().equalsIgnoreCase("Fix Salary") && txtSalary.getText().equals("")) {
				new Alert(Alert.AlertType.ERROR, "Enter Salary").showAndWait();
				txtSalary.requestFocus();
				return 0;
			}
			if (dateOfExit.getValue() == null) {
				dateOfExit.setValue(dateOfJoin.getValue().minusMonths(1));
			}
			return 1;
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR, "Error").showAndWait();
			return 0;
		}
	}

	private void clear() {
		txtAddress.setText("");
		txtAlternateMobileNo.setText("");
		txtCity.setText("");
		txtDistrict.setText("");
		txtEmail.setText("");
		txtFirstName.setText("");
		txtLastName.setText("");
		txtMiddleName.setText("");
		txtMobileNo.setText("");
		txtPin.setText("");
		
		txtTaluka.setText("");
		cmbJobPost.getSelectionModel().clearSelection();
		cmbSalaryDuration.getSelectionModel().clearSelection();
		cmbSalaryType.getSelectionModel().clearSelection();
		txtSalary.setText("");
		txtSalary.setDisable(true);
		id = 0;
	}
}
