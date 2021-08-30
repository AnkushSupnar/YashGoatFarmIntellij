package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.CompanyDao;
import main.java.main.java.hibernate.entities.CompanyDetails;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.time.LocalDate;

public class CompanyDaoImpl implements CompanyDao {

	@Override
	public CompanyDetails getCompanyDetails(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			CompanyDetails company = session.get(CompanyDetails.class, id);
			return company;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public int saveCompany(CompanyDetails company) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			if(company.getId()==0)
			{
				session.save(company);
				session.getTransaction().commit();
				return 1;
			}
			else
			{
				CompanyDetails com = getCompanyDetails(company.getId());
				company.setDate(com.getDate());
				session.update(company);
				session.getTransaction().commit();
				return 2;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	

	@Override
	public int checkLicense(LocalDate date) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			//String hql="from CompanyDetails where date=:d";
			CompanyDetails com = getCompanyDetails(1);
			System.out.println("Compaire "+com.getDate().compareTo(date));
			if(com.getDate().compareTo(date)>=0)
			{
				return 1;
			}else
			 return 0;
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public CompanyDetails getCompanyByName(String name) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()){
			session.beginTransaction();
			String hql="from CompanyDetails where name=:n";
			CompanyDetails company =null;
			try{
			 company = session.createQuery(hql,CompanyDetails.class).setParameter("n", name).getSingleResult();
			}catch(NoResultException e)
			{
				return null;
			}
			return company;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	
}
