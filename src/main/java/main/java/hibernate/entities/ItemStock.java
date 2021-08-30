package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ItemStock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String itemname;
	String unit;
	float quantity;
	public ItemStock() {
		super();
	}
	public ItemStock(String itemname,String unit, float quantity) {
		super();
		this.itemname = itemname;
		this.unit=unit;
		this.quantity = quantity;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "ItemStock [id=" + id + ", itemname=" + itemname + ", unit=" + unit + ", quantity=" + quantity + "]";
	}
	public String toString2() {
		return  id + "|" + itemname + "|" + unit + "|" + quantity;
	}
	
}
