package main.java.main.java.hibernate.entities;

import javax.persistence.*;

@Entity
public class Login {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String username;
	String password;
	@OneToOne
	@JoinColumn(name="employeeid")
	Employee employee;
	String status;
	public Login() {
		super();
	}
	public Login(String username, String password, Employee employee, String status) {
		super();
		this.username = username;
		this.password = password;
		this.employee = employee;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Login [id=" + id + ", username=" + username + ", password=" + password + ", employee=" + employee
				+ ", status=" + status + "]";
	}
	public String toString2() {
		return  id + "|" + username + "|" + password + "|" + employee
				+ "|" + status;
	}
}
