package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-daoSpring-test.xml"})
public class BankDepositDaoImplTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    BankDepositDao depositDao;

    @Autowired
    BankDepositorDao depositorDao;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String ERROR_EMPTY_BD = "Data Base is empty";
    private static final String ERROR_SIZE = "Size can not be 0";
    private static final String ERROR_NULL = "The parameter can not be NULL";

    private BankDeposit deposit;
    private BankDepositor depositor;
    private List<BankDeposit> deposits;
    private List<BankDepositor> depositors;

    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0;

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
        assertEquals("BankDeposit: { depositId=1, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName0");
        LOGGER.debug("deposit = {}", deposit);

        assertNotNull(ERROR_NULL,deposit);
        assertEquals("BankDeposit: { depositId=1, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByCurrencyCriteria(){
        deposits = depositDao.getBankDepositsByCurrencyCriteria("usd");
        LOGGER.debug("deposits: {}", deposits);

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue("usd".equalsIgnoreCase(aDeposit.getDepositCurrency()));
        }
    }

    @Test
    public void testGetBankDepositsByInterestRateCriteria(){
        deposits = depositDao.getBankDepositsByInterestRateCriteria(6);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue(6==aDeposit.getDepositInterestRate());
        }
    }

    @Test
    public void testGetBankDepositsFromToMinTermCriteria(){
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(13, 14);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue((13==aDeposit.getDepositMinTerm())||(14==aDeposit.getDepositMinTerm()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBankDepositsFromToMinTermCriteriaNullFirstArgs(){
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(null, 14);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBankDepositsFromToMinTermCriteriaNullSecondArgs(){
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(13, null);
    }

    @Test
    public void testGetBankDepositsFromToInterestRateCriteria(){
        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(5,6);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(ERROR_EMPTY_BD,deposits.isEmpty());
        assertThat(ERROR_SIZE,deposits.size(), is(not(0)));
        assertNotNull(ERROR_NULL,deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue((5==aDeposit.getDepositInterestRate())||(6==aDeposit.getDepositInterestRate()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBankDepositsFromToInterestRateCriteriaNullFirstArgs(){
        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(null, 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBankDepositsFromToInterestRateCriteriaNullSecondArgs(){
        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(5, null);
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

        assertEquals("[BankDeposit: { depositId=1, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
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

        assertEquals("[BankDeposit: { depositId=1, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
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
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Date startDate = dateFormat.parse("2015-12-01");
        Date endDate = dateFormat.parse("2015-12-02");

        deposits = depositDao.getBankDepositsFromToDateDeposit(startDate, endDate);
        LOGGER.debug("deposits size={},\ndeposits = {}", deposits.size(),deposits);

        List<BankDepositor> depositors = depositorDao.getBankDepositorsFromToDateDeposit(startDate,endDate);
        LOGGER.debug("depositors size={},\ndepositors = {}", depositors.size(),depositors);

        for (BankDepositor depositor1 : depositors) {
            sumAmountDeposit[0] += depositor1.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] += depositor1.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor1.getDepositorAmountMinusDeposit();
        }

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
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Date startDate = dateFormat.parse("2015-12-02");
        Date endDate = dateFormat.parse("2015-12-03");

        deposits = depositDao.getBankDepositsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("deposits size={},\ndeposits = {}", deposits.size(),deposits);

        List<BankDepositor> depositors = depositorDao.getBankDepositorsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("depositors size={},\ndepositors = {}", depositors.size(), depositors);

        for (BankDepositor depositor1 : depositors) {
            sumAmountDeposit[0] += depositor1.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] += depositor1.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor1.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsFromToDateReturnDepositWithDepositors(startDate, endDate);
        LOGGER.debug("list size={},\nlist = {}", list.size(), list);

        assertFalse(ERROR_EMPTY_BD, list.isEmpty());
        assertThat(ERROR_SIZE, list.size(), is(not(0)));
        assertNotNull(ERROR_NULL,list);

        assertTrue(list.size() == deposits.size());

        for (int i=0; i<list.size(); i++) {
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
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws ParseException{
        int sumAmountDeposit = 0,
            sumAmountPlusDeposit = 0,
            sumAmountMinusDeposit = 0;
        String name = "depositName0";

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

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
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName0";
        Date start = dateFormat.parse("2015-12-01");
        Date end = dateFormat.parse("2015-12-05");

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

        assertTrue("Error sum amount", sumAmountDeposit >=
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
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

        Map list = depositDao.getBankDepositByNameFromToDateReturnDepositWithDepositors(name, start, end);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit >=
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByIdWithDepositors() throws ParseException{
        int sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByIdWithDepositors(deposit.getDepositId());
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
    public void testGetBankDepositByIdFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;

        Date start = dateFormat.parse("2015-12-01");
        Date end = dateFormat.parse("2015-12-05");

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByIdFromToDateDepositWithDepositors(deposit.getDepositId(), start, end);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit >=
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositByIdFromToDateReturnDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        Date start = dateFormat.parse("2015-12-02");
        Date end = dateFormat.parse("2015-12-07");

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(deposit.getDepositId());
        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();
        }

        Map list = depositDao.getBankDepositByIdFromToDateReturnDepositWithDepositors(deposit.getDepositId(), start, end);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);

        assertEquals(deposit.getDepositId(), list.get("depositId"));
        assertEquals(deposit.getDepositName(), list.get("depositName"));
        assertEquals(deposit.getDepositMinTerm(), list.get("depositMinTerm"));
        assertEquals(deposit.getDepositMinAmount(), list.get("depositMinAmount"));
        assertEquals(deposit.getDepositCurrency(), list.get("depositCurrency"));
        assertEquals(deposit.getDepositInterestRate(), list.get("depositInterestRate"));
        assertEquals(deposit.getDepositAddConditions(), list.get("depositAddConditions"));

        assertTrue("Error sum amount", sumAmountDeposit >=
                Integer.parseInt(list.get("depositorAmountSum").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("depositorAmountPlusSum").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
                Integer.parseInt(list.get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testGetBankDepositsByCurrencyWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String currency = "usd";

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

        String currency = "usd";

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
        Date startDate = dateFormat.parse("2015-12-02");
        Date endDate = dateFormat.parse("2015-12-07");

        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String currency = "usd";

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

        List<Map> list =
                depositDao.getBankDepositsByCurrencyFromToDateReturnDepositWithDepositors(currency,startDate,endDate);
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
    public void testAddBankDeposit() throws Exception {
        deposit = new BankDeposit();
            deposit.setDepositName("name");
            deposit.setDepositMinTerm(24);
            deposit.setDepositMinAmount(1000);
            deposit.setDepositCurrency("grb");
            deposit.setDepositInterestRate(4);
            deposit.setDepositAddConditions("condition");
        LOGGER.debug("new deposit - {}",deposit);

        sizeBefore = rowCount(BankDeposit.class);
        LOGGER.debug("size before add = {}",sizeBefore);

        LOGGER.debug("start added new deposit - {}",deposit);
        depositDao.addBankDeposit(deposit);
        LOGGER.debug("new deposit was added - {}",deposit);

        assertTrue(deposit.getDepositId()!= null);
        assertEquals(deposit.toString(),depositDao.getBankDepositByIdCriteria(deposit.getDepositId()).toString());

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("size after add = {}",sizeAfter);

        assertTrue(sizeAfter == sizeBefore+1);
    }

    @Test
    public void testUpdateBankDeposit(){
        String testDeposit;

        deposit = deposits.get(3);
        Long id = deposit.getDepositId();

        testDeposit = deposit.toString();

        deposit.setDepositName("UpdateDepositName");
        deposit.setDepositMinAmount(10);
        deposit.setDepositCurrency("grb");
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
    public void testRemoveBankDeposit2() throws Exception{
        sizeBefore = rowCount(BankDeposit.class);
        LOGGER.debug("sizeBefore = {}", sizeBefore);

        int sizeDepositorsBefore = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsBefore = {}", sizeDepositorsBefore);

        deposit = depositDao.getBankDepositByIdCriteria(5L);

        BankDepositor depositor = new BankDepositor();
            depositor.setDepositorName("newName");
            depositor.setDepositorDateDeposit(dateFormat.parse("2014-12-02"));
            depositor.setDepositorAmountDeposit(1000);
            depositor.setDepositorAmountPlusDeposit(10);
            depositor.setDepositorAmountMinusDeposit(10);
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-12-03"));
            depositor.setDepositorMarkReturnDeposit(0);

        Set depositors = new HashSet();
            depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        sizeDepositorsBefore = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsBefore = {}", sizeDepositorsBefore);

        depositDao.deleteBankDeposit(5L);

        assertNull(depositDao.getBankDepositByIdCriteria(5L));

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("sizeAfter = {}", sizeAfter);

        int sizeDepositorsAfter = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsAfter = {}", sizeDepositorsAfter);

        assertTrue(sizeAfter == sizeBefore - 1);
        assertTrue(sizeDepositorsAfter == sizeDepositorsBefore - 1);
    }

    public Integer rowCount(Class<?> name) throws ClassNotFoundException{

        sessionFactory.getCurrentSession().beginTransaction();
        //--- query
        result = sessionFactory.getCurrentSession().createCriteria(name)
                .setProjection(Projections.rowCount()).uniqueResult();

        sessionFactory.getCurrentSession().getTransaction().commit();
        return Integer.parseInt(result.toString());
    }
}