package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.BankTransferDao;
import main.java.main.java.hibernate.dao.daoImpl.BankTransferDaoImpl;
import main.java.main.java.hibernate.entities.BankTransfer;
import main.java.main.java.hibernate.service.service.BankTransferService;

import java.time.LocalDate;
import java.util.List;

public class BankTransferServiceImpl implements BankTransferService {

	private BankTransferDao dao;
	public BankTransferServiceImpl()
	{
		this.dao= new BankTransferDaoImpl();
	}
	@Override
	public BankTransfer getBankTransferById(int id) {
		return dao.getBankTransferById(id);
	}

	@Override
	public List<BankTransfer> getAllBankTransfer() {
		return dao.getAllBankTransfer();
	}

	@Override
	public List<BankTransfer> getBankTransferByDate(LocalDate date) {
		return dao.getBankTransferByDate(date);
	}

	@Override
	public List<BankTransfer> getBankTransferByDatePeriod(LocalDate from, LocalDate to) {
		return dao.getBankTransferByDatePeriod(from, to);
	}

	@Override
	public List<BankTransfer> getBankTransferByBank(int bankid) {
		return dao.getBankTransferByBank(bankid);
	}

	@Override
	public int saveBankTransfer(BankTransfer transfer) {
		return dao.saveBankTransfer(transfer);
	}

}
