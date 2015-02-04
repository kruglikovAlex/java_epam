package com.brest.bank.dao;

import com.brest.bank.domain.BankDeposit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dbunit.*;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;

import org.junit.*;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Properties;

public class BankDepositDaoImplDBUnitTest extends DBUnitConfig{

    private static final Logger LOGGER = LogManager.getLogger();
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
        deposit = depositDao.getBankDepositByNameSQL("depositName4");

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
        deposit = depositDao.getBankDepositByNameCriteria("depositName4");

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
    public void testAddBankDeposit() throws Exception{
        LOGGER.debug("testAddBankDeposit() - run");
        deposit = new BankDeposit();
            deposit.setDepositName("name");
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

    public void testUpdateBankDeposit() throws Exception {
        LOGGER.debug("testUpdateBankDeposit() - run");
        deposit = new BankDeposit();
            deposit.setDepositId(4L);
            deposit.setDepositName("UpdateName");
            deposit.setDepositMinTerm(12);
            deposit.setDepositMinAmount(100);
            deposit.setDepositCurrency("usd");
            deposit.setDepositInterestRate(4);
            deposit.setDepositAddConditions("condition4");
        LOGGER.debug("deposit to update: {}",deposit);

        depositDao.updateBankDeposit(deposit);
        LOGGER.debug("deposit after update: {}",deposit);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositUpdate-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    public void testRemoveBankDeposit() throws Exception {
        LOGGER.debug("testRemoveBankDeposit() - run");

        depositDao.removeBankDeposit(4L);

        expectedData = new FlatXmlDataSetBuilder().build(getClass().getResourceAsStream("/com/brest/bank/dao/depositRemove-data.xml"));
        expectedTable = expectedData.getTable("bankdeposit");

        actualData = databaseTester.getConnection().createDataSet();
        actualTable = actualData.getTable("bankdeposit");

        Assertion.assertEquals(expectedTable, actualTable);

        expectedTable = expectedData.getTable("bankdepositor");
        actualTable = actualData.getTable("bankdepositor");

        Assertion.assertEquals(expectedTable, actualTable);
    }
}
