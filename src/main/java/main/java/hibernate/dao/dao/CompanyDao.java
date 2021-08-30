package main.java.main.java.hibernate.dao.dao;

import main.java.main.java.hibernate.entities.CompanyDetails;

import java.time.LocalDate;

public interface CompanyDao {

	public CompanyDetails getCompanyDetails(int id);
	public int saveCompany(CompanyDetails company);
	public int checkLicense(LocalDate date);
	public CompanyDetails getCompanyByName(String name);
}
