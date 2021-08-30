package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.PaymentRecieptDao;
import main.java.main.java.hibernate.dao.daoImpl.PaymentReceiptDaoImpl;
import main.java.main.java.hibernate.entities.PaymentReciept;
import main.java.main.java.hibernate.service.service.PaymentRecieptService;

import java.time.LocalDate;
import java.util.List;

public class PaymentRecieptServiceImpl implements PaymentRecieptService {

	private PaymentRecieptDao dao;
	public PaymentRecieptServiceImpl()
	{
		this.dao= new PaymentReceiptDaoImpl();
	}
	@Override
	public PaymentReciept getPaymentRecieptById(long id) {
		return dao.getPaymentRecieptById(id);
	}

	@Override
	public List<PaymentReciept> getAllPaymentReciept() {
		return dao.getAllPaymentReciept();
	}

	@Override
	public List<PaymentReciept> getDateWisePaymentReciept(LocalDate date) {
		return dao.getDateWisePaymentReciept(date);
	}

	@Override
	public List<PaymentReciept> getDatePeriodPaymentReciept(LocalDate start, LocalDate end) {
		return dao.getDatePeriodPaymentReciept(start, end);
	}

	@Override
	public List<PaymentReciept> getNameWisePaymentReciept(String name) {
		return dao.getNameWisePaymentReciept(name);
	}

	@Override
	public List<PaymentReciept> getBankWisePaymentReciept(int bankid) {
		return dao.getBankWisePaymentReciept(bankid);
	}

	@Override
	public int savePaymentReciept(PaymentReciept reciept) {
		return dao.savePaymentReciept(reciept);
	}

}
