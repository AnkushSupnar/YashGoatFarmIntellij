package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.LabourChargesDao;
import main.java.main.java.hibernate.dao.daoImpl.LabourChargesDaoImpl;
import main.java.main.java.hibernate.entities.LabourCharges;
import main.java.main.java.hibernate.service.service.LabourChargesService;

import java.time.LocalDate;
import java.util.List;

public class LabourChargesServiceImpl implements LabourChargesService {
	private LabourChargesDao dao;
	public LabourChargesServiceImpl() {
	this.dao = new LabourChargesDaoImpl();
}
	@Override
	public LabourCharges getLabourChargesById(long id) {
		return dao.getLabourChargesById(id);
	}

	@Override
	public List<LabourCharges> getLabourChargesByDate(LocalDate date) {
		return dao.getLabourChargesByDate(date);
	}

	@Override
	public List<LabourCharges> getAllLabourCharges() {
		return dao.getAllLabourCharges();
	}

	@Override
	public List<LabourCharges> getPeriodWiseLabourCharges(LocalDate start, LocalDate end) {
		return  dao.getPeriodWiseLabourCharges(start, end);
	}

	@Override
	public List<LabourCharges> getPeriodWiseLabourChargesByEmployee(LocalDate start, LocalDate end, long empid) {
		return dao.getPeriodWiseLabourChargesByEmployee(start,end,empid);
	}

	@Override
	public int saveLabourCharges(LabourCharges labourCharges) {
		return dao.saveLabourCharges(labourCharges);
	}

}
