package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.PurchasInvoiceDao;
import main.java.main.java.hibernate.entities.PurchaseInvoice;
import main.java.main.java.hibernate.entities.PurchaseTransaction;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PurchaseInvoiceDaoImpl implements PurchasInvoiceDao {

	@Override
	public PurchaseInvoice getPurchaseInvoice(long billno) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			PurchaseInvoice invoice = session.get(PurchaseInvoice.class, billno);
			return invoice;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PurchaseInvoice> getAllPurchaseInvoice() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from PurchaseInvoice";
			List<PurchaseInvoice> list = session.createQuery(hql,PurchaseInvoice.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PurchaseInvoice> getDateWisePurchaseInvoice(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from PurchaseInvoice where date=:d";
			List<PurchaseInvoice> list = session.createQuery(hql,PurchaseInvoice.class).setParameter("d", date).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PurchaseInvoice> getMonthWisePurchaseInvoice(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from PurchaseInvoice where MONTH(date)=MONTH(:d)";
			List<PurchaseInvoice> list = session.createQuery(hql,PurchaseInvoice.class).setParameter("d", date).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PurchaseInvoice> getThisYearPurchaseInvoice() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from PurchaseInvoice where WEEKOFYEAR(date)=WEEKOFYEAR(CURDATE())";
			List<PurchaseInvoice> list = session.createQuery(hql,PurchaseInvoice.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PurchaseInvoice> getThisWeekInvoice() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from PurchaseInvoice where date between DATE_FORMAT(CURDATE(),'%Y-01-01') AND CURDATE()";
			List<PurchaseInvoice> list = session.createQuery(hql,PurchaseInvoice.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int savePurchaseInvoice(PurchaseInvoice purchaseInvoice) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(getPurchaseInvoice(purchaseInvoice.getBillno())==null)
			{
				session.save(purchaseInvoice);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				deleteTransaction(purchaseInvoice.getBillno());
				session.update(purchaseInvoice);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public long getNewInvoiceNo() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			
			String hql = "select MAX(billno) from PurchaseInvoice";
			if(session.createQuery(hql).uniqueResult()==null)
			{
				return 1;
			}
			long billno = (long) session.createQuery(hql).uniqueResult();
			return ++billno;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	@Override
	public void deleteTransaction(long billno) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			PurchaseInvoice invoice = getPurchaseInvoice(billno);
			if(invoice!=null)
			{
				for(PurchaseTransaction tr:invoice.getTransaction())
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
	public List<PurchaseInvoice> getPartyWiseUnpaidPurchaseInvoice(int partyId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from PurchaseInvoice where  partyid=:pid and paid<grandtotal ";
			List<PurchaseInvoice> list = session.createQuery(hql,PurchaseInvoice.class).setParameter("pid", partyId) .list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int updatePaidAmount(long billno, float amount) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "update PurchaseInvoice set paid=paid+:amt where billno=:bno";
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery(hql).setParameter("amt", amount).setParameter("bno",billno);
			query.executeUpdate();
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<PurchaseInvoice> getPurchaseInvoicePartyWise(LocalDate date, int partyid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from PurchaseInvoice where date=:d and partyid=:pid";
			List<PurchaseInvoice> list = session.createQuery(hql,PurchaseInvoice.class)
					.setParameter("d", date)
					.setParameter("pid", partyid)
					.list();
			return list;
		} catch (Exception e) {
			System.out.println("Error from Here");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public double getAllPaidAmountByparty(int partyId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql=" select sum(paid) from PurchaseInvoice where partyid=:partyid";
			return session.createQuery(hql,Double.class).setParameter("partyid",partyId).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public double getPartyUnpaidAmount(int partyId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="select sum(nettotal)-sum(paid) from PurchaseInvoice where partyid=:pid";
			return session.createQuery(hql,Double.class).setParameter("pid",partyId).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
}
