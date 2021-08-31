package main.java.main.java.controller.report.chart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import main.java.main.java.hibernate.reportEntity.DailyItemSales;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.util.ResourceBundle;

public class DailyItemSalesChart implements Initializable {

	 @FXML private BarChart<String,Float> barChart;
	 @FXML private CategoryAxis x;
	 @FXML private NumberAxis y;
	 
	 @FXML private BarChart<String,Float> stickBarChart;
	 
	 @FXML private PieChart pieChart;
	 @FXML private PieChart stickPieChart;
	 

	 private XYChart.Series<String, Float> series,series2;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//for Items
		series = new XYChart.Series<String, Float>();		
		for(DailyItemSales tr: CommonData.dailyItemSaleList)
		{			
			series.getData().add(new XYChart.Data<String, Float>(tr.getItemName(),tr.getQty()));	
		}
		barChart.getData().add(series);
		//for Stick Items
		series2 = new XYChart.Series<String, Float>();
		for(DailyItemSales tr:CommonData.dailyItemSaleStickList)
		{
			
			series2.getData().add(new XYChart.Data<String, Float>(tr.getItemName(),tr.getQty()));
					
		}
		stickBarChart.getData().add(series2);
		//for Items Pie Chart
		ObservableList<PieChart.Data>pieChartData = FXCollections.observableArrayList();
		for(DailyItemSales tr:CommonData.dailyItemSaleList)
		{			
			pieChartData.add(new PieChart.Data(tr.getItemName()+" Quantity:"+tr.getQty()+" "+tr.getUnit(),tr.getQty()));
		}
		pieChart.setData(pieChartData);
		//pieChartData.forEach(data->data.nameProperty().bind(Bindings.concat(data.getName()," Quantity: ",data.getPieValue()," KG/NOS")));
		//for stick pie Chart
		ObservableList<PieChart.Data>stickPieChartData = FXCollections.observableArrayList();
		for(DailyItemSales tr:CommonData.dailyItemSaleStickList)
		{			
			stickPieChartData.add(new PieChart.Data(tr.getItemName()+" Quantity:"+tr.getQty()+" "+tr.getUnit(),tr.getQty()));
		}
		stickPieChart.setData(stickPieChartData);
		//stickPieChartData.forEach(data->data.nameProperty().bind(Bindings.concat(data.getName()," Quantity: ",data.getPieValue()," Nos")));
	}

}
