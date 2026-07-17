package main.java.main.java.hibernate.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Quotation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "customerid")
	private Customer customer;

	private LocalDate date;
	private LocalDate validUntil;

	private float nettotal;
	private float transportingchrges;
	private float otherchargs;
	private float igstTotal;

	@ManyToOne
	@JoinColumn(name = "bankid")
	private Bank bank;

	@ManyToOne
	@JoinColumn(name = "employeeid")
	private Employee employee;

	private String notes;
	private String status;
	private boolean billed;

	@OneToMany(mappedBy = "quotation", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<QuotationTransaction> transaction;

	public Quotation() { super(); }

	public Quotation(Customer customer, LocalDate date, LocalDate validUntil,
			float nettotal, float transportingchrges, float otherchargs,
			Bank bank, Employee employee, String notes, String status,
			List<QuotationTransaction> transaction) {
		super();
		this.customer = customer;
		this.date = date;
		this.validUntil = validUntil;
		this.nettotal = nettotal;
		this.transportingchrges = transportingchrges;
		this.otherchargs = otherchargs;
		this.bank = bank;
		this.employee = employee;
		this.notes = notes;
		this.status = status;
		this.transaction = transaction;
	}

	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }
	public LocalDate getDate() { return date; }
	public void setDate(LocalDate date) { this.date = date; }
	public LocalDate getValidUntil() { return validUntil; }
	public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
	public float getNettotal() { return nettotal; }
	public void setNettotal(float nettotal) { this.nettotal = nettotal; }
	public float getTransportingchrges() { return transportingchrges; }
	public void setTransportingchrges(float transportingchrges) { this.transportingchrges = transportingchrges; }
	public float getOtherchargs() { return otherchargs; }
	public void setOtherchargs(float otherchargs) { this.otherchargs = otherchargs; }
	public float getIgstTotal() { return igstTotal; }
	public void setIgstTotal(float igstTotal) { this.igstTotal = igstTotal; }
	public Bank getBank() { return bank; }
	public void setBank(Bank bank) { this.bank = bank; }
	public Employee getEmployee() { return employee; }
	public void setEmployee(Employee employee) { this.employee = employee; }
	public String getNotes() { return notes; }
	public void setNotes(String notes) { this.notes = notes; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public boolean isBilled() { return billed; }
	public void setBilled(boolean billed) { this.billed = billed; }
	public List<QuotationTransaction> getTransaction() { return transaction; }
	public void setTransaction(List<QuotationTransaction> transaction) { this.transaction = transaction; }
}
