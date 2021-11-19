package main.java.main.java.controller.masterReport;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import main.java.main.java.print.PrintAllSalesmanMasterReport;
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PrintSalesmanMasterReport;
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
    ProgressBar progress;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progress = new ProgressBar();
        progress.setLayoutX(hbox.getLayoutX());
        progress.setLayoutY(hbox.getLayoutY()-20);
        progress.setMaxWidth(500);
        mainPane.getChildren().add(progress);
        progress.setVisible(false);



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
        btnShowAll.setOnAction(e->showAll());
        btnReset.setOnAction(e->reset());
        btnPrint.setOnAction(e->print());

    }

    private void print() {
        if(list.size()==0){
            alert.showErrorMessage("No data to Print");
            return;
        }
        if(txtSalesman.getText().isEmpty()|| txtSalesman.getText().equals(""))//all salesman
        {
            if(dateTo.getValue()==null)dateTo.setValue(dateFrom.getValue());
            new PrintAllSalesmanMasterReport(list,dateFrom.getValue(),dateTo.getValue());
            new PrintFile().openFile( "D:\\Software\\Prints\\AllSalesmanMaster.pdf");
            return;
        }
        if(!txtSalesman.getText().isEmpty() && !txtSalesman.getText().equals(""))
        {
            if(dateTo.getValue()==null)dateTo.setValue(dateFrom.getValue());
            new PrintSalesmanMasterReport(list,dateFrom.getValue(),dateTo.getValue());
            new PrintFile().openFile("D:\\Software\\Prints\\SalesmanMaster.pdf");

        }

    }

    private void reset() {
        list.clear();
        txtCommission.setText(""+0.0f);
        txtSalesman.setText("");
        dateTo.setValue(null);
        dateFrom.setValue(null);
    }

    private void show() {
        if(txtSalesman.getText().isEmpty())
        {
            alert.showErrorMessage("select Salesman Name");
            txtSalesman.requestFocus();
            return;
        }
//        ProgressBar progress = new ProgressBar();
//        progress.setLayoutX(hbox.getLayoutX());
//        progress.setLayoutY(hbox.getLayoutY()-20);
//
//        progress.setMaxWidth(500);
//        mainPane.getChildren().add(progress);
        //table.setVisible(false);
        Task<Void>task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // table.setVisible(false);
                progress.setMaxWidth(hbox.getMaxWidth());
                progress.setVisible(true);
                return null;
            }
        };
        Task<Void>task2 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(dateFrom.getValue()==null && dateTo.getValue()==null)//all Commision Paid
                {
                    list.clear();
                    txtCommission.setText(""+0.0f);
                    list.addAll(commisionService.getEmployeeAllCommision(employeeService.getEmployeeByName(txtSalesman.getText()).getId()));
                    calculateTotal();
                }
                if(dateFrom.getValue()!=null && dateTo.getValue()==null)//date wise
                {
                    list.clear();
                    list.addAll(commisionService.getEmployeeDateWiseCommision(
                        employeeService.getEmployeeByName(txtSalesman.getText()).getId(),
                        dateFrom.getValue()
                    ));
                    calculateTotal();
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
                }
                progress.setVisible(false);
                return null;
            }
        };
        Thread t = new Thread(task);
        Thread t2 = new Thread(task2);
        // t2.isDaemon();
        t.start();
        t2.start();

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
    private void showAll() {
        txtSalesman.setText("");
//        ProgressBar progress = new ProgressBar();
//        progress.setLayoutX(hbox.getLayoutX());
//        progress.setLayoutY(hbox.getLayoutY()-20);
//        progress.setMaxWidth(hbox.getMaxWidth());
//        mainPane.getChildren().add(progress);
        //table.setVisible(false);
        Task<Void>task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
               // table.setVisible(false);
                progress.setVisible(true);
                return null;
            }
        };


        Task<Void>task2 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
              //  table.setVisible(false);
                if(dateFrom.getValue()==null && dateTo.getValue()==null)
                {
                    list.clear();
                    list.addAll(commisionService.getAllCommision());
                    calculateTotal();
                }

                if(dateFrom.getValue()!=null && dateTo.getValue()==null)
                {
                    list.clear();
                    list.addAll(commisionService.getDateWiseCommision(dateFrom.getValue()));
                    calculateTotal();
                }
                if(dateFrom.getValue()!=null && dateTo.getValue()!=null)
                {
                    list.clear();
                    list.addAll(commisionService.getDatePeriodCommision(dateFrom.getValue(),dateTo.getValue()));
                    calculateTotal();
                }
                //table.setVisible(true);
                progress.setVisible(false);
                return null;
            }
        };
        Thread t = new Thread(task);


        Thread t2 = new Thread(task2);

       // t2.isDaemon();
        t.start();
        t2.start();










    }
}
