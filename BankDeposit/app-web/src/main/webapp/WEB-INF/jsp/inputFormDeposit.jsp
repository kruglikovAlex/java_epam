<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isELIgnored="false"%>

<html>
<body>
<form action="/submitDataDeposit" method="post">
    <h1><spring:message code="deposit.create" /></h1>
        <ul>
            <label path="depositName">Deposit Name :</label><input type="text" name="depositName"/><br/>
            <label path="depositMinTerm">Deposit Minimum Termin, (mounth) :</label><input type="text" name="depositMinTerm"/><br/>
            <label path="depositMinAmount">Deposit Minimum Amount :</label><input type="text" name="depositMinAmount"/><br/>
            <label path="depositCurrency">Deposit Currency :</label><input type="text" name="depositCurrency"/><br/>
            <label path="depositInterestRate">Deposit Interest Rate, (%) :</label><input type="text" name="depositInterestRate"/><br/>
            <label path="depositAddConditions">Deposit Add Conditions :</label><input type="text" name="depositAddConditions"/><br/>
        </ul>
        <input type="submit" name="Submit">
</form>

</body>
</html>