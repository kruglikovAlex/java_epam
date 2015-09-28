package com.brest.bank.client;

import com.brest.bank.client.GenerateWSDL;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.domain.BankDeposit;

import com.brest.bank.service.BankDepositServiceImpl;
import groovy.xml.QName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.w3c.dom.NodeList;

import javax.activation.DataHandler;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.*;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.StringTokenizer;

public class BankDepositSoapClient extends HttpServlet {

    public BankDepositService depositService = new BankDepositServiceImpl();

    private static final Logger LOGGER = LogManager.getLogger();

    private SOAPConnection connection;
    private GenerateWSDL generateWSDL = new GenerateWSDL();
    private StringWriter wr;

    public void init(ServletConfig servletConfig) throws ServletException{
        super.init(servletConfig);
        try{
            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
            LOGGER.error("connection - {}", connection.toString());
        }catch (Exception e){

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String outString = "<HTML><H1>Sending and reading the SOAP Message</H1><P>";
        LOGGER.debug("outString-{}", outString);
        //TODO oбработка mainSoap.js
        final PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }
        out.write("GET:\n"+sb.toString());

        try {
            MessageFactory messageFactory1 = MessageFactory.newInstance();
            SOAPMessage outGoingMessage1 = messageFactory1.createMessage();
            out.write("\n from request + SOAPMessage:\n"+outGoingMessage1.toString());
        }catch (Throwable e) {

        }

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
            URL url = new URL(baseUrl);//+"/indexSoap.html");

            AttachmentPart attachmentPart = outGoingMessage.createAttachmentPart(new DataHandler(url));

            attachmentPart.setContentType("text/html");
            outGoingMessage.addAttachmentPart(attachmentPart);

            URL server = new URL(baseUrl+"/soap/server");

            FileOutputStream outGoingFile = new FileOutputStream("out.msg");
            outGoingMessage.writeTo(outGoingFile);
            outGoingFile.close();

            outString += "SOAP outgoingMessage sent (see out.msg). <BR>";
            LOGGER.debug("outString-{}", outString);


            SOAPMessage incomingMessage = connection.call(outGoingMessage, server);

            if(incomingMessage != null){
                FileOutputStream incomingFile = new FileOutputStream("in.msg");
                incomingMessage.writeTo(incomingFile);
                incomingFile.close();
                outString += "SOAP incomingMessage received (see in.msg).</HTML>";
                LOGGER.debug("outString-{}", outString);
            }
        }catch (Throwable e){

        }
        try{
            //StringWriter wr = generateWSDL.dumpWSDL(generateWSDL.createWSDL());
            PrintWriter outputStream = response.getWriter();
            outputStream.write(wr.toString());
            outputStream.write(outString);


            //outputStream.write(wr.toString().getBytes());

            outputStream.flush();
            outputStream.close();
        }catch (IOException e){

        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        final OutputStream out = response.getOutputStream();
        StringBuilder sb = new StringBuilder();
        String s, values = null;
        BankDeposit deposit = null;

        String str = request.getPathInfo();
        StringTokenizer pathInfo = new StringTokenizer(str);
        int c = 0;
        while (pathInfo.hasMoreTokens()){
            pathInfo.nextToken("/");
            c++;
        }
        String[] path = new String[c];
        pathInfo = new StringTokenizer(str);
        c = 0;
        while (pathInfo.hasMoreTokens()) {
            path[c] = pathInfo.nextToken("/");
            c++;
        }

        /**
         * @path /getBankDeposits
         *
         */
        if(path[0].equalsIgnoreCase("getBankDeposits")){
            try{
                MessageFactory messageFactory = MessageFactory.newInstance();
                SOAPMessage outGoingMessage = messageFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                SOAPHeader header = envelope.getHeader();

                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("getAllDeposits", "method", "http://localhost:8080/BankDeposit/soap/"));

                for(Object d: depositService.getBankDeposits()) {
                    deposit = (BankDeposit) d;

                    SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit")).addTextNode(deposit.getDepositId().toString());
                    eDeposit.addChildElement(envelope.createName("depositName")).addTextNode(deposit.getDepositName());
                    eDeposit.addChildElement(envelope.createName("depositMinTerm")).addTextNode(""+deposit.getDepositMinTerm());
                    eDeposit.addChildElement(envelope.createName("depositMinAmount")).addTextNode(""+deposit.getDepositMinAmount());
                    eDeposit.addChildElement(envelope.createName("depositCurrency")).addTextNode(deposit.getDepositCurrency());
                    eDeposit.addChildElement(envelope.createName("depositInterestRate")).addTextNode(""+deposit.getDepositInterestRate());
                    eDeposit.addChildElement(envelope.createName("depositAddConditions")).addTextNode(deposit.getDepositAddConditions());
                }
                outGoingMessage.writeTo(out);
            } catch (HibernateException e) {
                LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                response.sendError(404,"Hibernate error - "+e.getMessage().toString()+"\n");
                throw new IOException(e.getMessage());
            } catch (SOAPException e) {
                LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
                response.sendError(404,"SOAP error - "+e.getMessage().toString()+"\n");
                throw new IOException(e.getMessage());
            }
        }

