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

	@FXML
	void openBilling(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (purchase != null)
			purchase.setVisible(false);
		billing = viewUtil.getPage("transaction/BillingFrame");
		pane.setCenter(billing);
		billing.setVisible(true);
	}

	@FXML
	void openPurchaseBilling(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (billing != null)
			billing.setVisible(false);

		purchase = viewUtil.getPage("transaction/PurchaseInviceFrame");
		pane.setCenter(purchase);
		purchase.setVisible(true);
	}

	@FXML
	void btnPaymentRecievedAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (billing != null)
			billing.setVisible(false);

		paymentRecieved = viewUtil.getPage("transaction/CustomerPayment");
		pane.setCenter(paymentRecieved);
		paymentRecieved.setVisible(true);
	}

	@FXML
	void openViewAllBill(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (billing != null)
			billing.setVisible(false);

		viewAllBills = viewUtil.getPage("report/ViewAllBills");
		pane.setCenter(viewAllBills);
		viewAllBills.setVisible(true);
	}

	@FXML
	void btnPayInvoiceAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (billing != null)
			billing.setVisible(false);

		payInvoice = viewUtil.getPage("transaction/PayPurchaseBills");
		pane.setCenter(payInvoice);
		payInvoice.setVisible(true);
	}

	@FXML
	void btnViewAllInvoicesAction(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (billing != null)
			billing.setVisible(false);

		viewInvoices = viewUtil.getPage("report/ViewAllInvoices");
		pane.setCenter(viewInvoices);
		viewInvoices.setVisible(true);
	}

	@FXML
	void openCuttingOrder(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (billing != null)
			billing.setVisible(false);

		cuttingOrder = viewUtil.getPage("transaction/CuttingOrderFrame2");
		pane.setCenter(cuttingOrder);
		cuttingOrder.setVisible(true);
	}

	@FXML
	void btnViewCounterStock(ActionEvent event) {
		pane = (BorderPane) transactionMenuPanel.getParent();

		if (billing != null)
			billing.setVisible(false);

		cuttingOrder = viewUtil.getPage("report/viewcounterstock");
		pane.setCenter(cuttingOrder);
		cuttingOrder.setVisible(true);

	}

	@FXML
	void btnAddCounterStock(ActionEvent event) {
		if (ViewUtil.login.getId() != 1) {
			new AlertNotification().showErrorMessage("You are not authorised to see this page");
			return;
		}
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null)
			billing.setVisible(false);

		cuttingOrder = viewUtil.getPage("transaction/counterstock");
		pane.setCenter(cuttingOrder);
		cuttingOrder.setVisible(true);
	}

	@FXML
	void btnPaymentReceiptAction(ActionEvent event) {
		pane=null;
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null)
			billing.setVisible(false);

		cuttingOrder = viewUtil.getPage("transaction/paymentreciept");
		pane.setCenter(cuttingOrder);
		cuttingOrder.setVisible(true);
	}
	@FXML
	void btnAdvancePayment(ActionEvent event) {
		pane=null;
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null)
			billing.setVisible(false);

		cuttingOrder = viewUtil.getPage("transaction/advancepayment");
		pane.setCenter(cuttingOrder);
		cuttingOrder.setVisible(true);
	}
	@FXML
    void btnCustomerAdvancePayment(ActionEvent event) {
		pane=null;
		pane = (BorderPane) transactionMenuPanel.getParent();
		if (billing != null)
			billing.setVisible(false);

		cuttingOrder = viewUtil.getPage("transaction/customeradvancepayment");
		pane.setCenter(cuttingOrder);
		cuttingOrder.setVisible(true);

    }
	   @FXML
	    void btnBankTransferAction(ActionEvent event) {
		   pane=null;
			pane = (BorderPane) transactionMenuPanel.getParent();
			if (billing != null)
				billing.setVisible(false);

			cuttingOrder = viewUtil.getPage("transaction/bankmoneytransfer");
			pane.setCenter(cuttingOrder);
			cuttingOrder.setVisible(true);

	    }

}
