package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositorParameterizedTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private BankDepositorDao bankDepositorDao;
	private BankDepositor bankDepositor;
	public BankDepositorParameterizedTest(BankDepositor bankDepositor) {
		this.bankDepositor = bankDepositor;
	}
	
	@Before
	public void setUp() throws Exception {
		TestContextManager testContextManager = new TestContextManager(getClass());
		testContextManager.prepareTestInstance(this);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test() {
		bankDepositorDao.addBankDepositor(bankDepositor);
	}
	
	@Parameterized.Parameters
	public static Collection data() throws ParseException{
		Object[][] params = new Object[][] {
				{null},
				{new BankDepositor()},
				{new BankDepositor(12L, "", 10L, dateFormat.parse("2014-12-01"), 0, 0, 0, dateFormat.parse("2014-12-01"), 0)}
		};
		return Arrays.asList(params);
	}
}