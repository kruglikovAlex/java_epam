package com.brest.bank.web;

import com.brest.bank.domain.BankDeposit;
import com.brest.bank.domain.BankDepositor;
import com.brest.bank.service.BankDepositService;
import com.brest.bank.service.BankDepositServiceImpl;
import com.brest.bank.service.BankDepositorService;
import com.brest.bank.service.BankDepositorServiceImpl;
import com.brest.bank.dao.BankDepositDao;
import com.brest.bank.dao.BankDepositDaoImpl;

import com.brest.bank.util.HibernateUtil;
import com.brest.bank.web.forms.MainFrameForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DepositFrameServlet extends HttpServlet{

    private static final Logger LOGGER = LogManager.getLogger();

    private BankDepositService depositService = new BankDepositServiceImpl();
    private BankDepositorService depositorService = new BankDepositorServiceImpl();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException,IOException
    {
        if("OK".equals(request.getParameter("action"))){
            try{
                if("".equals(request.getParameter("depositId"))){
                    insertDeposit(request);
                } else {
                    updateDeposit(request);
                }
            } catch (HibernateException e) {
                LOGGER.error("Hibernate error - {},/n{}", e.getMessage(), e.getStackTrace());
                throw new IOException(e.getMessage());
            }
        }

        MainFrameForm mainForm = new MainFrameForm();
        try {
            BankDeposit deposit;
            Collection deposits = new ArrayList<BankDeposit>();
            Collection depositors  = new ArrayList<BankDepositor>();

            for(Object d: (List)depositService.getBankDeposits()){
                deposits.add((BankDeposit)d);
            }

            if (deposits.size() == 0){
                deposit = new BankDeposit(1L," ",0,0," ",0," ",null);
                deposits.add(deposit);
            } else{
                Iterator i = deposits.iterator();
                deposit = (BankDeposit) i.next();
            }

            for(Object d: depositorService.getBankDepositors()){
                depositors.add((BankDepositor)d);
            }

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

    private void updateDeposit(HttpServletRequest request) throws HibernateException{

        BankDeposit deposit = prepareDeposit(request);
        deposit.setDepositId(Long.parseLong(request.getParameter("depositId")));

        depositService.updateBankDeposit(deposit);
    }

    private void insertDeposit(HttpServletRequest request) throws HibernateException{

        depositService.addBankDeposit(prepareDeposit(request));
    }

    private BankDeposit prepareDeposit(HttpServletRequest request){
        String depositName =  request.getParameter("depositName");
        String depositMinTerm =  request.getParameter("depositMinTerm");
        String depositMinAmount =  request.getParameter("depositMinAmount");
        String depositCurrency =  request.getParameter("depositCurrency");
        String depositInterestRate =  request.getParameter("depositInterestRate");
        String depositAddConditions =  request.getParameter("depositAddConditions");

        BankDeposit deposit = new BankDeposit();
            deposit.setDepositName(depositName);
            deposit.setDepositMinTerm(Integer.parseInt(depositMinTerm));
            deposit.setDepositMinAmount(Integer.parseInt(depositMinAmount));
            deposit.setDepositCurrency(depositCurrency);
            deposit.setDepositInterestRate(Integer.parseInt(depositInterestRate));
            deposit.setDepositAddConditions(depositAddConditions);
        return deposit;
    }
}
