package com.brest.bank.client;

import com.brest.bank.client.GenerateWSDL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class BankDepositSoapClient extends HttpServlet {

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
        //TODO oбработка mainSoap.js
        final OutputStream out = response.getOutputStream();
        StringBuilder sb = new StringBuilder();
        String s, values = null;

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();
            //header.setHeader("Content-Type", "text/xml");
            //or
            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            SOAPMessage outGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            try {

                SOAPPart sp = outGoingMessage.getSOAPPart();
                SOAPEnvelope env = sp.getEnvelope();
                SOAPHeader hdr = env.getHeader();
                SOAPBody bdy = env.getBody();

                Iterator ii = bdy.getChildElements();
                SOAPElement ee[] = new SOAPElement[7];

                while (ii.hasNext()) {

                    SOAPElement e = (SOAPElement)ii.next();
                    String methodName = e.getElementName().getLocalName();

                    s = "method name: "+ methodName + "\n";
                    out.write(s.getBytes());

                    Iterator kk = e.getChildElements();

                    int count = 0;

                    while (kk.hasNext()) {

                        ee[count] = (SOAPElement)kk.next();
                        //String name = ee[count].getElementName().getLocalName();

                        /*if( name != null && name.equals("depositName") ) {
                            values = ee.getValue();

                            s = "depositName = " + values;
                            out.write(s.getBytes());
                            break;
                        }*/

                        //values = ee[count].getValue();

                        //s = " value = " + values + "\n";
                        //out.write(s.getBytes());
                        count ++;
                    }
                    if (ee.length>0){
                        s = "{\n";
                        out.write(s.getBytes());
                        for (int i=0; i<ee.length; i++){
                            s = "" + ee[i].getElementName().getLocalName()+ ": " + ee[i].getValue() + ",\n";
                            out.write(s.getBytes());
                        }
                        s = "}\n";
                        out.write(s.getBytes());
                    }
                }

                if (ee.length>0) {
                    outGoingMessage.writeTo(out);
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }catch (Throwable e) {

        }
    }
}
