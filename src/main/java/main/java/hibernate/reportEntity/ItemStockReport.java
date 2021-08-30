package main.java.main.java.hibernate.reportEntity;

public class ItemStockReport {

	long id;
	String itemName;
	float soldQty;
	float qty;
	String unit;
	public ItemStockReport() {
		super();		
	}
	public ItemStockReport(String itemName, float soldQty, float qty, String unit) {
		super();
		this.itemName = itemName;
		this.soldQty = soldQty;
		this.qty = qty;
		this.unit = unit;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public float getSoldQty() {
		return soldQty;
	}
	public void setSoldQty(float soldQty) {
		this.soldQty = soldQty;
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
	@Override
	public String toString() {
		return "ItemStockReport [id=" + id + ", itemName=" + itemName + ", soldQty=" + soldQty + ", qty=" + qty
				+ ", unit=" + unit + "]";
	}
	
}
