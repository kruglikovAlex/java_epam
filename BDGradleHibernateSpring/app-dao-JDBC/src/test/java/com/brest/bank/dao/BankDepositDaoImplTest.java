package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
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
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositDaoImplTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String ERROR_EMPTY_BD = "Data Base is empty";
    private static final String ERROR_SIZE = "Size can not be 0";
    private static final String ERROR_NULL = "The parameter can not be NULL";
    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0;

    @Autowired
    BankDepositDao depositDao;
    @Autowired
    BankDepositorDao depositorDao;
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

    @After
    public void endUp() throws Exception{
        //hibernateTemplate.getSessionFactory().getCurrentSession().getTransaction().commit();
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
        assertEquals("BankDeposit: { depositId=1, depositName=depositName1, " +
                "depositMinTerm=13, depositMinAmount=200, depositCurrency=eur, " +
                "depositInterestRate=5, depositAddConditions=condition1}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName0");
        LOGGER.debug("deposit = {}", deposit);

        assertNotNull(ERROR_NULL,deposit);
        assertEquals("BankDeposit: { depositId=0, depositName=depositName0, " +
                "depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, " +
                "depositInterestRate=4, depositAddConditions=condition0}",deposit.toString());
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

        assertEquals("[BankDeposit: { depositId=0, depositName=depositName0, " +
                "depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, " +
                "depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, " +
                "depositMinAmount=200, depositCurrency=eur, depositInterestRate=5, " +
                "depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, " +
                "depositMinAmount=300, depositCurrency=usd, depositInterestRate=6, " +
                "depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=15, " +
                "depositMinAmount=400, depositCurrency=usd, depositInterestRate=7, " +
                "depositAddConditions=condition3}, " +
                "BankDeposit: { depositId=4, depositName=depositName4, depositMinTerm=16, " +
                "depositMinAmount=500, depositCurrency=grb, depositInterestRate=8, " +
                "depositAddConditions=condition4}]",deposits.toString());
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

        assertEquals("[BankDeposit: { depositId=0, depositName=depositName0, " +
                "depositMinTerm=12, depositMinAmount=100, depositCurrency=usd, " +
                "depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=1, depositName=depositName1, depositMinTerm=13, " +
                "depositMinAmount=200, depositCurrency=eur, depositInterestRate=5, " +
                "depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=2, depositName=depositName2, depositMinTerm=14, " +
                "depositMinAmount=300, depositCurrency=usd, depositInterestRate=6, " +
                "depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=3, depositName=depositName3, depositMinTerm=15, " +
                "depositMinAmount=400, depositCurrency=usd, depositInterestRate=7, " +
                "depositAddConditions=condition3}, " +
                "BankDeposit: { depositId=4, depositName=depositName4, depositMinTerm=16, " +
                "depositMinAmount=500, depositCurrency=grb, depositInterestRate=8, " +
                "depositAddConditions=condition4}]",deposits.toString());
    }
