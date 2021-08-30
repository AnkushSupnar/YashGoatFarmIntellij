package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.Employee;

import java.util.List;

public interface EmployeeDao {

	public Employee getEmployeeById(int id);
	public Employee getEmployeeByName(String name);
	public List<String> getAllEmployeeNames();
	public List<Employee>getAllEmployee();
	
	public List<String> getAllSalesmanNames();
	
	public int saveEmployee(Employee employee);
	
	public List<String>getAllLabourNames();
	
}
