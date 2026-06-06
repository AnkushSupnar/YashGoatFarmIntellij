package main.java.main.java.controller.masterReport;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Commision;
import main.java.main.java.hibernate.entities.LabourCharges;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.entities.SalesmanCuttingCharges;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.reportEntity.PLItem;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.CommisionService;
import main.java.main.java.hibernate.service.service.LabourChargesService;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.service.SalesmanCuttingChargesService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CommisionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.LabourChargesServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.SalesmanCuttingChargesServiceImpl;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PLReportController implements Initializable {

    @FXML private AnchorPane mainPane;
    @FXML private ComboBox<Integer> cmbYear;
    @FXML private Button btnCalculate;
    @FXML private Button btnReset;
    @FXML private Button btnExit;

    // KPI tiles
    @FXML private Label lblTotalSales;
    @FXML private Label lblTotalPurchase;
    @FXML private Label lblGrossProfit;
    @FXML private Label lblTotalExpenses;
    @FXML private Label lblNetProfit;
    @FXML private Label lblNetLabel;
    @FXML private Label lblCashCollected;
    @FXML private Label lblOutstanding;
    @FXML private VBox netProfitTile;

    // Summary rows
    @FXML private Label lblRowSalesNet;
    @FXML private Label lblRowTransport;
    @FXML private Label lblRowOther;
    @FXML private Label lblRowTotalRevenue;
    @FXML private Label lblRowPurchase;
    @FXML private Label lblRowTotalCogs;
    @FXML private Label lblRowGrossProfit;
    @FXML private Label lblRowCommissions;
    @FXML private Label lblRowLabour;
    @FXML private Label lblRowCutting;
    @FXML private Label lblRowTotalExpenses;
    @FXML private Label lblRowNetProfit;
    @FXML private Label lblRowNetLabel;
    @FXML private HBox netRowBar;

    // Item table (preserved from original)
    @FXML private TableView<PLItem> tableItem;
    @FXML private TableColumn<PLItem, String> colItemName;
    @FXML private TableColumn<PLItem, Float> colMargin;
    @FXML private TableColumn<PLItem, Integer> colNo;
    @FXML private TableColumn<PLItem, Float> colPurchaseAmount;
    @FXML private TableColumn<PLItem, Float> colPurchaseRate;
    @FXML private TableColumn<PLItem, Float> colSolQty;
    @FXML private TableColumn<PLItem, Float> colSoldAmount;
    @FXML private TableColumn<PLItem, Float> colSoldRate;
    @FXML private TableColumn<PLItem, String> colUnit;
    @FXML private TextField txtTotalMargin;

    // Expense breakdown table
    @FXML private TableView<ExpenseRow> tableExpense;
    @FXML private TableColumn<ExpenseRow, Integer> colExpSr;
    @FXML private TableColumn<ExpenseRow, String> colExpCategory;
    @FXML private TableColumn<ExpenseRow, Integer> colExpCount;
    @FXML private TableColumn<ExpenseRow, String> colExpAmount;

    private final ObservableList<PLItem> plList = FXCollections.observableArrayList();
    private final ObservableList<ExpenseRow> expenseList = FXCollections.observableArrayList();

    private BillService billService;
    private PurchaseInvoiceService purchaseService;
    private CommisionService commisionService;
    private LabourChargesService labourService;
    private SalesmanCuttingChargesService cuttingService;

    private final NumberFormat money = createIndianMoneyFormatter();

    private static NumberFormat createIndianMoneyFormatter() {
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "IN"));
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(true);
        return nf;
    }

    private <T> void installIndianMoneyCell(TableColumn<T, Float> column) {
        column.setCellFactory(c -> new javafx.scene.control.TableCell<T, Float>() {
            @Override
            protected void updateItem(Float value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(money.format(value.doubleValue()));
                }
            }
        });
    }

    public static class ExpenseRow {
        private final SimpleIntegerProperty sr;
        private final SimpleStringProperty category;
        private final SimpleIntegerProperty count;
        private final SimpleStringProperty amount;

        public ExpenseRow(int sr, String category, int count, String amount) {
            this.sr = new SimpleIntegerProperty(sr);
            this.category = new SimpleStringProperty(category);
            this.count = new SimpleIntegerProperty(count);
            this.amount = new SimpleStringProperty(amount);
        }

        public int getSr() { return sr.get(); }
        public String getCategory() { return category.get(); }
        public int getCount() { return count.get(); }
        public String getAmount() { return amount.get(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billService = new BillServiceImpl();
        purchaseService = new PurchaseInvoiceServiceImpl();
        commisionService = new CommisionServiceImpl();
        labourService = new LabourChargesServiceImpl();
        cuttingService = new SalesmanCuttingChargesServiceImpl();

        // Year combo: last 10 years up to current
        int currentYear = Year.now().getValue();
        for (int y = currentYear; y >= currentYear - 9; y--) {
            cmbYear.getItems().add(y);
        }
        cmbYear.getSelectionModel().select(Integer.valueOf(currentYear));

        // Item table bindings (preserve original PLItem mapping)
        colNo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemname"));
        colUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        colSolQty.setCellValueFactory(new PropertyValueFactory<>("soldqty"));
        colSoldRate.setCellValueFactory(new PropertyValueFactory<>("soldrate"));
        colSoldAmount.setCellValueFactory(new PropertyValueFactory<>("soldamt"));
        colPurchaseRate.setCellValueFactory(new PropertyValueFactory<>("purchaserate"));
        colPurchaseAmount.setCellValueFactory(new PropertyValueFactory<>("purchaseamt"));
        colMargin.setCellValueFactory(new PropertyValueFactory<>("margin"));
        installIndianMoneyCell(colSoldAmount);
        installIndianMoneyCell(colPurchaseAmount);
        installIndianMoneyCell(colMargin);
        tableItem.setItems(plList);

        // Expense table bindings
        colExpSr.setCellValueFactory(new PropertyValueFactory<>("sr"));
        colExpCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colExpCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colExpAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        tableExpense.setItems(expenseList);

        btnCalculate.setOnAction(e -> calculateReport());
        btnReset.setOnAction(e -> resetReport());
        btnExit.setOnAction(e -> mainPane.setVisible(false));

        calculateReport();
    }

    private void calculateReport() {
        Integer selectedYear = cmbYear.getValue();
        if (selectedYear == null) {
            selectedYear = Year.now().getValue();
        }
        int year = selectedYear;
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        plList.clear();
        expenseList.clear();

        // === Revenue ===
        double salesNet = 0;
        double transportRecovered = 0;
        double otherRecovered = 0;
        double cashCollected = 0;
        double outstanding = 0;

        List<Bill> bills = billService.getYearWiseBills(year);
        if (bills == null) bills = List.of();

        for (Bill bill : bills) {
            salesNet += bill.getNettotal();
            transportRecovered += bill.getTransportingchrges();
            otherRecovered += bill.getOtherchargs();
            cashCollected += bill.getRecivedamount();

            double billTotal = bill.getNettotal() + bill.getTransportingchrges() + bill.getOtherchargs();
            outstanding += Math.max(0, billTotal - bill.getRecivedamount());

            if (bill.getTransaction() != null) {
                for (Transaction tr : bill.getTransaction()) {
                    addInSale(tr);
                }
            }
        }
        double totalRevenue = salesNet + transportRecovered + otherRecovered;

        // === COGS (Cost of Goods Sold) ===
        double totalPurchase = 0;
        int purchaseCount = 0;
        List<PurchaseInvoice> purchases = purchaseService.getPeriodPurchaseInvoice(start, end);
        if (purchases != null) {
            for (PurchaseInvoice inv : purchases) {
                totalPurchase += inv.getGrandtotal();
                purchaseCount++;
            }
        }

        double grossProfit = totalRevenue - totalPurchase;

        // === Operating Expenses ===
        double totalCommissions = 0;
        int commissionCount = 0;
        List<Commision> commissions = commisionService.getDatePeriodCommision(start, end);
        if (commissions != null) {
            for (Commision c : commissions) {
                totalCommissions += c.getPaidCommision();
                commissionCount++;
            }
        }

        double totalLabour = 0;
        int labourCount = 0;
        List<LabourCharges> labour = labourService.getPeriodWiseLabourCharges(start, end);
        if (labour != null) {
            for (LabourCharges lc : labour) {
                totalLabour += lc.getAmount();
                labourCount++;
            }
        }

        double totalCutting = 0;
        int cuttingCount = 0;
        List<SalesmanCuttingCharges> cutting = cuttingService.getPeriodSalesmanCuttingCharges(start, end);
        if (cutting != null) {
            for (SalesmanCuttingCharges sc : cutting) {
                totalCutting += sc.getCuttingCharges();
                cuttingCount++;
            }
        }

        double totalExpenses = totalCommissions + totalLabour + totalCutting;
        double netProfit = grossProfit - totalExpenses;

        // === Update KPI tiles ===
        lblTotalSales.setText(money.format(totalRevenue));
        lblTotalPurchase.setText(money.format(totalPurchase));
        lblGrossProfit.setText(money.format(grossProfit));
        lblTotalExpenses.setText(money.format(totalExpenses));
        lblNetProfit.setText(money.format(netProfit));
        lblCashCollected.setText(money.format(cashCollected));
        lblOutstanding.setText(money.format(outstanding));

        // Net Profit tile color: green if profit, red if loss
        netProfitTile.getStyleClass().removeAll("kpi-net-pos", "kpi-net-neg");
        if (netProfit < 0) {
            netProfitTile.getStyleClass().add("kpi-net-neg");
            lblNetLabel.setText("NET LOSS");
        } else {
            netProfitTile.getStyleClass().add("kpi-net-pos");
            lblNetLabel.setText("NET PROFIT");
        }

        // === Update Summary rows ===
        lblRowSalesNet.setText(money.format(salesNet));
        lblRowTransport.setText(money.format(transportRecovered));
        lblRowOther.setText(money.format(otherRecovered));
        lblRowTotalRevenue.setText(money.format(totalRevenue));
        lblRowPurchase.setText(money.format(totalPurchase));
        lblRowTotalCogs.setText(money.format(totalPurchase));
        lblRowGrossProfit.setText(money.format(grossProfit));
        lblRowCommissions.setText(money.format(totalCommissions));
        lblRowLabour.setText(money.format(totalLabour));
        lblRowCutting.setText(money.format(totalCutting));
        lblRowTotalExpenses.setText(money.format(totalExpenses));
        lblRowNetProfit.setText(money.format(netProfit));
        netRowBar.getStyleClass().removeAll("pl-row-total-bar-pos", "pl-row-total-bar-neg");
        if (netProfit < 0) {
            netRowBar.getStyleClass().add("pl-row-total-bar-neg");
            lblRowNetLabel.setText("Net Loss (Gross − C)");
        } else {
            netRowBar.getStyleClass().add("pl-row-total-bar-pos");
            lblRowNetLabel.setText("Net Profit (Gross − C)");
        }

        // === Item table total margin ===
        double totalItemMargin = 0;
        for (PLItem p : plList) {
            totalItemMargin += p.getMargin();
        }
        txtTotalMargin.setText(money.format(totalItemMargin));
        tableItem.refresh();

        // === Expense breakdown rows ===
        int sr = 1;
        expenseList.add(new ExpenseRow(sr++, "Purchase Invoices (COGS)", purchaseCount, money.format(totalPurchase)));
        expenseList.add(new ExpenseRow(sr++, "Employee Commissions Paid", commissionCount, money.format(totalCommissions)));
        expenseList.add(new ExpenseRow(sr++, "Labour Charges Paid", labourCount, money.format(totalLabour)));
        expenseList.add(new ExpenseRow(sr++, "Salesman Cutting Charges", cuttingCount, money.format(totalCutting)));
        expenseList.add(new ExpenseRow(sr, "TOTAL OUTFLOWS", purchaseCount + commissionCount + labourCount + cuttingCount,
                money.format(totalPurchase + totalExpenses)));
    }

    private void resetReport() {
        cmbYear.getSelectionModel().select(Integer.valueOf(Year.now().getValue()));
        calculateReport();
    }

    private void addInSale(Transaction tr) {
        double purchaseRate = purchaseService.getAveragePurchaseRate(tr.getItemname());
        int index = -1;
        for (PLItem p : plList) {
            if (p.getItemname().equals(tr.getItemname()) && p.getSoldrate() == tr.getRate()) {
                index = plList.indexOf(p);
                break;
            }
        }
        if (index == -1) {
            plList.add(new PLItem(
                    plList.size() + 1,
                    tr.getItemname(),
                    tr.getUnit(),
                    tr.getQuantity(),
                    tr.getRate(),
                    tr.getAmount(),
                    purchaseRate,
                    (float) (purchaseRate * tr.getQuantity())
            ));
        } else {
            PLItem existing = plList.get(index);
            existing.setSoldqty(existing.getSoldqty() + tr.getQuantity());
            existing.setSoldamt(existing.getSoldamt() + tr.getAmount());
            existing.setPurchaseamt((float) (existing.getPurchaserate() * existing.getSoldqty()));
            existing.setMargin(0);
        }
    }
}
