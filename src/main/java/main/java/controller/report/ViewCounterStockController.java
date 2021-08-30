package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.CounterStockData;
import main.java.main.java.hibernate.service.service.CounterStockDataService;
import main.java.main.java.hibernate.service.serviceImpl.CounterStockDataServiceImpl;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.ViewAllCounterStock;

import java.net.URL;
import java.util.ResourceBundle;


public class ViewCounterStockController implements Initializable {
	
		@FXML private AnchorPane mainPane;
		@FXML private Button btnPrint;
 	    @FXML private TableView<CounterStockData> table;
	    @FXML private TableColumn<CounterStockData,Integer> colSrNo;
	    @FXML private TableColumn<CounterStockData,String> colItemName;
 	    @FXML private TableColumn<CounterStockData,Double> colQuantity;
	    @FXML private TableColumn<CounterStockData,String> colUnit;
	    private CounterStockDataService service;
	    private ObservableList<CounterStockData>stockList = FXCollections.observableArrayList();
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			service = new CounterStockDataServiceImpl();
			colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
			colItemName.setCellValueFactory(new PropertyValueFactory<>("itemname"));
			colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
			colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
			stockList.addAll(service.getAllCounterStockData());
			table.setItems(stockList);
			btnPrint.setOnAction(e->{
				new ViewAllCounterStock();
				new PrintFile().openFile("D:\\Software\\Prints\\ViewAllCounterStock.pdf");
			});
		}
}
