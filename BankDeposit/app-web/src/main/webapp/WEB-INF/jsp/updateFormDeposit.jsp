<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
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
        	        <span class="error">
        	            <form:errors path="depositId" size="100"/>
        	        </span><br/>
            	<label path="depositName:">Deposit Name :</label>
            	    <input type="text" id="depositName" name="depositName" value='<c:out value="${deposit.depositName}"/>' size="30"/>
            	    <span class="error">
            	        <form:errors path="depositName" size="100"/>
            	    </span><br/>
                <label path="depositMinTerm:">Deposit Minimum termin, (mounth) :</label>
                	<input type="text" id="depositMinTerm" name="depositMinTerm" value='<c:out value="${deposit.depositMinTerm}"/>' size="3"/>
                	<span class="error">
                	    <form:errors id="errordepositMinTerm" path="depositMinTerm"/>
                	</span><br/>
                <label path="depositMinAmount:">Deposit Minimum Amount :</label>
                    <input type="text" id="depositMinAmount" name="depositMinAmount" value='<c:out value="${deposit.depositMinAmount}"/>'/>
                    <span class="error">
                    	<form:errors id="errordepositMinAmount" path="depositMinAmount"/>
                    </span><br/>
                <label path="depositCurrency:">Deposit Currency :</label>
                	<input type="text" name="depositCurrency" value='<c:out value="${deposit.depositCurrency}"/>' size="3"/>
                	<span class="error">
                	    <form:errors path="depositCurrency"/>
                	</span><br/>
                <label path="depositInterestRate:">Deposit Interest Rate, (%) :</label>
                	<input type="text" id="depositInterestRate" name="depositInterestRate" value='<c:out value="${deposit.depositInterestRate}"/>' size="3"/>
                	<span class="error">
                	    <form:errors id="errordepositInterestRate" path="depositInterestRate"/>
                	</span><br/>
                <label path="depositAddConditions:">Deposit Add Conditions :</label>
                	<input type="text" name="depositAddConditions" value='<c:out value="${deposit.depositAddConditions}"/>'/>
                	<span class="error">
                	    <form:errors path="depositAddConditions"/>
                	</span><br/>
                </ul>
    		<input id="submit" type="submit" name="submit">
 		</form:form>
 	</form>

	<script src='<c:url value="/resources/js/jquery-1.11.1.js"/>'></script>

	<script>
    	var nullInt = function () {
        	if(this.value == null || this.value == '') {
            	document.getElementById('error'+this.name).style.border=  "1px dotted red"
            	document.getElementById('error'+this.name).style.fontSize = "x-large";
            	alert('The fild '+ this.name+' is empty!!!');
        	}
    	}
    	depositMinTerm.onchange = nullInt;
    	depositMinTerm.onkeyup = nullInt;

    	depositMinAmount.onchange = nullInt;
    	depositMinAmount.onkeyup = nullInt;

    	depositInterestRate.onchange = nullInt;
    	depositInterestRate.onkeyup = nullInt;

	</script>
	<script>
    	var nullIntSubmit = function () {
            if(depositMinTerm.value == null || depositMinTerm.value == '') {
                depositMinTerm.value = "0";
            }
            if(depositMinAmount.value == null || depositMinAmount.value == '') {
                depositMinAmount.value = "0";
            }
            if(depositInterestRate.value == null || depositInterestRate.value == '') {
                depositInterestRate.value = "0";
            }
    	}
    	submit.onclick = nullIntSubmit;

	</script>
</body>
</html>
