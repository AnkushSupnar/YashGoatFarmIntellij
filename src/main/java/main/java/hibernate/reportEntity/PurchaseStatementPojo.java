package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class PurchaseStatementPojo {
	int id;
	String particulars;
	float debit,credit,balance;
	LocalDate date;
	long billno;
	public PurchaseStatementPojo() {
		super();
	}
	public PurchaseStatementPojo(int id, String particulars, float debit, float credit, float balance,
			LocalDate date, long billno) {
		super();
		this.id = id;
		this.particulars = particulars;
		this.debit = debit;
		this.credit = credit;
		this.balance = balance;
		this.date = date;
		this.billno = billno;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public float getDebit() {
		return debit;
	}
	public void setDebit(float debit) {
		this.debit = debit;
	}
	public float getCredit() {
		return credit;
	}
	public void setCredit(float credit) {
		this.credit = credit;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public long getBillno() {
		return billno;
	}
	public void setBillno(long billno) {
		this.billno = billno;
	}
	@Override
	public String toString() {
		return "PurchaseStatementPojo [id=" + id + ", particulars=" + particulars + ", debit=" + debit + ", credit="
				+ credit + ", balance=" + balance + ", date=" + date + ", billno=" + billno + "]";
	}
	
}
