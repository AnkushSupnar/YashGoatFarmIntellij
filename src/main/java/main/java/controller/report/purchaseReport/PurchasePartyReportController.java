package main.java.main.java.controller.report.purchaseReport;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.service.PurchasePartyService;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchasePartyServiceImpl;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PurchasePartyReportPrint;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PurchasePartyReportController implements Initializable {

    @FXML private ComboBox<String> cmbPartyName;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button btnShow;
    @FXML private Button btnShowAll;
    @FXML private Button btnPrint;
    @FXML private Button btnReset;
    @FXML private Button btnHome;
    @FXML private TextField txtTotal;
    @FXML private TableView<PurchaseInvoice> table;
    @FXML private TableColumn<PurchaseInvoice, Float> colSrno;
    @FXML private TableColumn<PurchaseInvoice,Long> colBillno;
    @FXML private TableColumn<PurchaseInvoice, LocalDate> colDate;
    @FXML private TableColumn<PurchaseInvoice,String> colInvoice;
    @FXML private TableColumn<PurchaseInvoice,String> colParty;
    @FXML private TableColumn<PurchaseInvoice,Float> colAmount;
    @FXML private TableColumn<PurchaseInvoice,Float> colPaid;
    private PurchasePartyService partyService;
    private PurchaseInvoiceService purchaseInvoiceService;
    private AlertNotification alert;
    private ObservableList<PurchaseInvoice>list = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partyService = new PurchasePartyServiceImpl();
        purchaseInvoiceService = new PurchaseInvoiceServiceImpl();
        alert = new AlertNotification();
        colSrno.setCellValueFactory(new PropertyValueFactory<>("gst"));
        colBillno.setCellValueFactory(new PropertyValueFactory<>("billno"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colInvoice.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        colParty.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getParty().getName()));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("grandtotal"));
        colPaid.setCellValueFactory(new PropertyValueFactory<>("paid"));
        table.setItems(list);

        cmbPartyName.getItems().addAll(partyService.getAllPurchasePartyNames());
        btnShow.setOnAction(e->show());
        btnShowAll.setOnAction(e->showAll());
        btnReset.setOnAction(e->reset());
        btnPrint.setOnAction(e->print());

    }

    private void print() {
        if(list.size()==0)
        {
            alert.showErrorMessage("No Data to Print");
            return;
        }
        new PurchasePartyReportPrint(list);
        new PrintFile().openFile("D:\\Software\\Prints\\PurchasePartyReport.pdf");
    }

    private void reset() {
        cmbPartyName.getSelectionModel().clearSelection();
        dateTo.setValue(null);
        dateFrom.setValue(null);
        list.clear();
        txtTotal.setText(""+0.0f);
        table.refresh();
    }

    private void showAll() {
        if(dateFrom.getValue()==null && dateTo.getValue()==null)
        {
            list.clear();
            txtTotal.setText(""+0.0f);
            list.addAll(purchaseInvoiceService.getAllPurchaseInvoice());
            resetSrNo();
            table.refresh();

            return;
        }
        if(dateFrom.getValue()!=null && dateTo.getValue()==null)//dateWise
        {
            list.clear();
            txtTotal.setText(""+0.0f);
            list.addAll(purchaseInvoiceService.getDateWisePurchaseInvoice(dateFrom.getValue()));
            resetSrNo();

            table.refresh();
            return;
        }
        if(dateFrom.getValue()!=null && dateTo.getValue()!=null)
        {
            txtTotal.setText(""+0.0f);
            list.clear();
            list.addAll(purchaseInvoiceService.getPeriodPurchaseInvoice(dateFrom.getValue(),dateTo.getValue()));
            resetSrNo();
            table.refresh();
            return;
        }
    }

    private void show() {
        if(cmbPartyName.getValue()==null)
        {
            alert.showErrorMessage("Select Party Name");
            cmbPartyName.requestFocus();
            return;
        }
        if(dateFrom.getValue()==null && dateTo.getValue()==null)
        {
            list.clear();
            txtTotal.setText(""+0.0f);
            list.addAll(purchaseInvoiceService.getPartyAllPurchaseInvoice(partyService.getPurchasePartyByName(cmbPartyName.getValue()).getId()));
            resetSrNo();
            table.refresh();

            return;
        }
        if(dateFrom.getValue()!=null && dateTo.getValue()==null)//date wise party wise Report
        {
            list.clear();
            txtTotal.setText(""+0.0f);
            list.addAll(purchaseInvoiceService.getPurchaseInvoicePartyWise(
                    dateFrom.getValue(),
                    partyService.getPurchasePartyByName(cmbPartyName.getValue()).getId()
            ));
            resetSrNo();
            table.refresh();
            return;
        }
        if(dateFrom.getValue()!=null && dateTo.getValue()!=null)//date period wise Report
        {
            list.clear();
            txtTotal.setText(""+0.0f);
            list.addAll(purchaseInvoiceService.getPartyPeriodPurchaseInvoice(
                    partyService.getPurchasePartyByName(cmbPartyName.getValue()).getId(),
                    dateFrom.getValue(),
                    dateTo.getValue()
            ));
            resetSrNo();
            table.refresh();
            return;
        }
        if(list.size()==0)
        {
            alert.showErrorMessage("No Data To Show");
        }

    }
    void resetSrNo()
    {
        int sr=0;
        float total=0.0f;
        for(PurchaseInvoice tr:list)
        {
            list.get(list.indexOf(tr)).setGst(++sr);
            total+=tr.getGrandtotal();
        }
        txtTotal.setText(String.valueOf(total));
    }
}
