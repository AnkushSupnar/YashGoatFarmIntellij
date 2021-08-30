package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class CompanyDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String name;
	String address;
	String city;
	String taluka;
	String district;
	String pin;
	String contact;
	String altercontact;
	String email;
	String gst;
	LocalDate date;
	public CompanyDetails() {
		super();
	}
	public CompanyDetails(String name, String address, String city, String taluka, String district, String pin,
			String contact, String altercontact, String email, String gst, LocalDate date) {
		super();
		this.name = name;
		this.address = address;
		this.city = city;
		this.taluka = taluka;
		this.district = district;
		this.pin = pin;
		this.contact = contact;
		this.altercontact = altercontact;
		this.email = email;
		this.gst = gst;
		this.date = date;
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
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAltercontact() {
		return altercontact;
	}
	public void setAltercontact(String altercontact) {
		this.altercontact = altercontact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGst() {
		return gst;
	}
	public void setGst(String gst) {
		this.gst = gst;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "CompanyDetails [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", taluka="
				+ taluka + ", district=" + district + ", pin=" + pin + ", contact=" + contact + ", altercontact="
				+ altercontact + ", email=" + email + ", gst=" + gst + ", date=" + date + "]";
	}
	public String toString2() {
		return  id + "|" + name + "|" + address + "|" + city + "|"
				+ taluka + "|" + district + "|" + pin + "|" + contact + "|"
				+ altercontact + "|" + email + "|" + gst + "|" + date;
	}
	
	
}
