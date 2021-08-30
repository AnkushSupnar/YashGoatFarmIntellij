package main.java.main.java.hibernate.reportEntity;

import java.util.ArrayList;
import java.util.List;

public class CuttingTransactionPojo {

	int id;
	String itemName;
	float quantity;
	List<CuttingLabourPojo> labourList = new ArrayList<CuttingLabourPojo>();
	public CuttingTransactionPojo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CuttingTransactionPojo(String itemName, float quantity, List<CuttingLabourPojo> labourList) {
		super();
		this.itemName = itemName;
		this.quantity = quantity;
		this.labourList = labourList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public List<CuttingLabourPojo> getLabourList() {
		return labourList;
	}
	public void setLabourList(List<CuttingLabourPojo> labourList) {
		this.labourList = labourList;
	}
	@Override
	public String toString() {
		return "CuttingTransactionPojo [id=" + id + ", itemName=" + itemName + ", quantity=" + quantity
				+ ", labourList=" + labourList + "]";
	}
		
}
