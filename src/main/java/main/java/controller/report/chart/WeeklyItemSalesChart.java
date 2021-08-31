package main.java.main.java.controller.report.chart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import main.java.main.java.hibernate.reportEntity.WeeklyItemSales;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class WeeklyItemSalesChart implements Initializable  {

	 	@FXML private BarChart<String, Float> barChartAllItems;

	    @FXML private PieChart pieChartAllItems;

	    @FXML private BarChart<String ,Float> barChartStickItems;

	    @FXML private PieChart pieChartStickItems;
	    
	    private XYChart.Series<String, Float> series,series2;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//for Items
				series = new XYChart.Series<String, Float>();		
				for(WeeklyItemSales tr: CommonData.weeklyItemSaleList)
				{			
					series.getData().add(new XYChart.Data<String, Float>(tr.getItemName(),tr.getQty()));	
				}
				LocalDate date = CommonData.weeklyItemSaleList.get(0).getDate();
				barChartAllItems.setTitle("Weekly All Items Sales ("+date.with(DayOfWeek.MONDAY)+" to "+date.with(DayOfWeek.SUNDAY)+")");
				barChartAllItems.getData().add(series);
				
				//for Stick Items
				series2 = new XYChart.Series<String, Float>();		
				for(WeeklyItemSales tr:CommonData.weeklyItemSaleStickList)
				{			
					series2.getData().add(new XYChart.Data<String, Float>(tr.getItemName(),tr.getQty()));	
				}
				date = CommonData.weeklyItemSaleStickList.get(0).getDate();
				barChartStickItems.setTitle("Weekly All Items Sales ("+date.with(DayOfWeek.MONDAY)+" to "+date.with(DayOfWeek.SUNDAY)+")");
				barChartStickItems.getData().add(series2);
		
				//pie chart for all items
				ObservableList<PieChart.Data>pieChartData = FXCollections.observableArrayList();
				for(WeeklyItemSales tr:CommonData.weeklyItemSaleList)
				{			
					pieChartData.add(new PieChart.Data(tr.getItemName()+" Quantity:"+tr.getQty()+" "+tr.getUnit(),tr.getQty()));
				}
				pieChartAllItems.setTitle("Weekly All Items Sales ("+date.with(DayOfWeek.MONDAY)+" to "+date.with(DayOfWeek.SUNDAY)+")");
				pieChartAllItems.setData(pieChartData);
				
				//pie Chart for Stick items
				ObservableList<PieChart.Data>pieChartData2 = FXCollections.observableArrayList();
				for(WeeklyItemSales tr:CommonData.weeklyItemSaleStickList)
				{			
					pieChartData2.add(new PieChart.Data(tr.getItemName()+" Quantity:"+tr.getQty()+" "+tr.getUnit(),tr.getQty()));
				}
				pieChartStickItems.setTitle("Weekly Stick Items Sales ("+date.with(DayOfWeek.MONDAY)+" to "+date.with(DayOfWeek.SUNDAY)+")");
				pieChartStickItems.setData(pieChartData2);
				
	}

}
