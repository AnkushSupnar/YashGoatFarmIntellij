package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.BankTransactionDao;
import main.java.main.java.hibernate.entities.BankTransaction;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class BankTransactionDaoImpl implements BankTransactionDao {

	@Override
	public BankTransaction getBankTransactionById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			BankTransaction bankTr = session.get(BankTransaction.class,id);
			return bankTr;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BankTransaction getBankTransactionByPartucular(String particular) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from BankTransaction where particulars=:parti";
			BankTransaction bt = session.createQuery(hql,BankTransaction.class).setParameter("parti", particular).uniqueResult();
			return bt;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	@Override
	public List<BankTransaction> getAllBankTransaction() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from BankTransaction";
			List<BankTransaction> list = session.createQuery(hql,BankTransaction.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BankTransaction> getBankTransaction(int bankid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from BankTransaction where bankid=:id";
			List<BankTransaction> list = session.createQuery(hql,BankTransaction.class).setParameter("id", bankid).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BankTransaction> getAllDebitTransaction() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from BankTransaction where debit!=0.0";
			List<BankTransaction> list = session.createQuery(hql,BankTransaction.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BankTransaction> getAllCreditTransaction() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from BankTransaction where creidt!=0.0";
			List<BankTransaction> list = session.createQuery(hql,BankTransaction.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BankTransaction> getAllDebitTransaction(int bankid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from BankTransaction where debit!=0.0 and bankid=:id";
			List<BankTransaction> list = session.createQuery(hql,BankTransaction.class).setParameter("id", bankid).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<BankTransaction> getAllCreditTransaction(int bankid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from BankTransaction where credit!=0.0 and bankid=:id";
			List<BankTransaction> list = session.createQuery(hql,BankTransaction.class).setParameter("is",bankid).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public int saveBankTransaction(BankTransaction btr) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(btr.getId()==0)
			{
				session.save(btr);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				session.update(btr);
				session.getTransaction().commit();
				return 2;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void deleteBankTransaction(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			session.delete(getBankTransactionById(id));
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		
	}

	@Override
	public List<BankTransaction> getPeriodWiseBankTransaction(LocalDate fromDate, LocalDate toDate, int bankid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from BankTransaction where date>=:datefrom and date<=:dateto and bankid=:id";
			List<BankTransaction> list = session.createQuery(hql,BankTransaction.class)
					.setParameter("datefrom",fromDate)
					.setParameter("dateto", toDate)
					.setParameter("id", bankid)				
					.list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public float getOpenigBalance(int bankid, LocalDate date) {
		return 0;
	}

	@Override
	public BankTransaction getTransactionByParticular(String particular, int bankid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from BankTransaction where particulars=:parti and bankid=:bid ";
			BankTransaction bt = session.createQuery(hql,BankTransaction.class)
					.setParameter("parti", particular)
					.setParameter("bid", bankid)
					.uniqueResult();
			return bt;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
}
