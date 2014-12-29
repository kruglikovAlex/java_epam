package com.brest.bank.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.domain.BankDepositor;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertTrue;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-services-mock-test.xml"})
public class DepositorServiceImplMockTest {

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
    @Autowired
    private BankDepositorService depositorService;

    @Autowired
    private BankDepositorDao depositorDao;
        
    @After
    public void clean() {
        reset(depositorDao);
    }

    @Test()
    public void getDepositors() throws ParseException{
    	List<BankDepositor> depositors = DataFixture.getDepositors();
    	
    	depositorDao.getBankDepositors();
    	expectLastCall().andReturn(depositors);
    	replay(depositorDao);
    	
    	List<BankDepositor> depositorsResult = depositorService.getBankDepositors();
    	verify(depositorDao);
    	
    	assertEquals(depositors, depositorsResult);
    	assertSame(depositors, depositorsResult);
    }
    
    @Test
    public void addDepositor() throws ParseException{
        BankDepositor depositor = DataFixture.getNewDepositor();
        
        depositorDao.getBankDepositorByName(depositor.getDepositorName());
        expectLastCall().andReturn(null);
        
        depositorDao.addBankDepositor(depositor);
        expectLastCall().andReturn(Long.valueOf(1L));
               
        replay(depositorDao);
        
        Long id = depositorService.addBankDepositor(depositor);
        verify(depositorDao);
        
        assertEquals(id, Long.valueOf(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTwoDepositor() throws ParseException{
        BankDepositor depositor = DataFixture.getNewDepositor();
        
        depositorDao.addBankDepositor(depositor);
        expectLastCall().andReturn(Long.valueOf(1L)).times(2);
        
        depositorDao.getBankDepositorByName(depositor.getDepositorName());
        expectLastCall().andReturn(depositor).times(2);
        
        replay(depositorDao);
        
        depositorService.addBankDepositor(depositor);
        depositorService.addBankDepositor(depositor);
        
        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepositorWithSameName() throws ParseException{
        BankDepositor depositor = DataFixture.getNewDepositor();
        
        expect(depositorDao.getBankDepositorByName(depositor.getDepositorName())).andReturn(depositor);
        replay(depositorDao);
        
        depositorService.addBankDepositor(depositor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddNotNullIdDepositor() {
        BankDepositor depositor = DataFixture.getNotNullIdDepositor();

        depositorDao.getBankDepositorByName(depositor.getDepositorName());
        expectLastCall().andReturn(depositor);
        replay(depositorDao);

        depositorService.addBankDepositor(depositor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullDepositor() {
        BankDepositor depositor = DataFixture.getNullDepositor();

        depositorDao.getBankDepositorByName(depositor.getDepositorName());
        expectLastCall().andReturn(depositor);
        replay(depositorDao);

        depositorService.addBankDepositor(depositor);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addEmptyDepositor() {
        BankDepositor depositor = DataFixture.getEmptyDepositor();

        depositorDao.getBankDepositorByName(depositor.getDepositorName());
        expectLastCall().andReturn(depositor);
        replay(depositorDao);

        depositorService.addBankDepositor(depositor);
    }
    
    @Test
    public void getDepositorByName() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        
        expect(depositorDao.getBankDepositorByName(depositor.getDepositorName())).andReturn(depositor);
        replay(depositorDao);
        
        BankDepositor depositorResult = depositorService.getBankDepositorByName(depositor.getDepositorName());
        verify(depositorDao);
        
        assertSame(depositor, depositorResult);
    }
    
    @Test
    public void getBankDepositorById() throws ParseException{
    	BankDepositor depositor = DataFixture.getExistDepositor(1L);
        
        expect(depositorDao.getBankDepositorById(depositor.getDepositorId())).andReturn(depositor);
        replay(depositorDao);
        
        BankDepositor depositorResult = depositorService.getBankDepositorById(depositor.getDepositorId());
        verify(depositorDao);
        
        assertSame(depositor, depositorResult);
    }

    @Test
    public void getDepositorByIdDeposit() throws ParseException{
    	List<BankDepositor> depositors = DataFixture.getExistDepositors();

    	depositorDao.getBankDepositorByIdDeposit(depositors.get(1).getDepositorIdDeposit());
    	expectLastCall().andReturn(depositors);
    	replay(depositorDao);
    	
    	List<BankDepositor> depositorsResult = depositorService.getBankDepositorByIdDeposit(depositors.get(1).getDepositorIdDeposit());
    	verify(depositorDao);
    	
    	assertEquals(depositors, depositorsResult);
    	assertSame(depositors, depositorsResult);
    }

    @Test
    public void getDepositorByIdDepositBetweenDateDeposit() throws ParseException{
        List<BankDepositor> depositors = DataFixture.getExistDepositors();

        depositorDao.getBankDepositorByIdDepositBetweenDateDeposit(depositors.get(1).getDepositorIdDeposit(),dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        expectLastCall().andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> depositorsResult = depositorService.getBankDepositorByIdDepositBetweenDateDeposit(depositors.get(1).getDepositorIdDeposit(),dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        verify(depositorDao);

        assertEquals(depositors, depositorsResult);
        assertSame(depositors, depositorsResult);
    }

    @Test
    public void getDepositorByIdDepositBetweenDateReturnDeposit() throws ParseException{
        List<BankDepositor> depositors = DataFixture.getExistDepositors();

        depositorDao.getBankDepositorByIdDepositBetweenDateReturnDeposit(depositors.get(1).getDepositorIdDeposit(), dateFormat.parse("2014-12-02"), dateFormat.parse("2014-12-04"));
        expectLastCall().andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> depositorsResult = depositorService.getBankDepositorByIdDepositBetweenDateReturnDeposit(depositors.get(1).getDepositorIdDeposit(), dateFormat.parse("2014-12-02"), dateFormat.parse("2014-12-04"));
        verify(depositorDao);

        assertEquals(depositors, depositorsResult);
        assertSame(depositors, depositorsResult);
    }

    @Test()
    public void getBankDepositorsAllSummAmount() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);

        depositorDao.getBankDepositorsAllSummAmount();
        expectLastCall().andReturn(depositor);
        replay(depositorDao);

        BankDepositor depositorResult = depositorService.getBankDepositorsAllSummAmount();
        verify(depositorDao);

        assertEquals(depositor, depositorResult);
        assertSame(depositor, depositorResult);
        assertTrue(depositor.getDepositorAmountDeposit()==depositorResult.getDepositorAmountDeposit());
        assertTrue(depositor.getDepositorAmountPlusDeposit()==depositorResult.getDepositorAmountPlusDeposit());
        assertTrue(depositor.getDepositorAmountMinusDeposit()==depositorResult.getDepositorAmountMinusDeposit());
    }

    @Test
    public void getBankDepositorsSummAmountByIdDeposit() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);

        depositorDao.getBankDepositorsSummAmountByIdDeposit(depositor.getDepositorIdDeposit());
        expectLastCall().andReturn(depositor);
        replay(depositorDao);

        BankDepositor depositorResult = depositorService.getBankDepositorsSummAmountByIdDeposit(depositor.getDepositorIdDeposit());
        verify(depositorDao);

        assertEquals(depositor, depositorResult);
        assertSame(depositor, depositorResult);
        assertTrue(depositor.getDepositorAmountDeposit()==depositorResult.getDepositorAmountDeposit());
        assertTrue(depositor.getDepositorAmountPlusDeposit() == depositorResult.getDepositorAmountPlusDeposit());
        assertTrue(depositor.getDepositorAmountMinusDeposit() == depositorResult.getDepositorAmountMinusDeposit());
    }

    @Test
    public void getBankDepositorSummAmountDepositBetweenDateDeposit() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        expect(depositorDao.getBankDepositorSummAmountDepositBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-02"))).andReturn(depositor);

        replay(depositorDao);

        BankDepositor depositorResult = depositorService.getBankDepositorSummAmountDepositBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-02"));
        verify(depositorDao);

        assertEquals(depositor, depositorResult);
        assertSame(depositor, depositorResult);
        assertTrue(depositor.getDepositorAmountDeposit()==depositorResult.getDepositorAmountDeposit());
        assertTrue(depositor.getDepositorAmountPlusDeposit()==depositorResult.getDepositorAmountPlusDeposit());
        assertTrue(depositor.getDepositorAmountMinusDeposit()==depositorResult.getDepositorAmountMinusDeposit());
    }

    @Test
    public void getBankDepositorSummAmountByIdDepositBetweenDateDeposit() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        expect(depositorDao.getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(1L,dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-02"))).andReturn(depositor);

        replay(depositorDao);

        BankDepositor depositorResult = depositorService.getBankDepositorsSummAmountByIdDepositBetweenDateDeposit(1L,dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-02"));
        verify(depositorDao);

        assertEquals(depositor, depositorResult);
        assertSame(depositor, depositorResult);
        assertTrue(depositor.getDepositorAmountDeposit()==depositorResult.getDepositorAmountDeposit());
        assertTrue(depositor.getDepositorAmountPlusDeposit()==depositorResult.getDepositorAmountPlusDeposit());
        assertTrue(depositor.getDepositorAmountMinusDeposit()==depositorResult.getDepositorAmountMinusDeposit());
    }

    @Test
    public void getBankDepositorSummAmountDepositBetweenDateReturnDeposit() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);

        depositorDao.getBankDepositorSummAmountDepositBetweenDateReturnDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        expectLastCall().andReturn(depositor);
        replay(depositorDao);

        BankDepositor depositorResult = depositorService.getBankDepositorSummAmountDepositBetweenDateReturnDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
        verify(depositorDao);

        assertEquals(depositor, depositorResult);
        assertSame(depositor, depositorResult);
        assertTrue(depositor.getDepositorAmountDeposit()==depositorResult.getDepositorAmountDeposit());
        assertTrue(depositor.getDepositorAmountPlusDeposit()==depositorResult.getDepositorAmountPlusDeposit());
        assertTrue(depositor.getDepositorAmountMinusDeposit()==depositorResult.getDepositorAmountMinusDeposit());
    }

    @Test
    public void getDepositorBetweenDateDeposit() throws ParseException{
    	List<BankDepositor> depositors = DataFixture.getExistDepositors();
    	
    	depositorDao.getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
    	expectLastCall().andReturn(depositors);
    	replay(depositorDao);
    	
    	List<BankDepositor> depositorsResult = depositorService.getBankDepositorBetweenDateDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
    	verify(depositorDao);
    	
    	assertEquals(depositors, depositorsResult);
    	assertSame(depositors, depositorsResult);
    }

    @Test
    public void getDepositorBetweenDateReturnDeposit() throws ParseException{
    	List<BankDepositor> depositors = DataFixture.getExistDepositors();
    	
    	depositorDao.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
    	expectLastCall().andReturn(depositors);
    	replay(depositorDao);
    	
    	List<BankDepositor> depositorsResult = depositorService.getBankDepositorBetweenDateReturnDeposit(dateFormat.parse("2014-12-01"), dateFormat.parse("2014-12-01"));
    	verify(depositorDao);
    	
    	assertEquals(depositors, depositorsResult);
    	assertSame(depositors, depositorsResult);
    }

    @Test//(expected = IllegalArgumentException.class)
    public void updateDepositor() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);

        depositorDao.getBankDepositorById(depositor.getDepositorId());
        expectLastCall().andReturn(depositor);
        
        depositorDao.updateBankDepositor(depositor);
        expectLastCall();
        
        replay(depositorDao);
        
        depositorService.updateBankDepositor(depositor);
        
        verify(depositorDao);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void updateEmptyDepositor(){
        BankDepositor depositor = DataFixture.getEmptyDepositor();

        depositorDao.getBankDepositorById(depositor.getDepositorId());
        expectLastCall().andReturn(depositor);
        replay(depositorDao);

        depositorService.updateBankDepositor(new BankDepositor());
    }
    
    @Test//(expected = IllegalArgumentException.class)
    public void removeDepositor() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);

        depositorDao.getBankDepositorById(depositor.getDepositorId());
        expectLastCall().andReturn(depositor);
        
        depositorDao.removeBankDepositor(depositor.getDepositorId());
        expectLastCall();
        
        replay(depositorDao);
        
        depositorService.removeBankDepositor(depositor.getDepositorId());
                
        verify(depositorDao);
    }

    @Test//(expected = IllegalArgumentException.class)
    public void removeDepositorByIdDeposit() throws ParseException{
    	List<BankDepositor> depositors = DataFixture.getExistDepositors();

        depositorDao.getBankDepositorByIdDeposit(depositors.get(1).getDepositorIdDeposit());
        expectLastCall().andReturn(depositors);
        
        depositorDao.removeBankDepositorByIdDeposit(depositors.get(1).getDepositorIdDeposit());
        expectLastCall();
        
        replay(depositorDao);
        
        boolean res = depositorService.removeBankDepositorByIdDeposit(depositors.get(1).getDepositorIdDeposit());
                
        verify(depositorDao);

        assertTrue(res);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullIdDepositor(){
        depositorService.removeBankDepositor(null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void throwException() throws ParseException{
        BankDepositor depositor = DataFixture.getNewDepositor();
        
        expect(depositorDao.getBankDepositorByName(depositor.getDepositorName())).andThrow(new UnsupportedOperationException());
        replay(depositorDao);
        
        depositorService.addBankDepositor(depositor);
    }
}