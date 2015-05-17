package com.brest.bank.web;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositDaoImpl;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;

import com.brest.bank.service.BankDepositorService;
import com.brest.bank.service.BankDepositorServiceImpl;
import com.brest.bank.util.HibernateUtil;
import com.brest.bank.web.forms.MainFrameForm;
import com.brest.bank.web.forms.DepositForm;
import com.brest.bank.web.forms.DepositorForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

public class MainFrameServlet extends HttpServlet {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    private BankDepositService depositService = new BankDepositServiceImpl();
    private BankDepositorService depositorService = new BankDepositorServiceImpl();
    BankDepositDao depositDao = new BankDepositDaoImpl();

    private Collection deposits;
    private Collection depositors;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        int answer;
        try {
            answer = checkAction(request);
        } catch (ServletException e) {
            LOGGER.error("Get parameters for 'answer' error - {}", e.toString());
            throw new IOException(e.getMessage());
        }
        if(answer == 1){
            /**
             * insert deposit
             * forward to depositFrame.jsp
             */
            try{
                BankDeposit deposit = new BankDeposit();
                    deposit.setDepositMinTerm(0);
                    deposit.setDepositMinAmount(0);
                    deposit.setDepositInterestRate(0);

                DepositForm depositForm = new DepositForm();
                depositForm.initFormDeposit(deposit);

                request.setAttribute("deposit",depositForm);
                getServletContext().getRequestDispatcher("/WEB-INF/jsp/depositFrame.jsp").forward(request,response);
                return;
            }catch (ServletException e){
                LOGGER.error("Servlet error - {}", e.toString());
                throw new IOException(e.getMessage());
            }
        }
        if(answer == 2){
            /**
             * update deposit
             * forward to depositFrame.jsp
             */
            try{
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                BankDeposit deposit = (BankDeposit)HibernateUtil.getSessionFactory().getCurrentSession()
                        .createCriteria(BankDeposit.class)
                        .add(Restrictions.eq("depositId", Long.parseLong(request.getParameter("depositId"))))
                        .uniqueResult();

                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

                DepositForm depositForm = new DepositForm();
                depositForm.initFormDeposit(deposit);

                request.setAttribute("deposit",depositForm);
                getServletContext().getRequestDispatcher("/WEB-INF/jsp/depositFrame.jsp").forward(request,response);
                return;
            }catch (ServletException e){
                LOGGER.error("Servlet error - {}", e.toString());
                throw new IOException(e.getMessage());
            }
        }
        if (answer == 3) {
            /**
             * insert depositor
             * forward to depositorFrame.jsp
             */
            if(request.getParameter("depositId")!=null) {
                try {
                    Long id = Long.parseLong(request.getParameter("depositId"));
                    BankDepositor depositor = new BankDepositor();
                        depositor.setDepositorDateDeposit(dateFormat.parse("2014-01-01"));
                        depositor.setDepositorDateReturnDeposit(dateFormat.parse("2014-02-02"));
                        depositor.setDepositorAmountDeposit(0);
                        depositor.setDepositorAmountPlusDeposit(0);
                        depositor.setDepositorAmountMinusDeposit(0);
                        depositor.setDepositorMarkReturnDeposit(0);

                    DepositorForm depositorForm = new DepositorForm();
                    depositorForm.initFormDepositor(depositor);
                    depositorForm.setDepositId(id);

                    request.setAttribute("depositor", depositorForm);
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/depositorFrame.jsp").forward(request, response);
                    return;
                } catch (ParseException e) {
                    LOGGER.error("Parse error - {},/n{}", e.getMessage(), e.getStackTrace());
                    throw new IOException(e.getMessage());
                }catch (ServletException e){
                    LOGGER.error("Servlet error - {}", e.toString());
                    throw new IOException(e.getMessage());
                }
            }
        }
        if (answer == 4) {
            /**
             * update depositor
             * forward to depositorFrame.jsp
             */
            if (request.getParameter("depositId") != null) {
                try {
                    HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                    Long id = Long.parseLong(request.getParameter("depositId"));
                    BankDepositor depositor = (BankDepositor)HibernateUtil.getSessionFactory().getCurrentSession()
                            .createCriteria(BankDepositor.class)
                            .add(Restrictions.eq("depositorId", Long.parseLong(request.getParameter("depositorId"))))
                            .uniqueResult();

                    HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

                    DepositorForm depForm = new DepositorForm();
                    depForm.initFormDepositor(depositor);
                    depForm.setDepositId(id);

                    request.setAttribute("depositor", depForm);
                    getServletContext().getRequestDispatcher("/WEB-INF/jsp/depositorFrame.jsp").forward(request, response);
                    return;
                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    throw new IOException(e.getMessage());
                }
            }
        }
        if (answer == 5) {
            /**
             * remove depositor
             */
            if (request.getParameter("depositId") != null) {
                try {
                    HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                    BankDepositor depositor = (BankDepositor)HibernateUtil.getSessionFactory().getCurrentSession()
                            .createCriteria(BankDepositor.class)
                            .add(Restrictions.eq("depositorId", Long.parseLong(request.getParameter("depositorId"))))
                            .uniqueResult();

                    BankDeposit theDeposit = (BankDeposit)HibernateUtil.getSessionFactory().getCurrentSession()
                            .createCriteria(BankDeposit.class)
                            .add(Restrictions.eq("depositId", Long.parseLong(request.getParameter("depositId"))))
                            .uniqueResult();

                    HibernateUtil.getSessionFactory().getCurrentSession().update(theDeposit);
                    HibernateUtil.getSessionFactory().getCurrentSession().delete(depositor);

                    HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

                } catch (HibernateException e) {
                    LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                    throw new IOException(e.getMessage());
                }
            }
        }
        if(answer == 6){
            /**
             * remove deposit
             */
            try{
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                BankDeposit deposit = (BankDeposit)HibernateUtil.getSessionFactory().getCurrentSession()
                        .createCriteria(BankDeposit.class)
                        .add(Restrictions.eq("depositId", Long.parseLong(request.getParameter("depositId"))))
                        .uniqueResult();

                HibernateUtil.getSessionFactory().getCurrentSession()
                            .delete(deposit);

                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

            }catch (HibernateException e){
                LOGGER.error("Hibernate error - {}", e.toString());
                throw new IOException(e.getMessage());
            }
        }
        /**
        * init main form
        * forward to mainFrame.jsp
        */
        Long depositId = -1L;
        String reqId = request.getParameter("depositId");
        if (reqId!= null){
            depositId = Long.parseLong(reqId);
        }

