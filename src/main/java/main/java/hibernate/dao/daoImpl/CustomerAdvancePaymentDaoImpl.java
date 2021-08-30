package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.CustomerAdvancePaymentDao;
import main.java.main.java.hibernate.entities.CustomerAdvancePayment;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

public class CustomerAdvancePaymentDaoImpl implements CustomerAdvancePaymentDao {

	@Override
	public CustomerAdvancePayment getCustomerAdvanceById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			return session.get(CustomerAdvancePayment.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CustomerAdvancePayment> getAllCustomerAdvance() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from CustomerAdvancePayment";
			return session.createQuery(hql, CustomerAdvancePayment.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByCustomer(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from CustomerAdvancePayment where customerid=:id";
			return session.createQuery(hql, CustomerAdvancePayment.class).setParameter("id", id).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByDate(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from CustomerAdvancePayment where date=:date";
			return session.createQuery(hql, CustomerAdvancePayment.class).setParameter("date", date).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByDatePeriod(LocalDate start, LocalDate end) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from CustomerAdvancePayment where date between :start and :end";
			return session.createQuery(hql, CustomerAdvancePayment.class).setParameter("start", start)
					.setParameter("end", end).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByCustomerAndDatePeriod(int customerid, LocalDate start, LocalDate end) {
	try(Session session = HibernateUtil.getSessionFactory().openSession()) {
		session.beginTransaction();
		String hql="from CustomerAdvancePayment where customerid=:cid and date between :start and :end";
		return session.createQuery(hql,CustomerAdvancePayment.class).setParameter("cid",customerid).setParameter("start",start).setParameter("end",end).list();
	}catch(Exception e)
	{
		e.printStackTrace();
		return null;
	}
	}

	@Override
	public List<CustomerAdvancePayment> getCustomerAdvanceByDatePeriodAndCustomer(LocalDate start, LocalDate end,
			int customerid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from CustomerAdvancePayment where customerid=:cid and date between ;start and :end";
			return session.createQuery(hql, CustomerAdvancePayment.class).setParameter("cid", customerid)
					.setParameter("start", start).setParameter("end", end).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveCustomerAdvance(CustomerAdvancePayment payment) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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
	public double getCustomerTotalAdvance(int customerId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="select sum(amount) from CustomerAdvancePayment where customerid=:cid";
			double advance =0;
			try {
				advance= session.createQuery(hql,Double.class).setParameter("cid", customerId).getSingleResult();
			}catch(NoResultException nex)
			{
				return 0;
			}
			return advance;
		} catch (Exception e) {
			//e.printStackTrace();
			return 0;
		}
	}

}
