package main.java.main.java.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String itemname;
	@Column(nullable = true)
	String hsn;
	float rate;
	String unit;
	float commision;
	String commisionrate;
	float labourCharges;
	public Item() {
		super();
	}

	public Item(String itemname,
				String hsn,
				float rate,
				String unit,
				float commision,
				String commisionrate,
				float labourCharges) {
		this.itemname = itemname;
		this.hsn = hsn;
		this.rate = rate;
		this.unit = unit;
		this.commision = commision;
		this.commisionrate = commisionrate;
		this.labourCharges = labourCharges;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getHsn() {
		return hsn;
	}

	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getCommision() {
		return commision;
	}

	public void setCommision(float commision) {
		this.commision = commision;
	}

	public String getCommisionrate() {
		return commisionrate;
	}

	public void setCommisionrate(String commisionrate) {
		this.commisionrate = commisionrate;
	}

	public float getLabourCharges() {
		return labourCharges;
	}

	public void setLabourCharges(float labourCharges) {
		this.labourCharges = labourCharges;
	}

	@Override
	public String toString() {
		return "Item{" +
				"id=" + id +
				", itemname='" + itemname + '\'' +
				", hsn='" + hsn + '\'' +
				", rate=" + rate +
				", unit='" + unit + '\'' +
				", commision=" + commision +
				", commisionrate='" + commisionrate + '\'' +
				", labourCharges=" + labourCharges +
				'}';
	}

	public String toString2() {
		return id + "|" + itemname + "|" + hsn + "|" + rate + "|" + unit
				+ "|" + commision + "|" + labourCharges;
	}
	
		
}
