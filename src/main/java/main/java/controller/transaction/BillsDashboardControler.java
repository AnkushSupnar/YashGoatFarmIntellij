package main.java.main.java.controller.transaction;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class BillsDashboardControler implements Initializable {

    @FXML private AnchorPane mainPanel;
    @FXML private TableView<Bill> table;
    @FXML private TableColumn<Bill, Long>   colBillNo;
    @FXML private TableColumn<Bill, LocalDate> colDate;
    @FXML private TableColumn<Bill, String> colCustomer;
    @FXML private TableColumn<Bill, Float>  colAmount;
    @FXML private TableColumn<Bill, Float>  colPaid;
    @FXML private TableColumn<Bill, Float>  colOutstanding;
    @FXML private TableColumn<Bill, String> colStatus;
    @FXML private TextField txtSearchCustomer;
    @FXML private TextField txtSearchBillNo;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Label lblCount;
    @FXML private ToggleGroup filterGroup;
    @FXML private ToggleButton btnToday;
    @FXML private ToggleButton btnThisWeek;
    @FXML private ToggleButton btnThisMonth;
    @FXML private ToggleButton btnAll;

    private final ObservableList<Bill> masterList = FXCollections.observableArrayList();
    private FilteredList<Bill> filteredList;
    private BillService billService;
    private AlertNotification notification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billService   = new BillServiceImpl();
        notification  = new AlertNotification();
        setupColumns();
        filteredList  = new FilteredList<>(masterList, b -> true);
        table.setItems(filteredList);

        // Live in-memory filter as user types
        txtSearchCustomer.textProperty().addListener((obs, o, n) -> applyInMemoryFilter());
        txtSearchBillNo.textProperty().addListener((obs, o, n) -> applyInMemoryFilter());

        // Double-click row to open for editing
        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null) {
                openBillForEdit();
            }
        });

        loadBills(billService.getDateWiseBill(LocalDate.now()));
    }

    // ── Column setup ────────────────────────────────────────────────────

    private void setupColumns() {
        colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        colCustomer.setCellValueFactory(p -> {
            Customer c = p.getValue().getCustomer();
            if (c == null) return new SimpleStringProperty("-");
            String name = (c.getFname() + " " + c.getMname() + " " + c.getLname())
                    .replaceAll("\\s+", " ").trim();
            return new SimpleStringProperty(name);
        });

        colAmount.setCellValueFactory(p ->
                new SimpleObjectProperty<>(p.getValue().getNettotal()));

        colPaid.setCellValueFactory(p ->
                new SimpleObjectProperty<>(p.getValue().getRecivedamount()));

        colOutstanding.setCellValueFactory(p -> {
            float out = p.getValue().getNettotal() - p.getValue().getRecivedamount();
            return new SimpleObjectProperty<>(Math.max(0f, out));
        });

        colStatus.setCellValueFactory(p -> new SimpleStringProperty(paymentStatus(p.getValue())));

        colStatus.setCellFactory(col -> new TableCell<Bill, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status) {
                        case "PAID":
                            setStyle("-fx-text-fill: #2E7D32; -fx-font-weight: bold;");
                            break;
                        case "PARTIAL":
                            setStyle("-fx-text-fill: #E65100; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: #C62828; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private static String paymentStatus(Bill b) {
        float paid = b.getRecivedamount();
        float total = b.getNettotal();
        if (paid <= 0)         return "UNPAID";
        if (paid >= total)     return "PAID";
        return "PARTIAL";
    }

    // ── Data loading ─────────────────────────────────────────────────────

    private void loadBills(List<Bill> bills) {
        masterList.clear();
        if (bills != null) {
            for (Bill b : bills) {
                // Fold all charges into nettotal so all derived columns are consistent
                b.setNettotal(b.getNettotal() + b.getIgstTotal() + b.getOtherchargs() + b.getTransportingchrges());
            }
            masterList.addAll(bills);
        }
        applyInMemoryFilter();
    }

    private void applyInMemoryFilter() {
        String customerText = txtSearchCustomer.getText().toLowerCase().trim();
        String billNoText   = txtSearchBillNo.getText().trim();

        filteredList.setPredicate(b -> {
            if (!customerText.isEmpty()) {
                Customer c = b.getCustomer();
                String name = c == null ? ""
                        : (c.getFname() + " " + c.getMname() + " " + c.getLname()).toLowerCase();
                if (!name.contains(customerText)) return false;
            }
            if (!billNoText.isEmpty()) {
                try {
                    if (b.getBillno() != Long.parseLong(billNoText)) return false;
                } catch (NumberFormatException ignored) {
                    return false;
                }
            }
            return true;
        });

        int shown = filteredList.size();
        int total = masterList.size();
        lblCount.setText(shown == total
                ? shown + " bills"
                : shown + " / " + total + " bills");
    }

    // ── Quick-filter button actions ───────────────────────────────────────

    @FXML void loadToday(ActionEvent e) {
        clearDateRange();
        loadBills(billService.getDateWiseBill(LocalDate.now()));
    }

    @FXML void loadThisWeek(ActionEvent e) {
        clearDateRange();
        loadBills(billService.getThisWeekBill());
    }

    @FXML void loadThisMonth(ActionEvent e) {
        clearDateRange();
        loadBills(billService.getMonthWiseBill(LocalDate.now()));
    }

    @FXML void loadAll(ActionEvent e) {
        clearDateRange();
        loadBills(billService.getAllBills());
    }

    // ── Search / reset ────────────────────────────────────────────────────

    @FXML void searchByDateRange(ActionEvent e) {
        LocalDate from = dateFrom.getValue();
        LocalDate to   = dateTo.getValue();
        if (from == null || to == null) {
            notification.showErrorMessage("Please select both From Date and To Date");
            return;
        }
        if (from.isAfter(to)) {
            notification.showErrorMessage("From Date cannot be after To Date");
            return;
        }
        loadBills(billService.getPeriodWiseBills(from, to));
    }

    @FXML void resetSearch(ActionEvent e) {
        txtSearchCustomer.clear();
        txtSearchBillNo.clear();
        clearDateRange();
        btnToday.setSelected(true);
        loadBills(billService.getDateWiseBill(LocalDate.now()));
    }

    // ── Actions ───────────────────────────────────────────────────────────

    @FXML void previewBill(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() == null) {
            notification.showErrorMessage("Please select a bill to preview");
            return;
        }
        doPreview();
        new ViewUtil().showBillPreview(event);
    }

    @FXML void openNewBill(ActionEvent event) {
        CommonData.editBillNo = 0;
        BorderPane bp = (BorderPane) mainPanel.getParent();
        if (bp == null) return;
        Pane billingFrame = new ViewUtil().getPage("transaction/BillingFrame");
        if (billingFrame != null) bp.setCenter(billingFrame);
    }

    @FXML void editBill(ActionEvent event) {
        openBillForEdit();
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void openBillForEdit() {
        Bill selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            notification.showErrorMessage("Please select a bill to edit");
            return;
        }
        CommonData.editBillNo = selected.getBillno();
        BorderPane bp = (BorderPane) mainPanel.getParent();
        if (bp == null) return;
        Pane billingFrame = new ViewUtil().getPage("transaction/BillingFrame");
        if (billingFrame != null) bp.setCenter(billingFrame);
    }

    private void doPreview() {
        Bill selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) CommonData.previewBillNo = selected.getBillno();
    }

    private void clearDateRange() {
        dateFrom.setValue(null);
        dateTo.setValue(null);
    }
}
