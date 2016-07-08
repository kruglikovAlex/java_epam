package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositDaoImplTest {

    private static final Logger LOGGER = LogManager.getLogger(BankDepositDaoImplTest.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String ERROR_EMPTY_BD = "Data Base is empty";
    private static final String ERROR_SIZE = "Size can not be 0";
    private static final String ERROR_NULL = "The parameter can not be NULL";
    private static final String ERROR_EMPTY_RESULT = "The result can not be empty";
    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0;

    @Autowired
    private BankDepositDao depositDao;

    private BankDeposit deposit;
    private List<BankDeposit> deposits;

    @Before
    public void setUp() throws Exception {
        deposit = new BankDeposit();
        deposits = new ArrayList<BankDeposit>();
        deposits = depositDao.getBankDepositsCriteria();
    }

    @Test
    public void testGetBankDepositsCriteria() throws Exception {
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

        assertNotNull(ERROR_NULL,deposit);
        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, depositMinAmount=200, " +
                "depositCurrency=eur, depositInterestRate=5, depositAddConditions=condition1}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName1");
        LOGGER.debug("deposit = {}", deposit);

        assertNotNull(ERROR_NULL,deposit);
        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, depositMinAmount=200, " +
                "depositCurrency=eur, depositInterestRate=5, depositAddConditions=condition1}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByCurrencyCriteria() throws Exception{
        deposits = depositDao.getBankDepositsByCurrencyCriteria("usd");
        LOGGER.debug("deposits: {}",deposits);

        assertNotNull(ERROR_NULL,deposits);
        assertEquals("[BankDeposit: { depositId=0, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=15, depositMinAmount=400, " +
                "depositCurrency=usd, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositByInterestRateCriteria() throws Exception{
        deposits = depositDao.getBankDepositsByInterestRateCriteria(4);
        LOGGER.debug("deposits: {}",deposits);

        assertNotNull(ERROR_NULL,deposits);
        assertEquals("[BankDeposit: { depositId=0, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=5, depositName=name, depositMinTerm=24, depositMinAmount=1000, " +
                "depositCurrency=grb, depositInterestRate=4, depositAddConditions=condition}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositFromToMinTermCriteria() throws Exception{
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(13,15);
        LOGGER.debug("deposits: {}",deposits);

        assertNotNull(ERROR_NULL,deposits);
        assertEquals("[BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, depositMinAmount=200, " +
                "depositCurrency=eur, depositInterestRate=5, depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=15, depositMinAmount=400, " +
                "depositCurrency=usd, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testRowCount(){
        Integer count = depositDao.rowCount();
        LOGGER.debug("count = {}",count);
        assertTrue(count == deposits.size());
    }

    @Test
    public void testAddBankDeposit() throws Exception {
        deposit = new BankDeposit();
            deposit.setDepositName("name");
            deposit.setDepositMinTerm(24);
            deposit.setDepositMinAmount(1000);
            deposit.setDepositCurrency("grb");
            deposit.setDepositInterestRate(4);
            deposit.setDepositAddConditions("condition");
        LOGGER.debug("new deposit - {}",deposit);

        sizeBefore = depositDao.rowCount();
        LOGGER.debug("size before add = {}",sizeBefore);

        LOGGER.debug("start added new deposit - {}",deposit);
        depositDao.addBankDeposit(deposit);
        LOGGER.debug("new deposit was added - {}",deposit);

        sizeAfter = depositDao.rowCount();
        LOGGER.debug("size after add = {}",sizeAfter);

        assertTrue(sizeAfter == sizeBefore+1);
    }
}