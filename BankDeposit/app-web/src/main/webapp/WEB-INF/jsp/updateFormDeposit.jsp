<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<html>
<body>
 	<form action='<spring:url value="/deposits/updateFormDeposit" > </spring:url>' method="POST">
    	<form:form method="get" modelAttribute="deposit">
			<h1><spring:message code="update.deposit" /></h1>
        	<ul>
        	    <label path="depositId:">Id:</label>
        	        <input type="text" name="depositId" value='<c:out value="${deposit.depositId}"/>' hidden/><br/>
            	<label path="depositName:">Name deposit:</label>
            	    <input type="text" name="depositName" value='<c:out value="${deposit.depositName}"/>'/><br/>
                <label path="depositMinTerm:">Min Term:</label>
                	<input type="text" name="depositMinTerm" value='<c:out value="${deposit.depositMinTerm}"/>'/><br/>
                <label path="depositMinAmount:">Min Amount:</label>
                	<input type="text" name="depositMinAmount" value='<c:out value="${deposit.depositMinAmount}"/>'/><br/>
                <label path="depositCurrency:">Currency:</label>
                	<input type="text" name="depositCurrency" value='<c:out value="${deposit.depositCurrency}"/>'/><br/>
                <label path="depositInterestRate:">Interest Rate, (%):</label>
                	<input type="text" name="depositInterestRate" value='<c:out value="${deposit.depositInterestRate}"/>'/><br/>
                <label path="depositAddConditions:">Add Conditions:</label>
                	<input type="text" name="depositAddConditions" value='<c:out value="${deposit.depositAddConditions}"/>'/><br/>
                </ul>
    		<input type="submit" name="Submit">
 	</form>

</body>
</html>
