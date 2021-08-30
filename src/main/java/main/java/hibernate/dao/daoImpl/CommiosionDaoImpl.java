package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.CommisionDao;
import main.java.main.java.hibernate.entities.Commision;
import main.java.main.java.hibernate.entities.CommisionTransaction;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class CommiosionDaoImpl implements CommisionDao {

	@Override
	public List<Commision> getAllCommision() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Commision";
			List<Commision> list= session.createQuery(hql,Commision.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Commision> getDateWiseCommision(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Commision where date=:d";
			List<Commision>list = session.createQuery(hql,Commision.class)
					.setParameter("d", date).list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Commision> getDatePeriodCommision(LocalDate fromDate, LocalDate toDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from Commision where date>=:fdate and date<=:tdate";
			List<Commision>list = session.createQuery(hql,Commision.class)
					.setParameter("fdate", fromDate)
					.setParameter("tdate", toDate)
					.list();
			return list;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Commision> getEmployeeAllCommision(int empid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Commision where employeeId=:eid";
			List<Commision>list = session.createQuery(hql,Commision.class)
					.setParameter("eid", empid)
					.list();
			return list;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Commision> getEmployeeDateWiseCommision(int empid, LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Commision where employeeId=:eid and date=:d";
			List<Commision>list = session.createQuery(hql,Commision.class)
					.setParameter("eid", empid)
					.setParameter("d", date)
					.list();
			return list;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveCommision(Commision commision) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			//if(commision.getId()==0 || getCommisionById(commision.getId())==null)
			Commision c = getCommisionById(commision.getId());
			if(c==null)
			{
				System.out.println(commision);
				session.save(commision);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				deleteTransaction(commision.getId());
				session.update(commision);
				session.getTransaction().commit();
				return 2;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public long getNewCommisionId() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="select MAX(id) from Commision";
			if(session.createQuery(hql).uniqueResult()==null)
			{
				return 1;
			}
			long id = (long) session.createQuery(hql).uniqueResult();
			return ++id;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Commision getCommisionById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			Commision c = session.get(Commision.class, id);
			return c;			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteTransaction(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			Commision c = getCommisionById(id);
			if(c!=null)
			{
				for(CommisionTransaction tr:c.getTransaction())
				{
					session.delete(tr);
				}
			}
			session.getTransaction().commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
}
