<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<html lang="en">
    <head>
        <title>Depositors list</title>
    </head>
    <body>
        <link href="<c:url value="/resources/css/bankDeposit.css" />" rel="stylesheet">
        <form method="GET" modelAttribute="deposits">
            <table>
                <tr>
                    <td>Year:<input type="text" name="year" value="${year}" size="4"/></td>
                    <td>Id:<input id="dId" type="text" name="Id" size="10"/></td>
                </tr>
                <tr>
                    <td>Deposits list:
                        <select name="depositId">
                            <c:forEach var="deposit" items="${deposits}">
                                <c:choose>
                                    <c:when test="${deposit.depositId==idDeposit}">
                                        <option value="${deposit.depositId}" selected>
                                            <c:out value="${deposit.depositId} | ${deposit.depositName} | ${deposit.depositCurrency} | ${deposit.depositMinTerm}"/>
                                        </option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${deposit.depositId}">
                                            <c:out value="${deposit.depositId} | ${deposit.depositName} | ${deposit.depositCurrency} | ${deposit.depositMinTerm}"/>
                                        </option>
                                    </c:otherwise>

                                </c:choose>
                            </c:forEach>
                        </select>
                    </td>
                    <td class="add" align=middle><a href='<spring:url value="/deposit/inputDeposit" > </spring:url>' class="buttonAdd"><b>Add</b></a></td>
                    <td class="update" align=middle><a href="#" id="aUpdate"></a></td>
                    <td class="delete" align=middle><a href="#" id="aDelete"></a></td>
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
                        <td><input type="radio" id="deprId" name="depositorId" value="${depositor.depositorId}"></td>
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
                    <td class="addDep" align=middle><a id="aAddDep"></a></td>
                    <td class="updateDep" align=middle><a id="aUpdDep"></a></td>
                    <td class="deleteDep" align=middle><a id="aDelDep"></a></td>
                </tr>
            </table>
        </form>
        <script src='<c:url value="/resources/js/jquery-1.11.1.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
        <script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>
        <script src='<c:url value="/resources/js/mainFrame.js"/>'></script>
    </body>
</html>