package main.java.main.java.controller.transaction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.main.java.Main;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.entities.Item;
import main.java.main.java.hibernate.entities.Login;
import main.java.main.java.hibernate.entities.Quotation;
import main.java.main.java.hibernate.entities.QuotationTransaction;
import main.java.main.java.hibernate.service.service.BankService;
import main.java.main.java.hibernate.service.service.CounterStockDataService;
import main.java.main.java.hibernate.service.service.CustomerService;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.service.QuotationService;
import main.java.main.java.hibernate.service.serviceImpl.BankServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CounterStockDataServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CustomerServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.QuotationServiceImpl;
import main.java.main.java.hibernate.util.CommonData;
import main.java.main.java.print.GenerateQuotation;
import main.java.main.java.print.PrintFile;

public class QuotationController implements Initializable {

	@FXML private AnchorPane mainPanel;
	@FXML private TextField txtQuotationNo;
	@FXML private DatePicker date;
	@FXML private DatePicker dateValidUntil;
	@FXML private TextField txtCustomerName;
	@FXML private Button btnSearch;
	@FXML private Button btnNew;
	@FXML private TextArea txtCustomerInfo;
	@FXML private ComboBox<String> cmbSalesman;

	@FXML private TextField txtItemName;
	@FXML private TextField txtUnit;
	@FXML private TextField txtQty;
	@FXML private TextField txtRate;
	@FXML private TextField txtAmount;

	@FXML private Button btnAdd;
	@FXML private Button btnClear;
	@FXML private Button btnRemove;
	@FXML private Button btnUpdate;

	@FXML private TableView<QuotationTransaction> table;
	@FXML private TableColumn<QuotationTransaction, Long> colSrNo;
	@FXML private TableColumn<QuotationTransaction, String> colItemName;
	@FXML private TableColumn<QuotationTransaction, String> colUnit;
	@FXML private TableColumn<QuotationTransaction, Float> colQty;
	@FXML private TableColumn<QuotationTransaction, Float> colRate;
	@FXML private TableColumn<QuotationTransaction, Float> colAmount;
	@FXML private TableColumn<QuotationTransaction, Float> colIgstPct;
	@FXML private TableColumn<QuotationTransaction, Float> colIgstAmt;
	@FXML private TextField txtIgstPct;
	@FXML private TextField txtIgstTotal;

	@FXML private ComboBox<String> cmbBankName;
	@FXML private TextField txtTransport;
	@FXML private TextField txtNotes;
	@FXML private TextField txtNetTotal;
	@FXML private TextField txtTransoChrgs;
	@FXML private TextField txtOtherChargs;
	@FXML private TextField txtGrandTotal;

	@FXML private Button btnSave;
	@FXML private Button btnPrint;
	@FXML private Button btnSavePdf;
	@FXML private Button btnClearAll;
	@FXML private Button btnExit;

	@FXML private StackPane loadingOverlay;
	@FXML private ProgressIndicator progressIndicator;

	@FXML private TextField txtSearchCustomer;
	@FXML private DatePicker dateSearchStart;
	@FXML private DatePicker dateSearchEnd;
	@FXML private Button btnSearchHistory;
	@FXML private Button btnClearSearch;
	@FXML private TableView<Quotation> tableHistory;
	@FXML private TableColumn<Quotation, Long> colHistNo;
	@FXML private TableColumn<Quotation, java.time.LocalDate> colHistDate;
	@FXML private TableColumn<Quotation, String> colHistCustomer;
	@FXML private TableColumn<Quotation, Float> colHistTotal;
	@FXML private TableColumn<Quotation, String> colHistStatus;
	@FXML private TableColumn<Quotation, String> colHistBilled;
	@FXML private Button btnEditQuotation;
	@FXML private Button btnGenerateBill;
	@FXML private Button btnRefreshHistory;

	private ObservableList<Quotation> historyList = FXCollections.observableArrayList();

	private static final String DEFAULT_PDF_PATH = "D:\\Software\\Prints\\quotation.pdf";

