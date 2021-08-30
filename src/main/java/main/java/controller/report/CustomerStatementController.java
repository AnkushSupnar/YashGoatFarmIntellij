package main.java.main.java.controller.report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.java.main.java.guiUtil.AlertNotification;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.entities.CustomerAdvancePayment;
import main.java.main.java.hibernate.reportEntity.CustomerStatement;
import main.java.main.java.hibernate.service.service.BankTransactionService;
import main.java.main.java.hibernate.service.service.BillService;
import main.java.main.java.hibernate.service.service.CustomerAdvancePaymentService;
import main.java.main.java.hibernate.service.service.CustomerService;
import main.java.main.java.hibernate.service.serviceImpl.BankTransactionServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.BillServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CustomerAdvancePaymentServiceImpl;
import main.java.main.java.hibernate.service.serviceImpl.CustomerServiceImpl;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.*;

public class CustomerStatementController implements Initializable{
    @FXML private AnchorPane mainPane;
    @FXML private TextField txtName;
    @FXML private DatePicker dateFrom;
    @FXML private DatePicker dateTo;
    @FXML private Button btnShowAll;
    @FXML private Button btnShow;
    @FXML private Button btnWeek;
    @FXML private Button btnMonth;
    @FXML private Button btnYear;
    @FXML private Button btnReset;
    @FXML private Button btnPrint;
    @FXML private TableView<CustomerStatement> table;
    @FXML private TableColumn<CustomerStatement,Integer > colSr;
    @FXML private TableColumn<CustomerStatement, LocalDate> colDate;
    @FXML private TableColumn<CustomerStatement, String> colParticulars;
    @FXML private TableColumn<CustomerStatement, Float> colDebit;
    @FXML private TableColumn<CustomerStatement,Float > colCredit;
    @FXML private TableColumn<CustomerStatement, Float> colBalance;
    @FXML private TextField txtDebit;
    @FXML private TextField txtCredit;
    @FXML private TextField txtBalance;
    private CustomerService customerService;
    private ObservableList<CustomerStatement>list = FXCollections.observableArrayList();
    private AlertNotification notify;
    private BillService billService;
    private BankTransactionService bankTrService;
    private CustomerAdvancePaymentService advanceService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerService = new CustomerServiceImpl();
        notify = new AlertNotification();
        bankTrService = new BankTransactionServiceImpl();
        billService = new BillServiceImpl();
        advanceService = new CustomerAdvancePaymentServiceImpl();
        TextFields.bindAutoCompletion(txtName,customerService.getAllCustomerNames());
        colSr.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        colDebit.setCellValueFactory(new PropertyValueFactory<>("debit"));
        colCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        colBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        table.setItems(list);
        btnShow.setOnAction(e->show(e));
        btnShowAll.setOnAction(e->show(e));
        btnWeek.setOnAction(e->show(e));
        btnMonth.setOnAction(e->show(e));
        btnYear.setOnAction(e->show(e));
        btnReset.setOnAction(e->reset());

    }

    private void show(ActionEvent e) {
        Button button = (Button) e.getSource();
        Customer customer = customerService.getCustomerByName(txtName.getText());
        if(customer==null)
        {
            notify.showErrorMessage("Select Customer Name again");
            txtName.requestFocus();
            return;
        }


        List<CustomerAdvancePayment> advanceList=null;
        List<Bill> billList=null;
        if(button.getId().equals("btnShow"))
        {
            try {
                if(dateFrom.getValue()==null)
                {
                    notify.showErrorMessage("Select From Date");
                    dateFrom.requestFocus();
                    return;
                }
                if (dateFrom.getValue() != null && dateTo.getValue() == null) {
                    //1 getting customer Advance
                    list.clear();
                    txtBalance.setText("");
                    txtCredit.setText("");
                    txtDebit.setText("");

                    advanceList = advanceService.getCustomerAdvanceByDate(dateFrom.getValue());
                    billList = billService.getCustomerAndDateWiseBill(customer.getId(), dateFrom.getValue());
                    addInList(advanceList,billList);
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        if(button.getId().equals("btnShowAll"))
        {
            list.clear();
            txtBalance.setText("");
            txtCredit.setText("");
            txtDebit.setText("");
            advanceList = advanceService.getCustomerAdvanceByCustomer(customer.getId());
            billList = billService.getBillsByCustomer(customer.getId());
            addInList(advanceList,billList);
            sortList();
        }
        if(button.getId().equals("btnWeek"))
        {
            if(dateFrom.getValue()==null)
            {
                notify.showErrorMessage("Select Starting date");
                dateFrom.requestFocus();
                return;
            }
            list.clear();
            txtBalance.setText("");
            txtCredit.setText("");
            txtDebit.setText("");
            advanceList= advanceService.getCustomerAdvanceByCustomerAndDatePeriod(
                    customer.getId(),
                    dateFrom.getValue().with(previousOrSame(MONDAY)),
                    dateFrom.getValue().with(nextOrSame(SUNDAY)));
            billList = billService.getBillsByCustomerAndPeriod(customer.getId(),
                    dateFrom.getValue().with(previousOrSame(MONDAY)),
                    dateFrom.getValue().with(nextOrSame(SUNDAY)));
            addInList(advanceList,billList);
            sortList();
        }
        if(button.getId().equals("btnMonth"))
        {
            if(dateFrom.getValue()==null)
            {
                notify.showErrorMessage("Select Starting date");
                dateFrom.requestFocus();
                return;
            }
            list.clear();
            txtBalance.setText("");
            txtCredit.setText("");
            txtDebit.setText("");
            advanceList= advanceService.getCustomerAdvanceByCustomerAndDatePeriod(
                    customer.getId(),
                    dateFrom.getValue().with(firstDayOfMonth()),
                    dateFrom.getValue().with(lastDayOfMonth()));
            billList = billService.getBillsByCustomerAndPeriod(
                    customer.getId(),
                    dateFrom.getValue().with(firstDayOfMonth()),
                    dateFrom.getValue().with(lastDayOfMonth()));
            addInList(advanceList,billList);
            sortList();
        }
        if(button.getId().equals("btnYear"))
        {
            if(dateFrom.getValue()==null)
            {
                notify.showErrorMessage("Select Starting date");
                dateFrom.requestFocus();
                return;
            }
            list.clear();
            txtBalance.setText("");
            txtCredit.setText("");
            txtDebit.setText("");
            advanceList= advanceService.getCustomerAdvanceByCustomerAndDatePeriod(
                    customer.getId(),
                    dateFrom.getValue().with(firstDayOfYear()),
                    dateFrom.getValue().with(lastDayOfYear()));
            billList = billService.getBillsByCustomerAndPeriod(
                    customer.getId(),
                    dateFrom.getValue().with(firstDayOfYear()),
                    dateFrom.getValue().with(lastDayOfYear()));
            addInList(advanceList,billList);
            sortList();
        }
    }
    private void reset() {
        list.clear();
        txtDebit.setText("");
        txtDebit.setText("");
        txtBalance.setText("");
        dateFrom.setValue(null);
        dateTo.setValue(null);
    }

    private void addInList(List<CustomerAdvancePayment> advanceList,List<Bill> billList)
    {
        float balance = 0;

        for (CustomerAdvancePayment advance : advanceList) {
            balance = balance + advance.getAmount();
            list.add(new CustomerStatement(
                    list.size() + 1,
                    advance.getDate(),
                    "Advance Payment",
                    advance.getAmount(),
                    0,
                    balance));

        }
        List<BankTransaction> bankTrList =  getBankTrList(billList);
        for(BankTransaction tr:bankTrList)
        {
            balance = balance-tr.getCredit();
            balance = balance+tr.getDebit();
            list.add(new CustomerStatement(list.size()+1,tr.getDate(),"Bill Amount",tr.getDebit(),tr.getCredit(),balance ));
            System.out.println("Date in Bank "+tr.getDate());
        }
    }
    private List<BankTransaction>getBankTrList(List<Bill> list)
    {
        List<BankTransaction>trList=null;
        try {
            trList = new ArrayList<>();
            for(Bill bill:list)
            {
                System.out.println("Bill Date="+bill.getDate());
                float amount = bill.getNettotal()+bill.getTransportingchrges()+bill.getOtherchargs();
                float remaining = amount-bill.getRecivedamount();
               BankTransaction tr =  bankTrService.getBankTransactionByPartucular("Add Bill Amount BillNo "+bill.getBillno());
               if(tr!=null) {
                       tr.setDebit(bill.getRecivedamount());
                       tr.setCredit(remaining);
                   }
               else {
                   tr = new BankTransaction();
                   tr.setCredit(remaining);
                   tr.setDebit(bill.getRecivedamount());
                   tr.setBankid(bill.getBank().getId());
                   tr.setDate(bill.getDate());
                   tr.setParticulars("Bill Amount ");
                   tr.setReffid(bill.getBillno());
               }

                   trList.add(tr);
            }
            return trList;
        }catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
    private void sortList()
    {
        Collections.sort(list,(l1,l2)->{
            if(l1.getDate().isBefore(l2.getDate())) return -1;
            else
                return 1;
        });
        float balance=0,credit =0,debit=0;
    int id=0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getParticulars().equalsIgnoreCase("Advance Payment"))
            {
                debit+=list.get(i).getDebit();
                balance = balance+list.get(i).getDebit();
            }
            balance = balance-list.get(i).getCredit();
            list.get(i).setBalance(balance);
            list.get(i).setId(++id);
            credit+=list.get(i).getCredit();
        }
        txtBalance.setText(""+balance);
        txtCredit.setText(""+credit);
        txtDebit.setText(""+debit);
    }
}
