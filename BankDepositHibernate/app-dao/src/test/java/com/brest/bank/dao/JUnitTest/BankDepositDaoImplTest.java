package com.brest.bank.dao.JUnitTest;

import com.brest.bank.dao.*;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.junit.*;
import org.junit.rules.ExternalResource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class BankDepositDaoImplTest {

    public BankDepositDao depositDao = new BankDepositDaoImpl();
    public BankDepositorDao depositorDao = new BankDepositorDaoImpl();

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Session session;

    private BankDeposit deposit = new BankDeposit();
    private BankDepositor depositor = new BankDepositor();
    private List<BankDeposit> deposits;
    private List<BankDepositor> depositors;

    public Object result;
    public Integer sizeBefore = 0, sizeAfter = 0, sizeDepositorsBefore = 0, sizeDepositorsAfter = 0;

    @ClassRule
    public static ExternalResource resource = new ExternalResource() {
        @Override
        public void before() throws Throwable {
            LOGGER.debug("JUnit rule - before() - run method DataFixture.init() for DaoImplTest: ");
            DataFixture.init();
        };
        @Override
        public void after(){
            LOGGER.debug("JUnit rule - after()");
        };
    };

    @Rule
    public JUnitRule rule = new JUnitRule();

    @Test
    public void testGetBankDepositsSQL() throws Exception {
        deposits = depositDao.getBankDepositsSQL();
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositsCriteria() throws Exception {
        deposits = depositDao.getBankDepositsCriteria();
        LOGGER.debug("deposits.size()= {}", deposits.size());

        assertFalse(deposits.isEmpty());
        assertThat(deposits.size(), is(not(0)));
        assertNotNull(deposits);
    }

    @Test
    public void testGetBankDepositByIdGet() throws Exception {
        deposit = depositDao.getBankDepositByIdGet(3L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByIdLoad() throws Exception {
        deposit = depositDao.getBankDepositByIdLoad(3L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(3L);
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameSQL() throws Exception {
        deposit = depositDao.getBankDepositByNameSQL("depositName2");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameCriteria("depositName2");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositByCurrencyCriteria() throws Exception {
        deposits = depositDao.getBankDepositsByCurrencyCriteria("usd");
        LOGGER.debug("deposit = {}", deposits);

        assertNotNull(deposits);
        assertTrue(deposits.size()!=0);
        for(BankDeposit aDeposit: deposits){
            assertEquals("usd",aDeposit.getDepositCurrency());
        }
    }

    @Test
    public void testGetBankDepositByInterestRateCriteria() throws Exception {
        deposits = depositDao.getBankDepositsByInterestRateCriteria(4);
        LOGGER.debug("deposit = {}", deposits);

        assertNotNull(deposits);
        assertTrue(deposits.size()!=0);
        for(BankDeposit aDeposit: deposits){
            assertTrue(4==aDeposit.getDepositInterestRate());
        }
    }

    @Test
    public void testGetBankDepositByNameByNaturalIdCriteria() throws Exception {
        deposit = depositDao.getBankDepositByNameByNaturalIdCriteria("depositName2");
        LOGGER.debug("deposit = {}", deposit);

        assertEquals("BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}",deposit.toString());
    }

    @Test
    public void testGetBankDepositBetweenMinTermCriteria() throws Exception {
        deposits = depositDao.getBankDepositsBetweenMinTermCriteria(14, 15);
        LOGGER.debug("deposits = {}", deposits);

        assertEquals("[BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300," +
                " depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=15, depositMinAmount=400," +
                " depositCurrency=usd, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositBetweenInterestRateCriteria() throws Exception {
        deposits = depositDao.getBankDepositsBetweenInterestRateCriteria(4, 6);
        LOGGER.debug("deposits = {}", deposits);

        assertEquals("[BankDeposit: { depositId=1, depositName=depositName0, depositMinTerm=9, depositMinAmount=10000, " +
                "depositCurrency=usd, depositInterestRate=4, depositAddConditions=condition0}, " +
                "BankDeposit: { depositId=2, depositName=depositName1, depositMinTerm=13, depositMinAmount=200, " +
                "depositCurrency=usd, depositInterestRate=5, depositAddConditions=condition1}, " +
                "BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsBetweenDateDeposit() throws Exception {
        Date startDate = dateFormat.parse("2014-12-03");
        Date endDate = dateFormat.parse("2014-12-04");
        deposits = depositDao.getBankDepositsBetweenDateDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertEquals("[BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=15, depositMinAmount=400, " +
                "depositCurrency=usd, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositsBetweenDateReturnDeposit() throws Exception {
        Date startDate = dateFormat.parse("2014-12-04");
        Date endDate = dateFormat.parse("2014-12-05");
        deposits = depositDao.getBankDepositsBetweenDateReturnDeposit(startDate, endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertEquals("[BankDeposit: { depositId=3, depositName=depositName2, depositMinTerm=14, depositMinAmount=300, " +
                "depositCurrency=usd, depositInterestRate=6, depositAddConditions=condition2}, " +
                "BankDeposit: { depositId=4, depositName=depositName3, depositMinTerm=15, depositMinAmount=400, " +
                "depositCurrency=usd, depositInterestRate=7, depositAddConditions=condition3}]",deposits.toString());
    }

    @Test
    public void testGetBankDepositByCurrencyWithDepositors() throws Exception {
        List<Map> list = depositDao.getBankDepositByCurrencyWithDepositors("usd");
        LOGGER.debug("deposits = {}", list);

        deposits = depositDao.getBankDepositsByCurrencyCriteria("usd");
        LOGGER.debug("deposits = {}", deposits);

        assertTrue(list.size()==deposits.size());

        Integer[] sumAmountDeposit= new Integer[deposits.size()];
        Integer[] sumAmountPlusDeposit= new Integer[deposits.size()];
        Integer[] sumAmountMinusDeposit= new Integer[deposits.size()];

        for (int i=0; i<deposits.size(); i++){
            sumAmountDeposit[i] = 0;
            sumAmountPlusDeposit[i] = 0;
            sumAmountMinusDeposit[i] = 0;
            for(BankDepositor aDeposit:depositorDao.getBankDepositorByIdDepositCriteria(deposits.get(i).getDepositId())) {
                sumAmountDeposit[i] += aDeposit.getDepositorAmountDeposit();
                sumAmountPlusDeposit[i] += aDeposit.getDepositorAmountPlusDeposit();
                sumAmountMinusDeposit[i] += aDeposit.getDepositorAmountMinusDeposit();
            }
        }

        for (int j=0; j<list.size(); j++) {
            assertEquals(deposits.get(j).getDepositId(), list.get(j).get("depositId"));
            assertEquals(deposits.get(j).getDepositName(), list.get(j).get("depositName"));
            assertEquals(deposits.get(j).getDepositMinTerm(), list.get(j).get("depositMinTerm"));
            assertEquals(deposits.get(j).getDepositMinAmount(), list.get(j).get("depositMinAmount"));
            assertEquals(deposits.get(j).getDepositCurrency(), list.get(j).get("depositCurrency"));
            assertEquals(deposits.get(j).getDepositInterestRate(), list.get(j).get("depositInterestRate"));
            assertEquals(deposits.get(j).getDepositAddConditions(), list.get(j).get("depositAddConditions"));
            assertTrue("sum all amount", sumAmountDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountSum").toString()));
            assertTrue("sum all plus amount", sumAmountPlusDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountPlusSum").toString()));
            assertTrue("sum all minus amount", sumAmountMinusDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByInterestRateWithDepositors() throws Exception {
        List<Map> list = depositDao.getBankDepositByInterestRateWithDepositors(4);
        LOGGER.debug("deposits = {}", list);

        deposits = depositDao.getBankDepositsByInterestRateCriteria(4);
        LOGGER.debug("deposits = {}", deposits);

        assertTrue(list.size()==deposits.size());

        Integer[] sumAmountDeposit= new Integer[deposits.size()];
        Integer[] sumAmountPlusDeposit= new Integer[deposits.size()];
        Integer[] sumAmountMinusDeposit= new Integer[deposits.size()];

        for (int i=0; i<deposits.size(); i++){
            sumAmountDeposit[i] = 0;
            sumAmountPlusDeposit[i] = 0;
            sumAmountMinusDeposit[i] = 0;
            for(BankDepositor aDeposit:depositorDao.getBankDepositorByIdDepositCriteria(deposits.get(i).getDepositId())) {
                sumAmountDeposit[i] += aDeposit.getDepositorAmountDeposit();
                sumAmountPlusDeposit[i] += aDeposit.getDepositorAmountPlusDeposit();
                sumAmountMinusDeposit[i] += aDeposit.getDepositorAmountMinusDeposit();
            }
        }

        for (int j=0; j<list.size(); j++) {
            assertEquals(deposits.get(j).getDepositId(), list.get(j).get("depositId"));
            assertEquals(deposits.get(j).getDepositName(), list.get(j).get("depositName"));
            assertEquals(deposits.get(j).getDepositMinTerm(), list.get(j).get("depositMinTerm"));
            assertEquals(deposits.get(j).getDepositMinAmount(), list.get(j).get("depositMinAmount"));
            assertEquals(deposits.get(j).getDepositCurrency(), list.get(j).get("depositCurrency"));
            assertEquals(deposits.get(j).getDepositInterestRate(), list.get(j).get("depositInterestRate"));
            assertEquals(deposits.get(j).getDepositAddConditions(), list.get(j).get("depositAddConditions"));
            assertTrue("sum all amount", sumAmountDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountSum").toString()));
            assertTrue("sum all plus amount", sumAmountPlusDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountPlusSum").toString()));
            assertTrue("sum all minus amount", sumAmountMinusDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositBetweenInterestRateWithDepositors() throws Exception {
        List<Map> list = depositDao.getBankDepositBetweenInterestRateWithDepositors(4,6);
        LOGGER.debug("deposits = {}", list);

        deposits = depositDao.getBankDepositsBetweenInterestRateCriteria(4, 6);
        LOGGER.debug("deposits = {}", deposits);

        assertTrue(list.size()==deposits.size());

        Integer[] sumAmountDeposit= new Integer[deposits.size()];
        Integer[] sumAmountPlusDeposit= new Integer[deposits.size()];
        Integer[] sumAmountMinusDeposit= new Integer[deposits.size()];

        for (int i=0; i<deposits.size(); i++){
            sumAmountDeposit[i] = 0;
            sumAmountPlusDeposit[i] = 0;
            sumAmountMinusDeposit[i] = 0;
            for(BankDepositor aDeposit:depositorDao.getBankDepositorByIdDepositCriteria(deposits.get(i).getDepositId())) {
                sumAmountDeposit[i] += aDeposit.getDepositorAmountDeposit();
                sumAmountPlusDeposit[i] += aDeposit.getDepositorAmountPlusDeposit();
                sumAmountMinusDeposit[i] += aDeposit.getDepositorAmountMinusDeposit();
            }
        }

        for (int j=0; j<list.size(); j++) {
            assertEquals(deposits.get(j).getDepositId(), list.get(j).get("depositId"));
            assertEquals(deposits.get(j).getDepositName(), list.get(j).get("depositName"));
            assertEquals(deposits.get(j).getDepositMinTerm(), list.get(j).get("depositMinTerm"));
            assertEquals(deposits.get(j).getDepositMinAmount(), list.get(j).get("depositMinAmount"));
            assertEquals(deposits.get(j).getDepositCurrency(), list.get(j).get("depositCurrency"));
            assertEquals(deposits.get(j).getDepositInterestRate(), list.get(j).get("depositInterestRate"));
            assertEquals(deposits.get(j).getDepositAddConditions(), list.get(j).get("depositAddConditions"));
            assertTrue("sum all amount", sumAmountDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountSum").toString()));
            assertTrue("sum all plus amount", sumAmountPlusDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountPlusSum").toString()));
            assertTrue("sum all minus amount", sumAmountMinusDeposit[j]==Integer.parseInt(list.get(j).get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositBetweenInterestRateBetweenDateDepositWithDepositors() throws Exception {
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");
        List<Map> list = depositDao.getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors(4,6,startDate,endDate);
        LOGGER.debug("list.size = {}", list.size());

        deposits = depositDao.getBankDepositsBetweenInterestRateCriteria(4, 6);
        LOGGER.debug("deposits = {}", deposits);

        assertThat(list.size(), is(not(0)));
        assertTrue(list.size()<=deposits.size());

        deposits = depositDao.getBankDepositsBetweenDateDeposit(startDate,endDate);
        LOGGER.debug("deposits = {}", deposits);

        assertTrue(list.size()<=deposits.size());
    }

    @Test
    public void testGetBankDepositsBetweenDateDepositWithDepositors() throws Exception {
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");
        List<Map> list = depositDao.getBankDepositsBetweenDateDepositWithDepositors(startDate,endDate);
        LOGGER.debug("deposits = {}", list);
        deposits = depositDao.getBankDepositsBetweenDateDeposit(startDate, endDate);

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
    public void testGetBankDepositByNameBetweenDateDepositWithDepositors() throws Exception {
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");
        String name = "depositName0";
        Integer sumAmountDeposit=0, sumAmountPlusDeposit=0, sumAmountMinusDeposit=0;

        List<Map> list = depositDao.getBankDepositByNameBetweenDateDepositWithDepositors(name,startDate,endDate);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorBetweenDateDeposit(deposit.getDepositId(),startDate,endDate);
        LOGGER.debug("depositors = {}", depositors);
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
            assertTrue("sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositByNameBetweenDateReturnDepositWithDepositors() throws Exception {
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-07");
        String name = "depositName0";
        Integer sumAmountDeposit=0, sumAmountPlusDeposit=0, sumAmountMinusDeposit=0;

        List<Map> list = depositDao.getBankDepositByNameBetweenDateReturnDepositWithDepositors(name, startDate, endDate);
        LOGGER.debug("deposits = {}", list);

        assertTrue(list.size()==1);

        deposit = depositDao.getBankDepositByNameCriteria(name);
        LOGGER.debug("deposit = {}", deposit);

        depositors = depositorDao.getBankDepositorBetweenDateReturnDeposit(deposit.getDepositId(), startDate, endDate);
        LOGGER.debug("depositors = {}", depositors);
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
            assertTrue("sum all amount", sumAmountDeposit==Integer.parseInt(aList.get("depositorAmountSum").toString()));
            assertTrue("sum all plus amount", sumAmountPlusDeposit==Integer.parseInt(aList.get("depositorAmountPlusSum").toString()));
            assertTrue("sum all minus amount", sumAmountMinusDeposit==Integer.parseInt(aList.get("depositorAmountMinusSum").toString()));
        }
    }

    @Test
    public void testGetBankDepositsBetweenDateReturnDepositWithDepositors() throws Exception {
        Date startDate = dateFormat.parse("2014-12-03");
        Date endDate = dateFormat.parse("2014-12-05");
        List<Map> list = depositDao.getBankDepositsBetweenDateReturnDepositWithDepositors(startDate, endDate);
        LOGGER.debug("deposits = {}", list);
        deposits = depositDao.getBankDepositsBetweenDateReturnDeposit(startDate, endDate);

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
        assertEquals(deposit.toString(),depositDao.getBankDepositByIdGet(deposit.getDepositId()).toString());

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("size after add = {}",sizeAfter);

        assertTrue(sizeAfter == sizeBefore+1);
    }

    @Test
    public void testUpdateBankDeposit() throws Exception {
        deposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit before update - {}",deposit);
        deposit.setDepositMinTerm(9);
        deposit.setDepositMinAmount(10000);

        depositDao.updateBankDeposit(deposit);

        BankDeposit testDeposit = depositDao.getBankDepositByIdCriteria(1L);
        LOGGER.debug("deposit after update - {}",testDeposit);

        assertEquals(deposit.toString(),testDeposit.toString());
    }

    @Test
    public void testRemoveBankDeposit() throws Exception{
        sizeBefore = rowCount(BankDeposit.class);
        LOGGER.debug("sizeBefore = {}", sizeBefore);

        sizeDepositorsBefore = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsBefore = {}", sizeDepositorsBefore);

        depositDao.removeBankDeposit(2L);

        assertNull(depositDao.getBankDepositByIdCriteria(2L));

        sizeAfter = rowCount(BankDeposit.class);
        LOGGER.debug("sizeAfter = {}", sizeAfter);

        sizeDepositorsAfter = rowCount(BankDepositor.class);
        LOGGER.debug("sizeDepositorsAfter = {}", sizeDepositorsAfter);

        assertTrue(sizeAfter == sizeBefore - 1);
        assertTrue(sizeDepositorsAfter == sizeDepositorsBefore - 2);
    }

    public Integer rowCount(Class<?> name) throws ClassNotFoundException{
        //--- соединение
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        //--- query
        result = session.createCriteria(name)
                .setProjection(Projections.rowCount()).uniqueResult();
        //--- завершение сессии
        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

        return Integer.parseInt(result.toString());
    }

}