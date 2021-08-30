package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.SalesmanCuttingChargesDao;
import main.java.main.java.hibernate.entities.SalesmanCuttingCharges;
import main.java.main.java.hibernate.entities.SalesmanCuttingTransaction;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class SalemanCuttingChargesDaoImpl implements SalesmanCuttingChargesDao {

	@Override
	public SalesmanCuttingCharges getSalesmanCuttingChargesById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			SalesmanCuttingCharges charges = session.get(SalesmanCuttingCharges.class, id);
			return charges;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SalesmanCuttingCharges> getSalesmanCuttingChargesBySalesman(String salesman) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from SalesmanCuttingCharges where saleman=:name";
			List<SalesmanCuttingCharges> list = session.createQuery(hql,SalesmanCuttingCharges.class).
					setParameter("name",salesman).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SalesmanCuttingCharges> getAllSalesmanCuttingCharges() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from SalesmanCuttingCharges";
			List<SalesmanCuttingCharges> list = session.createQuery(hql,SalesmanCuttingCharges.class).list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SalesmanCuttingCharges> getPeriodSalesmanCuttingCharges(LocalDate start, LocalDate end) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from SalesmanCuttingCharges where date>=:d1 and date<=:d2";
			List<SalesmanCuttingCharges> list = session.createQuery(hql,SalesmanCuttingCharges.class).
					setParameter("d1", start).setParameter("d2",end).list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveSalesmanCuttingCharges(SalesmanCuttingCharges salemanCuttingCharges) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(salemanCuttingCharges.getId()==0)
			{
				session.save(salemanCuttingCharges);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				deleteSalesmanCuttingChargesTransaction(salemanCuttingCharges.getId());
				session.update(salemanCuttingCharges);
				session.getTransaction().commit();
				return 2;
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void deleteSalesmanCuttingChargesTransaction(long id) {
		try(Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			SalesmanCuttingCharges charges =getSalesmanCuttingChargesById(id); 
			if(charges!=null)
			{
				for(SalesmanCuttingTransaction tr:charges.getTransaction())
				{
					session.delete(tr);
				}
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<SalesmanCuttingCharges> getDateWiseCharges(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from SalesmanCuttingCharges where date=:d1 ";
			List<SalesmanCuttingCharges> list = session.createQuery(hql,SalesmanCuttingCharges.class).
					setParameter("d1", date).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int checkPaidCuttingCharges(long orderId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from SalesmanCuttingTransaction where orderId=:i";
			SalesmanCuttingTransaction tr=null;
			try {
				tr = session.createQuery(hql,SalesmanCuttingTransaction.class).
						setParameter("i", orderId).getSingleResult();
				if(tr!=null)
					return 1;
				else 
					return 0;
			} catch (Exception e) {
				return 0;
			}
			
		} catch (Exception e) {
			return 0;
		}
	}

}
