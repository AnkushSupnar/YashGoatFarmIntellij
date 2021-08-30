package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.AdvancePaymentDao;
import main.java.main.java.hibernate.dao.daoImpl.AdvancePaymentDaoImpl;
import main.java.main.java.hibernate.entities.AdvancePayment;
import main.java.main.java.hibernate.service.service.AdvancePaymentService;

import java.time.LocalDate;
import java.util.List;

public class AdvancePaymentServiceImpl implements AdvancePaymentService {

	private AdvancePaymentDao dao;
	public AdvancePaymentServiceImpl() {
		this.dao = new AdvancePaymentDaoImpl();
	}
	@Override
	public AdvancePayment getAdvancePaymentById(long id) {
		return dao.getAdvancePaymentById(id);
	}

	@Override
	public List<AdvancePayment> getAllAdvancePayment() {
		return dao.getAllAdvancePayment();
	}

	@Override
	public List<AdvancePayment> getPartyWiseAdvancePayment(int partyId) {
		return dao.getDatePartyWiseAdvancePayment(null, partyId);
	}

	@Override
	public List<AdvancePayment> getDateWiseAdvancePayment(LocalDate date) {
		return dao.getDateWiseAdvancePayment(date);
	}

	@Override
	public List<AdvancePayment> getDatePartyWiseAdvancePayment(LocalDate date, int party) {
		return dao.getDatePartyWiseAdvancePayment(date, party);
	}

	@Override
	public int saveAdvancePayment(AdvancePayment payment) {
		return dao.saveAdvancePayment(payment);
	}
	@Override
	public double getPartyAdvancePayment(int partyId) {
		return dao.getPartyAdvancePayment(partyId);
	}

}
