<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>BankDeposit management</title>
	<link href='<c:url value="/resources/css/tether.css"/>' rel="stylesheet">
	<link href='<c:url value="/resources/css/bootstrap.min.css"/>' rel="stylesheet">
	<link href='<c:url value="/resources/css/bootstrap-theme.min.css"/>' rel="stylesheet">
	<link href='<c:url value="/resources/css/jquery-ui.css"/>' rel="stylesheet">
</head>
<body>
	<div class="panel panel-success">
		<div class="panel-heading" style="font-weight: bold; font-size: 16px">BANK DEPOSITS:</div>
		<div class="panel-body">
			<div id="accordionMy">
				<h3>Request:</h3>
				<div>
					<form method="post" id="requestFrom" action='<c:url value="/restClient/deposit/submitRestQuery"/>' modelAttribute="query">
						<div id="hostURL-contain" class="ui-widget">
							<span class="label label-info" style="font-size: 17px">
								<span class="glyphicon glyphicon-question-sign"></span> URL->
							</span>
				 			<input id="HOST" name="HOST" type="text" size="70"/>
							<input id="URL" name="URL" type="text" size="50"/><br>
						</div>

						<div style="font-weight: bold" aria-setsize="50">
							<span class="label label-success" style="font-size: 13px">
								<input type="radio" name="httpMethod" checked> GET
								<input type="radio" name="httpMethod"/> PUT
								<input type="radio" name="httpMethod"/> POST
								<input type="radio" name="httpMethod"/> DELETE
							</span>
						</div>

						<p></p>

						<ul class="nav nav-tabs nav-justified smile-tabs">
							<li class="active"><a data-toggle="tab" href="#home">Raw payload</a></li>
							<li><a data-toggle="tab" href="#menu1">Deposit data form</a></li>
							<li><a data-toggle="tab" href="#menu2">Depositor data form</a></li>
						</ul>

						<div class="tab-content">
							<div id="home" class="tab-pane fade in active">
								<p></p>
								<textarea id="JsonRequest" name="JsonRequest" rows="4" cols="146"><c:out value="${jsonRequest}"/></textarea>
							</div>
							<div id="menu1" class="tab-pane fade">
								<p></p>
							    <div class="mainDepositArea">
								    <table frame="hsides" rules="cols">
									    <th>
											<td><label>Deposit Id:</label></td>
											<td><label>Deposit Name:</label></td>
											<td><label>Minimum Termin:</label></td>
											<td><label>Minimum Amount:</label></td>
											<td><label>Currency:</label></td>
											<td><label>Interest Rate:</label></td>
											<td><label>Add Conditions:</label></td>
									    </th>
									    <tr>
										    <td></td>
										    <td><input id="depositId" name="depositId" type="text" disabled size="15" value='<c:out value="${deposit.depositId}"/>'/></td>
										    <td><input id="depositName" name="depositName" type="text" size="35" value='<c:out value="${deposit.depositName}"/>'/></td>
										    <td><input id="depositMinTerm" name="depositMinTerm" type="text" size="5" value='<c:out value="${deposit.depositMinTerm}"/>'></td>
										    <td><input id="depositMinAmount" name="depositMinAmount" type="text" size="20" value='<c:out value="${deposit.depositMinAmount}"/>'/></td>
										    <td><input id="depositCurrency" name="depositCurrency" type="text" size="5" value='<c:out value="${deposit.depositCurrency}"/>'/></td>
										    <td><input id="depositInterestRate" name="depositInterestRate" type="text" size="5" value='<c:out value="${deposit.depositInterestRate}"/>'/></td>
										    <td><input id="depositAddConditions" name="depositAddConditions" type="text" size="25" value='<c:out value="${deposit.depositAddConditions}"/>'/></td>
									    </tr>
								    </table>
							    </div>
							</div>
							<div id="menu2" class="tab-pane fade">
								<p></p>
							    <div class="mainDepositorArea">
								    <table frame="hsides" rules="cols">
									    <th>
											<td><label>Depositor Id:</label></td>
											<td><label>Depositor Name:</label></td>
											<td><label>Date Deposit:</label></td>
											<td><label>Amount Deposit:</label></td>
											<td><label>Amount Plus:</label></td>
											<td><label>Amount Minus:</label></td>
											<td><label>Date Return :</label></td>
											<td><label>Return Mark:</label></td>
									    </th>
									    <tr>
										    <td></td>
										    <td><input id="depositorId" name="depositorId" type="text" disabled size="15" value='<c:out value="${depositor.depositorId}"/>'/></td>
										    <td><input id="depositorName" name="depositorName" type="text" size="38" value='<c:out value="${depositor.depositorName}"/>'/></td>
										    <td><input id="depositorDateDeposit" name="depositorDateDeposit" type="datetime"
														   pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" class="datepicker" value='<c:out value="${depositor.depositorId}"/>'/></td>
										    <td><input id="depositorAmountDeposit" name="depositorAmountDeposit" type="text" size="10" value='<c:out value="${depositor.depositorId}"/>'/></td>
										    <td><input id="depositorAmountPlusDeposit" name="depositorAmountPlusDeposit" type="text" size="10" value='<c:out value="${depositor.depositorId}"/>'/></td>
										    <td><input id="depositorAmountMinusDeposit" name="depositorAmountMinusDeposit" type="text" size="10" value='<c:out value="${depositor.depositorId}"/>'/></td>
										    <td><input id="depositorDateReturnDeposit" name="depositorDateReturnDeposit" type="datetime"
														   pattern = "(19|20)\d\d-((0[1-9]|1[012])-(0[1-9]|[12]\d)|(0[13-9]|1[012])-30|(0[13578]|1[02])-31)" size="10" class="datepicker" value='<c:out value="${depositor.depositorId}"/>'/></td>
										    <td><input id="depositorMarkReturnDeposit" name="depositorMarkReturnDeposit" type="text" size="1" value='<c:out value="${depositor.depositorId}"/>'/></td>
									    </tr>
								    </table>
							    </div>
							</div>
						</div>

						<p></p>

						<div class="btn-group btn-group-justified">
							<div class="btn-group">
								<button id="btnClear" type="button" class="btn btn-default">
									<span class="glyphicon glyphicon-trash"></span> CLEAR
								</button>
							</div>
							<div class="btn-group">
								<button id="btnSendMessage" type="button" class="btn btn-default">
									<span class="glyphicon glyphicon-envelope"></span> SEND
								</button>
							</div>
							<div class="btn-group">
							    <input type="submit" value="SEND Request" name="action"/>
                            </div>
						</div>

					</form>
				</div>
				<h3>Response:</h3>
				<div>
					<textarea class="responseHeader" id="responseHeader" name="responseHeader" rows="5" cols="146" ><c:out value="${responseHeader}"/></textarea>

					<div class="btn-group btn-group-justified">
						<div class="btn-group">
							<button id="btnResponseRaw" type="button" class="btn btn-default"><span class="glyphicon glyphicon-eye-close"></span> RAW</button>
						</div>
						<div class="btn-group">
							<button id="btnResponseJson" type="button" class="btn btn-default"><span class="glyphicon glyphicon-eye-open"></span> JSON</button>
						</div>
					</div>

					<textarea class="responseRaw" id="responseRaw" name="responseRaw" rows="10" cols="146" ><c:out value="${responseRaw}"/></textarea>
					<textarea class="responseJson" id="responseJson" name="responseJson" rows="10" cols="146" ><c:out value="${responseJson}"/></textarea>

				</div>

			</div>
		</div>
	</div>

	<script src='<c:url value="/resources/js/jquery-3.1.1.js"/>'></script>
	<script src='<c:url value="/resources/js/bootstrap.min.js"/>'></script>
	<script src='<c:url value="/resources/js/bootstrap-tab.js"/>'></script>
	<script src='<c:url value="/resources/js/tether.js"/>'></script>
	<script src='<c:url value="/resources/js/main.js"/>'></script>
	<script src='<c:url value="/resources/js/jquery.maskedinput.js"/>'></script>
	<script src='<c:url value="/resources/js/jquery-ui.js"/>'></script>

</body>
</html>
