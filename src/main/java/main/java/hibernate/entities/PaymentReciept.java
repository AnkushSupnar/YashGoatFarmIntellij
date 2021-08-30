package main.java.main.java.hibernate.entities;

import main.java.main.java.hibernate.entities.Bank;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="paymentreciept")
public class PaymentReciept {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	LocalDate date;
	String name;
	String note;
	float amount;
	@ManyToOne
	@JoinColumn(name="bankid")
	Bank bank;
	public PaymentReciept() {
		super();		
	}
	public PaymentReciept(LocalDate date, String name, String note, float amount, Bank bank) {
		super();
		this.date = date;
		this.name = name;
		this.note = note;
		this.amount = amount;
		this.bank = bank;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	@Override
	public String toString() {
		return "PaymentReciept [id=" + id + ", date=" + date + ", name=" + name + ", note=" + note + ", amount="
				+ amount + ", bank=" + bank + "]";
	}
	
	
}
