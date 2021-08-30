package main.java.main.java.hibernate.service.serviceImpl;

import main.java.main.java.hibernate.dao.dao.LoginDao;
import main.java.main.java.hibernate.dao.daoImpl.LoginDaoImpl;
import main.java.main.java.hibernate.entities.Login;
import main.java.main.java.hibernate.service.service.LoginService;

import java.util.List;

public class LoginServiceImpl implements LoginService {

	LoginDao dao;
	public LoginServiceImpl() {
		this.dao = new LoginDaoImpl();
	}
	@Override
	public Login getLoginById(int id) {
		return dao.getLoginById(id);
	}

	@Override
	public Login getLoginByName(String name) {
		return dao.getLoginByName(name);
	}

	@Override
	public List<Login> getAllLogin() {
		return dao.getAllLogin();
	}

	@Override
	public List<String> getAllUserNames() {
		return dao.getAllUserNames();
	}

	@Override
	public int saveLogin(Login login) {
		return dao.saveLogin(login);
	}

	@Override
	public int validateLogin(String username, String Password) {
		return dao.validateLogin(username, Password);
	}

	@Override
	public int changeLoginStatus(String Status, int id) {
		return dao.changeLoginStatus(Status, id);
	}

}
