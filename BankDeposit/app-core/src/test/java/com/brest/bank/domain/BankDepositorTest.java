package com.brest.bank.domain;

import org.junit.Test;

import org.junit.Before;
import org.junit.Test;
import java.lang.Math;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class BankDepositorTest {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	BankDepositor bankDepositor;
	
	@Before
	public void setUp() throws Exception{
		bankDepositor = new BankDepositor();
	}
	
	//--- ������������
	@Test //--- �������������
	public void testGetDepositorId() throws Exception{
		Long idDepositor = (long)(Math.random()*10);
		bankDepositor.setDepositorId(idDepositor);
		assertEquals(idDepositor,bankDepositor.getDepositorId());
	}

	@Test //--- ��� ���������
	public void testGetDepositorName() throws Exception{
		bankDepositor.setDepositorName("Ivan Ivanov");
		assertEquals("Ivan Ivanov",bankDepositor.getDepositorName());
	}
	
	@Test //--- ������������� ������
	public void testGetDepositorIdDeposit() throws Exception{
		Long idDeposit = (long)(Math.random()*10);
		bankDepositor.setDepositorIdDeposit(idDeposit);
		assertEquals(idDeposit,bankDepositor.getDepositorIdDeposit());
	}
	
	@Test //--- ���� ������
	public void testGetDepositorDateDeposit() throws ParseException{
		Date dateDeposit = dateFormat.parse("2014-12-01");
		
		bankDepositor.setDepositorDateDeposit(dateDeposit);
		assertEquals(dateDeposit,bankDepositor.getDepositorDateDeposit());
	}
	
	@Test //--- ����� ������
	public void testGetDepositorAmountDeposit() throws Exception{
		bankDepositor.setDepositorAmountDeposit(1000000);
		assertEquals(1000000, bankDepositor.getDepositorAmountDeposit());
	}
	
	@Test //--- ����� ���������� ������
	public void testGetDepositorAmountPlusDeposit() throws Exception{
		bankDepositor.setDepositorAmountPlusDeposit(1000000);
		assertEquals(1000000, bankDepositor.getDepositorAmountPlusDeposit());
	}
	
	@Test //--- ����� �������� ������
	public void testGetDepositorAmountMinusDeposit() throws Exception{
		bankDepositor.setDepositorAmountMinusDeposit(1000000);
		assertEquals(1000000, bankDepositor.getDepositorAmountMinusDeposit());
	}
	
	@Test //--- ���� �������� ������
	public void testGetDepositorDateReturnDeposit() throws ParseException{
		Date dateDeposit = dateFormat.parse("2015-12-01");
		bankDepositor.setDepositorDateReturnDeposit(dateDeposit);
		assertEquals(dateDeposit,bankDepositor.getDepositorDateReturnDeposit());
	}
	
	@Test //--- ������� � �������� ������
	public void testGetDepositorMarkReturnDeposit() throws Exception{
		bankDepositor.setDepositorMarkReturnDeposit(1);
		assertEquals(1,bankDepositor.getDepositorMarkReturnDeposit());
		
		//bankDepositor.setDepositorMarkReturnDeposit(0); ??????
		bankDepositor.setDepositorAmountDeposit(1000000);
		bankDepositor.setDepositorAmountMinusDeposit(1000000);
		bankDepositor.setDepositorMarkReturnDeposit();
		assertEquals(1,bankDepositor.getDepositorMarkReturnDeposit());
	}
}
