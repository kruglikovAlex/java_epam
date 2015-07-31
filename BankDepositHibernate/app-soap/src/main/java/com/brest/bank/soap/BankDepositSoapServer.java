package com.brest.bank.soap;

import java.io.*;
import java.net.*;

import javax.servlet.ServletConfig;
import javax.xml.soap.*;
import javax.activation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BankDepositSoapServer extends HttpServlet {

    private SOAPConnection connection;

    public void init(ServletConfig servletConfig) throws ServletException{
        super.init(servletConfig);
        try{
            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
        }catch (Exception e){

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String outString = "<HTML><H1>Sending and reading the SOAP Message</H1><P>";
        try{
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage outGoingMessage = messageFactory.createMessage();

            SOAPPart soapPart = outGoingMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();

            body.addBodyElement(envelope.createName("getAllDeposits","methodName","http://www.XMLPowerCorp.com")).addTextNode("testBankDeposit");

            StringBuffer clientUrl = new StringBuffer();
            clientUrl.append(request.getScheme()).append("://").append(request.getServerName());
            clientUrl.append(":").append(request.getServerPort()).append(request.getContextPath());

            String baseUrl = clientUrl.toString();
            URL url = new URL(baseUrl+"/indexSoap.html");

            AttachmentPart attachmentPart = outGoingMessage.createAttachmentPart(new DataHandler(url));

            attachmentPart.setContentType("text/html");
            outGoingMessage.addAttachmentPart(attachmentPart);

            URL server = new URL(baseUrl+"/soap/client");

            FileOutputStream outGoingFile = new FileOutputStream("out.msg");
            outGoingMessage.writeTo(outGoingFile);
            outGoingFile.close();

            outString += "SOAP outgoingMessage sent (see out.msg). <BR>";

            SOAPMessage incomingMessage = connection.call(outGoingMessage, server);

            if(incomingMessage != null){
                FileOutputStream incomingFile = new FileOutputStream("in.msg");
                incomingMessage.writeTo(incomingFile);
                incomingFile.close();
                outString += "SOAP outgoingMessage received (see in.msg).</HTML>";
            }
        }catch (Throwable e){

        }
        try{
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(outString.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){

        }
    }
}
