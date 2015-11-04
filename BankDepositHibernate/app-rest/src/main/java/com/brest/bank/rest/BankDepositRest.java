package com.brest.bank.rest;

/**
 * Created by alexander on 27.4.15.
 */
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.service.BankDepositService;

import com.brest.bank.service.BankDepositorServiceImpl;
import com.brest.bank.util.HibernateUtil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class BankDepositRest extends HttpServlet{

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private List<BankDeposit> deposits = new ArrayList<BankDeposit>();
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();
    private BankDeposit deposit;
    private BankDepositor depositor;

    private BankDepositorService depositorService = new BankDepositorServiceImpl();
    private BankDepositService depositService = new BankDepositServiceImpl();

    private HttpServletRequest request= null;
    private HttpServletResponse response = null;

    public void setHttpServletRequest(HttpServletRequest request){
        this.request = request;
    }

    public void setHttpServletResponse(HttpServletResponse response){
        this.response = response;
    }

    /**
     * Servlet method responding to HTTP GET methods calls.
     *
     * @param req HTTP request.
     * @param res HTTP response.
     */
    @Override
    public void doGet( HttpServletRequest req,
                       HttpServletResponse res ) throws IOException
    {
        if ((request == null)&&(response == null)){
            //request = new HttpServletRequestWrapper(req);
            //response = new HttpServletResponseWrapper(res);
            setHttpServletRequest(req);
            setHttpServletResponse(res);
        }

        response.setContentType("application/json");
        final PrintWriter out = response.getWriter();

        //call get method
        get(request, response, out);

        out.flush();
        out.close();

        response = null;
        request = null;
    }

    /**
     * Servlet method responding to HTTP PUT methods calls.
     * update BankDeposit
     *
     * @param req HTTP request.
     * @param res HTTP response.
     */
    @Override
    public void doPut( HttpServletRequest req,
                       HttpServletResponse res ) throws IOException
    {
        if ((request == null)&&(response == null)){
            setHttpServletRequest(req);
            setHttpServletResponse(res);
        }
        final PrintWriter out = response.getWriter();

        //call update method
        update(request,response,out);
        out.flush();
        out.close();

        response = null;
        request = null;
    }

    /**
     * Servlet method responding to HTTP POST methods calls.
     * insert BankDeposit
     *
     * @param req HTTP request.
     * @param res HTTP response.
     */
    @Override
    public void doPost( HttpServletRequest req,
                        HttpServletResponse res ) throws IOException
    {
        if ((request == null)&&(response == null)){
            setHttpServletRequest(req);
            setHttpServletResponse(res);
        }

        response.setContentType("application/json");
        final PrintWriter out = response.getWriter();

        //call insert method
        insert(request,response,out);
        out.flush();
        out.close();

        response = null;
        request = null;
    }

    /**
     * Servlet method responding to HTTP DELETE methods calls.
     * delete BankDeposit
     *
     * @param req HTTP request.
     * @param res HTTP response.
     */
    @Override
    public void doDelete( HttpServletRequest req,
                          HttpServletResponse res ) throws IOException
    {
        if ((request == null)&&(response == null)){
            setHttpServletRequest(req);
            setHttpServletResponse(res);
        }

        final PrintWriter out = response.getWriter();
        out.write("DELETE method (removing data) was invoked!\n");

        //call delete method
        delete(request, response, out);
        out.flush();
        out.close();

        response = null;
        request = null;
    }

    @Override
    public String getServletInfo()
    {
        return "Server-side application REST HTTP methods.";
    }

    public void get(HttpServletRequest request,
                       HttpServletResponse response,
                       PrintWriter out) throws IOException{

        JsonObject jsonDeposit,jsonDepositor;
        JsonArray jsonDeposits = new JsonArray(), jsonDepositors = new JsonArray();

        String str = request.getPathInfo();
        StringTokenizer pathInfo = new StringTokenizer(str);
        int count = 0;
        while (pathInfo.hasMoreTokens()){
            pathInfo.nextToken("/");
            count++;
        }
        String[] path = new String[count];
        pathInfo = new StringTokenizer(str);
        count = 0;
        while (pathInfo.hasMoreTokens()) {
            path[count] = pathInfo.nextToken("/");
            count++;
        }

        if(path.length==1){
            /**
             * @path /deposits
             *
             */
            if(path[0].equalsIgnoreCase("deposits")){
                try{

                    for(Object d: depositService.getBankDeposits()){
                        deposit = (BankDeposit)d;
                        jsonDeposit= new JsonObject();
                            jsonDeposit.addProperty("depositId",deposit.getDepositId());
                            jsonDeposit.addProperty("depositName",deposit.getDepositName());
                            jsonDeposit.addProperty("depositMinTerm",deposit.getDepositMinTerm());
                            jsonDeposit.addProperty("depositMinAmount",deposit.getDepositMinAmount());
                            jsonDeposit.addProperty("depositCurrency",deposit.getDepositCurrency());
                            jsonDeposit.addProperty("depositInterestRate",deposit.getDepositInterestRate());
                            jsonDeposit.addProperty("depositAddConditions",deposit.getDepositAddConditions());

                        jsonDeposits.add(jsonDeposit);
                        deposits.add((BankDeposit)d);
                    }

                    out.print(jsonDeposits);

                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage().toString()+"\n");
                    throw new IOException(e.getMessage());
                }
            }

            /**
             * @path /depositors
             *
             */
            if(path[0].equalsIgnoreCase("depositors")){
                try{

                    for(Object d: depositorService.getBankDepositors()){
                        depositor = (BankDepositor)d;
                        jsonDepositor= new JsonObject();
                            jsonDepositor.addProperty("depositorId",depositor.getDepositorId());
                            jsonDepositor.addProperty("depositorName",depositor.getDepositorName());
                            jsonDepositor.addProperty("depositorDateDeposit",dateFormat.format(depositor.getDepositorDateDeposit()));
                            jsonDepositor.addProperty("depositorAmountDeposit",depositor.getDepositorAmountDeposit());
                            jsonDepositor.addProperty("depositorAmountPlusDeposit",depositor.getDepositorAmountPlusDeposit());
                            jsonDepositor.addProperty("depositorAmountMinusDeposit",depositor.getDepositorAmountMinusDeposit());
                            jsonDepositor.addProperty("depositorDateReturnDeposit",dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                            jsonDepositor.addProperty("depositorMarkReturnDeposit",depositor.getDepositorMarkReturnDeposit());

                        jsonDepositors.add(jsonDepositor);
                        depositors.add((BankDepositor)d);
                    }

                    out.print(jsonDepositors);
                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage().toString()+"\n");
                    throw new IOException(e.getMessage());
                }
            }
        }
        if(path.length==3){
            /**
             * @path /deposit/id/{id}
             *
             */
            if(path[0].equalsIgnoreCase("deposit") & path[1].equalsIgnoreCase("id")){
                try{

                    deposit = depositService.getBankDepositById(Long.parseLong(path[2]));

                    jsonDeposit= new JsonObject();
                        jsonDeposit.addProperty("depositId",deposit.getDepositId());
                        jsonDeposit.addProperty("depositName",deposit.getDepositName());
                        jsonDeposit.addProperty("depositMinTerm",deposit.getDepositMinTerm());
                        jsonDeposit.addProperty("depositMinAmount",deposit.getDepositMinAmount());
                        jsonDeposit.addProperty("depositCurrency",deposit.getDepositCurrency());
                        jsonDeposit.addProperty("depositInterestRate",deposit.getDepositInterestRate());
                        jsonDeposit.addProperty("depositAddConditions",deposit.getDepositAddConditions());

                    out.print(jsonDeposit);
                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage().toString()+"\n");
                    throw new IOException(e.getMessage());
                }
            }

            /**
             * @path /depositor/id/{id}
             *
             */
            if(path[0].equalsIgnoreCase("depositor") & path[1].equalsIgnoreCase("id")){
                try{

                    depositor = depositorService.getBankDepositorById(Long.parseLong(path[2]));

                    jsonDepositor= new JsonObject();
                        jsonDepositor.addProperty("depositorId",depositor.getDepositorId());
                        jsonDepositor.addProperty("depositorName",depositor.getDepositorName());
                        jsonDepositor.addProperty("depositorDateDeposit",dateFormat.format(depositor.getDepositorDateDeposit()));
                        jsonDepositor.addProperty("depositorAmountDeposit",depositor.getDepositorAmountDeposit());
                        jsonDepositor.addProperty("depositorAmountPlusDeposit",depositor.getDepositorAmountPlusDeposit());
                        jsonDepositor.addProperty("depositorAmountMinusDeposit",depositor.getDepositorAmountMinusDeposit());
                        jsonDepositor.addProperty("depositorDateReturnDeposit",dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                    jsonDepositor.addProperty("depositorMarkReturnDeposit", depositor.getDepositorMarkReturnDeposit());

                    out.print(jsonDepositor);

                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage().toString()+"\n");
                    throw new IOException(e.getMessage());
                }
            }

            /**
             * @path /deposit/name/{name}
             *
             */
            if(path[0].equalsIgnoreCase("deposit") & path[1].equalsIgnoreCase("name")){
                try{

                    deposit = depositService.getBankDepositByName(path[2]);

                    jsonDeposit= new JsonObject();
                        jsonDeposit.addProperty("depositId",deposit.getDepositId());
                        jsonDeposit.addProperty("depositName",deposit.getDepositName());
                        jsonDeposit.addProperty("depositMinTerm",deposit.getDepositMinTerm());
                        jsonDeposit.addProperty("depositMinAmount",deposit.getDepositMinAmount());
                        jsonDeposit.addProperty("depositCurrency",deposit.getDepositCurrency());
                        jsonDeposit.addProperty("depositInterestRate",deposit.getDepositInterestRate());
                        jsonDeposit.addProperty("depositAddConditions",deposit.getDepositAddConditions());

                    out.print(jsonDeposit);
                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage().toString()+"\n");
                    throw new IOException(e.getMessage());
                }
            }

            /**
             * @path /depositor/name/{name}
             *
             */
            if(path[0].equalsIgnoreCase("depositor") & path[1].equalsIgnoreCase("name")){
                try{

                    depositor = depositorService.getBankDepositorByName(path[2]);

                    jsonDepositor= new JsonObject();
                        jsonDepositor.addProperty("depositorId",depositor.getDepositorId());
                        jsonDepositor.addProperty("depositorName",depositor.getDepositorName());
                        jsonDepositor.addProperty("depositorDateDeposit",dateFormat.format(depositor.getDepositorDateDeposit()));
                        jsonDepositor.addProperty("depositorAmountDeposit",depositor.getDepositorAmountDeposit());
                        jsonDepositor.addProperty("depositorAmountPlusDeposit",depositor.getDepositorAmountPlusDeposit());
                        jsonDepositor.addProperty("depositorAmountMinusDeposit",depositor.getDepositorAmountMinusDeposit());
                        jsonDepositor.addProperty("depositorDateReturnDeposit",dateFormat.format(depositor.getDepositorDateReturnDeposit()));
                        jsonDepositor.addProperty("depositorMarkReturnDeposit",depositor.getDepositorMarkReturnDeposit());

                    out.print(jsonDepositor);
                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
                    throw new IOException(e.getMessage());
                }
            }
        }
    }

    public void delete(HttpServletRequest request,
                              HttpServletResponse response,
                              PrintWriter out) throws IOException {
        String str = request.getPathInfo();
        StringTokenizer pathInfo = new StringTokenizer(str);
        int count = 0;
        while (pathInfo.hasMoreTokens()){
            pathInfo.nextToken("/");
            count++;
        }
        String[] path = new String[count];
        pathInfo = new StringTokenizer(str);
        count = 0;
        while (pathInfo.hasMoreTokens()) {
            path[count] = pathInfo.nextToken("/");
            count++;
        }

        /**
         * @path /deposit/{id}
         *
         */
        if((path.length==2)&(path[0].equalsIgnoreCase("deposit"))){
            Long depositId = Long.parseLong(path[1]);
            try {

                depositService.deleteBankDeposit(depositId);

                out.write("deposit with " + depositId + " was deleted\n");

            } catch (HibernateException e) {
                LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                response.sendError(404, "Hibernate error - " + e.getMessage() + "\n");
                throw new IOException(e.getMessage());
            }
        }

        /**
         * @path /depositor/{id}
         *
         */
        if((path.length==2)&(path[0].equalsIgnoreCase("depositor"))){
            Long depositorId = Long.parseLong(path[1]);
            try {

                depositorService.removeBankDepositor(depositorId);

                out.write("depositor with " + depositorId + " was deleted\n");

            } catch (HibernateException e) {
                LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                response.sendError(404, "Hibernate error - " + e.getMessage() + "\n");
                throw new IOException(e.getMessage());
            }
        }
    }

    public void insert(HttpServletRequest request,
                       HttpServletResponse response,
                       PrintWriter out) throws IOException{

        //if (request.getContentType().equals("application/json")){
            Gson gson = new Gson();
            String str = request.getPathInfo();
            StringTokenizer pathInfo = new StringTokenizer(str);
            int count = 0;
            while (pathInfo.hasMoreTokens()){
                pathInfo.nextToken("/");
                count++;
            }
            String[] path = new String[count];
            pathInfo = new StringTokenizer(str);
            count = 0;
            while (pathInfo.hasMoreTokens()) {
                path[count] = pathInfo.nextToken("/");
                count++;
            }

            /**
             * @path /deposit
             *
             * {"depositName":"Name1","depositMinTerm":12,"depositMinAmount":1000,
             * "depositCurrency":"usd","depositInterestRate":5,"depositAddConditions":"conditions"}
             */
            if((path.length==1)&(path[0].equalsIgnoreCase("deposit"))){
                try {
                    StringBuilder sb = new StringBuilder();
                    String s;
                    while ((s = request.getReader().readLine()) != null) {
                        sb.append(s);
                    }

                    deposit = gson.fromJson(sb.toString(), BankDeposit.class);

                    depositService.addBankDeposit(deposit);

                    String jsonDeposit = gson.toJson(deposit);
                    out.write(jsonDeposit);

                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
                    throw new IOException(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    out.flush();
                    out.close();
                }
            }
            /**
             * @path /depositor/{depositId}
             *
             * {"depositorName":"depositorName2","depositorDateDeposit":"Jan 1, 2014 12:00:00 AM",
             * "depositorAmountDeposit":1000,"depositorAmountPlusDeposit":100,"depositorAmountMinusDeposit":10,
             * "depositorDateReturnDeposit":"Mar 1, 2015 12:00:00 AM","depositorMarkReturnDeposit":0}
             */
            if((path.length==2)&(path[0].equalsIgnoreCase("depositor"))){
                try {
                    Long depositId = Long.parseLong(path[1]);
                    StringBuilder sb = new StringBuilder();
                    String s;
                    while ((s = request.getReader().readLine()) != null) {
                        sb.append(s);
                    }

                    depositor = gson.fromJson(sb.toString(), BankDepositor.class);
                    depositor.setDepositId(depositId);

                    depositorService.addBankDepositor(depositId,depositor);

                    String jsonDepositor = gson.toJson(depositor);
                    out.write(jsonDepositor);

                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage().toString());
                    throw new IOException(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    out.flush();
                    out.close();
                }
            }
        //} else {
        //    response.setStatus(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
            out.flush();
            out.close();
        //}
    }

    public void update(HttpServletRequest request,
                       HttpServletResponse response,
                       PrintWriter out) throws IOException {

        //if (request.getContentType().equals("application/json")){
            Gson gson = new Gson();
            String str = request.getPathInfo();
            StringTokenizer pathInfo = new StringTokenizer(str);
            int count = 0;
            while (pathInfo.hasMoreTokens()){
                pathInfo.nextToken("/");
                count++;
            }
            String[] path = new String[count];
            pathInfo = new StringTokenizer(str);
            count = 0;
            while (pathInfo.hasMoreTokens()) {
                path[count] = pathInfo.nextToken("/");
                count++;
            }

            /**
             * @path /deposit
             *
             * {"depositId":1,"depositName":"updateName1","depositMinTerm":10,"depositMinAmount":1000,
             * "depositCurrency":"usd","depositInterestRate":5,"depositAddConditions":"conditions"}
             */
            if((path.length==1)&(path[0].equalsIgnoreCase("deposit"))){
                try {
                    StringBuilder sb = new StringBuilder();
                    String s;
                    while ((s = request.getReader().readLine()) != null) {
                        sb.append(s);
                    }

                    deposit = gson.fromJson(sb.toString(), BankDeposit.class);

                    depositService.updateBankDeposit(deposit);

                    String jsonDeposit = gson.toJson(deposit);
                    out.write(jsonDeposit);

                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage()+"\n");
                    throw new IOException(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    out.flush();
                    out.close();
                }
            }
            /**
             * @path /depositor/{depositId}
             *
             * {"depositorId":1,"depositorName":"updateDepositorName2","depositorDateDeposit":"Jan 1, 2014 12:00:00 AM",
             * "depositorAmountDeposit":1000,"depositorAmountPlusDeposit":100,"depositorAmountMinusDeposit":10,
             * "depositorDateReturnDeposit":"Mar 1, 2015 12:00:00 AM","depositorMarkReturnDeposit":0}
             */
            if((path.length==2)&(path[0].equalsIgnoreCase("depositor"))){
                try {
                    Long depositId = Long.parseLong(path[1]);
                    StringBuilder sb = new StringBuilder();
                    String s;
                    while ((s = request.getReader().readLine()) != null) {
                        sb.append(s);
                    }

                    depositor = gson.fromJson(sb.toString(), BankDepositor.class);

                    depositorService.updateBankDepositor(depositor);

                    String jsonDepositor = gson.toJson(depositor);
                    out.write(jsonDepositor);

                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    response.sendError(404,"Hibernate error - "+e.getMessage().toString());
                    throw new IOException(e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    out.flush();
                    out.close();
                }
            }
        //} else {
        //    response.setStatus(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);
        //    out.flush();
        //    out.close();
        //}
    }
}
