package main.java.main.java.controller.masterReport;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.Commision;
import main.java.main.java.hibernate.service.service.CommisionService;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.serviceImpl.CommisionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SalesmanCommisionReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML
    private HBox hbox;

    @FXML private TextField txtSalesman;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button btnShow;
    @FXML private Button btnShowAll;
    @FXML private Button btnPrint;
    @FXML private Button btnReset;
    @FXML private Button btnHome;
    @FXML private TableView<Commision> table;
    @FXML private TableColumn<Commision, Long> colsrno;
    @FXML private TableColumn<Commision, LocalDate> colDate;
    @FXML private TableColumn<Commision,String> colSalesman;
    @FXML private TableColumn<Commision,Float> colAmount;
    @FXML private TableColumn<Commision,String> colBank;
    @FXML private TextField txtCommission;
    private CommisionService commisionService;
    private AlertNotification alert;
    private ObservableList<Commision>list = FXCollections.observableArrayList();
    private ObservableList<String>names = FXCollections.observableArrayList();
    private EmployeeService employeeService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        commisionService = new CommisionServiceImpl();
        alert  = new AlertNotification();
        employeeService = new EmployeeServiceImpl();
        TextFields.bindAutoCompletion(txtSalesman,employeeService.getAllSalesmanNames());

//        Popup pop = PopupBuilder.create().content(contentNode).width(50).height(100).autoFix(true).build();
//        pop.show(stage);
        colsrno.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSalesman.setCellValueFactory(
                cellData->new SimpleStringProperty(
                        cellData.getValue().getEmployee().getFname()+" "
                        +cellData.getValue().getEmployee().getMname()+" "
                        +cellData.getValue().getEmployee().getLname()
                        )
        );
        colAmount.setCellValueFactory(new PropertyValueFactory<>("paidCommision"));
        colBank.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getBank().getBankname()));
        table.setItems(list);
        btnShow.setOnAction(e->show());

    }

    private void show() {
        if(txtSalesman.getText().isEmpty())
        {
            alert.showErrorMessage("select Salesman Name");
            txtSalesman.requestFocus();
            return;
        }
        if(dateFrom.getValue()==null && dateTo.getValue()==null)//all Commision Paid
        {
            list.clear();
            txtCommission.setText(""+0.0f);
            list.addAll(commisionService.getEmployeeAllCommision(employeeService.getEmployeeByName(txtSalesman.getText()).getId()));
            calculateTotal();
            return;
        }
        if(dateFrom.getValue()!=null && dateTo.getValue()==null)//date wise
        {
            list.clear();
            list.addAll(commisionService.getEmployeeDateWiseCommision(
                    employeeService.getEmployeeByName(txtSalesman.getText()).getId(),
                    dateFrom.getValue()
            ));
            calculateTotal();
            return;
        }
        if(dateFrom.getValue()!=null && dateTo.getValue()!=null)//date period
        {
            list.clear();
            list.addAll(commisionService.getEmployeeDatePeriodCommision(
                    employeeService.getEmployeeByName(txtSalesman.getText()).getId(),
                    dateFrom.getValue(),
                    dateTo.getValue()
            ));
            calculateTotal();
            return;
        }
    }
    private void calculateTotal()
    {
        float total=0.0f;
        int sr=0;
        for(Commision com:list)
        {
            list.get(list.indexOf(com)).setId(++sr);
            total+=com.getPaidCommision();
        }
        txtCommission.setText(String.valueOf(total));
        table.refresh();
    }
}
