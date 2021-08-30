package main.java.main.java.hibernate.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.main.java.hibernate.reportEntity.DailyItemSales;
import main.java.main.java.hibernate.reportEntity.WeeklyItemSales;
import main.java.main.java.hibernate.service.service.CounterStockDataService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.service.ItemStockService;
import main.java.main.java.hibernate.service.serviceImpl.CounterStockDataServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemStockServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class CommonData {
	public static long previewBillNo;
	public static long previewInvoiceno;
	private static ItemService itemService = new ItemServiceImpl();
	private static ItemStockService stockService = new ItemStockServiceImpl();
	public static ObservableList<String> stockItemNames = FXCollections.observableArrayList();
	public static ObservableList<String>itemNames = FXCollections.observableArrayList();
	public static List<DailyItemSales> dailyItemSaleStickList = new ArrayList<>();
	public static List<DailyItemSales> dailyItemSaleList = new ArrayList<>();
	public static List<WeeklyItemSales> weeklyItemSaleStickList = new ArrayList<>();
	 public static List<WeeklyItemSales> weeklyItemSaleList = new ArrayList<>();;
	 private static CounterStockDataService counterStockDataService = new CounterStockDataServiceImpl();
	public static void setStockItemNames()
	{
		stockItemNames.clear();
		//stockItemNames.addAll(stockService.getItemNames());
		stockItemNames.addAll(counterStockDataService.getAllCounterItemNames());
	}
	public static void setItemNames()
	{
		itemNames.clear();
		itemNames.addAll(itemService.getAllItemNames());
		
	}
	
}
