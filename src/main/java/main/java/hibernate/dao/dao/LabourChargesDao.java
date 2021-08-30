package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.LabourCharges;

import java.time.LocalDate;
import java.util.List;

public interface LabourChargesDao {

	public LabourCharges getLabourChargesById(long id);
	public List<LabourCharges> getLabourChargesByDate(LocalDate date);
	public List<LabourCharges>getAllLabourCharges();
	public List<LabourCharges>getPeriodWiseLabourCharges(LocalDate start,LocalDate end);
	public int saveLabourCharges(LabourCharges labourCharges);
	
}
