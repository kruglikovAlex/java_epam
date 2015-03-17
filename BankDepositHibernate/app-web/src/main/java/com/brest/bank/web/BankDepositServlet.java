package com.brest.bank.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import javax.servlet.RequestDispatcher;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import java.io.IOException;
import java.util.List;

public class BankDepositServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException{

       // BankDepositService depositService = new BankDepositServiceImpl();
        //List<BankDeposit> deposits = depositService.getBankDeposits();
        //request.getServletContext().setAttribute("deposits",deposits);
        getServletContext().getRequestDispatcher("/WEB-INF/jsp/depositList.jsp").forward(request,response);
    }
}
