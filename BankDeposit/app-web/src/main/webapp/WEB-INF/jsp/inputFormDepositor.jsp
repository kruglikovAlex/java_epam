<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<html>
<body>
<form action="/inputFormDepositor" method="POST">
    	<form:form method="get" modelAttribute="depositor">
			<h1><spring:message code="depositor.create" /></h1>
        	<ul>
  	            <label path="depositorName"><b>Full name Depositor:</b></label><input type="text" name="depositorName"/><br/>
                <label path="depositorIdDeposit">Id Deposit:</label>
                    <input type="text" name="depositorIdDeposit" value='<c:out value="${depositor.depositorIdDeposit}"/>' /><br/>
                <label path="depositorDateDeposit">Date Deposit, (yyyy-mm-dd):</label>
                    <input id="MyDate5" type="text" name="depositorDateDeposit" /><br/>
                <label path="depositorAmountDeposit">Amount:</label>
                    <input type="text" name="depositorAmountDeposit"/><br/>
                <label path="depositorAmountPlusDeposit">Amount Plus:</label>
                    <input type="text" name="depositorAmountPlusDeposit"/><br/>
                <label path="depositorAmountMinusDeposit">Amount Minus:</label>
                    <input type="text" name="depositorAmountMinusDeposit"/><br/>
                <label path="depositorDateReturnDeposit">Date Return, (yyyy-mm-dd):</label>
                    <input id="MyDate6" type="text" name="depositorDateReturnDeposit"/><br/>
                <label path="depositorMarkReturnDeposit">Mark Return:</label>
                    <input type="text" name="depositorMarkReturnDeposit"/><br/>
            </ul>
            <input type="submit" name="Submit">
</form>

<script src="/resources/js/jquery-1.11.1.js"></script>
<script src="/resources/js/jquery.maskedinput.js"></script>
<script src="/resources/js/bankDeposit.js"></script>
</body>
</html>