package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.PurchaseInvoice;

import java.time.LocalDate;
import java.util.List;

public interface PurchasInvoiceDao {
	public PurchaseInvoice getPurchaseInvoice(long billno);
	public List<PurchaseInvoice> getAllPurchaseInvoice();
	public List<PurchaseInvoice>getDateWisePurchaseInvoice(LocalDate date);
	public List<PurchaseInvoice>getMonthWisePurchaseInvoice(LocalDate date);
	public List<PurchaseInvoice>getThisYearPurchaseInvoice();
	public List<PurchaseInvoice>getThisWeekInvoice();
	public List<PurchaseInvoice>getPartyWiseUnpaidPurchaseInvoice(int partyId);
	public int savePurchaseInvoice(PurchaseInvoice purchaseInvoice);
	public int updatePaidAmount(long billno,float amount);
	public long getNewInvoiceNo();
	public void deleteTransaction(long billno);
	
	public List<PurchaseInvoice>getPurchaseInvoicePartyWise(LocalDate date,int partyid);
	public double getAllPaidAmountByparty(int partyId);
	public double getPartyUnpaidAmount(int partyId);

	public List<PurchaseInvoice>getPartyPeriodPurchaseInvoice(int partyid,LocalDate start,LocalDate end);
	public List<PurchaseInvoice>getPeriodPurchaseInvoice(LocalDate start,LocalDate end);
	public List<PurchaseInvoice>getPartyAllPurchaseInvoice(int partyid);

	public double getAveragePurchaseRate(String itemname);
}
