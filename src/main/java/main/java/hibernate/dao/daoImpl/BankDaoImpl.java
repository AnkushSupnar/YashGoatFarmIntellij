package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.BankDao;
import main.java.main.java.hibernate.entities.Bank;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class BankDaoImpl implements BankDao {

	@Override
	public Bank getBankById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			 Bank bank = session.get(Bank.class, id);
			return bank;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Bank getBankByName(String name) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from Bank where bankname=:name";
			Bank bank = session.createQuery(hql,Bank.class).setParameter("name", name).getSingleResult();
			return bank;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bank> getAllBanks() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql= "from Bank";
			List<Bank> list = session.createQuery(hql,Bank.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	}

	@Override
	public List<String> getAllBankNames() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "Select bankname from Bank";
			List<String>list = session.createQuery(hql,String.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public float getBankBalance(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "select balance from Bank where id=:i";
			return session.createQuery(hql,Float.class).setParameter("i", id).uniqueResult();
		
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int saveBank(Bank bank) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			if(bank.getId()==0)
			{
				session.save(bank);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				session.update(bank);
				session.getTransaction().commit();
				return 2;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void addBankBalance(int id,float amount) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			Bank bank = session.get(Bank.class, id);
			if(bank!=null)
			{
				bank.setBalance(bank.getBalance()+amount);
				session.update(bank);
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void reduceBankBalance(int id,float amount) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			Bank bank = session.get(Bank.class, id);
			if(bank!=null)
			{
				bank.setBalance(bank.getBalance()-amount);
				session.update(bank);
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Bank getCashAccount() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bank where accountno='cash'";
			Bank bank = session.createQuery(hql,Bank.class).uniqueResult();
			return bank;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
