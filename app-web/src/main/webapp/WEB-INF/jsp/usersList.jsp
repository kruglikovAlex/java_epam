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

<form:form method="get" modelAttribute="users">
<h1><spring:message code="user.list" /></h1>
<ul>
    <table>
        <th>
            <td>id</td>
            <td>login</td>
            <td>name</td>
        </th>
    <c:forEach items="${users}" var="user">
        <tr>
            <td/>
            <td>${user.userId}</td>
            <td>${user.login}</td>
            <td>${user.name}</td>
        </tr>
    </c:forEach>
    </table>
</ul>
</form:form>

<a href='<spring:url value="/inputForm" />'> <spring:message code="user.create" /></a>
</body>
</html>