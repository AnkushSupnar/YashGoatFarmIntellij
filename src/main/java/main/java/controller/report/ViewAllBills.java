package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.ViewUtil;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ViewAllBills implements Initializable {
	@FXML
    private AnchorPane mainFrame;

		@FXML
	    private TableView<Bill> table;
	 	
	    @FXML
	    private TableColumn<Bill,Long> colBillNo;
	    @FXML
	    private TableColumn<Bill,LocalDate> colDate;
	    @FXML
	    private TableColumn<Bill,Double> colAmount;
	    @FXML
	    private TableColumn<Bill,Double> colPaidAmount;
	    @FXML
	    private TableColumn<Bill,Double> colRemaining;
	    @FXML
	    private Button btnPreview;
	    @FXML
	    private Button btnExit;
	    private ObservableList<Bill>list = FXCollections.observableArrayList();
	    private BillService billService;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		billService = new BillServiceImpl();
		
		colBillNo.setCellValueFactory(new PropertyValueFactory<>("billno"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colAmount.setCellValueFactory(new PropertyValueFactory<>("nettotal"));
		colPaidAmount.setCellValueFactory(new PropertyValueFactory<>("recivedamount"));
		colRemaining.setCellValueFactory(new PropertyValueFactory<>("otherchargs"));
		list.addAll(billService.getAllBills());
		for(int i=0;i<list.size();i++)
		{
			list.get(i).setNettotal(
					list.get(i).getNettotal()+
					list.get(i).getOtherchargs()+
					list.get(i).getTransportingchrges());
			list.get(i).setOtherchargs(list.get(i).getNettotal()-list.get(i).getRecivedamount());
		}
		table.setItems(list);
	}
	 @FXML
	    void btnExitAction(ActionEvent event) {
		 mainFrame.setVisible(false);
	    }

	    @FXML
	    void btnPreviewAction(ActionEvent event) {
	    	if(table.getSelectionModel().getSelectedItem()==null)
	    	{
	    		return;
	    	}
	    	Bill bill =table.getSelectionModel().getSelectedItem();
	    	if(bill==null)
	    	{
	    		return;
	    	}
	    	CommonData.previewBillNo= bill.getBillno();
	    	new ViewUtil().showBillPreview(event);
	    	
	    }

}
