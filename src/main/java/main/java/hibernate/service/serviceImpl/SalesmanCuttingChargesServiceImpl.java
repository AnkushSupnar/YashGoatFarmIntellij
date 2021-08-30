package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.SalesmanCuttingChargesDao;
import main.java.main.java.hibernate.dao.daoImpl.SalemanCuttingChargesDaoImpl;
import main.java.main.java.hibernate.entities.SalesmanCuttingCharges;
import main.java.main.java.hibernate.service.service.SalesmanCuttingChargesService;

import java.time.LocalDate;
import java.util.List;

public class SalesmanCuttingChargesServiceImpl implements SalesmanCuttingChargesService {

	private SalesmanCuttingChargesDao dao;
	public SalesmanCuttingChargesServiceImpl() {
		dao = new SalemanCuttingChargesDaoImpl();
	}
	@Override
	public SalesmanCuttingCharges getSalesmanCuttingChargesById(long id) {
		return dao.getSalesmanCuttingChargesById(id);
	}

	@Override
	public List<SalesmanCuttingCharges> getSalesmanCuttingChargesBySalesman(String salesman) {
		return dao.getSalesmanCuttingChargesBySalesman(salesman);
	}

	@Override
	public List<SalesmanCuttingCharges> getAllSalesmanCuttingCharges() {
		return dao.getAllSalesmanCuttingCharges();
	}

	@Override
	public List<SalesmanCuttingCharges> getPeriodSalesmanCuttingCharges(LocalDate satrt, LocalDate end) {
		return dao.getPeriodSalesmanCuttingCharges(satrt, end);
	}

	@Override
	public int saveSalesmanCuttingCharges(SalesmanCuttingCharges salemanCuttingCharges) {
		return dao.saveSalesmanCuttingCharges(salemanCuttingCharges);
	}

	@Override
	public void deleteSalesmanCuttingChargesTransaction(long id) {
		dao.deleteSalesmanCuttingChargesTransaction(id);

	}

	@Override
	public List<SalesmanCuttingCharges> getDateWiseCharges(LocalDate date) {
		return dao.getDateWiseCharges(date);
	}
	@Override
	public int checkPaidCuttingCharges(long orderId) {
		return dao.checkPaidCuttingCharges(orderId);
	}

}
