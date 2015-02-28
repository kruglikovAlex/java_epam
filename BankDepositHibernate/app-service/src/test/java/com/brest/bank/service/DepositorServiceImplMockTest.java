package com.brest.bank.service;

import com.brest.bank.dao.BankDepositorDaoImpl;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.dao.BankDepositorDao;
import com.brest.bank.service.BankDepositorService;

import com.brest.bank.util.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.easymock.EasyMock;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import static org.easymock.EasyMock.*;

public class DepositorServiceImplMockTest {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    private BankDepositorDao depositorDao;
    private BankDepositorServiceImpl depositorService = new BankDepositorServiceImpl();

    @Before
    public void setUp() throws Exception {
        depositorDao = EasyMock.createMock(BankDepositorDaoImpl.class);
        depositorService.setBankDepositorDao(depositorDao);
    }

    @After
    public void clean() {
        reset(depositorDao);
    }

    @Test
    public void testGetDepositors() throws ParseException{
        LOGGER.debug("testGetDepositors() - run");
        List<BankDepositor> depositors = DataFixture.getDepositors();
        LOGGER.info("depositors: {}", depositors);

        expect(depositorDao.getBankDepositorsCriteria()).andReturn(depositors);
        replay(depositorDao);

        List<BankDepositor> resultDepositors  = depositorService.getBankDepositors();
        LOGGER.info("resultDepositors: {}", resultDepositors);
        verify(depositorDao);

        assertEquals(depositors, resultDepositors);
        assertSame(depositors, resultDepositors);
    }
}