/*
    //@Test
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
*/
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
            sumAmountDeposit[1] +=       Integer.parseInt(list.get(i).get("sumAmount").toString());
            sumAmountPlusDeposit[1] +=   Integer.parseInt(list.get(i).get("sumPlusAmount").toString());
            sumAmountMinusDeposit[1] +=  Integer.parseInt(list.get(i).get("sumMinusAmount").toString());
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
            sumAmountDeposit[1] +=       Integer.parseInt(list.get(i).get("sumAmount").toString());
            sumAmountPlusDeposit[1] +=   Integer.parseInt(list.get(i).get("sumPlusAmount").toString());
            sumAmountMinusDeposit[1] +=  Integer.parseInt(list.get(i).get("sumMinusAmount").toString());
        }

        assertTrue("Error sum amount", sumAmountDeposit[0] == sumAmountDeposit[1]);
        assertTrue("Error sum plus amount", sumAmountPlusDeposit[0] == sumAmountPlusDeposit[1]);
        assertTrue("Error sum minus amount", sumAmountMinusDeposit[0] == sumAmountMinusDeposit[1]);
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws ParseException{
        int     sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;
        String name = "depositName1";

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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("sumMinusAmount").toString()));
    }

    @Test
    public void testGetBankDepositByNameFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName1";
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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
                Integer.parseInt(list.get("SumMinusAmount").toString()));
    }

    @Test
    public void testGetBankDepositByNameFromToDateReturnDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName1";
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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
                Integer.parseInt(list.get("sumMinusAmount").toString()));
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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("sumMinusAmount").toString()));
    }

    @Test
    public void testGetBankDepositByDepositorIdWithDepositors() throws ParseException{
        int sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

        depositor = depositorDao.getBankDepositorByIdCriteria(1L);
        LOGGER.debug("depositor={}",depositor);

        sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
        sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
        sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();

        Map list = depositDao.getBankDepositByDepositorIdWithDepositors(depositor.getDepositorId());
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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("sumMinusAmount").toString()));
    }

    @Test
    public void testGetBankDepositByDepositorNameWithDepositors() throws ParseException{
        int sumAmountDeposit = 0,
                sumAmountPlusDeposit = 0,
                sumAmountMinusDeposit = 0;

        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit = {}", deposit);

        depositor = depositorDao.getBankDepositorByNameCriteria("depositorName1");
        LOGGER.debug("depositor={}",depositor);

        sumAmountDeposit +=      depositor.getDepositorAmountDeposit();
        sumAmountPlusDeposit +=  depositor.getDepositorAmountPlusDeposit();
        sumAmountMinusDeposit += depositor.getDepositorAmountMinusDeposit();

        Map list = depositDao.getBankDepositByDepositorNameWithDepositors(depositor.getDepositorName());
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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit ==
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit ==
                Integer.parseInt(list.get("sumMinusAmount").toString()));
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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
                Integer.parseInt(list.get("sumMinusAmount").toString()));
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
                Integer.parseInt(list.get("sumAmount").toString()));
        assertTrue("Error sum plus amount", sumAmountPlusDeposit >=
                Integer.parseInt(list.get("sumPlusAmount").toString()));
        assertTrue("Error sum minus amount", sumAmountMinusDeposit >=
                Integer.parseInt(list.get("sumMinusAmount").toString()));
    }
