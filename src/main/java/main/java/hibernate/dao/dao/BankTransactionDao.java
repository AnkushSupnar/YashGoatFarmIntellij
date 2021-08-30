package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.BankTransaction;

import java.time.LocalDate;
import java.util.List;





public interface BankTransactionDao {
	public BankTransaction getBankTransactionById(long id);
	public BankTransaction getBankTransactionByPartucular(String particular);
	public List<BankTransaction> getAllBankTransaction();
	public List<BankTransaction> getBankTransaction(int bankid);
	public List<BankTransaction>getAllDebitTransaction();
	public List<BankTransaction>getAllCreditTransaction();
	public List<BankTransaction>getAllDebitTransaction(int bankid);
	public List<BankTransaction>getAllCreditTransaction(int bankid);
	
	public int saveBankTransaction(BankTransaction btr);
	
	public void deleteBankTransaction(long id);
	
	public List<BankTransaction>getPeriodWiseBankTransaction(LocalDate fromDate,LocalDate toDate,int bankid);
	public float getOpenigBalance(int bankid,LocalDate date);
	public BankTransaction getTransactionByParticular(String particular,int bankid);
}
