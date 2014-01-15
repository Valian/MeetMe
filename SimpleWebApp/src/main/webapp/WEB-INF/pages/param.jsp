<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>param view</title>
    </head>
    
    <body>
        
        URL variable: ${urlVar}
        <br/>
        <c:if test="${not empty reqParam}">
            
            Request parameter: ${reqParam}
            
        </c:if>
        <c:if test="${empty reqParam}">
            
            No request parameter(?param=:value:)!
            
        </c:if>
        
    </body>
</html>