package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class CustomerStatement {
    int id;
    LocalDate date;
    String particulars;
    float debit;
    float credit;
    float balance;

    public CustomerStatement() {
        super();
    }

    public CustomerStatement(int id, LocalDate date, String particulars, float debit, float credit,float balance) {
        this.id = id;
        this.date = date;
        this.particulars = particulars;
        this.debit = debit;
        this.credit = credit;
        this.balance=balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "CustomerStatement{" +
                "id=" + id +
                ", date=" + date +
                ", particulars='" + particulars + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                ", balance=" + balance +
                '}';
    }
}
