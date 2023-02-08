<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="es"/>

<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
<script src="/tpv/js/tpv.js"></script>
<link rel="stylesheet" href="/tpv/css/tpv.css">
</head>
<body>
<img src="/tpv/images/aeat.png" style="padding-left:45px;">
<ul class="no-bullets">
<li>
	<h1>Va a realizar un pago con los siguientes datos mediante un TPV ajeno a esta Sede Electr&oacute;nica</h1>
</li>
		<li>Modelo: ${param.modelo}</li>
		<li>Ejercicio: ${param.ejercicio}</li>
		<li>Periodo: ${param.periodo}</li>
		<li>NIF: ${param.nif}</li>
		<li>Importe de la Autoliquidación: <fmt:formatNumber type="number" pattern="0.00" value="${param.importe}"/>€</li>
		<li>Importe a pagar en TPV: <span class="textoDestacado"><fmt:formatNumber type="number" pattern="0.00" value="${datosPago.importeIngresar}"/>€</span></li>
<li>
&nbsp;
</li>
<li>
	<input type="checkbox" id="condiciones" name="condiciones" onChange="aceptaCondiciones(this)"> Acepta las condiciones del servicio incluido el intercambio de datos con la entidad gestora del TPV además de un recargo del 0,31% en pago con tarjetas, totalmente ajeno a la AEAT.
</li>
<li>
&nbsp;
</li>

<li id="formularioPago"  style="visibility:hidden;">
	<form action="https://sis-t.redsys.es:25443/sis/realizarPago" method="POST">
		<input type="hidden" id="versionTpv" name="Ds_SignatureVersion" value=""/>
		<input type="hidden" id="parametrosTpv" name="Ds_MerchantParameters" value=""/>
		<input type="hidden" id="firmaTpv" name="Ds_Signature" value= ""/>
		<input type="submit" value= "Realizar Pago TPV"/>
	</form>
</li>
</ul>

<br/>

<ul class="no-bullets">
<li class="textoTarjetas">
Tarjeta VISA pago EMV3 DS - Num: 4548810000000003 Caducidad: 12/49 CVV2: 123
</li>
<li class="textoTarjetas">
Tarjeta VISA pago no seguro - Num: 4918019160034602 Caducidad: 12/34 CVV2: 123
</li>
<li class="textoTarjetas">
Tel&eacute;fono para pago bon Bizum: 700 000 000
</li>
</ul>
</body>
</html>