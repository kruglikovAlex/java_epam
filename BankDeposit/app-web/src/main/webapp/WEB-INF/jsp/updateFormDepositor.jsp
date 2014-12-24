<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<html>
<body>
 	<form action='<spring:url value="/depositors/updateFormDepositor" > </spring:url>' method="POST">
    	<form:form method="get" modelAttribute="depositor">
			<h1><spring:message code="update.depositor" /></h1>
        	<ul>
        	    <label path="depositorId:">Id:</label>
        	        <input type="text" name="depositorId" value='<c:out value="${depositor.depositorId}"/>' hidden/><br/>
            	<label path="depositorName:">Full name:</label>
            	    <input type="text" name="depositorName" value='<c:out value="${depositor.depositorName}"/>'/><br/>
                <label path="depositorIdDeposit">Id Deposit:</label>
                	<input type="text" name="depositorIdDeposit" value='<c:out value="${depositor.depositorIdDeposit}"/>'/><br/>
                <label path="depositorDateDeposit:">Date Deposit:</label>
                	<input id="MyDate7" type="text" name="depositorDateDeposit" value='<c:out value="${depositor.depositorDateDeposit}"/>'/><br/>
                <label path="depositorAmountDeposit:">Amount:</label>
                	<input type="text" name="depositorAmountDeposit" value='<c:out value="${depositor.depositorAmountDeposit}"/>'/><br/>
                <label path="depositorAmountPlusDeposit:">Amount Plus:</label>
                	<input type="text" name="depositorAmountPlusDeposit" value='<c:out value="${depositor.depositorAmountPlusDeposit}"/>'/><br/>
                <label path="depositorAmountMinusDeposit:">Amount Minus:</label>
                	<input type="text" name="depositorAmountMinusDeposit" value='<c:out value="${depositor.depositorAmountMinusDeposit}"/>'/><br/>
                <label path="depositorDateReturnDeposit:">Date Return:</label>
                	<input id="MyDate8" type="text" name="depositorDateReturnDeposit" value='<c:out value="${depositor.depositorDateReturnDeposit}"/>'/><br/>
                <label path="depositorMarkReturnDeposit:">Mark Return:</label>
                	<input type="text" name="depositorMarkReturnDeposit" value='<c:out value="${depositor.depositorMarkReturnDeposit}"/>'/><br/>
			</ul>
    		<input type="submit" name="Submit">
 </form>

<script src="/resources/js/jquery-1.11.1.js"></script>
<script src="/resources/js/jquery.maskedinput.js"></script>
<script src="/resources/js/bankDeposit.js"></script>
</body>
</html>