package main.java.main.java.controller.masterReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.EmployeeService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.EmployeeServiceImpl;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class TodayDashboardController implements Initializable {

    @FXML private Label lblBills;
    @FXML private Label lblAmount;
    @FXML private Label lblCustomer;
    @FXML private Label lblKG;
    @FXML private Label lblNos;

    @FXML private LineChart<?, ?> lineChart;
    @FXML private PieChart pichart;
    ObservableList<PieChart.Data>pichartData = FXCollections.observableArrayList();

    @FXML private LineChart<?, ?> lineChartNos;

    @FXML private LineChart<?, ?> lineChartKg;


    private BillService billService;
    private EmployeeService employeeService;

    private ObservableList<Bill>billList = FXCollections.observableArrayList();
    HashMap<Integer,Float>kgMap = new HashMap<>();
    HashMap<Integer,Float>nosMap = new HashMap<>();
    HashMap<Integer,List<Float>>piMap = new HashMap<>();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        billService = new BillServiceImpl();
        employeeService = new EmployeeServiceImpl();
        lblAmount.setText(""+0.0f);
        lblBills.setText(""+0);
        lblCustomer.setText(""+0);
        lblKG.setText(""+0.0f);
        lblNos.setText(""+0.0f);
        LocalDate date = LocalDate.of(2021,10,19);
        billList.addAll(billService.getDateWiseBill(date));

        loadLineChart();
        loadData();
        loadChart();

    }


    void loadLineChart()
    {
        XYChart.Series series = new XYChart.Series();
        int i=0,customer=0;
        Set<Customer> set = new HashSet<>();
        float amt=0.0f,kg=0.0f,nos=0.0f;
        for(Bill bill:billList)
        {
            kg+=getQty(bill)[0];
            nos+=getQty(bill)[1];
            series.getData().add(
                    new XYChart.Data(
                            ""+bill.getBillno(),
                            (
                                    bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges()
                                    )));

            set.add(bill.getCustomer());
            i++;
            amt+=(bill.getNettotal()+bill.getTransportingchrges()+ bill.getOtherchargs());
        }

        lblAmount.setText(String.valueOf(amt));
        lblBills.setText(String.valueOf(i));
        lblCustomer.setText(String.valueOf(set.size()));
        lblNos.setText(String.valueOf(nos));
        lblKG.setText(String.valueOf(kg));
        lineChart.getData().add(series);




    }
    float[] getQty(Bill bill)
    {
        float qty[] = new float[2];
        float kg=0.0f,nos=0.0f;
        for(Transaction tr:bill.getTransaction())
        {
            if(tr.getUnit().equalsIgnoreCase("kg"))

                kg+=tr.getQuantity();
            else
                nos+=tr.getQuantity();
        }
        qty[0]=kg;
        qty[1]=nos;
        return qty;
    }
    void loadData(){


        for(Bill bill:billList)
        {
           for(Transaction tr:bill.getTransaction())
           {
               addInMap(tr,bill.getEmployee().getId());
           }
        }
    }

    private void addInMap(Transaction tr,int emp) {

        if(tr.getUnit().equalsIgnoreCase("kg"))
        {
            if(kgMap.containsKey(emp))
                kgMap.put(emp,kgMap.get(emp)+tr.getQuantity());
            else
                kgMap.put(emp,tr.getQuantity());
        }
        else{
            if(nosMap.containsKey(emp))
                nosMap.put(emp,nosMap.get(emp)+tr.getQuantity());
            else
                nosMap.put(emp,tr.getQuantity());
        }

    }
    private void loadChart(){
        XYChart.Series salesmanKg = new XYChart.Series();
        XYChart.Series salesmannos = new XYChart.Series();
        for(Map.Entry<Integer,Float>entry:kgMap.entrySet())
        {
            salesmanKg.getData().add(
                    new XYChart.Data<>(
                            employeeService.getEmployeeById(entry.getKey()).getFname(),
                            entry.getValue()
                    )
            );
        }
        for(Map.Entry<Integer,Float>entry:nosMap.entrySet())
        {
            salesmannos.getData().add(
                    new XYChart.Data<>(
                            employeeService.getEmployeeById(entry.getKey()).getFname(),
                            entry.getValue()
                    )
            );
        }

        lineChartKg.getData().add(salesmanKg);
        lineChartNos.getData().add(salesmannos);
    }
}
