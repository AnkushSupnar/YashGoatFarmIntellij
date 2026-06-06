package main.java.main.java.hibernate.dao.daoImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import main.java.main.java.hibernate.dao.dao.QuotationDao;
import main.java.main.java.hibernate.entities.Quotation;
import main.java.main.java.hibernate.entities.QuotationTransaction;
import main.java.main.java.hibernate.util.HibernateUtil;

public class QuotationDaoImpl implements QuotationDao {

	@Override
	public int saveQuotation(Quotation quotation) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			if (quotation.getId() == 0) {
				session.save(quotation);
			} else {
				Quotation existing = session.get(Quotation.class, quotation.getId());
				if (existing == null) {
					session.save(quotation);
				} else {
					if (existing.isBilled()) {
						session.getTransaction().rollback();
						return 2;
					}
					existing.setCustomer(quotation.getCustomer());
					existing.setDate(quotation.getDate());
					existing.setValidUntil(quotation.getValidUntil());
					existing.setNettotal(quotation.getNettotal());
					existing.setTransportingchrges(quotation.getTransportingchrges());
					existing.setOtherchargs(quotation.getOtherchargs());
					existing.setBank(quotation.getBank());
					existing.setEmployee(quotation.getEmployee());
					existing.setNotes(quotation.getNotes());
					existing.setStatus(quotation.getStatus());
					existing.getTransaction().clear();
					if (quotation.getTransaction() != null) {
						for (QuotationTransaction tr : quotation.getTransaction()) {
							tr.setQuotation(existing);
							tr.setId(0);
							existing.getTransaction().add(tr);
						}
					}
					session.merge(existing);
					quotation.setId(existing.getId());
				}
			}
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public long getNewQuotationNo() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			Long max = session.createQuery("select max(id) from Quotation", Long.class).uniqueResult();
			return (max == null ? 0 : max) + 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}

	@Override
	public Quotation getQuotationById(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			return session.get(Quotation.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Quotation> getAllQuotations() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			return session.createQuery("from Quotation order by id desc", Quotation.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public List<Quotation> searchQuotations(String customerName, LocalDate startDate, LocalDate endDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			StringBuilder hql = new StringBuilder("select distinct q from Quotation q left join q.customer c where 1=1");
			boolean hasName = customerName != null && !customerName.trim().isEmpty();
			if (hasName) {
				hql.append(" and (lower(coalesce(c.fname,'')) like :name")
						.append(" or lower(coalesce(c.mname,'')) like :name")
						.append(" or lower(coalesce(c.lname,'')) like :name)");
			}
			if (startDate != null) hql.append(" and q.date >= :s");
			if (endDate != null) hql.append(" and q.date <= :e");
			hql.append(" order by q.id desc");

			Query<Quotation> query = session.createQuery(hql.toString(), Quotation.class);
			if (hasName) query.setParameter("name", "%" + customerName.trim().toLowerCase() + "%");
			if (startDate != null) query.setParameter("s", startDate);
			if (endDate != null) query.setParameter("e", endDate);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public int markBilled(long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			Quotation q = session.get(Quotation.class, id);
			if (q == null) {
				session.getTransaction().rollback();
				return 0;
			}
			q.setBilled(true);
			session.update(q);
			session.getTransaction().commit();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
