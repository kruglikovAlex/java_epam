<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Tell the JSP Page that please do not ignore Expression Language -->
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <title>Error page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<button onclick="history.back()">Back to Previous Page</button>

<p><b>Date:</b> ${datetime}</p>
<p><b>Error code:</b> ${exception}</p>
<p><b>Request URI:</b> ${url}</p><br/>
</body>
</html>