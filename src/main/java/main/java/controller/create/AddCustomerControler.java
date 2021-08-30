package main.java.main.java.controller.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.service.service.CustomerService;
import main.java.main.java.hibernate.service.serviceImpl.CustomerServiceImpl;
import main.java.main.java.hibernate.util.GetBackup;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerControler implements Initializable {
	    @FXML
		private AnchorPane mainPanel;
	 	@FXML private TextField txtFname;
	    @FXML private TextField txtMname;
	    @FXML private TextField txtLname;
	    @FXML private TextField txtMobile;
	    @FXML private TextField txtAlterMobile;
	    @FXML private TextField txtEmail;
	    @FXML private TextField txtAddress;
	    @FXML private TextField txtCity;
	    @FXML private TextField txtTaluka;
	    @FXML private TextField txtDistrict;
	    @FXML private TextField txtState;
	    @FXML private TextField txtPin;
	    @FXML private Button btnSave;
	    @FXML private TableView<Customer> table;
	    @FXML private TableColumn<Customer, Integer> colId;
	    @FXML private TableColumn<Customer, String> colName;
	    @FXML private TableColumn<Customer,String> colContact;
	    @FXML private TableColumn<Customer, String> colAddress;
	    @FXML private Button btnClear;
	    @FXML private Button btnUpdate;
	    @FXML private Button btnExit;
	    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
	    private CustomerService service;
	    private int id;
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
	    	service = new CustomerServiceImpl();
	    	id=0;
		colId.setCellValueFactory(new PropertyValueFactory<Customer,Integer>("id"));
		colName.setCellValueFactory(new PropertyValueFactory<Customer,String>("fname"));
		colAddress.setCellValueFactory(new PropertyValueFactory<Customer,String>("address"));
		colContact.setCellValueFactory(new PropertyValueFactory<Customer,String>("mobileno"));
		 
		customerList.addAll(service.getAllCustomer());
		for(Customer c:customerList)
		{
			c.setFname(c.getFname()+" "+c.getMname()+" "+c.getLname());
			c.setAddress(c.getAddress()+" "+c.getCity()+" "+c.getTaluka()+" "+c.getDistrict()+" "+c.getState()+" "+c.getPin());
			c.setMobileno(c.getMobileno()+" / "+c.getAltermobileno());
		}
		table.setItems(customerList);
		}
	    @FXML
	    void clear(ActionEvent event) {
	    	clear();
	    }

	    @FXML
	    void exit(ActionEvent event) {
	    	mainPanel.setVisible(false);
	    }

	    @FXML
	    void save(ActionEvent event) {
	    	try {
	    		if(validateData()!=1)
	    		{
	    			return;
	    		}
				Customer cust = new Customer(
						txtFname.getText().trim(),
						txtMname.getText().trim(),
						txtLname.getText().trim(),
						txtMobile.getText().trim(),
						txtAlterMobile.getText().trim(),
						txtEmail.getText().trim(),
						txtAddress.getText().trim(),
						txtCity.getText().trim(), 
						txtTaluka.getText().trim(),
						txtDistrict.getText().trim(),
						txtState.getText().trim(), 
						Integer.parseInt(txtPin.getText().trim()));
				cust.setId(id);
				int flag = service.saveCustomer(cust);
				cust.setFname(cust.getFname()+" "+cust.getMname()+" "+cust.getLname());
				cust.setAddress(cust.getAddress()+" "+cust.getCity()+" "+cust.getTaluka()+" "+cust.getDistrict()+" "+cust.getState()+" "+cust.getPin());
				cust.setMobileno(cust.getMobileno()+" / "+cust.getAltermobileno());
				if(flag==1)
				{
					new Alert(Alert.AlertType.INFORMATION,"Record Save Success").showAndWait();
					customerList.add(cust);
					new GetBackup("D:\\Software\\Backup\\");
					clear();
				}
				if(flag==2)
				{
					new Alert(Alert.AlertType.INFORMATION,"Record Update Success").showAndWait();
					int index=-1;
					for(int i=0;i<customerList.size();i++)
					{
						if(customerList.get(i).getId()==cust.getId())
						{
							index=i;
							break;
						}
					}
					if(index!=-1)
					{						
						customerList.remove(index);
						customerList.add(index, cust);
					}
					new GetBackup("D:\\Software\\Backup\\");
					clear();
				}
				System.out.println(cust);
			} catch (Exception e) {
				new Alert(Alert.AlertType.ERROR,"Error in Saving Record").showAndWait();
				e.getMessage();
				
			}
	    }
	   
	    @FXML
	    void edit(ActionEvent event) {
	    	Customer c = service.getCustomerbyId(table.getSelectionModel().getSelectedItem().getId());
	    	if(c==null)
	    	{
	    		return;
	    	}
	    	txtFname.setText(c.getFname());
		    txtMname.setText(c.getMname());
		    txtLname.setText(c.getLname());
		    txtMobile.setText(c.getMobileno());
		    txtAlterMobile.setText(c.getAltermobileno());
		    txtEmail.setText(c.getEmail());
		    txtAddress.setText(c.getAddress());
		    txtCity.setText(c.getCity());
		    txtTaluka.setText(c.getTaluka());
		    txtDistrict.setText(c.getDistrict());
		    txtState.setText(c.getState());
		    txtPin.setText(""+c.getPin());
		    id=c.getId();
	    	
	    }
	    private int validateData()
	    {
	    	try {
				if(txtFname.getText().equals("") ||txtFname.getText().equals(null))
				{
					new Alert(Alert.AlertType.ERROR,"Enter First Name !!!").showAndWait();
					txtFname.requestFocus();
					return 0;
				}
				if(txtMname.getText().equals("") ||txtMname.getText().equals(null))
				{
					new Alert(Alert.AlertType.ERROR,"Enter Middle Name !!!").showAndWait();
					txtMname.requestFocus();
					return 0;
				}
				if(txtLname.getText().equals("") ||txtLname.getText().equals(null))
				{
					new Alert(Alert.AlertType.ERROR,"Enter Last Name !!!").showAndWait();
					txtLname.requestFocus();
					return 0;
				}
				if(txtMobile.getText().equals("") ||txtMobile.getText().equals(null))
				{
					new Alert(Alert.AlertType.ERROR,"Enter Mobile No !!!").showAndWait();
					txtMobile.requestFocus();
					return 0;
				}
				if(txtAlterMobile.getText().equals("") ||txtAlterMobile.getText().equals(null))
				{
					
					txtAlterMobile.setText("-");
					
				}
				if(txtEmail.getText().equals("") ||txtEmail.getText().equals(null))
				{
					txtEmail.setText("-");
				}
				if(txtAlterMobile.getText().equals("") ||txtAlterMobile.getText().equals(null))
				{
					new Alert(Alert.AlertType.ERROR,"Enter Alternate Mobile No !!!").showAndWait();
					txtAlterMobile.requestFocus();
					return 0;
				}
				if(txtAddress.getText().equals("") ||txtAddress.getText().equals(null))
				{
					txtAddress.setText("-");
				}
				if(txtCity.getText().equals("") ||txtCity.getText().equals(null))
				{
					txtCity.setText("-");
				}
				if(txtTaluka.getText().equals("") ||txtTaluka.getText().equals(null))
				{
					txtTaluka.setText("-");
				}
				if(txtDistrict.getText().equals("") ||txtDistrict.getText().equals(null))
				{
					txtDistrict.setText("-");
				}
				if(txtState.getText().equals("") ||txtState.getText().equals(null))
				{
					txtState.setText("-");
				}
				if(txtPin.getText().equals("") ||txtPin.getText().equals(null))
				{
					txtPin.setText("0");
				}
				return 1;
			} catch (Exception e) {
				new Alert(Alert.AlertType.ERROR,"Error").showAndWait();
				return 0;
			}
	    }
	    private void clear()
	    {
	    	txtFname.setText("");
		    txtMname.setText("");
		    txtLname.setText("");
		    txtMobile.setText("");
		    txtAlterMobile.setText("");
		    txtEmail.setText("");
		    txtAddress.setText("");
		    txtCity.setText("");
		    txtTaluka.setText("");
		    txtDistrict.setText("");
		    txtState.setText("");
		    txtPin.setText("");
		    id=0;
	    }
	    
	    
}
