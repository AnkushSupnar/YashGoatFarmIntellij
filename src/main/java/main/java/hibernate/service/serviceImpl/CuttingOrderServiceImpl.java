package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.CuttingOrderDao;
import main.java.main.java.hibernate.dao.daoImpl.CuttingOrderdaoImpl;
import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.entities.CuttingOrder;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.service.service.CuttingOrderService;

import java.time.LocalDate;
import java.util.List;

public class CuttingOrderServiceImpl implements CuttingOrderService
{
    private CuttingOrderDao dao;
    
    public CuttingOrderServiceImpl() {
        this.dao = (CuttingOrderDao)new CuttingOrderdaoImpl();
    }
    
    public CuttingOrder getCuttingOrderById(final long id) {
        return this.dao.getCuttingOrderById(id);
    }
    
    public List<CuttingOrder> getAllCuttingOrders() {
        return (List<CuttingOrder>)this.dao.getAllCuttingOrders();
    }
    
    public List<CuttingOrder> getSalesmanWiseCuttingOrder(final Employee employee, final LocalDate date) {
        return (List<CuttingOrder>)this.dao.getSalesmanWiseCuttingOrder(employee, date);
    }
    
    public List<CuttingOrder> getCustomerWiseCuttingOrder(final Customer customer, final LocalDate date) {
        return (List<CuttingOrder>)this.dao.getCustomerWiseCuttingOrder(customer, date);
    }
    
    public List<CuttingOrder> getDateWiseCuttingOrder(final LocalDate date) {
        return (List<CuttingOrder>)this.dao.getDateWiseCuttingOrder(date);
    }
    
    public int saveCuttingOrder(final CuttingOrder order) {
        return this.dao.saveCuttingOrder(order);
    }
    
    public void deleteOrderTransaction(final long id) {
    }
    
    public List<CuttingOrder> getLabourWiseCuttingOrder(final Employee employee, final LocalDate date) {
        return (List<CuttingOrder>)this.dao.getLabourWiseCuttingOrder(employee, date);
    }
    
    public long getNewCuttingOrderId() {
        return this.dao.getNewCuttingOrderId();
    }
    
    public List<CuttingOrder> getPeriodWiseCuttingOrder(final LocalDate startDate, final LocalDate dateEnd) {
        return (List<CuttingOrder>)this.dao.getPeriodWiseCuttingOrder(startDate, dateEnd);
    }
    
    public int updatePaidLabourCharges(final long id, final int empId) {
        return this.dao.updatePaidLabourCharges(id, empId);
    }
    
    public List<CuttingOrder> getSalesmanPeriodCuttingOrders(final int empId, final LocalDate start, final LocalDate end) {
        return (List<CuttingOrder>)this.dao.getSalesmanPeriodCuttingOrders(empId, start, end);
    }
}

