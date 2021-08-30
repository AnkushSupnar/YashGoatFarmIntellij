package main.java.main.java.controller.create;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import main.java.main.java.guiUtil.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateMenuControler implements Initializable {

	 @FXML
     private AnchorPane transactionMenuPanel;
     @FXML private AnchorPane createMenuPanel;
    
    @FXML private Button btnAddEmployee;
    @FXML private Button btnEditEmployee;
    @FXML private Button btnViewEmployee;
    @FXML private Button btnAddUser;
    @FXML private Button btnEditUser;
    @FXML private Button btnViewUser;
    @FXML private Button btnAddCustomer;
    @FXML private Button btnEditCustomer;
    @FXML private Button btnViewCustomer;
    @FXML private Button btnAddItem;
    @FXML private Button btnEditItem;
    @FXML private Button btnVIewItem;
    @FXML private Button btnAddParty;
    @FXML private Button btnEditParty;
    @FXML private Button btnViewParty;
    @FXML private Button btnAddBank;
    @FXML private Button btnEditBank;
    @FXML private Button btnViewBank;
    private BorderPane pane;
    private ViewUtil viewUtil;
    Pane centerPane;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		viewUtil = new ViewUtil();







	    
	    //PaneviewBank = 
	    //PaneviewCustomer,
	    //PaneviewEmployee,
	    //PaneviewItem,
	    //PaneviewParty,
	    //PaneviewUser;
	    
	}
	@FXML
    void AddUser(ActionEvent event) {
		System.out.println(createMenuPanel.getParent());
        centerPane = viewUtil.getPage("create/CreateUserFrame");
		pane =(BorderPane) createMenuPanel.getParent();
		pane.setCenter(centerPane);
        centerPane.setVisible(true);
    }
   

    @FXML
    void addBank(ActionEvent event) {
        centerPane = viewUtil.getPage("create/AddBankFrame");
    	pane =(BorderPane) createMenuPanel.getParent();
		pane.setCenter(centerPane);

        centerPane.setVisible(true);
    }

    @FXML
    void addCustomer(ActionEvent event) {
        centerPane = viewUtil.getPage("create/AddCustomerFrame");
    	pane =(BorderPane) createMenuPanel.getParent();
		pane.setCenter(centerPane);

        centerPane.setVisible(true);
    }

    @FXML
    void addEmployee(ActionEvent event) {
        centerPane =  viewUtil.getPage("create/AddEmployeeFrame");
    	pane =(BorderPane) createMenuPanel.getParent();
		pane.setCenter(centerPane);
        centerPane.setVisible(true);
    }

    @FXML
    void addItem(ActionEvent event) {
        centerPane = viewUtil.getPage("create/AddItemFrame");
    	pane =(BorderPane) createMenuPanel.getParent();
		pane.setCenter(centerPane);
        centerPane.setVisible(true);
    }
    @FXML
    void addParty(ActionEvent event) {
        centerPane = viewUtil.getPage("create/AddPurchasePartyFrame");
    	pane =(BorderPane) createMenuPanel.getParent();
		pane.setCenter(centerPane);
        centerPane.setVisible(true);
    }

    @FXML
    void viewBank(ActionEvent event) {

    }

    @FXML
    void viewCustomer(ActionEvent event) {

    }

    @FXML
    void viewEmployee(ActionEvent event) {
        centerPane = viewUtil.getPage("report/ViewAllEmployee");
    	pane =(BorderPane) createMenuPanel.getParent();
    	
    	pane.setCenter(centerPane);
        centerPane.setVisible(true);
    }

    @FXML
    void viewItem(ActionEvent event) {
        centerPane = viewUtil.getPage("report/ViewAllItems");
    	pane =(BorderPane) createMenuPanel.getParent();
    	pane.setCenter(centerPane);
        centerPane.setVisible(true);
    }

    @FXML
    void viewParty(ActionEvent event) {

    }

    @FXML
    void viewUser(ActionEvent event) {

    }
    
}
