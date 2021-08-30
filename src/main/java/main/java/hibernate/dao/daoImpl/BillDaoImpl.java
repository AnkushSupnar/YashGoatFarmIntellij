package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.BillDao;
import main.java.main.java.hibernate.entities.Bill;
import main.java.main.java.hibernate.entities.Transaction;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class BillDaoImpl implements BillDao {

	@Override
	public Bill getBillByBillno(long billno) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			Bill bill = session.get(Bill.class, billno);
			return bill;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bill> getAllBills() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from Bill";
			List<Bill> list = session.createQuery(hql,Bill.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bill> getDateWiseBill(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from Bill where date=:d";
			List<Bill> list = session.createQuery(hql,Bill.class).setParameter("d",date).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bill> getMonthWiseBill(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from Bill where MONTH(date)=MONTH(:d)";
			List<Bill> list = session.createQuery(hql,Bill.class).setParameter("d",date).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<Bill> getThisWeekBill() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			//String hql = "from Bill where date between DATE_FORMAT(CURDATE(),'%Y-01-01') AND CURDATE()";
			String hql="FROM Bill WHERE WEEKOFYEAR(date)=WEEKOFYEAR(curdate())";
			List<Bill> list = session.createQuery(hql,Bill.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<Bill> getThisYearBill() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from Bill where Year(date)=Year(CURDATE())";		
			List<Bill> list = session.createQuery(hql,Bill.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Transaction> getBillTransactions(int billno) {
	try(Session session = HibernateUtil.getSessionFactory().openSession()) {
		session.beginTransaction();
		String hql="select Transaction Bill where billno=:no";
		List<Transaction>list = session.createQuery(hql,Transaction.class).setParameter("no", billno).list();
		return list;
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	}

	@Override
	public int saveBill(Bill bill) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			Bill b =getBillByBillno(bill.getBillno()); 
			if(b==null)
			{
				session.save(bill);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				deleteTransactions(bill.getBillno());
				session.update(bill);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public long getNewBNillNo() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "select MAX(billno) from Bill";
			if(session.createQuery(hql).uniqueResult()==null)
			{
				return 1;
			}
			long billno = (long) session.createQuery(hql).uniqueResult();
			return ++billno;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("////////error //////"+e.getMessage());
			return 0;
		}
	}

	@Override
	public void deleteTransactions(long l) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			Bill bill = getBillByBillno(l);
			if(bill!=null)
			{
				for(Transaction tr:bill.getTransaction())
				{
					System.out.println(tr.getId());
					session.delete(tr);
				}
				session.getTransaction().commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Bill> getUnpaidCommisionBills(int employee) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bill where employeeid=:eid AND paidcommision<=0";
			List<Bill>list = session.createQuery(hql,Bill.class).setParameter("eid", employee).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bill> getUnpaidCommisionBillsPeriodWise(int employee,LocalDate fromDate,LocalDate toDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bill where employeeid=:eid AND paidcommision<=0 and date BETWEEN :fromD AND :toD";
			List<Bill>list = session.createQuery(hql,Bill.class)
					.setParameter("eid",employee)
					.setParameter("fromD",fromDate)
					.setParameter("toD", toDate).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int updatePaidCommision(List<Bill> list) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="update Bill set paidcommision=:c where billno=:no";
			
			for(Bill bill:list)
			{
				 @SuppressWarnings("rawtypes")
				 Query query=session.createQuery(hql).setParameter("c", bill.getOtherchargs())
						 .setParameter("no", bill.getBillno());
				 query.executeUpdate();
			}
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Bill> getPeriodWiseBills(LocalDate fromDate, LocalDate toDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql = "from Bill where date>=:datefrom and date<=:dateto";
			List<Bill> list = session.createQuery(hql,Bill.class).setParameter("datefrom", fromDate).setParameter("dateto", toDate).list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Bill> getYearWiseBills(int year) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bill where Year(date)=:year";
			List<Bill> list = session.createQuery(hql,Bill.class).setParameter("year",year).list();
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bill> getUnpaidBills(int customer) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bill where "
					+ "nettotal+otherchargs+transportingchrges>recivedamount And customerid=:cid ";
			List<Bill> list = session.createQuery(hql,Bill.class).setParameter("cid", customer).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int updateReceivedAmount(Bill bill) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="update Bill set recivedamount=recivedamount+:amt where billno=:no";
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery(hql).setParameter("amt", bill.getTransportingchrges()).
						setParameter("no", bill.getBillno());
				query.executeUpdate();
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Bill> getAllUnpaidBills() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bill where (nettotal+otherchargs+transportingchrges)>recivedamount";
			List<Bill> list = session.createQuery(hql,Bill.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public double getCustomerTotalPaidBillAmount(int customerId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="select sum(recivedamount) from bill where customerid=:cid";
			return session.createQuery(hql,Double.class).setParameter("cid",customerId).uniqueResult();			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public double getCustomerTotalBillAmount(int customerId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="select sum(nettotal+otherchargs+transportingchrges) from bill where customerid=:cid";
			return session.createQuery(hql,Double.class).setParameter("cid",customerId).uniqueResult();			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public double getWholeSaleBillAmount(int customerid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql=" select sum(nettotal+otherchargs+transportingchrges) from Bill where customerid=:cid and bankid=2";
			return session.createQuery(hql,Double.class).setParameter("cid", customerid).uniqueResult();			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<Bill> getDateWiseSalesmanBills(int empid, LocalDate date) {
	try (Session session = HibernateUtil.getSessionFactory().openSession()){
		session.beginTransaction();
		String hql="from Bill where employeeid=:empid and date=:date";
		return session.createQuery(hql,Bill.class).setParameter("empid",empid).setParameter("date", date).list();
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
	}

	@Override
	public List<Bill>getPeriodWiseSalesmanBills(int empid,LocalDate start,LocalDate end) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
	String hql="from Bill where employeeid=:empid and date between :start and :end";
			return session.createQuery(hql,Bill.class).
					setParameter("empid",empid).
					setParameter("start",start).
					setParameter("end",end).
					list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bill> getSalesmanAllBills(int empid) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
	String hql="from Bill where employeeid=:empid";
			return session.createQuery(hql,Bill.class).
					setParameter("empid",empid).					
					list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public List<Bill> getCustomerAndDateWiseBill(int customerId, LocalDate date) {
		try(Session session = HibernateUtil.getSessionFactory().openSession())
		{
			session.beginTransaction();
			String hql="from Bill where customerid=:cid and date=:d ";
			return session.createQuery(hql,Bill.class).setParameter("cid",customerId).setParameter("d",date).list();
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Bill> getBillsByCustomer(int customerId) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bill where customerid=:cid";
			return session.createQuery(hql,Bill.class).setParameter("cid",customerId).list();

		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<Bill> getBillsByCustomerAndPeriod(int customerId, LocalDate start, LocalDate end) {
		try(Session session =HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from Bill where customerid=:cid and date between :start and :end";
			return session.createQuery(hql,Bill.class).
					setParameter("cid",customerId).
					setParameter("start",start).
					setParameter("end",end).list();
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
