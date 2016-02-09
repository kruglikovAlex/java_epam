<%@ page language="java" isErrorPage="true" contentType="application/json" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
{
    status:<%=request.getAttribute("javax.servlet.error.status_code") %>,
    reason:<%=request.getAttribute("javax.servlet.error.message") %>
}
