package main.java.main.java.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Employee {

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
	String post;
	int pin;
	String jobpost;
	float salary;
	String salarytime;
	String salarytype;
	LocalDate joiningdate;
	LocalDate exitdate;
	public Employee() {
		super();
	}
	public Employee(String fname, String mname, String lname, String mobileno, String altermobileno, String email,
			String address, String city, String taluka, String district, String post, int pin, String jobpost,
			float salary, String salarytime, String salarytype, LocalDate joiningdate, LocalDate exitdate) {
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
		this.post = post;
		this.pin = pin;
		this.jobpost = jobpost;
		this.salary = salary;
		this.salarytime = salarytime;
		this.salarytype = salarytype;
		this.joiningdate = joiningdate;
		this.exitdate = exitdate;
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
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public String getJobpost() {
		return jobpost;
	}
	public void setJobpost(String jobpost) {
		this.jobpost = jobpost;
	}
	public float getSalary() {
		return salary;
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}
	public String getSalarytime() {
		return salarytime;
	}
	public void setSalarytime(String salarytime) {
		this.salarytime = salarytime;
	}
	public String getSalarytype() {
		return salarytype;
	}
	public void setSalarytype(String salarytype) {
		this.salarytype = salarytype;
	}
	public LocalDate getJoiningdate() {
		return joiningdate;
	}
	public void setJoiningdate(LocalDate joiningdate) {
		this.joiningdate = joiningdate;
	}
	public LocalDate getExitdate() {
		return exitdate;
	}
	public void setExitdate(LocalDate exitdate) {
		this.exitdate = exitdate;
	}
	public String getFullName()
	{
		return fname+" "+mname+" "+lname;
	}
	@Override
	public String toString() {
		return "Employee [id=" + id + ", fname=" + fname + ", mname=" + mname + ", lname=" + lname + ", mobileno="
				+ mobileno + ", altermobileno=" + altermobileno + ", email=" + email + ", address=" + address
				+ ", city=" + city + ", taluka=" + taluka + ", district=" + district + ", post=" + post + ", pin=" + pin
				+ ", jobpost=" + jobpost + ", salary=" + salary + ", salarytime=" + salarytime + ", salarytype="
				+ salarytype + ", joiningdate=" + joiningdate + ", exitdate=" + exitdate + "]";
	}
	public String toString2() {
		return  id + "|" + fname + "|" + mname + "|" + lname + "|"
				+ mobileno + "|" + altermobileno + "|" + email + "|" + address
				+ "|" + city + "|" + taluka + "|" + district + "|" + post + "|" + pin
				+ "|" + jobpost + "|" + salary + "|" + salarytime + "|"
				+ salarytype + "|" + joiningdate + "|" + exitdate ;
	}
				
}
