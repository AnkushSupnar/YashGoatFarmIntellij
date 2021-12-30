package main.java.main.java.controller.masterReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.reportEntity.PLItem;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PLReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML private Button btnCalculate;
    @FXML private TableView<PLItem> tableItem;
    @FXML private TableColumn<PLItem,String> colItemName;
    @FXML private TableColumn<PLItem,Float> colMargin;
    @FXML private TableColumn<PLItem,Integer> colNo;
    @FXML private TableColumn<PLItem,Float> colPurchaseAmount;
    @FXML private TableColumn<PLItem,Float> colPurchaseRate;
    @FXML private TableColumn<PLItem,Float> colSolQty;
    @FXML private TableColumn<PLItem,Float> colSoldAmount;
    @FXML private TableColumn<PLItem,Float> colSoldRate;
    @FXML private TableColumn<PLItem,String> colUnit;
    @FXML private TextField txtTotalMargin;
    private ObservableList<PLItem>plList = FXCollections.observableArrayList();
    private BillService billService;
    private ItemService itemService;
    private PurchaseInvoiceService purchaseService;
    private List<Bill> billList;
    Map<Integer, Transaction> saleMap;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billService = new BillServiceImpl();
        itemService = new ItemServiceImpl();
        purchaseService = new PurchaseInvoiceServiceImpl();
        saleMap = new HashMap<>();
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemname"));
        colMargin.setCellValueFactory(new PropertyValueFactory<>("margin"));
        colNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPurchaseAmount.setCellValueFactory(new PropertyValueFactory<>("purchaseamt"));
        colPurchaseRate.setCellValueFactory(new PropertyValueFactory<>("purchaserate"));
        colSolQty.setCellValueFactory(new PropertyValueFactory<>("soldqty"));
        colSoldAmount.setCellValueFactory(new PropertyValueFactory<>("soldamt"));
        colSoldRate.setCellValueFactory(new PropertyValueFactory<>("soldrate"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        tableItem.setItems(plList);
        btnCalculate.setOnAction(e->calculateReport());
    }

    private void calculateReport() {
        loadItemReport();
    }

    private void loadItemReport() {
        //1. load item sold itemname and its rate qty

        billList = billService.getAllBills();
        for(Bill bill:billList)
        {
            for(Transaction tr:bill.getTransaction())
            {
                addInSale(tr);
            }
        }
        tableItem.refresh();
    }

    private void addInSale(Transaction tr) {
        int index=-1;
        double purchaseRate = purchaseService.getAveragePurchaseRate(tr.getItemname());
        double total = Double.parseDouble(txtTotalMargin.getText());
        for(PLItem p:plList)
        {
            if(p.getItemname().equals(tr.getItemname()) && p.getSoldrate()==tr.getRate())
            {
                index = plList.indexOf(p);
                break;
            }
        }
        if(index==-1)
        {
            plList.add(new PLItem(
                plList.size()+1,
                    tr.getItemname(),
                    tr.getUnit(),
                    tr.getQuantity(),
                    tr.getRate(),
                    tr.getAmount(),
                    purchaseRate,
                    (float) (purchaseRate*tr.getQuantity())
            ));
        }
        else {
            plList.get(index).setSoldqty(
                    plList.get(index).getSoldqty()+tr.getQuantity());
            plList.get(index).setSoldamt(
                    plList.get(index).getSoldamt()+tr.getAmount());
            plList.get(index).setPurchaseamt(
                    (float) (plList.get(index).getPurchaserate()*plList.get(index).getSoldqty()));
        }

    }
}
