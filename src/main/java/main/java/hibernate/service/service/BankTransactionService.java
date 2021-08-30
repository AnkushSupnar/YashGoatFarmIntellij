package main.java.main.java.hibernate.service.service;

import main.java.main.java.hibernate.dao.dao.BankTransactionDao;
import main.java.main.java.hibernate.entities.BankTransaction;

import java.time.LocalDate;
import java.util.List;

public interface BankTransactionService extends BankTransactionDao {
public BankTransaction getBankTransactionById(long id);
	
	public List<BankTransaction> getAllBankTransaction();
	public List<BankTransaction> getBankTransaction(int bankid);
	public List<BankTransaction>getAllDebitTransaction();
	public List<BankTransaction>getAllCreditTransaction();
	public List<BankTransaction>getAllDebitTransaction(int bankid);
	public List<BankTransaction>getAllCreditTransaction(int bankid);
	
	public int saveBankTransaction(BankTransaction btr);
	
	public void deleteBankTransaction(String particulars);
	public List<BankTransaction>getPeriodWiseBankTransaction(LocalDate fromDate, LocalDate toDate, int bankid);

	public float getOpenigBalance(int bankid,LocalDate date);
	public BankTransaction getTransactionByParticular(String particular,int bankid);
}
