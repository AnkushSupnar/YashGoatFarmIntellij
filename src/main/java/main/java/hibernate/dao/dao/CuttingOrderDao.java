package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.Customer;
import main.java.main.java.hibernate.entities.CuttingOrder;
import main.java.main.java.hibernate.entities.Employee;

import java.time.LocalDate;
import java.util.List;

public interface CuttingOrderDao
{
    CuttingOrder getCuttingOrderById(final long p0);
    
    List<CuttingOrder> getAllCuttingOrders();
    
    List<CuttingOrder> getSalesmanWiseCuttingOrder(final Employee p0, final LocalDate p1);
    
    List<CuttingOrder> getLabourWiseCuttingOrder(final Employee p0, final LocalDate p1);
    
    List<CuttingOrder> getCustomerWiseCuttingOrder(final Customer p0, final LocalDate p1);
    
    List<CuttingOrder> getDateWiseCuttingOrder(final LocalDate p0);
    
    int saveCuttingOrder(final CuttingOrder p0);
    
    void deleteOrderTransaction(final long p0);
    
    long getNewCuttingOrderId();
    
    List<CuttingOrder> getPeriodWiseCuttingOrder(final LocalDate p0, final LocalDate p1);
    
    int updatePaidLabourCharges(final long p0, final int p1);
    
    List<CuttingOrder> getSalesmanPeriodCuttingOrders(final int p0, final LocalDate p1, final LocalDate p2);
}