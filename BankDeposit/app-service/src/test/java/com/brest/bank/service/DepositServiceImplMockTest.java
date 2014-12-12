package com.brest.bank.service;

import java.text.ParseException;
import java.util.List;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNotNull;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-services-mock-test.xml"})
public class DepositServiceImplMockTest {

    @Autowired
    private BankDepositService depositService;
    private BankDepositorService depositorService;

    @Autowired
    private BankDepositDao depositDao;

    
    @Before
    public void setUp() throws Exception {
    	depositorService = EasyMock.createMock(BankDepositorService.class);
	}

    @After
    public void clean() {
        reset(depositDao);
    }

    @Test()
    public void getDeposits() {
    	List<BankDeposit> deposits = DataFixture.getDeposits();
    	
    	depositDao.getBankDeposits();
    	expectLastCall().andReturn(deposits);
    	replay(depositDao);
    	
    	List<BankDeposit> depositsResult = depositService.getBankDeposits();
    	verify(depositDao);
    	
    	assertEquals(deposits, depositsResult);
    	assertSame(deposits, depositsResult); 
    }
    
    @Test
    public void addDeposit() {
        BankDeposit deposit = DataFixture.getNewDeposit();
        
        depositDao.addBankDeposit(deposit);
        expectLastCall().andReturn(Long.valueOf(1L));
        
        depositDao.getBankDepositByName(deposit.getDepositName());
        expectLastCall().andReturn(null);
        
        replay(depositDao);
        
        Long id = depositService.addBankDeposit(deposit);
        verify(depositDao);
        assertEquals(id, Long.valueOf(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTwoDeposit() {
        BankDeposit deposit = DataFixture.getNewDeposit();
        
        depositDao.addBankDeposit(deposit);
        expectLastCall().andReturn(Long.valueOf(1L)).times(2);
        
        depositDao.getBankDepositByName(deposit.getDepositName());
        expectLastCall().andReturn(deposit).times(2);
        
        replay(depositDao);
        
        depositService.addBankDeposit(deposit);
        depositService.addBankDeposit(deposit);
        
        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDepositWithSameName() {
        BankDeposit deposit = DataFixture.getNewDeposit();
        
        expect(depositDao.getBankDepositByName(deposit.getDepositName())).andReturn(deposit);
        replay(depositDao);
        
        depositService.addBankDeposit(deposit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddNotNullIdDeposit() {
        BankDeposit deposit = DataFixture.getNotNullIdDeposit();

        depositDao.getBankDepositByName(deposit.getDepositName());
        expectLastCall().andReturn(deposit);
        replay(depositDao);

        depositService.addBankDeposit(deposit);
        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddNullDeposit() {
        BankDeposit deposit = DataFixture.getNullDeposit();

        depositDao.getBankDepositByName(deposit.getDepositName());
        expectLastCall().andReturn(deposit);
        replay(depositDao);

        depositService.addBankDeposit(deposit);
        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void AddEmptyDeposit() {
        BankDeposit deposit = DataFixture.getEmptyDeposit();

        depositDao.getBankDepositByName(deposit.getDepositName());
        expectLastCall().andReturn(deposit);
        replay(depositDao);

        depositService.addBankDeposit(deposit);
        verify(depositDao);
    }
    
    @Test
    public void getDepositByName() {
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        
        expect(depositDao.getBankDepositByName(deposit.getDepositName())).andReturn(deposit);
        replay(depositDao);
        
        BankDeposit depositResult = depositService.getBankDepositByName(deposit.getDepositName());
        verify(depositDao);
        
        assertSame(deposit, depositResult);
    }
    
    @Test
    public void getBankDepositById() {
    	BankDeposit deposit = DataFixture.getExistDeposit(1L);
        
        expect(depositDao.getBankDepositById(deposit.getDepositId())).andReturn(deposit);
        replay(depositDao);
        
        BankDeposit depositResult = depositService.getBankDepositById(deposit.getDepositId());
        verify(depositDao);
        
        assertSame(deposit, depositResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void UpdateEmptyDeposit(){
        BankDeposit deposit = DataFixture.getEmptyDeposit();

        depositDao.getBankDepositById(deposit.getDepositId());
        expectLastCall().andReturn(deposit);
        replay(depositDao);

        depositService.updateBankDeposit(new BankDeposit());
        verify(depositDao);
    }
    
    @Test//(expected = IllegalArgumentException.class)
    public void UpdateDeposit(){
        BankDeposit deposit = DataFixture.getExistDeposit(1L);

        depositDao.getBankDepositById(deposit.getDepositId());
        expectLastCall().andReturn(deposit);

        depositDao.updateBankDeposit(deposit);
        expectLastCall();
        
        replay(depositDao);
        
        depositService.updateBankDeposit(deposit);
        
        verify(depositDao);
    }
    
    @Test//(expected = IllegalArgumentException.class)
    public void RemoveDeposit() throws ParseException{
        depositorService = EasyMock.createMock(BankDepositorService.class);
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        List<BankDepositor> depositors = DataFixture.getExistDepositors();

        depositDao.getBankDepositById(deposit.getDepositId());
        expectLastCall().andReturn(deposit);

        depositDao.removeBankDeposit(deposit.getDepositId());
        expectLastCall();

        replay(depositDao);

        depositorService.getBankDepositorByIdDeposit(deposit.getDepositId());
        expectLastCall().andReturn(depositors);

        depositorService.removeBankDepositorByIdDeposit(deposit.getDepositId());
        expectLastCall().andReturn(true);

        depositService.removeBankDeposit(deposit.getDepositId());

        verify(depositDao);

    }

    @Test(expected = IllegalArgumentException.class)
    public void RemoveNullIdDeposit(){
        depositService.removeBankDeposit(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throwException() {
        BankDeposit deposit = DataFixture.getNewDeposit();
        
        expect(depositDao.getBankDepositByName(deposit.getDepositName())).andThrow(new UnsupportedOperationException());
        replay(depositDao);
        
        depositService.addBankDeposit(deposit);
    }
}