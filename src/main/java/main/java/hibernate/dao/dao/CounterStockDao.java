package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.CounterStock;

import java.time.LocalDate;
import java.util.List;

public interface CounterStockDao {

	List<CounterStock> getAllCounterStock();
	CounterStock getCounterStockById(long id);
	List<CounterStock>getCounterStockByDate(LocalDate date);
	List<CounterStock>getCounterStockByDatePeriod(LocalDate start,LocalDate end);
	int saveCounterStock(CounterStock stock);
	void deleteTransaction(long stockid);
	float getAvailableCounterStock(String itemname);
	
	
}
