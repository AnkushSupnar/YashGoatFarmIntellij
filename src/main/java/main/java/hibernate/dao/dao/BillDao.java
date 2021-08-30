package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface BillDao {
	public Bill getBillByBillno(long billno);
	public List<Bill>getAllBills();
	public List<Bill>getDateWiseBill(LocalDate date);
	public List<Bill>getPeriodWiseBills(LocalDate fromDate,LocalDate toDate);
	public List<Bill> getMonthWiseBill(LocalDate date);
	public List<Bill> getThisWeekBill();
	public List<Bill> getThisYearBill();
	public List<Transaction>getBillTransactions(int billno);
	public List<Bill>getYearWiseBills(int year);
	public int saveBill(Bill bill);
	public long getNewBNillNo();
	public void deleteTransactions(long billno);
	
	public List<Bill>getUnpaidCommisionBills(int employee);
	public List<Bill>getUnpaidCommisionBillsPeriodWise(int employee,LocalDate fromDate,LocalDate toDate);
	public int updatePaidCommision(List<Bill>list);
	public List<Bill>getUnpaidBills(int customer);
	public int updateReceivedAmount(Bill bill);
	public List<Bill>getAllUnpaidBills();
	double getCustomerTotalPaidBillAmount(int customerId);
	double getCustomerTotalBillAmount(int customerId);
	double getWholeSaleBillAmount(int customerid);
	
	List<Bill>getDateWiseSalesmanBills(int empid,LocalDate date);
	List<Bill>getPeriodWiseSalesmanBills(int empid,LocalDate start,LocalDate end);
	List<Bill>getSalesmanAllBills(int empid);
	List<Bill>getCustomerAndDateWiseBill(int customerId,LocalDate date);
	List<Bill>getBillsByCustomer(int customerId);
	List<Bill>getBillsByCustomerAndPeriod(int customerId,LocalDate start,LocalDate end);

}
