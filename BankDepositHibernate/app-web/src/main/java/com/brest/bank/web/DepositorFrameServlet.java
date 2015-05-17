package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.util.HibernateUtil;
import com.brest.bank.web.forms.MainFrameForm;

import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.service.BankDepositorServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class DepositorFrameServlet extends HttpServlet{

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER = LogManager.getLogger();

    //private BankDepositService depositService = new BankDepositServiceImpl();
    //private BankDepositorService depositorService = new BankDepositorServiceImpl();

    private Collection deposits;
    private Collection depositors;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException
    {
        if("OK".equals(request.getParameter("action"))){
            try{
                if("".equals(request.getParameter("depositorId"))){
                    insertDepositor(request);
                } else {
                    updateDepositor(request);
                }
            }catch(HibernateException e){
                LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                throw new IOException(e.getMessage());
            }catch (ParseException e){
                LOGGER.error("Parse error - {},/n{}", e.getMessage(), e.getStackTrace());
                throw new IOException(e.getMessage());
            }
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    private void updateDepositor(HttpServletRequest request) throws HibernateException, ParseException{

        BankDepositor depositor = prepareDepositor(request);

        String depositorId = request.getParameter("depositorId");
        Long depId;
        if(depositorId.equals("")){
            depId= null;
        } else {
            depId = Long.parseLong(request.getParameter("depositorId"));
        }
        depositor.setDepositorId(depId);

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        HibernateUtil.getSessionFactory()
                .getCurrentSession().update(depositor);

        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
    }

    private void insertDepositor(HttpServletRequest request) throws HibernateException, ParseException{

        BankDepositor depositor = prepareDepositor(request);

        HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

        HibernateUtil.getSessionFactory()
                .getCurrentSession().save(depositor);

        BankDeposit theDeposit = (BankDeposit)HibernateUtil.getSessionFactory()
                .getCurrentSession().createQuery("select p from BankDeposit p left join fetch p.depositors where p.depositId = :pid")
                .setParameter("pid", Long.parseLong(request.getParameter("depositId")))
                .uniqueResult();

        theDeposit.getDepositors().add(depositor);

        HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
    }

    private BankDepositor prepareDepositor(HttpServletRequest request) throws ParseException{

        String depositorName = request.getParameter("depositorName");
        String depositorDateDeposit = request.getParameter("depositorDateDeposit");
        String depositorAmountDeposit = request.getParameter("depositorAmountDeposit");
        String depositorAmountPlusDeposit = request.getParameter("depositorAmountPlusDeposit");
        String depositorAmountMinusDeposit = request.getParameter("depositorAmountMinusDeposit");
        String depositorDateReturnDeposit = request.getParameter("depositorDateReturnDeposit");
        String depositorMarkReturnDeposit = request.getParameter("depositorMarkReturnDeposit");

        BankDepositor depositor = new BankDepositor(null,"",dateFormat.parse("1900-01-01"),0,0,0,dateFormat.parse("1900-01-01"),0,null);
            depositor.setDepositorName(depositorName);
            depositor.setDepositorDateDeposit(dateFormat.parse(depositorDateDeposit));
            depositor.setDepositorAmountDeposit(Integer.parseInt(depositorAmountDeposit));
            depositor.setDepositorAmountPlusDeposit(Integer.parseInt(depositorAmountPlusDeposit));
            depositor.setDepositorAmountMinusDeposit(Integer.parseInt(depositorAmountMinusDeposit));
            depositor.setDepositorDateReturnDeposit(dateFormat.parse(depositorDateReturnDeposit));
            depositor.setDepositorMarkReturnDeposit(Integer.parseInt(depositorMarkReturnDeposit));

        return depositor;
    }
}
