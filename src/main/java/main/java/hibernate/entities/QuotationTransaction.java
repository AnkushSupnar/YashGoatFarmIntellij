package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QuotationTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String itemname;
	private String unit;
	private float rate;
	private float quantity;
	private float amount;
	private String hsn;

	@ManyToOne
	@JoinColumn(name = "quotationno")
	private Quotation quotation;

	public QuotationTransaction() { super(); }

	public QuotationTransaction(String itemname, String unit, float rate, float quantity, float amount, String hsn, Quotation quotation) {
		super();
		this.itemname = itemname;
		this.unit = unit;
		this.rate = rate;
		this.quantity = quantity;
		this.amount = amount;
		this.hsn = hsn;
		this.quotation = quotation;
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public String getItemname() { return itemname; }
	public void setItemname(String itemname) { this.itemname = itemname; }
	public String getUnit() { return unit; }
	public void setUnit(String unit) { this.unit = unit; }
	public float getRate() { return rate; }
	public void setRate(float rate) { this.rate = rate; }
	public float getQuantity() { return quantity; }
	public void setQuantity(float quantity) { this.quantity = quantity; }
	public float getAmount() { return amount; }
	public void setAmount(float amount) { this.amount = amount; }
	public String getHsn() { return hsn; }
	public void setHsn(String hsn) { this.hsn = hsn; }
	public Quotation getQuotation() { return quotation; }
	public void setQuotation(Quotation quotation) { this.quotation = quotation; }
}
