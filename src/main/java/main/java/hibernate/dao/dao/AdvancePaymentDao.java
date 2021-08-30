package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.AdvancePayment;

import java.time.LocalDate;
import java.util.List;

public interface AdvancePaymentDao {

	AdvancePayment getAdvancePaymentById(long id);
	List<AdvancePayment>getAllAdvancePayment();
	List<AdvancePayment> getPartyWiseAdvancePayment(int partyId);
	List<AdvancePayment>getDateWiseAdvancePayment(LocalDate date);
	List<AdvancePayment>getDatePartyWiseAdvancePayment(LocalDate date,int party);
	int saveAdvancePayment(AdvancePayment payment);
	double getPartyAdvancePayment(int partyId);
	
}
