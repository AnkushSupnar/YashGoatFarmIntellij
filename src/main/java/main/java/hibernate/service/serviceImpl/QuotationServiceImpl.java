package main.java.main.java.hibernate.service.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import main.java.main.java.hibernate.dao.dao.QuotationDao;
import main.java.main.java.hibernate.dao.daoImpl.QuotationDaoImpl;
import main.java.main.java.hibernate.entities.Quotation;
import main.java.main.java.hibernate.service.service.QuotationService;

public class QuotationServiceImpl implements QuotationService {

	private QuotationDao dao;

	public QuotationServiceImpl() {
		this.dao = new QuotationDaoImpl();
	}

	@Override
	public int saveQuotation(Quotation quotation) {
		return dao.saveQuotation(quotation);
	}

	@Override
	public long getNewQuotationNo() {
		return dao.getNewQuotationNo();
	}

	@Override
	public Quotation getQuotationById(long id) {
		return dao.getQuotationById(id);
	}

	@Override
	public List<Quotation> getAllQuotations() {
		return dao.getAllQuotations();
	}

	@Override
	public List<Quotation> searchQuotations(String customerName, LocalDate startDate, LocalDate endDate) {
		return dao.searchQuotations(customerName, startDate, endDate);
	}

	@Override
	public int markBilled(long id) {
		return dao.markBilled(id);
	}
}
