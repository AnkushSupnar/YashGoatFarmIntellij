package main.java.main.java.hibernate.reportEntity;

public class CuttingLabourPojo {

	int id;
	String name;
	float qty;
	public CuttingLabourPojo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CuttingLabourPojo(String name, float qty) {
		super();
		this.name = name;
		this.qty = qty;
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
	public float getQty() {
		return qty;
	}
	public void setQty(float qty) {
		this.qty = qty;
	}
	@Override
	public String toString() {
		return  name + ", qty=" + qty + "]";
	}
	
	
}
