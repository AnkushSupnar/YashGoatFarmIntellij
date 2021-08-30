package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.Customer;

import java.util.List;

public interface CustomerDao {

	public Customer getCustomerbyId(int id);
	public Customer getCustomerByName(String fname,String mname,String lname);
	public List<Customer> getAllCustomer();
	public List<String> getAllCustomerNames();
	
	public int saveCustomer(Customer customer);
}
