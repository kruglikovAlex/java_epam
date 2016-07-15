package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.text.ParseException;
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

        assertEquals("[BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, depositMinAmount=200, " +
                "depositCurrency=eur, depositInterestRate=5, depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=15, depositMinAmount=400, " +
                "depositCurrency=usd, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsBetweenDateReturnDeposit() throws Exception {
        Date startDate = dateFormat.parse("2015-12-06");
        Date endDate = dateFormat.parse("2015-12-08");

        deposits = depositDao.getBankDepositsFromToDateDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);

        assertEquals("[BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, depositMinAmount=200, " +
                "depositCurrency=eur, depositInterestRate=5, depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=15, depositMinAmount=400, " +
                "depositCurrency=usd, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws ParseException {
        int sumAmountDeposit = 2000,
                sumAmountPlusDeposit = 80,
                sumAmountMinusDeposit = 80;
        String name = "depositName1";

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        Map list = depositDao.getBankDepositByNameWithDepositors(name);
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
    public void testGetBankDepositByNameFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=1001,
                sumAmountPlusDeposit=20,
                sumAmountMinusDeposit=20;
        String name = "depositName1";
        Date start = dateFormat.parse("2015-12-01");
        Date end = dateFormat.parse("2015-12-05");

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

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
        Integer sumAmountDeposit=999,
                sumAmountPlusDeposit=60,
                sumAmountMinusDeposit=60;
        String name = "depositName1";
        Date start = dateFormat.parse("2015-12-06");
        Date end = dateFormat.parse("2015-12-07");

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

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
        int sumAmountDeposit = 2000,
                sumAmountPlusDeposit = 80,
                sumAmountMinusDeposit = 80;
        String name = "depositName1";

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

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
        Integer sumAmountDeposit=1001,
                sumAmountPlusDeposit=20,
                sumAmountMinusDeposit=20;
        Long id = 1L;
        Date start = dateFormat.parse("2015-12-01");
        Date end = dateFormat.parse("2015-12-05");

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

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
        Integer sumAmountDeposit=999,
                sumAmountPlusDeposit=60,
                sumAmountMinusDeposit=60;
        Long id = 1L;
        Date start = dateFormat.parse("2015-12-06");
        Date end = dateFormat.parse("2015-12-07");

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

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
        int[]   sumAmountDeposit =      new int[]{2000, 0},
                sumAmountPlusDeposit =  new int[]{120, 0},
                sumAmountMinusDeposit = new int[]{120, 0};

        Integer term = 14;

        deposits = depositDao.getBankDepositsFromToMinTermCriteria(14,14);
        LOGGER.debug("deposits = {}", deposits);

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
        int[]   sumAmountDeposit =      new int[]{2000, 0},
                sumAmountPlusDeposit =  new int[]{80, 0},
                sumAmountMinusDeposit = new int[]{80, 0};

        Integer amount = 200;

        deposits = new ArrayList<BankDeposit>();
        deposits.add(depositDao.getBankDepositByIdCriteria(1L));
        LOGGER.debug("deposits = {}", deposit);

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
        int[]   sumAmountDeposit =      new int[]{2000, 0},
                sumAmountPlusDeposit =  new int[]{80, 0},
                sumAmountMinusDeposit = new int[]{80, 0};

        Integer rate = 5;

        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(5,5);
        LOGGER.debug("deposits = {}", deposits);

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
        depositDao.deleteBankDeposit(3L);
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