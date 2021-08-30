package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.ItemDao;
import main.java.main.java.hibernate.dao.daoImpl.ItemDaoImpl;
import main.java.main.java.hibernate.entities.Item;
import main.java.main.java.hibernate.service.service.ItemService;

import java.util.List;

public class ItemServiceImpl implements ItemService {

	private ItemDao dao;
	public ItemServiceImpl() {
		this.dao = new ItemDaoImpl();
	}
	@Override
	public Item getItemById(int id) {
		return dao.getItemById(id);
	}

	@Override
	public Item getItemByName(String name) {
		return dao.getItemByName(name);
	}

	@Override
	public List<Item> getAllItems() {
		return dao.getAllItems();
	}

	@Override
	public List<String> getAllItemNames() {
		return dao.getAllItemNames();
	}

	@Override
	public int saveItem(Item item) {
		return dao.saveItem(item);
	}
	@Override
	public float getCommision(String itemName) {
		return dao.getCommision(itemName);
	}
	@Override
	public List<String> getCuttingItemNames() {
		return dao.getCuttingItemNames();
	}
	@Override
	public float getLabourCharges(String itemName) {
		return getItemByName(itemName).getLabourCharges();
	}
	@Override
	public List<String> getStockableItemNames() {
		return dao.getStockableItemNames();
	}

}
