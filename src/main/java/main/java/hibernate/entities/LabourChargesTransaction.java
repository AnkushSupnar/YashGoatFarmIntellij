package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class LabourChargesTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	LocalDate date;
	
	String itemName;
	float qty;
	float charges;
	float paidLabourCharges;
	
	@ManyToOne
	@JoinColumn(name = "labourChargesId")
	LabourCharges labourCharges;

	public LabourChargesTransaction() {
		super();
	}

	public LabourChargesTransaction(LocalDate date, String itemName, float qty, float charges, float paidLabourCharges,
			LabourCharges labourCharges) {
		super();
		this.date = date;
		this.itemName = itemName;
		this.qty = qty;
		this.charges = charges;
		this.paidLabourCharges = paidLabourCharges;
		this.labourCharges = labourCharges;
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
		return itemName;
	}

	public void setItem(String itemName) {
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

	public float getPaidLabourCharges() {
		return paidLabourCharges;
	}

	public void setPaidLabourCharges(float paidLabourCharges) {
		this.paidLabourCharges = paidLabourCharges;
	}

	public LabourCharges getLabourCharges() {
		return labourCharges;
	}

	public void setLabourCharges(LabourCharges labourCharges) {
		this.labourCharges = labourCharges;
	}

	@Override
	public String toString() {
		return "LabourChargesTransaction [id=" + id + ", date=" + date + ", itemName=" + itemName + ", qty=" + qty
				+ ", charges=" + charges + ", paidLabourCharges=" + paidLabourCharges + ", labourCharges="
				+ labourCharges + "]";
	}
	
}
