package main.java.main.java.controller.transaction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;
public class TransactionMenuControler implements Initializable {

	@FXML private AnchorPane transactionMenuPanel;
 	@FXML private Pane purchaseMenu,menuBankTransaction;
	@FXML private Button btnBilling;
	@FXML private Button btnAllBill;
	@FXML private Button btnPaymentRecieved;
	@FXML private Button btnPayInvoice;
	@FXML private Button btnCuttingOrder;
	@FXML private Button btnViewAllInvoices;
	

	private BorderPane pane;
	private ViewUtil viewUtil;
	private Pane billing, purchase, paymentRecieved, payInvoice, viewAllBills, viewInvoices, cuttingOrder;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewUtil = new ViewUtil();
		if (ViewUtil.login.getId() != 1) {
			purchaseMenu.setVisible(false);
			//menuBankTransaction.setVisible(false);
		}
	}

	private void navigate(Pane page) {
		if (page == null) return;
		pane.setCenter(page);
		page.setVisible(true);
	}

	@FXML
	void openBilling(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (purchase != null) purchase.setVisible(false);
		billing = viewUtil.getPage("transaction/BillsDashboard");
		navigate(billing);
	}

	@FXML
	void openPurchaseBilling(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		purchase = viewUtil.getPage("transaction/PurchaseInvoiceDashboard");
		navigate(purchase);
	}

	@FXML
	void btnPaymentRecievedAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		paymentRecieved = viewUtil.getPage("transaction/CustomerPayment");
		navigate(paymentRecieved);
	}

	@FXML
	void openViewAllBill(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		viewAllBills = viewUtil.getPage("report/ViewAllBills");
		navigate(viewAllBills);
	}

	@FXML
	void btnPayInvoiceAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		payInvoice = viewUtil.getPage("transaction/PayPurchaseBills");
		navigate(payInvoice);
	}

	@FXML
	void btnViewAllInvoicesAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		viewInvoices = viewUtil.getPage("report/ViewAllInvoices");
		navigate(viewInvoices);
	}

	@FXML
	void openCuttingOrder(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("transaction/CuttingOrderFrame2");
		navigate(cuttingOrder);
	}

	@FXML
	void btnViewCounterStock(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("report/viewcounterstock");
		navigate(cuttingOrder);
	}

	@FXML
	void btnAddCounterStock(ActionEvent event) {
		if (ViewUtil.login.getId() != 1) {
			new AlertNotification().showErrorMessage("You are not authorised to see this page");
			return;
		}
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("transaction/counterstock");
		navigate(cuttingOrder);
	}

	@FXML
	void btnPaymentReceiptAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("transaction/paymentreciept");
		navigate(cuttingOrder);
	}

	@FXML
	void btnAdvancePayment(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("transaction/advancepayment");
		navigate(cuttingOrder);
	}

	@FXML
	void btnCustomerAdvancePayment(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("transaction/customeradvancepayment");
		navigate(cuttingOrder);
	}

	@FXML
	void btnBankTransferAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("transaction/bankmoneytransfer");
		navigate(cuttingOrder);
	}

	@FXML
	void openQuotation(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null) billing.setVisible(false);
		cuttingOrder = viewUtil.getPage("transaction/QuotationFrame");
		navigate(cuttingOrder);
	}

}
