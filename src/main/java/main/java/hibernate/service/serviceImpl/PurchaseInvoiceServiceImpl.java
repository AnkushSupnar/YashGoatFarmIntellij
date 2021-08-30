package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.daoImpl.PurchaseInvoiceDaoImpl;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;

import java.time.LocalDate;
import java.util.List;

public class PurchaseInvoiceServiceImpl implements PurchaseInvoiceService {

	private PurchaseInvoiceDaoImpl dao;
	public PurchaseInvoiceServiceImpl()
	{
		this.dao = new PurchaseInvoiceDaoImpl();
	}
	@Override
	public PurchaseInvoice getPurchaseInvoice(long billno) {
		return dao.getPurchaseInvoice(billno);
	}

	@Override
	public List<PurchaseInvoice> getAllPurchaseInvoice() {
		return dao.getAllPurchaseInvoice();
	}

	@Override
	public List<PurchaseInvoice> getDateWisePurchaseInvoice(LocalDate date) {
		return dao.getDateWisePurchaseInvoice(date);
	}

	@Override
	public List<PurchaseInvoice> getMonthWisePurchaseInvoice(LocalDate date) {
		return dao.getMonthWisePurchaseInvoice(date);
	}

	@Override
	public List<PurchaseInvoice> getThisYearPurchaseInvoice() {
		return dao.getThisYearPurchaseInvoice();
	}

	@Override
	public List<PurchaseInvoice> getThisWeekInvoice() {
		return dao.getThisWeekInvoice();
	}

	@Override
	public int savePurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		return dao.savePurchaseInvoice(purchaseInvoice);
	}

	@Override
	public long getNewInvoiceNo() {
		return dao.getNewInvoiceNo();
	}

	@Override
	public void deleteTransaction(long billno) {
		dao.deleteTransaction(billno);	
	}
	@Override
	public List<PurchaseInvoice> getPartyWiseUnpaidPurchaseInvoice(int partyId) {
		return dao.getPartyWiseUnpaidPurchaseInvoice(partyId);
	}
	@Override
	public int updatePaidAmount(long billno, float amount) {
		return dao.updatePaidAmount(billno, amount);
	}
	@Override
	public List<PurchaseInvoice> getPurchaseInvoicePartyWise(LocalDate date, int partyid) {
		return dao.getPurchaseInvoicePartyWise(date, partyid);
	}
	@Override
	public double getAllPaidAmountByparty(int partyId) {
		return dao.getAllPaidAmountByparty(partyId);
	}
	@Override
	public double getPartyUnpaidAmount(int partyId) {
		return dao.getAllPaidAmountByparty(partyId);
	}

}
