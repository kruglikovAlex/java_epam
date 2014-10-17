package com.epam.brest.courses.domain;

public class User{
	private long userId;
	private String login;
	private String userName;

	//-----------
	public long getUserId(){
		return userId;
	}

	public void setUserId(long userId){
		this.userId = userId;	
	}

	//-----------
	public String getLogin(){
		return login;	
	}

	public void setLogin(String login){
		this.login = login;
	}

	//------------
	public String getUserName(){
		return userName;	
	}

	public void setUserName(String userName){
		this.userName = userName;	
	}
}
