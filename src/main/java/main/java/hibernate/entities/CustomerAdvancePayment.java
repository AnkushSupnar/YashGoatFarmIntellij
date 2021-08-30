package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="customeradvancepayment")
public class CustomerAdvancePayment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@ManyToOne
	@JoinColumn(name="customerid")
	Customer customer;
	
	LocalDate date;
	
	@ManyToOne
	@JoinColumn(name="bankid")
	Bank bank;
	
	float amount;
	String refference;
	public CustomerAdvancePayment() {
		super();	
	}
	public CustomerAdvancePayment(Customer customer, LocalDate date, Bank bank, float amount, String refference) {
		super();
		this.customer = customer;
		this.date = date;
		this.bank = bank;
		this.amount = amount;
		this.refference = refference;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
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
	public String getRefference() {
		return refference;
	}
	public void setRefference(String refference) {
		this.refference = refference;
	}
	@Override
	public String toString() {
		return "CustomerAdvancePayment [id=" + id + ", customer=" + customer + ", date=" + date + ", bank=" + bank
				+ ", amount=" + amount + ", refference=" + refference + "]";
	}
	
}
