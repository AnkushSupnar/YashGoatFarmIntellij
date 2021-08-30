package main.java.main.java.hibernate.entities;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Commision {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	LocalDate date;
	float toatalCommision;
	float paidCommision;
	float totalBill;
	float bankAmount;
	float cashamount;
	float transaportingCharges;
	String bankReffNo;
	@ManyToOne
	@JoinColumn(name="employeeId")
	Employee employee;
	
	@ManyToOne
	@JoinColumn(name="bankId")
	Bank bank;
	
	@OneToMany(mappedBy = "commision",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<CommisionTransaction> transaction = new ArrayList<>();

	public Commision() {
		super();
	}

	public Commision(LocalDate date, float toatalCommision, float paidCommision, float totalBill, float bankAmount,
					 float cashamount, float transaportingCharges, String bankReffNo, Employee employee, Bank bank,
					 List<CommisionTransaction> transaction) {
		super();
		this.date = date;
		this.toatalCommision = toatalCommision;
		this.paidCommision = paidCommision;
		this.totalBill = totalBill;
		this.bankAmount = bankAmount;
		this.cashamount = cashamount;
		this.transaportingCharges = transaportingCharges;
		this.bankReffNo = bankReffNo;
		this.employee = employee;
		this.bank = bank;
		this.transaction = transaction;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public float getToatalCommision() {
		return toatalCommision;
	}

	public void setToatalCommision(float toatalCommision) {
		this.toatalCommision = toatalCommision;
	}

	public float getPaidCommision() {
		return paidCommision;
	}

	public void setPaidCommision(float paidCommision) {
		this.paidCommision = paidCommision;
	}

	public float getTotalBill() {
		return totalBill;
	}

	public void setTotalBill(float totalBill) {
		this.totalBill = totalBill;
	}

	public float getBankAmount() {
		return bankAmount;
	}

	public void setBankAmount(float bankAmount) {
		this.bankAmount = bankAmount;
	}

	public float getCashamount() {
		return cashamount;
	}

	public void setCashamount(float cashamount) {
		this.cashamount = cashamount;
	}

	public float getTransaportingCharges() {
		return transaportingCharges;
	}

	public void setTransaportingCharges(float transaportingCharges) {
		this.transaportingCharges = transaportingCharges;
	}

	public String getBankReffNo() {
		return bankReffNo;
	}

	public void setBankReffNo(String bankReffNo) {
		this.bankReffNo = bankReffNo;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public List<CommisionTransaction> getTransaction() {
		return transaction;
	}

	public void setTransaction(List<CommisionTransaction> transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "Commision [id=" + id + ", date=" + date + ", toatalCommision=" + toatalCommision + ", paidCommision="
				+ paidCommision + ", totalBill=" + totalBill + ", bankAmount=" + bankAmount + ", cashamount="
				+ cashamount + ", transaportingCharges=" + transaportingCharges + ", bankReffNo=" + bankReffNo
				+ ", employee=" + employee + ", bank=" + bank + ", transaction=" + "]";
	}

	
	
}