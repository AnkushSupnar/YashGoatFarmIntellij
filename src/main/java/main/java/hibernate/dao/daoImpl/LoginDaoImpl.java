package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.LoginDao;
import main.java.main.java.hibernate.entities.Login;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class LoginDaoImpl implements LoginDao {

	@Override
	public Login getLoginById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession())
		{
			session.beginTransaction();
			Login login = session.get(Login.class, id);
			return login;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Login getLoginByName(String name) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from Login where username=:name";
			Login login = session.createQuery(hql, Login.class).setParameter("name", name).uniqueResult();
			return login;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Login> getAllLogin() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "From Login";
			List<Login> list = session.createQuery(hql, Login.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getAllUserNames() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "select username from Login";
			@SuppressWarnings("unchecked")
			List<String> list = session.createQuery(hql).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveLogin(Login login) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			if (login.getId() == 0) {
				session.save(login);
				session.getTransaction().commit();
				return 1;
			} else {

				Login l = new Login(login.getUsername(), login.getPassword(), login.getEmployee(), login.getStatus());
				l.setId(login.getId());
				session.update(l);
				session.getTransaction().commit();
				return 2;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int validateLogin(String username, String Password) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			Login login = getLoginByName(username);
			if (login != null && login.getPassword().equals(Password))
				return 1;
			else
				return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	@Override
	public int changeLoginStatus(String Status,int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			Login login = getLoginById(id);
			if(login!=null)
			{
				login.setStatus(Status);
				session.update(login);
				session.getTransaction().commit();
				return 1;
			}
			else
				return 0;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
