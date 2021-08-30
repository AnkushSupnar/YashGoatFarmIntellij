package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class ReportTable {
	int srNo;
	LocalDate date;
	String salesman;
	String itemName;
	float qty;
	float rate;
	float amount;
	float commision;
	float labour;
	float remain;
	public ReportTable() {
		super();
	}
	public ReportTable(int srNo, LocalDate date, String salesman, String itemName, float qty, float rate,
			float amount, float commision, float labour, float remain) {
		super();
		this.srNo = srNo;
		this.date = date;
		this.salesman = salesman;
		this.itemName = itemName;
		this.qty = qty;
		this.rate = rate;
		this.amount = amount;
		this.commision = commision;
		this.labour = labour;
		this.remain = remain;
	}
	public int getSrNo() {
		return srNo;
	}
	public void setSrNo(int srNo) {
		this.srNo = srNo;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getSalesman() {
		return salesman;
	}
	public void setSalesman(String salesman) {
		this.salesman = salesman;
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
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public float getCommision() {
		return commision;
	}
	public void setCommision(float commision) {
		this.commision = commision;
	}
	public float getLabour() {
		return labour;
	}
	public void setLabour(float labour) {
		this.labour = labour;
	}
	public float getRemain() {
		return remain;
	}
	public void setRemain(float remain) {
		this.remain = remain;
	}
	@Override
	public String toString() {
		return "ReportTable [srNo=" + srNo + ", date=" + date + ", salesman=" + salesman + ", itemName=" + itemName
				+ ", qty=" + qty + ", rate=" + rate + ", amount=" + amount + ", commision=" + commision + ", labour="
				+ labour + ", remain=" + remain + "]";
	}
}