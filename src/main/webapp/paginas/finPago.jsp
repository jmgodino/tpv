<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<fmt:setLocale value="es" />

<html>
<head>
<script src="/tpv/js/tpv.js"></script>
<link rel="stylesheet" href="/tpv/css/tpv.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
</head>
<body>
	<img src="/tpv/images/aeat.png" style="padding-left: 45px;">
	<ul class="no-bullets">
		<li>
			<h1>Pago finalizado correctamente. NRC Consolidado:
				${datosPago.nrc}</h1>
		</li>
		<li>Importe del pago (comisión de gestión incluída): <span
			class="textoDestacado"><fmt:formatNumber type="number"
					pattern="0.00" value="${datosPago.importe}" />€</span>
		</li>
		<li>Pago seguro: <span class="textoDestacado">${datosPago.pagoSeguroTexto}</span>
		</li>
		<li>Pa&iacute;s del m&eacute;todo de pago: <span
			class="textoDestacado">${datosPago.detallePaisTarjeta}</span>
		</li>
		<li>M&eacute;todo de pago: <span class="textoDestacado">${datosPago.detalleMetodoPago}</span>
		</li>
		<c:if test="${not datosPago.pagoBizum}">
			<li>Marca de la tarjeta: <span class="textoDestacado">${datosPago.detalleMarcaTarjeta}</span>
			</li>
		</c:if>
		<li>&nbsp;</li>
		<li>Detalles del pago: ${datosPago.detalles}</li>
	</ul>
	<ul class="no-bullets">
		<li><a href="/tpv/TpvInicio">Realizar otro pago</a></li>
		<li><a href="/tpv/TpvPagos">Ver mis pagos</a></li>
	</ul>

	<c:if test="${datosPago.necesitaConfirmacion}">
		<div id="confirmando" class="textoPanel">
			<h1>Confirmando pago desde la AEAT con TPV antes de consolidar
				el NRC</h1>
			(Solo a título instructivo, en realidad esta operación es casi inmediata)
		</div>
		<script>
			setTimeout(function() {
				$('#confirmando').hide();
			}, 8000);
		</script>
	</c:if>
</body>
</html>