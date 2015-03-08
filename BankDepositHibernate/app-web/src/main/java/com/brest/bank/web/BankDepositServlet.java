package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class BankDepositServlet extends HttpServlet {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public BankDepositService depositService = new BankDepositServiceImpl();
    private PrintWriter out;
    private List<BankDeposit> deposits = null;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException{
        try{
            out = response.getWriter();
            //query & build view
            out.println("<html><head><title>Bank Deposit Manager</title></head><body>");
            //handle actions
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
                }
            }
            //Print page
            printDepositForm(out);
            listDeposit(out);
            // Write HTML footer
            out.println("</body></html>");
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

    private void printDepositForm(PrintWriter out){
        out.println("<h2>Add new Deposit:</h2>");
        out.println("<form>");
        out.println("Deposit Name: <input name ='depositName' length='50'/><br/>");
        out.println("Minimum term of deposit: <input name='depositMinTerm' length='3'/><br/>");
        out.println("Minimum sum of amount: <input name='depositMinAmount' length='10'/><br/>");
        out.println("Currency of deposit: <input name='depositCurrency' length='3'/><br/>");
        out.println("Interest rate of deposit: <input name='depositInterestRate' length='3'/><br/>");
        out.println("Conditions: <input name='depositAddConditions' length='50'/><br/>");
        out.println("<input type='submit' name='action' value='store'/>");
        out.println("</form>");
    }

    private void listDeposit(PrintWriter out){
        //--- получение списка событий из БД
        deposits = depositService.getBankDeposits();
        //--- вывод полученного списка в HTML таблицу
        if (deposits.size() > 0) {
            out.println("<h2>Deposits in database:</h2>");
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("<th>Deposit Name</th>");
            out.println("<th>Minimum term of deposit</th>");
            out.println("<th>Minimum sum of amount</th>");
            out.println("<th>Currency of deposit</th>");
            out.println("<th>Interest rate of deposit</th>");
            out.println("<th>Conditions</th>");
            out.println("</tr>");

            for(BankDeposit dep:deposits) {
                out.println("<tr>");
                out.println("<td>" + dep.getDepositName() + "</td>");
                out.println("<td>" + dep.getDepositMinTerm() + "</td>");
                out.println("<td>" + dep.getDepositMinAmount() + "</td>");
                out.println("<td>" + dep.getDepositCurrency() + "</td>");
                out.println("<td>" + dep.getDepositInterestRate() + "</td>");
                out.println("<td>" + dep.getDepositAddConditions() + "</td>");
                out.println("</tr>");
            }
            deposits = null;
            out.println("</table>");
        } else{
            out.println("<h2>Deposits in database:"+deposits.size()+"</h2>");
        }
    }

    protected void createAndStoreDeposit(BankDeposit deposit){
        depositService.addBankDeposit(deposit);
    }
}
