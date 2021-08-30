package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.CustomerAdvancePaymentDao;
import main.java.main.java.hibernate.dao.daoImpl.CustomerAdvancePaymentDaoImpl;
import main.java.main.java.hibernate.entities.CustomerAdvancePayment;
import main.java.main.java.hibernate.service.service.CustomerAdvancePaymentService;

import java.time.LocalDate;
import java.util.List;

public class CustomerAdvancePaymentServiceImpl implements CustomerAdvancePaymentService {

	private CustomerAdvancePaymentDao dao;
	public CustomerAdvancePaymentServiceImpl()
	{
		this.dao = new CustomerAdvancePaymentDaoImpl();
	}
	@Override
	public CustomerAdvancePayment getCustomerAdvanceById(long id) {
		return dao.getCustomerAdvanceById(id);
	}

	@Override
	public List<CustomerAdvancePayment> getAllCustomerAdvance() {
		return dao.getAllCustomerAdvance();
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByCustomer(int id) {
		return dao.getCustomerAdvanceByCustomer(id);
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByDate(LocalDate date) {
		return dao.getCustomerAdvanceByDate(date);
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByDatePeriod(LocalDate start, LocalDate end) {
		return dao.getCustomerAdvanceByDatePeriod(start, end);
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByCustomerAndDatePeriod(int customerid, LocalDate start, LocalDate end) {
		return dao.getCustomerAdvanceByCustomerAndDatePeriod(customerid,start,end);
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByDatePeriodAndCustomer(LocalDate start, LocalDate end,
			int customerid) {
		return dao.getCustomerAdvanceByDatePeriodAndCustomer(start, end, customerid);
	}

	@Override
	public int saveCustomerAdvance(CustomerAdvancePayment payment) {
		return dao.saveCustomerAdvance(payment);
	}
	@Override
	public double getCustomerTotalAdvance(int customerId) {
		return dao.getCustomerTotalAdvance(customerId);
	}

}
