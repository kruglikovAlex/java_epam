package com.brest.bank.client;

import com.brest.bank.client.GenerateWSDL;

import javax.activation.DataHandler;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;

public class BankDepositSoapClient extends HttpServlet {

    //private GenerateWSDL generateWSDL = new GenerateWSDL();

    private SOAPConnection connection;
    private GenerateWSDL generateWSDL = new GenerateWSDL();
    private Writer wr;

    public void init(ServletConfig servletConfig) throws ServletException{
        super.init(servletConfig);
        try{
            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
            wr = generateWSDL.dumpWSDL(generateWSDL.createWSDL());
        }catch (Exception e){

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String outString = "<HTML><H1>Sending and reading the SOAP Message</H1><P>";
        //-- TODO предоставить список методов или файл WSDL
        //-- обработать request
        //-- создать SOAP сообщение для сервера
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
                outString += "SOAP incomingMessage received (see in.msg).</HTML>";
            }
        }catch (Throwable e){

        }
        try{
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(outString.getBytes());
            outputStream.write(wr.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){

        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

    }
}
