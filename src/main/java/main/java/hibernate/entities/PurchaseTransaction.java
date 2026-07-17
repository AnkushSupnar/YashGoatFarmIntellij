package main.java.main.java.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PurchaseTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String itemname;
	String unit;
	float rate;
	float quantity;
	float amount;
	@Column(columnDefinition = "DECIMAL(5,2) DEFAULT 0.00")
	private float igstPercent;
	@Column(columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
	private float igstAmount;
	@ManyToOne
	@JoinColumn(name = "billno")
	PurchaseInvoice invoice;

	public PurchaseTransaction() {
		super();
	}

	public PurchaseTransaction(String itemname, 
			String unit, float rate, float quantity, float amount,
			PurchaseInvoice invoice) {
		super();
		this.itemname = itemname;
		this.unit = unit;
		this.rate = rate;
		this.quantity = quantity;
		this.amount = amount;
		this.invoice = invoice;
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

	public float getIgstPercent() { return igstPercent; }
	public void setIgstPercent(float igstPercent) { this.igstPercent = igstPercent; }

	public float getIgstAmount() { return igstAmount; }
	public void setIgstAmount(float igstAmount) { this.igstAmount = igstAmount; }

	public PurchaseInvoice getInvoiceno() {
		return invoice;
	}

	public void setInvoiceno(PurchaseInvoice invoice) {
		this.invoice = invoice;
	}

	@Override
	public String toString() {
		return "PurchaseTransaction [id=" + id + ", itemname=" + itemname + ", unit=" + unit + ", rate=" + rate
				+ ", quantity=" + quantity + ", amount=" + amount + ", invoice=" + invoice + "]";
	}
	public String toString2() {
		return id + "|" + itemname + "|" + unit + "|" + rate
				+ "|" + quantity + "|" + amount + "|" + invoice;
	}

}
