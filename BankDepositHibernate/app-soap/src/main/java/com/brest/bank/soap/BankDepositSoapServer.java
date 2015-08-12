package com.brest.bank.soap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.messaging.JAXMServlet;
import javax.xml.messaging.ReqRespListener;
import javax.xml.soap.*;
import java.util.Iterator;

/**
 * Created by alexander on 22.5.15.
 */
public class BankDepositSoapServer extends JAXMServlet implements ReqRespListener {
    static MessageFactory messageFactory = null;

    public void init(ServletConfig servletConfig) throws ServletException{
        super.init(servletConfig);
        try{
            messageFactory = MessageFactory.newInstance();

        }catch (SOAPException e){

        }
    }

    public SOAPMessage onMessage(SOAPMessage msg) {
        try{
            SOAPPart soapPart = msg.getSOAPPart();
            SOAPEnvelope incomingEnvelope = soapPart.getEnvelope();
            SOAPBody body = incomingEnvelope.getBody();

            //-- TODO проверить имя метода
            //-- и параметры
            //-- выполнить метод сервиса
            //-- вернуть клиенту SOAP сообщение с результатами метода сервиса

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

