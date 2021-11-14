package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class PurchaseInvoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long billno;
	String invoiceNo;
	@ManyToOne
	@JoinColumn(name="partyid")
	PurchaseParty party;
	LocalDate date;
	float nettotal;
	float gst;
	float othercharges;
	float transportcharges;
	float grandtotal;
	float paid;

	@OneToMany(mappedBy = "invoice",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private List<PurchaseTransaction> transaction;
	@ManyToOne
	@JoinColumn(name="bankid")
	Bank bank;
	String bankreffno;
	public PurchaseInvoice() {
		super();
	}
	public PurchaseInvoice(String invoiceNo,
						   PurchaseParty party,
						   LocalDate date,
						   float nettotal,
						   float gst,
						   float othercharges,
						   float transportcharges,
						   float grandtotal,
						   List<PurchaseTransaction> transaction,
						   Bank bank,
						   String bankreffno,
						   float paid) {
		super();
		this.invoiceNo = invoiceNo;
		this.party = party;
		this.date = date;
		this.nettotal = nettotal;
		this.gst = gst;
		this.othercharges = othercharges;
		this.transportcharges = transportcharges;
		this.grandtotal = grandtotal;
		this.transaction = transaction;
		this.bank = bank;
		this.bankreffno = bankreffno;
		this.paid = paid;
	}
	public float getPaid() {
		return paid;
	}
	public void setPaid(float paid) {
		this.paid = paid;
	}
	public String getBankreffno() {
		return bankreffno;
	}
	public void setBankreffno(String bankreffno) {
		this.bankreffno = bankreffno;
	}
	public long getBillno() {
		return billno;
	}
	public void setBillno(long billno) {
		this.billno = billno;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public PurchaseParty getParty() {
		return party;
	}
	public void setParty(PurchaseParty party) {
		this.party = party;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public float getNettotal() {
		return nettotal;
	}
	public void setNettotal(float nettotal) {
		this.nettotal = nettotal;
	}
	public float getGst() {
		return gst;
	}
	public void setGst(float gst) {
		this.gst = gst;
	}
	public float getOthercharges() {
		return othercharges;
	}
	public void setOthercharges(float othercharges) {
		this.othercharges = othercharges;
	}
	public float getTransportcharges() {
		return transportcharges;
	}
	public void setTransportcharges(float transportcharges) {
		this.transportcharges = transportcharges;
	}
	public float getGrandtotal() {
		return grandtotal;
	}
	public void setGrandtotal(float grandtotal) {
		this.grandtotal = grandtotal;
	}
	public List<PurchaseTransaction> getTransaction() {
		return transaction;
	}
	public void setTransaction(List<PurchaseTransaction> transaction) {
		this.transaction = transaction;
	}
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	
	
	@Override
	public String toString() {
		return "PurchaseInvoice [billno=" + billno + ", invoiceNo=" + invoiceNo + ", party=" + party + ", date=" + date
				+ ", nettotal=" + nettotal + ", gst=" + gst + ", othercharges=" + othercharges + ", transportcharges="
				+ transportcharges + ", grandtotal=" + grandtotal + ", paid=" + paid + ", transaction=" + transaction
				+ ", bank=" + bank + ", bankreffno=" + bankreffno + "]";
	}
	public String toString2() {
		return  billno + "|" + invoiceNo + "|" + party + "|" + date
				+ "|" + nettotal + "|" + gst + "|" + othercharges + "|"
				+ transportcharges + "|" + grandtotal + "|" + paid + "|" + transaction
				+ "|" + bank + "|" + bankreffno;
	}
	
}
