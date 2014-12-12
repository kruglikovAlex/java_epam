package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Collection;
import java.text.SimpleDateFormat;

@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositParameterizedTest {

	@Autowired
	private BankDepositDao bankDepositDao;
	private BankDeposit bankDeposit;

	public BankDepositParameterizedTest(BankDeposit bankDeposit) {
		this.bankDeposit = bankDeposit;
	}
	
	@Before
	public void setUp() throws Exception {
		TestContextManager testContextManager = new TestContextManager(getClass());
		testContextManager.prepareTestInstance(this);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test() {
		bankDepositDao.addBankDeposit(bankDeposit);
	}
	
	@Parameterized.Parameters
	public static Collection data() throws ParseException{
		Object[][] params = new Object[][] {
				{null},
				{new BankDeposit()},
				{new BankDeposit(14L, "", 10, 10, "", 10, "")}
		};
		return Arrays.asList(params);
	}
}