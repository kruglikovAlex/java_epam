<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Depositors list</title>
    </head>
    <body>
        <form action='<c:url value="/submitDataDepositor"/>' method="POST">
            <input type="hidden" name="depositorId" value='<c:out value="${depositor.depositorId}"/>'/>
            <table>
                <tr>
                    <td>Deposit Id:</td><td><input type="text" name="depositId" value='<c:out value="${idDeposit.depositId}"/>'/></td>
                </tr>
                <tr>
                    <td>Depositor name:</td><td><input type="text" name="depositorName" value='<c:out value="${depositor.depositorName}"/>'/></td>
                </tr>
                <tr>
                    <td>Date deposit:</td><td><input type="text" name="depositorDateDeposit" value='<c:out value="${depositor.depositorDateDeposit}"/>'/></td>
                </tr>
                <tr>
                    <td>Amount deposit:</td><td><input type="text" name="depositorAmountDeposit" value='<c:out value="${depositor.depositorAmountDeposit}"/>'/><td>
                </tr>
                <tr>
                    <td>Plus deposit:</td><td><input type="text" name="depositorAmountPlusDeposit" value='<c:out value="${depositor.depositorAmountPlusDeposit}"/>'/></td>
                </tr>
                <tr>
                    <td>Minus deposit:</td><td><input type="text" name="depositorAmountMinusDeposit" value='<c:out value="${depositor.depositorAmountMinusDeposit}"/>'/></td>
                </tr>
                <tr>
                    <td>Date return deposit:</td><td><input type="text" name="depositorDateReturnDeposit" value='<c:out value="${depositor.depositorDateReturnDeposit}"/>'/></td>
                </tr>
                <tr>
                    <td>Mark return deposit:</td>
                    <td>
                        <c:choose>
                            <c:when test="${depositor.depositorMarkReturnDeposit==0}">
                                <input type="radio" name="depositorMarkReturnDeposit" value='1'>Yes</input>
                                <input type="radio" name="depositorMarkReturnDeposit" value="0" checked>No</input>
                            </c:when>
                            <c:otherwise>
                                <input type="radio" name="depositorMarkReturnDeposit" value='1' checked>Yes</input>
                                <input type="radio" name="depositorMarkReturnDeposit" value="0">No</input>
                            </c:otherwise>
                        </c:choose>
                    </td>
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