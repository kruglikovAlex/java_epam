<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<html>
<head>
</head>
<body>

<form action='<spring:url value="/depositors/inputFormDepositor" > </spring:url>' method="POST">
    	<form:form method="get" modelAttribute="depositor">
			<h1><spring:message code="depositor.create" /></h1>
        	<ul>
  	            <label path="depositorName"><b>Full name Depositor:</b></label><input type="text" name="depositorName"/><br/>
                <label path="depositorIdDeposit">Id Deposit:</label>
                    <input type="text" name="depositorIdDeposit" value='<c:out value="${depositor.depositorIdDeposit}"/>' /><br/>
                <label path="depositorDateDeposit">Date Deposit, (yyyy-mm-dd):</label>
                    <input id="MyDate5" type="text" name="depositorDateDeposit"
                    pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/><br/>
                <label path="depositorAmountDeposit">Amount:</label>
                    <input type="text" name="depositorAmountDeposit"/><br/>
                <label path="depositorAmountPlusDeposit">Amount Plus:</label>
                    <input type="text" name="depositorAmountPlusDeposit"/><br/>
                <label path="depositorAmountMinusDeposit">Amount Minus:</label>
                    <input type="text" name="depositorAmountMinusDeposit"/><br/>
                <label path="depositorDateReturnDeposit">Date Return, (yyyy-mm-dd):</label>
                    <input id="MyDate6" type="text" name="depositorDateReturnDeposit"
                    pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size=10/><br/>
                <label path="depositorMarkReturnDeposit">Mark Return:</label>
                    <input id="depositorMarkReturnDeposit" name="depositorMarkReturnDeposit" type="text" value="0" size=1 /><br/>
            </ul>
            <input type="submit" name="Submit">
</form>

<script src='<c:url value="/resources/js/jquery-1.11.1.js"/>'></script>
<script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
<script src='<c:url value="/resources/js/bankDeposit.js"/>'></script>
</body>
</html>