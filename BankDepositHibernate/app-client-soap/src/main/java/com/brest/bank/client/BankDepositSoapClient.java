package com.brest.bank.client;

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
import java.io.PrintWriter;

/**
 * Created by alexander on 21.5.15.
 */
public class BankDepositSoapClient extends HttpServlet {
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

            body.addBodyElement(envelope.createName("numberAvailable","laptops","http://www.XMLPowerCorp.com")).addTextNode("216");

            StringBuffer clientUrl = new StringBuffer();
            clientUrl.append(request.getScheme()).append("://").append(request.getServerName());
            clientUrl.append(":").append(request.getServerPort()).append(request.getContextPath());

            String baseUrl = clientUrl.toString();
            URL url = new URL(baseUrl+"/indexSoap.html");

            AttachmentPart attachmentPart = outGoingMessage.createAttachmentPart(new DataHandler(url));

            attachmentPart.setContentType("text/html");
            outGoingMessage.addAttachmentPart(attachmentPart);

            URL server = new URL(baseUrl+"/soap/server");

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
