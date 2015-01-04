<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>

<html>
<style>
    .error {
        color: red; font-weight: bold;
    }
</style>
<body>
	<form action='<spring:url value="/deposits/submitDataDeposit" > </spring:url>' method="post" modelAttribute="deposit" commandName="deposit">
   		<form:form method="get" modelAttribute="deposit">
        	<h1><spring:message code="deposit.create" /></h1>
            <ul>
            	<label path="depositName:">Deposit Name :</label>
                	<input type="text" name="depositName" value='<c:out value="${deposit.depositName}"/>' size="30"/>
                    <span class="error"><form:errors path="depositName" size="100"/></span><br/>
                <label path="depositMinTerm:">Deposit Minimum termin, (mounth) :</label>
                    <input type="text" name="depositMinTerm" value='<c:out value="${deposit.depositMinTerm}"/>' size="3"/>
                    <span class="error"><form:errors path="depositMinTerm"/></span><br/>
                <label path="depositMinAmount:">Deposit Minimum Amount :</label>
                    <input type="text" name="depositMinAmount" value='<c:out value="${deposit.depositMinAmount}"/>'/>
                    <span class="error"><form:errors path="depositMinAmount"/></span><br/>
                <label path="depositCurrency:">Deposit Currency :</label>
                    <input type="text" name="depositCurrency" value='<c:out value="${deposit.depositCurrency}"/>' size="3"/>
                    <span class="error"><form:errors path="depositCurrency"/></span><br/>
                <label path="depositInterestRate:">Deposit Interest Rate, (%) :</label>
                    <input type="text" name="depositInterestRate" value='<c:out value="${deposit.depositInterestRate}"/>' size="3"/>
                    <span class="error"><form:errors path="depositInterestRate"/></span><br/>
                <label path="depositAddConditions:">Deposit Add Conditions :</label>
                    <input type="text" name="depositAddConditions" value='<c:out value="${deposit.depositAddConditions}"/>'/>
                    <span class="error"><form:errors path="depositAddConditions"/></span><br/>
            </ul>
            <input type="submit" name="Submit">
        </form:form>
	</form>
</body>
</html>