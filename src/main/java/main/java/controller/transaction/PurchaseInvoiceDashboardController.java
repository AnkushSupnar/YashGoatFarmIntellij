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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.service.service.PurchaseInvoiceService;
import main.java.main.java.hibernate.service.service.PurchasePartyService;
import main.java.main.java.hibernate.service.serviceImpl.PurchaseInvoiceServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.PurchasePartyServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PurchaseInvoiceDashboardController implements Initializable {

    @FXML private AnchorPane mainPanel;
    @FXML private TableView<PurchaseInvoice> table;
    @FXML private TableColumn<PurchaseInvoice, Long>      colBillNo;
    @FXML private TableColumn<PurchaseInvoice, String>    colInvoiceNo;
    @FXML private TableColumn<PurchaseInvoice, String>    colParty;
    @FXML private TableColumn<PurchaseInvoice, LocalDate> colDate;
    @FXML private TableColumn<PurchaseInvoice, Float>     colAmount;
    @FXML private TableColumn<PurchaseInvoice, Float>     colPaid;
    @FXML private TableColumn<PurchaseInvoice, Float>     colOutstanding;
    @FXML private TableColumn<PurchaseInvoice, String>    colStatus;
    @FXML private TextField txtSearchParty;
    @FXML private TextField txtSearchBillNo;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Label lblCount;
    @FXML private ToggleGroup filterGroup;
    @FXML private ToggleButton btnToday;
    @FXML private ToggleButton btnThisWeek;
    @FXML private ToggleButton btnThisMonth;
    @FXML private ToggleButton btnAll;

    private final ObservableList<PurchaseInvoice> masterList = FXCollections.observableArrayList();
    private FilteredList<PurchaseInvoice> filteredList;
    private PurchaseInvoiceService invoiceService;
    private PurchasePartyService partyService;
    private AlertNotification notification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        invoiceService = new PurchaseInvoiceServiceImpl();
        partyService   = new PurchasePartyServiceImpl();
        notification   = new AlertNotification();
        setupColumns();
        filteredList = new FilteredList<>(masterList, b -> true);
        table.setItems(filteredList);

        List<String> partyNames = partyService.getAllPurchasePartyNames();
        if (partyNames != null && !partyNames.isEmpty()) {
            TextFields.bindAutoCompletion(txtSearchParty, partyNames);
        }

        txtSearchParty.textProperty().addListener((obs, o, n) -> applyInMemoryFilter());
        txtSearchBillNo.textProperty().addListener((obs, o, n) -> applyInMemoryFilter());

        table.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null) {
                openInvoiceForEdit();
            }
        });

        loadInvoices(invoiceService.getDateWisePurchaseInvoice(LocalDate.now()));
    }

    // ── Column setup ────────────────────────────────────────────────────

    private void setupColumns() {
        colBillNo.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getBillno()));
        colInvoiceNo.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getInvoiceNo()));
        colDate.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getDate()));

        colParty.setCellValueFactory(p -> {
            if (p.getValue().getParty() == null) return new SimpleStringProperty("-");
            return new SimpleStringProperty(p.getValue().getParty().getName());
        });

        colAmount.setCellValueFactory(p ->
                new SimpleObjectProperty<>(p.getValue().getGrandtotal()));

        colPaid.setCellValueFactory(p ->
                new SimpleObjectProperty<>(p.getValue().getPaid()));

        colOutstanding.setCellValueFactory(p -> {
            float out = p.getValue().getGrandtotal() - p.getValue().getPaid();
            return new SimpleObjectProperty<>(Math.max(0f, out));
        });

        colStatus.setCellValueFactory(p -> new SimpleStringProperty(paymentStatus(p.getValue())));

        colStatus.setCellFactory(col -> new TableCell<PurchaseInvoice, String>() {
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

    private static String paymentStatus(PurchaseInvoice inv) {
        float paid  = inv.getPaid();
        float total = inv.getGrandtotal();
        if (paid <= 0)     return "UNPAID";
        if (paid >= total) return "PAID";
        return "PARTIAL";
    }

    // ── Data loading ─────────────────────────────────────────────────────

    private void loadInvoices(List<PurchaseInvoice> invoices) {
        masterList.clear();
        if (invoices != null) masterList.addAll(invoices);
        applyInMemoryFilter();
    }

    private void applyInMemoryFilter() {
        String partyText  = txtSearchParty.getText().toLowerCase().trim();
        String billNoText = txtSearchBillNo.getText().trim();

        filteredList.setPredicate(inv -> {
            if (!partyText.isEmpty()) {
                String name = inv.getParty() == null ? "" : inv.getParty().getName().toLowerCase();
                if (!name.contains(partyText)) return false;
            }
            if (!billNoText.isEmpty()) {
                try {
                    if (inv.getBillno() != Long.parseLong(billNoText)) return false;
                } catch (NumberFormatException ignored) {
                    return false;
                }
            }
            return true;
        });

        int shown = filteredList.size();
        int total = masterList.size();
        lblCount.setText(shown == total
                ? shown + " invoices"
                : shown + " / " + total + " invoices");
    }

    // ── Quick-filter button actions ───────────────────────────────────────

    @FXML void loadToday(ActionEvent e) {
        clearDateRange();
        loadInvoices(invoiceService.getDateWisePurchaseInvoice(LocalDate.now()));
    }

    @FXML void loadThisWeek(ActionEvent e) {
        clearDateRange();
        loadInvoices(invoiceService.getThisWeekInvoice());
    }

    @FXML void loadThisMonth(ActionEvent e) {
        clearDateRange();
        loadInvoices(invoiceService.getMonthWisePurchaseInvoice(LocalDate.now()));
    }

    @FXML void loadAll(ActionEvent e) {
        clearDateRange();
        loadInvoices(invoiceService.getAllPurchaseInvoice());
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
        loadInvoices(invoiceService.getPeriodPurchaseInvoice(from, to));
    }

    @FXML void resetSearch(ActionEvent e) {
        txtSearchParty.clear();
        txtSearchBillNo.clear();
        clearDateRange();
        btnToday.setSelected(true);
        loadInvoices(invoiceService.getDateWisePurchaseInvoice(LocalDate.now()));
    }

    // ── Actions ───────────────────────────────────────────────────────────

    @FXML void previewInvoice(ActionEvent event) {
        PurchaseInvoice selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            notification.showErrorMessage("Please select an invoice to preview");
            return;
        }
        CommonData.previewInvoiceno = selected.getBillno();
        new ViewUtil().showInvoicePreview(event);
    }

    @FXML void openNewInvoice(ActionEvent event) {
        CommonData.editInvoiceNo = 0;
        navigateToForm();
    }

    @FXML void editInvoice(ActionEvent event) {
        openInvoiceForEdit();
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void openInvoiceForEdit() {
        PurchaseInvoice selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            notification.showErrorMessage("Please select an invoice to edit");
            return;
        }
        CommonData.editInvoiceNo = selected.getBillno();
        navigateToForm();
    }

    private void navigateToForm() {
        BorderPane bp = (BorderPane) mainPanel.getParent();
        if (bp == null) return;
        Pane form = new ViewUtil().getPage("transaction/PurchaseInviceFrame");
        if (form != null) bp.setCenter(form);
    }

    private void clearDateRange() {
        dateFrom.setValue(null);
        dateTo.setValue(null);
    }
}
