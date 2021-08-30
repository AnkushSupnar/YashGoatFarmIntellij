package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class WeeklyItemSales {

	int srno;
	LocalDate date;
	long billNo;
	String itemName;
	float qty;
	String unit;
	float rate;
	float amount;
	public WeeklyItemSales() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WeeklyItemSales(int srno, LocalDate date, long billNo, String itemName, float qty, String unit, float rate,
			float amount) {
		super();
		this.srno = srno;
		this.date = date;
		this.billNo = billNo;
		this.itemName = itemName;
		this.qty = qty;
		this.unit = unit;
		this.rate = rate;
		this.amount = amount;
	}
	public int getSrno() {
		return srno;
	}
	public void setSrno(int srno) {
		this.srno = srno;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public long getBillNo() {
		return billNo;
	}
	public void setBillNo(long billNo) {
		this.billNo = billNo;
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
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "WeeklyItemSales [srno=" + srno + ", date=" + date + ", billNo=" + billNo + ", itemName=" + itemName
				+ ", qty=" + qty + ", unit=" + unit + ", rate=" + rate + ", amount=" + amount + "]";
	}
	
}
