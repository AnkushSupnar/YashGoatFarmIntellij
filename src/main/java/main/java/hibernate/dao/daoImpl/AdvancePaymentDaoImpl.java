package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.AdvancePaymentDao;
import main.java.main.java.hibernate.entities.AdvancePayment;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class AdvancePaymentDaoImpl implements AdvancePaymentDao {
	@Override
	public AdvancePayment getAdvancePaymentById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession() ){
			session.beginTransaction();
			return session.get(AdvancePayment.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AdvancePayment> getAllAdvancePayment() {
		try (Session session = HibernateUtil.getSessionFactory().openSession() ){
			session.beginTransaction();
			String hql="from AdvancePayment";
			return session.createQuery(hql,AdvancePayment.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AdvancePayment> getPartyWiseAdvancePayment(int partyId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession() ){
			session.beginTransaction();
			String hql="from AdvancePayment where partyid=:pid";
			return session.createQuery(hql,AdvancePayment.class).setParameter("pid", partyId).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AdvancePayment> getDateWiseAdvancePayment(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession() ){
			session.beginTransaction();
			String hql="from AdvancePayment where date=:date";
			return session.createQuery(hql,AdvancePayment.class).setParameter("date",date).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<AdvancePayment> getDatePartyWiseAdvancePayment(LocalDate date, int party) {
		try (Session session = HibernateUtil.getSessionFactory().openSession() ){
			session.beginTransaction();
			String hql="from AdvancePayment where partyid=:pid and date=:date";
			return session.createQuery(hql,AdvancePayment.class).setParameter("pid", party).setParameter("date", date).list();			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveAdvancePayment(AdvancePayment payment) {
		try (Session session = HibernateUtil.getSessionFactory().openSession() ){
			session.beginTransaction();
			if(payment.getId()==0)
			{
				session.save(payment);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				session.update(payment);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public double getPartyAdvancePayment(int partyId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "select sum(amount) from AdvancePayment where partyid=:partyid";
			return session.createQuery(hql,Double.class).setParameter("partyid",partyId).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
