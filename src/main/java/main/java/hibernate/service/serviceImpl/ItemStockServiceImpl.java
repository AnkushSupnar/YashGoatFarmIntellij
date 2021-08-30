package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.ItemStockDao;
import main.java.main.java.hibernate.dao.daoImpl.ItemStockDaoImpl;
import main.java.main.java.hibernate.entities.ItemStock;
import main.java.main.java.hibernate.service.service.ItemStockService;

import java.util.List;

public class ItemStockServiceImpl implements ItemStockService {

	private ItemStockDao dao;
	public ItemStockServiceImpl() {
		this.dao = new ItemStockDaoImpl();
	}
	@Override
	public ItemStock getItemStockById(long id) {
		return dao.getItemStockById(id);
	}

	@Override
	public ItemStock getItemStockByItemName(String name) {
		return dao.getItemStockByItemName(name);
	}

	@Override
	public int saveItemStock(ItemStock stock) {
		return dao.saveItemStock(stock);
	}

	@Override
	public List<ItemStock> getAllItemStock() {
		return dao.getAllItemStock();
	}

	@Override
	public float getItemStock(String name) {
		return dao.getItemStock(name);
	}
	@Override
	public List<String> getItemNames() {
		return dao.getItemNames();
	}
	@Override
	public int reduceItemStock(String itemname, float qty) {
		return dao.reduceItemStock(itemname, qty);
	}

}
