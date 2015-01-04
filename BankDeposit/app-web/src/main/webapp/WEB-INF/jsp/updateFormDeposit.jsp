<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>

<html>
<style>
    .error {
        color: red; font-weight: bold;
    }
</style>
<body>
 	<form action='<spring:url value="/deposits/updateFormDeposit" > </spring:url>' method="POST" modelAttribute="deposit" commandName="deposit">
    	<form:form method="get" modelAttribute="deposit">
			<h1><spring:message code="update.deposit" /></h1>
        	<ul>
        	    <label path="depositId:">Id:</label>
        	        <input type="text" name="depositId" value='<c:out value="${deposit.depositId}"/>' hidden/>
        	        <span class="error"><form:errors path="depositName" size="100"/></span><br/>
            	<label path="depositName:">Name deposit:</label>
            	    <input type="text" name="depositName" value='<c:out value="${deposit.depositName}"/>'/>
            	    <span class="error"><form:errors path="depositMinTerm"/></span><br/>
                <label path="depositMinTerm:">Min Term:</label>
                	<input type="text" name="depositMinTerm" value='<c:out value="${deposit.depositMinTerm}"/>'/>
                	<span class="error"><form:errors path="depositMinAmount"/></span><br/>
                <label path="depositMinAmount:">Min Amount:</label>
                	<input type="text" name="depositMinAmount" value='<c:out value="${deposit.depositMinAmount}"/>'/>
                	<span class="error"><form:errors path="depositCurrency"/></span><br/>
                <label path="depositCurrency:">Currency:</label>
                	<input type="text" name="depositCurrency" value='<c:out value="${deposit.depositCurrency}"/>'/>
                	<span class="error"><form:errors path="depositInterestRate"/></span><br/>
                <label path="depositInterestRate:">Interest Rate, (%):</label>
                	<input type="text" name="depositInterestRate" value='<c:out value="${deposit.depositInterestRate}"/>'/>
                	<br/>
                <label path="depositAddConditions:">Add Conditions:</label>
                	<input type="text" name="depositAddConditions" value='<c:out value="${deposit.depositAddConditions}"/>'/>
                	<span class="error"><form:errors path="depositAddConditions"/></span><br/>
                </ul>
    		<input type="submit" name="Submit">
 		</form:form>
 	</form>

</body>
</html>
