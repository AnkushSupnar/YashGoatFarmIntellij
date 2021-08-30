package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.PaymentReciept;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRecieptDao {

	PaymentReciept getPaymentRecieptById(long id);
	List<PaymentReciept> getAllPaymentReciept();
	List<PaymentReciept>getDateWisePaymentReciept(LocalDate date);
	List<PaymentReciept>getDatePeriodPaymentReciept(LocalDate start,LocalDate end);
	List<PaymentReciept>getNameWisePaymentReciept(String name);
	List<PaymentReciept>getBankWisePaymentReciept(int bankid);
	int savePaymentReciept(PaymentReciept reciept);
}
