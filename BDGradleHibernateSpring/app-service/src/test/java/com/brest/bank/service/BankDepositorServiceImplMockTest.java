package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.easymock.EasyMock.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-services-mock-test.xml"})
public class BankDepositorServiceImplMockTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    BankDepositorService depositorService;

    @Autowired
    BankDepositorDao depositorDao;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void clean() throws Exception {
        reset(depositorDao);
    }

    @Test
    public void testGetBankDepositors() throws Exception {
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.debug("depositors: {}",depositors);

        expect(depositorDao.getBankDepositorsCriteria()).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> result = depositorService.getBankDepositors();
        LOGGER.debug("result: {}",result);

        verify(depositorDao);

        assertEquals(depositors,result);
        assertSame(depositors,result);
    }

    @Test
    public void testGetBankDepositorsFromToDateDeposit() throws Exception {
        List<BankDepositor> depositors = DataFixture.getExistDepositors();
        LOGGER.debug("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorsFromToDateDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-09-09"))).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> result = depositorService.getBankDepositorsFromToDateDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-09-09"));
        LOGGER.debug("result: {}",result);

        verify(depositorDao);

        assertEquals(depositors,result);
        assertSame(depositors,result);
    }

    @Test
    public void testGetBankDepositorsFromToDateReturnDeposit() throws Exception {
        List<BankDepositor> depositors = DataFixture.getExistDepositors();
        LOGGER.debug("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorsFromToDateReturnDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-09-09"))).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> result = depositorService
                .getBankDepositorsFromToDateReturnDeposit(dateFormat.parse("2015-01-01"),
                                                        dateFormat.parse("2015-09-09"));
        LOGGER.debug("result: {}",result);

        verify(depositorDao);

        assertEquals(depositors,result);
        assertSame(depositors,result);
    }

    @Test
    public void testGetBankDepositorById() throws Exception {
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.debug("depositor={}",depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(1L)).andReturn(depositor);
        replay(depositorDao);

        BankDepositor result = depositorService.getBankDepositorById(1L);
        LOGGER.debug("result={}",result);

        verify(depositorDao);

        assertEquals(depositor,result);
        assertSame(depositor,result);
    }

    @Test
    public void testGetBankDepositorByIdDeposit() throws Exception {
        List<BankDepositor> depositors = DataFixture.getExistDepositors();
        LOGGER.debug("depositors={}",depositors);

        expect(depositorDao.getBankDepositorByIdDepositCriteria(1L)).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> result = depositorService.getBankDepositorByIdDeposit(1L);
        LOGGER.debug("result={}",result);

        verify(depositorDao);

        assertEquals(depositors,result);
        assertSame(depositors,result);
    }

    @Test
    public void testGetBankDepositorByName() throws Exception {
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.debug("depositor={}",depositor);

        expect(depositorDao.getBankDepositorByNameCriteria("depositorName1")).andReturn(depositor);
        replay(depositorDao);

        BankDepositor result = depositorService.getBankDepositorByName("depositorName1");
        LOGGER.debug("result={}",result);

        verify(depositorDao);

        assertEquals(depositor,result);
        assertSame(depositor,result);
    }

    @Test
    public void testAddBankDepositor() throws Exception {
        BankDepositor depositor = DataFixture.getNewDepositor();
        LOGGER.debug("new depositor = {}",depositor);

        expect(depositorDao.getBankDepositorByNameCriteria(depositor.getDepositorName())).andReturn(null);

        depositorDao.addBankDepositor(1L, depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.addBankDepositor(1L, depositor);

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTwoDepositor() throws ParseException{
        BankDepositor depositor = DataFixture.getNewDepositor();
        LOGGER.debug("new depositor = {}",depositor);

        depositorDao.addBankDepositor(1L, depositor);
        expectLastCall().times(2);

        depositorDao.getBankDepositorByNameCriteria(depositor.getDepositorName());
        expectLastCall().andReturn(depositor).times(2);

        replay(depositorDao);

        depositorService.addBankDepositor(1L, depositor);
        depositorService.addBankDepositor(2L, depositor);

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDepositorWithSameName() throws ParseException{
        BankDepositor depositor = DataFixture.getNewDepositor();
        LOGGER.debug("new depositor = {}",depositor);

        depositorDao.addBankDepositor(1L, depositor);
        expectLastCall();

        expect(depositorDao.getBankDepositorByNameCriteria(depositor.getDepositorName())).andReturn(depositor);
        replay(depositorDao);

        depositorService.addBankDepositor(1L, depositor);

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNotNullIdDepositor() throws ParseException{
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.debug("depositor = {}",depositor);

        expect(depositorDao.getBankDepositorByNameCriteria(depositor.getDepositorName())).andReturn(depositor);

        depositorDao.addBankDepositor(1L, depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.addBankDepositor(1L,depositor);

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullDepositor() {
        BankDepositor depositor = DataFixture.getNullDepositor();
        LOGGER.debug("null depositor = {}",depositor);

        depositorDao.addBankDepositor(1L, depositor);
        expectLastCall();

        expect(depositorDao.getBankDepositorByNameCriteria("name")).andReturn(null);
        replay(depositorDao);

        depositorService.addBankDepositor(1L,depositor);

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyDepositor(){
        BankDepositor depositor = DataFixture.getEmptyDepositor();
        LOGGER.debug("empty depositor = {}", depositor);

        expect(depositorDao.getBankDepositorByNameCriteria(depositor.getDepositorName())).andReturn(null);

        depositorDao.addBankDepositor(1L,depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.addBankDepositor(1L,depositor);

        verify(depositorDao);
    }

    @Test
    public void testUpdateBankDepositor() throws Exception {
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.debug("depositor = {}", depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId())).andReturn(depositor);

        depositorDao.updateBankDepositor(depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.updateBankDepositor(depositor);

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEmptyDepositor(){
        BankDepositor depositor = DataFixture.getEmptyDepositor();
        LOGGER.debug("empty deposiort = {}", depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId())).andReturn(depositor);

        depositorDao.updateBankDepositor(depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.updateBankDepositor(depositor);

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullDepositor(){
        BankDepositor depositor = DataFixture.getNullDepositor();
        LOGGER.debug("null deposiort = {}", depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(1L)).andReturn(null);

        depositorDao.updateBankDepositor(depositor);
        expectLastCall();

        replay(depositorDao);

        depositorService.updateBankDepositor(depositor);

        verify(depositorDao);
    }

    @Test
    public void testRemoveBankDepositor() throws Exception {
        BankDepositor depositor = DataFixture.getExistDepositor(1L);
        LOGGER.debug("depositor = {}", depositor);

        expect(depositorDao.getBankDepositorByIdCriteria(depositor.getDepositorId())).andReturn(depositor);

        depositorDao.removeBankDepositor(depositor.getDepositorId());
        expectLastCall();

        replay(depositorDao);

        depositorService.removeBankDepositor(depositor.getDepositorId());

        verify(depositorDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullIdDepositor(){
        depositorService.removeBankDepositor(null);
    }
}