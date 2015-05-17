package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alexander on 11.4.15.
 */
public class InputDepositorTest extends HttpServlet {

    SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        try {
            // Начало единицы работы
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            // Запрос и создание страницы...
            PrintWriter out = response.getWriter();
            out.println("<html><head><title>Depositor Manager</title></head><body>");

            // Handle actions
            if ( "store".equals(request.getParameter("action")) ) {

                String depositId = request.getParameter("depositId");
                String depositorName = request.getParameter("depositorName");
                String depositorDateDeposit = request.getParameter("depositorDateDeposit");
                String depositorAmountDeposit = request.getParameter("depositorAmountDeposit");
                String depositorAmountPlusDeposit = request.getParameter("depositorAmountPlusDeposit");
                String depositorAmountMinusDeposit = request.getParameter("depositorAmountMinusDeposit");
                String depositorDateReturnDeposit = request.getParameter("depositorDateReturnDeposit");
                String depositorMarkReturnDeposit = request.getParameter("depositorMarkReturnDeposit");

                if ( "".equals(depositId) ||
                        "".equals(depositorName) ||
                        "".equals(depositorDateDeposit) ||
                        "".equals(depositorAmountDeposit)) {
                    out.println("<b><i>Please enter parameters of the deposit.</i></b>");
                }
                else {
                    createAndStoreDepositor(out, depositId,depositorName,depositorDateDeposit,depositorAmountDeposit,
                            depositorAmountPlusDeposit,depositorAmountMinusDeposit,depositorDateReturnDeposit,depositorMarkReturnDeposit);
                    out.println("<b><i>Added depositor.</i></b>");
                }
            }

            // Print page
            printDepositorForm(out);
            listDepositors(out);

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

    private void printDepositorForm(PrintWriter out) {
        out.println("<h2>Add new depositor:</h2>");
        out.println("<form>");
        out.println("id deposit: <input name='depositId' length='50'/><br/>");
        out.println("Name: <input name='depositorName' length='50'/><br/>");
        out.println("Date: <input name='depositorDateDeposit' length='50'/><br/>");
        out.println("Amount: <input name='depositorAmountDeposit' length='50'/><br/>");
        out.println("Plus: <input name='depositorAmountPlusDeposit' length='50'/><br/>");
        out.println("Minus: <input name='depositorAmountMinusDeposit' length='50'/><br/>");
        out.println("Date return: <input name='depositorDateReturnDeposit' length='50'/><br/>");
        out.println("Mark: <input name='depositorMarkReturnDeposit' length='50'/><br/>");

        out.println("<input type='submit' name='action' value='store'/>");
        out.println("</form>");
    }

    private void listDepositors(PrintWriter out) {
        //--- получение списка событий из БД
        List result = HibernateUtil.getSessionFactory()
                .getCurrentSession().createCriteria(BankDepositor.class).list();
        //--- вывод полученного списка в HTML таблицу
        if (result.size() > 0) {
            out.println("<h2>depositors in database:</h2>");
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("<th>Name</th>");
            out.println("<th>Date</th>");
            out.println("<th>Amount</th>");
            out.println("<th>Plus</th>");
            out.println("<th>Minus</th>");
            out.println("<th>Date return</th>");
            out.println("<th>Mark</th>");
            out.println("</tr>");
            Iterator it = result.iterator();
            while (it.hasNext()) {
                BankDepositor depositor = (BankDepositor) it.next();
                out.println("<tr>");
                out.println("<td>" + depositor.getDepositorName() + "</td>");
                out.println("<td>" + dateFormatter.format(depositor.getDepositorDateDeposit()) + "</td>");
                out.println("<td>" + depositor.getDepositorAmountDeposit() + "</td>");
                out.println("<td>" + depositor.getDepositorAmountPlusDeposit() + "</td>");
                out.println("<td>" + depositor.getDepositorAmountMinusDeposit() + "</td>");
                out.println("<td>" + dateFormatter.format(depositor.getDepositorDateReturnDeposit()) + "</td>");
                out.println("<td>" + depositor.getDepositorMarkReturnDeposit() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
    }

    protected void createAndStoreDepositor(PrintWriter out,String id, String name, String dateDeposit,
                                           String amount, String amountPlus, String amountMinus,
                                           String dateReturn,String mark) throws ParseException{
        BankDepositor theDepositor = new BankDepositor();
            theDepositor.setDepositorName(name);
            theDepositor.setDepositorDateDeposit(dateFormatter.parse(dateDeposit));
            theDepositor.setDepositorAmountDeposit(Integer.parseInt(amount));
            theDepositor.setDepositorAmountPlusDeposit(Integer.parseInt(amountPlus));
            theDepositor.setDepositorAmountMinusDeposit(Integer.parseInt(amountMinus));
            theDepositor.setDepositorDateReturnDeposit(dateFormatter.parse(dateReturn));
            theDepositor.setDepositorMarkReturnDeposit(Integer.parseInt(mark));

        HibernateUtil.getSessionFactory()
                .getCurrentSession().save(theDepositor);

        //BankDeposit theDeposit = (BankDeposit)HibernateUtil.getSessionFactory()
        //        .getCurrentSession().createCriteria(BankDeposit.class)
        //        .add(Restrictions.eq("depositId", Long.parseLong(id))).uniqueResult();

        BankDeposit theDeposit = (BankDeposit)HibernateUtil.getSessionFactory()
                .getCurrentSession().createQuery("select p from BankDeposit p left join fetch p.depositors where p.depositId = :pid")
                .setParameter("pid", Long.parseLong(id))
                .uniqueResult();

        out.println(theDeposit.toString());

        theDeposit.getDepositors().add(theDepositor);

        out.println(theDeposit.getDepositors());
    }
}