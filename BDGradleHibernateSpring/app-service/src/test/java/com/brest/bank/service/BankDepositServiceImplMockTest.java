package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.easymock.EasyMock.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-services-mock-test.xml"})
public class BankDepositServiceImplMockTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositDao depositDao;

    @Autowired
    BankDepositorDao depositorDao;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void clean() throws Exception {
        reset(depositDao);
        reset(depositorDao);
    }

    @Test
    public void testGetBankDeposits() throws Exception {
        List<BankDeposit> deposits = DataFixture.getDeposits();
        LOGGER.debug("deposits - {}",deposits);

        depositDao.getBankDepositsCriteria();
        expectLastCall().andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits = depositService.getBankDeposits();
        LOGGER.debug("resultDeposits - {}",resultDeposits);

        assertEquals(deposits,resultDeposits);
        assertSame(deposits,resultDeposits);

        verify(depositDao);
    }

    @Test
    public void testGetBankDepositById() throws Exception {
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.debug("deposit = {}",deposit);

        expect(depositDao.getBankDepositByIdCriteria(1L)).andReturn(deposit);
        replay(depositDao);

        BankDeposit resultDeposit = depositService.getBankDepositById(1L);
        LOGGER.debug("resultDeposit = {}",resultDeposit);

        verify(depositDao);

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByName() throws Exception {
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.debug("deposit = {}",deposit);

        expect(depositDao.getBankDepositByNameCriteria(deposit.getDepositName())).andReturn(deposit);
        replay(depositDao);

        BankDeposit resultDeposit = depositService.getBankDepositByName(deposit.getDepositName());
        LOGGER.debug("resultDeposit = {}",resultDeposit);

        verify(depositDao);

        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositsByCurrency() throws Exception {
        List<BankDeposit> deposits = DataFixture.getDeposits();
        LOGGER.debug("deposits - {}",deposits);

        expect(depositDao.getBankDepositsByCurrencyCriteria(deposits.get(0).getDepositCurrency())).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits = depositService.getBankDepositsByCurrency(deposits.get(0).getDepositCurrency());
        LOGGER.debug("resultDeposits = {}",resultDeposits);

        verify(depositDao);

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositsByInterestRate() throws Exception {
        List<BankDeposit> deposits = DataFixture.getDeposits();
        LOGGER.debug("deposits - {}",deposits);

        expect(depositDao.getBankDepositsByInterestRateCriteria(deposits.get(0)
                .getDepositInterestRate())).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits = depositService.getBankDepositsByInterestRate(deposits.get(0)
                .getDepositInterestRate());
        LOGGER.debug("resultDeposits = {}",resultDeposits);

        verify(depositDao);

        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositsFromToMinTerm() throws Exception {
        List<BankDeposit> deposits = DataFixture.getDeposits();
        deposits.get(0).setDepositMinTerm(11);
        deposits.add(DataFixture.getExistDeposit(2L));
        LOGGER.debug("deposits - {}",deposits);

        expect(depositDao.getBankDepositsFromToMinTermCriteria(11,12)).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits = depositService.getBankDepositsFromToMinTerm(11,12);
        LOGGER.debug("resultDeposits = {}", resultDeposits);

        verify(depositDao);

        assertSame(deposits,resultDeposits);
    }

    @Test
    public void testGetBankDepositsFromToInterestRate() throws Exception {
        List<BankDeposit> deposits = DataFixture.getDeposits();
        deposits.get(0).setDepositInterestRate(3);
        deposits.add(DataFixture.getExistDeposit(2L));
        LOGGER.debug("deposits - {}",deposits);

        expect(depositDao.getBankDepositsFromToInterestRateCriteria(3,4)).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits = depositService.getBankDepositsFromToInterestRate(3,4);
        LOGGER.debug("resultDeposits = {}", resultDeposits);

        verify(depositDao);

        assertSame(deposits,resultDeposits);
    }

    @Test
    public void testGetBankDepositsFromToDateDeposit() throws Exception {
        List<BankDeposit> deposits = DataFixture.getDeposits();
        deposits.get(0).setDepositInterestRate(3);
        deposits.add(DataFixture.getExistDeposit(2L));

        Set<BankDepositor> depositors = new HashSet<BankDepositor>();
        depositors.add(DataFixture.getExistDepositor(1L));
        depositors.add(DataFixture.getExistDepositor(2L));
        LOGGER.debug("depositors - {}",depositors);

        deposits.get(0).setDepositors(depositors);
        LOGGER.debug("deposits - {}",deposits);

        expect(depositDao.getBankDepositsFromToDateDeposit(dateFormat.parse("2015-01-01"),
                                                            dateFormat.parse("2015-02-02"))).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits =
                depositService.getBankDepositsFromToDateDeposit(dateFormat.parse("2015-01-01"),
                                                                    dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposits = {}", resultDeposits);

        verify(depositDao);

        assertSame(deposits,resultDeposits);
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDeposit() throws Exception {
        List<BankDeposit> deposits = DataFixture.getDeposits();
        deposits.get(0).setDepositInterestRate(3);
        deposits.add(DataFixture.getExistDeposit(2L));

        Set<BankDepositor> depositors = new HashSet<BankDepositor>();
        depositors.add(DataFixture.getExistDepositor(1L));
        depositors.add(DataFixture.getExistDepositor(2L));
        LOGGER.debug("depositors - {}",depositors);

        deposits.get(0).setDepositors(depositors);
        LOGGER.debug("deposits - {}",deposits);

        expect(depositDao.getBankDepositsFromToDateReturnDeposit(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02")))
                .andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits =
                depositService.getBankDepositsFromToDateReturnDeposit(dateFormat.parse("2015-01-01"),
                                                                        dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposits = {}", resultDeposits);

        verify(depositDao);

        assertSame(deposits,resultDeposits);
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws Exception {
        Map depositsAllDepositors = DataFixture.getExistDepositAllDepositors(1L,2L);
        LOGGER.debug("depositsAllDepositors = {}",depositsAllDepositors);

        expect(depositDao.getBankDepositByNameWithDepositors("depositName1")).andReturn(depositsAllDepositors);

        replay(depositDao);

        Map resultDeposits = depositService.getBankDepositByNameWithDepositors("depositName1");
        LOGGER.debug("resultDeposits = {}",resultDeposits);

        verify(depositDao);

        assertEquals(depositsAllDepositors, resultDeposits);
        assertSame(depositsAllDepositors, resultDeposits);
    }

    @Test
    public void testGetBankDepositByNameFromToDateDepositWithDepositors() throws Exception {
        Map depositsByNameAllDepositors = DataFixture.getExistDepositAllDepositors(1L,2L);
        LOGGER.debug("depositsByNameAllDepositors: {}",depositsByNameAllDepositors);

        expect(depositDao.getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-01-02"))).andReturn(depositsByNameAllDepositors);
        replay(depositDao);

        Map resultDeposits = depositService.getBankDepositByNameFromToDateDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-01-02"));
        LOGGER.debug("result deposit: {}",resultDeposits);

        verify(depositDao);

        assertEquals(depositsByNameAllDepositors, resultDeposits);
        assertSame(depositsByNameAllDepositors, resultDeposits);
    }

    @Test
    public void testGetBankDepositByNameFromToDateReturnDepositWithDepositors() throws Exception {
        Map depositByNameAllDepositors = DataFixture.getExistDepositAllDepositors(1L,2L);
        LOGGER.debug("depositsByNameAllDepositors: {}",depositByNameAllDepositors);

        expect(depositDao.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-01-02"))).andReturn(depositByNameAllDepositors);
        replay(depositDao);

        Map resultDeposit = depositService.getBankDepositByNameFromToDateReturnDepositWithDepositors("depositName1",
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-01-02"));
        LOGGER.debug("result deposit: {}",resultDeposit);

        verify(depositDao);

        assertEquals(depositByNameAllDepositors, resultDeposit);
        assertSame(depositByNameAllDepositors, resultDeposit);
    }

    @Test
    public void testGetBankDepositByIdWithDepositors() throws Exception {
        Map deposit = DataFixture.getExistDepositAllDepositors(1L,2L);
        LOGGER.debug("deposit: {}",deposit);

        expect(depositDao.getBankDepositByIdWithDepositors(1L)).andReturn(deposit);

        replay(depositDao);

        Map resultDeposit = depositService.getBankDepositByIdWithDepositors(1L);
        LOGGER.debug("result deposit: {}",resultDeposit);

        verify(depositDao);

        assertEquals(deposit, resultDeposit);
        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByIdFromToDateDepositWithDepositors() throws Exception {
        Map deposit = DataFixture.getExistDepositAllDepositors(1L,2L);
        LOGGER.debug("deposit: {}",deposit);

        expect(depositDao.getBankDepositByIdFromToDateDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02"))).andReturn(deposit);
        replay(depositDao);

        Map resultDeposit = depositService.getBankDepositByIdFromToDateDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"),dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposit: {}",resultDeposit);

        verify(depositDao);

        assertEquals(deposit, resultDeposit);
        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositByIdFromToDateReturnDepositWithDepositors() throws Exception {
        Map deposit = DataFixture.getExistDepositAllDepositors(1L,2L);
        LOGGER.debug("deposit: {}",deposit);

        expect(depositDao.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02"))).andReturn(deposit);
        replay(depositDao);

        Map resultDeposit = depositService.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,
                dateFormat.parse("2015-01-01"), dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposit: {}",resultDeposit);

        verify(depositDao);

        assertEquals(deposit, resultDeposit);
        assertSame(deposit, resultDeposit);
    }

    @Test
    public void testGetBankDepositsWithDepositors() throws Exception {
        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.debug("depositors: {}", deposits);

        expect(depositDao.getBankDepositsWithDepositors()).andReturn(deposits);

        replay(depositDao,depositorDao);

        List<Map> resultDeposits = depositService.getBankDepositsWithDepositors();
        LOGGER.debug("resultDeposits: {}",resultDeposits);

        verify(depositDao,depositorDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositsFromToDateDepositWithDepositors() throws Exception {
        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.debug("depositors: {}", deposits);

        expect(depositDao.getBankDepositsFromToDateDepositWithDepositors(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"))).andReturn(deposits);
        replay(depositorDao,depositDao);

        List<Map> resultDeposits =
                depositService.getBankDepositsFromToDateDepositWithDepositors(dateFormat.parse("2015-01-01"),
                                                                                dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposits: {}",resultDeposits);

        verify(depositorDao,depositDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDepositWithDepositors() throws Exception {
        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.debug("depositors: {}", deposits);

        expect(depositDao.getBankDepositsFromToDateReturnDepositWithDepositors(dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"))).andReturn(deposits);
        replay(depositDao);

        List<Map> resultDeposits =
                depositService.getBankDepositsFromToDateReturnDepositWithDepositors(dateFormat.parse("2015-01-01"),
                        dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposits: {}",resultDeposits);

        verify(depositDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositsByCurrencyWithDepositors() throws Exception {
        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.debug("deposit: {}",deposits);

        expect(depositDao.getBankDepositsByCurrencyWithDepositors("usd")).andReturn(deposits);

        replay(depositDao);

        List<Map> resultDeposits = depositService.getBankDepositsByCurrencyWithDepositors("usd");
        LOGGER.debug("result deposit: {}",resultDeposits);

        verify(depositDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateDepositWithDepositors() throws Exception {
        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.debug("depositors: {}", deposits);

        expect(depositDao.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"))).andReturn(deposits);
        replay(depositDao);

        List<Map> resultDeposits =
                depositService.getBankDepositsByCurrencyFromToDateDepositWithDepositors("usd",dateFormat.parse("2015-01-01"),
                        dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposits: {}",resultDeposits);

        verify(depositDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testGetBankDepositByCurrencyFromToDateReturnDepositWithDepositors() throws Exception {
        List<Map> deposits = DataFixture.getExistAllDepositsAllDepositors();
        LOGGER.debug("depositors: {}", deposits);

        expect(depositDao.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd",dateFormat.parse("2015-01-01"),
                dateFormat.parse("2015-02-02"))).andReturn(deposits);
        replay(depositDao);

        List<Map> resultDeposits =
                depositService.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors("usd", dateFormat.parse("2015-01-01"),
                        dateFormat.parse("2015-02-02"));
        LOGGER.debug("resultDeposits: {}",resultDeposits);

        verify(depositDao);

        assertEquals(deposits, resultDeposits);
        assertSame(deposits, resultDeposits);
    }

    @Test
    public void testAddBankDeposit() throws Exception {
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.debug("new deposit = {}",deposit);

        depositDao.addBankDeposit(deposit);
        expectLastCall();

        expect(depositDao.getBankDepositByNameCriteria(deposit.getDepositName())).andReturn(null);

        replay(depositDao);

        depositService.addBankDeposit(deposit);

        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddTwoDeposit() {
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.debug("new deposit = {}",deposit);

        depositDao.addBankDeposit(deposit);
        expectLastCall().times(2);

        depositDao.getBankDepositByNameCriteria(deposit.getDepositName());
        expectLastCall().andReturn(deposit).times(2);

        replay(depositDao);

        depositService.addBankDeposit(deposit);
        depositService.addBankDeposit(deposit);

        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDepositWithSameName() {
        BankDeposit deposit = DataFixture.getNewDeposit();
        LOGGER.debug("new deposit = {}",deposit);

        depositDao.addBankDeposit(deposit);
        expectLastCall();

        expect(depositDao.getBankDepositByNameCriteria(deposit.getDepositName())).andReturn(deposit);
        replay(depositDao);

        depositService.addBankDeposit(deposit);

        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNotNullIdDeposit() {
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.debug("deposit = {}",deposit);

        depositDao.addBankDeposit(deposit);
        expectLastCall();

        expect(depositDao.getBankDepositByNameCriteria(deposit.getDepositName())).andReturn(null);
        replay(depositDao);

        depositService.addBankDeposit(deposit);

        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullDeposit() {
        BankDeposit deposit = DataFixture.getNullDeposit();
        LOGGER.debug("null deposit = {}",deposit);

        depositDao.addBankDeposit(deposit);
        expectLastCall();

        expect(depositDao.getBankDepositByNameCriteria("name")).andReturn(null);
        replay(depositDao);

        depositService.addBankDeposit(deposit);

        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmptyDeposit(){
        BankDeposit deposit = DataFixture.getEmptyDeposit();
        LOGGER.debug("empty deposit = {}", deposit);

        expect(depositDao.getBankDepositByNameCriteria(deposit.getDepositName())).andReturn(null);

        depositDao.addBankDeposit(deposit);
        expectLastCall();

        replay(depositDao);

        depositService.addBankDeposit(deposit);

        verify(depositDao);
    }

    @Test
    public void testUpdateBankDeposit() throws Exception {
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.debug("deposit = {}", deposit);

        expect(depositDao.getBankDepositByIdCriteria(deposit.getDepositId())).andReturn(deposit);

        depositDao.updateBankDeposit(deposit);
        expectLastCall();

        replay(depositDao);

        depositService.updateBankDeposit(deposit);

        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEmptyDeposit(){
        BankDeposit deposit = DataFixture.getEmptyDeposit();
        LOGGER.debug("empty deposit = {}", deposit);

        expect(depositDao.getBankDepositByIdCriteria(deposit.getDepositId())).andReturn(deposit);

        depositDao.updateBankDeposit(deposit);
        expectLastCall();

        replay(depositDao);

        depositService.updateBankDeposit(deposit);

        verify(depositDao);
    }

    @Test
    public void testDeleteBankDeposit() throws Exception {
        BankDeposit deposit = DataFixture.getExistDeposit(1L);
        LOGGER.debug("deposit = {}", deposit);

        expect(depositDao.getBankDepositByIdCriteria(deposit.getDepositId())).andReturn(deposit);

        depositDao.deleteBankDeposit(deposit.getDepositId());
        expectLastCall();

        replay(depositDao);

        depositService.deleteBankDeposit(deposit.getDepositId());

        verify(depositDao);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullIdDeposit(){
        depositService.deleteBankDeposit(null);
    }
}