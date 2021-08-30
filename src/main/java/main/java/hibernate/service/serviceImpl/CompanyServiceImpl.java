package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.CompanyDao;
import main.java.main.java.hibernate.dao.daoImpl.CompanyDaoImpl;
import main.java.main.java.hibernate.entities.CompanyDetails;
import main.java.main.java.hibernate.service.service.CompanyService;

import java.time.LocalDate;

public class CompanyServiceImpl implements CompanyService {

	private CompanyDao dao;
	public CompanyServiceImpl() {
		dao = new CompanyDaoImpl();
	}
	@Override
	public CompanyDetails getCompanyDetails(int id) {
		return dao.getCompanyDetails(id);
	}

	@Override
	public int saveCompany(CompanyDetails company) {
		return dao.saveCompany(company);
	}

	@Override
	public int checkLicense(LocalDate date) {
		return dao.checkLicense(date);		
	}
	@Override
	public CompanyDetails getCompanyByName(String name) {
		return dao.getCompanyByName(name);
	}

}
