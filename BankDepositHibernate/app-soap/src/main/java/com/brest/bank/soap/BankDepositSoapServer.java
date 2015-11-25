package com.brest.bank.soap;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.service.BankDepositorServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class BankDepositSoapServer extends HttpServlet {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final Logger LOGGER = LogManager.getLogger();

    public BankDepositService depositService = new BankDepositServiceImpl();
    public BankDepositorService depositorService = new BankDepositorServiceImpl();

    private BankDeposit deposit = null;
    private BankDepositor depositor = null;

    protected SOAPConnection connection;

    private MessageFactory messageFactory = null;
    private SOAPMessage outGoingMessage = null;
    private SOAPMessage inGoingMessage = null;

    private HttpServletRequest request= null;
    private HttpServletResponse response = null;

    public void init(ServletConfig servletConfig) throws ServletException{
        super.init(servletConfig);
        try{
            SOAPConnectionFactory connectionFactory = SOAPConnectionFactory.newInstance();
            connection = connectionFactory.createConnection();
            LOGGER.info("connection - {}", connection.toString());
        }catch (SOAPException e){
            LOGGER.error("error - {}", e.getMessage());
            throw new IllegalArgumentException("ERROR: SOAPException - "+e.getMessage());
        }
    }

    public void setHttpServletRequest(HttpServletRequest request){
        this.request = request;
    }

    public void setHttpServletResponse(HttpServletResponse response){
        this.response = response;
    }

    /**
     * Servlet method responding to HTTP GET methods calls.
     * open WSDL file
     *
     * @param req HTTP request.
     * @param res HTTP response.
     */
    @Override
    public void doGet(HttpServletRequest req,
                      HttpServletResponse res) throws ServletException, IOException{

        if ((request == null)&&(response == null)){
            setHttpServletRequest(req);
            setHttpServletResponse(res);
        }

        final OutputStream out = response.getOutputStream();

        BufferedInputStream is = new BufferedInputStream(new FileInputStream("depositsWSDL.xml"));
        byte[] buf = new byte[4 * 1024]; // 4K buffer
        int bytesRead;
        while ((bytesRead = is.read(buf)) != -1) {
            out.write(buf, 0, bytesRead);
        }
        is.close();

        out.flush();
        out.close();

        response = null;
        request = null;
    }

    /**
     * Servlet method responding to HTTP POST methods calls.
     *
     * @param req HTTP request.
     * @param res HTTP response.
     */
    @Override
    public void doPost(HttpServletRequest req,
                       HttpServletResponse res) throws ServletException, IOException, NullPointerException{

        if ((request == null)&&(response == null)){
            setHttpServletRequest(req);
            setHttpServletResponse(res);
        }

        final OutputStream out = response.getOutputStream();

        try{
            /**
            * @path /getBankDeposits
            */
            assert request != null;
            if("getBankDeposits".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                getBankDeposits(response,out);
            }

            /**
             * @path /addBankDeposit
             * @param SOAPMessage <BankDeposit>{<></>}</BankDeposit>
             */
            if("addBankDeposit".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                addBankdeposit(request,response,out);
            }

            /**
             * @path /getBankDepositById
             * @param SOAPMessage <depositId>{id}</depositId>
             */
            if("getBankDepositById".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                getBankDepositById(request,response,out);
            }

            /**
             * @path /getBankDepositByName
             * @param SOAPMessage <depositName>{Name}</depositName>
             */
            if("getBankDepositByName".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                getBankDepositByName(request,response,out);
            }

            /**
             * @path /updateBankDeposit
             * @param SOAPMessage <BankDeposit>{<></>}</BankDeposit>
             */
            if("updateBankDeposit".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                updateBankDeposit(request,response,out);
            }

            /**
             * @path /getBankDepositors
             */
            if("getBankDepositors".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                getBankDepositors(response,out);
            }

            /**
             * @path /addBankDepositor
             * @param SOAPMessage <BankDepositor>{<></>}</BankDepositor>
             */
            if("addBankDepositor".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                addBankDepositor(request,response,out);
            }

            /**
             * @path /getBankDepositorById
             * @param SOAPMessage <depositorId>{id}</depositorId>
             */
            if("getBankDepositorById".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                getBankDepositorById(request,response,out);
            }

            /**
             * @path /getBankDepositorByName
             * @param SOAPMessage <depositorName>{Name}</depositorName>
             */
            if("getBankDepositorByName".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                getBankDepositorByName(request,response,out);
            }

            /**
             * @path /updateBankDepositor
             * @param SOAPMessage <BankDepositor>{<></>}</BankDepositor>
             */
            if("updateBankDepositor".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                updateBankDepositor(request,response,out);
            }

            /**
             * @path /removeBankDepositor
             * @param SOAPMessage <depositorId>{id}</depositorId>
             */
            if("removeBankDepositor".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                removeBankDepositor(request,response,out);
            }

            /**
             * @path /deleteBankDeposit
             * @param SOAPMessage <depositId>{id}</depositId>
             */
            if("deleteBankDeposit".equalsIgnoreCase(request.getHeader("SOAPAction"))){
                deleteBankDeposit(request,response,out);
            }
        }catch (NullPointerException e){
            LOGGER.error("Null Pointer Exception - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Null Pointer Exception - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();

            response = null;
            request = null;
        }

    }

    /**
     * @param response Http response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void getBankDeposits(HttpServletResponse response,
                                 OutputStream out) throws ServletException, IOException{
        try{
            messageFactory = MessageFactory.newInstance();
            outGoingMessage = messageFactory.createMessage();

            SOAPPart soapPart = outGoingMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            //SOAPHeader header = envelope.getHeader();

            SOAPBody body = envelope.getBody();

            SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("getAllDeposits", "method", "http://localhost:8080/BankDeposit/soap/"));

            for(Object d: depositService.getBankDeposits()) {
                deposit = (BankDeposit) d;

                SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit"));
                    eDeposit.addChildElement(envelope.createName("depositId")).addTextNode(deposit.getDepositId().toString());
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
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http request
     * @param response Http response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void addBankdeposit(HttpServletRequest request,
                                HttpServletResponse response,
                                OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();
            //header.setHeader("Content-Type", "text/xml");
            //or
            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();

            SOAPElement ee[] = new SOAPElement[7];

            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
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
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("addBankDeposit", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit"));
                    eDeposit.addChildElement(envelope.createName("depositId")).addTextNode(deposit.getDepositId().toString());
                    eDeposit.addChildElement(envelope.createName("depositName")).addTextNode(deposit.getDepositName());
                    eDeposit.addChildElement(envelope.createName("depositMinTerm")).addTextNode(""+deposit.getDepositMinTerm());
                    eDeposit.addChildElement(envelope.createName("depositMinAmount")).addTextNode(""+deposit.getDepositMinAmount());
                    eDeposit.addChildElement(envelope.createName("depositCurrency")).addTextNode(deposit.getDepositCurrency());
                    eDeposit.addChildElement(envelope.createName("depositInterestRate")).addTextNode(""+deposit.getDepositInterestRate());
                    eDeposit.addChildElement(envelope.createName("depositAddConditions")).addTextNode(deposit.getDepositAddConditions());

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http request
     * @param response Http response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void getBankDepositById(HttpServletRequest request,
                                    HttpServletResponse response,
                                    OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();
            //header.setHeader("Content-Type", "text/xml");
            //or
            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();
            SOAPElement ee = null;
            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                while (kk.hasNext()) {
                    ee = (SOAPElement)kk.next();
                }
            }
            if (ee!=null) {
                deposit = depositService.getBankDepositById(Long.parseLong(ee.getValue()));

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("getBankDepositById", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit"));
                    eDeposit.addChildElement(envelope.createName("depositId")).addTextNode(deposit.getDepositId().toString());
                    eDeposit.addChildElement(envelope.createName("depositName")).addTextNode(deposit.getDepositName());
                    eDeposit.addChildElement(envelope.createName("depositMinTerm")).addTextNode(""+deposit.getDepositMinTerm());
                    eDeposit.addChildElement(envelope.createName("depositMinAmount")).addTextNode(""+deposit.getDepositMinAmount());
                    eDeposit.addChildElement(envelope.createName("depositCurrency")).addTextNode(deposit.getDepositCurrency());
                    eDeposit.addChildElement(envelope.createName("depositInterestRate")).addTextNode(""+deposit.getDepositInterestRate());
                    eDeposit.addChildElement(envelope.createName("depositAddConditions")).addTextNode(deposit.getDepositAddConditions());

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void getBankDepositByName(HttpServletRequest request,
                                      HttpServletResponse response,
                                      OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();

            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();
            SOAPElement ee = null;
            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                while (kk.hasNext()) {
                    ee = (SOAPElement)kk.next();
                }
            }
            if (ee!=null) {
                deposit = depositService.getBankDepositByName(ee.getValue());

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("getBankDepositByName", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit"));
                    eDeposit.addChildElement(envelope.createName("depositId")).addTextNode(deposit.getDepositId().toString());
                    eDeposit.addChildElement(envelope.createName("depositName")).addTextNode(deposit.getDepositName());
                    eDeposit.addChildElement(envelope.createName("depositMinTerm")).addTextNode(""+deposit.getDepositMinTerm());
                    eDeposit.addChildElement(envelope.createName("depositMinAmount")).addTextNode(""+deposit.getDepositMinAmount());
                    eDeposit.addChildElement(envelope.createName("depositCurrency")).addTextNode(deposit.getDepositCurrency());
                    eDeposit.addChildElement(envelope.createName("depositInterestRate")).addTextNode(""+deposit.getDepositInterestRate());
                    eDeposit.addChildElement(envelope.createName("depositAddConditions")).addTextNode(deposit.getDepositAddConditions());

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void updateBankDeposit(HttpServletRequest request,
                                   HttpServletResponse response,
                                   OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();

            MimeHeaders header = new MimeHeaders();
            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();

            SOAPElement ee[] = new SOAPElement[7];

            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                int count = 0;
                while (kk.hasNext()) {
                    ee[count] = (SOAPElement)kk.next();
                    count ++;
                }
            }
            if (ee.length>0) {
                deposit = new BankDeposit();
                    deposit.setDepositId(Long.parseLong(ee[0].getValue()));
                    deposit.setDepositName(ee[1].getValue());
                    deposit.setDepositMinTerm(Integer.parseInt(ee[2].getValue()));
                    deposit.setDepositMinAmount(Integer.parseInt(ee[3].getValue()));
                    deposit.setDepositCurrency(ee[4].getValue());
                    deposit.setDepositInterestRate(Integer.parseInt(ee[5].getValue()));
                    deposit.setDepositAddConditions(ee[6].getValue());

                depositService.updateBankDeposit(deposit);

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("updateBankDeposit", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit"));
                    eDeposit.addChildElement(envelope.createName("depositId")).addTextNode(deposit.getDepositId().toString());
                    eDeposit.addChildElement(envelope.createName("depositName")).addTextNode(deposit.getDepositName());
                    eDeposit.addChildElement(envelope.createName("depositMinTerm")).addTextNode(""+deposit.getDepositMinTerm());
                    eDeposit.addChildElement(envelope.createName("depositMinAmount")).addTextNode(""+deposit.getDepositMinAmount());
                    eDeposit.addChildElement(envelope.createName("depositCurrency")).addTextNode(deposit.getDepositCurrency());
                    eDeposit.addChildElement(envelope.createName("depositInterestRate")).addTextNode(""+deposit.getDepositInterestRate());
                    eDeposit.addChildElement(envelope.createName("depositAddConditions")).addTextNode(deposit.getDepositAddConditions());

                outGoingMessage.writeTo(out);
            }
        } catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404, "Hibernate error - " + e.getMessage() + "\n");
            throw new IOException(e.getMessage());
        }catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void getBankDepositors(HttpServletResponse response,
                                   OutputStream out) throws ServletException, IOException{
        try{
            messageFactory = MessageFactory.newInstance();
            outGoingMessage = messageFactory.createMessage();

            SOAPPart soapPart = outGoingMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            //SOAPHeader header = envelope.getHeader();

            SOAPBody body = envelope.getBody();

            SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("getAllDepositors", "method", "http://localhost:8080/BankDeposit/soap/"));

            for(Object d: depositorService.getBankDepositors()) {
                depositor = (BankDepositor) d;

                SOAPElement eDepositor = eMethod.addChildElement(envelope.createName("BankDepositor"));
                    eDepositor.addChildElement(envelope.createName("depositorId")).addTextNode(depositor.getDepositorId().toString());
                    eDepositor.addChildElement(envelope.createName("depositorName")).addTextNode(depositor.getDepositorName());
                    eDepositor.addChildElement(envelope.createName("depositorDateDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountPlusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountPlusDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountMinusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountMinusDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorDateReturnDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorMarkReturnDeposit")).addTextNode(Integer.toString(depositor.getDepositorMarkReturnDeposit()));
            }
            outGoingMessage.writeTo(out);
        } catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void addBankDepositor(HttpServletRequest request,
                                  HttpServletResponse response,
                                  OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();

            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());
            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();

            SOAPElement ee[] = new SOAPElement[9];

            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                int count = 0;
                while (kk.hasNext()) {
                    ee[count] = (SOAPElement)kk.next();
                    count ++;
                }
            }
            if (ee.length>0) {
                depositor = new BankDepositor(null,"",dateFormat.parse("1900-01-01"),0,0,0,dateFormat.parse("1900-01-01"),0,null);
                    depositor.setDepositorName(ee[2].getValue());
                    depositor.setDepositorDateDeposit(dateFormat.parse(ee[3].getValue()));
                    depositor.setDepositorAmountDeposit(Integer.parseInt(ee[4].getValue()));
                    depositor.setDepositorAmountPlusDeposit(Integer.parseInt(ee[5].getValue()));
                    depositor.setDepositorAmountMinusDeposit(Integer.parseInt(ee[6].getValue()));
                    depositor.setDepositorDateReturnDeposit(dateFormat.parse(ee[7].getValue()));
                    depositor.setDepositorMarkReturnDeposit(Integer.parseInt(ee[8].getValue()));

                depositorService.addBankDepositor(Long.parseLong(ee[0].getValue()),depositor);

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("addBankDepositor", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDepositor = eMethod.addChildElement(envelope.createName("BankDepositor"));
                    eDepositor.addChildElement(envelope.createName("depositorId")).addTextNode(Long.toString(depositor.getDepositorId()));
                    eDepositor.addChildElement(envelope.createName("depositorName")).addTextNode(depositor.getDepositorName());
                    eDepositor.addChildElement(envelope.createName("depositorDateDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountPlusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountPlusDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountMinusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountMinusDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorDateReturnDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorMarkReturnDeposit")).addTextNode(Integer.toString(depositor.getDepositorMarkReturnDeposit()));

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (ParseException e){
            LOGGER.error("Parse error - {},/n{}", e.getMessage(), e.getStackTrace());
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void getBankDepositorById(HttpServletRequest request,
                                      HttpServletResponse response,
                                      OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();

            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();
            SOAPElement ee = null;
            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                while (kk.hasNext()) {
                    ee = (SOAPElement)kk.next();
                }
            }
            if (ee!=null) {
                depositor = depositorService.getBankDepositorById(Long.parseLong(ee.getValue()));

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("getBankDepositorById", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDepositor = eMethod.addChildElement(envelope.createName("BankDepositor"));
                    eDepositor.addChildElement(envelope.createName("depositorId")).addTextNode(depositor.getDepositorId().toString());
                    eDepositor.addChildElement(envelope.createName("depositorName")).addTextNode(depositor.getDepositorName());
                    eDepositor.addChildElement(envelope.createName("depositorDateDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountPlusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountPlusDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountMinusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountMinusDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorDateReturnDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorMarkReturnDeposit")).addTextNode(Integer.toString(depositor.getDepositorMarkReturnDeposit()));

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void getBankDepositorByName(HttpServletRequest request,
                                        HttpServletResponse response,
                                        OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();

            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();
            SOAPElement ee = null;
            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                while (kk.hasNext()) {
                    ee = (SOAPElement)kk.next();
                }
            }
            if (ee!=null) {
                depositor = depositorService.getBankDepositorByName(ee.getValue());

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("getBankDepositorByName", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDepositor = eMethod.addChildElement(envelope.createName("BankDepositor"));
                eDepositor.addChildElement(envelope.createName("depositorId")).addTextNode(depositor.getDepositorId().toString());
                eDepositor.addChildElement(envelope.createName("depositorName")).addTextNode(depositor.getDepositorName());
                eDepositor.addChildElement(envelope.createName("depositorDateDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateDeposit()));
                eDepositor.addChildElement(envelope.createName("depositorAmountDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountDeposit()));
                eDepositor.addChildElement(envelope.createName("depositorAmountPlusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountPlusDeposit()));
                eDepositor.addChildElement(envelope.createName("depositorAmountMinusDeposit")).addTextNode(Integer.toString(depositor.getDepositorAmountMinusDeposit()));
                eDepositor.addChildElement(envelope.createName("depositorDateReturnDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                eDepositor.addChildElement(envelope.createName("depositorMarkReturnDeposit")).addTextNode(Integer.toString(depositor.getDepositorMarkReturnDeposit()));

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void updateBankDepositor(HttpServletRequest request,
                                     HttpServletResponse response,
                                     OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();

            MimeHeaders header = new MimeHeaders();
            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();

            SOAPElement ee[] = new SOAPElement[9];

            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                int count = 0;
                while (kk.hasNext()) {
                    ee[count] = (SOAPElement)kk.next();
                    count ++;
                }
            }
            if (ee.length>0) {
                depositor = new BankDepositor(null,"",dateFormat.parse("1900-01-01"),0,0,0,dateFormat.parse("1900-01-01"),0,null);
                    depositor.setDepositorId(Long.parseLong(ee[1].getValue()));
                    depositor.setDepositorName(ee[2].getValue());
                    depositor.setDepositorDateDeposit(dateFormat.parse(ee[3].getValue()));
                    depositor.setDepositorAmountDeposit(Integer.parseInt(ee[4].getValue()));
                    depositor.setDepositorAmountPlusDeposit(Integer.parseInt(ee[5].getValue()));
                    depositor.setDepositorAmountMinusDeposit(Integer.parseInt(ee[6].getValue()));
                    depositor.setDepositorDateReturnDeposit(dateFormat.parse(ee[7].getValue()));
                    depositor.setDepositorMarkReturnDeposit(Integer.parseInt(ee[8].getValue()));

                depositorService.updateBankDepositor(depositor);

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("updateBankDepositor", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDepositor = eMethod.addChildElement(envelope.createName("BankDepositor"));
                    eDepositor.addChildElement(envelope.createName("depositorId")).addTextNode(depositor.getDepositorId().toString());
                    eDepositor.addChildElement(envelope.createName("depositorName")).addTextNode(depositor.getDepositorName());
                    eDepositor.addChildElement(envelope.createName("depositorDateDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorAmountDeposit")).addTextNode(""+depositor.getDepositorAmountDeposit());
                    eDepositor.addChildElement(envelope.createName("depositorAmountPlusDeposit")).addTextNode(""+depositor.getDepositorAmountPlusDeposit());
                    eDepositor.addChildElement(envelope.createName("depositorAmountMinusDeposit")).addTextNode(""+depositor.getDepositorAmountMinusDeposit());
                    eDepositor.addChildElement(envelope.createName("depositorDateReturnDeposit")).addTextNode(dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                    eDepositor.addChildElement(envelope.createName("depositorMarkReturnDeposit")).addTextNode(""+depositor.getDepositorMarkReturnDeposit());

                outGoingMessage.writeTo(out);
            }
        } catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404, "Hibernate error - " + e.getMessage() + "\n");
            throw new IOException(e.getMessage());
        }catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }catch (ParseException e){
            LOGGER.error("Parse error - {},/n{}", e.getMessage(), e.getStackTrace());
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out OutputStream
     * @throws ServletException
     * @throws IOException
     */
    private void removeBankDepositor(HttpServletRequest request,
                                     HttpServletResponse response,
                                     OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();

            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();
            SOAPElement ee = null;
            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                while (kk.hasNext()) {
                    ee = (SOAPElement)kk.next();
                }
            }
            if (ee!=null) {
                depositorService.removeBankDepositor(Long.parseLong(ee.getValue()));

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("removeBankDepositor", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDepositor"));
                    eDeposit.addChildElement(envelope.createName("result")).addTextNode("Depositor with ID:"+ee.getValue()+" removed");

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }

    /**
     * @param request Http servlet request
     * @param response Http servlet response
     * @param out Outputstream
     * @throws ServletException
     * @throws IOException
     */
    private void deleteBankDeposit(HttpServletRequest request,
                                   HttpServletResponse response,
                                   OutputStream out) throws ServletException, IOException{
        String s;
        try {
            messageFactory = MessageFactory.newInstance();
            MimeHeaders header = new MimeHeaders();

            s = request.getHeader("Content-Type");
            header.setHeader("Content-Type",s);

            inGoingMessage = messageFactory.createMessage(header,request.getInputStream());

            SOAPPart sp = inGoingMessage.getSOAPPart();
            SOAPEnvelope env = sp.getEnvelope();
            //SOAPHeader hdr = env.getHeader();
            SOAPBody bdy = env.getBody();

            Iterator ii = bdy.getChildElements();
            SOAPElement ee = null;
            while (ii.hasNext()) {
                SOAPElement e = (SOAPElement)ii.next();
                Iterator kk = e.getChildElements();
                while (kk.hasNext()) {
                    ee = (SOAPElement)kk.next();
                }
            }
            if (ee!=null) {
                depositService.deleteBankDeposit(Long.parseLong(ee.getValue()));

                MessageFactory mFactory = MessageFactory.newInstance();
                outGoingMessage = mFactory.createMessage();

                SOAPPart soapPart = outGoingMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //SOAPHeader h = envelope.getHeader();
                SOAPBody body = envelope.getBody();

                SOAPBodyElement eMethod = body.addBodyElement(envelope.createName("deleteBankDeposit", "method", "http://localhost:8080/BankDeposit/soap/"));

                SOAPElement eDeposit = eMethod.addChildElement(envelope.createName("BankDeposit"));
                    eDeposit.addChildElement(envelope.createName("result")).addTextNode("Deposit with ID:"+ee.getValue()+" removed");

                outGoingMessage.writeTo(out);
            }
        }catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        } catch (SOAPException e) {
            LOGGER.error("SOAP error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"SOAP error - "+e.getMessage()+"\n");
            throw new IOException(e.getMessage());
        }finally {
            out.flush();
            out.close();
        }
    }
}
