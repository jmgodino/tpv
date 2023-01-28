<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setBundle basename="erroresTpv" var="msg"/>

<html>
<body>
	<p>Error: ${datosPago.error}</p>
	<p><fmt:message key="${datosPago.codigoError}" bundle="${msg}"/></p>
</body>
</html>