        /**
         * @path /addBankDeposit
         *
         */
        if(path[0].equalsIgnoreCase("addBankDeposit")){
            try {
                MessageFactory messageFactory = MessageFactory.newInstance();
                MimeHeaders header = new MimeHeaders();
                //header.setHeader("Content-Type", "text/xml");
                //or
                s = request.getHeader("Content-Type");
                header.setHeader("Content-Type",s);
                SOAPMessage inGoingMessage = messageFactory.createMessage(header,request.getInputStream());
                try {
                    SOAPPart sp = inGoingMessage.getSOAPPart();
                    SOAPEnvelope env = sp.getEnvelope();
                    SOAPHeader hdr = env.getHeader();
                    SOAPBody bdy = env.getBody();

                    Iterator ii = bdy.getChildElements();

                    SOAPElement ee[] = new SOAPElement[7];

                    while (ii.hasNext()) {
                        SOAPElement e = (SOAPElement)ii.next();
                        String methodName = e.getElementName().getLocalName();
                        Iterator kk = e.getChildElements();
                        int count = 0;
                        while (kk.hasNext()) {
                            ee[count] = (SOAPElement)kk.next();
                            count ++;
                        }
                    }
                    if (ee.length>0) {
                        deposit = new BankDeposit();
                            deposit.setDepositName(ee[1].getValue());
                            deposit.setDepositMinTerm(Integer.parseInt(ee[2].getValue()));
                            deposit.setDepositMinAmount(Integer.parseInt(ee[3].getValue()));
                            deposit.setDepositCurrency(ee[4].getValue());
                            deposit.setDepositInterestRate(Integer.parseInt(ee[5].getValue()));
                            deposit.setDepositAddConditions(ee[6].getValue());

                        depositService.addBankDeposit(deposit);

                        MessageFactory mFactory = MessageFactory.newInstance();
                        SOAPMessage outGoingMessage = mFactory.createMessage();

                        SOAPPart soapPart = outGoingMessage.getSOAPPart();
                        SOAPEnvelope envelope = soapPart.getEnvelope();
                        SOAPHeader h = envelope.getHeader();
                        SOAPBody body = envelope.getBody();

                        SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("addBankDeposit", "method", "http://localhost:8080/BankDeposit/soap/"));

                        SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit")).addTextNode(deposit.getDepositId().toString());
                        eDeposit.addChildElement(envelope.createName("depositName")).addTextNode(deposit.getDepositName());
                        eDeposit.addChildElement(envelope.createName("depositMinTerm")).addTextNode(""+deposit.getDepositMinTerm());
                        eDeposit.addChildElement(envelope.createName("depositMinAmount")).addTextNode(""+deposit.getDepositMinAmount());
                        eDeposit.addChildElement(envelope.createName("depositCurrency")).addTextNode(deposit.getDepositCurrency());
                        eDeposit.addChildElement(envelope.createName("depositInterestRate")).addTextNode(""+deposit.getDepositInterestRate());
                        eDeposit.addChildElement(envelope.createName("depositAddConditions")).addTextNode(deposit.getDepositAddConditions());

                        outGoingMessage.writeTo(out);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }catch (Throwable e) {
            }
        }

    }
}
