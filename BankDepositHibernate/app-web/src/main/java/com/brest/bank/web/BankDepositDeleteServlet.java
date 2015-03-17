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
import java.text.SimpleDateFormat;
import java.util.List;

public class BankDepositDeleteServlet extends HttpServlet {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public BankDepositService depositService = new BankDepositServiceImpl();
    private PrintWriter out;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException{
        try{
            response.setContentType("text/html");
            out = response.getWriter();
            //query & build view
            out.println("<html><head><title>Bank Deposit Manager</title></head><body>");
            //handle actions
            if("store".equals(request.getParameter("action"))){
                String depositName =  request.getParameter("depositName");
                if("".equals(depositName)){
                    out.println("<b><i>Please enter Deposit name.</i></b>");
                } else{
                    if (findDeposit(depositName)!=null){
                        deleteDeposit(findDeposit(depositName));
                        out.println("<b><i>Bank Deposit deleted.</i></b>");
                    } else {
                        out.println("<b><i>Don't deleted Bank Deposit.</i></b>");
                    }
                }
            }
            //Print page
            printDepositForm(out);
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
        out.println("<h2>Delete Deposit:</h2>");
        out.println("<form>");
        out.println("Deposit Name: <input name ='depositName' length='50'/><br/>");
        out.println("<input type='submit' name='action' value='store'/>");
        out.println("</form>");
    }

    protected void deleteDeposit(Long id){
        depositService.removeBankDeposit(id);
    }

    protected Long findDeposit(String name){
        return depositService.getBankDepositByName(name).getDepositId();
    }
}
