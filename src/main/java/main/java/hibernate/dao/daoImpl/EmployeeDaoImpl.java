package main.java.main.java.hibernate.dao.daoImpl;

import main.java.main.java.hibernate.dao.dao.EmployeeDao;
import main.java.main.java.hibernate.entities.Employee;
import main.java.main.java.hibernate.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

	@Override
	public Employee getEmployeeById(int id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, id);
			session.getTransaction().commit();
			session.close();
			return employee;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Employee getEmployeeByName(String name) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String n[] = name.split(" ");
			String hql = "from Employee where fname=:f and mname=:m and lname=:l";
			Employee employee = session.createQuery(hql, Employee.class).setParameter("f", n[0]).setParameter("m", n[1])
					.setParameter("l", n[2]).getSingleResult();
			return employee;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getAllEmployeeNames() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "select concat(fname,' ',mname,' ',lname) from Employee";
			@SuppressWarnings("unchecked")
			List<String> list = session.createNativeQuery(hql).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Employee> getAllEmployee() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "from Employee";
			List<Employee> list = session.createQuery(hql, Employee.class).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int saveEmployee(Employee employee) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			if (employee.getId() == 0) {
				session.save(employee);
				session.getTransaction().commit();
				return 1;
			} else {
				Employee e1 = new Employee(employee.getFname(), employee.getMname(), employee.getLname(),
						employee.getMobileno(), employee.getAltermobileno(), employee.getEmail(), employee.getAddress(),
						employee.getCity(), employee.getTaluka(), employee.getDistrict(), employee.getPost(),
						employee.getPin(), employee.getJobpost(), employee.getSalary(), employee.getSalarytime(),
						employee.getSalarytype(), employee.getJoiningdate(), employee.getExitdate());
				e1.setId(employee.getId());
				session.update(e1);
				session.getTransaction().commit();
				return 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<String> getAllSalesmanNames() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "select concat(fname,' ',mname,' ',lname) from Employee where jobpost='Salesman'";
			@SuppressWarnings("unchecked")
			List<String> list = session.createNativeQuery(hql).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getAllLabourNames() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			session.beginTransaction();
			String hql = "select concat(fname,' ',mname,' ',lname) from Employee where jobpost='Labour'";
			@SuppressWarnings("unchecked")
			List<String> list = session.createNativeQuery(hql).list();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
