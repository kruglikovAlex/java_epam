<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <title>Depositor</title>
    </head>
    <body>
        <link href='<c:url value="/resources/css/jquery-ui.css" />' rel="stylesheet">

        <form action='<spring:url value="/depositor/submitDataDepositor"> <spring:param name="idDeposit" value="${idDeposit}"/> </spring:url>' method="post" modelAttribute="depositor" commandName="depositor">
            <h1><spring:message code="depositor.create" /></h1>
            <input type="hidden" name="depositorId" value='<c:out value="${depositor.depositorId}"/>'/>
            <table>
                <tr>
                    <td>Deposit Id:</td>
                    <td><input type="text" name="depositId" value='<c:out value="${idDeposit}"/>'/></td>
                </tr>
                <tr>
                    <td>Full name Depositor:</td>
                    <td><input type="text" name="depositorName" value='<c:out value="${depositor.depositorName}"/>'/></td>
                </tr>
                <tr>
                    <td>Date Deposit:</td>
                    <td><input type="text" name="depositorDateDeposit"
                    pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" value='<fmt:formatDate value="${depositor.depositorDateDeposit}" type="both" pattern="yyyy-MM-dd"/>' class="datepicker"/></td>
                </tr>
                <tr>
                    <td>Amount:</td>
                    <td><input type="text" name="depositorAmountDeposit" value='<c:out value="${depositor.depositorAmountDeposit}"/>'/><td>
                </tr>
                <tr>
                    <td>Amount Plus:</td>
                    <td><input type="text" name="depositorAmountPlusDeposit" value='<c:out value="${depositor.depositorAmountPlusDeposit}"/>'/></td>
                </tr>
                <tr>
                    <td>Amount Minus:</td>
                    <td><input type="text" name="depositorAmountMinusDeposit" value='<c:out value="${depositor.depositorAmountMinusDeposit}"/>'/></td>
                </tr>
                <tr>
                    <td>Date Return:</td>
                    <td><input type="text" name="depositorDateReturnDeposit"
                     pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" value='<fmt:formatDate value="${depositor.depositorDateReturnDeposit}" type="both" pattern="yyyy-MM-dd"/>'  class="datepicker"/></td>
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
                    <td><input id="submit" type="submit" name="submit"></td>
                </tr>
            </table>
        </form>
        <script src='<c:url value="/resources/js/jquery-1.12.3.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery.min.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
        <script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>
        <script src='<c:url value="/resources/js/depositorFrame.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery-ui.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery-ui.min.js"/>'></script>
    </body>
</html>
