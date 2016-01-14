package com.brest.bank.soap;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.service.DataFixture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.easymock.EasyMock.*;

import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.server.RequestCreators;
import org.springframework.ws.test.server.ResponseMatchers;

import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-soap-Mock-test.xml"})
public class DepositSoapEndpointTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    BankDepositService depositService;

    @Autowired
    BankDepositorService depositorService;

    @Autowired
    DepositSoapEndpoint depositSoapEndpoint;

    @Autowired
    ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @Before
    public void createClient() {
        depositSoapEndpoint = new DepositSoapEndpoint(depositService, depositorService);
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @After
    public void tearDown(){
        reset(depositService);
    }

    @Test
    public void testGetBankDepositById() throws Exception {
        LOGGER.debug("testGetBankDepositById() - run");

        expect(depositService.getBankDepositById(1L)).andReturn(DataFixture.getExistDeposit(1L));
        replay(depositService);

        Source requestPayload = new StringSource(
                "<getBankDepositByIdRequest xmlns='http://bank.brest.com/soap'>" +
                    "<depositId>1</depositId>" +
                "</getBankDepositByIdRequest>");

        Source responsePayload = new StringSource(
                "<getBankDepositByIdResponse xmlns=\"http://bank.brest.com/soap\">" +
                        "<bankDeposit>" +
                            "<depositId>1</depositId>" +
                            "<depositName>depositName1</depositName>" +
                            "<depositMinTerm>12</depositMinTerm>" +
                            "<depositMinAmount>1000</depositMinAmount>" +
                            "<depositCurrency>usd</depositCurrency>" +
                            "<depositInterestRate>4</depositInterestRate>" +
                            "<depositAddConditions>conditions1</depositAddConditions>" +
                        "</bankDeposit>" +
                "</getBankDepositByIdResponse>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }

    @Test
    public void testInvalidRequestGetBankDepositById() {
        LOGGER.debug("testInvalidGetBankDepositById() - run");

        Source requestPayload = new StringSource(
                "<getBankDepositByIdRequest xmlns='http://bank.brest.com/soap' " +
                        "depositId='1'/>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">java.lang.NullPointerException</faultstring>" +
                "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));
    }

    @Test
    public void testInvalidIdGetBankDepositById() {
        LOGGER.debug("testInvalidGetBankDepositById() - run");

        depositService.getBankDepositById(-1L);
        expectLastCall().andThrow(new IllegalArgumentException());
        replay(depositService);


        Source requestPayload = new StringSource(
                "<getBankDepositByIdRequest xmlns='http://bank.brest.com/soap'>" +
                        "<depositId>-1</depositId>" +
                "</getBankDepositByIdRequest>");

        Source responsePayload = new StringSource(
                "<SOAP-ENV:Fault xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                        "<faultcode>SOAP-ENV:Server</faultcode>" +
                        "<faultstring xml:lang=\"en\">java.lang.IllegalArgumentException</faultstring>" +
                        "</SOAP-ENV:Fault>");

        RequestCreator creator = RequestCreators.withPayload(requestPayload);

        this.mockClient
                .sendRequest(creator)
                .andExpect(ResponseMatchers.payload(responsePayload));

        verify(depositService);
    }
}
