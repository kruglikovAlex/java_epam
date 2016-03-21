<%--
Document : index
Created on : March 8, 2015, 18:25:23 PM
Author	 : KAS
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<html lang="en">
    <head>
        <title>Depositors list</title>
    </head>
    <body>
        <form action='<c:url value="/main"/>' method="GET" modelAttribute="deposits">
            <table>
                <tr>
                    <td>Year:<input type="text" name="year" value="${year}"/></td>
                </tr>
                <tr>
                    <td>Deposits list:
                        <select name="depositId">
                            <c:forEach var="deposit" items="${deposits}">
                                <c:choose>
                                    <c:when test="${deposit.depositId==idDeposit}">
                                        <option value="${deposit.depositId}" selected>
                                            <c:out value="${deposit.depositName}"/>
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${deposit.depositId}">
                                            <c:out value="${deposit.depositName}"/>
                                        </option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                    </td>
                    <td><input type="submit" name="AddDeposit" value="Add"/></td>
                    <td><input type="submit" name="EditDeposit" value="Edit"/></td>
                    <td><input type="submit" name="DeleteDeposit" value="Delete"/></td>
                </tr>
            </table>

            <p/><b>List depositors for choose parameters:<b></br>
            <table border='1'>
                <tr>
                    <th>&nbsp;</th>
                    <th>Depositor Name</th>
                    <th>Date of deposit</th>
                    <th>Amount</th>
                    <th>Amount plus</th>
                    <th>Amount minus</th>
                    <th>Date return</th>
                    <th>Mark return</th>
                </tr>
                <c:forEach items="${depositors}" var="depositor">
                    <tr>
                        <td><input type="radio" name="depositorId" value="${depositor.depositorId}"></td>
                        <td><c:out value="${depositor.depositorName}"/></td>
                        <td><c:out value="${depositor.depositorDateDeposit}"/></td>
                        <td><c:out value="${depositor.depositorAmountDeposit}"/></td>
                        <td><c:out value="${depositor.depositorAmountPlusDeposit}"/></td>
                        <td><c:out value="${depositor.depositorAmountMinusDeposit}"/></td>
                        <td><c:out value="${depositor.depositorDateReturnDeposit}"/></td>
                        <td><c:out value="${depositor.depositorMarkReturnDeposit}"/></td>
                    </tr>
                </c:forEach>
            </table>

            <table>
                <tr>
                    <input type="submit" value="Add" name="Add">
                    <input type="submit" value="Edit" name="Edit">
                    <input type="submit" value="Delete" name="Delete">
                </tr>
            </table>
        </form>
    </body>
</html>