package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.PaymentRecieptDao;
import main.java.main.java.hibernate.entities.PaymentReciept;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

public class PaymentReceiptDaoImpl implements PaymentRecieptDao {

	
	@Override
	public PaymentReciept getPaymentRecieptById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			try {
				return session.get(PaymentReciept.class, id);
			} catch (NoResultException no) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PaymentReciept> getAllPaymentReciept() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			return session.createQuery("from PaymentReciept",PaymentReciept.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PaymentReciept> getDateWisePaymentReciept(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from PaymentReciept where date=:d";
			return session.createQuery(hql,PaymentReciept.class).setParameter("d",date).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PaymentReciept> getDatePeriodPaymentReciept(LocalDate start, LocalDate end) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from PaymentReciept where date between :start and :end";
			return session.createQuery(hql,PaymentReciept.class).setParameter("start", start).setParameter("end",end).list();
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<PaymentReciept> getNameWisePaymentReciept(String name) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from PaymentReciept where name=:name";
			return session.createQuery(hql,PaymentReciept.class).setParameter("name",name).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	}

	@Override
	public List<PaymentReciept> getBankWisePaymentReciept(int bankid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from PaymentReciept where bankid=:bid";
			return session.createQuery(hql,PaymentReciept.class).setParameter("bid", bankid).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int savePaymentReciept(PaymentReciept reciept) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(reciept.getId()==0)
			{
				session.save(reciept);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				PaymentReciept p = getPaymentRecieptById(reciept.getId());
				reciept.setId(p.getId());
				session.update(reciept);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
