<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Deposits list</title>
    </head>
    <body>
        <form action='<c:url value="/editDeposit"/>' method="POST">
            <input type="hidden" name="depositId" value='<c:out value="${deposit.depositId}"/>'/>
            <table>
                <tr>
                    <td>Deposit name:</td><td><input type="text" name="depositName" value='<c:out value="${deposit.depositName}"/>'/></td>
                </tr>
                <tr>
                    <td>Min term deposit:</td><td><input type="text" name="depositMinTerm" value='<c:out value="${deposit.depositMinTerm}"/>'/></td>
                </tr>
                <tr>
                    <td>Min amount deposit:</td><td><input type="text" name="depositMinAmount" value='<c:out value="${deposit.depositMinAmount}"/>'/><td>
                </tr>
                <tr>
                    <td>Currency:</td><td><input type="text" name="depositCurrency" value='<c:out value="${deposit.depositCurrency}"/>'/></td>
                </tr>
                <tr>
                    <td>Interest rate:</td><td><input type="text" name="depositInterestRate" value='<c:out value="${deposit.depositInterestRate}"/>'/></td>
                </tr>
                <tr>
                    <td>Add conditions:</td><td><input type="text" name="depositAddConditions" value='<c:out value="${deposit.depositAddConditions}"/>'/></td>
                </tr>
            </table>
            <table>
                <tr>
                    <td><input type="submit" value="OK" name="action"/></td>
                    <td><input type="submit" value="Cancel" name="action"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>