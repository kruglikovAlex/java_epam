package com.brest.bank.soap;

import javax.xml.soap.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BankDepositSoapConnectionTest
{
    private SOAPConnection connection;

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
            connection = SOAPConnectionFactory.newInstance().createConnection();
            connection.close();
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

        connection = null;
        try {
            connection = soapConnectionFactory.createConnection();
            connection.close();
        } catch (SOAPException e) {
            fail("Unexpected Exception " + e);
        }

        try {
            connection.close();
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

        connection = null;
        try {
            connection = soapConnectionFactory.createConnection();
            connection.close();
        } catch (SOAPException e) {
            fail("Unexpected Exception " + e);
        }

        try {
            connection.call(null, new Object());
            fail("Expected Exception did not occur");
        } catch (SOAPException e) {
            assertTrue(true);
        }
    }
}
