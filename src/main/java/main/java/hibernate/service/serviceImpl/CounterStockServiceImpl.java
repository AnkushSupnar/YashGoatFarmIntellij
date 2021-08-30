package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.CounterStockDao;
import main.java.main.java.hibernate.dao.daoImpl.CounterStockDaoImpl;
import main.java.main.java.hibernate.entities.CounterStock;
import main.java.main.java.hibernate.service.service.CounterStockService;

import java.time.LocalDate;
import java.util.List;

public class CounterStockServiceImpl implements CounterStockService {

	private CounterStockDao dao;
	public CounterStockServiceImpl()
	{
		this.dao = new CounterStockDaoImpl();
	}
	
	@Override
	public List<CounterStock> getAllCounterStock() {
		return dao.getAllCounterStock();
	}

	@Override
	public CounterStock getCounterStockById(long id) {
		return dao.getCounterStockById(id);
	}

	@Override
	public List<CounterStock> getCounterStockByDate(LocalDate date) {
		return dao.getCounterStockByDate(date);
	}

	@Override
	public List<CounterStock> getCounterStockByDatePeriod(LocalDate start, LocalDate end) {
		return dao.getCounterStockByDatePeriod(start, end);
	}

	@Override
	public int saveCounterStock(CounterStock stock) {
		return dao.saveCounterStock(stock);
	}

	@Override
	public void deleteTransaction(long stockid) {
		dao.deleteTransaction(stockid);
		
	}

	@Override
	public float getAvailableCounterStock(String itemname) {
	return dao.getAvailableCounterStock(itemname);
	}

}
