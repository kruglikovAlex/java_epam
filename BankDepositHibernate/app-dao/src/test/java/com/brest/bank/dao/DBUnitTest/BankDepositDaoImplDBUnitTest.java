package com.brest.bank.dao.DBUnitTest;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.*;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import org.junit.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BankDepositDaoImplDBUnitTest extends DBUnitConfig{

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    IDataSet expectedData, actualData;
    ITable expectedTable, actualTable;

    private BankDepositDao depositDao = new BankDepositDaoImpl();
    private BankDeposit deposit = new BankDeposit();
    private List<BankDeposit> deposits;

    @Before
    public void setUp() throws Exception{
        super.setUp();

        beforeData = getDataSet();
        databaseTester.setDataSet(beforeData);
        databaseTester.onSetup();
    }

    //--- конструктор по умолчанию
    public BankDepositDaoImplDBUnitTest(String name){
        super(name);
    }

    //--- для возврата тестовых значений реализуем метод по умолчанию (необходим по шаблону)
    //--- getDataSet() - возвращает значение типа IDataSet
    protected IDataSet getDataSet() throws Exception{
        return new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositInit-data.xml"));
    }

    //--- поведение после выполнения теста
    @After
    protected void tearDown() throws Exception{
        super.tearDown();
        databaseTester.onTearDown();
    }

    @Test
    public void testGetBankDepositsSQL() throws Exception{
        LOGGER.debug("testGetBankDepositsSQL() - run");
        deposits = depositDao.getBankDepositsSQL();

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositInit-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedData.getTable("bankdeposit").getRowCount(), deposits.size());

    }

    @Test
    public void testGetBankDepositsCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositsCriteria() - run");
        deposits = depositDao.getBankDepositsCriteria();

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositInit-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedData.getTable("bankdeposit").getRowCount(), deposits.size());
    }

    @Test
    public void testGetBankDepositByIdGet() throws Exception{
        LOGGER.debug("testGetBankDepositByIdGet() - run");
        deposit = depositDao.getBankDepositByIdGet(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0, "depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0, "depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }

    @Test
    public void testGetBankDepositByIdLoad() throws Exception{
        LOGGER.debug("testGetBankDepositByIdLoad() - run");
        deposit = depositDao.getBankDepositByIdLoad(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }

    @Test
    public void testGetBankDepositByIdCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositByIdCriteria() - run");
        deposit = depositDao.getBankDepositByIdCriteria(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }

    @Test
    public void testGetBankDepositByNameSQL() throws Exception{
        LOGGER.debug("testGetBankDepositByNameSQL() - run");
        deposit = depositDao.getBankDepositByNameSQL("depositName3");

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }

    @Test
    public void testGetBankDepositByNameCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositByNameSQL() - run");
        deposit = depositDao.getBankDepositByNameCriteria("depositName3");

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }

    @Test
    public void testGetBankDepositByNameByNaturalIdCriteria() throws Exception{
        LOGGER.debug("testGetBankDepositByNameSQL() - run");
        deposit = depositDao.getBankDepositByNameByNaturalIdCriteria("depositName3");

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositFind-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        Assert.assertEquals(expectedTable.getValue(0,"depositName").toString(),deposit.getDepositName());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinTerm").toString()) == deposit.getDepositMinTerm());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositMinAmount").toString()) == deposit.getDepositMinAmount());
        Assert.assertEquals(expectedTable.getValue(0,"depositCurrency").toString(), deposit.getDepositCurrency());
        Assert.assertTrue(Integer.parseInt(expectedTable.getValue(0,"depositInterestRate").toString()) == deposit.getDepositInterestRate());
        Assert.assertEquals(expectedTable.getValue(0,"depositAddConditions").toString(), deposit.getDepositAddConditions());
    }

    @Test
    public void testGetBankDepositBetweenInterestRateBetweenDateDepositWithDepositors() throws Exception{
        LOGGER.debug("testGetBankDepositBetweenInterestRateBetweenDateDepositWithDepositors() - run");
        Date startDate = dateFormat.parse("2014-12-01");
        Date endDate = dateFormat.parse("2014-12-06");
        List<Map> list = depositDao.getBankDepositBetweenInterestRateBetweenDateDepositWithDepositors(4,6,startDate,endDate);
        LOGGER.debug("list.size = {}", list.size());

        Assert.assertTrue(list.size()!=0);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositBetweenIntRateDepositDate-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        for (int i=0; i<list.size(); i++){
            Assert.assertEquals(expectedTable.getValue(i,"depositName").toString(),list.get(i).get("depositName"));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositMinTerm").toString()) == Integer.parseInt(list.get(i).get("depositMinTerm").toString()));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositMinAmount").toString()) == Integer.parseInt(list.get(i).get("depositMinAmount").toString()));
            Assert.assertEquals(expectedTable.getValue(i, "depositCurrency").toString(), list.get(i).get("depositCurrency"));
            Assert.assertTrue(Integer.parseInt(expectedTable.getValue(i, "depositInterestRate").toString()) == Integer.parseInt(list.get(i).get("depositInterestRate").toString()));
            Assert.assertEquals(expectedTable.getValue(i,"depositAddConditions").toString(), list.get(i).get("depositAddConditions"));
        }

        Assert.assertTrue(Integer.parseInt(list.get(0).get("depositorCount").toString())==2);
        Assert.assertTrue(Integer.parseInt(list.get(1).get("depositorCount").toString())==1);
        Assert.assertTrue(Integer.parseInt(list.get(2).get("depositorCount").toString())==1);

        expectedTable = expectedData.getTable("bankdepositor");
        Assert.assertTrue((Integer.parseInt(expectedTable.getValue(0,"depositorAmountDeposit").toString())+
                    Integer.parseInt(expectedTable.getValue(1,"depositorAmountDeposit").toString()))==Integer.parseInt(list.get(0).get("depositorAmountSum").toString()));
        Assert.assertTrue((Integer.parseInt(expectedTable.getValue(0,"depositorAmountPlusDeposit").toString())+
                    Integer.parseInt(expectedTable.getValue(1,"depositorAmountPlusDeposit").toString()))==Integer.parseInt(list.get(0).get("depositorAmountPlusSum").toString()));
        Assert.assertTrue((Integer.parseInt(expectedTable.getValue(0,"depositorAmountMinusDeposit").toString())+
                    Integer.parseInt(expectedTable.getValue(1,"depositorAmountMinusDeposit").toString()))==Integer.parseInt(list.get(0).get("depositorAmountMinusSum").toString()));
    }

    @Test
    public void testAddBankDeposit() throws Exception{
        LOGGER.debug("testAddBankDeposit() - run");
        deposit = new BankDeposit();
        deposit.setDepositName("name1");
        deposit.setDepositMinTerm(24);
        deposit.setDepositMinAmount(1000);
        deposit.setDepositCurrency("grb");
        deposit.setDepositInterestRate(4);
        deposit.setDepositAddConditions("condition");
        LOGGER.debug("new deposit - {}",deposit);

        depositDao.addBankDeposit(deposit);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositAdd-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
        Assert.assertEquals(expectedData.getTable("bankdeposit").getRowCount(), actualData.getTable("bankdeposit").getRowCount());
        Assert.assertEquals(expectedData.getTable("bankdepositor").getRowCount(), actualData.getTable("bankdepositor").getRowCount());
    }

    @Test
    public void testUpdateBankDepositDBUnit() throws Exception {
        LOGGER.debug("testUpdateBankDeposit() - run");
        deposit = depositDao.getBankDepositByIdCriteria(2L);
            deposit.setDepositMinTerm(24);
            deposit.setDepositMinAmount(1000);

        LOGGER.debug("deposit to update: {}",deposit);

        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit after update: {}",deposit);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositUpdate-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    @Test
    public void testRemoveBankDeposit() throws Exception {
        LOGGER.debug("testRemoveBankDeposit() - run");

        depositDao.removeBankDeposit(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositRemove-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
    }
}
