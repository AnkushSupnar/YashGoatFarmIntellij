package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.Bank;

import java.util.List;

public interface BankDao {
	public Bank getBankById(int id);
	public Bank getBankByName(String name);
	public List<Bank> getAllBanks();
	public List<String>getAllBankNames();
	public float getBankBalance(int id);
	
	public int saveBank(Bank bank);
	public void addBankBalance(int id,float amount);
	public void reduceBankBalance(int id,float amount);
	public Bank getCashAccount();
}
