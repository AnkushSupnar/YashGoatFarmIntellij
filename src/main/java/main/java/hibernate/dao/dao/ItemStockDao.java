package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.ItemStock;

import java.util.List;

public interface ItemStockDao {
	public ItemStock getItemStockById(long id);
	public ItemStock getItemStockByItemName(String name);
	public int saveItemStock(ItemStock stock);
	public List<ItemStock> getAllItemStock();
	public float getItemStock(String name);
	public List<String> getItemNames();
	public int reduceItemStock(String itemname,float qty);
}
