package main.java.main.java.controller.masterReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.*;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.CuttingOrderService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CuttingOrderServiceImpl;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class WeekDashboardController implements Initializable {

    @FXML private AreaChart<?, ?> areaChart;
    @FXML private BarChart<?, ?> barChart;
    @FXML private LineChart<?, ?> labourLineChart;
    @FXML private Label lblAmount;
    @FXML private Label lblBills;
    @FXML private Label lblCustomer;
    @FXML private Label lblKg;
    @FXML private Label lblNos;
    @FXML private AnchorPane mainPane;
    @FXML private LineChart<?, ?> salesmanKgLineChart;
    @FXML private LineChart<?, ?> salesmanNosLineChart;
    @FXML private Tab tabLabour;
    @FXML private Tab tabSalesmanKg;
    @FXML private Tab tabSalesmanNos;
    @FXML private TabPane employeeTabPane;
    private LocalDate date;
    private ObservableList<Bill>billList = FXCollections.observableArrayList();
    private BillService billService;
    private Map<LocalDate,Float>saleMmap;
    private Map<String,Float>salesmanMap;
    private XYChart.Series series;
    private XYChart.Series seriesSalesMan;

    private Map<String,Float>salesmanKgMap;
    private Map<String,Float>salesmanNosMap;
    private XYChart.Series seriesKg;
    private XYChart.Series seriesNos;
    private CuttingOrderService cuttingService;
    private Map<String,Float>labourMap;
    private XYChart.Series seriesLabour;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeeTabPane.getSelectionModel().clearSelection();
        lblBills.setText(""+0);
        lblKg.setText(""+0.0f);
        lblAmount.setText(""+0.0f);
        lblCustomer.setText(""+0);
        lblNos.setText(""+0.0f);

        saleMmap = new HashMap<>();
        salesmanMap = new HashMap<>();
        labourMap = new HashMap<>();



        series = new XYChart.Series();
        seriesSalesMan = new XYChart.Series();
        seriesLabour = new XYChart.Series();
        series.setName("Week Sale");
        seriesSalesMan.setName("Salesman Wise Sale");



//        date = LocalDate.now();
        LocalDate date = LocalDate.of(2021,10,23);
        billService = new BillServiceImpl();
        cuttingService = new CuttingOrderServiceImpl();
        billList.addAll(billService.getPeriodWiseBills(date.with(DayOfWeek.MONDAY),date.with(DayOfWeek.SUNDAY)));
        setMainData();
        loadAreaChart();

        tabSalesmanKg.setOnSelectionChanged(e->{
            loadSalesmanSoldKg();
        });
        tabSalesmanNos.setOnSelectionChanged(e->{
            loadSalesmanSoldNos();
        });
        tabLabour.setOnSelectionChanged(e->{
            loadLabourChart(date);
        });

    }
    private void setMainData()
    {
        Set<Integer>salesmanSet = new HashSet<>();
        Set<Integer>customerSet = new HashSet<>();
        float amount = 0.0f,kg=0.0f,nos=0.0f;
        for(Bill bill:billList)
        {
            salesmanSet.add(bill.getEmployee().getId());
            customerSet.add(bill.getCustomer().getId());
            amount=amount+(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs());
            kg+=getKg(bill.getTransaction());
            nos+=getNos(bill.getTransaction());

        }
        lblBills.setText(String.valueOf(billList.size()));
        lblCustomer.setText(String.valueOf(customerSet.size()));
        lblAmount.setText(String.format("%.2f",amount));
        lblKg.setText(String.format("%.2f",kg));
        lblNos.setText(String.format("%.2f",nos));

    }
    private float getKg(List<Transaction> trList)
    {
        float kg = 0.0f;
        for(Transaction tr:trList)
        {
            if(tr.getUnit().equalsIgnoreCase("KG"))
                kg+=tr.getQuantity();
        }
        return kg;
    }
    private float getNos(List<Transaction> trList)
    {
        float nos = 0.0f;
        for(Transaction tr:trList)
        {
            if(tr.getUnit().equalsIgnoreCase("Nos"))
                nos+=tr.getQuantity();
        }
        return nos;
    }
    void loadAreaChart()
    {
        saleMmap.clear();
        salesmanMap.clear();
        for(Bill bill:billList)
        {
         //   series.getData().add(new XYChart.Data<>(""+bill.getDate(),(bill.getNettotal()+bill.getOtherchargs()+bill.getTransportingchrges())));
        loadSalemap(bill.getDate(),(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs()));
            loadSalemanMap(bill);
        }
        for(Map.Entry<LocalDate,Float>entry:saleMmap.entrySet())
        {
            series.getData().add(new XYChart.Data<>(""+entry.getKey()+"("+entry.getValue()+")",entry.getValue()));
        }
        for(Map.Entry<String,Float>entry:salesmanMap.entrySet())
        {
            seriesSalesMan.getData().add(new XYChart.Data<>(entry.getKey()+"("+entry.getValue()+")",entry.getValue()));
        }
        areaChart.getData().clear();
        barChart.getData().clear();
        areaChart.getData().addAll(series);
        barChart.getData().addAll(seriesSalesMan);
    }
    void loadSalemap(LocalDate date,float amount)
    {
        if(saleMmap.isEmpty()){
            saleMmap.put(date,amount);
        }
        else if(saleMmap.containsKey(date)) {
            saleMmap.put(date,saleMmap.get(date)+amount);
        }
        else{
            saleMmap.put(date,amount);
        }
    }
    void loadSalemanMap(Bill bill)
    {
        if(salesmanMap.isEmpty())
        {
            salesmanMap.put(bill.getEmployee().getFname(),(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs()));
        }
        else if(salesmanMap.containsKey(bill.getEmployee().getId()))
        {
            salesmanMap.put(bill.getEmployee().getFname(),salesmanMap.get(bill.getEmployee().getId())+(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs()));
        }
        else
        {
            salesmanMap.put(bill.getEmployee().getFname(),(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs()));
        }
    }
    private void loadSalesmanSoldKg() {

        salesmanKgMap = new HashMap<>();
        for(Bill bill:billList)
        {
            addInSalesmanKgMap(bill.getEmployee().getFname(),getKg(bill.getTransaction()));
        }
        seriesKg = new XYChart.Series();
        seriesKg.setName("Salesman Sold KG");

        for(Map.Entry<String,Float>entry:salesmanKgMap.entrySet())
        {
            seriesKg.getData().add(new XYChart.Data<>(entry.getKey()+"("+entry.getValue()+")",entry.getValue()));
        }
        salesmanKgLineChart.getData().clear();
        salesmanKgLineChart.getData().setAll(seriesKg);
    }
    void addInSalesmanKgMap(String fname,float qty)
    {
        if(salesmanKgMap.isEmpty())
        {
            salesmanKgMap.put(fname,qty);
        }
        else if(salesmanKgMap.containsKey(fname))
        {
            salesmanKgMap.put(fname,salesmanKgMap.get(fname)+qty);
        }
        else{
            salesmanKgMap.put(fname,qty);
        }
    }
    private void loadSalesmanSoldNos() {
        salesmanNosMap = new HashMap<>();
        for(Bill bill:billList)
        {
            addInSalesmanNosMap(bill.getEmployee().getFname(),getNos(bill.getTransaction()));
        }
        seriesNos = new XYChart.Series();
        seriesNos.setName("Salesman Sold Nos");
        for(Map.Entry<String,Float>entry:salesmanNosMap.entrySet())
        {
            seriesNos.getData().add(new XYChart.Data<>(entry.getKey()+"("+entry.getValue()+")",entry.getValue()));
        }
        salesmanNosLineChart.getData().clear();
        salesmanNosLineChart.getData().addAll(seriesNos);
    }
    private void addInSalesmanNosMap(String fname, float nos) {
        if(salesmanNosMap.isEmpty())
        {
            salesmanNosMap.put(fname,nos);
        }
        else if(salesmanNosMap.containsKey(fname))
        {
            salesmanNosMap.put(fname,salesmanNosMap.get(fname)+nos);
        }
        else{
            salesmanNosMap.put(fname,nos);
        }
    }
    private void loadLabourChart(LocalDate date)
    {
        List<CuttingOrder>list = cuttingService.getPeriodWiseCuttingOrder(date.with(DayOfWeek.MONDAY),date.with(DayOfWeek.SUNDAY));
        for(CuttingOrder co:list)
        {
            for(CuttingTransaction ct:co.getTransaction())
            {
                for(CuttingLabour cl:ct.getLabourList())
                {
                    addInLabourmap(cl.getLabour().getFname(),cl.getCuttingCharges());
                }
            }
        }
        seriesLabour = new XYChart.Series();
        seriesLabour.setName("Labour Charges");

        for(Map.Entry<String,Float>entry:labourMap.entrySet())
        {
            seriesLabour.getData().add(new XYChart.Data<>(entry.getKey()+"("+entry.getValue()+")",entry.getValue()));
        }
        labourLineChart.getData().clear();
        labourLineChart.getData().setAll(seriesLabour);
    }

    private void addInLabourmap(String fname, float cuttingCharges) {
        if(labourMap.isEmpty())
        {
            labourMap.put(fname,cuttingCharges);
        }
        else if(labourMap.containsKey(fname))
        {
            labourMap.put(fname,labourMap.get(fname)+cuttingCharges);
        }
        else
        {
            labourMap.put(fname,cuttingCharges);
        }
    }

}
