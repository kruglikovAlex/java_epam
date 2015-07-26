package com.brest.bank.client;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RestClient extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private List<BankDeposit> deposits = new ArrayList<BankDeposit>();
    private List<BankDepositor> depositors = new ArrayList<BankDepositor>();
    private BankDeposit deposit;
    private BankDepositor depositor;

    public static String USER_AGENT = "";

    /**
     * Servlet method responding to HTTP GET methods calls.
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    public void doGet( HttpServletRequest request,
                       HttpServletResponse response ) throws IOException
    {
        response.setContentType("application/json");
        final PrintWriter out = response.getWriter();

        //call get method
        get(request, response, out);
        out.flush();
        out.close();
    }

    /**
     * Servlet method responding to HTTP PUT methods calls.
     * update BankDeposit
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    public void doPut( HttpServletRequest request,
                       HttpServletResponse response ) throws IOException
    {
        final PrintWriter out = response.getWriter();

        //call update method
        update(request,response,out);
        out.flush();
        out.close();
    }

    /**
     * Servlet method responding to HTTP POST methods calls.
     * insert BankDeposit
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    public void doPost( HttpServletRequest request,
                        HttpServletResponse response ) throws IOException
    {
        response.setContentType("application/json");
        final PrintWriter out = response.getWriter();

        //call insert method
        insert(request,response,out);
        out.flush();
        out.close();
    }

    /**
     * Servlet method responding to HTTP DELETE methods calls.
     * delete BankDeposit
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    public void doDelete( HttpServletRequest request,
                          HttpServletResponse response ) throws IOException
    {

        final PrintWriter out = response.getWriter();
        out.write("DELETE method (removing data) was invoked!\n");

        //call delete method
        delete(request, response, out);
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

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet(url);

        try{
            HttpResponse getResponce = httpClient.execute(getRequest);
            HttpEntity getEntity = getResponce.getEntity();
            String getResponceString = EntityUtils.toString(getEntity,"UTF-8");
            out.write(getResponceString);

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

        HttpEntity entity=new StringEntity(sb.toString());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(url);

        postRequest.setEntity(entity);

        try{
            HttpResponse postResponce = httpClient.execute(postRequest);
            HttpEntity postEntity = postResponce.getEntity();
            String postResponceString = EntityUtils.toString(postEntity,"UTF-8");
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

        HttpEntity entity=new StringEntity(sb.toString());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut putRequest = new HttpPut(url);

        putRequest.setEntity(entity);

        try{
            HttpResponse putResponce = httpClient.execute(putRequest);
            HttpEntity putEntity = putResponce.getEntity();
            String putResponceString = EntityUtils.toString(putEntity,"UTF-8");
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

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpDelete deleteRequest = new HttpDelete(url);

        try{
            HttpResponse deleteResponce = httpClient.execute(deleteRequest);
            HttpEntity deleteEntity = deleteResponce.getEntity();
            String deleteResponceString = EntityUtils.toString(deleteEntity,"UTF-8");
            out.write(deleteResponceString);

        }catch (ClientProtocolException e){
            LOGGER.error("Client Protocol DELETE -> error - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404, "Client Protocol error - " + e.getMessage() + "\n");
            throw new IOException(e.getMessage());
        }
    }
}