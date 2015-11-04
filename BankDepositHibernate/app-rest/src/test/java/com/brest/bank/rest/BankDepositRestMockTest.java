package com.brest.bank.rest;

/**
 * Created by alexander on 1.11.15.
 */
import junit.framework.*;
import org.junit.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.easymock.EasyMock;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

public class BankDepositRestMockTest extends TestCase {

    private static final Logger LOGGER = LogManager.getLogger();;

    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;

    private BankDepositRest bankDepositRest = new BankDepositRest();

    @Before
    public void setUp() throws Exception {
        requestMock = EasyMock.createMock(HttpServletRequest.class);
        responseMock = EasyMock.createMock(HttpServletResponse.class);
        bankDepositRest.setHttpServletRequest(requestMock);
        bankDepositRest.setHttpServletResponse(responseMock);
    }

    @After
    public void clean() {
        reset(requestMock,responseMock);
    }

    @Test
    public void testGet()throws ServletException, IOException {
        LOGGER.info("testGet() - run");

        responseMock.setContentType("application/json");
        expectLastCall();

        responseMock.getWriter();
        expectLastCall().andReturn(createMock(PrintWriter.class));

        expect(requestMock.getPathInfo()).andReturn("/rest/deposits");

        replay(requestMock,responseMock);

        bankDepositRest.doGet(requestMock,responseMock);

        verify(requestMock,responseMock);
    }

    @Test
    public void testPost() throws ServletException, IOException {
        LOGGER.debug("testPost() - run");

        responseMock.setContentType("application/json");
        expectLastCall();

        responseMock.getWriter();
        expectLastCall().andReturn(createMock(PrintWriter.class));

        expect(requestMock.getPathInfo()).andReturn("/rest/deposit");

        replay(requestMock,responseMock);

        bankDepositRest.doPost(requestMock, responseMock);

        verify(requestMock,responseMock);
    }

    @Test
    public void testPut() throws ServletException, IOException {
        LOGGER.debug("testPut() - run");

        responseMock.getWriter();
        expectLastCall().andReturn(createMock(PrintWriter.class));

        expect(requestMock.getPathInfo()).andReturn("/rest/deposit");

        replay(requestMock,responseMock);

        bankDepositRest.doPut(requestMock, responseMock);

        verify(requestMock,responseMock);
    }

    @Test
    public void testDelete() throws ServletException, IOException {
        LOGGER.debug("testDelete() - run");

        responseMock.getWriter();
        expectLastCall().andReturn(createMock(PrintWriter.class));

        expect(requestMock.getPathInfo()).andReturn("/rest/deposit/1");

        replay(requestMock,responseMock);

        bankDepositRest.doDelete(requestMock, responseMock);

        verify(requestMock,responseMock);
    }

}