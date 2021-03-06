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
import main.java.main.java.print.PrintFile;
import main.java.main.java.print.PrintLabourWeeklyCharges;
import main.java.main.java.print.PrintWeeklyAllLabourReport;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

public class WeeklyLabourChargesReportController implements Initializable {
    @FXML private AnchorPane mainPane;
    @FXML private TextField txtLabourName;
    @FXML private DatePicker date;
    @FXML private Button btnShow;
    @FXML private Button btnShowAll;
    @FXML private Button btnReset;
    @FXML private Button btnExit;
    @FXML private Button btnPrint;
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
        date.setValue(LocalDate.now());
        btnShow.setOnAction(e->show());
        btnShowAll.setOnAction(e->showAll());
        btnReset.setOnAction(e->{
            txtLabourName.setText("");
            date.setValue(LocalDate.now());
            list.clear();
        });
        btnExit.setOnAction(e->mainPane.setVisible(false));
        btnPrint.setOnAction(e->{
            if(list.isEmpty())
                notify.showErrorMessage("No Data To Print");
            else
            {
                System.out.println(txtLabourName.getText());
                if(txtLabourName.getText().isEmpty()||txtLabourName.getText().equals(""))
                {
                    new PrintWeeklyAllLabourReport(date.getValue().with(previousOrSame(MONDAY)),
                            date.getValue().with(nextOrSame(SUNDAY)));
                    new PrintFile().openFile("D:\\Software\\Prints\\WeeklyAllLabouCharges.pdf");
                }
                else{
                    new PrintLabourWeeklyCharges(
                            employeeService.getEmployeeByName(txtLabourName.getText()).getId(),
                            date.getValue().with(previousOrSame(MONDAY)),
                            date.getValue().with(nextOrSame(SUNDAY))
                    );
                    new PrintFile().openFile("D:\\Software\\Prints\\WeeklyLabouCharges.pdf");
                }
            }

        });
    }

    private void show() {
        try {
            if (txtLabourName.getText().isEmpty()) {
                notify.showErrorMessage("Select Labour Name");
                txtLabourName.requestFocus();
                return;
            }
            if (date.getValue() == null) {
                notify.showErrorMessage("Select Date");
                date.requestFocus();
                return;
            }
            list.clear();

            Employee labour = employeeService.getEmployeeByName(txtLabourName.getText());
            if(labour==null)
            {
                notify.showErrorMessage("Labour Not found Please Select Again");
                txtLabourName.requestFocus();
                return;
            }
            List<LabourCharges> labourList =labourService.getPeriodWiseLabourCharges(
                    date.getValue().with(previousOrSame(MONDAY)),
                    date.getValue().with(nextOrSame(SUNDAY))
            );
            int sr=0;
            float total=0;
            for(LabourCharges lc:labourList)
            {
                if(lc.getLabour().getId()==labour.getId())
                {

                    lc.setId(++sr);
                    total+=lc.getAmount();
                    list.add(lc);
                }
            }
            txtTotal.setText(""+total);

        }catch(Exception e)
        {
            notify.showErrorMessage("Error in loading Data "+e.getMessage());
        }

    }

    private void showAll() {
        try {
            if (date.getValue() == null) {
                notify.showErrorMessage("Select Date");
                date.requestFocus();
                return;
            }
            list.clear();
            txtTotal.setText("");
            list.addAll(labourService.getPeriodWiseLabourCharges(
                    date.getValue().with(previousOrSame(MONDAY)),
                    date.getValue().with(nextOrSame(SUNDAY))
            ));
            float total=0;
            for(LabourCharges lc:list)
            {
                total +=lc.getAmount();
            }
            txtTotal.setText(String.valueOf(total));
            
        }catch(Exception e)
        {
        notify.showErrorMessage("Error in Loading Data "+e.getMessage());
        }
    }

}
