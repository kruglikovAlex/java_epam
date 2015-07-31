package com.brest.bank.client;

import java.util.*;

import javax.servlet.ServletConfig;
import javax.xml.soap.*;
import javax.xml.messaging.*;
import javax.servlet.ServletException;

/**
 * Created by alexander on 22.5.15.
 */
public class BankDepositSoapClient extends JAXMServlet implements ReqRespListener {
    static MessageFactory messageFactory = null;

    public void init(ServletConfig servletConfig) throws ServletException{
        super.init(servletConfig);
        try{
            messageFactory = MessageFactory.newInstance();
        }catch (SOAPException e){

        }
    }

    public SOAPMessage onMessage(SOAPMessage msg){
        try{
            SOAPPart soapPart = msg.getSOAPPart();
            SOAPEnvelope incomingEnvelope = soapPart.getEnvelope();
            SOAPBody body = incomingEnvelope.getBody();

            Iterator iterator = body.getChildElements(incomingEnvelope.createName("getAllDeposits","methodName",  "http://www.XMLPowerCorp.com"));

            SOAPElement element = (SOAPElement)iterator.next();

            SOAPMessage message = messageFactory.createMessage();
            SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

            envelope.getBody().addChildElement(envelope.createName("Response")).addTextNode("Got the SOAP message indicating there are " + element.getValue() + " getAllDeposits available.");

            return message;
        }catch (SOAPException e){
            return null;
        }
    }
}

