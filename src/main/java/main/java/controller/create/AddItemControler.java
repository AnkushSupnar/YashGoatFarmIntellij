package main.java.main.java.controller.create;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.Item;
import main.java.main.java.hibernate.service.service.ItemService;
import main.java.main.java.hibernate.service.serviceImpl.ItemServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.util.ResourceBundle;

public class AddItemControler implements Initializable {

	@FXML
    private AnchorPane mainPane;

	@FXML
    private TextField txtItemName;
	@FXML
	private TextField txtHsnCode;

    @FXML
    private ComboBox<String> cmbUnit;
    @FXML
    private TextField txtRate;
    @FXML
    private TextField txtCommision;
    @FXML
    private TextField txtLabour;
    @FXML
    private Button btnSave;
    @FXML
    private TableView<Item> table;
    @FXML
    private TableColumn<Item, Integer> colSrNo;
    @FXML
    private TableColumn<Item, String> colItemName;
    @FXML
    private TableColumn<Item,String> colHsnCode;
    @FXML
    private TableColumn<Item, String> colUnit;
    @FXML
    private TableColumn<Item, Float> colRate;
    @FXML
    private TableColumn<Item, Float> colCommision;
    @FXML
    private TableColumn<Item, Float> colLabour;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnExit;
    private ItemService service;
    private ObservableList<Item> itemList = FXCollections.observableArrayList();
    private int id;
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		service = new ItemServiceImpl();
		colSrNo.setCellValueFactory(new PropertyValueFactory<Item, Integer>("id"));
		colItemName.setCellValueFactory(new PropertyValueFactory<Item, String>("itemname"));
		colUnit.setCellValueFactory(new PropertyValueFactory<Item, String>("unit"));
		colRate.setCellValueFactory(new PropertyValueFactory<Item, Float>("rate"));
		colCommision.setCellValueFactory(new PropertyValueFactory<Item,Float>("commision"));
		colHsnCode.setCellValueFactory(new PropertyValueFactory<Item,String>("hsn"));
		colLabour.setCellValueFactory(new PropertyValueFactory<Item,Float>("labourCharges"));
		itemList.addAll(service.getAllItems());
		table.setItems(itemList);
		
		cmbUnit.getItems().add("Nos");
		cmbUnit.getItems().add("KG");
	
		txtRate.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue.matches("\\d.*")) return;
		    txtRate.setText(newValue.replaceAll("[^\\d]", ""));
		});
		txtCommision.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue.matches("\\d.*")) return;
		    txtCommision.setText(newValue.replaceAll("[^\\d]", ""));
		});
		txtLabour.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue.matches("\\d.*")) return;
		    txtCommision.setText(newValue.replaceAll("[^\\d]", ""));
		});
		
		id=0;
	}
    @FXML
    void clear(ActionEvent event) {
    	clear();
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
			Item item = new Item(
					txtItemName.getText().trim(),
					txtHsnCode.getText().trim(),
					Float.parseFloat(txtRate.getText().trim()),
					cmbUnit.getValue(),
					Float.parseFloat(txtCommision.getText().trim()),
					Float.parseFloat(txtLabour.getText().trim()));
			item.setId(id);
			int f=0;
			for(Item i:itemList)
			{
				if(i.getItemname().equals(item.getItemname()))
				{
					f=1;
				}
			}
			if(item.getId()==0 && f!=0)
			{
				new Alert(Alert.AlertType.ERROR,"Item Name Already Exist Please Select Another").showAndWait();
				txtItemName.requestFocus();
				return;
			}
			int flag = service.saveItem(item);
			if(flag==1)
			{
				new Alert(Alert.AlertType.INFORMATION,"Item Saved Success").showAndWait();
				clear();
				itemList.add(item);
				
			}
			if(flag==2)
			{
				int index=-1;
				Item oldItem =table.getSelectionModel().getSelectedItem();
				for(int i=0;i<itemList.size();i++)
				{
					if(itemList.get(i).getItemname().equals(oldItem.getItemname()))
					{
						index=i;
						break;
					}
				}
				System.out.println(index);
				itemList.remove(index);
				itemList.add(index, item);
				new Alert(Alert.AlertType.INFORMATION,"Item Updated Success").showAndWait();
				clear();
				
			}
			CommonData.setItemNames();
			
			//CommonData.setItemNames();
		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR,"Error in Saving Item").showAndWait();
			
		}
    }

    @FXML
    void update(ActionEvent event) {
    	Item item = table.getSelectionModel().getSelectedItem();
    	if(item!=null)
    	{
    		id= item.getId();
    		txtItemName.setText(item.getItemname());
    		cmbUnit.setValue(item.getUnit());
    		txtRate.setText(""+item.getRate());
    		txtCommision.setText(""+item.getCommision());
    		txtLabour.setText(""+item.getLabourCharges());
    		if(item.getHsn()==null)
    		txtHsnCode.setText("");
    		else txtHsnCode.setText(item.getHsn());
    	}
    
    	
    }
    private int validateData()
    {
    	try {
    		if(txtItemName.getText().equals("")|| txtItemName.getText()==null)
    		{
    			new Alert(Alert.AlertType.ERROR,"Enter Item Name !!!").showAndWait();
    			txtItemName.requestFocus();
    			return 0;
    		}
    		if(cmbUnit.getValue()==null)
    		{
    			new Alert(Alert.AlertType.ERROR,"Select Item Quantity Unit !!!").showAndWait();
    			cmbUnit.requestFocus();
    			return 0;
    		}
    		if(txtRate.getText().equals("")|| txtRate.getText()==null)
    		{
    			new Alert(Alert.AlertType.ERROR,"Enter Item Rate !!!").showAndWait();
    			txtRate.requestFocus();
    			return 0;
    		}
    		if(txtCommision.getText().equals("")|| txtCommision.getText()==null)
    		{
    			new Alert(Alert.AlertType.ERROR,"Enter Item Salesman Commision in Rupees !!!").showAndWait();
    			txtCommision.requestFocus();
    			return 0;
    		}
    		if(txtLabour.getText().equals("")||txtLabour.getText()==null)
    		{
    			txtLabour.setText(""+0.0);
    		}
    		return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
    private void clear()
    {
    	txtItemName.setText("");
    	cmbUnit.getSelectionModel().clearSelection();
    	txtRate.setText("");
    	txtCommision.setText("");
    	txtHsnCode.setText("");
    	txtLabour.setText("");
    	id=0;
    }
   
}

