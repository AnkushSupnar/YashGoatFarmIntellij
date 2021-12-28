package main.java.main.java.controller.masterReport;

import javafx.application.Platform;
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
import main.java.main.java.hibernate.util.CommonData;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DayWiseDashboardController implements Initializable {

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
    private Map<Long,Float>saleMmap;
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
        series.setName("Date Sale");
        seriesSalesMan.setName("Salesman Wise Sale");
        //LocalDate date = LocalDate.of(2021,10,23);
        date = CommonData.dashboardDate;
        billService = new BillServiceImpl();
        cuttingService = new CuttingOrderServiceImpl();
        //billList.addAll(billService.getPeriodWiseBills(date.with(DayOfWeek.MONDAY),date.with(DayOfWeek.SUNDAY)));

        billList.addAll(billService.getDateWiseBill(date));

        setMainData();
        loadAreaChart();
        loadSalesmanSoldKg();
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
            loadSalemap(bill.getBillno(),(bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs()));
            loadSalemanMap(bill);
        }
        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                for(Map.Entry<Long,Float>entry:saleMmap.entrySet())
                {
                    series.getData().add(new XYChart.Data<>(""+entry.getKey()+"\n("+entry.getValue()+")",entry.getValue()));
                }
                for(Map.Entry<String,Float>entry:salesmanMap.entrySet())
                {
                    seriesSalesMan.getData().add(new XYChart.Data<>(entry.getKey()+"\n("+entry.getValue()+")",entry.getValue()));
                }
            });
        }, 0, 10, TimeUnit.SECONDS);


        areaChart.getData().clear();
        barChart.getData().clear();
        areaChart.getData().addAll(series);
        barChart.getData().addAll(seriesSalesMan);
    }
    void loadSalemap(Long billno,float amount)
    {
        if(saleMmap.isEmpty()){
            saleMmap.put(billno,amount);
        }
        else if(saleMmap.containsKey(billno)) {
            saleMmap.put(billno,saleMmap.get(billno)+amount);
        }
        else{
            saleMmap.put(billno,amount);
        }
    }
    void loadSalemanMap(Bill bill)
    {
        float amt = bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs();
        if(salesmanMap.isEmpty())
        {
            salesmanMap.put(bill.getEmployee().getFname(),amt);

        }
        else if(salesmanMap.containsKey(bill.getEmployee().getFname()))
        {
            salesmanMap.put(
                    bill.getEmployee().getFname(),
                    salesmanMap.get(bill.getEmployee().getFname())+amt);
        }
        else
        {
            salesmanMap.put(bill.getEmployee().getFname(),amt);
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

        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                for(Map.Entry<String,Float>entry:salesmanKgMap.entrySet())
                {
                    seriesKg.getData().add(new XYChart.Data<>(entry.getKey()+"\n("+entry.getValue()+")",entry.getValue()));
                }
            });
        }, 0, 10, TimeUnit.SECONDS);
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
        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                for(Map.Entry<String,Float>entry:salesmanNosMap.entrySet())
                {
                    seriesNos.getData().add(new XYChart.Data<>(entry.getKey()+"\n("+entry.getValue()+")",entry.getValue()));
                }
            });
        }, 0, 10, TimeUnit.SECONDS);

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
        //List<CuttingOrder>list = cuttingService.getPeriodWiseCuttingOrder(date.with(DayOfWeek.MONDAY),date.with(DayOfWeek.SUNDAY));
        List<CuttingOrder>list = cuttingService.getDateWiseCuttingOrder(date);
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

        ScheduledExecutorService scheduledExecutorService;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                for(Map.Entry<String,Float>entry:labourMap.entrySet())
                {
                    seriesLabour.getData().add(new XYChart.Data<>(entry.getKey()+"\n("+entry.getValue()+")",entry.getValue()));
                }
            });
        }, 0, 10, TimeUnit.SECONDS);

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