/*
    @Test
    public void testGetBankDepositsByTermWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Integer term = 12;

        deposits = depositDao.getBankDepositsFromToMinTermCriteria(12,12);
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

        Integer amount = 100;

        deposit = depositDao.getBankDepositByIdCriteria(1L);
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
    public void testGetBankDepositsByDepositorAmountWithDepositors() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        Integer amount = 997;

        deposits = new ArrayList<BankDeposit>();
        deposits.add(depositDao.getBankDepositByIdCriteria(1L));
        deposits.add(depositDao.getBankDepositByIdCriteria(2L));
        //deposits.add(depositDao.getBankDepositByIdCriteria(3L));
        LOGGER.debug("deposits = {}", deposit);

        depositors = depositorDao.getBankDepositorByIdDepositCriteria(1L);
        depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(2L));
        //depositors.addAll(depositorDao.getBankDepositorByIdDepositCriteria(3L));

        LOGGER.debug("depositors={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByDepositorAmountWithDepositors(amount,amount+6);
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
    public void testGetBankDepositsByVarArgsCurrencyInterestRate(){
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "usd";
        String nameAliasField2 = "deposit.depositInterestRate";
        Integer interestRate = 4;

        deposits = new ArrayList<BankDeposit>();

        List<BankDeposit> depositsIR = depositDao.getBankDepositsFromToInterestRateCriteria(4,6);
        LOGGER.debug("depositsIR = {}", depositsIR);

        for(BankDeposit dIR:depositsIR){
            for(BankDeposit d:depositDao.getBankDepositsByCurrencyCriteria(currency)){
                if(dIR.toString().equals(d.toString())){
                    deposits.add(dIR);
                }
            }
        }
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
                nameAliasField1,currency,
                nameAliasField2,interestRate,
                nameAliasField2,interestRate+2);
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
    public void testGetBankDepositsByVarArgsCurrencyDateDeposit() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "usd";
        String nameAliasField2 = "depositor.depositorDateDeposit";
        Date dateDeposit = dateFormat.parse("2015-12-01");

        List<BankDeposit> depositsCur = depositDao.getBankDepositsByCurrencyCriteria(currency);
        LOGGER.debug("depositsCur = {}", depositsCur);

        depositors = depositorDao.getBankDepositorsFromToDateDeposit(dateDeposit,dateDeposit);
        LOGGER.debug("depositors={}",depositors);

        deposits = new ArrayList<BankDeposit>();
        for(BankDeposit dCur:depositsCur){
            for(BankDepositor d:depositorDao.getBankDepositorByIdDepositCriteria(dCur.getDepositId())){
                if(d.toString().equals(depositors.get(0).toString())){
                    deposits.add(dCur);
                }
            }
        }
        LOGGER.debug("deposits={}",deposits);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(nameAliasField1,currency,nameAliasField2,dateDeposit);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);
        assertTrue(ERROR_SIZE, deposits.size()==list.size());

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
    public void testGetBankDepositsByVarArgsCurrencyInterestRateDateDeposit() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "usd";
        String nameAliasField2 = "deposit.depositInterestRate";
        Integer interestRate = 4;
        String nameAliasField3 = "depositor.depositorDateDeposit";
        Date dateDepositFrom = dateFormat.parse("2015-12-04");
        Date dateDepositTo = dateFormat.parse("2015-12-09");

        deposits = new ArrayList<BankDeposit>();

        List<BankDeposit> depositsIR = depositDao.getBankDepositsFromToInterestRateCriteria(interestRate,interestRate+2);
        LOGGER.debug("depositsIR = {}", depositsIR);

        List<BankDeposit> depositsIRCur = new ArrayList<BankDeposit>();
        for(BankDeposit dIR:depositsIR){
            for(BankDeposit d:depositDao.getBankDepositsByCurrencyCriteria(currency)){
                if(dIR.toString().equals(d.toString())){
                    depositsIRCur.add(dIR);
                }
            }
        }
        LOGGER.debug("deposits - IR = {}", depositsIRCur);

        depositors = new ArrayList<BankDepositor>();

        for(BankDeposit dIrCur:depositsIRCur){
            for(BankDepositor d:depositorDao.getBankDepositorByIdDepositCriteria(dIrCur.getDepositId())){
                if(d.getDepositorDateDeposit().equals(dateDepositFrom)
                        ||d.getDepositorDateDeposit().equals(dateDepositTo)
                        ||(d.getDepositorDateDeposit().after(dateDepositFrom)
                        &d.getDepositorDateDeposit().before(dateDepositTo))
                        ){
                    depositors.add(d);
                    if(!deposits.contains(dIrCur)){
                        deposits.add(dIrCur);
                    }
                }
            }
        }

        LOGGER.debug("deposits End={}",deposits);
        LOGGER.debug("depositors End={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(
                nameAliasField1,currency,
                nameAliasField2,interestRate,
                nameAliasField2,interestRate+2,
                nameAliasField3,dateDepositFrom,
                nameAliasField3,dateDepositTo
        );
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);
        assertTrue(ERROR_SIZE, deposits.size()==list.size());

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
    public void testGetBankDepositsByVarArgsCurrencyInterestRateDateDepositArrayParam() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "usd";
        String nameAliasField2 = "deposit.depositInterestRate";
        Integer interestRate = 4;
        String nameAliasField3 = "depositor.depositorDateDeposit";
        Date dateDepositFrom = dateFormat.parse("2015-12-04");
        Date dateDepositTo = dateFormat.parse("2015-12-09");

        deposits = new ArrayList<BankDeposit>();

        List<BankDeposit> depositsIR = depositDao.getBankDepositsFromToInterestRateCriteria(interestRate,interestRate+2);
        LOGGER.debug("depositsIR = {}", depositsIR);

        List<BankDeposit> depositsIRCur = new ArrayList<BankDeposit>();
        for(BankDeposit dIR:depositsIR){
            for(BankDeposit d:depositDao.getBankDepositsByCurrencyCriteria(currency)){
                if(dIR.toString().equals(d.toString())){
                    depositsIRCur.add(dIR);
                }
            }
        }
        LOGGER.debug("deposits - IR = {}", depositsIRCur);

        depositors = new ArrayList<BankDepositor>();

        for(BankDeposit dIrCur:depositsIRCur){
            for(BankDepositor d:depositorDao.getBankDepositorByIdDepositCriteria(dIrCur.getDepositId())){
                if(d.getDepositorDateDeposit().equals(dateDepositFrom)
                        ||d.getDepositorDateDeposit().equals(dateDepositTo)
                        ||(d.getDepositorDateDeposit().after(dateDepositFrom)
                        &d.getDepositorDateDeposit().before(dateDepositTo))
                        ){
                    depositors.add(d);
                    if(!deposits.contains(dIrCur)){
                        deposits.add(dIrCur);
                    }
                }
            }
        }

        LOGGER.debug("deposits End={}",deposits);
        LOGGER.debug("depositors End={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        Object[] args = {nameAliasField1,currency,
                nameAliasField2,"4",
                nameAliasField2,"6",
                nameAliasField3,"2015-12-04",
                nameAliasField3,"2015-12-09"};

        List<Map> list = depositDao.getBankDepositsByVarArgs(args);
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);
        assertTrue(ERROR_SIZE, deposits.size()==list.size());

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
    public void testGetBankDepositsByVarArgsCurrencyInterestRateDateReturnDeposit() throws ParseException{
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "usd";
        String nameAliasField2 = "deposit.depositInterestRate";
        Integer interestRate = 5;
        String nameAliasField3 = "depositor.depositorDateReturnDeposit";
        Date dateReturnDepositFrom = dateFormat.parse("2015-12-04");
        Date dateReturnDepositTo = dateFormat.parse("2015-12-09");

        deposits = new ArrayList<BankDeposit>();

        List<BankDeposit> depositsIR = depositDao.getBankDepositsFromToInterestRateCriteria(interestRate,interestRate+2);
        LOGGER.debug("depositsIR = {}", depositsIR);

        List<BankDeposit> depositsIRCur = new ArrayList<BankDeposit>();
        for(BankDeposit dIR:depositsIR){
            for(BankDeposit d:depositDao.getBankDepositsByCurrencyCriteria(currency)){
                if(dIR.toString().equals(d.toString())){
                    depositsIRCur.add(dIR);
                }
            }
        }
        LOGGER.debug("deposits - IR = {}", depositsIRCur);

        depositors = new ArrayList<BankDepositor>();

        for(BankDeposit dIrCur:depositsIRCur){
            for(BankDepositor d:depositorDao.getBankDepositorByIdDepositCriteria(dIrCur.getDepositId())){
                if(d.getDepositorDateReturnDeposit().equals(dateReturnDepositFrom)
                        ||d.getDepositorDateReturnDeposit().equals(dateReturnDepositTo)
                        ||(d.getDepositorDateReturnDeposit().after(dateReturnDepositFrom)
                        &d.getDepositorDateReturnDeposit().before(dateReturnDepositTo))
                        ){
                    depositors.add(d);
                    if(!deposits.contains(dIrCur)){
                        deposits.add(dIrCur);
                    }
                }
            }
        }

        LOGGER.debug("deposits End={}",deposits);
        LOGGER.debug("depositors End={}",depositors);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(
                nameAliasField1,currency,
                nameAliasField2,interestRate,
                nameAliasField2,interestRate+2,
                nameAliasField3,dateReturnDepositFrom,
                nameAliasField3,dateReturnDepositTo
        );
        LOGGER.debug("list = {}", list);

        assertNotNull(ERROR_NULL,list);
        assertTrue(ERROR_SIZE, deposits.size()==list.size());

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
    public void testGetBankDepositsByVarArgsCurrencyDepositorName(){
        int[]   sumAmountDeposit =      new int[]{0, 0},
                sumAmountPlusDeposit =  new int[]{0, 0},
                sumAmountMinusDeposit = new int[]{0, 0};

        String nameAliasField1 = "deposit.depositCurrency";
        String currency = "usd";
        String nameAliasField2 = "depositor.depositorName";
        String depsitorName = "depositorName2";

        deposits = new ArrayList<BankDeposit>();
        depositors = new ArrayList<BankDepositor>();

        depositors.add(depositorDao.getBankDepositorByNameCriteria(depsitorName));
        LOGGER.debug("depositors={}",depositors);

        List<BankDeposit> depositsCur = depositDao.getBankDepositsByCurrencyCriteria(currency);
        LOGGER.debug("depositsCur = {}", depositsCur);

        for(BankDeposit dCur:depositsCur){
            for(BankDepositor d:depositorDao.getBankDepositorByIdDepositCriteria(dCur.getDepositId())){
                if(d.toString().equals(depositors.get(0).toString())){
                    deposits.add(dCur);
                }
            }
        }
        LOGGER.debug("deposits = {}", deposits);

        for(BankDepositor depositor: depositors){
            sumAmountDeposit[0] +=      depositor.getDepositorAmountDeposit();
            sumAmountPlusDeposit[0] +=  depositor.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit[0] += depositor.getDepositorAmountMinusDeposit();
        }

        List<Map> list = depositDao.getBankDepositsByVarArgs(nameAliasField1,currency,nameAliasField2,depsitorName);
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
*/
}