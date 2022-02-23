package main.java.main.java.hibernate.util;

import main.java.main.java.hibernate.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.io.FileReader;
import java.util.Properties;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

	// for Local instance
	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();
				Properties setting = new Properties();
				FileReader read = new FileReader("D:\\Software\\yash2022.properties");
				setting.load(read);
				System.out.println(setting);
				setting.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
//				setting.put(Environment.URL, "jdbc:mysql://localhost:3306/yash");
//				setting.put(Environment.USER, "root");
//				setting.put(Environment.PASS, "2355");
				setting.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
				setting.put(Environment.SHOW_SQL, "false");
				setting.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
				setting.put(Environment.HBM2DDL_AUTO, "update");
//				
//allow access remote
//				mysql> CREATE USER 'monty'@'localhost' IDENTIFIED BY 'some_pass';
//				mysql> GRANT ALL PRIVILEGES ON *.* TO 'monty'@'localhost'
//				    ->     WITH GRANT OPTION;
//				mysql> CREATE USER 'monty'@'%' IDENTIFIED BY 'some_pass';
//				mysql> GRANT ALL PRIVILEGES ON *.* TO 'monty'@'%'
//				    ->     WITH GRANT OPTION;
//				
				
				
				configuration.setProperties(setting);
				configuration.addAnnotatedClass(Login.class);
				configuration.addAnnotatedClass(Employee.class);
				configuration.addAnnotatedClass(Item.class);
				configuration.addAnnotatedClass(Customer.class);
				configuration.addAnnotatedClass(PurchaseParty.class);
				configuration.addAnnotatedClass(Bank.class);
				configuration.addAnnotatedClass(Transaction.class);
				configuration.addAnnotatedClass(Bill.class);
				configuration.addAnnotatedClass(BankTransaction.class);
				configuration.addAnnotatedClass(PurchaseInvoice.class);
				configuration.addAnnotatedClass(PurchaseTransaction.class);
				configuration.addAnnotatedClass(ItemStock.class);
				configuration.addAnnotatedClass(Commision.class);
				configuration.addAnnotatedClass(CompanyDetails.class);
				configuration.addAnnotatedClass(CommisionTransaction.class);
				configuration.addAnnotatedClass(CuttingOrder.class);
				configuration.addAnnotatedClass(CuttingTransaction.class);
				configuration.addAnnotatedClass(CuttingLabour.class);
				configuration.addAnnotatedClass(LabourCharges.class);
				configuration.addAnnotatedClass(LabourChargesTransaction.class);
				configuration.addAnnotatedClass(CounterStock.class);
				configuration.addAnnotatedClass(CounterStockTransaction.class);
				configuration.addAnnotatedClass(CounterStockData.class);
				configuration.addAnnotatedClass(SalesmanCuttingCharges.class);
				configuration.addAnnotatedClass(SalesmanCuttingTransaction.class);
				configuration.addAnnotatedClass(PaymentReciept.class);
				configuration.addAnnotatedClass(AdvancePayment.class);
				configuration.addAnnotatedClass(CustomerAdvancePayment.class);
				configuration.addAnnotatedClass(BankTransfer.class);

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();
				System.out.println("java hibernate config service registry created from File:- "+"D:\\Software\\yash2022.properties");
				System.out.println(serviceRegistry);
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				return sessionFactory;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return sessionFactory;
	}

	public static void main(String[] args) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			System.out.println("Session Created");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
