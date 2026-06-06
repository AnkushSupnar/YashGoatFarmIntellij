package main.java.main.java.hibernate.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BillPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "billno")
	private Bill bill;

	@ManyToOne
	@JoinColumn(name = "bankid")
	private Bank bank;

	private float amount;
	private String refNo;
	private LocalDate date;

	public BillPayment() {
		super();
	}

	public BillPayment(Bill bill, Bank bank, float amount, String refNo, LocalDate date) {
		super();
		this.bill = bill;
		this.bank = bank;
		this.amount = amount;
		this.refNo = refNo;
		this.date = date;
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public Bill getBill() { return bill; }
	public void setBill(Bill bill) { this.bill = bill; }
	public Bank getBank() { return bank; }
	public void setBank(Bank bank) { this.bank = bank; }
	public float getAmount() { return amount; }
	public void setAmount(float amount) { this.amount = amount; }
	public String getRefNo() { return refNo; }
	public void setRefNo(String refNo) { this.refNo = refNo; }
	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }

	@Override
	public String toString() {
		return "BillPayment [id=" + id + ", bank=" + (bank != null ? bank.getBankname() : "null")
				+ ", amount=" + amount + ", refNo=" + refNo + ", date=" + date + "]";
	}
}
