package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewAllPurchaseInvoiceControler implements Initializable {
		@FXML private AnchorPane mainFrame;
	    @FXML private TableView<PurchaseInvoice> table;
	    @FXML private TableColumn<PurchaseInvoice, Long> colBillNo;
	    @FXML private TableColumn<PurchaseInvoice, LocalDate> colDate;
	    @FXML private TableColumn<PurchaseInvoice,String> colInvoiceno;
	    @FXML private TableColumn<PurchaseInvoice,Double> colAmount;
	    @FXML private TableColumn<PurchaseInvoice,Double> colPaid;
	    @FXML private TableColumn<PurchaseInvoice,Double> colRemaining;
	    @FXML private Button btnPreview;
	    @FXML private Button btnBack;
	    private PurchaseInvoiceService service;
	    private ObservableList<PurchaseInvoice>list = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		service = new PurchaseInvoiceServiceImpl();
		colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colInvoiceno.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("grandtotal"));
		colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
		colRemaining.setCellValueFactory(new PropertyValueFactory<>("othercharges"));
		list.addAll( service.getAllPurchaseInvoice());
		table.setItems(list);
	}
	 @FXML
	    void btnBackAction(ActionEvent event) {
		 mainFrame.setVisible(false);
	    }

	    @FXML
	    void btnPreviewAction(ActionEvent event) {
	    	if(table.getSelectionModel().getSelectedItem()==null) return;
	    	PurchaseInvoice invoice = table.getSelectionModel().getSelectedItem();
	    	if(invoice==null) return;
	    	CommonData.previewInvoiceno = invoice.getBillno();
	    	new ViewUtil().showInvoicePreview(event);
	    	
	    }

}
