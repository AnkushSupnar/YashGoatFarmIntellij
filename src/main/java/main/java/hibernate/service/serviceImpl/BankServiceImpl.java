package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.BankDao;
import main.java.main.java.hibernate.dao.daoImpl.BankDaoImpl;
import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.service.service.BankService;

import java.util.List;

public class BankServiceImpl implements BankService {

	private BankDao dao;

	public BankServiceImpl() {
		this.dao = new BankDaoImpl();
	}

	@Override
	public Bank getBankById(int id) {
		return dao.getBankById(id);
	}

	@Override
	public Bank getBankByName(String name) {
		return dao.getBankByName(name);
	}

	@Override
	public List<Bank> getAllBanks() {
		return dao.getAllBanks();
	}

	@Override
	public List<String> getAllBankNames() {
		return dao.getAllBankNames();
	}

	@Override
	public float getBankBalance(int id) {
		return dao.getBankBalance(id);
	}

	@Override
	public int saveBank(Bank bank) {
		return dao.saveBank(bank);
	}

	@Override
	public void addBankBalance(int id, float amount) {
		dao.addBankBalance(id, amount);
	}

	@Override
	public void reduceBankBalance(int id, float amount) {
		dao.reduceBankBalance(id, amount);
	}

	@Override
	public Bank getCashAccount() {
		return dao.getCashAccount();
	}

}
