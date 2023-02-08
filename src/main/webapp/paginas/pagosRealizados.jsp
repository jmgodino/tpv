<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="es"/>

<html>
<head>
<script src="/tpv/js/tpv.js"></script>
<link rel="stylesheet" href="/tpv/css/tpv.css">
</head>
<body>
<img src="/tpv/images/aeat.png" style="padding-left:45px;">
<ul class="no-bullets">
<li>
	<h1>Pagos Realizados con el TPV</h1>
</li>
<c:forEach var="pago" items="${pagos}">
	<li><c:out value = "${pago}"/></li>
</c:forEach>
</ul>

<ul class="no-bullets">
<li>
	<a href="/tpv/TpvInicio">Realizar otro pago</a> 
</li>
</ul>
</body>
</html>