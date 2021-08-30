package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.BankTransfer;

import java.time.LocalDate;
import java.util.List;
public interface BankTransferDao {

	BankTransfer getBankTransferById(int id);
	List<BankTransfer>getAllBankTransfer();
	List<BankTransfer>getBankTransferByDate(LocalDate date);
	List<BankTransfer>getBankTransferByDatePeriod(LocalDate from,LocalDate to);
	List<BankTransfer>getBankTransferByBank(int bankid);
	int saveBankTransfer(BankTransfer transfer);
	
	
}
