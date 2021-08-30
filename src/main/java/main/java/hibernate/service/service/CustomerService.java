package main.java.main.java.hibernate.service.service;

import main.java.main.java.hibernate.entities.Customer;

import java.util.List;

public interface CustomerService {
	public Customer getCustomerbyId(int id);
	public Customer getCustomerByName(String name);
	public List<Customer> getAllCustomer();
	public List<String> getAllCustomerNames();
	
	public int saveCustomer(Customer customer);
}
