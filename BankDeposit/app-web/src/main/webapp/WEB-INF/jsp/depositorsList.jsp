<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
<body>

<style type="text/css">
    TABLE {
        width: 300px;
        border-collapse: collapse;
    }
    TD, TH {
        padding: 3px;
        border: 1px solid black;
    }
    TH {
        background: #b0e0e6;
    }
</style>

<a href='<spring:url value="/inputFormDepositor" />' > <spring:message code="depositor.create" /></a>

<form:form method="get" modelAttribute="depositors">
<h1><spring:message code="depositor.list" /></h1>
<ul>
    <table frame="hsides" rules="cols">
        <th>
          	<td>depositorId</td>
            <td>depositorName</td>
            <td>depositorIdDeposit</td>
            <td>depositorDateDeposit</td>
            <td>depositorAmountDeposit</td>
            <td>depositorAmountPlusDeposit</td>
            <td>depositorAmountMinusDeposit</td>
            <td>depositorDateReturnDeposit</td>
            <td>depositorMarkReturnDeposit</td>
        </th>
    <c:forEach items="${depositors}" var="depositor">
        <tr>
            <td/>
            <td>${depositor.depositorId}</td>
            <td>${depositor.depositorName}</td>
            <td>${depositor.depositorIdDeposit}</td>
            <td>${depositor.depositorDateDeposit}</td>
            <td>${depositor.depositorAmountDeposit}</td>
            <td>${depositor.depositorAmountPlusDeposit}</td>
            <td>${depositor.depositorAmountMinusDeposit}</td>
            <td>${depositor.depositorDateReturnDeposit}</td>
            <td>${depositor.depositorMarkReturnDeposit}</td>
       </tr>
    </c:forEach>
    </table>
</ul>
</form:form>

<a href='<spring:url value="/depositsList" />' > <spring:message code="deposit.create" /></a>

</body>
</html>