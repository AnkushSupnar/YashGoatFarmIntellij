package main.java.main.java.controller.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.PurchaseParty;
import main.java.main.java.hibernate.service.service.PurchasePartyService;
import main.java.main.java.hibernate.service.serviceImpl.PurchasePartyServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPartyControler implements Initializable {

	@FXML
	private AnchorPane mainPane;
	@FXML private TextField txtPartyName;
	@FXML private TextArea txtAddress;
    @FXML private TextField txtPanno;
    @FXML private TextField txtGst;
    @FXML private Button btnAdd;
    @FXML private Button btnClear;
    @FXML private Button btnEdit;
    @FXML private Button btnExit;
    @FXML private TableView<PurchaseParty> table;
    @FXML private TableColumn<PurchaseParty,Integer> colSrNo;
    @FXML private TableColumn<PurchaseParty,String> colName;
    @FXML private TableColumn<PurchaseParty,String> colAddress;
    private ObservableList<PurchaseParty> partyList = FXCollections.observableArrayList();
    private PurchasePartyService service;
    private int id;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		colSrNo.setCellValueFactory(new PropertyValueFactory<PurchaseParty,Integer>("id"));
		colName.setCellValueFactory(new PropertyValueFactory<PurchaseParty,String>("name"));
		colAddress.setCellValueFactory(new PropertyValueFactory<PurchaseParty,String>("address"));
		service = new PurchasePartyServiceImpl();
		partyList.addAll(service.getAllPurchaseParty());
		table.setItems(partyList);
		id=0;
	}

    @FXML
    void clear(ActionEvent event) {
    	clear();
    }

    @FXML
    void edit(ActionEvent event) {
    	
    	PurchaseParty party = service.getPurchasePartyById(table.getSelectionModel().getSelectedItem().getId());
    	if(party!=null)
    	{
    		txtAddress.setText(party.getAddress());
        	txtPartyName.setText(party.getName());
        	txtGst.setText(party.getGst());
        	txtPanno.setText(party.getPan());
        	id=party.getId();
    	}
    	
    }

    @FXML
    void exit(ActionEvent event) {
    	mainPane.setVisible(false);
    }

    @FXML
    void save(ActionEvent event) {
    	try {
			if(validateData()!=1)
			{
				return;
			}
			PurchaseParty party = new PurchaseParty(txtPartyName.getText().trim(), 
					txtAddress.getText().trim(), txtGst.getText().trim(), txtPanno.getText().trim());
			party.setId(id);
			int flag = service.savePurchaseParty(party);
			if(flag==1)
			{
				new Alert(Alert.AlertType.INFORMATION,"Record Save Success ").showAndWait();
				partyList.add(party);
				clear();
			}
			if(flag==2)
			{
				new Alert(Alert.AlertType.INFORMATION,"Record Update Success ").showAndWait();
				int index=-1;
				for(int i=0;i<partyList.size();i++)
				{
					if(partyList.get(i).getId()==party.getId())
					{
						index=i;
						break;
					}
				}
				partyList.remove(index);
				partyList.add(index, party);
				clear();
			}
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR,"Error in Saving Record").showAndWait();
		}
    }
    private int validateData()
    {
    	try {
			if(txtPartyName.getText().equals("")||txtPartyName.getText()==null)
			{
				new Alert(Alert.AlertType.ERROR,"Enter Party Name !!!").showAndWait();
				txtPartyName.requestFocus();
				return 0;
			}
			if(txtAddress.getText().equals("")|| txtAddress.getText()==null)
			{
				new Alert(Alert.AlertType.ERROR,"Enter Part Address!!!").showAndWait();
				txtAddress.requestFocus();
				return 0;
			}
			if(txtPanno.getText().equals("")|| txtPanno.getText()==null)
			{
				txtPanno.setText("-");
			}
			if(txtGst.getText().equals("")||txtGst.getText()==null)
			{
				txtGst.setText("-");
			}
			return 1;
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR,"Error").showAndWait();
			return 0;
		}
    }
    private void clear()
    {
    	txtAddress.setText("");
    	txtPartyName.setText("");
    	txtGst.setText("");
    	txtPanno.setText("");
    	id=0;
    }
}
