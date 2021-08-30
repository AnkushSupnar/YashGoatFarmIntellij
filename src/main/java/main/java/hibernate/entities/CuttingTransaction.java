package main.java.main.java.hibernate.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class CuttingTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@ManyToOne
	Item item;
	
	float quantity;
	
	@ManyToOne
	@JoinColumn(name="cuttingOrderId")
	CuttingOrder cuttingOrder;
	
	@OneToMany(mappedBy = "transaction",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	List<CuttingLabour> labourList;
	public CuttingTransaction() {
		super();
	}
	public CuttingTransaction(Item item,
			float quantity,
			CuttingOrder cuttingOrder, 
			List<CuttingLabour> labourList) {
		super();
		this.item = item;
		this.quantity = quantity;
		this.cuttingOrder = cuttingOrder;
		this.labourList = labourList;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public float getQuantity() {
		return quantity;
	}
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}
	public CuttingOrder getCuttingOrder() {
		return cuttingOrder;
	}
	public void setCuttingOrder(CuttingOrder cuttingOrder) {
		this.cuttingOrder = cuttingOrder;
	}
	public List<CuttingLabour> getLabourList() {
		return labourList;
	}
	public void setLabourList(List<CuttingLabour> labourList) {
		this.labourList = labourList;
	}
	@Override
	public String toString() {
		return "CuttingTransaction [id=" + id + ", item=" + item + ", quantity=" + quantity + ", cuttingOrder="
				+ cuttingOrder + ", labourList=" + labourList + "]";
	}

	
	
}
