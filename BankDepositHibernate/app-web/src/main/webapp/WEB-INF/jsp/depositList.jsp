<%--
Document : index
Created on : March 8, 2015, 18:25:23 PM
Author	 : KAS
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<%@page import="java.util.List"%>
<%@page import="com.brest.bank.service.BankDepositService"%>
<%@page import="com.brest.bank.service.BankDepositServiceImpl"%>
<%@page import="java.util.Date"%>
<%@page import="com.brest.bank.domain.BankDeposit"%>
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<link rel="stylesheet" type="text/css" href="/WEB-INF/css/style.css"/>
<title>Deposits list</title>
</head>
<body>
<center>
	 <div id="mystyle">
        <form>
            <h1>Deposits in database list:</h1>
            <table border='1'>
               <thead>
                  <tr>
                       <th>Deposit Name</th>
                       <th>Minimum term of deposit</th>
                       <th>Minimum sum of amount</th>
                       <th>Currency of deposit</th>
                       <th>Interest rate of deposit</th>
                       <th>Conditions</th>
                  </tr>
               </thead>
               <tbody>
                  <%
                     BankDepositService depositService = new BankDepositServiceImpl();
                     List<BankDeposit> deposits = depositService.getBankDeposits();
                     for (BankDeposit dep : deposits) {
                  %>
                  <tr>
                   		<td><%=dep.getDepositName()%></td>
                   		<td><%=dep.getDepositMinTerm()%></td>
                   		<td><%=dep.getDepositMinAmount()%></td>
                   		<td><%=dep.getDepositCurrency()%></td>
                   		<td><%=dep.getDepositInterestRate()%></td>
                   		<td><%=dep.getDepositAddConditions()%></td>
                  </tr>
                  <%}%>
               <tbody>
            </table>
            <h2>Deposits in database:<%=deposits.size()%></h2>
        </form>
	 </div>

</center>
</body>
</html>