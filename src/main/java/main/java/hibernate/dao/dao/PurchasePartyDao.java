package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.PurchaseParty;

import java.util.List;

public interface PurchasePartyDao {
	public PurchaseParty getPurchasePartyById(int id);
	public PurchaseParty getPurchasePartyByName(String name);
	public List<PurchaseParty> getAllPurchaseParty();
	public List<String> getAllPurchasePartyNames();
	
	public int savePurchaseParty(PurchaseParty party);
}
