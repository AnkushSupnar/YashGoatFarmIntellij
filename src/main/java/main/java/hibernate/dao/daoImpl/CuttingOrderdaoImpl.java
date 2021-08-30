package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.CuttingOrderDao;
import main.java.main.java.hibernate.entities.*;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class CuttingOrderdaoImpl implements CuttingOrderDao {
	
	@Override
	public CuttingOrder getCuttingOrderById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			CuttingOrder order = session.get(CuttingOrder.class, id);
			return order;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CuttingOrder> getAllCuttingOrders() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CuttingOrder";
			List<CuttingOrder> orderList = session.createQuery(hql,CuttingOrder.class).list();
			return orderList;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CuttingOrder> getSalesmanWiseCuttingOrder(Employee employee, LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CuttingOrder where employeeId=:eid and date=:d";
			List<CuttingOrder> orderList = session.createQuery(hql,CuttingOrder.class).
					setParameter("eid", employee.getId()).
					setParameter("d", date).list();
			return orderList;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<CuttingOrder> getLabourWiseCuttingOrder(Employee employee, LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CuttingOrder where labourId=:lid and date=:d";
			List<CuttingOrder> orderList = session.createQuery(hql,CuttingOrder.class).
					setParameter("lid", employee.getId()).
					setParameter("d", date).list();
			return orderList;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	public List<CuttingOrder> getCustomerWiseCuttingOrder(Customer customer, LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CuttingOrder where customerId=:cid and date=:d";
			List<CuttingOrder> orderList = session.createQuery(hql,CuttingOrder.class).
					setParameter("cid", customer.getId()).
					setParameter("d", date).list();
			return orderList;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CuttingOrder> getDateWiseCuttingOrder(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CuttingOrder where date=:d";
			List<CuttingOrder> orderList = session.createQuery(hql,CuttingOrder.class).
					setParameter("d", date).list();
			return orderList;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveCuttingOrder(CuttingOrder order) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(order.getId()==0)
			{
				session.save(order);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				deleteOrderTransaction(order.getId());
				session.update(order);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void deleteOrderTransaction(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			CuttingOrder order = getCuttingOrderById(id);
			if(order!=null)
			{
				for(CuttingTransaction tr:order.getTransaction())
				{
					session.delete(tr);
				}
			}
				session.getTransaction().commit();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public long getNewCuttingOrderId() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "select MAX(id) from CuttingOrder";
			if(session.createQuery(hql).uniqueResult()==null)
			{
				return 1;
			}
			long billno = (long) session.createQuery(hql).uniqueResult();
			return ++billno;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("////////error //////"+e.getMessage());
			return 0;
		}
	}

	@Override
	public List<CuttingOrder> getPeriodWiseCuttingOrder(LocalDate startDate, LocalDate dateEnd) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from CuttingOrder where date>=:d1 and date<=:d2";
			List<CuttingOrder>list = session.createQuery(hql,CuttingOrder.class).
					setParameter("d1",startDate).
					setParameter("d2",dateEnd).list();
					return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int updatePaidLabourCharges(long id, int empId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			CuttingOrder order = getCuttingOrderById(id);
			String hql = "update CuttingLabour set paidCuttingCharges=:amt where id=:i and labourId=:lid";
			for (CuttingTransaction tr : order.getTransaction()) {
                for (CuttingLabour cl : tr.getLabourList()) {
                    @SuppressWarnings({ "rawtypes" })
					Query query = session.createQuery(hql).
					setParameter("amt", cl.getCuttingCharges()).
					setParameter("i", cl.getId()).
					setParameter("lid",empId);
                    query.executeUpdate();
                }
			}
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<CuttingOrder> getSalesmanPeriodCuttingOrders(int empid, LocalDate p1, LocalDate p2) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from CuttingOrder where employeeId=:e and date>=:d1 and date<=:d2";
			return session.createQuery(hql,CuttingOrder.class).
					setParameter("e", empid).
					setParameter("d1", p1).
					setParameter("d2", p2).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
 
	
}
