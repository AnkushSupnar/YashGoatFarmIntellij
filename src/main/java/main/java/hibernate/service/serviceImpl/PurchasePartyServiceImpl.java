package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.PurchasePartyDao;
import main.java.main.java.hibernate.dao.daoImpl.PurchasePartyDaoImpl;
import main.java.main.java.hibernate.entities.PurchaseParty;
import main.java.main.java.hibernate.service.service.PurchasePartyService;

import java.util.List;

public class PurchasePartyServiceImpl implements PurchasePartyService {

	private PurchasePartyDao dao;
	public PurchasePartyServiceImpl() {
	this.dao = new PurchasePartyDaoImpl();
	}
	@Override
	public PurchaseParty getPurchasePartyById(int id) {
		return dao.getPurchasePartyById(id);
	}
	@Override
	public PurchaseParty getPurchasePartyByName(String name) {
		return dao.getPurchasePartyByName(name);
	}
	@Override
	public List<PurchaseParty> getAllPurchaseParty() {
		return dao.getAllPurchaseParty();
	}
	@Override
	public List<String> getAllPurchasePartyNames() {
		return dao.getAllPurchasePartyNames();
	}
	@Override
	public int savePurchaseParty(PurchaseParty party) {
		return dao.savePurchaseParty(party);
	}
}