        MainFrameForm mainForm = new MainFrameForm();
        try {
            BankDeposit deposit;
            deposits = new ArrayList<BankDeposit>();
            depositors  = new ArrayList<BankDepositor>();

            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDeposit.class).list()){
                deposits.add((BankDeposit)d);
            }

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

            if (deposits.size() == 0){
                deposit = new BankDeposit(1L," ",0,0," ",0," ",null);
                deposits.add(deposit);
            } else{
                deposit = new BankDeposit();
                Iterator i = deposits.iterator();
                deposit = (BankDeposit) i.next();
            }

            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            for(Object d: HibernateUtil.getSessionFactory().getCurrentSession()
                    .createCriteria(BankDepositor.class).list()){
                depositors.add((BankDepositor)d);
            }

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

            mainForm.setDepositId(deposit.getDepositId());
            mainForm.setDeposits(deposits);
            mainForm.setDepositors(depositors);

        } catch (HibernateException e) {
            LOGGER.error("Hibernate error - {},/n{}", e.getMessage(),e.getStackTrace());
            throw new IOException(e.getMessage());
        }
        request.setAttribute("form", mainForm);
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/mainFrame.jsp").forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    private int checkAction(HttpServletRequest request) throws ServletException{
        if(request.getParameter("AddDeposit")!=null){
            return 1;
        }
        if(request.getParameter("EditDeposit")!=null){
            return 2;
        }
        if(request.getParameter("Add")!=null){
            return 3;
        }
        if(request.getParameter("Edit")!=null){
            return 4;
        }
        if(request.getParameter("Delete")!=null){
            return 5;
        }
        if(request.getParameter("DeleteDeposit")!=null){
            return 6;
        }
        return 0;
    }
}