package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.CustomerDao;
import main.java.main.java.hibernate.dao.daoImpl.CustomerDaoImpl;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.service.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

	private CustomerDao dao;
	public CustomerServiceImpl() {
		this.dao = new CustomerDaoImpl();
	}
	@Override
	public Customer getCustomerbyId(int id) {
		return dao.getCustomerbyId(id);
	}

	@Override
	public Customer getCustomerByName(String name) {
		String n[] = name.split(" ");
		if(n.length!=3)
			return null;
		else			
		return dao.getCustomerByName(n[0], n[1], n[2]);
				
	}

	@Override
	public List<Customer> getAllCustomer() {
		return dao.getAllCustomer();
	}

	@Override
	public List<String> getAllCustomerNames() {
		return dao.getAllCustomerNames();
	}

	@Override
	public int saveCustomer(Customer customer) {
		return dao.saveCustomer(customer);
	}
}
