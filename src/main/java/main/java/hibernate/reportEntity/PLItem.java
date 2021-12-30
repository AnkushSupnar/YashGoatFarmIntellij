package main.java.main.java.hibernate.reportEntity;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PLItem {
    int id;
    String itemname,unit;
    float soldqty,soldrate,soldamt,purchaseamt,margin;
    double purchaserate;
    NumberFormat formatter = new DecimalFormat("#0.00");
    public PLItem() {
        super();
    }

    public PLItem(int id, String itemname, String unit, float soldqty,
                   float soldrate, float soldamt, double purchaserate,
                   float purchaseamt) {
        this.id=id;
        this.itemname = itemname;
        this.unit = unit;
        this.soldqty = soldqty;
        this.soldrate = soldrate;
        this.soldamt = soldamt;
        this.purchaserate = purchaserate;
        this.purchaseamt = purchaseamt;
        this.margin = this.soldamt-this.purchaseamt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getSoldqty() {
        return Float.parseFloat(formatter.format(soldqty));
    }

    public void setSoldqty(float soldqty) {
        this.soldqty = soldqty;
    }

    public double getSoldrate() {
        return Double.parseDouble(formatter.format(soldrate));
    }

    public void setSoldrate(float soldrate) {
        this.soldrate = soldrate;
    }

    public float getSoldamt() {
        return Float.parseFloat(formatter.format(soldamt));
    }

    public void setSoldamt(float soldamt) {
        this.soldamt = soldamt;
    }

    public double getPurchaserate() {
        return Double.parseDouble(formatter.format(purchaserate));
    }

    public void setPurchaserate(double purchaserate) {
        this.purchaserate = purchaserate;
    }

    public float getPurchaseamt() {
        return Float.parseFloat(formatter.format(purchaseamt));
    }

    public void setPurchaseamt(float purchaseamt) {
        this.purchaseamt = purchaseamt;
    }

    public float getMargin() {
        return Float.parseFloat(formatter.format(margin));
    }

    public void setMargin(float margin) {
        this.margin = soldamt-purchaseamt;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "PLItem{" +
                "id=" + id +
                ", itemname='" + itemname + '\'' +
                ", unit='" + unit + '\'' +
                ", soldqty=" + soldqty +
                ", soldrate=" + soldrate +
                ", soldamt=" + soldamt +
                ", purchaserate=" + purchaserate +
                ", purchaseamt=" + purchaseamt +
                ", margin=" + margin +
                '}';
    }
}
