package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.ItemStockDao;
import main.java.main.java.hibernate.entities.ItemStock;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.util.List;

public class ItemStockDaoImpl implements ItemStockDao {

	@Override
	public ItemStock getItemStockById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			ItemStock stock = session.get(ItemStock.class, id);
			return stock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ItemStock getItemStockByItemName(String name) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from ItemStock where itemname=:n";
			ItemStock stock=null;
			try {
			stock= session.createQuery(hql,ItemStock.class).setParameter("n", name).getSingleResult();
			}catch(NoResultException nex)
			{
				return null;
			}
			return stock;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveItemStock(ItemStock stock) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(stock.getId()==0)
			{
				session.save(stock);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				ItemStock s = getItemStockById(stock.getId());
				s.setQuantity(stock.getQuantity()+s.getQuantity());
				session.update(s);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<ItemStock> getAllItemStock() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from ItemStock";
			List<ItemStock>list = session.createQuery(hql,ItemStock.class).getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public float getItemStock(String name) {
		if(getItemStockByItemName(name)!=null)
			return getItemStockByItemName(name).getQuantity();
		else
			return 0;
	}

	@Override
	public List<String> getItemNames() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="select itemname from ItemStock where quantity>0";
			@SuppressWarnings("unchecked")
			List<String> list = session.createQuery(hql).getResultList();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int reduceItemStock(String itemname, float qty) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			ItemStock stock = getItemStockByItemName(itemname);
			stock.setQuantity(stock.getQuantity()-qty);
			session.update(stock);
			session.getTransaction().commit();
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	

}
