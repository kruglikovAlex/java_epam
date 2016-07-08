<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Deposit</title>
    </head>

    <body>
        <style>
            .error {
                color: red; font-weight: bold;
            }
        </style>

        <form action='<c:url value="/deposit/submitDataDeposit"/>' method="POST">
            <form:form method="GET" modelAttribute="deposit">
            <h1><spring:message code="deposit.create" /></h1>
            <input type="hidden" name="depositId" value='<c:out value="${deposit.depositId}"/>'/>
            <table>
                <tr>
                    <td>Name:</td>
                    <td><input type="text" name="depositName" value='<c:out value="${deposit.depositName}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositName" path="depositName" size="100"/>
                        </span><br/>
                    </td>
                </tr>
                <tr>
                    <td>Minimum term, (month) :</td>
                    <td><input type="text" name="depositMinTerm" value='<c:out value="${deposit.depositMinTerm}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositMinTerm" path="depositMinTerm"/>
                        </span><br/>
                    </td>
                </tr>
                <tr>
                    <td>Min amount:</td>
                    <td><input type="text" name="depositMinAmount" value='<c:out value="${deposit.depositMinAmount}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositMinAmount" path="depositMinAmount"/>
                        </span><br/>
                    <td>
                </tr>
                <tr>
                    <td>Currency:</td>
                    <td><input type="text" name="depositCurrency" value='<c:out value="${deposit.depositCurrency}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositCurrency" path="depositCurrency"/>
                        </span><br/>
                    </td>
                </tr>
                <tr>
                    <td>Interest rate,(%):</td>
                    <td><input type="text" name="depositInterestRate" value='<c:out value="${deposit.depositInterestRate}"/>'/>
                        <span class="error">
                            <form:errors id="errordepositInterestRate" path="depositInterestRate"/>
                        </span><br/>
                    </td>
                </tr>
                <tr>
                    <td>Add conditions:</td>
                    <td><textarea id="depositAddConditions" name="depositAddConditions" rows="10" cols="70" ><c:out value="${deposit.depositAddConditions}"/></textarea>
                        <span class="error">
                            <form:errors id="errordepositAddConditions" path="depositAddConditions"/>
                        </span><br/>
                    </td>
                </tr>
            </table>
            <table>
                <tr>
                    <td><input type="submit" value="OK" name="action"/></td>
                    <td><input type="submit" value="Cancel" name="cancel"/></td>
                </tr>
            </table>
            </form:form>
        </form>
        <script src='<c:url value="/resources/js/jquery.min.js"/>'></script>
        <script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
        <script src='<c:url value="/resources/js/depositFrame.js"/>'></script>
    </body>
</html>