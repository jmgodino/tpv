<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<fmt:setLocale value="es"/>

<html>
<head>
<style>
.no-bullets {
    list-style-type: none;
}
</style>
</head>
<body>
<ul class="no-bullets">
<li>
	<h1>Pago finalizado correctamente. NRC ${datosPago.nrc} por importe <fmt:formatNumber type="number" pattern="0.00" value="${datosPago.importe}"/>€</h1> 
</li>
<li>
Pago seguro: ${datosPago.pagoSeguroTexto}
</li>
<li>
Pa&iacute;s del m&eacute;todo de pago: ${datosPago.detallePaisTarjeta}
</li>
<li>
M&eacute;todo de pago: ${datosPago.detalleMetodoPago}
</li>
<c:if test="${not datosPago.pagoBizum}">
	<li>
		Marca de la tarjeta: ${datosPago.detalleMarcaTarjeta}
	</li>
	<li>
		Tipo de tarjeta: ${datosPago.detalleTipoTarjeta}
	</li>
	<li>
		Tarjeta obligada a PSD2: ${datosPago.tarjetaPSD2}
	</li>		
</c:if>
<li>
&nbsp;
</li>
<li>
Detalles del pago: ${datosPago.detalles}
</li>
</ul>	
</body>
</html>