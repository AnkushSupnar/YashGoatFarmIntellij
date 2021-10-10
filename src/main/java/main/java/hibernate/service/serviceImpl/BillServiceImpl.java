package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.BillDao;
import main.java.main.java.hibernate.dao.daoImpl.BillDaoImpl;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.service.service.BillService;

import java.time.LocalDate;
import java.util.List;

public class BillServiceImpl implements BillService {

	private BillDao dao;
	public BillServiceImpl() {
		this.dao = new BillDaoImpl();
	}
	@Override
	public Bill getBillByBillno(long billno) {
		return dao.getBillByBillno(billno);
	}

	@Override
	public List<Bill> getAllBills() {
		return dao.getAllBills();
	}

	@Override
	public List<Bill> getDateWiseBill(LocalDate date) {
		return dao.getDateWiseBill(date);
	}

	@Override
	public List<Bill> getMonthWiseBill(LocalDate date) {
		return dao.getMonthWiseBill(date);
	}

	@Override
	public int saveBill(Bill bill) {
		return dao.saveBill(bill);
	}
	@Override
	public List<Transaction> getBillTransactions(int billno) {
		return dao.getBillTransactions(billno); 
	}
	@Override
	public long getNewBNillNo() {
		return dao.getNewBNillNo();
	}
	@Override
	public void deleteTransactions(long billno) {
		dao.deleteTransactions(billno);
		
	}
	@Override
	public List<Bill> getThisWeekBill() {
		return dao.getThisWeekBill();
	}
	@Override
	public List<Bill> getThisYearBill() {
		return dao.getThisYearBill();
	}
	@Override
	public List<Bill> getUnpaidCommisionBills(int employee) {
		return dao.getUnpaidCommisionBills(employee);
	}
	@Override
	public List<Bill> getUnpaidCommisionBillsPeriodWise(int employee,LocalDate fromDate, LocalDate toDate) {
		return dao.getUnpaidCommisionBillsPeriodWise(employee,fromDate, toDate);
	}
	@Override
	public int updatePaidCommision(List<Bill> list) {
		return dao.updatePaidCommision(list);
	}
	@Override
	public List<Bill> getPeriodWiseBills(LocalDate fromDate, LocalDate toDate) {
		return dao.getPeriodWiseBills(fromDate, toDate);
	}
	@Override
	public List<Bill> getYearWiseBills(int year) {
		return dao.getYearWiseBills(year);
	}
	@Override
	public List<Bill> getUnpaidBills(int customer) {
		return dao.getUnpaidBills(customer);
	}
	@Override
	public int updateReceivedAmount(Bill bill) {
		return dao.updateReceivedAmount(bill);
	}
	@Override
	public List<Bill> getAllUnpaidBills() {
		return dao.getAllUnpaidBills();
	}
	@Override
	public double getCustomerTotalPaidBillAmount(int customerId) {
		return dao.getCustomerTotalPaidBillAmount(customerId);
	}
	@Override
	public double getCustomerTotalBillAmount(int customerId) {
		return dao.getCustomerTotalBillAmount(customerId);
	}
	@Override
	public double getWholeSaleBillAmount(int customerid) {

		return dao.getWholeSaleBillAmount(customerid);
	}
	@Override
	public List<Bill> getDateWiseSalesmanBills(int empid, LocalDate date) {
		return dao.getDateWiseSalesmanBills(empid, date);
	}
	@Override
	public List<Bill> getPeriodWiseSalesmanBills(int empid, LocalDate start, LocalDate end) {
		return dao.getPeriodWiseSalesmanBills(empid, start, end);
	}
	@Override
	public List<Bill> getSalesmanAllBills(int empid) {
		return dao.getSalesmanAllBills(empid);
	}

	@Override
	public List<Bill> getCustomerAndDateWiseBill(int customerId, LocalDate date) {
		return dao.getCustomerAndDateWiseBill(customerId,date);
	}

	@Override
	public List<Bill> getBillsByCustomer(int customerId) {
		return dao.getBillsByCustomer(customerId);
	}

	@Override
	public List<Bill> getBillsByCustomerAndPeriod(int customerId, LocalDate start, LocalDate end) {
		return dao.getBillsByCustomerAndPeriod(customerId,start,end);
	}
}
