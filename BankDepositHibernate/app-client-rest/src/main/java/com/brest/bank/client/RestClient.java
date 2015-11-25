package com.brest.bank.client;

import java.io.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestClient extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger();

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
        LOGGER.info("run method doGet: request-{}, response-{}",req,res);

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        //call get method
        get(req, res, out);

        out.flush();
        out.close();
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
        LOGGER.info("run method doPut: request-{}, response-{}",req,res);

        res.setContentType("application/json");
        LOGGER.info("Content Type-{}",res.getContentType());
        PrintWriter out = res.getWriter();
        //call update method
        update(req,res,out);

        out.flush();
        out.close();
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
        LOGGER.info("run method doPost: request-{}, response-{}",req,res);

        res.setContentType("application/json");
        LOGGER.info("Content Type-{}",res.getContentType());
        PrintWriter out = res.getWriter();
        //call insert method
        insert(req,res,out);

        out.flush();
        out.close();
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
        LOGGER.info("run method doPost: request-{}, response-{}",req,res);

        PrintWriter out = res.getWriter();
        out.write("DELETE method (removing data) was invoked!\n");
        //call delete method
        delete(req, res, out);

        out.flush();
        out.close();
    }

    @Override
    public String getServletInfo()
    {
        return "Client-side application REST HTTP methods.";
    }


    public void get(HttpServletRequest request,
                    HttpServletResponse response,
                    PrintWriter out) throws IOException{

        String url = request.getRequestURL().toString();

        String cl = "client/";
        url = url.replaceAll(cl, "");

        HttpGet getRequest = new HttpGet(url);

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse getResponse = client.execute(getRequest);
            HttpEntity entity = getResponse.getEntity();

            String getResponseString = EntityUtils.toString(entity,"UTF-8");
            out.write(getResponseString);
        }catch (ClientProtocolException e){
            LOGGER.error("Client Protocol GET -> error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404, "Client Protocol error - " + e.getMessage() + "\n");
            throw new IOException(e.getMessage());
        }
    }

    public void insert(HttpServletRequest request,
                       HttpServletResponse response,
                       PrintWriter out) throws IOException{

        String url = request.getRequestURL().toString();

        String cl = "client/";
        url = url.replaceAll(cl, "");

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }

        HttpEntity entityRequest=new StringEntity(sb.toString());
        HttpPost postRequest = new HttpPost(url);
        postRequest.setEntity(entityRequest);

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse postResponse = client.execute(postRequest);
            HttpEntity entity = postResponse.getEntity();

            String postResponceString = EntityUtils.toString(entity,"UTF-8");
            out.write(postResponceString);
        }catch (ClientProtocolException e){
            LOGGER.error("Client Protocol POST -> error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404, "Client Protocol error - " + e.getMessage() + "\n");
            throw new IOException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request,
                       HttpServletResponse response,
                       PrintWriter out) throws IOException {

        String url = request.getRequestURL().toString();

        String cl = "client/";
        url = url.replaceAll(cl, "");

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }

        HttpEntity entityRequest=new StringEntity(sb.toString());
        HttpPut putRequest = new HttpPut(url);
        putRequest.setEntity(entityRequest);

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse putResponse = client.execute(putRequest);
            HttpEntity entity = putResponse.getEntity();

            String putResponceString = EntityUtils.toString(entity,"UTF-8");
            out.write(putResponceString);
        }catch (ClientProtocolException e){
            LOGGER.error("Client Protocol PUT -> error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404, "Client Protocol error - " + e.getMessage() + "\n");
            throw new IOException(e.getMessage());
        }
    }

    public void delete(HttpServletRequest request,
                       HttpServletResponse response,
                       PrintWriter out) throws IOException {

        String url = request.getRequestURL().toString();

        String cl = "client/";
        url = url.replaceAll(cl, "");

        HttpDelete deleteRequest = new HttpDelete(url);

        try{
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse deleteResponse = client.execute(deleteRequest);
            HttpEntity entity = deleteResponse.getEntity();

            String deleteResponceString = EntityUtils.toString(entity,"UTF-8");
            out.write(deleteResponceString);
        }catch (ClientProtocolException e){
            LOGGER.error("Client Protocol DELETE -> error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404, "Client Protocol error - " + e.getMessage() + "\n");
            throw new IOException(e.getMessage());
        }
    }
}