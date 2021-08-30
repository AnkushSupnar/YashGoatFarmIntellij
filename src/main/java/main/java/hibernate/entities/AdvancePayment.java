package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="advancepayment")
public class AdvancePayment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	@ManyToOne
	@JoinColumn(name="partyid")
	PurchaseParty party;
	
	LocalDate date;
	
	@ManyToOne
	@JoinColumn(name="bankid")
	Bank bank;
	
	float amount;
	String refference;
	public AdvancePayment() {
		super();		
	}
	
	public AdvancePayment(PurchaseParty party, LocalDate date, Bank bank, float amount, String refference) {
		super();
		this.party = party;
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
	public PurchaseParty getParty() {
		return party;
	}
	public void setParty(PurchaseParty party) {
		this.party = party;
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
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "AdvancePayment [id=" + id + ", party=" + party + ", date=" + date + ", bank=" + bank + ", amount="
				+ amount + ", refference=" + refference + "]";
	}

		
}
