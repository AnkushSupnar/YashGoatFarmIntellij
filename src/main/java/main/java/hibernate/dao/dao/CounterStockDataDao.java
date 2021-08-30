package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.CounterStockData;

import java.util.List;

public interface CounterStockDataDao {

	float getCounterItemStock(String itemname);
	List<CounterStockData>getAllCounterStockData();
	int saveCounterStockdata(CounterStockData counterStockData);
	int updateQuantity(String itemname,float newqty);
	CounterStockData getItemNameWiseCounterStockData(String itemname);
	List<String> getAllCounterItemNames();
}
