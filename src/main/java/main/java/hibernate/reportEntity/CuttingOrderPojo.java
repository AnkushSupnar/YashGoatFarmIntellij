package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class CuttingOrderPojo {
	long id;
	LocalDate date;
	String salesmanName;
	String customerName;
	String labourName;
	public CuttingOrderPojo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CuttingOrderPojo(long id, 
			LocalDate date, 
			String salesmanName, 
			String customerName,
			String labourName) {
		super();
		this.id = id;
		this.date = date;
		this.salesmanName = salesmanName;
		this.customerName = customerName;
		this.labourName = labourName;
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
	public String getSalesmanName() {
		return salesmanName;
	}
	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getLabourName() {
		return labourName;
	}
	public void setLabourName(String labourName) {
		this.labourName = labourName;
	}
	@Override
	public String toString() {
		return "CuttingOrderPojo [id=" + id + ", date=" + date + ", salesmanName=" + salesmanName + ", customerName="
				+ customerName + ", labourName=" + labourName + "]";
	}
	
}
