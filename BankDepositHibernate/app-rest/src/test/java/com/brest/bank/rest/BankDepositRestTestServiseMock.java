package com.brest.bank.rest;

import org.apache.cxf.message.Message;
import org.apache.cxf.transport.MessageObserver;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.transport.http.DestinationRegistry;
import org.apache.cxf.transport.servlet.ServletController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class BankDepositRestTestServiseMock extends Assert {

    private static final Logger LOGGER = LogManager.getLogger();

    private HttpServletRequest req;
    private HttpServletResponse res;
    private DestinationRegistry registry;
    private BankDepositRest serviceListGenerator;

    @Before
    public void setUp() {
        req = EasyMock.createMock(HttpServletRequest.class);
        res = EasyMock.createMock(HttpServletResponse.class);
        registry = EasyMock.createMock(DestinationRegistry.class);
        serviceListGenerator = EasyMock.createMock(BankDepositRest.class);
        serviceListGenerator.setHttpServletRequest(req);
        serviceListGenerator.setHttpServletResponse(res);
    }

    private void setReq(String pathInfo, String requestUri, String styleSheet, String formatted) {
        LOGGER.debug("setReq({},{},{},{})",pathInfo,requestUri,styleSheet,formatted);
        req.getPathInfo();
        EasyMock.expectLastCall().andReturn(pathInfo).anyTimes();
        req.getContextPath();
        EasyMock.expectLastCall().andReturn("");
        req.getServletPath();
        EasyMock.expectLastCall().andReturn("");
        req.setAttribute(Message.BASE_PATH, "http://localhost:8080");
        EasyMock.expectLastCall().anyTimes();
        req.getRequestURI();
        EasyMock.expectLastCall().andReturn(requestUri).times(2);
        req.getParameter("stylesheet");
        EasyMock.expectLastCall().andReturn(styleSheet);
        req.getParameter("formatted");
        EasyMock.expectLastCall().andReturn(formatted);
        req.getRequestURL();
        EasyMock.expectLastCall().andReturn(new StringBuffer("http://localhost:8080" + requestUri));
        registry.getDestinationsPaths();
        EasyMock.expectLastCall().andReturn(Collections.emptySet()).atLeastOnce();
        registry.getDestinationForPath("", true);
        EasyMock.expectLastCall().andReturn(null).anyTimes();
    }

    private void expectServiceListGeneratorCalled() throws ServletException, IOException {
        serviceListGenerator.service(req, res);
        EasyMock.expectLastCall();
        serviceListGenerator.doGet(req,res);
        EasyMock.expectLastCall();
        serviceListGenerator.doPost(req,res);
        EasyMock.expectLastCall();
        serviceListGenerator.doPut(req,res);
        EasyMock.expectLastCall();
        serviceListGenerator.doDelete(req,res);
        EasyMock.expectLastCall();
    }

    private void expectServiceListGeneratorNotCalled() throws ServletException, IOException {
    }

    @Test
    public void testGenerateServiceListing() throws Exception {
        setReq(null, "/BankDeposit/rest/deposits", null, "true");
        expectServiceListGeneratorCalled();
        EasyMock.replay(req, registry, serviceListGenerator);
        TestServletController sc = new TestServletController(registry, serviceListGenerator);
        sc.invoke(req, res);
        assertFalse(sc.invokeDestinationCalled());
    }

    @Test
    public void testGenerateUnformattedServiceListing() throws Exception {
        req.getPathInfo();
        EasyMock.expectLastCall().andReturn(null).anyTimes();
        req.getContextPath();
        EasyMock.expectLastCall().andReturn("");
        req.getServletPath();
        EasyMock.expectLastCall().andReturn("");
        req.getRequestURI();
        EasyMock.expectLastCall().andReturn("/BankDeposit/rest/deposits").times(2);

        req.getParameter("stylesheet");
        EasyMock.expectLastCall().andReturn(null);
        req.getParameter("formatted");
        EasyMock.expectLastCall().andReturn("false");
        req.getRequestURL();
        EasyMock.expectLastCall().andReturn(new StringBuffer("http://localhost:8080/BankDeposit/rest/deposits"));
        req.setAttribute(Message.BASE_PATH, "http://localhost:8080");
        EasyMock.expectLastCall().anyTimes();
        registry.getDestinationsPaths();
        EasyMock.expectLastCall().andReturn(Collections.emptySet()).atLeastOnce();
        registry.getDestinationForPath("", true);
        EasyMock.expectLastCall().andReturn(null).anyTimes();

        expectServiceListGeneratorCalled();
        EasyMock.replay(req, registry, serviceListGenerator);

        TestServletController sc = new TestServletController(registry, serviceListGenerator);
        sc.invoke(req, res);
        assertFalse(sc.invokeDestinationCalled());
    }

    @Test
    public void testHideServiceListing() throws Exception {
        req.getPathInfo();
        EasyMock.expectLastCall().andReturn(null);

        registry.getDestinationForPath("", true);
        EasyMock.expectLastCall().andReturn(null).atLeastOnce();
        AbstractHTTPDestination dest = EasyMock.createMock(AbstractHTTPDestination.class);
        registry.checkRestfulRequest("");
        EasyMock.expectLastCall().andReturn(dest).atLeastOnce();
        dest.getBus();
        EasyMock.expectLastCall().andReturn(null).anyTimes();
        dest.getMessageObserver();
        EasyMock.expectLastCall().andReturn(EasyMock.createMock(MessageObserver.class)).atLeastOnce();

        expectServiceListGeneratorNotCalled();

        EasyMock.replay(req, registry, serviceListGenerator, dest);
        TestServletController sc = new TestServletController(registry, serviceListGenerator);
        sc.setHideServiceList(true);
        sc.invoke(req, res);
        assertTrue(sc.invokeDestinationCalled());
    }

    @Test
    public void testDifferentServiceListPath() throws Exception {
        setReq(null, "/BankDeposit/rest/depositors", null, "true");
        expectServiceListGeneratorCalled();
        EasyMock.replay(req, registry, serviceListGenerator);
        TestServletController sc = new TestServletController(registry, serviceListGenerator);
        sc.setServiceListRelativePath("/BankDeposit/rest/depositors");
        sc.invoke(req, res);
        assertFalse(sc.invokeDestinationCalled());
    }

    public static class TestServletController extends ServletController {
        private boolean invokeDestinationCalled;

        public TestServletController(DestinationRegistry destinationRegistry,
                                     HttpServlet serviceListGenerator) {
            super(destinationRegistry, null, serviceListGenerator);
        }

        public void setHideServiceList(boolean b) {
            this.isHideServiceList = b;
        }

        @Override
        protected void updateDestination(HttpServletRequest request, AbstractHTTPDestination d) {
        }

        @Override
        public void invokeDestination(final HttpServletRequest request, HttpServletResponse response,
                                      AbstractHTTPDestination d) throws ServletException {
            invokeDestinationCalled = true;
        }

        public boolean invokeDestinationCalled() {
            return invokeDestinationCalled;
        }
    }
}
