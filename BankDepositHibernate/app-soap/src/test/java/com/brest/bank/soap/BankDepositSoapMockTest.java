package com.brest.bank.soap;

/**
 * Created by alexander on 1.11.15.
 */

import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static org.easymock.EasyMock.*;

public class BankDepositSoapMockTest extends TestCase {

    private static final Logger LOGGER = LogManager.getLogger();

    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;

    private BankDepositSoapServer bankDepositRest = new BankDepositSoapServer();

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
    public void testPost() throws ServletException, IOException {
        LOGGER.debug("testSoapPost() - run");

        expect(responseMock.getOutputStream()).andReturn(createMock(ServletOutputStream.class));

        expect(requestMock.getHeader("SOAPAction")).andReturn("getBankDeposits");

        expect(requestMock.getHeader("SOAPAction")).andReturn("addBankDeposit");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.addBankDepositSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("getBankDepositById");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.getBankDepositByIdSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("getBankDepositByName");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.getBankDepositByNameSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("updateBankDeposit");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.updateBankDepositSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("getBankDepositors");

        expect(requestMock.getHeader("SOAPAction")).andReturn("addBankDepositor");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.addBankDepositorSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("getBankDepositorById");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.getBankDepositorByIdSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("getBankDepositorByName");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.getBankDepositorByNameSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("updateBankDepositor");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.updateBankDepositorSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("removeBankDepositor");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.removeBankDepositorSoapRequest());

        expect(requestMock.getHeader("SOAPAction")).andReturn("deleteBankDeposit");
            expect(requestMock.getHeader("Content-Type")).andReturn("text/xml");
            expect(requestMock.getInputStream()).andReturn(DataFixture.deleteBankDepositSoapRequest());

        replay(requestMock,responseMock);

        bankDepositRest.doPost(requestMock, responseMock);

        verify(requestMock,responseMock);
    }

}