package com.brest.bank.dao;

import com.brest.bank.domain.BankDepositor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.text.ParseException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositorDaoImplTest {
	
	@Autowired
	private BankDepositorDao bankDepositorDao;
	BankDepositor bankDepositor;
	List<BankDepositor> bankDepositors;
	int sizeBefore = 0;
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
	@Before
	public void setUp() throws Exception {
		bankDepositor = new BankDepositor();
		bankDepositors = bankDepositorDao.getBankDepositors();
	}

	@Test
	public void testGetBankDepositors() {
		assertNotNull(bankDepositors);
		assertFalse(bankDepositors.isEmpty());
	}
	
	@Test
	public void testAddBankDepositor() throws ParseException{
		sizeBefore = bankDepositors.size();
		
		bankDepositor.setDepositorName("depositorName3");
		bankDepositor.setDepositorIdDeposit(3L);
		bankDepositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
		bankDepositor.setDepositorAmountDeposit(100);
		bankDepositor.setDepositorAmountPlusDeposit(100);
		bankDepositor.setDepositorAmountMinusDeposit(0);
		bankDepositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-12-01"));
		bankDepositor.setDepositorMarkReturnDeposit();
				
		bankDepositorDao.addBankDepositor(bankDepositor);
		bankDepositors = bankDepositorDao.getBankDepositors();
		
		assertEquals(sizeBefore,bankDepositors.size()-1);
	}

    @Test
    public void testUpdateBankDepositor() throws ParseException{
        sizeBefore = bankDepositors.size();
        int indexDep = (int)(Math.random()*sizeBefore);
        Long testid = bankDepositors.get(indexDep).getDepositorId();
        String testDepositor;

        bankDepositor = bankDepositors.get(indexDep);

        testDepositor = bankDepositor.toString();

        bankDepositor.setDepositorName("UpdateDepositorName");
        bankDepositor.setDepositorIdDeposit(1L);
        bankDepositor.setDepositorDateDeposit(dateFormat.parse("2014-12-01"));
        bankDepositor.setDepositorAmountDeposit(100);
        bankDepositor.setDepositorAmountPlusDeposit(100);
        bankDepositor.setDepositorAmountMinusDeposit(0);
        bankDepositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-06-01"));
        bankDepositor.setDepositorMarkReturnDeposit(0);

        bankDepositorDao.updateBankDepositor(bankDepositor);

        assertNotEquals(bankDepositorDao.getBankDepositorById(testid).toString(),testDepositor);
        assertEquals(bankDepositorDao.getBankDepositorById(testid).toString(),bankDepositor.toString());
    }

	@Test
	public void testGetBankDepositorById(){
		sizeBefore = bankDepositors.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testid = bankDepositors.get(indexDep).getDepositorId();
		
		/*	или так
		bankDepositor.setDepositorId(testid);
		
		bankDepositor.setDepositorName(bankDepositors.get(indexDep).getDepositorName());
		bankDepositor.setDepositorIdDeposit(bankDepositors.get(indexDep).getDepositorIdDeposit());
		bankDepositor.setDepositorDateDeposit(bankDepositors.get(indexDep).getDepositorDateDeposit());
		bankDepositor.setDepositorAmountDeposit(bankDepositors.get(indexDep).getDepositorAmountDeposit());
		bankDepositor.setDepositorAmountPlusDeposit(bankDepositors.get(indexDep).getDepositorAmountPlusDeposit());
		bankDepositor.setDepositorAmountMinusDeposit(bankDepositors.get(indexDep).getDepositorAmountMinusDeposit());
		bankDepositor.setDepositorDateReturnDeposit(bankDepositors.get(indexDep).getDepositorDateReturnDeposit());
		bankDepositor.setDepositorMarkReturnDeposit(bankDepositors.get(indexDep).getDepositorMarkReturnDeposit());
		*/


		bankDepositor = bankDepositors.get(indexDep);
		bankDepositor.setDepositorId(testid);

		assertEquals(bankDepositorDao.getBankDepositorById(testid).toString(),bankDepositor.toString());
	}
	
	@Test
	public void testGetBankDepositorByName(){
		sizeBefore = bankDepositors.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		String testDepositorName = bankDepositors.get(indexDep).getDepositorName();
		
		bankDepositor.setDepositorName(testDepositorName);
		
		bankDepositor.setDepositorId(bankDepositors.get(indexDep).getDepositorId());	
		bankDepositor.setDepositorIdDeposit(bankDepositors.get(indexDep).getDepositorIdDeposit());
		bankDepositor.setDepositorDateDeposit(bankDepositors.get(indexDep).getDepositorDateDeposit());
		bankDepositor.setDepositorAmountDeposit(bankDepositors.get(indexDep).getDepositorAmountDeposit());
		bankDepositor.setDepositorAmountPlusDeposit(bankDepositors.get(indexDep).getDepositorAmountPlusDeposit());
		bankDepositor.setDepositorAmountMinusDeposit(bankDepositors.get(indexDep).getDepositorAmountMinusDeposit());
		bankDepositor.setDepositorDateReturnDeposit(bankDepositors.get(indexDep).getDepositorDateReturnDeposit());
		bankDepositor.setDepositorMarkReturnDeposit(bankDepositors.get(indexDep).getDepositorMarkReturnDeposit());
		// или так
		/*
		 * bankDepositor = bankDepositors.get(indexDep);
		 * bankDepositor.setDepositorName(testDepositorName);
		 */	
		assertEquals(bankDepositorDao.getBankDepositorByName(testDepositorName).toString(),bankDepositor.toString());
	}
	
	@Test
	public void testGetBankDepositorByIdDeposit() {
		sizeBefore = bankDepositors.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testid = bankDepositors.get(indexDep).getDepositorIdDeposit();
		
		List<BankDepositor> bankDepositorsByIdDeposit = bankDepositorDao.getBankDepositorByIdDeposit(testid);
		
		assertNotNull(bankDepositorsByIdDeposit);
		assertFalse(bankDepositorsByIdDeposit.isEmpty());
	}
	//============================
	@Test
	public void testGetBankDepositorsAllSummAmount() {
		List<BankDepositor> depositors = bankDepositorDao.getBankDepositors();
		Integer Summ1=0, Summ2=0, Summ3=0;
		BankDepositor depositor = bankDepositorDao.getBankDepositorsAllSummAmount();

		for (int i=0; i<depositors.size(); i++) {
			Summ1 += bankDepositors.get(i).getDepositorAmountDeposit();
			Summ2 += bankDepositors.get(i).getDepositorAmountPlusDeposit();
			Summ3 += bankDepositors.get(i).getDepositorAmountMinusDeposit();
		}
		assertTrue(Summ1 == depositor.getDepositorAmountDeposit());
		assertTrue(Summ2 == depositor.getDepositorAmountPlusDeposit());
		assertTrue(Summ3 == depositor.getDepositorAmountMinusDeposit());
	}

	@Test
	public void testGetBankDepositorsSummAmountByIdDeposit() {
		sizeBefore = bankDepositors.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testid = bankDepositors.get(indexDep).getDepositorIdDeposit();

		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorByIdDeposit(testid);

		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());

		Integer Summ1=0, Summ2=0, Summ3=0;
		BankDepositor depositor = bankDepositorDao.getBankDepositorsSummAmountByIdDeposit(testid);

		for (int i=0; i<depositors.size(); i++) {
			Summ1 += depositors.get(i).getDepositorAmountDeposit();
			Summ2 += depositors.get(i).getDepositorAmountPlusDeposit();
			Summ3 += depositors.get(i).getDepositorAmountMinusDeposit();
		}
		assertTrue(Summ1 == depositor.getDepositorAmountDeposit());
		assertTrue(Summ2 == depositor.getDepositorAmountPlusDeposit());
		assertTrue(Summ3 == depositor.getDepositorAmountMinusDeposit());
	}

	@Test
	public void testGetBankDepositorSummAmountDepositBetweenDateDeposit() throws ParseException{
		Date start = dateFormat.parse("2014-12-01");
		Date end = dateFormat.parse("2014-12-03");
		Integer Summ1=0, Summ2=0, Summ3=0;

		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorBetweenDateDeposit(start,end);
		BankDepositor depositorSum = bankDepositorDao.getBankDepositorSummAmountDepositBetweenDateDeposit(start, end);

		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());
		assertNotNull(depositorSum);

		for (int i=0; i<depositors.size(); i++){
			Summ1 += depositors.get(i).getDepositorAmountDeposit();
			Summ2 += depositors.get(i).getDepositorAmountPlusDeposit();
			Summ3 += depositors.get(i).getDepositorAmountMinusDeposit();
		}

		assertTrue(Summ1 == depositorSum.getDepositorAmountDeposit());
		assertTrue(Summ2 == depositorSum.getDepositorAmountPlusDeposit());
		assertTrue(Summ3 == depositorSum.getDepositorAmountMinusDeposit());
	}

	@Test
	public void testGetBankDepositorSummAmountByIdDepositDepositBetweenDateDeposit() throws ParseException{
		Date start = dateFormat.parse("2014-12-01");
		Date end = dateFormat.parse("2014-12-03");
		Integer Summ1=0, Summ2=0, Summ3=0;
		sizeBefore = bankDepositors.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testId = bankDepositors.get(indexDep).getDepositorIdDeposit();

		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorByIdDepositBetweenDateDeposit(testId,start,end);
		BankDepositor depositorSum = bankDepositorDao.getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(testId,start, end);

		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());
		assertNotNull(depositorSum);

		for (int i=0; i<depositors.size(); i++){
			Summ1 += depositors.get(i).getDepositorAmountDeposit();
			Summ2 += depositors.get(i).getDepositorAmountPlusDeposit();
			Summ3 += depositors.get(i).getDepositorAmountMinusDeposit();
		}

		assertTrue(Summ1 == depositorSum.getDepositorAmountDeposit());
		assertTrue(Summ2 == depositorSum.getDepositorAmountPlusDeposit());
		assertTrue(Summ3 == depositorSum.getDepositorAmountMinusDeposit());
	}

	@Test
	public void testGetBankDepositorSummAmountDepositBetweenDateReturnDeposit() throws ParseException{
		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-04");
		Integer Summ1=0, Summ2=0, Summ3=0;

		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorBetweenDateReturnDeposit(start,end);
		BankDepositor depositorSum = bankDepositorDao.getBankDepositorSummAmountDepositBetweenDateReturnDeposit(start, end);

		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());
		assertNotNull(depositorSum);

		for (int i=0; i<depositors.size(); i++){
			Summ1 += depositors.get(i).getDepositorAmountDeposit();
			Summ2 += depositors.get(i).getDepositorAmountPlusDeposit();
			Summ3 += depositors.get(i).getDepositorAmountMinusDeposit();
		}

		assertTrue(Summ1 == depositorSum.getDepositorAmountDeposit());
		assertTrue(Summ2 == depositorSum.getDepositorAmountPlusDeposit());
		assertTrue(Summ3 == depositorSum.getDepositorAmountMinusDeposit());;

	}
	//============================
	@Test
	public void testGetBankDepositorBetweenDateDeposit() throws ParseException{
		Date start = dateFormat.parse("2014-12-01");
		Date end = dateFormat.parse("2014-12-02");
		List<BankDepositor> bankDepositorBetweenDateDeposit = bankDepositorDao.getBankDepositorBetweenDateDeposit(start, end);
		
		assertNotNull(bankDepositorBetweenDateDeposit);
		assertFalse(bankDepositorBetweenDateDeposit.isEmpty());
	}

	@Test
	public void testGetBankDepositorByIdDepositBetweenDateDeposit() throws ParseException{
		Date start = dateFormat.parse("2014-12-01");
		Date end = dateFormat.parse("2014-12-03");
		sizeBefore = bankDepositors.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testId = bankDepositors.get(indexDep).getDepositorIdDeposit();

		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorByIdDepositBetweenDateDeposit(testId, start, end);

		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());
	}

	@Test
	public void testGetBankDepositorByIdDepositBetweenDateReturnDeposit() throws ParseException{
		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-04");
		sizeBefore = bankDepositors.size();
		int indexDep = (int)(Math.random()*sizeBefore);
		Long testId = bankDepositors.get(indexDep).getDepositorIdDeposit();

		List<BankDepositor> depositors = bankDepositorDao.getBankDepositorByIdDepositBetweenDateReturnDeposit(testId, start, end);

		assertNotNull(depositors);
		assertFalse(depositors.isEmpty());
	}
	
	@Test
	public void testGetBankDepositorBetweenDateReturnDeposit() throws ParseException{
		Date start = dateFormat.parse("2014-12-02");
		Date end = dateFormat.parse("2014-12-05");
		List<BankDepositor> bankDepositorBetweenDateReturnDeposit = bankDepositorDao.getBankDepositorBetweenDateReturnDeposit(start, end);
		
		assertNotNull(bankDepositorBetweenDateReturnDeposit);
		assertFalse(bankDepositorBetweenDateReturnDeposit.isEmpty());
	}

    @Test
    public void testRemoveBankDepositor() {
        assertNotNull(bankDepositors);
        assertFalse(bankDepositors.isEmpty());
        sizeBefore = bankDepositors.size();
        bankDepositorDao.removeBankDepositor(bankDepositors.get((int)(Math.random()*sizeBefore)).getDepositorId());
        bankDepositors = bankDepositorDao.getBankDepositors();
        assertEquals(sizeBefore-1,bankDepositors.size());
    }

    @Test
    public void testRemoveBankDepositorByIdDeposit() {
        assertNotNull(bankDepositors);
        assertFalse(bankDepositors.isEmpty());
        sizeBefore = bankDepositors.size();
        bankDepositorDao.removeBankDepositorByIdDeposit(bankDepositors.get((int)(Math.random()*sizeBefore)).getDepositorIdDeposit());
        bankDepositors = bankDepositorDao.getBankDepositors();
        assertFalse(bankDepositors.isEmpty());
        assertTrue(sizeBefore > bankDepositors.size());
    }
}
