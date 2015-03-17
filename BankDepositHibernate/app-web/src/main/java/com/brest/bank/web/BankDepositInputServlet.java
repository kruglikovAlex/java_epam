package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BankDepositInputServlet extends HttpServlet {

    public BankDepositService depositService = new BankDepositServiceImpl();
    private PrintWriter out;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException{

            getServletContext().getRequestDispatcher("/WEB-INF/jsp/inputForm.jsp").forward(request,response);
    }

    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException{
        try{
            response.setContentType("text/html");
            out = response.getWriter();
            out.println("<html><head><title>Bank Deposit Manager</title></head><body>");
            if("store".equals(request.getParameter("action"))){
                String depositName =  request.getParameter("depositName");
                String depositMinTerm =  request.getParameter("depositMinTerm");
                String depositMinAmount =  request.getParameter("depositMinAmount");
                String depositCurrency =  request.getParameter("depositCurrency");
                String depositInterestRate =  request.getParameter("depositInterestRate");
                String depositAddConditions =  request.getParameter("depositAddConditions");
                if("".equals(depositName)||"".equals(depositMinTerm)||"".equals(depositMinAmount)
                        ||"".equals(depositCurrency)||"".equals(depositInterestRate)||"".equals(depositAddConditions)){
                    out.println("<b><i>Please enter Deposit name.</i></b>");
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/inputForm.jsp").forward(request,response);
                } else{
                    BankDeposit deposit = new BankDeposit();
                    deposit.setDepositName(depositName);
                    deposit.setDepositMinTerm(Integer.parseInt(depositMinTerm));
                    deposit.setDepositMinAmount(Integer.parseInt(depositMinAmount));
                    deposit.setDepositCurrency(depositCurrency);
                    deposit.setDepositInterestRate(Integer.parseInt(depositInterestRate));
                    deposit.setDepositAddConditions(depositAddConditions);
                    createAndStoreDeposit(deposit);
                    out.println("<b><i>Added Bank Deposit.</i></b>");
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/depositList.jsp").forward(request, response);
                }
            } else {
                out.println("<b><i>"+request.getParameter("action")+"</i></b>");
                getServletContext().getRequestDispatcher("/WEB-INF/jsp/inputForm.jsp").forward(request,response);
            }
            out.flush();
            out.close();
        }catch (Exception e){
            if (HibernateUtil.getSessionFactory().getCurrentSession()!=null){
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            }
            if(ServletException.class.isInstance(e)){
                throw (ServletException) e;
            } else {
                throw new ServletException(e);
            }
        }finally {
            out.close();
        }
    }

    protected void createAndStoreDeposit(BankDeposit deposit){
        depositService.addBankDeposit(deposit);
    }
}
