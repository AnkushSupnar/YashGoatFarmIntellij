package main.java.main.java.hibernate.reportEntity;

public class DailyItemSales {

	int srno;
	long billNo;
	String itemName;
	float qty;
	String unit;
	float rate;
	float amount;
	public DailyItemSales() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DailyItemSales(int srno, long billNo, String itemName, float qty, String unit, float rate, float amount) {
		super();
		this.srno = srno;
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
		return "DailyItemSales [srno=" + srno + ", billNo=" + billNo + ", itemName=" + itemName + ", qty=" + qty
				+ ", unit=" + unit + ", rate=" + rate + ", amount=" + amount + "]";
	}
	
	
}
