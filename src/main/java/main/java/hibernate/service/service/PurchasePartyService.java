package main.java.main.java.hibernate.service.service;

import main.java.main.java.hibernate.entities.PurchaseParty;

import java.util.List;

public interface PurchasePartyService {

	public PurchaseParty getPurchasePartyById(int id);
	public PurchaseParty getPurchasePartyByName(String name);
	public List<PurchaseParty> getAllPurchaseParty();
	public List<String> getAllPurchasePartyNames();
	
	public int savePurchaseParty(PurchaseParty party);
}
