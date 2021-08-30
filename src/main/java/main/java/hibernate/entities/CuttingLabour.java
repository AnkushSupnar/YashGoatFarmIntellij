package main.java.main.java.hibernate.entities;

import javax.persistence.*;
@Entity
public class CuttingLabour {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	long id;
	
	@ManyToOne
	@JoinColumn(name="labourId")
	Employee labour;
	
	float quantity;
	
	@ManyToOne
	@JoinColumn(name="cuttingTransactionId")
	CuttingTransaction transaction;
	
	float cuttingCharges;
	float paidCuttingCharges;
	
	public CuttingLabour() {
		super();
	}

	public CuttingLabour(Employee labour,
						 float quantity,
						 CuttingTransaction transaction,
						 float cuttingCharges,
						 float paidCuttingCharges) {
		super();
		this.labour = labour;
		this.quantity = quantity;
		this.transaction = transaction;
		this.cuttingCharges = cuttingCharges;
		this.paidCuttingCharges = paidCuttingCharges;
	}

	public float getCuttingCharges() {
		return cuttingCharges;
	}

	public void setCuttingCharges(float cuttingCharges) {
		this.cuttingCharges = cuttingCharges;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Employee getLabour() {
		return labour;
	}

	public void setLabour(Employee labour) {
		this.labour = labour;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public CuttingTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(CuttingTransaction transaction) {
		this.transaction = transaction;
	}

	public float getPaidCuttingCharges() {
		return paidCuttingCharges;
	}

	public void setPaidCuttingCharges(float paidCuttingCharges) {
		this.paidCuttingCharges = paidCuttingCharges;
	}

	@Override
	public String toString() {
		return "CuttingLabour [id=" + id + ", labour=" + labour + ", quantity=" + quantity + ", transaction="
				+ transaction + ", cuttingCharges=" + cuttingCharges + ", paidCuttingCharges=" + paidCuttingCharges
				+ "]";
	}

	

	
	
}
