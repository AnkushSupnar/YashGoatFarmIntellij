package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.entities.PurchaseTransaction;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.util.ResourceBundle;

public class PurchaseInvoicePreviewControler implements Initializable {

	@FXML private AnchorPane mainFrame;
    @FXML private TextField txtBillNo;
    @FXML private TextField txtInvoiceNo;
    @FXML private DatePicker date;
    @FXML private TextField txtPartyName;
    @FXML private TextArea txtAddress;
    
    @FXML private TableView<PurchaseTransaction> table;
    @FXML private TableColumn<PurchaseTransaction, Long> colSrNo;
    @FXML private TableColumn<PurchaseTransaction, String> colItemName;
    @FXML private TableColumn<PurchaseTransaction, Double> colQty;
    @FXML private TableColumn<PurchaseTransaction, String> colUnit;
    @FXML private TableColumn<PurchaseTransaction, Double> colRate;
    @FXML private TableColumn<PurchaseTransaction, Double> colAmount;
    
    @FXML private TextField txtNetTotal;
    @FXML private TextField txtOtherCharges;
    @FXML private TextField txtTransport;
    @FXML private TextField txtGrand;
    @FXML private TextField txtPaid;
    @FXML private TextField txtRemaining;
    @FXML private TextField txtBankName;
    private PurchaseInvoiceService purchaseService;
    private ObservableList<PurchaseTransaction>trList = FXCollections.observableArrayList();
    private PurchaseInvoice invoice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		purchaseService = new PurchaseInvoiceServiceImpl();
		invoice = purchaseService.getPurchaseInvoice(CommonData.previewInvoiceno);
		
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id")); 
		colItemName.setCellValueFactory(new PropertyValueFactory<>("itemname"));
		colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
		colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		if(invoice==null)
		{
			return;
		}
		trList.addAll(invoice.getTransaction());
		int srNo=0;
		for(int i=0;i<trList.size();i++)
		{
			trList.get(i).setId(++srNo);
		}
		table.setItems(trList);
		txtAddress.setText(invoice.getParty().getAddress());
		txtBankName.setText(invoice.getBank().getBankname());
		txtBillNo.setText(""+invoice.getBillno());
		txtGrand.setText(""+invoice.getGrandtotal());
		txtInvoiceNo.setText(invoice.getInvoiceNo());
		txtNetTotal.setText(""+invoice.getNettotal());
		txtOtherCharges.setText(""+invoice.getOthercharges());
		txtPaid.setText(""+invoice.getPaid());
		txtPartyName.setText(invoice.getParty().getName());
		txtRemaining.setText(""+(
				Double.parseDouble(txtGrand.getText())-Double.parseDouble(txtPaid.getText())
				));
		txtTransport.setText(""+invoice.getTransportcharges());
		date.setValue(invoice.getDate());
		
	}

}
