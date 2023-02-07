<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setBundle basename="erroresTpv" var="msg"/>

<html>
<body>
<img src="/tpv/images/aeat.png" style="padding-left:45px;">
	<p>Error: ${datosPago.error}</p>
	<%-- <p><fmt:message key="${datosPago.codigoError}" bundle="${msg}"/></p> --%>
	
<ul class="no-bullets">
<li>
	<a href="/tpv/TpvInicio">Realizar otro pago</a> 
</li>
</ul>
	
</body>
</html>