package com.brest.bank.soap;

import javax.xml.soap.*;
import java.io.PrintStream;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BankDepositSoapServerTest
{
    private final String TARGET_ENDPOINT = "http://localhost:8080/BankDeposit/soap/server";
    private SOAPConnection connection;
    PrintStream out = System.out;

    private static final Logger LOGGER = LogManager.getLogger();

    /** Use the SAAJ API to send the SOAP message.
     * This simulates an external client and tests server side message handling.
     */
    @Before
    public void init(){
        try{
            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
        }catch (Exception e){
            LOGGER.error("Error create connection, Exception - {}",e.toString());
        }
    }

    @Test
    public void testClose() {
        LOGGER.info("Running test on close connection");
        try {
            SOAPConnection sCon = SOAPConnectionFactory.newInstance().createConnection();
            sCon.close();
        } catch (SOAPException e) {
            fail("Unexpected Exception " + e);
        }
    }

    @Test
    public void testCloseTwice() {
        LOGGER.info("Running the test trying to close the connection twice");
        SOAPConnectionFactory soapConnectionFactory = null;
        try {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
        } catch (SOAPException e) {
            fail("Unexpected Exception " + e);
        }

        SOAPConnection sCon = null;
        try {
            sCon = soapConnectionFactory.createConnection();
            sCon.close();
        } catch (SOAPException e) {
            fail("Unexpected Exception " + e);
        }

        try {
            sCon.close();
            fail("Expected Exception did not occur");
        } catch (SOAPException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCallOnCloseConnection() {
        LOGGER.info("Running the test call closed connection");
        SOAPConnectionFactory soapConnectionFactory = null;
        try {
            soapConnectionFactory = SOAPConnectionFactory.newInstance();
        } catch (SOAPException e) {
            fail("Unexpected Exception " + e);
        }

        SOAPConnection sCon = null;
        try {
            sCon = soapConnectionFactory.createConnection();
            sCon.close();
        } catch (SOAPException e) {
            fail("Unexpected Exception " + e);
        }

        try {
            sCon.call(null, new Object());
            fail("Expected Exception did not occur");
        } catch (SOAPException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSOAPConnection() throws Exception
    {
        LOGGER.debug("run test SOAP connection & calling");
        try {
            // Building the request document
            SOAPMessage reqMsg = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();
            SOAPEnvelope envelope = reqMsg.getSOAPPart().getEnvelope();
            SOAPBody body = envelope.getBody();

            body.addBodyElement(envelope.createName("getAllDeposits","methodName","http://www.XMLPowerCorp.com")).addTextNode("testBankDeposit");

            // Connecting and calling
            URL server = new URL(TARGET_ENDPOINT);

            SOAPMessage resMsg = connection.call(reqMsg, server);
            connection.close();

            // Showing output
            out.println("\n\nRequest:");
            reqMsg.writeTo(out);
            out.println("\n\nResponse:");
            resMsg.writeTo(out);
        } catch (Exception e) {
            LOGGER.error("Exception",e.toString());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate SOAP message", e);
        }
    }
}
