package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.criterion.Projections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-dao-test.xml"})
public class BankDepositDaoImplTest {

    @Autowired
    private BankDepositDao depositDao;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private BankDeposit deposit;
    private BankDepositor depositor;
    private List<BankDeposit> deposits;
    private Set<BankDepositor> depositors;

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

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(3L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, " +
                "depositMinAmount=300, depositCurrency=usd, depositInterestRate=6, " +
                "depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName2");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, " +
                "depositMinAmount=300, depositCurrency=usd, depositInterestRate=6, " +
                "depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByCurrencyCriteria(){
        deposits = depositDao.getBankDepositsByCurrencyCriteria("usd");
        LOGGER.debug("deposits: {}", deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue("usd".equalsIgnoreCase(aDeposit.getDepositCurrency()));
        }
    }

    @Test
    public void testGetBankDepositsByInterestRateCriteria(){
        deposits = depositDao.getBankDepositsByInterestRateCriteria(6);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue(6==aDeposit.getDepositInterestRate());
        }
    }

    @Test
    public void testGetBankDepositsFromToMinTermCriteria(){
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(13, 14);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue((13==aDeposit.getDepositMinTerm())||(14==aDeposit.getDepositMinTerm()));
        }
    }

    @Test(expected = AssertionError.class)
    public void testGetBankDepositsFromToMinTermCriteriaNullFirstArgs(){
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(null, 14);
    }

    @Test(expected = AssertionError.class)
    public void testGetBankDepositsFromToMinTermCriteriaNullSecondArgs(){
        deposits = depositDao.getBankDepositsFromToMinTermCriteria(13, null);
    }

    @Test
    public void testGetBankDepositsFromToInterestRateCriteria(){
        deposits = depositDao.getBankDepositsFromToInterestRateCriteria(5,6);
        LOGGER.debug("deposits: {}",deposits);

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);

        for(BankDeposit aDeposit: deposits){
            assertTrue((5==aDeposit.getDepositInterestRate())||(6==aDeposit.getDepositInterestRate()));
        }
    }

    @Test
    public void testGetBankDepositsBetweenDateDeposit() throws Exception {
        Date startDate = dateFormat.parse("2015-11-02");
        Date endDate = dateFormat.parse("2015-12-04");

        //--- Initialization test data
        //--- first
        deposit = depositDao.getBankDepositByIdCriteria(1L);

        depositor = DataFixture.getNewDepositor("newName");

        Set depositors = new HashSet();
            depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        //--- second
        deposit = depositDao.getBankDepositByIdCriteria(3L);

        depositor = DataFixture.getNewDepositor("newName");

        depositors = new HashSet();
            depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        //--- Testing
        deposits = depositDao.getBankDepositsFromToDateDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertEquals("[BankDeposit: { depositId=1, depositName=depositName0, depositMinTerm=12, depositMinAmount=100, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsBetweenDateReturnDeposit() throws Exception {
        Date startDate = dateFormat.parse("2015-12-01");
        Date endDate = dateFormat.parse("2015-12-26");

        //--- Initialization test data
        //--- first
        deposit = depositDao.getBankDepositByIdCriteria(2L);

        depositor = DataFixture.getNewDepositor("newName1");

        Set depositors = new HashSet();
            depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        //--- second
        deposit = depositDao.getBankDepositByIdCriteria(3L);

        depositor = DataFixture.getNewDepositor("newName2");
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-12-25"));

        depositors = new HashSet();
            depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        //--- Testing
        deposits = depositDao.getBankDepositsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertEquals("[BankDeposit: { depositId=2, depositName=depositName1, depositMinTerm=13, depositMinAmount=200," +
                " depositCurrency=eur, depositInterestRate=5, depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositByNameWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName0";

        //--- Initialization test data
        deposit = depositDao.getBankDepositByNameCriteria(name);

        BankDepositor depositor1 = DataFixture.getNewDepositor("newNam3");

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");

        Set deps = new HashSet();
            deps.add(depositor1);
            deps.add(depositor2);
        deposit.setDepositors(deps);
        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit = {}", deposit);

        depositors = deposit.getDepositors();
        LOGGER.debug("depositors = {}", depositors);

        //--- Testing
        List<Map> list = depositDao.getBankDepositByNameWithDepositors(name);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        for(BankDepositor aDepositors: depositors){
            sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
            sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);

        for (Map aList: list) {
            assertEquals(deposit.getDepositId(), aList.get("depositId"));
            assertEquals(deposit.getDepositName(), aList.get("depositName"));
            assertEquals(deposit.getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposit.getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposit.getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposit.getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposit.getDepositAddConditions(), aList.get("depositAddConditions"));
            assertTrue("Error sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByNameFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName0";
        Date start = dateFormat.parse("2015-10-02");
        Date end = dateFormat.parse("2015-11-01");

        //--- Initialization test data
        deposit = depositDao.getBankDepositByNameCriteria(name);

        BankDepositor depositor1 = DataFixture.getNewDepositor("newName3");
            depositor1.setDepositorDateDeposit(dateFormat.parse("2015-10-02"));

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");

        Set deps = new HashSet();
            deps.add(depositor1);
            deps.add(depositor2);
        deposit.setDepositors(deps);
        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit = {}", deposit);

        depositors = deposit.getDepositors();
        LOGGER.debug("depositors = {}", depositors.toString());

        //--- Testing
        List<Map> list = depositDao.getBankDepositByNameFromToDateDepositWithDepositors(name,start,end);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        for(BankDepositor aDepositors: depositors){
            Date testDate = aDepositors.getDepositorDateDeposit();
            if( (testDate.after(start) & testDate.before(end))
                    || (testDate.compareTo(start)==0)
                    || (testDate.compareTo(end)==0)){
                sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
                sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
                sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
            }
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);

        for (Map aList: list) {
            assertEquals(deposit.getDepositId(), aList.get("depositId"));
            assertEquals(deposit.getDepositName(), aList.get("depositName"));
            assertEquals(deposit.getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposit.getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposit.getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposit.getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposit.getDepositAddConditions(), aList.get("depositAddConditions"));
            assertTrue("Error sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByNameFromToDateReturnDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        String name = "depositName0";
        Date start = dateFormat.parse("2015-11-03");
        Date end = dateFormat.parse("2015-11-25");

        //--- Initialization test data
        deposit = depositDao.getBankDepositByNameCriteria(name);

        BankDepositor depositor1 = DataFixture.getNewDepositor("newName3");
            depositor1.setDepositorDateReturnDeposit(dateFormat.parse("2015-11-03"));

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");
            depositor2.setDepositorDateReturnDeposit(dateFormat.parse("2015-11-25"));

        Set deps = new HashSet();
            deps.add(depositor1);
            deps.add(depositor2);
        deposit.setDepositors(deps);
        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit = {}", deposit);

        depositors = deposit.getDepositors();
        LOGGER.debug("depositors = {}", depositors.toString());

        //--- Testing
        List<Map> list = depositDao.getBankDepositByNameFromToDateReturnDepositWithDepositors(name,start,end);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        for(BankDepositor aDepositors: depositors){
            Date testDate = aDepositors.getDepositorDateReturnDeposit();
            if( (testDate.after(start) & testDate.before(end))
                    || (testDate.compareTo(start)==0)
                    || (testDate.compareTo(end)==0)){
                sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
                sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
                sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
            }
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);

        for (Map aList: list) {
            assertEquals(deposit.getDepositId(), aList.get("depositId"));
            assertEquals(deposit.getDepositName(), aList.get("depositName"));
            assertEquals(deposit.getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposit.getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposit.getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposit.getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposit.getDepositAddConditions(), aList.get("depositAddConditions"));
            assertTrue("Error sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByIdWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;

        //--- Initialization test data
        deposit = depositDao.getBankDepositByIdCriteria(1L);

        BankDepositor depositor1 = DataFixture.getNewDepositor("newNam3");

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");

        Set deps = new HashSet();
            deps.add(depositor1);
            deps.add(depositor2);
        deposit.setDepositors(deps);
        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit = {}", deposit);

        depositors = deposit.getDepositors();
        LOGGER.debug("depositors = {}", depositors);

        //--- Testing
        List<Map> list = depositDao.getBankDepositByIdWithDepositors(1L);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        for(BankDepositor aDepositors: depositors){
            sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
            sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);

        for (Map aList: list) {
            assertEquals(deposit.getDepositId(), aList.get("depositId"));
            assertEquals(deposit.getDepositName(), aList.get("depositName"));
            assertEquals(deposit.getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposit.getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposit.getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposit.getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposit.getDepositAddConditions(), aList.get("depositAddConditions"));
            assertTrue("Error sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByIdFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        Date start = dateFormat.parse("2015-10-02");
        Date end = dateFormat.parse("2015-11-02");

        //--- Initialization test data
        deposit = depositDao.getBankDepositByIdCriteria(1L);

        BankDepositor depositor1 = DataFixture.getNewDepositor("newName3");
            depositor1.setDepositorDateDeposit(dateFormat.parse("2015-10-02"));

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");

        Set deps = new HashSet();
            deps.add(depositor1);
            deps.add(depositor2);
        deposit.setDepositors(deps);
        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit = {}", deposit);

        depositors = deposit.getDepositors();
        LOGGER.debug("depositors = {}", depositors.toString());

        //--- Testing
        List<Map> list = depositDao.getBankDepositByIdFromToDateDepositWithDepositors(1L, start, end);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        for(BankDepositor aDepositors: depositors){
            Date testDate = aDepositors.getDepositorDateDeposit();
            if( (testDate.after(start) & testDate.before(end))
                    || (testDate.compareTo(start)==0)
                    || (testDate.compareTo(end)==0)){
                sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
                sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
                sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
            }
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);

        for (Map aList: list) {
            assertEquals(deposit.getDepositId(), aList.get("depositId"));
            assertEquals(deposit.getDepositName(), aList.get("depositName"));
            assertEquals(deposit.getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposit.getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposit.getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposit.getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposit.getDepositAddConditions(), aList.get("depositAddConditions"));
            assertTrue("Error sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByIdFromToDateReturnDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;
        Date start = dateFormat.parse("2015-11-03");
        Date end = dateFormat.parse("2015-11-23");

        //--- Initialization test data
        deposit = depositDao.getBankDepositByIdCriteria(1L);

        BankDepositor depositor1 = DataFixture.getNewDepositor("newName3");
            depositor1.setDepositorDateReturnDeposit(dateFormat.parse("2015-11-03"));

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");
            depositor2.setDepositorDateReturnDeposit(dateFormat.parse("2015-11-25"));

        Set deps = new HashSet();
            deps.add(depositor1);
            deps.add(depositor2);
        deposit.setDepositors(deps);
        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit = {}", deposit);

        depositors = deposit.getDepositors();
        LOGGER.debug("depositors = {}", depositors.toString());

        //--- Testing
        List<Map> list = depositDao.getBankDepositByIdFromToDateReturnDepositWithDepositors(1L,start,end);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        for(BankDepositor aDepositors: depositors){
            Date testDate = aDepositors.getDepositorDateReturnDeposit();
            if( (testDate.after(start) & testDate.before(end))
                    || (testDate.compareTo(start)==0)
                    || (testDate.compareTo(end)==0)){
                sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
                sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
                sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
            }
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);

        for (Map aList: list) {
            assertEquals(deposit.getDepositId(), aList.get("depositId"));
            assertEquals(deposit.getDepositName(), aList.get("depositName"));
            assertEquals(deposit.getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposit.getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposit.getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposit.getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposit.getDepositAddConditions(), aList.get("depositAddConditions"));
            assertTrue("Error sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositsFromToDateDepositWithDepositors() throws Exception {
        Date startDate = dateFormat.parse("2015-11-02");
        Date endDate = dateFormat.parse("2015-12-05");

        //--- Initialization test data
        //--- first
        deposit = depositDao.getBankDepositByIdCriteria(1L);

        depositor = DataFixture.getNewDepositor("newName");

        Set depositors = new HashSet();
            depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        //--- second
        deposit = depositDao.getBankDepositByIdCriteria(3L);

        depositor = DataFixture.getNewDepositor("newName2");

        depositors = new HashSet();
            depositors.add(depositor);
        deposit.setDepositors(depositors);

        depositDao.updateBankDeposit(deposit);

        //--- testing
        List<Map> list = depositDao.getBankDepositsFromToDateDepositWithDepositors(startDate,endDate);
        LOGGER.debug("list = {}", list);

        deposits = depositDao.getBankDepositsFromToDateDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertTrue(list.size()==deposits.size());

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
    public void testGetBankDepositsFromToDateReturnDepositWithDepositors() throws Exception {
        Date startDate = dateFormat.parse("2015-11-02");
        Date endDate = dateFormat.parse("2015-12-03");

        //--- Initialization test data
        //--- first
        deposit = depositDao.getBankDepositByIdCriteria(1L);

        depositor = DataFixture.getNewDepositor("newName");

        Set depositors = new HashSet();
        depositors.add(depositor);
        deposit.setDepositors(depositors);
        depositDao.updateBankDeposit(deposit);

        //--- second
        deposit = depositDao.getBankDepositByIdCriteria(3L);

        depositor = DataFixture.getNewDepositor("newName2");
            depositor.setDepositorDateReturnDeposit(dateFormat.parse("2015-12-11"));

        depositors = new HashSet();
        depositors.add(depositor);
        deposit.setDepositors(depositors);

        depositDao.updateBankDeposit(deposit);

        //--- testing
        List<Map> list = depositDao.getBankDepositsFromToDateReturnDepositWithDepositors(startDate, endDate);
        LOGGER.debug("list = {}", list);

        deposits = depositDao.getBankDepositsFromToDateReturnDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertTrue(list.size()==deposits.size());

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
    public void testGetBankDepositsByCurrencyWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;

        //--- Initialization test data
        deposits = depositDao.getBankDepositsByCurrencyCriteria("eur");

        BankDepositor depositor1 = DataFixture.getNewDepositor("newNam3");

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");

        Set deps = new HashSet();
            deps.add(depositor1);
            deps.add(depositor2);
        deposits.get(0).setDepositors(deps);
        depositDao.updateBankDeposit(deposits.get(0));
        LOGGER.debug("deposits = {}", deposits);

        depositors = deposits.get(0).getDepositors();
        LOGGER.debug("depositors = {}", depositors);

        //--- Testing
        List<Map> list = depositDao.getBankDepositsByCurrencyWithDepositors("eur");
        LOGGER.debug("list = {}", list);

        assertTrue(list.size()==1);

        for(BankDepositor aDepositors: depositors){
            sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
            sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);

        for (Map aList: list) {
            assertEquals(deposits.get(0).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(0).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(0).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(0).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(0).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(0).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(0).getDepositAddConditions(), aList.get("depositAddConditions"));
            assertTrue("Error sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositsByCurrencyFromToDateDepositWithDepositors() throws ParseException{
        Integer sumAmountDeposit=0,
                sumAmountPlusDeposit=0,
                sumAmountMinusDeposit=0;

        //--- Initialization test data
        deposits = depositDao.getBankDepositsByCurrencyCriteria("usd");

        BankDepositor depositor1 = DataFixture.getNewDepositor("newNam3");

        BankDepositor depositor2 = DataFixture.getNewDepositor("newName4");

        Set deps = new HashSet();
        deps.add(depositor1);
        deposits.get(0).setDepositors(deps);
        depositDao.updateBankDeposit(deposits.get(0));

        deps = new HashSet();
        deps.add(depositor2);
        deposits.get(1).setDepositors(deps);
        depositDao.updateBankDeposit(deposits.get(1));
        LOGGER.debug("deposits = {}", deposits);

        depositors = deposits.get(0).getDepositors();
        depositors.addAll(deposits.get(1).getDepositors());
        LOGGER.debug("depositors = {}", depositors);

        //--- Testing
        List<Map> list = depositDao.getBankDepositsByCurrencyWithDepositors("usd");
        LOGGER.debug("list = {}", list);

        for(BankDepositor aDepositors: depositors){
            sumAmountDeposit += aDepositors.getDepositorAmountDeposit();
            sumAmountPlusDeposit += aDepositors.getDepositorAmountPlusDeposit();
            sumAmountMinusDeposit += aDepositors.getDepositorAmountMinusDeposit();
        }
        LOGGER.debug("sumAmountDeposit = {}", sumAmountDeposit);
        LOGGER.debug("sumAmountPlusDeposit = {}", sumAmountPlusDeposit);
        LOGGER.debug("sumAmountMinusDepositt = {}", sumAmountMinusDeposit);
        int i = 0;
        for (Map aList: list) {
            assertEquals(deposits.get(i).getDepositId(), aList.get("depositId"));
            assertEquals(deposits.get(i).getDepositName(), aList.get("depositName"));
            assertEquals(deposits.get(i).getDepositMinTerm(), aList.get("depositMinTerm"));
            assertEquals(deposits.get(i).getDepositMinAmount(), aList.get("depositMinAmount"));
            assertEquals(deposits.get(i).getDepositCurrency(), aList.get("depositCurrency"));
            assertEquals(deposits.get(i).getDepositInterestRate(), aList.get("depositInterestRate"));
            assertEquals(deposits.get(i).getDepositAddConditions(), aList.get("depositAddConditions"));
            i++;
        }
            assertTrue("Error sum all amount", sumAmountDeposit==(Integer.parseInt(list.get(0).get("depositorAmountSum").toString())+Integer.parseInt(list.get(1).get("depositorAmountSum").toString())));
            assertTrue("Error sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(list.get(0).get("depositorAmountPlusSum").toString())+Integer.parseInt(list.get(1).get("depositorAmountPlusSum").toString()));
            assertTrue("Error sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(list.get(0).get("depositorAmountMinusSum").toString())+Integer.parseInt(list.get(1).get("depositorAmountMinusSum").toString()));

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
        depositDao.deleteBankDeposit(5L);
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

        deposit = depositDao.getBankDepositByIdCriteria(2L);

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

        depositDao.deleteBankDeposit(2L);

        assertNull(depositDao.getBankDepositByIdCriteria(2L));

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("sizeAfter = {}", sizeAfter);

        int sizeDepositorsAfter = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsAfter = {}", sizeDepositorsAfter);

        assertTrue(sizeAfter == sizeBefore - 1);
        assertTrue(sizeDepositorsAfter == sizeDepositorsBefore - 1);
    }

    public Integer rowCount(Class<?> name) throws ClassNotFoundException{

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        //--- query
        result = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(name)
                .setProjection(Projections.rowCount()).uniqueResult();

        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        return Integer.parseInt(result.toString());
    }
}