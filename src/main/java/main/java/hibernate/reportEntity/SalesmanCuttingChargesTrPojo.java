package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class SalesmanCuttingChargesTrPojo {
	long id;
	LocalDate date;
	String itemName;
	float qty;
	float charges;
	public SalesmanCuttingChargesTrPojo() {
		super();		
	}
	public SalesmanCuttingChargesTrPojo(long id, LocalDate date, String itemName, float qty, float charges) {
		super();
		this.id = id;
		this.date = date;
		this.itemName = itemName;
		this.qty = qty;
		this.charges = charges;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public float getQty() {
		return qty;
	}
	public void setQty(float qty) {
		this.qty = qty;
	}
	public float getCharges() {
		return charges;
	}
	public void setCharges(float charges) {
		this.charges = charges;
	}
	@Override
	public String toString() {
		return "SalesmanCuttingChargesTrPojo [id=" + id + ", date=" + date + ", itemName=" + itemName + ", qty=" + qty
				+ ", charges=" + charges + "]";
	}
	
}
