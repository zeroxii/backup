package com.kakao2.test;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class TestAction extends ActionSupport{
	private Logger logger = Logger.getLogger(getClass());
	
	public TestAction() {
		// TODO Auto-generated constructor stub
		  
	}   
	public String execute(){
		logger.info("execute!");
		logger.info("test :: "+ getText("test.action.msg"));
		return SUCCESS; 
	}
	public void send(){
		try {
			logger.info("test ");			
			logger.info("test :: "+ getText("tt.tt"));
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestAction t = new TestAction();
		t.send();
	}
	

}
