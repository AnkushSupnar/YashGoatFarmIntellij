package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.EmployeeDao;
import main.java.main.java.hibernate.dao.daoImpl.EmployeeDaoImpl;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.service.service.EmployeeService;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

	private EmployeeDao dao;
	public  EmployeeServiceImpl() {
		this.dao = new EmployeeDaoImpl();

	}
	@Override
	public Employee getEmployeeById(int id) {
		return dao.getEmployeeById(id);
	}

	@Override
	public Employee getEmployeeByName(String name) {
		return dao.getEmployeeByName(name);
	}

	@Override
	public List<String> getAllEmployeeNames() {
		return dao.getAllEmployeeNames();
	}

	@Override
	public List<Employee> getAllEmployee() {
		return dao.getAllEmployee();
	}

	@Override
	public int saveEmployee(Employee employee) {
		return dao.saveEmployee(employee);
	}
	@Override
	public List<String> getAllSalesmanNames() {
		return dao.getAllSalesmanNames();
	}
	@Override
	public List<String> getAllLabourNames() {
		return dao.getAllLabourNames();
	}

}
