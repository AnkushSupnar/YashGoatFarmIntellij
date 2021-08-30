package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String itemname;
	private String unit;
	private float rate;
	private float quantity;
	private float amount;
	private float commision;
	@ManyToOne
	@JoinColumn(name="billno")
	private Bill bill;
	public Transaction() {
		super();
	}
	public Transaction(String itemname, String unit, float rate, float quantity, float amount,
			Bill bill,float commision) {
		super();
		this.itemname = itemname;
		this.unit = unit;
		this.rate = rate;
		this.quantity = quantity;
		this.amount = amount;
		this.commision = commision;
		this.bill = bill;
	}
	public float getCommision() {
		return commision;
	}
	public void setCommision(float commision) {
		this.commision = commision;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Bill getBill() {
		return bill;
	}
	public void setBill(Bill bill) {
		this.bill = bill;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", itemname=" + itemname + ", unit=" + unit + ", rate=" + rate + ", quantity="
				+ quantity + ", amount=" + amount + ", commision=" + commision + ", bill=" + bill + "]";
	}
	public String toString2() {
		return  id + "|" + itemname + "|" + unit + "|" + rate + "|"
				+ quantity + "|" + amount + "|" + commision + "|" + bill ;
	}
	
	
}
