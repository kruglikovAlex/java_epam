<%--
Document : input deposit
Created on : March 12, 2015, 21:25:23 PM
Author	 : KAS
--%>
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<form method="post">
    <label path="depositName">Deposit name:</label><input type="text" name="depositName"/><br/>
    <label path="depositMinTerm">Min term deposit:</label><input type="text" name="depositMinTerm"/><br/>
    <label path="depositMinAmount">Min amount deposit:</label><input type="text" name="depositMinAmount"/><br/>
    <label path="depositCurrency">Currency:</label><input type="text" name="depositCurrency"/><br/>
    <label path="depositInterestRate">Interest rate:</label><input type="text" name="depositInterestRate"/><br/>
    <label path="depositAddConditions">Add conditions:</label><input type="text" name="depositAddConditions"/><br/>
    <input type="submit" name="action" value="store"/>
</form>

</body>
</html