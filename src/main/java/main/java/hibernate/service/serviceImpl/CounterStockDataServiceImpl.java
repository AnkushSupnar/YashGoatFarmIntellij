package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.CounterStockDataDao;
import main.java.main.java.hibernate.dao.daoImpl.CounterStockDataDaoImpl;
import main.java.main.java.hibernate.entities.CounterStockData;
import main.java.main.java.hibernate.service.service.CounterStockDataService;

import java.util.List;

public class CounterStockDataServiceImpl implements CounterStockDataService {

	private CounterStockDataDao dao;
	public CounterStockDataServiceImpl()
	{
		this.dao = new CounterStockDataDaoImpl();
	}
	
	@Override
	public float getCounterItemStock(String itemname) {
		return dao.getCounterItemStock(itemname);
	}

	@Override
	public List<CounterStockData> getAllCounterStockData() {
		return dao.getAllCounterStockData();
	}

	@Override
	public int saveCounterStockdata(CounterStockData counterStockData) {
		return dao.saveCounterStockdata(counterStockData);
	}

	@Override
	public int updateQuantity(String itemname, float newqty) {
		return dao.updateQuantity(itemname, newqty);
	}

	@Override
	public CounterStockData getItemNameWiseCounterStockData(String itemname) {
		
		return dao.getItemNameWiseCounterStockData(itemname);
	}

	@Override
	public List<String> getAllCounterItemNames() {
		return dao.getAllCounterItemNames(); 
	}

}
