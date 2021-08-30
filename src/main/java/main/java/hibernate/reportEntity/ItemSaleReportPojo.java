package main.java.main.java.hibernate.reportEntity;

import java.time.LocalDate;

public class ItemSaleReportPojo {
    int id;
    long billno;
    LocalDate date;
    String itemName;
    String unit;
    float qty;
    float rate;
    float amount;

    public ItemSaleReportPojo() {
        super();
    }

    public ItemSaleReportPojo(int id, long billno, LocalDate date, String itemName, String unit, float qty, float rate, float amount) {
        this.id = id;
        this.billno = billno;
        this.date = date;
        this.itemName = itemName;
        this.unit = unit;
        this.qty = qty;
        this.rate = rate;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBillno() {
        return billno;
    }

    public void setBillno(long billno) {
        this.billno = billno;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "ItemSaleReportPojo{" +
                "id=" + id +
                ", billno=" + billno +
                ", date=" + date +
                ", itemName='" + itemName + '\'' +
                ", qty=" + qty +
                ", rate=" + rate +
                ", amount=" + amount +
                '}';
    }
}
