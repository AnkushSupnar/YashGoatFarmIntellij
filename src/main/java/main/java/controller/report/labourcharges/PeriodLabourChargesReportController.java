package main.java.main.java.controller.report.labourcharges;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.entities.LabourCharges;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.service.LabourChargesService;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.LabourChargesServiceImpl;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class PeriodLabourChargesReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML private TextField txtLabourName;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button btnShow;
    @FXML private Button btnShowAll;
    @FXML private Button btnReset;
    @FXML private Button btnExit;
    @FXML private TableView<LabourCharges> table;
    @FXML private TableColumn<LabourCharges,Long> colSr;
    @FXML private TableColumn<LabourCharges, LocalDate> colDate;
    @FXML private TableColumn<LabourCharges,String> colLabour;
    @FXML private TableColumn<LabourCharges,Float> colAmount;
    @FXML private TableColumn<LabourCharges,String> colBank;
    @FXML private TextField txtTotal;

    private LabourChargesService labourService;
    private EmployeeService employeeService;
    private AlertNotification notify;
    private final ObservableList<LabourCharges>list= FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colSr.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLabour.setCellValueFactory(
                cellData->
                        new SimpleStringProperty(""+
                                cellData.getValue().getLabour().getFname()+" "+
                                cellData.getValue().getLabour().getMname()+" "+
                                cellData.getValue().getLabour().getLname()));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colBank.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBank().getBankname()));
        table.setItems(list);


        labourService = new LabourChargesServiceImpl();
        employeeService = new EmployeeServiceImpl();
        notify = new AlertNotification();
        TextFields.bindAutoCompletion(txtLabourName,employeeService.getAllEmployeeNames());

        btnShow.setOnAction(e->show());
        btnShowAll.setOnAction(e->showAll());
        btnReset.setOnAction(e->{
            txtLabourName.setText("");
            dateFrom.setValue(null);
            dateTo.setValue(null);
            list.clear();
            txtTotal.setText("");
        });
        btnExit.setOnAction(e->mainPane.setVisible(false));
    }

    private void show() {
        try {
            if (txtLabourName.getText().isEmpty()) {
                notify.showErrorMessage("Select Labour Name");
                txtLabourName.requestFocus();
                return;
            }
            if (dateFrom.getValue() == null) {
                notify.showErrorMessage("Select Date");
                dateFrom.requestFocus();
                return;
            }
            if (dateTo.getValue() == null) {
                notify.showErrorMessage("Select Date");
                dateTo.requestFocus();
                return;
            }
            if(dateFrom.getValue().compareTo(dateTo.getValue())>0)
            {
                notify.showErrorMessage("Start date must be smaller than end date");
                dateTo.requestFocus();
                return;
            }
            list.clear();
            txtTotal.setText("");
            Employee labour = employeeService.getEmployeeByName(txtLabourName.getText());
            if(labour==null)
            {
                notify.showErrorMessage("Labour Not found Please Select Again");
                txtLabourName.requestFocus();
                return;
            }
            List<LabourCharges> labourList =labourService.getPeriodWiseLabourCharges(
                    dateFrom.getValue(),
                    dateTo.getValue()
            );
            int sr=0;
            float amount=0;
            for(LabourCharges lc:labourList)
            {
                if(lc.getLabour().getId()==labour.getId())
                {
                    lc.setId(++sr);
                    amount+=lc.getAmount();
                    list.add(lc);
                }
            }
            txtTotal.setText(""+amount);

        }catch(Exception e)
        {
            notify.showErrorMessage("Error in loading Data "+e.getMessage());
        }

    }

    private void showAll() {
        try {
            if (dateFrom.getValue() == null) {
                notify.showErrorMessage("Select Date");
                dateFrom.requestFocus();
                return;
            }
            if (dateTo.getValue() == null) {
                notify.showErrorMessage("Select Date");
                dateTo.requestFocus();
                return;
            }
            if (dateFrom.getValue() == null) {
                notify.showErrorMessage("Select Date");
                dateFrom.requestFocus();
                return;
            }
            if (dateFrom.getValue().compareTo(dateTo.getValue())>0) {
                notify.showErrorMessage("Start Date must be smaller than to date");
                dateFrom.requestFocus();
                return;
            }
            list.clear();
            txtTotal.setText("");
            list.addAll(labourService.getPeriodWiseLabourCharges(
                    dateFrom.getValue(),
                    dateTo.getValue()
            ));
            NumberFormat formatter = new DecimalFormat("#0.00");

            txtTotal.setText(""+formatter.format(list.stream().filter(o -> o.getAmount() > 10).mapToDouble(o -> o.getAmount()).sum()));
                   //list.stream().filter(o->o.getAmount()>0).mapToDouble(o.getAmount()).sum();
            
        }catch(Exception e)
        {
        notify.showErrorMessage("Error in Loading Data "+e.getMessage());
        e.printStackTrace();
        }
    }

}
