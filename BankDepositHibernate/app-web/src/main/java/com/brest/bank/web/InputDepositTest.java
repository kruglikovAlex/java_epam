package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
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

/**
 * Created by alexander on 11.4.15.
 */
public class InputDepositTest extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        SimpleDateFormat dateFormatter = new SimpleDateFormat( "dd.MM.yyyy" );

        try {
            // Начало единицы работы
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            // Запрос и создание страницы...
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>Deposit Manager</title></head><body>");

            // Handle actions
            if ( "store".equals(request.getParameter("action")) ) {

                String depositName = request.getParameter("depositName");
                String depositMinTerm = request.getParameter("depositMinTerm");
                String depositMinAmount = request.getParameter("depositMinAmount");
                String depositCurrency = request.getParameter("depositCurrency");
                String depositInterestRate = request.getParameter("depositInterestRate");
                String depositAddConditions = request.getParameter("depositAddConditions");

                if ( "".equals(depositName) ||
                        "".equals(depositMinTerm) ||
                        "".equals(depositMinAmount)) {
                    out.println("<b><i>Please enter parameters of the deposit.</i></b>");
                }
                else {
                    createAndStoreDeposit(depositName,depositMinTerm,depositMinAmount,
                            depositCurrency,depositInterestRate,depositAddConditions);
                    out.println("<b><i>Added deposit.</i></b>");
                }
            }

            // Print page
            printDepositForm(out);
            listDeposits(out);

            // Write HTML footer
            out.println("</body></html>");
            out.flush();
            out.close();

            // Окончание единицы работы
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        }
        catch (Exception ex) {
            // Неудача — откат назад
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            if ( ServletException.class.isInstance( ex ) ) {
                throw ( ServletException ) ex;
            }
            else {
                throw new ServletException( ex );
            }
        }
    }

    private void printDepositForm(PrintWriter out) {
        out.println("<h2>Add new deposit:</h2>");
        out.println("<form>");
        out.println("Name: <input name='depositName' length='50'/><br/>");
        out.println("Term: <input name='depositMinTerm' length='50'/><br/>");
        out.println("Amount: <input name='depositMinAmount' length='50'/><br/>");
        out.println("Currency: <input name='depositCurrency' length='50'/><br/>");
        out.println("Rate: <input name='depositInterestRate' length='50'/><br/>");
        out.println("Conditions: <input name='depositAddConditions' length='50'/><br/>");
        out.println("<input type='submit' name='action' value='store'/>");
        out.println("</form>");
    }

    private void listDeposits(PrintWriter out) {
        //--- получение списка событий из БД
        List result = HibernateUtil.getSessionFactory()
                .getCurrentSession().createCriteria(BankDeposit.class).list();
        //--- вывод полученного списка в HTML таблицу
        if (result.size() > 0) {
            out.println("<h2>deposits in database:</h2>");
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("<th>deposit id</th>");
            out.println("<th>name</th>");
            out.println("<th>Term</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Currency</th>");
            out.println("<th>Interest rate</th>");
            out.println("<th>Conditions</th>");
            out.println("</tr>");
            Iterator it = result.iterator();
            while (it.hasNext()) {
                BankDeposit deposit = (BankDeposit) it.next();
                out.println("<tr>");
                out.println("<td>" + deposit.getDepositId() + "</td>");
                out.println("<td>" + deposit.getDepositName() + "</td>");
                out.println("<td>" + deposit.getDepositMinTerm() + "</td>");
                out.println("<td>" + deposit.getDepositMinAmount() + "</td>");
                out.println("<td>" + deposit.getDepositCurrency() + "</td>");
                out.println("<td>" + deposit.getDepositInterestRate() + "</td>");
                out.println("<td>" + deposit.getDepositAddConditions() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
    }

    protected void createAndStoreDeposit(String name, String term, String amount, String currency, String rate, String addCond) {
        BankDeposit theDeposit = new BankDeposit();
            theDeposit.setDepositName(name);
            theDeposit.setDepositMinTerm(Integer.parseInt(term));
            theDeposit.setDepositMinAmount(Integer.parseInt(amount));
            theDeposit.setDepositCurrency(currency);
            theDeposit.setDepositInterestRate(Integer.parseInt(rate));
            theDeposit.setDepositAddConditions(addCond);

        HibernateUtil.getSessionFactory()
                .getCurrentSession().save(theDeposit);
    }
}