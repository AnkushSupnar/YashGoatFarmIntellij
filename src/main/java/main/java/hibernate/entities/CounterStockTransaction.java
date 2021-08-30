package main.java.main.java.hibernate.entities;

import javax.persistence.*;

@Entity
@Table(name="counterstocktransaction")
public class CounterStockTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	String itemname;
	float oldqty;
	float newqty;
	float totalqty;
	@ManyToOne
	@JoinColumn(name="counterstockid")
	CounterStock counterstock;
	public CounterStockTransaction() {
		super();		
	}
	public CounterStockTransaction(String itemname, 
			float oldqty,
			float newqty,
			float totalqty,
			CounterStock counterstock) {
		super();
		this.itemname = itemname;
		this.oldqty = oldqty;
		this.newqty = newqty;
		this.totalqty = totalqty;
		this.counterstock = counterstock;
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
	public float getOldqty() {
		return oldqty;
	}
	public void setOldqty(float oldqty) {
		this.oldqty = oldqty;
	}
	public float getNewqty() {
		return newqty;
	}
	public void setNewqty(float newqty) {
		this.newqty = newqty;
	}
	public float getTotalqty() {
		return totalqty;
	}
	public void setTotalqty(float totalqty) {
		this.totalqty = totalqty;
	}
	public CounterStock getCounterstock() {
		return counterstock;
	}
	public void setCounterstock(CounterStock counterstock) {
		this.counterstock = counterstock;
	}
	@Override
	public String toString() {
		return "CounterStockTransaction [id=" + id + ", itemname=" + itemname + ", oldqty=" + oldqty + ", newqty="
				+ newqty + ", totalqty=" + totalqty + ", counterstock=" + counterstock + "]";
	}
	
	
}

