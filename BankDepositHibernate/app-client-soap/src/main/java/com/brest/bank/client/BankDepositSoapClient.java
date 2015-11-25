package com.brest.bank.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class BankDepositSoapClient extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Servlet method responding to HTTP GET methods calls.
     * generate WSDL
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {
        try{
            StringWriter wr = GenerateWSDL.dumpWSDL(GenerateWSDL.createWSDL());
            PrintWriter outputStream = response.getWriter();
            outputStream.write(wr.toString());

            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            LOGGER.error("IOException - {},/n{}", e.getMessage(), e.getStackTrace());
            response.sendError(404,"IOException - "+e.getMessage()+"\n");
        }
    }

    /**
     * Servlet method responding to HTTP PUT methods calls.
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    public void doPut( HttpServletRequest request,
                       HttpServletResponse response ) throws IOException
    {
        final PrintWriter out = response.getWriter();
        out.write("PUT method (updating data) wasn't invoked!\n");
        out.flush();
        out.close();
    }

    /**
     * Servlet method responding to HTTP POST methods calls.
     * result Soap service
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    public void doPost( HttpServletRequest request,
                        HttpServletResponse response ) throws IOException
    {
        response.setContentType("text/xml; charset=UTF-8");
        final PrintWriter out = response.getWriter();

        //call SOAP methods
        post(request, response, out);
        out.flush();
        out.close();
    }

    /**
     * Servlet method responding to HTTP DELETE methods calls.
     *
     * @param request HTTP request.
     * @param response HTTP response.
     */
    @Override
    public void doDelete( HttpServletRequest request,
                          HttpServletResponse response ) throws IOException
    {

        final PrintWriter out = response.getWriter();
        out.write("DELETE method (removing data) wasn't invoked!\n");
        out.flush();
        out.close();
    }

    @Override
    public String getServletInfo()
    {
        return "Client-side application SOAP HTTP methods.";
    }
    

    public void post(HttpServletRequest request,
                     HttpServletResponse response,
                     PrintWriter out) throws IOException{

        String url = request.getRequestURL().toString();

        String cl = "client/";
        url = url.replaceAll(cl, "server/");

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
            sb.append(s);
        }

        StringEntity entity = new StringEntity(sb.toString());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost(url);

        postRequest.addHeader("SOAPAction", request.getHeader("SOAPAction"));
        postRequest.addHeader("Content-Type", request.getHeader("Content-Type"));

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
        }finally {
            out.flush();
            out.close();
        }

    }
}