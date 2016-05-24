<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Deposit</title>
    </head>
    <body>
        <form action='<c:url value="/deposit/submitDataDeposit"/>' method="POST">
            <h1><spring:message code="deposit.create" /></h1>
            <input type="hidden" name="depositId" value='<c:out value="${deposit.depositId}"/>'/>
            <table>
                <tr>
                    <td>Name:</td>
                    <td><input type="text" name="depositName" value='<c:out value="${deposit.depositName}"/>'/></td>
                </tr>
                <tr>
                    <td>Minimum term, (month) :</td>
                    <td><input type="text" name="depositMinTerm" value='<c:out value="${deposit.depositMinTerm}"/>'/></td>
                </tr>
                <tr>
                    <td>Min amount:</td>
                    <td><input type="text" name="depositMinAmount" value='<c:out value="${deposit.depositMinAmount}"/>'/><td>
                </tr>
                <tr>
                    <td>Currency:</td>
                    <td><input type="text" name="depositCurrency" value='<c:out value="${deposit.depositCurrency}"/>'/></td>
                </tr>
                <tr>
                    <td>Interest rate,(%):</td>
                    <td><input type="text" name="depositInterestRate" value='<c:out value="${deposit.depositInterestRate}"/>'/></td>
                </tr>
                <tr>
                    <td>Add conditions:</td>
                    <td><textarea id="depositAddConditions" name="depositAddConditions" rows="10" cols="70" ><c:out value="${deposit.depositAddConditions}"/></textarea></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td><input type="submit" value="OK" name="action"/></td>
                    <td><input type="submit" value="Cancel" name="action"/></td>
                </tr>
            </table>
        </form>
        <script src='<c:url value="/resources/js/jquery-1.11.1.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
        <script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>
    </body>
</html>