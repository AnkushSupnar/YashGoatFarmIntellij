package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String fname;
	String mname;
	String lname;
	String mobileno;
	String altermobileno;
	String email;
	String address;
	String city;
	String taluka;
	String district;
	String state;
	int pin;
	public Customer() {
		super();
	}
	public Customer(String fname, String mname, String lname, String mobileno, String altermobileno, String email,
			String address, String city, String taluka, String district, String state, int pin) {
		super();
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
		this.mobileno = mobileno;
		this.altermobileno = altermobileno;
		this.email = email;
		this.address = address;
		this.city = city;
		this.taluka = taluka;
		this.district = district;
		this.state = state;
		this.pin = pin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getAltermobileno() {
		return altermobileno;
	}
	public void setAltermobileno(String altermobileno) {
		this.altermobileno = altermobileno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getTaluka() {
		return taluka;
	}
	public void setTaluka(String taluka) {
		this.taluka = taluka;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", fname=" + fname + ", mname=" + mname + ", lname=" + lname + ", mobileno="
				+ mobileno + ", altermobileno=" + altermobileno + ", email=" + email + ", address=" + address
				+ ", city=" + city + ", taluka=" + taluka + ", district=" + district + ", state=" + state + ", pin="
				+ pin + "]";
	}
	public String toString2() {
		return  id + "|" + fname + "|" + mname + "|" + lname + "|"
				+ mobileno + "|" + altermobileno + "|" + email + "|" + address
				+ "|" + city + "|" + taluka + "|" + district + "|" + state + "|"
				+ pin;
	}
	
}
