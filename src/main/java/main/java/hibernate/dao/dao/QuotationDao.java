package main.java.main.java.hibernate.dao.dao;

import java.time.LocalDate;
import java.util.List;

import main.java.main.java.hibernate.entities.Quotation;

public interface QuotationDao {
	int saveQuotation(Quotation quotation);
	long getNewQuotationNo();
	Quotation getQuotationById(long id);
	List<Quotation> getAllQuotations();
	List<Quotation> searchQuotations(String customerName, LocalDate startDate, LocalDate endDate);
	int markBilled(long id);
}
