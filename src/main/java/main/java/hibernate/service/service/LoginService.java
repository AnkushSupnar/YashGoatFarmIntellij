package main.java.main.java.hibernate.service.service;

import main.java.main.java.hibernate.entities.Login;

import java.util.List;

public interface LoginService {
	public Login getLoginById(int id);
	public Login getLoginByName(String name);
	public List<Login> getAllLogin();
	public List<String> getAllUserNames();
	
	public int saveLogin(Login login);
	public int validateLogin(String username,String Password);
	public int changeLoginStatus(String Status,int id);

}
