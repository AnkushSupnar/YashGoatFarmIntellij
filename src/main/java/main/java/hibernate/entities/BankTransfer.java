package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name="banktransfer")
public class BankTransfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	LocalDate date;
	@ManyToOne
	@JoinColumn(name="frombankid")
	Bank fromBank;
	@ManyToOne
	@JoinColumn(name="tobankid")
	Bank toBank;
	float amount;
	public BankTransfer() {
		super();		
	}
	public BankTransfer(LocalDate date, Bank fromBank, Bank toBank, float amount) {
		super();		
		this.date = date;
		this.fromBank = fromBank;
		this.toBank = toBank;
		this.amount = amount;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}
	/**
	 * @return the fromBank
	 */
	public Bank getFromBank() {
		return fromBank;
	}
	/**
	 * @param fromBank the fromBank to set
	 */
	public void setFromBank(Bank fromBank) {
		this.fromBank = fromBank;
	}
	/**
	 * @return the toBank
	 */
	public Bank getToBank() {
		return toBank;
	}
	/**
	 * @param toBank the toBank to set
	 */
	public void setToBank(Bank toBank) {
		this.toBank = toBank;
	}
	/**
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "BankTransfer [id=" + id + ", date=" + date + ", fromBank=" + fromBank.getBankname() + ", toBank=" + toBank.getBankname()
				+ ", amount=" + amount + "]";
	}
	
		
}
