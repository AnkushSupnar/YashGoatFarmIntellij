package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class CommisionTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	LocalDate date;
	float totalCommision;
	float paidCommision;
	
	@ManyToOne
	@JoinColumn(name="commision")
	Commision commision;
	
	@OneToOne
	@JoinColumn(name="billNo")
	Bill bill;

	public CommisionTransaction() {
		super();
	}

	public CommisionTransaction( LocalDate date, float totalCommision, float paidCommision,
			Commision commision, Bill bill) {
		super();
		this.date = date;
		this.totalCommision = totalCommision;
		this.paidCommision = paidCommision;
		this.commision = commision;
		this.bill = bill;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public float getTotalCommision() {
		return totalCommision;
	}

	public void setTotalCommision(float totalCommision) {
		this.totalCommision = totalCommision;
	}

	public float getPaidCommision() {
		return paidCommision;
	}

	public void setPaidCommision(float paidCommision) {
		this.paidCommision = paidCommision;
	}

	public Commision getCommision() {
		return commision;
	}

	public void setCommision(Commision commision) {
		this.commision = commision;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

	@Override
	public String toString() {
		return "CommisionTransaction [id=" + id + ", date=" + date + ", totalCommision=" + totalCommision
				+ ", paidCommision=" + paidCommision + ", commision=" + commision + ", bill=" + bill + "]";
	}
	
	
	
}
