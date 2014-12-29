package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;

import com.brest.bank.domain.BankDepositor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositDaoImplTest {
	
	@Autowired
	private BankDepositDao bankDepositDao;

	@Autowired
	private BankDepositorDao bankDepositorDao;

	BankDeposit bankDeposit;
	List<BankDeposit> bankDeposits;

	int sizeBefore = 0;
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
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
	public void testGetBankDepositsAllDepositors() {
		List<BankDeposit> deposits = bankDepositDao.getBankDeposits();
		assertNotNull(deposits);
		assertFalse(deposits.isEmpty());

		List<Map> list = bankDepositDao.getBankDepositsAllDepositors();
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==deposits.size());

		for (int i=0; i<list.size(); i++) {
			assertEquals(deposits.get(i).getDepositId(), list.get(i).get("depositId"));
			assertEquals(deposits.get(i).getDepositName(), list.get(i).get("depositName"));
			assertEquals(deposits.get(i).getDepositMinTerm(), list.get(i).get("depositMinTerm"));
			assertEquals(deposits.get(i).getDepositMinAmount(), list.get(i).get("depositMinAmount"));
			assertEquals(deposits.get(i).getDepositCurrency(), list.get(i).get("depositCurrency"));
			assertEquals(deposits.get(i).getDepositInterestRate(), list.get(i).get("depositInterestRate"));
			assertEquals(deposits.get(i).getDepositAddConditions(), list.get(i).get("depositAddConditions"));
		}
	}

	@Test
	public void testGetBankDepositsAllDepositorsBetweenDateDeposit() throws ParseException{
		List<BankDeposit> deposits = bankDepositDao.getBankDeposits();
		assertNotNull(deposits);
		assertFalse(deposits.isEmpty());

		Date start = dateFormat.parse("2014-12-01");
		Date end = dateFormat.parse("2014-12-02");
		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorBetweenDateDeposit(start, end);
		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());

		List<Map> list = bankDepositDao.getBankDepositsAllDepositorsBetweenDateDeposit(start, end);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==deposits.size());

		int count=0;
		for (int i=0; i<list.size(); i++) {
			assertEquals(deposits.get(i).getDepositId(), list.get(i).get("depositId"));
			assertEquals(deposits.get(i).getDepositName(), list.get(i).get("depositName"));
			assertEquals(deposits.get(i).getDepositMinTerm(), list.get(i).get("depositMinTerm"));
			assertEquals(deposits.get(i).getDepositMinAmount(), list.get(i).get("depositMinAmount"));
			assertEquals(deposits.get(i).getDepositCurrency(), list.get(i).get("depositCurrency"));
			assertEquals(deposits.get(i).getDepositInterestRate(), list.get(i).get("depositInterestRate"));
			assertEquals(deposits.get(i).getDepositAddConditions(), list.get(i).get("depositAddConditions"));
			count += (Integer)list.get(i).get("numDepositors");
		}

		assertTrue(count==depositors.size());
	}

	@Test
	public void testGetBankDepositsAllDepositorsBetweenDateReturnDeposit() throws ParseException{
		List<BankDeposit> deposits = bankDepositDao.getBankDeposits();
		assertNotNull(deposits);
		assertFalse(deposits.isEmpty());

		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-04");
		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorBetweenDateReturnDeposit(start, end);
		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());

		List<Map> list = bankDepositDao.getBankDepositsAllDepositorsBetweenDateReturnDeposit(start, end);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==deposits.size());

		int count=0;
		for (int i=0; i<list.size(); i++) {
			assertEquals(deposits.get(i).getDepositId(), list.get(i).get("depositId"));
			assertEquals(deposits.get(i).getDepositName(), list.get(i).get("depositName"));
			assertEquals(deposits.get(i).getDepositMinTerm(), list.get(i).get("depositMinTerm"));
			assertEquals(deposits.get(i).getDepositMinAmount(), list.get(i).get("depositMinAmount"));
			assertEquals(deposits.get(i).getDepositCurrency(), list.get(i).get("depositCurrency"));
			assertEquals(deposits.get(i).getDepositInterestRate(), list.get(i).get("depositInterestRate"));
			assertEquals(deposits.get(i).getDepositAddConditions(), list.get(i).get("depositAddConditions"));
			count += (Integer)list.get(i).get("numDepositors");
		}

		assertTrue(count==depositors.size());
	}

	@Test
	public void testGetBankDepositByIdAllDepositors(){
		sizeBefore = bankDeposits.size();
		int indexDep = sizeBefore-1;
		Long testid = bankDeposits.get(indexDep).getDepositId();

		bankDeposit = bankDepositDao.getBankDepositById(testid);
		List<Map> list = bankDepositDao.getBankDepositByIdAllDepositors(testid);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==1);

		assertEquals(bankDeposit.getDepositId(), list.get(0).get("depositId"));
		assertEquals(bankDeposit.getDepositName(), list.get(0).get("depositName"));
		assertEquals(bankDeposit.getDepositMinTerm(), list.get(0).get("depositMinTerm"));
		assertEquals(bankDeposit.getDepositMinAmount(), list.get(0).get("depositMinAmount"));
		assertEquals(bankDeposit.getDepositCurrency(), list.get(0).get("depositCurrency"));
		assertEquals(bankDeposit.getDepositInterestRate(), list.get(0).get("depositInterestRate"));
		assertEquals(bankDeposit.getDepositAddConditions(), list.get(0).get("depositAddConditions"));
	}

	@Test
	public void testGetBankDepositByIdWithAllDepositorsBetweenDateDeposit() throws ParseException{
		sizeBefore = bankDeposits.size();
		int indexDep = sizeBefore-1;
		Long testId = bankDeposits.get(indexDep).getDepositId();

		bankDeposit = bankDepositDao.getBankDepositById(testId);

		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-04");

		List<Map> list = bankDepositDao.getBankDepositByIdWithAllDepositorsBetweenDateDeposit(testId, start,end);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==1);

		assertEquals(bankDeposit.getDepositId(), list.get(0).get("depositId"));
		assertEquals(bankDeposit.getDepositName(), list.get(0).get("depositName"));
		assertEquals(bankDeposit.getDepositMinTerm(), list.get(0).get("depositMinTerm"));
		assertEquals(bankDeposit.getDepositMinAmount(), list.get(0).get("depositMinAmount"));
		assertEquals(bankDeposit.getDepositCurrency(), list.get(0).get("depositCurrency"));
		assertEquals(bankDeposit.getDepositInterestRate(), list.get(0).get("depositInterestRate"));
		assertEquals(bankDeposit.getDepositAddConditions(), list.get(0).get("depositAddConditions"));
	}

	@Test
	public void testGetBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit() throws ParseException{
		sizeBefore = bankDeposits.size();
		int indexDep = sizeBefore-1;
		Long testId = bankDeposits.get(indexDep).getDepositId();

		bankDeposit = bankDepositDao.getBankDepositById(testId);

		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-04");

		List<Map> list = bankDepositDao.getBankDepositByIdWithAllDepositorsBetweenDateReturnDeposit(testId, start,end);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==1);

		assertEquals(bankDeposit.getDepositId(), list.get(0).get("depositId"));
		assertEquals(bankDeposit.getDepositName(), list.get(0).get("depositName"));
		assertEquals(bankDeposit.getDepositMinTerm(), list.get(0).get("depositMinTerm"));
		assertEquals(bankDeposit.getDepositMinAmount(), list.get(0).get("depositMinAmount"));
		assertEquals(bankDeposit.getDepositCurrency(), list.get(0).get("depositCurrency"));
		assertEquals(bankDeposit.getDepositInterestRate(), list.get(0).get("depositInterestRate"));
		assertEquals(bankDeposit.getDepositAddConditions(), list.get(0).get("depositAddConditions"));
	}

	@Test
	public void testGetBankDepositByNameAllDepositors(){
		sizeBefore = bankDeposits.size();
		int indexDep = sizeBefore-1;
		String testName = bankDeposits.get(indexDep).getDepositName();

		bankDeposit = bankDepositDao.getBankDepositByName(testName);
		List<Map> list = bankDepositDao.getBankDepositByNameAllDepositors(testName);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==1);

		assertEquals(bankDeposit.getDepositId(), list.get(0).get("depositId"));
		assertEquals(bankDeposit.getDepositName(), list.get(0).get("depositName"));
		assertEquals(bankDeposit.getDepositMinTerm(), list.get(0).get("depositMinTerm"));
		assertEquals(bankDeposit.getDepositMinAmount(), list.get(0).get("depositMinAmount"));
		assertEquals(bankDeposit.getDepositCurrency(), list.get(0).get("depositCurrency"));
		assertEquals(bankDeposit.getDepositInterestRate(), list.get(0).get("depositInterestRate"));
		assertEquals(bankDeposit.getDepositAddConditions(), list.get(0).get("depositAddConditions"));
	}

	@Test
	public void testGetBankDepositByNameWithAllDepositorsBetweenDateDeposit() throws ParseException{
		sizeBefore = bankDeposits.size();
		int indexDep = sizeBefore-1;
		String testName = bankDeposits.get(indexDep).getDepositName();

		bankDeposit = bankDepositDao.getBankDepositByName(testName);

		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-04");

		List<Map> list = bankDepositDao.getBankDepositByNameWithAllDepositorsBetweenDateDeposit(testName, start,end);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==1);

		assertEquals(bankDeposit.getDepositId(), list.get(0).get("depositId"));
		assertEquals(bankDeposit.getDepositName(), list.get(0).get("depositName"));
		assertEquals(bankDeposit.getDepositMinTerm(), list.get(0).get("depositMinTerm"));
		assertEquals(bankDeposit.getDepositMinAmount(), list.get(0).get("depositMinAmount"));
		assertEquals(bankDeposit.getDepositCurrency(), list.get(0).get("depositCurrency"));
		assertEquals(bankDeposit.getDepositInterestRate(), list.get(0).get("depositInterestRate"));
		assertEquals(bankDeposit.getDepositAddConditions(), list.get(0).get("depositAddConditions"));
	}

	@Test
	public void testGetBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit() throws ParseException{
		sizeBefore = bankDeposits.size();
		int indexDep = sizeBefore-1;
		String testName = bankDeposits.get(indexDep).getDepositName();

		bankDeposit = bankDepositDao.getBankDepositByName(testName);

		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-04");

		List<Map> list = bankDepositDao.getBankDepositByNameWithAllDepositorsBetweenDateReturnDeposit(testName, start,end);
		assertNotNull(list);
		assertFalse(list.isEmpty());

		assertTrue(list.size()==1);

		assertEquals(bankDeposit.getDepositId(), list.get(0).get("depositId"));
		assertEquals(bankDeposit.getDepositName(), list.get(0).get("depositName"));
		assertEquals(bankDeposit.getDepositMinTerm(), list.get(0).get("depositMinTerm"));
		assertEquals(bankDeposit.getDepositMinAmount(), list.get(0).get("depositMinAmount"));
		assertEquals(bankDeposit.getDepositCurrency(), list.get(0).get("depositCurrency"));
		assertEquals(bankDeposit.getDepositInterestRate(), list.get(0).get("depositInterestRate"));
		assertEquals(bankDeposit.getDepositAddConditions(), list.get(0).get("depositAddConditions"));
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