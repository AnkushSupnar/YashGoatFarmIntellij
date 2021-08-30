package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
public class LabourCharges {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	LocalDate date;
	
	@ManyToOne
	@JoinColumn(name="labourId")
	Employee labour;
	
	@ManyToOne
	@JoinColumn(name="bankId")
	Bank bank;
	String bankReffNo;
	float amount;
	
	@OneToMany(mappedBy = "labourCharges",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	List<LabourChargesTransaction> transaction = new ArrayList<LabourChargesTransaction>();

	public LabourCharges() {
		super();
	}

	public LabourCharges(LocalDate date, Employee labour, Bank bank, float amount,
						 List<LabourChargesTransaction> transaction, String bankReffNo) {
		super();
		this.date = date;
		this.labour = labour;
		this.bank = bank;
		this.amount = amount;
		this.bankReffNo = bankReffNo;
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

	public Employee getLabour() {
		return labour;
	}

	public void setLabour(Employee labour) {
		this.labour = labour;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public List<LabourChargesTransaction> getTransaction() {
		return transaction;
	}
	
	public String getBankReffNo() {
		return bankReffNo;
	}

	public void setBankReffNo(String bankReffNo) {
		this.bankReffNo = bankReffNo;
	}

	public void setTransaction(List<LabourChargesTransaction> transaction) {
		this.transaction = transaction;
	}

	@Override
	public String toString() {
		return "LabourCharges [id=" + id + ", date=" + date + ", labour=" + labour + ", bank=" + bank + ", bankReffNo="
				+ bankReffNo + ", amount=" + amount + ", transaction=" + transaction + "]";
	}

	
}