	private QuotationService quotationService;
	private CustomerService customerService;
	private ItemService itemService;
	private EmployeeService employeeService;
	private BankService bankService;
	private CounterStockDataService counterStockDataService;
	private AlertNotification notification;
	private Login login;

	private ObservableList<QuotationTransaction> trList = FXCollections.observableArrayList();
	private SuggestionProvider<String> customerNameProvider;
	private ObservableList<String> customerNameList = FXCollections.observableArrayList();
	private long currentQuotationId = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		quotationService = new QuotationServiceImpl();
		customerService = new CustomerServiceImpl();
		itemService = new ItemServiceImpl();
		employeeService = new EmployeeServiceImpl();
		bankService = new BankServiceImpl();
		counterStockDataService = new CounterStockDataServiceImpl();
		notification = new AlertNotification();
		login = ViewUtil.login;

		date.setValue(LocalDate.now());
		dateValidUntil.setValue(LocalDate.now().plusDays(7));
		txtNotes.setText("ESTIMATE / QUOTATION");

		colSrNo.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, Long>("id"));
		colItemName.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, String>("itemname"));
		colUnit.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, String>("unit"));
		colQty.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, Float>("quantity"));
		colRate.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, Float>("rate"));
		colAmount.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, Float>("amount"));
		colIgstPct.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, Float>("igstPercent"));
		colIgstAmt.setCellValueFactory(new PropertyValueFactory<QuotationTransaction, Float>("igstAmount"));
		table.setItems(trList);

		txtQuotationNo.setText("" + quotationService.getNewQuotationNo());

		customerNameList.addAll(customerService.getAllCustomerNames());
		customerNameProvider = SuggestionProvider.create(customerNameList);
		new AutoCompletionTextFieldBinding<>(txtCustomerName, customerNameProvider);

		CommonData.setStockItemNames();
		TextFields.bindAutoCompletion(txtItemName, CommonData.stockItemNames);

		if (login != null && login.getId() == 1) {
			cmbSalesman.getItems().addAll(employeeService.getAllSalesmanNames());
		} else if (login != null && login.getEmployee() != null) {
			String name = login.getEmployee().getFname() + " " + login.getEmployee().getMname() + " " + login.getEmployee().getLname();
			cmbSalesman.getItems().add(name);
			cmbSalesman.setValue(name);
		}
		cmbBankName.getItems().addAll(bankService.getAllBankNames());

		colHistNo.setCellValueFactory(new PropertyValueFactory<Quotation, Long>("id"));
		colHistDate.setCellValueFactory(new PropertyValueFactory<Quotation, java.time.LocalDate>("date"));
		colHistCustomer.setCellValueFactory(p -> {
			Quotation q = p.getValue();
			if (q.getCustomer() == null) return new SimpleStringProperty("");
			String name = safe(q.getCustomer().getFname()) + " " + safe(q.getCustomer().getMname()) + " " + safe(q.getCustomer().getLname());
			return new SimpleStringProperty(name.replaceAll(" +", " ").trim());
		});
		colHistTotal.setCellValueFactory(p -> new SimpleObjectProperty<Float>(
				p.getValue().getNettotal() + p.getValue().getIgstTotal() + p.getValue().getTransportingchrges() + p.getValue().getOtherchargs()));
		colHistStatus.setCellValueFactory(new PropertyValueFactory<Quotation, String>("status"));
		colHistBilled.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().isBilled() ? "YES" : "NO"));
		tableHistory.setItems(historyList);

		loadHistory(null, null, null);
	}

	private void loadHistory(String customer, java.time.LocalDate s, java.time.LocalDate e) {
		setBusy(true);
		Task<java.util.List<Quotation>> task = new Task<java.util.List<Quotation>>() {
			@Override
			protected java.util.List<Quotation> call() throws Exception {
				if ((customer == null || customer.isEmpty()) && s == null && e == null) {
					return quotationService.getAllQuotations();
				}
				return quotationService.searchQuotations(customer, s, e);
			}
		};
		task.setOnSucceeded(ev -> {
			historyList.setAll(task.getValue());
			setBusy(false);
		});
		task.setOnFailed(ev -> {
			task.getException().printStackTrace();
			setBusy(false);
			notification.showErrorMessage("Error loading quotations: " + task.getException().getMessage());
		});
		Thread t = new Thread(task, "Quotation-History");
		t.setDaemon(true);
		t.start();
	}

	private void setBusy(boolean busy) {
		if (loadingOverlay != null) {
			loadingOverlay.setVisible(busy);
			loadingOverlay.setManaged(busy);
		}
		btnSearchHistory.setDisable(busy);
		btnClearSearch.setDisable(busy);
		btnEditQuotation.setDisable(busy);
		if (btnGenerateBill != null) btnGenerateBill.setDisable(busy);
		btnRefreshHistory.setDisable(busy);
		btnSave.setDisable(busy);
		btnPrint.setDisable(busy);
		btnSavePdf.setDisable(busy);
	}

	@FXML
	void btnSearchHistoryAction(ActionEvent event) {
		loadHistory(txtSearchCustomer.getText(), dateSearchStart.getValue(), dateSearchEnd.getValue());
	}

	@FXML
	void btnClearSearchAction(ActionEvent event) {
		txtSearchCustomer.setText("");
		dateSearchStart.setValue(null);
		dateSearchEnd.setValue(null);
		loadHistory(null, null, null);
	}

	@FXML
	void btnRefreshHistoryAction(ActionEvent event) {
		loadHistory(txtSearchCustomer.getText(), dateSearchStart.getValue(), dateSearchEnd.getValue());
	}

	@FXML
	void btnEditQuotationAction(ActionEvent event) {
		Quotation selected = tableHistory.getSelectionModel().getSelectedItem();
		if (selected == null) {
			notification.showErrorMessage("Select a quotation from the right list");
			return;
		}
		if (selected.isBilled()) {
			notification.showErrorMessage("This quotation has been billed and cannot be edited");
			return;
		}
		Quotation q = quotationService.getQuotationById(selected.getId());
		if (q == null) {
			notification.showErrorMessage("Quotation not found");
			return;
		}
		loadIntoForm(q);
	}

	@FXML
	void btnGenerateBillAction(ActionEvent event) {
		Quotation selected = tableHistory.getSelectionModel().getSelectedItem();
		if (selected == null) {
			notification.showErrorMessage("Select a quotation from the right list");
			return;
		}
		if (selected.isBilled()) {
			notification.showErrorMessage("Bill is already generated for this quotation");
			return;
		}
		Quotation full = quotationService.getQuotationById(selected.getId());
		String stockMessage = checkStockAvailability(full != null ? full : selected);
		if (stockMessage != null) {
			notification.showErrorMessage(stockMessage);
			return;
		}
		CommonData.billFromQuotationId = selected.getId();
		if (mainPanel == null || !(mainPanel.getParent() instanceof BorderPane)) {
			notification.showErrorMessage("Cannot open Billing screen");
			CommonData.billFromQuotationId = 0;
			return;
		}
		BorderPane parent = (BorderPane) mainPanel.getParent();
		Pane billing = new ViewUtil().getPage("transaction/BillingFrame");
		if (billing == null) {
			notification.showErrorMessage("Cannot load Billing screen");
			CommonData.billFromQuotationId = 0;
			return;
		}
		mainPanel.setVisible(false);
		parent.setCenter(billing);
		billing.setVisible(true);
	}

	private String checkStockAvailability(Quotation q) {
		if (q == null || q.getTransaction() == null || q.getTransaction().isEmpty()) {
			return null;
		}
		java.util.Map<String, Float> required = new java.util.LinkedHashMap<>();
		java.util.Map<String, String> units = new java.util.HashMap<>();
		for (QuotationTransaction tr : q.getTransaction()) {
			if (tr.getItemname() == null) continue;
			required.merge(tr.getItemname(), tr.getQuantity(), Float::sum);
			if (tr.getUnit() != null) units.putIfAbsent(tr.getItemname(), tr.getUnit());
		}
		StringBuilder shortages = new StringBuilder();
		for (java.util.Map.Entry<String, Float> e : required.entrySet()) {
			String itemname = e.getKey();
			float needed = e.getValue();
			float available = counterStockDataService.getCounterItemStock(itemname);
			if (needed > available) {
				if (shortages.length() > 0) shortages.append("\n");
				String unit = units.getOrDefault(itemname, "");
				shortages.append("• ").append(itemname)
						.append("  required ").append(needed).append(" ").append(unit)
						.append(", available ").append(available).append(" ").append(unit);
			}
		}
		if (shortages.length() == 0) return null;
		return "Insufficient stock to generate bill from this quotation.\n"
				+ "Please add stock for the items below and try again:\n\n"
				+ shortages.toString();
	}

	private void loadIntoForm(Quotation q) {
		currentQuotationId = q.getId();
		txtQuotationNo.setText("" + q.getId());
		date.setValue(q.getDate());
		dateValidUntil.setValue(q.getValidUntil());
		if (q.getCustomer() != null) {
			String cname = (safe(q.getCustomer().getFname()) + " " + safe(q.getCustomer().getMname()) + " " + safe(q.getCustomer().getLname())).replaceAll(" +", " ").trim();
			txtCustomerName.setText(cname);
			searchCustomer(null);
		}
		if (q.getEmployee() != null) {
			String ename = safe(q.getEmployee().getFname()) + " " + safe(q.getEmployee().getMname()) + " " + safe(q.getEmployee().getLname());
			cmbSalesman.setValue(ename.replaceAll(" +", " ").trim());
		}
		if (q.getBank() != null) {
			cmbBankName.setValue(q.getBank().getBankname());
		}
		txtTransport.setText(safe(q.getNotes()));
		txtNotes.setText(q.getStatus() == null || q.getStatus().isEmpty() ? "ESTIMATE / QUOTATION" : q.getStatus());
		txtTransoChrgs.setText("" + q.getTransportingchrges());
		txtOtherChargs.setText("" + q.getOtherchargs());
		trList.clear();
		if (q.getTransaction() != null) {
			int sr = 1;
			for (QuotationTransaction tr : q.getTransaction()) {
				QuotationTransaction copy = new QuotationTransaction(tr.getItemname(), tr.getUnit(), tr.getRate(), tr.getQuantity(), tr.getAmount(), tr.getHsn(), null);
				copy.setIgstPercent(tr.getIgstPercent());
				copy.setIgstAmount(tr.getIgstAmount());
				copy.setId(sr++);
				trList.add(copy);
			}
		}
		recomputeNetTotal();
		recalculateIgstTotal();
		recomputeGrandTotal();
	}

	@FXML
	void customerNameAction(ActionEvent event) {
		if (!txtCustomerName.getText().isEmpty()) btnSearch.requestFocus();
	}

	@FXML
	void searchCustomer(ActionEvent event) {
		try {
			if (txtCustomerName.getText() == null || txtCustomerName.getText().isEmpty()) {
				txtCustomerName.requestFocus();
				return;
			}
			Customer customer = customerService.getCustomerByName(txtCustomerName.getText());
			if (customer != null) {
				txtCustomerInfo.setText(safe(customer.getMobileno()) + "\n"
						+ safe(customer.getAddress()) + " City-" + safe(customer.getCity())
						+ "\nTaluka-" + safe(customer.getTaluka())
						+ " District-" + safe(customer.getDistrict())
						+ " Pin-" + customer.getPin());
				cmbSalesman.requestFocus();
			} else {
				notification.showErrorMessage("No Customer Found Select Again !!!");
				txtCustomerName.requestFocus();
				txtCustomerInfo.setText("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			notification.showErrorMessage(e.getMessage());
		}
	}

	private static String safe(String s) { return s == null ? "" : s; }

	@FXML
	void btnNewAction(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(Main.class.getResource("/view/create/AddCustomerFrame.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Add Customer");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(((Node) event.getSource()).getScene().getWindow());
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				customerNameList.clear();
				customerNameList.addAll(customerService.getAllCustomerNames());
				customerNameProvider.clearSuggestions();
				customerNameProvider.addPossibleSuggestions(customerNameList);
			}
		});
	}

	@FXML
	void searchItem(ActionEvent event) {
		if (txtItemName.getText() == null || txtItemName.getText().isEmpty()) {
			txtItemName.requestFocus();
			return;
		}
		Item item = itemService.getItemByName(txtItemName.getText());
		if (item != null) {
			txtUnit.setText(safe(item.getUnit()));
			txtRate.setText("" + item.getRate());
			txtIgstPct.setText("" + item.getIgst());
			txtQty.requestFocus();
		}
	}

	@FXML
	void txtQtyAction(ActionEvent event) {
		try {
			if (txtQty.getText() == null || txtQty.getText().isEmpty()) return;
			if (txtItemName.getText().isEmpty() || txtUnit.getText().isEmpty()) {
				notification.showErrorMessage("Select Item Again!!!");
				txtItemName.requestFocus();
				return;
			}
			if (!isNumber(txtRate.getText())) {
				notification.showErrorMessage("Enter Rate in Digit!!!");
				txtRate.requestFocus();
				return;
			}
			if (!isNumber(txtQty.getText())) {
				notification.showErrorMessage("Enter Quantity in Digit!!!");
				txtQty.requestFocus();
				txtQty.selectAll();
				return;
			}
			txtAmount.setText("" + (Float.parseFloat(txtRate.getText()) * Float.parseFloat(txtQty.getText())));
			btnAdd.requestFocus();
		} catch (Exception e) {
			notification.showErrorMessage("Enter Quantity in Digits!!!");
			txtQty.setText("");
			txtQty.requestFocus();
		}
	}

	@FXML
	void btnAddAction(ActionEvent event) {
		if (txtAmount.getText().isEmpty() || txtItemName.getText().isEmpty() || txtUnit.getText().isEmpty()) {
			notification.showErrorMessage("Select Item Again");
			txtItemName.requestFocus();
			return;
		}
		float igstPct = parseOrZero(txtIgstPct.getText());
		float igstAmt = Float.parseFloat(txtAmount.getText()) * igstPct / 100f;
		QuotationTransaction tr = new QuotationTransaction(
				txtItemName.getText(),
				txtUnit.getText(),
				Float.parseFloat(txtRate.getText()),
				Float.parseFloat(txtQty.getText()),
				Float.parseFloat(txtAmount.getText()),
				itemService.getItemByName(txtItemName.getText()) != null ? itemService.getItemByName(txtItemName.getText()).getHsn() : "",
				null);
		tr.setIgstPercent(igstPct);
		tr.setIgstAmount(igstAmt);

		int idx = -1;
		for (int i = 0; i < trList.size(); i++) {
			if (trList.get(i).getItemname().equals(tr.getItemname()) && trList.get(i).getRate() == tr.getRate()) {
				idx = i;
				break;
			}
		}
		if (idx == -1) {
			tr.setId(trList.size() + 1);
			trList.add(tr);
		} else {
			float newQty = tr.getQuantity() + trList.get(idx).getQuantity();
			tr.setQuantity(newQty);
			tr.setAmount(newQty * tr.getRate());
			tr.setIgstAmount(tr.getAmount() * igstPct / 100f);
			tr.setId(idx + 1);
			trList.remove(idx);
			trList.add(idx, tr);
		}
		recomputeNetTotal();
		recalculateIgstTotal();
		recomputeGrandTotal();
		clearItemInputs();
		txtItemName.requestFocus();
	}

	@FXML
	void btnClearAction(ActionEvent event) { clearItemInputs(); }

	@FXML
	void btnRemoveAction(ActionEvent event) {
		int sel = table.getSelectionModel().getSelectedIndex();
		if (sel < 0) return;
		trList.remove(sel);
		int n = 1;
		for (QuotationTransaction t : trList) t.setId(n++);
		recomputeNetTotal();
		recalculateIgstTotal();
		recomputeGrandTotal();
	}

	@FXML
	void btnUpdateAction(ActionEvent event) {
		QuotationTransaction tr = table.getSelectionModel().getSelectedItem();
		if (tr == null) return;
		txtItemName.setText(tr.getItemname());
		txtUnit.setText(tr.getUnit());
		txtRate.setText("" + tr.getRate());
		txtQty.setText("" + tr.getQuantity());
		txtAmount.setText("" + tr.getAmount());
		txtIgstPct.setText("" + tr.getIgstPercent());
	}

	@FXML
	void totalsAction(ActionEvent event) { recomputeGrandTotal(); }

	@FXML
	void btnSaveAction(ActionEvent event) {
		if (validateData() != 1) return;
		Quotation q = buildQuotationFromForm();
		int flag = quotationService.saveQuotation(q);
		if (flag == 1) {
			long savedId = q.getId();
			notification.showSuccessMessage("Quotation Saved (No " + savedId + ")");
			loadHistory(null, null, null);
			askPrintAndOpen(savedId);
			resetForm();
		} else if (flag == 2) {
			notification.showErrorMessage("This quotation has been billed and cannot be edited");
		} else {
			notification.showErrorMessage("Failed to save quotation");
		}
	}

	@FXML
	void btnPrintAction(ActionEvent event) {
		long id;
		Quotation selected = tableHistory.getSelectionModel().getSelectedItem();
		if (selected != null) {
			id = selected.getId();
		} else {
			id = ensureSavedForPrint();
			if (id == 0) return;
		}
		try {
			new GenerateQuotation(id, DEFAULT_PDF_PATH);
			new PrintFile().openFile(DEFAULT_PDF_PATH);
		} catch (Exception e) {
			notification.showErrorMessage("Error generating PDF: " + e.getMessage());
		}
	}

	@FXML
	void btnSavePdfAction(ActionEvent event) {
		long id = ensureSavedForPrint();
		if (id == 0) return;
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Save Quotation PDF");
		chooser.setInitialFileName("Quotation_" + id + ".pdf");
		chooser.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
		Stage owner = (Stage) mainPanel.getScene().getWindow();
		File f = chooser.showSaveDialog(owner);
		if (f == null) return;
		try {
			new GenerateQuotation(id, f.getAbsolutePath());
			notification.showSuccessMessage("PDF saved: " + f.getAbsolutePath());
		} catch (Exception e) {
			notification.showErrorMessage("Error saving PDF: " + e.getMessage());
		}
	}

	private void askPrintAndOpen(long id) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initModality(Modality.APPLICATION_MODAL);
		if (mainPanel != null && mainPanel.getScene() != null) {
			alert.initOwner(mainPanel.getScene().getWindow());
		}
		alert.setTitle("Print Quotation");
		alert.setHeaderText("Quotation No " + id + " saved");
		alert.setContentText("Do you want to open the Quotation PDF now?");
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {
			try {
				new GenerateQuotation(id, DEFAULT_PDF_PATH);
				new PrintFile().openFile(DEFAULT_PDF_PATH);
			} catch (Exception e) {
				notification.showErrorMessage("Error opening PDF: " + e.getMessage());
			}
		}
	}

	private long ensureSavedForPrint() {
		if (currentQuotationId != 0) return currentQuotationId;
		if (validateData() != 1) return 0;
		Quotation q = buildQuotationFromForm();
		int flag = quotationService.saveQuotation(q);
		if (flag != 1) {
			notification.showErrorMessage("Failed to save quotation");
			return 0;
		}
		currentQuotationId = q.getId();
		txtQuotationNo.setText("" + q.getId());
		return currentQuotationId;
	}

	@FXML
	void btnClearAllAction(ActionEvent event) { resetForm(); }

	@FXML
	void btnExitAction(ActionEvent event) {
		if (mainPanel != null) mainPanel.setVisible(false);
	}

	private Quotation buildQuotationFromForm() {
		Quotation q = new Quotation();
		q.setId(currentQuotationId);
		q.setCustomer(customerService.getCustomerByName(txtCustomerName.getText()));
		q.setDate(date.getValue());
		q.setValidUntil(dateValidUntil.getValue());
		q.setNettotal(parseOrZero(txtNetTotal.getText()));
		q.setTransportingchrges(parseOrZero(txtTransoChrgs.getText()));
		q.setOtherchargs(parseOrZero(txtOtherChargs.getText()));
		q.setBank(cmbBankName.getValue() != null ? bankService.getBankByName(cmbBankName.getValue()) : null);
		q.setEmployee(cmbSalesman.getValue() != null ? employeeService.getEmployeeByName(cmbSalesman.getValue()) : null);
		q.setNotes(txtTransport.getText());
		q.setStatus(txtNotes.getText() == null || txtNotes.getText().isEmpty() ? "ESTIMATE / QUOTATION" : txtNotes.getText());
		q.setIgstTotal(parseOrZero(txtIgstTotal.getText()));

		List<QuotationTransaction> copy = new ArrayList<>();
		for (QuotationTransaction tr : trList) {
			QuotationTransaction nt = new QuotationTransaction(tr.getItemname(), tr.getUnit(),
					tr.getRate(), tr.getQuantity(), tr.getAmount(), tr.getHsn(), q);
			nt.setIgstPercent(tr.getIgstPercent());
			nt.setIgstAmount(tr.getIgstAmount());
			copy.add(nt);
		}
		q.setTransaction(copy);
		return q;
	}

	private int validateData() {
		if (date.getValue() == null) {
			notification.showErrorMessage("Select Quotation Date!!!");
			date.requestFocus();
			return 0;
		}
		if (customerService.getCustomerByName(txtCustomerName.getText()) == null) {
			notification.showErrorMessage("Select Customer!!!");
			txtCustomerName.requestFocus();
			return 0;
		}
		if (trList.size() == 0) {
			notification.showErrorMessage("No items to save!!!");
			txtItemName.requestFocus();
			return 0;
		}
		if (cmbBankName.getValue() == null) {
			notification.showErrorMessage("Select Payment Bank!!!");
			cmbBankName.requestFocus();
			return 0;
		}
		return 1;
	}

	private void recomputeNetTotal() {
		float sum = 0f;
		for (QuotationTransaction t : trList) sum += t.getAmount();
		txtNetTotal.setText("" + sum);
	}

	private void recalculateIgstTotal() {
		float sum = 0f;
		for (QuotationTransaction t : trList) sum += t.getIgstAmount();
		txtIgstTotal.setText(String.format("%.2f", sum));
	}

	private void recomputeGrandTotal() {
		float net = parseOrZero(txtNetTotal.getText());
		float tp = parseOrZero(txtTransoChrgs.getText());
		float oc = parseOrZero(txtOtherChargs.getText());
		float igst = parseOrZero(txtIgstTotal.getText());
		txtGrandTotal.setText("" + (net + igst + tp + oc));
	}

	private float parseOrZero(String s) {
		try { return Float.parseFloat(s); } catch (Exception e) { return 0f; }
	}

	private boolean isNumber(String s) {
		if (s == null) return false;
		try { Float.parseFloat(s); return true; } catch (Exception e) { return false; }
	}

	private void clearItemInputs() {
		txtItemName.setText("");
		txtUnit.setText("");
		txtRate.setText("");
		txtAmount.setText("");
		txtQty.setText("");
		txtIgstPct.setText("");
	}

	private void resetForm() {
		currentQuotationId = 0;
		txtQuotationNo.setText("" + quotationService.getNewQuotationNo());
		date.setValue(LocalDate.now());
		dateValidUntil.setValue(LocalDate.now().plusDays(7));
		txtCustomerName.setText("");
		txtCustomerInfo.setText("");
		cmbSalesman.getSelectionModel().clearSelection();
		clearItemInputs();
		trList.clear();
		cmbBankName.getSelectionModel().clearSelection();
		txtTransport.setText("");
		txtNotes.setText("ESTIMATE / QUOTATION");
		txtNetTotal.setText("0.0");
		txtTransoChrgs.setText("0.0");
		txtOtherChargs.setText("0.0");
		txtIgstTotal.setText("0.0");
		txtGrandTotal.setText("0.0");
	}

	private void showPrintConfirmation(long id) {
		Stage stage = (Stage) mainPanel.getScene().getWindow();
		Alert alert = new Alert(AlertType.CONFIRMATION, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setHeaderText("Quotation No " + id);
		alert.getDialogPane().setContentText("PDF generated at:\n" + DEFAULT_PDF_PATH);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			// no-op
		}
	}
}
