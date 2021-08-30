package main.java.main.java.controller.transaction;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.main.java.Main;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.CounterStockData;
import main.java.main.java.hibernate.entities.CuttingLabour;
import main.java.main.java.hibernate.entities.CuttingOrder;
import main.java.main.java.hibernate.entities.CuttingTransaction;
import main.java.main.java.hibernate.reportEntity.CuttingLabourPojo;
import main.java.main.java.hibernate.reportEntity.CuttingOrderPojo;
import main.java.main.java.hibernate.reportEntity.CuttingTransactionPojo;
import main.java.main.java.hibernate.service.service.*;
import main.java.main.java.hibernate.service.serviceImpl.*;
import main.java.main.java.hibernate.util.CommonData;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CuttingOrderControler implements Initializable {
    @FXML private TextField txtOrderId;
    @FXML private DatePicker date;
    @FXML private TextField txtCustomerName;
    @FXML private Button btnAddNewCustomer;
    @FXML private ComboBox<String> cmbSalemanName;
    @FXML private TextField txtItemName;
    @FXML private TextField txtQuantity;
    @FXML private ComboBox<String> cmbLabourName;
    @FXML private TextField txtCuttingQuantity;
    @FXML private Button btnAddLabour;
    @FXML private Button btnRemoveLabour;
    
    @FXML private TableView<CuttingLabourPojo> tableLabourList;
    @FXML private TableColumn<CuttingLabourPojo,Integer> colId;
    @FXML private TableColumn<CuttingLabourPojo,String> colLabourName;
    @FXML private TableColumn<CuttingLabourPojo,String> colCuttingQty;    
    
    @FXML private Button btnAdd;
    @FXML private Button btnClear;
    @FXML private Button btnUpdate;
    @FXML private Button btnRemove;
    
    @FXML private TableView<CuttingTransactionPojo> table;
    @FXML private TableColumn<CuttingTransactionPojo,Integer> colSrNo;
    @FXML private TableColumn<CuttingTransactionPojo,String> colItemName;
    @FXML private TableColumn<CuttingTransactionPojo,Float> colQty;
    @FXML private TableColumn<CuttingTransactionPojo,List<CuttingLabourPojo>>colLabour;
    
    @FXML private Button btnSave;
    @FXML private Button btnClear2;
    @FXML private Button btnUpdate2;
    @FXML private Button btnBack;
    
    @FXML private TableView<CuttingOrderPojo> tableOldOrder;
    @FXML private TableColumn<CuttingOrderPojo,Long> colOrderId;
    @FXML private TableColumn<CuttingOrderPojo,LocalDate> colDate;
    @FXML private TableColumn<CuttingOrderPojo,String> colSalesmanName;
    @FXML private TableColumn<CuttingOrderPojo,String> colCustomerName;
    @FXML private TableColumn<CuttingOrderPojo,String> colLabourName2;

    @FXML private Button btnLoadToday;
    @FXML private DatePicker dateSearch;
    @FXML private Button btnLoadAll;


    
    private ObservableList<String>customerNameList = FXCollections.observableArrayList();
    private ObservableList<CuttingLabourPojo> labourNameList = FXCollections.observableArrayList();
    private ObservableList<CuttingTransactionPojo> cuttingTransactionList = FXCollections.observableArrayList();
    private CustomerService customerService;
    private SuggestionProvider<String> customerNameProvider;
    //private Login login;
    private EmployeeService employeeService;
    private ItemService itemService;
    private CuttingOrderService cuttingService;
    //private ItemStockService stockService;
    private CounterStockDataService counterStockDataService;
    private ObservableList<CuttingOrderPojo>oldCutingOrderList = FXCollections.observableArrayList();
    private AlertNotification notification;
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	customerService = new CustomerServiceImpl();
    	itemService = new ItemServiceImpl();
    	cuttingService = new CuttingOrderServiceImpl();
		customerNameList.addAll(customerService.getAllCustomerNames());
		//login = ViewUtil.login;
		employeeService = new EmployeeServiceImpl();
		//stockService = new ItemStockServiceImpl();
		counterStockDataService = new CounterStockDataServiceImpl();
		notification = new AlertNotification();
		customerNameProvider = SuggestionProvider.create(customerService.getAllCustomerNames());
		new AutoCompletionTextFieldBinding<>(txtCustomerName, customerNameProvider);
		cmbSalemanName.getItems().addAll(employeeService.getAllSalesmanNames());
		/*if (login.getId() == 1) {
			cmbSalemanName.getItems().addAll(employeeService.getAllSalesmanNames());
		} else {
			cmbSalemanName.getItems().add(login.getEmployee().getFname() + " " + login.getEmployee().getMname() + " "
					+ login.getEmployee().getLname());
			// cmbSalesman.getSelectionModel().select(1);
			cmbSalemanName.setValue(login.getEmployee().getFname() + " " + login.getEmployee().getMname() + " "
					+ login.getEmployee().getLname());
		}*/
		cmbLabourName.getItems().addAll(employeeService.getAllEmployeeNames());
		CommonData.setStockItemNames();
		TextFields.bindAutoCompletion(txtItemName, itemService.getCuttingItemNames());
		txtOrderId.setText(""+cuttingService.getNewCuttingOrderId());
		
		//properties of CuttingLabour Table
		colLabourName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colCuttingQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableLabourList.setItems(labourNameList);
	
		date.setValue(LocalDate.now());
		
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		colLabour.setCellValueFactory(new PropertyValueFactory<>("labourList"));
		table.setItems(cuttingTransactionList);
		
		colOrderId.setCellValueFactory(new PropertyValueFactory<>("id")); 
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colSalesmanName.setCellValueFactory(new PropertyValueFactory<>("salesmanName"));
		colCustomerName .setCellValueFactory(new PropertyValueFactory<>("customerName"));
		colLabourName2.setCellValueFactory(new PropertyValueFactory<>("labourName"));
		loadOldCuttingOrder(cuttingService.getDateWiseCuttingOrder(LocalDate.now()));
		tableOldOrder.setItems(oldCutingOrderList);
    }

    
    @FXML
    void btnAddAction(ActionEvent event) {
    	try {
			if(!validateData())
			{
				return;
			}
			List<CuttingLabourPojo>lList = new ArrayList<CuttingLabourPojo>();
			lList.addAll(labourNameList);
			//find item in table
			int flag=-1;
			for(int i=0;i<cuttingTransactionList.size();i++)
			{
				if(cuttingTransactionList.get(i).getItemName().equals(txtItemName.getText()))
				{
					flag=i;
				}
			}
			CuttingTransactionPojo ctp = new CuttingTransactionPojo(txtItemName.getText(), Float.parseFloat(txtQuantity.getText()), lList);
			if(flag==-1)
			{
				//add new
				
				ctp.setId(cuttingTransactionList.size()+1);
				cuttingTransactionList.add(ctp);

				
			}
			else
			{
				//update 
				//System.out.println("Flag="+flag);
				cuttingTransactionList.remove(flag);
				//ctp.setId(++flag);
				cuttingTransactionList.add(flag, ctp);
			}
			txtItemName.setText("");
			txtQuantity.setText(""+0);
			txtCuttingQuantity.setText(""+0);
			
			
			clear();
		} catch (Exception e) {
			notification.showErrorMessage("Error123 "+e.getMessage());
		}
    }

    @FXML
    void btnAddNewCustomerAction(ActionEvent event) throws IOException {
    	Stage stage = new Stage();
		Parent root = FXMLLoader.load(Main.class.getResource("/view/create/AddCustomerFrame.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("My modal window");
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
    void btnAddLabourAction(ActionEvent event) {
    	try {
			if(cmbLabourName.getValue()==null||cmbLabourName.getValue().equals(""))
			{
				notification.showErrorMessage("Select Labour Name!!!");
				cmbLabourName.requestFocus();
				return;
			}
			if(txtCuttingQuantity.getText().equals("") ||!isNumber(txtCuttingQuantity.getText()))
			{
				notification.showErrorMessage("Enter Valied Cutting Quantity!!!");
				txtCuttingQuantity.requestFocus();
				return;
			}
			int flag=0;
			for(CuttingLabourPojo c:labourNameList)
			{
				if(c.getName().equals(cmbLabourName.getValue()))
				{
					flag=1;
					break;
				}
			}
			if(flag==1)
			{
				notification.showErrorMessage("Labour Already Exist!!!");
				cmbLabourName.requestFocus();
				return;
			}
			if((Float.parseFloat(txtQuantity.getText())- getLabourAllQty())<Float.parseFloat(txtCuttingQuantity.getText()))
			{
				notification.showErrorMessage("Quantity must be less than or equal Remaining Cutting Quantity !!!");
				txtCuttingQuantity.requestFocus();
				return;
			}
		CuttingLabourPojo cp =	new CuttingLabourPojo(cmbLabourName.getValue(),Float.parseFloat(txtCuttingQuantity.getText()));
			cp.setId(labourNameList.size()+1);
			labourNameList.add(cp);
			cmbLabourName.setValue("");
			if(Float.parseFloat(txtQuantity.getText())- getLabourAllQty()>0)
			txtCuttingQuantity.setText(""+
			(Float.parseFloat(txtQuantity.getText())- getLabourAllQty()));
			else
				txtCuttingQuantity.setText(""+0);
		} catch (Exception e) {
			e.printStackTrace();
			notification.showErrorMessage("Error in Adding Labour "+e.getMessage());
		}
    }
    @FXML
    void btnRemoveLabourAction(ActionEvent event) {
    	try {
			CuttingLabourPojo c = tableLabourList.getSelectionModel().getSelectedItem();
			if(c==null)
			{
				return;
			}
			tableLabourList.getSelectionModel().getSelectedIndex();
			labourNameList.remove(tableLabourList.getSelectionModel().getSelectedIndex());
			int sr=0;
			for(int i =0;i<labourNameList.size();i++)
			{
				labourNameList.get(i).setId(++sr);
			}
		} catch (Exception e) {
			notification.showErrorMessage("Error in Removing Labour!!!");
			
		}
    }
    @FXML
    void btnBackAction(ActionEvent event) {

    }
    @FXML
    void btnClearAction(ActionEvent event) {
    	clear();
    }

    @FXML
    void btnClear2Action(ActionEvent event) {
    	cuttingTransactionList.clear();
    	cmbSalemanName.getSelectionModel().clearSelection();
    	txtCustomerName.setText("");
    	txtOrderId.setText(""+cuttingService.getNewCuttingOrderId());
    	date.setValue(LocalDate.now());
    	labourNameList.clear();
    }

    @FXML
    void btnRemoveAction(ActionEvent event) {
    	try {
//			CuttingTransactionPojo c =
//					cuttingTransactionList.get(table.getSelectionModel().getSelectedIndex());
			int flag = table.getSelectionModel().getSelectedIndex();
			cuttingTransactionList.remove(flag);
			int sr=0;
			for(int i=0;i<cuttingTransactionList.size();i++)
			{
				cuttingTransactionList.get(i).setId(++sr);
			}
			table.getSelectionModel().clearSelection();
		} catch (Exception e) {
			notification.showErrorMessage("Error in Removing Item");
		}
    }

    @FXML
    void btnSaveAction(ActionEvent event) {
    	try {
			if(!validate())
			{
				return;
			}
			long orderId=Long.parseLong(txtOrderId.getText());
			CuttingOrder oldOrder=null;
			CuttingOrder co = new CuttingOrder(date.getValue(),
					employeeService.getEmployeeByName(cmbSalemanName.getValue()),
					customerService.getCustomerByName(txtCustomerName.getText()),
					null);
			
			List<CuttingTransaction>trList = new ArrayList<CuttingTransaction>();
			List<CuttingLabour>labourList = new ArrayList<CuttingLabour>();
			CuttingTransaction cTransaction=null;
			for(CuttingTransactionPojo ctp:cuttingTransactionList)
			{
				cTransaction = new CuttingTransaction(itemService.getItemByName(ctp.getItemName()),
						ctp.getQuantity(), co, null);
				for(CuttingLabourPojo cl:ctp.getLabourList())
				{
					labourList.add(
							new CuttingLabour(
									employeeService.getEmployeeByName(cl.getName()),
							cl.getQty(),
							cTransaction,
							itemService.getLabourCharges(cTransaction.getItem().getItemname())*cl.getQty(),
							0));
				}
				cTransaction.setLabourList(labourList);
				trList.add(cTransaction);
			}
			co.setTransaction(trList);
			if(orderId==cuttingService.getNewCuttingOrderId()) 
				co.setId(0);
			else {
				co.setId(orderId);
				oldOrder = cuttingService.getCuttingOrderById(orderId);
			}
			String labourNames="";
			for(CuttingTransaction ct:co.getTransaction())
			{
				System.out.println(ct.getItem()+" "+ct.getQuantity());
				for(CuttingLabour cl:ct.getLabourList())
					labourNames=labourNames+" "+
							cl.getLabour().getFname()+" "+
							cl.getLabour().getMname()+" "+
							cl.getLabour().getLname();
			}
			int flag = cuttingService.saveCuttingOrder(co);
			if(flag==1)
			{
				
				//save
				//Add Item Stock
				addStock(co.getTransaction()); 
				notification.showSuccessMessage("Record Save Success!!!");
				txtOrderId.setText(""+cuttingService.getNewCuttingOrderId());
				
				btnClear2.fire();
			}
			else if(flag==2)
			{
				//update
				//reduce old stock
				reduceStock(oldOrder.getTransaction());
				//add new Stock
				addStock(co.getTransaction());
				notification.showSuccessMessage("Record Update Success!!!");
				txtOrderId.setText(""+cuttingService.getNewCuttingOrderId());
				btnClear2.fire();
			}
			loadOldCuttingOrder(cuttingService.getDateWiseCuttingOrder(LocalDate.now()));
		} catch (Exception e) {
			notification.showErrorMessage("Error"+e.getMessage());
			e.printStackTrace();
		}
    }

    @FXML
    void btnUpdate(ActionEvent event) {
    	try {
    		CuttingTransactionPojo clp = table.getSelectionModel().getSelectedItem();
    		if(clp==null) return;
    		txtItemName.setText(clp.getItemName());
    		txtQuantity.setText(""+clp.getQuantity());
    		labourNameList.clear();
    		labourNameList.addAll(clp.getLabourList());
    		System.out.println(clp);
    		System.out.println(clp.getLabourList().size());
    		System.out.println(cuttingTransactionList.get(0));
    		for(CuttingLabourPojo c:clp.getLabourList())
    		{
    			System.out.println(c);
    		}
    		
		} catch (Exception e) {
			notification.showErrorMessage("Error in Update "+e.getMessage());
		}
    }

    @FXML
    void btnUpdate2Action(ActionEvent event) {
    	try {
    		CuttingOrderPojo cop = tableOldOrder.getSelectionModel().getSelectedItem();
    		if(cop==null)
    		{
    			return;
    		}
    		CuttingOrder co = cuttingService.getCuttingOrderById(cop.getId());
    		txtOrderId.setText(""+co.getId());
    		date.setValue(co.getDate());
    		txtCustomerName.setText(co.getCustomer().getFname()+" "+co.getCustomer().getMname()+" "+co.getCustomer().getLname());
			cmbSalemanName.setValue(co.getEmployee().getFname()+" "+co.getEmployee().getMname()+" "+co.getEmployee().getLname());
			
			List<CuttingTransactionPojo> trList = new ArrayList<CuttingTransactionPojo>();
			CuttingTransactionPojo ctp=null;
			CuttingLabourPojo clp=null;
			List<CuttingLabourPojo>clpList=new ArrayList<CuttingLabourPojo>();
			for(CuttingTransaction ctr:co.getTransaction())
			{
				
				clpList=new ArrayList<CuttingLabourPojo>();
				for(CuttingLabour cl:ctr.getLabourList())
				{
					clp = new CuttingLabourPojo(
							cl.getLabour().getFname()+" "+cl.getLabour().getMname()+" "+cl.getLabour().getLname(),
							cl.getQuantity());
					clp.setId(clpList.size()+1);
					clpList.add(clp);
				}
				System.out.println("Exit");
				ctp = new CuttingTransactionPojo(ctr.getItem().getItemname(),ctr.getQuantity(),clpList);
				ctp.setId(trList.size()+1);
				trList = addInTrList(trList,ctp);		
			}
			cuttingTransactionList.clear();
			cuttingTransactionList.addAll(trList);
			
			
		} catch (Exception e) {
			notification.showErrorMessage("Error in Update!!!"+e.getMessage());
		}
    }
  
    @FXML
    void btnLoadTodayActiion(ActionEvent event) {
    	if(dateSearch.getValue()==null)
    	{
    		return;
    	}
    	loadOldCuttingOrder(cuttingService.getDateWiseCuttingOrder(dateSearch.getValue()));
    	
    }
    @FXML
    void btnLoadAllAction(ActionEvent event) {
    	loadOldCuttingOrder(cuttingService.getAllCuttingOrders());
    }

    private List<CuttingTransactionPojo> addInTrList(List<CuttingTransactionPojo>list,CuttingTransactionPojo ctp)
    {
    	try {
    		ctp.setId(list.size()+1);
			if(list.size()==0)
			{
				
				list.add(ctp);
				return list;
			}
			else
			{
				int flag=-1;
				for(int i=0;i<list.size();i++)
				{
					if(list.get(i).getItemName().equals(ctp.getItemName())&& list.get(i).getQuantity()==ctp.getQuantity())
					{
						//System.out.println("Found");
						flag=i;
						//
						
					}
				}
				if(flag==-1)
				{
					list.add(ctp);
				}
				return list;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    private boolean isNumber(String num) {
		if (num == null) {
			return false;
		}
		try {
			Float.parseFloat(num);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

    private float getLabourAllQty()
    {
    	float qty=0;
    	for(CuttingLabourPojo c:labourNameList)
    	{
    		qty=c.getQty()+qty;
    	}
    	return qty;
    }
    private void clear()
    {
    	txtItemName.setText("");
    	txtQuantity.setText(""+0);
    	txtCuttingQuantity.setText(""+0);
    	cmbLabourName.setValue(null);
    	labourNameList.clear();
    }
    private boolean validateData()
    {
    	try {
			if(date.getValue()==null)
			{
				notification.showErrorMessage("Enter Date!!!");
				date.requestFocus();
				return false;
			}
			if(txtCustomerName.getText().equals(""))
			{
				notification.showErrorMessage("Enter Customer Name!!!");
				txtCustomerName.requestFocus();
				return false;
			}
			if(customerService.getCustomerByName(txtCustomerName.getText())==null)
			{
				notification.showErrorMessage("Enter Corrext Customer Name!!!");
				txtCustomerName.requestFocus();
				return false;
			}
			if(cmbSalemanName.getValue()==null)
			{
				notification.showErrorMessage("Select Salesman Name!!!");
				cmbSalemanName.requestFocus();
				return false;
			}
			if(txtItemName.getText().equals(""))
			{
				notification.showErrorMessage("Enter Item Name!!!");
				txtItemName.requestFocus();
				return false;
			}
			if(itemService.getItemByName(txtItemName.getText())==null)
			{
				notification.showErrorMessage("Enter Correct Item Name!!!");
				txtItemName.requestFocus();
				return false;
			}
			if(txtQuantity.getText().equals("")||!isNumber(txtQuantity.getText()))
			{
				notification.showErrorMessage("Enter Correct Quantity in Digits !!!");
				txtQuantity.requestFocus();
				return false;
			}
			if(labourNameList.size()==0)
			{
				notification.showErrorMessage("Select At Least one labour!!!");
				cmbLabourName.requestFocus();
				return false;
			}
			return true;
		} catch (Exception e) {
			notification.showErrorMessage("Erorr "+e.getMessage());
			return false;
		}
    }
    private boolean validate()
    {
    	try {
			if(cuttingTransactionList.size()==0)
			{
				notification.showErrorMessage("No Order Data to Save!!!");
				txtCustomerName.requestFocus();
				return false;
			}
			
			return true;
		} catch (Exception e) {
			notification.showErrorMessage("Error in Saving Order");
			return false;
		}
    }
    private void addStock(List<CuttingTransaction> list)
    {
    	try {
    		
    		//add stock in conter
    		for(CuttingTransaction ts:list)
			{
    			counterStockDataService.saveCounterStockdata(new CounterStockData(ts.getItem().getItemname(), ts.getQuantity(), ts.getItem().getUnit()));
			}
//    		ItemStock stock=null;
//			for(CuttingTransaction ts:list)
//			{
//				stock = stockService.getItemStockByItemName(ts.getItem().getItemname());
//				if(stock!=null)
//					stock.setQuantity(ts.getQuantity());
//				else
//					stock = new ItemStock(ts.getItem().getItemname(),ts.getItem().getUnit(),ts.getQuantity());
//					
//				stockService.saveItemStock(stock);
//				
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    private void reduceStock(List<CuttingTransaction>list)
    {
    	try {
			for(int i=0;i<list.size();i++)
			{
				list.get(i).setQuantity(
						list.get(i).getQuantity()-
						(list.get(i).getQuantity()*2));
			}
			addStock(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    private void loadOldCuttingOrder(List<CuttingOrder>list)
    {
    	try {
    		oldCutingOrderList.clear();
			for(CuttingOrder co:list)
			{
				String labourName="";
				for(CuttingTransaction tr:co.getTransaction())
				{
					for(CuttingLabour cl:tr.getLabourList())
					{
						labourName =labourName+","+ cl.getLabour().getFname()+" "+cl.getLabour().getMname()+" "+cl.getLabour().getLname()+", ";
					}
				}
				oldCutingOrderList.add(new CuttingOrderPojo(
						co.getId(),
						co.getDate(),
						co.getEmployee().getFname()+" "+co.getEmployee().getMname()+" "+co.getEmployee().getLname(), 
						co.getCustomer().getFname()+" "+co.getCustomer().getMname()+" "+co.getCustomer().getLname(),
						labourName));
			}
		} catch (Exception e) {
			notification.showErrorMessage("Error in Loading Old Cutting Orders !!!"+e.getMessage());
		}
    }
}
