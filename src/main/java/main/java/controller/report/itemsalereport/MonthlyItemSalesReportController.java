package main.java.main.java.controller.report.itemsalereport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Item;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.reportEntity.ItemSaleReportPojo;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PrintMonthlyItemSaleReport;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class MonthlyItemSalesReportController implements Initializable {
@FXML private AnchorPane mainPane;
@FXML  private TextField txtItemName;
@FXML  private DatePicker date;
@FXML  private Button btnShow;
@FXML  private Button btnShowAll;
@FXML  private Button btnReset;
@FXML  private Button btnPrint;
@FXML  private Button btnExit;
@FXML  private TableView<ItemSaleReportPojo> table;
@FXML  private TableColumn<ItemSaleReportPojo,Integer> colSrNo;
@FXML  private TableColumn<ItemSaleReportPojo, LocalDate> colDate;
@FXML  private TableColumn<ItemSaleReportPojo,Long> colBillno;
@FXML  private TableColumn<ItemSaleReportPojo,String> colItem;
@FXML  private TableColumn<ItemSaleReportPojo,String> colUnit;
@FXML  private TableColumn<ItemSaleReportPojo,Float> colQty;
@FXML  private TableColumn<ItemSaleReportPojo,Float> colRate;
@FXML  private TableColumn<ItemSaleReportPojo,Float> colAmount;
@FXML  private TextField txtQty;
@FXML  private TextField txtAmount;
@FXML private TextField txtKG;
@FXML private TextField txtNos;
private ItemService itemService;
private BillService billService;
private AlertNotification notify;
private ObservableList<ItemSaleReportPojo>list = FXCollections.observableArrayList();

@Override
public void initialize(URL location, ResourceBundle resources) {;
    itemService = new ItemServiceImpl();
    billService = new BillServiceImpl();
    notify = new AlertNotification();
    date.setValue(LocalDate.now());
    TextFields.bindAutoCompletion(txtItemName, itemService.getAllItemNames());
    colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
    colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    colBillno.setCellValueFactory(new PropertyValueFactory<>("billno"));
    colItem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
    colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
    colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
    colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
    colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    table.setItems(list);
    btnShow.setOnAction(e->show());
    btnShowAll.setOnAction(e->showAll());
    btnReset.setOnAction(e->{
        list.clear();
        txtItemName.setText("");
        date.setValue(LocalDate.now());
        txtAmount.setText("");
        txtQty.setText("");
    });
    btnExit.setOnAction(e->mainPane.setVisible(false));
    btnPrint.setOnAction(e->{
        if(list.isEmpty()) {
            notify.showErrorMessage("No Data to Print");
            return;
        }
        new PrintMonthlyItemSaleReport(list,date.getValue().with(firstDayOfMonth()),date.getValue().with(lastDayOfMonth()));
        new PrintFile().openFile("D:\\Software\\Prints\\ItemSalesReport.pdf");
    });
    }
    private void showAll() {
        if(date.getValue()==null)
        {
            notify.showErrorMessage("Enter Date");
            date.requestFocus();
            return;
        }
        list.clear();
        txtQty.setText("");
        txtAmount.setText("");
        int sr=0;
        float kg=0,nos=0,totalamt=0;

        List<Bill>billList = billService.getMonthWiseBill(date.getValue());
            for(String name:itemService.getAllItemNames())
            {
                float qty=0,amount=0,rate=0;
                qty=getItemSale(billList,name);
                Item item =itemService.getItemByName(name);
                rate=item.getRate();
                if(item.getUnit().equals("KG"))
                    kg+=qty;
                else nos+=qty;

                totalamt+=(qty*rate);

                list.add(new ItemSaleReportPojo(
                        ++sr,0,date.getValue(),name,itemService.getItemByName(name).getUnit(),qty,rate,qty*rate));
            }
            txtAmount.setText(String.valueOf(totalamt));
            txtKG.setText(String.valueOf(kg));
            txtNos.setText(String.valueOf(nos));
    }
    private float getItemSale(List<Bill>billList,String name)
    {
        float qty=0;
        for(Bill bill:billList)
        {
            for(Transaction tr:bill.getTransaction())
            {
                if(tr.getItemname().equalsIgnoreCase(name))
                {
                    qty+=tr.getQuantity();
                }
            }
        }
        return qty;
    }


    private void show() {
        if(txtItemName.getText().isEmpty())
        {
            notify.showErrorMessage("Enter Item Name");
            txtItemName.requestFocus();
            return;
        }
        if(date.getValue()==null)
        {
            notify.showErrorMessage("Enter Date");
            date.requestFocus();
            return;
        }
        getItemWiseList(billService.getMonthWiseBill(date.getValue()),txtItemName.getText());

    }

    private void getItemWiseList(List<Bill>billList,String item) {
        try {
            list.clear();
            int sr=0;
            float amt=0,qty=0;

            for (Bill bill : billList) {
                for(Transaction tr:bill.getTransaction())
                {
                    if(tr.getItemname().equalsIgnoreCase(item)) {
                        list.add(new ItemSaleReportPojo(
                                ++sr,
                                bill.getBillno(),
                                bill.getDate(),
                                tr.getItemname(),
                                tr.getUnit(),
                                tr.getQuantity(),
                                tr.getRate(),
                                tr.getAmount()));
                        amt +=tr.getAmount();
                        qty+=tr.getQuantity();
                    }
                }
            }
            txtAmount.setText(""+amt);
            txtQty.setText(""+qty);
        } catch (Exception e) {
            notify.showErrorMessage("No Data TO  show");
            e.printStackTrace();
        }
    }

}
