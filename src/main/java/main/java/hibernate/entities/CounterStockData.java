package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="counterstockdata")
public class CounterStockData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String itemname;
	float qty;
	String unit;
	public CounterStockData() {
		super();	
	}
	public CounterStockData(String itemname, float qty, String unit) {
		super();
		this.itemname = itemname;
		this.qty = qty;
		this.unit = unit;
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
		return "CounterStockData [id=" + id + ", itemname=" + itemname + ", qty=" + qty + ", unit=" + unit + "]";
	}
	
	
}
