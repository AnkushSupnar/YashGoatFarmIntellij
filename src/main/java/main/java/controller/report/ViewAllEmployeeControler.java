package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewAllEmployeeControler implements Initializable {
	@FXML
    private TableView<Employee> table;
	@FXML
	private TableColumn<Employee,Integer> colSrNo;
	@FXML
	private TableColumn<Employee,String> colName;
	@FXML
	private TableColumn<Employee,String> colAddress;
	@FXML
	private TableColumn<Employee,String> colMobileNo;
	@FXML
	private TableColumn<Employee,String> colEmail;
	@FXML
	private TableColumn<Employee,String> colPost;
	@FXML
	private TableColumn<Employee,Double> colSalary;
	@FXML
	private TableColumn<Employee,String> colSalaryTyep;
	@FXML
	private TableColumn<Employee,String> colSalaryDuration;
	@FXML
	private TableColumn<Employee,LocalDate> colDoj;
	@FXML
	private TableColumn<Employee,LocalDate> colDoe;
	private EmployeeService service;
	private ObservableList<Employee>list = FXCollections.observableArrayList();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
		
		service = new EmployeeServiceImpl();
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colName.setCellValueFactory(new PropertyValueFactory<>("fname"));
		colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		colMobileNo.setCellValueFactory(new PropertyValueFactory<>("mobileno"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colPost.setCellValueFactory(new PropertyValueFactory<>("jobpost"));
		colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
		colSalaryTyep.setCellValueFactory(new PropertyValueFactory<>("salarytype"));
		colSalaryDuration.setCellValueFactory(new PropertyValueFactory<>("salarytime"));
		colDoj.setCellValueFactory(new PropertyValueFactory<>("joiningdate"));
		colDoe.setCellValueFactory(new PropertyValueFactory<>("exitdate"));
		
		list.addAll(service.getAllEmployee());
		
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setFname(list.get(i).getFname()+" "+
					list.get(i).getMname()+" "+
					list.get(i).getLname());
			list.get(i).setAddress(list.get(i).getAddress()+" "+
					list.get(i).getCity()+" "+
					list.get(i).getPost()+" "+
					list.get(i).getTaluka()+" "+
					list.get(i).getDistrict());
			list.get(i).setMobileno(list.get(i).getMobileno()+","+list.get(i).getAltermobileno());
					
		}
		
		table.setItems(list);
		
	}catch(Exception e)
	{
		e.printStackTrace();
	}
	}

}
