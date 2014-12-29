package com.brest.bank.domain;

import org.junit.Test;

import org.junit.Before;
import java.lang.Math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BankDepositTest{

	BankDeposit bankDeposit;
	
	//--- ������������� (�������������)
	@Before //--- ���������� ����� ������ ������
	public void setUp() throws Exception{
		bankDeposit = new BankDeposit();
	}
	
	//--- ������������
	@Test //--- ������������� ������
	public void testGetDepositId() throws Exception{
		Long idDeposit = (long)(Math.random()*10);
		bankDeposit.setDepositId(idDeposit);
		assertEquals(idDeposit, bankDeposit.getDepositId());
	}
	
	@Test //--- ������������ ������
	public void testGetDepositName() throws Exception{
		bankDeposit.setDepositName("Line growth");
		assertEquals("Line growth",bankDeposit.getDepositName());
	}
	
	@Test //--- ����������� ���� ������
	public void testGetDepositMinTerm() throws Exception{
		bankDeposit.setDepositMinTerm(10);
		assertEquals(10,bankDeposit.getDepositMinTerm());
	}
	
	@Test //--- ����������� ����� ������
	public void testGetDepositMinAmount() throws Exception{
		bankDeposit.setDepositMinAmount(1000000);
		assertEquals(1000000,bankDeposit.getDepositMinAmount());
	}
	
	@Test //--- ������ ������
	public void testGetDepositCurrency() throws Exception{
		bankDeposit.setDepositCurrency("eur");
		assertEquals("eur", bankDeposit.getDepositCurrency());
		
		bankDeposit.setDepositCurrency("usd");
		assertFalse("eur".equals(bankDeposit.getDepositCurrency()));
	}
	
	@Test //--- % ������
	public void testGetDepositInterestRate() throws Exception{
		bankDeposit.setDepositInterestRate(12);
		assertEquals(12,bankDeposit.getDepositInterestRate());
	}
	
	@Test //--- �������������� �������
	public void testGetDepositAddConditions() throws Exception{
		bankDeposit.setDepositAddConditions("additional conditions");
		assertEquals("additional conditions",bankDeposit.getDepositAddConditions());
	}
}