package com.brest.bank.service;

import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.service.BankDepositService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.easymock.EasyMock;

import org.junit.After;
import org.junit.Before;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.easymock.EasyMock.*;

public class DepositServiceImplMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    public BankDepositDao depositDao;// = new BankDepositDaoImpl();
    private BankDepositService depositService = new BankDepositServiceImpl();

    @ClassRule
    public static ExternalResource resource = new ExternalResource() {
        @Override
        public void before() throws Throwable {
            LOGGER.debug("JUnit rule - before() - run method DataFixture.init() for DaoImplTest: ");
            DataFixtureInitDB.init();
        };
        @Override
        public void after(){
            LOGGER.debug("JUnit rule - after()");
        };
    };

    @Before
    public void setUp() throws Exception {
        depositDao = EasyMock.createMock(BankDepositDao.class);
    }

    @After
    public void clean() {
        reset(depositDao);
    }

    @Test
    public void testGetDeposits() {
        LOGGER.debug("testGetDeposits() - run");
        List<BankDeposit> deposits = DataFixture.getDeposits();

        expect(depositDao.getBankDepositsCriteria()).andReturn(deposits);
        replay(depositDao);

        List<BankDeposit> resultDeposits  = depositService.getBankDeposits();
        //verify(depositDao);

        assertEquals(deposits.toString(), resultDeposits.toString());
    }
}
