package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class LabourCharges {
	long id;
	LocalDate date;
	String item;
	float qty;
	float labourcharges;
	float paidLabourCharges;
	public LabourCharges() {
		super();		
	}
	public LabourCharges(long id, LocalDate date, String item, float qty, float labourcharges,
			float paidLabourCharges) {
		super();
		this.id = id;
		this.date = date;
		this.item = item;
		this.qty = qty;
		this.labourcharges = labourcharges;
		this.paidLabourCharges = paidLabourCharges;
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
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public float getQty() {
		return qty;
	}
	public void setQty(float qty) {
		this.qty = qty;
	}
	public float getLabourcharges() {
		return labourcharges;
	}
	public void setLabourcharges(float labourcharges) {
		this.labourcharges = labourcharges;
	}
	public float getPaidLabourCharges() {
		return paidLabourCharges;
	}
	public void setPaidLabourCharges(float paidLabourCharges) {
		this.paidLabourCharges = paidLabourCharges;
	}
	@Override
	public String toString() {
		return "LabourCharges [id=" + id + ", date=" + date + ", item=" + item + ", qty=" + qty + ", labourcharges="
				+ labourcharges + ", paidLabourCharges=" + paidLabourCharges + "]";
	}
	
}
