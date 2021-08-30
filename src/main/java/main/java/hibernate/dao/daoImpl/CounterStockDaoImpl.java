package main.java.main.java.hibernate.dao.daoImpl;


import main.java.main.java.hibernate.dao.dao.CounterStockDao;
import main.java.main.java.hibernate.entities.CounterStock;
import main.java.main.java.hibernate.entities.CounterStockTransaction;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

public class CounterStockDaoImpl implements CounterStockDao {

	
	@Override
	public List<CounterStock> getAllCounterStock() {
	try (Session session =HibernateUtil.getSessionFactory().openSession()){
		session.beginTransaction();
		String hql = "from CounterStock";
		return session.createQuery(hql,CounterStock.class).list();
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	}

	@Override
	public CounterStock getCounterStockById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			try {
				return session.get(CounterStock.class, id);
			} catch (NoResultException nr) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CounterStock> getCounterStockByDate(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CounterStock where date=:date";
			return session.createQuery(hql,CounterStock.class).setParameter("date", date).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<CounterStock> getCounterStockByDatePeriod(LocalDate start, LocalDate end) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CounterStock where date beetween :start and :end";
			return session.createQuery(hql,CounterStock.class)
					.setParameter("start",start).
					setParameter("end", end).list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveCounterStock(CounterStock stock) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(stock.getId()==0)
			{
				//System.out.println("Saving"+stock.getTransaction().size());
				//stock.getTransaction().stream().forEach(e->System.out.println(e));
				session.save(stock);
				
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				System.out.println("Updating");
				deleteTransaction(stock.getId());
				session.update(stock);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void deleteTransaction(long stockid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			for(CounterStockTransaction tr:getCounterStockById(stockid).getTransaction())
			{
				session.delete(tr);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public float getAvailableCounterStock(String itemname) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			//String hql="select ";
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
