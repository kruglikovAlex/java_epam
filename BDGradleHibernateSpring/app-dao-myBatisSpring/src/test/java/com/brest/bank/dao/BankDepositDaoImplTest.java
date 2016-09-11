package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-mybatisSpring-test.xml"})
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

    @Autowired
    private BankDepositorDao depositorDao;

    private BankDeposit deposit;
    private BankDepositor depositor;
    private List<BankDeposit> deposits;
    private List<BankDepositor> depositors;

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
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositByInterestRateCriteria() throws Exception{
        deposits = depositDao.getBankDepositsByInterestRateCriteria(4);
        LOGGER.debug("deposits: {}",deposits);

        assertNotNull(ERROR_NULL,deposits);
        assertEquals("[BankDeposit: { depositId=0, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}]",deposits.toString());
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
                "depositCurrency=eur, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositFromToInterestRateCriteria() throws Exception{
        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(5,6);
        LOGGER.debug("deposits: {}",deposits);

        assertNotNull(ERROR_NULL,deposits);
        assertEquals("[BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, depositMinAmount=200, " +
                "depositCurrency=eur, depositInterestRate=5, depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsBetweenDateDeposit() throws Exception {
        Date startDate = dateFormat.parse("2015-12-01");
        Date endDate = dateFormat.parse("2015-12-02");

        deposits = depositDao.getBankDepositsFromToDateDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);

        assertEquals("[BankDeposit: { depositId=0, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsBetweenDateReturnDeposit() throws Exception {
        Date startDate = dateFormat.parse("2015-12-02");
        Date endDate = dateFormat.parse("2015-12-03");

        deposits = depositDao.getBankDepositsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);

        assertEquals("[BankDeposit: { depositId=0, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }
    @Test
    public void testGetBankDepositByNameWithDepositors() throws ParseException {
        int sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;
        String name = "depositName1";

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        Map list = depositDao.getBankDepositByNameWithDepositors(name);
        LOGGER.debug("list = {}", list);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByNameFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName0";
        Date start = dateFormat.parse("2015-12-01");
        Date end = dateFormat.parse("2015-12-06");

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByNameFromToDateDepositWithDepositors(name,start,end);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByNameFromToDateReturnDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName0";
        Date start = dateFormat.parse("2015-12-02");
        Date end = dateFormat.parse("2015-12-07");

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByNameFromToDateReturnDepositWithDepositors(name,start,end);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByIdWithDepositors() throws ParseException {
        int sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;
        String name = "depositName1";

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByIdWithDepositors(1L);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByIdFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        Long id = 0L;
        Date start = dateFormat.parse("2015-12-01");
        Date end = dateFormat.parse("2015-12-06");

        deposit = depositDao.getBankDepositByIdCriteria(0L);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByIdFromToDateDepositWithDepositors(id,start,end);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByIdFromToDateReturnDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        Long id = 0L;
        Date start = dateFormat.parse("2015-12-01");
        Date end = dateFormat.parse("2015-12-07");

        deposit = depositDao.getBankDepositByIdCriteria(0L);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByIdFromToDateReturnDepositWithDepositors(id,start,end);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositsByTermWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Integer term = 14;

        deposits = depositDao.getBankDepositsFromToMinTermCriteria(14,14);
        LOGGER.debug("deposits = {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId()));
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByTermWithDepositors(term);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByAmountWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Integer amount = 200;

        deposits = new ArrayList<BankDeposit>();
        deposits.add(depositDao.getBankDepositByIdCriteria(1L));
        LOGGER.debug("deposits = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(1L);

        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByAmountWithDepositors(amount);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByRateWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Integer rate = 5;

        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(5,5);
        LOGGER.debug("deposits = {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId()));
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByRateWithDepositors(rate);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositByDepositorIdWithDepositors() throws ParseException{
        int     sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;

        deposit = depositDao.getBankDepositByIdCriteria(0L);
        LOGGER.debug("deposit = {}", deposit);

        depositor = depositorDao.getBankDepositorByIdCriteria(1L);
        LOGGER.debug("depositor={}",depositor);

        sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
        sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
        sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();


        Map list = depositDao.getBankDepositByDepositorIdWithDepositors(1L);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL, list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByDepositorNameWithDepositors() throws ParseException{
        int     sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;

        deposit = depositDao.getBankDepositByIdCriteria(0L);
        LOGGER.debug("deposit = {}", deposit);

        depositor = depositorDao.getBankDepositorByIdCriteria(1L);
        LOGGER.debug("depositor={}",depositor);

        sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
        sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
        sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();

        Map list = depositDao.getBankDepositByDepositorNameWithDepositors("depositorName6");
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL, list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit ==
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositsByDepositorAmountWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Integer amount = 997;

        depositors = new ArrayList<BankDepositor>();
        for(BankDepositor depr: depositorDao.getBankDepositorsCriteria()){
            if((amount<=depr.getDepositorAmountDeposit()) && (depr.getDepositorAmountDeposit()<=(amount+3))){
                depositors.add(depr);
            }
        }
        LOGGER.debug("depositors={}",depositors);

        deposits = new ArrayList<BankDeposit>();
        for(BankDepositor depr:depositors){
            deposits.add(depositDao.getBankDepositByIdCriteria(depr.getDepositId()));
        }
        LOGGER.debug("deposits = {}", deposits);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByDepositorAmountWithDepositors(amount,amount+3);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsWithDepositors() throws Exception {
        LOGGER.debug("deposits.size()= {}", deposits.size());

        List<Map> list = depositDao.getBankDepositsWithDepositors();
        LOGGER.debug("list size={},\nlist = {}", list.size(),list);

        assertFalse(ERROR_EMPTY_BD,list.isEmpty());
        assertThat(ERROR_SIZE,list.size(), is(not(0)));
        assertNotNull(ERROR_NULL,list);

        assertEquals(deposits.size(), list.size());
        for (int i=0; i<list.size(); i++) {
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositId(), list.get(i).get("depositId"));
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositName(), list.get(i).get("depositName"));
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositMinTerm(), list.get(i).get("depositMinTerm"));
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositMinAmount(), list.get(i).get("depositMinAmount"));
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositCurrency(), list.get(i).get("depositCurrency"));
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositInterestRate(), list.get(i).get("depositInterestRate"));
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositAddConditions(), list.get(i).get("depositAddConditions"));
            LOGGER.debug("i-{}, deposits-{}, list-{}",i,deposits.get(i).getDepositors(), list.get(i).get("depositors"));
        }

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
    public void testGetBankDepositsFromToDateDepositWithDepositors() throws Exception {
        int[]   sumAmountDeposit =      new int[]{2003, 0},
                sumAmountPlusDeposit =  new int[]{50, 0},
                sumAmountMinusDeposit = new int[]{50, 0};

        Date startDate = dateFormat.parse("2015-12-01");
        Date endDate = dateFormat.parse("2015-12-02");

        deposits = depositDao.getBankDepositsFromToDateDeposit(startDate, endDate);
        LOGGER.debug("deposits size={},\ndeposits = {}", deposits.size(),deposits);

        List<Map> list = depositDao.getBankDepositsFromToDateDepositWithDepositors(startDate,endDate);
        LOGGER.debug("list size={},\nlist = {}", list.size(),list);

        assertFalse(ERROR_EMPTY_BD,list.isEmpty());
        assertThat(ERROR_SIZE,list.size(), is(not(0)));
        assertNotNull(ERROR_NULL,list);

        assertTrue(list.size()==deposits.size());

        int i=0;
        while (i<list.size()) {
            assertEquals(deposits.get(i).getDepositId(), list.get(i).get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), list.get(i).get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), list.get(i).get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), list.get(i).get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), list.get(i).get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), list.get(i).get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), list.get(i).get("depositAddConditions"));
            sumAmountDeposit[1] +=       Integer.parseInt(list.get(i).get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=   Integer.parseInt(list.get(i).get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] +=  Integer.parseInt(list.get(i).get("depositorAmountMinusSum").toString());
            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsFromToDateReturnDepositWithDepositors() throws Exception {
        int[]   sumAmountDeposit =      new int[]{2003, 0},
                sumAmountPlusDeposit =  new int[]{50, 0},
                sumAmountMinusDeposit = new int[]{50, 0};

        Date startDate = dateFormat.parse("2015-12-02");
        Date endDate = dateFormat.parse("2015-12-03");

        deposits = depositDao.getBankDepositsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("deposits size={},\ndeposits = {}", deposits.size(),deposits);

        List<Map> list = depositDao.getBankDepositsFromToDateReturnDepositWithDepositors(startDate,endDate);
        LOGGER.debug("list size={},\nlist = {}", list.size(),list);

        assertFalse(ERROR_EMPTY_BD,list.isEmpty());
        assertThat(ERROR_SIZE,list.size(), is(not(0)));
        assertNotNull(ERROR_NULL,list);

        assertTrue(list.size()==deposits.size());

        int i=0;
        while (i<list.size()) {
            assertEquals(deposits.get(i).getDepositId(), list.get(i).get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), list.get(i).get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), list.get(i).get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), list.get(i).get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), list.get(i).get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), list.get(i).get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), list.get(i).get("depositAddConditions"));
            sumAmountDeposit[1] +=       Integer.parseInt(list.get(i).get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=   Integer.parseInt(list.get(i).get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] +=  Integer.parseInt(list.get(i).get("depositorAmountMinusSum").toString());
            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByCurrencyWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String currency = "eur";

        deposits = depositDao.getBankDepositsByCurrencyCriteria(currency);
        LOGGER.debug("deposits = {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId()));
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByCurrencyWithDepositors(currency);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateDepositWithDepositors() throws ParseException{
        Date startDate = dateFormat.parse("2015-12-01");
        Date endDate = dateFormat.parse("2015-12-05");

        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String currency = "eur";

        deposits = depositDao.getBankDepositsByCurrencyCriteria(currency);
        LOGGER.debug("deposits = {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId()));
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByCurrencyFromToDateDepositWithDepositors(currency,startDate,endDate);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] >= sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] >= sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] >= sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateReturnDepositWithDepositors() throws ParseException{
        Date startDate = dateFormat.parse("2015-12-05");
        Date endDate = dateFormat.parse("2015-12-10");

        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String currency = "eur";

        deposits = depositDao.getBankDepositsByCurrencyCriteria(currency);
        LOGGER.debug("deposits = {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId()));
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(currency,startDate,endDate);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] >= sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] >= sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] >= sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByDepositorMarkReturnWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Integer mark = 1;

        depositors = new ArrayList<BankDepositor>();
        for(BankDepositor depr:depositorDao.getBankDepositorsCriteria()){
            if(depr.getDepositorMarkReturnDeposit() == mark){
                depositors.add(depr);
            }
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        deposits = new ArrayList<BankDeposit>();
        for(BankDeposit dep:depositDao.getBankDepositsCriteria()){
            for(BankDepositor depr:depositors){
                if(dep.getDepositId().equals(depr.getDepositId())){
                    if(!deposits.contains(dep)){
                        deposits.add(dep);
                        LOGGER.debug("deposits = {}", deposits);
                    }
                }
            }
        }
        LOGGER.debug("deposits = {}", deposits);

        List<Map> list = depositDao.getBankDepositsByDepositorMarkReturnWithDepositors(mark);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByVarArgsIdInterestRate() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField2 = "deposit.depositInterestRate";
        Integer rate = 6;
        String nameAliasField3 = "deposit.depositId";
        Long id = 2L;
        deposits = new ArrayList<BankDeposit>();

        deposits = new ArrayList<BankDeposit>();
        deposits.add(depositDao.getBankDepositByIdCriteria(id));
        LOGGER.debug("deposits - IR = {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId()));
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(
                nameAliasField2,rate,
                nameAliasField3,id);

        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByVarArgsNameCurrency() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "eur";
        String nameAliasField4 = "deposit.depositName";
        String name = "depositName1";

        deposits = new ArrayList<BankDeposit>();

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        for(BankDeposit d:depositDao.getBankDepositsByCurrencyCriteria(currency)){
            if(deposit.toString().equals(d.toString())){
                deposits.add(deposit);
            }
        }
        LOGGER.debug("deposits {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId()));
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(
                nameAliasField1,currency,
                nameAliasField4,name);

        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByVarArgsDateDeposit() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField5 = "depositor.depositorDateDepositLe";
        Date dateDeposit = dateFormat.parse("2015-12-05");
        String nameAliasField6 = "depositor.depositorDateDepositGe";
        Date dateDeposit2 = dateFormat.parse("2015-12-12");

        deposits = new ArrayList<BankDeposit>();

        deposits = depositDao.getBankDepositsFromToDateDeposit(dateDeposit,dateDeposit2);
        LOGGER.debug("deposits: {}", deposits);

        depositors = depositorDao.getBankDepositorsFromToDateDeposit(dateDeposit,dateDeposit2);
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(
                nameAliasField5,dateDeposit,
                nameAliasField6,dateDeposit2);

        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByVarArgsTermAmountMark() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField9 = "deposit.depositMinTerm";
        Integer term = 13;
        String nameAliasField10 = "deposit.depositMinAmount";
        Integer amount = 200;
        String nameAliasField11 = "depositor.depositorMarkReturnDeposit";
        Integer mark = 0;


        deposits = new ArrayList<BankDeposit>();
        List<BankDeposit> depositsMTA = new ArrayList<BankDeposit>();

        List<BankDeposit> depositsTerm = depositDao.getBankDepositsFromToMinTermCriteria(13,13);
        LOGGER.debug("depositsTerm = {}", depositsTerm);

        for(BankDeposit dMT:depositsTerm){
            for(Map d:depositDao.getBankDepositsByAmountWithDepositors(amount)){
                if(dMT.getDepositId().equals(d.get("depositId"))){
                    depositsMTA.add(dMT);
                }
            }
        }
        LOGGER.debug("deposits - MTA = {}", depositsMTA);

        for(BankDeposit dMTAM:depositsMTA){
            for(Map d:depositDao.getBankDepositsByDepositorMarkReturnWithDepositors(mark))
                if(dMTAM.getDepositId().equals(d.get("depositId"))){
                    deposits.add(dMTAM);
                }
        }
        LOGGER.debug("deposits - MTAM = {}", deposits);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            for(BankDepositor depr:depositorDao.getBankDepositorByIdDepositCriteria(d.getDepositId())){
                if(depr.getDepositorMarkReturnDeposit() != 1){
                    depositors.add(depr);
                }
            }
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(
                nameAliasField9,term,
                nameAliasField10,amount,
                nameAliasField11,mark);

        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositsByVarArgs() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "eur";
        String nameAliasField4 = "deposit.depositName";
        String name = "depositName2";
        String nameAliasField2 = "deposit.depositInterestRate";
        Integer rate = 5;
        String nameAliasField3 = "deposit.depositId";
        Long id = 1L;
        String nameAliasField5 = "depositor.depositorDateDeposit";
        Date dateDeposit = dateFormat.parse("2015-12-05");
        String nameAliasField6 = "depositor.depositorDateDeposit";
        Date dateDeposit2 = dateFormat.parse("2015-12-12");
        String nameAliasField7 = "depositor.depositorDateReturnDeposit";
        Date dateDeposit3 = dateFormat.parse("2015-12-06");
        String nameAliasField8 = "depositor.depositorDateReturnDeposit";
        Date dateDeposit4 = dateFormat.parse("2015-12-12");
        String nameAliasField9 = "deposit.depositMinTerm";
        Integer term = 13;
        String nameAliasField10 = "deposit.depositMinAmount";
        Integer amount = 200;
        String nameAliasField11 = "depositor.depositorMarkReturnDeposit";
        Integer mark = 0;


        deposits = new ArrayList<BankDeposit>();

        deposits.add(depositDao.getBankDepositByIdCriteria(id));
        //deposits.add(depositDao.getBankDepositByNameCriteria(name));
        LOGGER.debug("deposits - IR = {}", deposits);

        List<BankDepositor> depositorsDDR = new ArrayList<BankDepositor>();
        List<BankDepositor> depositorsDR = depositorDao.getBankDepositorsFromToDateDeposit(dateDeposit,dateDeposit2);
        LOGGER.debug("depositorsDR={}",depositorsDR);
        for(BankDepositor d:depositorDao.getBankDepositorsFromToDateReturnDeposit(dateDeposit3,dateDeposit4)){
            for(BankDepositor dr:depositorsDR){
                if(d.getDepositorId().equals(dr.getDepositorId())){
                    depositorsDDR.add(dr);
                }
            }
        }
        LOGGER.debug("depositorsDDR={}",depositorsDDR);

        depositors = new ArrayList<BankDepositor>();
        for(BankDeposit d: deposits){
            for(BankDepositor depr:depositorsDDR){
                if(depr.getDepositId().equals(d.getDepositId())){
                    depositors.add(depr);
                }
            }
        }
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }
        LOGGER.debug("sum={}, {}, {}",sumAmountDeposit[0],sumAmountMinusDeposit[0],sumAmountPlusDeposit[0]);

        List<Map> list = depositDao.getBankDepositsByVarArgs(
                nameAliasField1,currency,
                nameAliasField2,rate,
                nameAliasField3,id,
                nameAliasField4,name,
                nameAliasField5,dateDeposit,
                nameAliasField6,dateDeposit2,
                nameAliasField7,dateDeposit3,
                nameAliasField8,dateDeposit4,
                nameAliasField9,term,
                nameAliasField10,amount,
                nameAliasField11,mark);

        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));

            sumAmountDeposit[1] +=      Integer.parseInt(aList.get("depositorAmountSum").toString());
            sumAmountPlusDeposit[1] +=  Integer.parseInt(aList.get("depositorAmountPlusSum").toString());
            sumAmountMinusDeposit[1] += Integer.parseInt(aList.get("depositorAmountMinusSum").toString());

            i++;
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testUpdateBankDeposit(){
        String testDeposit;

        deposit = deposits.get(4);
        Long id = deposit.getDepositId();

        testDeposit = deposit.toString();

        deposit.setDepositName("UpdateDepositName");
        deposit.setDepositMinAmount(10);
        deposit.setDepositCurrency("eur");
        deposit.setDepositAddConditions("UpdateConditions");

        depositDao.updateBankDeposit(deposit);

        assertNotEquals(depositDao.getBankDepositByIdCriteria(id).toString(),testDeposit);
        assertEquals(depositDao.getBankDepositByIdCriteria(id).toString(),deposit.toString());
    }

    @Test
    public void testRemoveBankDeposit() {
        assertNotNull(deposits);
        assertFalse(deposits.isEmpty());
        sizeBefore = deposits.size();
        LOGGER.debug("size before - {}",sizeBefore);
        depositDao.deleteBankDeposit(4L);
        deposits = depositDao.getBankDepositsCriteria();
        LOGGER.debug("size after - {}",deposits.size());
        assertEquals(sizeBefore-1,deposits.size());
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
        deposit.setDepositName("testName");
        deposit.setDepositMinTerm(24);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("grb");
        deposit.setDepositInterestRate(9);
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