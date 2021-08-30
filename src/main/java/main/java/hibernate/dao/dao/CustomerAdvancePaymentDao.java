package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.CustomerAdvancePayment;

import java.time.LocalDate;
import java.util.List;

public interface CustomerAdvancePaymentDao {

	CustomerAdvancePayment getCustomerAdvanceById(long id);
	List<CustomerAdvancePayment> getAllCustomerAdvance();
	List<CustomerAdvancePayment>getCustomerAdvanceByCustomer(int id);
	List<CustomerAdvancePayment>getCustomerAdvanceByDate(LocalDate date);
	List<CustomerAdvancePayment>getCustomerAdvanceByDatePeriod(LocalDate start,LocalDate end);
	List<CustomerAdvancePayment>getCustomerAdvanceByCustomerAndDatePeriod(int customerid,LocalDate start,LocalDate end);
	List<CustomerAdvancePayment>getCustomerAdvanceByDatePeriodAndCustomer(LocalDate start,LocalDate end,int customerid);
	int saveCustomerAdvance(CustomerAdvancePayment payment);
	double getCustomerTotalAdvance(int customerId);
}
