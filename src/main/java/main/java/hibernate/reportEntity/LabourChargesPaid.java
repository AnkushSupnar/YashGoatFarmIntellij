package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class LabourChargesPaid {

	long id;
	String labourName;
	LocalDate date;
	float commision;
	float paidCommision;
	public LabourChargesPaid() {
		super();
	}
	public LabourChargesPaid(long id, String labourName, LocalDate date, float commision, float paidCommision) {
		super();
		this.id = id;
		this.labourName = labourName;
		this.date = date;
		this.commision = commision;
		this.paidCommision = paidCommision;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLabourName() {
		return labourName;
	}
	public void setLabourName(String labourName) {
		this.labourName = labourName;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public float getCommision() {
		return commision;
	}
	public void setCommision(float commision) {
		this.commision = commision;
	}
	public float getPaidCommision() {
		return paidCommision;
	}
	public void setPaidCommision(float paidCommision) {
		this.paidCommision = paidCommision;
	}
	@Override
	public String toString() {
		return "LabourChargesPaid [id=" + id + ", labourName=" + labourName + ", date=" + date + ", commision="
				+ commision + ", paidCommision=" + paidCommision + "]";
	}
	
	
}
