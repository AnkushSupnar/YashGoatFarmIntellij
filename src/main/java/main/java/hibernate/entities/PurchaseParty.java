package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PurchaseParty {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String name;
	String address;
	String gst;
	String pan;
	public PurchaseParty() {
		super();
	}
	public PurchaseParty(String name, String address, String gst, String pan) {
		super();
		this.name = name;
		this.address = address;
		this.gst = gst;
		this.pan = pan;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGst() {
		return gst;
	}
	public void setGst(String gst) {
		this.gst = gst;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	@Override
	public String toString() {
		return "PurchaseParty [id=" + id + ", name=" + name + ", address=" + address + ", gst=" + gst + ", pan=" + pan
				+ "]";
	}
	public String toString2() {
		return  id + "|" + name + "|" + address + "|" + gst + "|" + pan;
	}
	
}
