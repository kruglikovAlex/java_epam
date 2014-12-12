package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositDaoImplTest {
	
	@Autowired
	private BankDepositDao bankDepositDao;
	BankDeposit bankDeposit;
	List<BankDeposit> bankDeposits;
	int sizeBefore = 0;
	
	@Before
	public void setUp() throws Exception {
		bankDeposit = new BankDeposit();
		bankDeposits = bankDepositDao.getBankDeposits();
	}
	
	@Test
	public void testGetBankDeposits() {
		assertNotNull(bankDeposits);
		assertFalse(bankDeposits.isEmpty());
	}
	
	@Test
	public void testAddBankDeposit(){
		sizeBefore = bankDeposits.size();
		
		bankDeposit.setDepositName("depositName3");
		bankDeposit.setDepositMinTerm(6);
		bankDeposit.setDepositMinAmount(50);
		bankDeposit.setDepositCurrency("eur");
		bankDeposit.setDepositInterestRate(4);
		bankDeposit.setDepositAddConditions("condition1");
		
		bankDepositDao.addBankDeposit(bankDeposit);
		bankDeposits = bankDepositDao.getBankDeposits();
		
		assertEquals(sizeBefore,bankDeposits.size()-1);
	}
	
	@Test
	public void testRemoveBankDeposit() {
        assertNotNull(bankDeposits);
        assertFalse(bankDeposits.isEmpty());
        sizeBefore = bankDeposits.size();
        bankDepositDao.removeBankDeposit(bankDeposits.get((int) (Math.random() * sizeBefore)).getDepositId());
        bankDeposits = bankDepositDao.getBankDeposits();
        assertEquals(sizeBefore-1,bankDeposits.size());
	}
	
	@Test
	public void testGetBankDepositById(){
		sizeBefore = bankDeposits.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testid = bankDeposits.get(indexDep).getDepositId();
		
		bankDeposit.setDepositId(testid);
		
		bankDeposit.setDepositName(bankDeposits.get(indexDep).getDepositName());
		bankDeposit.setDepositMinTerm(bankDeposits.get(indexDep).getDepositMinTerm());
		bankDeposit.setDepositMinAmount(bankDeposits.get(indexDep).getDepositMinAmount());
		bankDeposit.setDepositCurrency(bankDeposits.get(indexDep).getDepositCurrency());
		bankDeposit.setDepositInterestRate(bankDeposits.get(indexDep).getDepositInterestRate());
		bankDeposit.setDepositAddConditions(bankDeposits.get(indexDep).getDepositAddConditions());
		// или так
		/*
		 * bankDeposit = bankDeposits.get(indexDep);
		 * bankDeposit.setDepositId(testid);
		 */
		assertEquals(bankDepositDao.getBankDepositById(testid).toString(),bankDeposit.toString());
	}
	
	@Test
	public void testGetBankDepositByName(){
		sizeBefore = bankDeposits.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		String testDepositName = bankDeposits.get(indexDep).getDepositName();
		
		bankDeposit.setDepositId(bankDeposits.get(indexDep).getDepositId());
		bankDeposit.setDepositMinTerm(bankDeposits.get(indexDep).getDepositMinTerm());
		bankDeposit.setDepositMinAmount(bankDeposits.get(indexDep).getDepositMinAmount());
		bankDeposit.setDepositCurrency(bankDeposits.get(indexDep).getDepositCurrency());
		bankDeposit.setDepositInterestRate(bankDeposits.get(indexDep).getDepositInterestRate());
		bankDeposit.setDepositAddConditions(bankDeposits.get(indexDep).getDepositAddConditions());
		
		bankDeposit.setDepositName(testDepositName);
		// или так
		/*
		 * bankDeposit = bankDeposits.get(indexDep);
		 * bankDeposit.setDepositName(tesDepositName);
		 */	
		assertEquals(bankDepositDao.getBankDepositByName(testDepositName).toString(),bankDeposit.toString());
	}
	
	@Test
	public void testUpdateBankDeposit(){
		sizeBefore = bankDeposits.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testid = bankDeposits.get(indexDep).getDepositId();
		String testDeposit;
		
		bankDeposit = bankDeposits.get(indexDep);
		
		testDeposit = bankDeposit.toString();
		
		bankDeposit.setDepositName("UpdateDepositName");
		bankDeposit.setDepositMinTerm(4);
		bankDeposit.setDepositMinAmount(10);
		bankDeposit.setDepositCurrency("grb");
		bankDeposit.setDepositInterestRate(3);
		bankDeposit.setDepositAddConditions("UpdateConditions");
		
		bankDepositDao.updateBankDeposit(bankDeposit);
		
		assertNotEquals(bankDepositDao.getBankDepositById(testid).toString(),testDeposit);
		assertEquals(bankDepositDao.getBankDepositById(testid).toString(),bankDeposit.toString());
	}
}