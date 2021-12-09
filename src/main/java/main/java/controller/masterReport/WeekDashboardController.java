package main.java.main.java.controller.masterReport;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class WeekDashboardController implements Initializable {
    @FXML private AreaChart<?, ?> areaChart;
    @FXML private BarChart<?, ?> barChart;
    @FXML private Label lblAmount;
    @FXML private Label lblBills;
    @FXML private Label lblCustomer;
    @FXML private Label lblKg;
    @FXML private Label lblNos;
    @FXML private AnchorPane mainPane;
    private LocalDate date;
    private ObservableList<Bill>billList = FXCollections.observableArrayList();
    private BillService billService;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblBills.setText(""+0);
        lblKg.setText(""+0.0f);
        lblAmount.setText(""+0.0f);
        lblCustomer.setText(""+0);
        lblNos.setText(""+0.0f);

//        date = LocalDate.now();
        LocalDate date = LocalDate.of(2021,10,23);
        System.out.println("Start Date="+date.with(DayOfWeek.MONDAY)+"\n End Date "+date.with(DayOfWeek.SUNDAY));
        billService = new BillServiceImpl();
        billList.addAll(billService.getPeriodWiseBills(date.with(DayOfWeek.MONDAY),date.with(DayOfWeek.SUNDAY)));
        System.out.println(billList.size());
        setMainData();
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
}
