package main.java.main.java.controller.report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.main.java.hibernate.entities.*;
import main.java.main.java.hibernate.reportEntity.LabourCharges;
import main.java.main.java.hibernate.reportEntity.LabourChargesPaid;
import main.java.main.java.hibernate.service.service.*;
import main.java.main.java.hibernate.service.serviceImpl.*;
import main.java.main.java.print.LabourChargesPrint;
import main.java.main.java.print.PrintFile;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LabourCommisionController implements Initializable {
	@FXML private AnchorPane mainPane;
	@FXML private ComboBox<String> cmbLabourName;
	@FXML private DatePicker dateStarting;
	@FXML private DatePicker dateEnd;
	@FXML private Button btnShow;
	@FXML private Button btnShowAll;
	@FXML private Button btnClear;

	@FXML private TableView<LabourCharges> table;
	@FXML private TableColumn<LabourCharges,Long> colId;
	@FXML private TableColumn<LabourCharges,LocalDate> colDate;
	@FXML private TableColumn<LabourCharges,String> colItemName;
	@FXML private TableColumn<LabourCharges,Double> colQty;
	@FXML private TableColumn<LabourCharges,Double> colLabourChrgs;
	@FXML private TableColumn<LabourCharges,Double> colPaidChargs;

	@FXML private TextField txtTotalCharges;
	@FXML private TextField txtTotalQty;
	@FXML private ComboBox<String> cmbBankName;
	@FXML private TextField txtTransactionNo;
	@FXML private Button btnPay;
	@FXML private Button btnClear2;
	@FXML private Button btnPrint;
	@FXML private Button btnExit;

	@FXML private TableView<LabourChargesPaid> tablepaidCommisions;
	@FXML private TableColumn<LabourChargesPaid,Long> colSrNo;
	@FXML private TableColumn<LabourChargesPaid,String> colLabourName;
	@FXML private TableColumn<LabourChargesPaid,LocalDate> colDate2;
	@FXML private TableColumn<LabourChargesPaid,Double> colCommision;
	@FXML private TableColumn<LabourChargesPaid,Double> colPaid;

	private EmployeeService employeeService;
	private BankService bankService;
	private CuttingOrderService cuttingService;
	private ItemService itemService;
	private LabourChargesService labourChargesService;

	private ObservableList<LabourCharges>chargesList = FXCollections.observableArrayList();
	private ObservableList<LabourChargesPaid>paidList = FXCollections.observableArrayList();



	private ObservableList<main.java.main.java.hibernate.entities.LabourCharges>allPaidChargesList = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		employeeService = new EmployeeServiceImpl();
		bankService = new BankServiceImpl();
		cuttingService = new CuttingOrderServiceImpl();
		itemService = new ItemServiceImpl();
		labourChargesService = new LabourChargesServiceImpl();
		cmbLabourName.getItems().addAll(employeeService.getAllEmployeeNames());
		cmbBankName.getItems().addAll(bankService.getAllBankNames());

		allPaidChargesList.addAll(labourChargesService.getAllLabourCharges());

		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colItemName.setCellValueFactory(new PropertyValueFactory<>("item"));
		colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
		colLabourChrgs.setCellValueFactory(new PropertyValueFactory<>("labourcharges"));
		colPaidChargs.setCellValueFactory(new PropertyValueFactory<>("paidLabourCharges"));
		table.setItems(chargesList);

		//odl Commision
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colLabourName.setCellValueFactory(new PropertyValueFactory<>("labourName"));
		colDate2.setCellValueFactory(new PropertyValueFactory<>("date"));
		colCommision.setCellValueFactory(new PropertyValueFactory<>("commision"));
		colPaid.setCellValueFactory(new PropertyValueFactory<>("paidCommision"));
		oldListToPaidList();
		tablepaidCommisions.setItems(paidList);
	}
	@FXML void btnClear(ActionEvent event) {
		cmbLabourName.getSelectionModel().clearSelection();
		dateStarting.setValue(null);
		dateEnd.setValue(null);


	}

	@FXML
	void btnClear2(ActionEvent event) {
		chargesList.clear();
		txtTotalCharges.setText(""+0);
		txtTotalQty.setText(""+0);
		cmbBankName.getSelectionModel().clearSelection();
		txtTransactionNo.setText("");
	}

	@FXML
	void btnExitAction(ActionEvent event) {
		mainPane.setVisible(false);
	}

	@FXML
	void btnPayAction(ActionEvent event) {
		try {
			if(chargesList.size()==0)
			{
				new Alert(AlertType.ERROR,"No data to save!!!!").showAndWait();
				return;
			}
			if(txtTotalCharges.getText().equals("") ||txtTotalCharges.getText().equals("0.0"))
			{
				new Alert(AlertType.ERROR,"No Charges to Pay").showAndWait();
				return;
			}
			if(cmbBankName.getSelectionModel().getSelectedItem()==null) {
				new Alert(AlertType.ERROR,"Select Bank to Pay Labourcharges!!!").showAndWait();
				cmbBankName.requestFocus();
				return;
			}
			if(txtTransactionNo.getText().equals(""))
			{
				txtTransactionNo.setText("-");
			}
			main.java.main.java.hibernate.entities.LabourCharges charges = new main.java.main.java.hibernate.entities.LabourCharges();
			charges.setAmount(Float.parseFloat(txtTotalCharges.getText()));
			charges.setBank(bankService.getBankByName(cmbBankName.getValue()));
			charges.setDate(LocalDate.now());
			charges.setLabour(employeeService.getEmployeeByName(cmbLabourName.getValue()));
			charges.setBankReffNo(txtTransactionNo.getText());
			LabourChargesTransaction tr ;
			List<LabourChargesTransaction>list = new ArrayList<LabourChargesTransaction>();
			for(LabourCharges ch:chargesList)
			{
				tr = new LabourChargesTransaction();
				tr.setCharges(ch.getLabourcharges());
				tr.setDate(ch.getDate());
				tr.setItem(ch.getItem());
				tr.setLabourCharges(charges);
				tr.setPaidLabourCharges(tr.getCharges());
				tr.setQty(ch.getQty());
				list.add(tr);
			}
			charges.setTransaction(list);
			int flag=labourChargesService.saveLabourCharges(charges);

			if(flag==1)
			{
				//change pay cutting order Labour charges
				for(LabourCharges lc:chargesList)
				{
					cuttingService.updatePaidLabourCharges(lc.getId(),charges.getLabour().getId());
				}
				//credit from Bank
				BankTransaction btr = new BankTransaction(
						"Pay Labour Charges id "+charges.getId(),
						charges.getId(),
						0,
						charges.getAmount(),
						bankService.getBankByName(cmbBankName.getValue()).getId(),
						charges.getDate());
				BankTransactionService btrService = new BankTransactionServiceImpl();
				int b =btrService.saveBankTransaction(btr);
				if(b==1)
					bankService.reduceBankBalance((int)(charges.getId()),charges.getAmount());

				new Alert(AlertType.INFORMATION,"Record saved").showAndWait();
				allPaidChargesList.add(charges);
				oldListToPaidList();
				btnClear2.fire();
				btnClear.fire();
				showPrintBillConfirmation(charges.getId());
			}
		} catch (Exception e) {
			new Alert(AlertType.ERROR,"Error in pay Labour Charges !!!"+e.getMessage()).showAndWait();

		}

	}

	@FXML
	void btnPrint(ActionEvent event) {
		try {
			if(tablepaidCommisions.getSelectionModel().getSelectedItem()==null)
			{
				return;
			}
			LabourChargesPaid p = tablepaidCommisions.getSelectionModel().getSelectedItem();
			if(p==null)
			{
				return;
			}
			//new LabourChargesPrint(p.getId());
			showPrintBillConfirmation(p.getId());
			//new PrintFile("D:\\Software\\Prints\\LabouCharges.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void btnShowAction(ActionEvent event) {
		try {
			chargesList.clear();
			txtTotalCharges.setText("");
			txtTotalQty.setText("");
			txtTransactionNo.setText("");
			cmbBankName.getSelectionModel().clearSelection();
			if(cmbLabourName.getSelectionModel().getSelectedItem()==null)
			{
				new Alert(AlertType.ERROR,"Select Labour Name!!!").showAndWait();
				cmbLabourName.requestFocus();
				return;
			}
			if(dateStarting.getValue()==null)
			{
				new Alert(AlertType.ERROR,"Select Starting Date!!!").showAndWait();
				dateStarting.requestFocus();
				return;
			}
			if(dateEnd.getValue()==null)
			{
				new Alert(AlertType.ERROR,"Select End Date!!!").showAndWait();
				dateEnd.requestFocus();
				return;
			}
			List<CuttingOrder> orderList= cuttingService.getPeriodWiseCuttingOrder(dateStarting.getValue(), dateEnd.getValue());
			long sr=0;
			double totalQty=0,totalcharge=0;
			LabourCharges lc=null;
			String name="";
			for(CuttingOrder order:orderList)
			{

				for(CuttingTransaction ctr:order.getTransaction())
				{
					for(CuttingLabour cl:ctr.getLabourList())
					{
						name = cl.getLabour().getFullName();
						if(name.equals(cmbLabourName.getValue()) && cl.getPaidCuttingCharges()< cl.getCuttingCharges())
						{
							lc = null;
							System.out.println("Order date++++====>"+order.getDate());
							lc = new LabourCharges(
									ctr.getCuttingOrder().getId(),
									order.getDate(),
									ctr.getItem().getItemname(),
									cl.getQuantity(),
									cl.getCuttingCharges(),
									cl.getPaidCuttingCharges());

							chargesList.add(lc);
							totalQty = totalQty+cl.getQuantity();
							totalcharge = totalcharge+(cl.getCuttingCharges()-cl.getPaidCuttingCharges());
						}
					}
				}
			}
			txtTotalCharges.setText(""+totalcharge);
			txtTotalQty.setText(""+totalQty);
		} catch (Exception e) {
			e.printStackTrace();
			new Alert(AlertType.ERROR,"Error in Show!!!"+e.getMessage()).showAndWait();
		}
	}

	private void showPrintBillConfirmation(long id) {
		Stage stage = (Stage) mainPane.getScene().getWindow();
		AlertType type = AlertType.CONFIRMATION;
		Alert alert = new Alert(type, "Print?");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		alert.getDialogPane().setContentText("Do You Want Print Labour Charges Report");
		alert.getDialogPane().setHeaderText("Confirmation");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			// new BillPrint(billno);
			try {
				new LabourChargesPrint(id);
				new PrintFile().openFile("D:\\Software\\Prints\\LabouCharges.pdf");;
			} catch (Exception e) {
				new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
			}
		} else if (result.get() == ButtonType.CANCEL) {

		}
	}
	void oldListToPaidList()
	{
		try {
			paidList.clear();
			for(main.java.main.java.hibernate.entities.LabourCharges ch:allPaidChargesList)
			{
				paidList.add(new LabourChargesPaid(ch.getId(),
						ch.getLabour().getFullName(),
						ch.getDate(),
						ch.getAmount(),ch.getAmount()));
			}
		} catch (Exception e) {

		}
	}
}
