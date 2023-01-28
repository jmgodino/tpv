<%@page language="java" %>

<html>
<head>
<style>
.no-bullets {
    list-style-type: none;
}

.rojo {
	color: red;
}

.textoTarjetas {
	color: red;
	font-style: italic;
	font-size: 12px;
}
</style>
</head>
<body>
<ul class="no-bullets">
<li>
	<h1>Va a realizar un pago con los siguientes datos mediante un TPV ajeno a esta Sede Electr&oacute;nica</h1>
</li>
		<li><label for="modelo">Modelo: </label>${param.modelo}</li>
		<li><label for="ejercicio">Ejercicio: </label>${param.ejercicio}</li>
		<li><label for="periodo">Periodo: </label>${param.periodo}</li>
		<li><label for="nif">NIF: </label>${param.nif}</li>
		<li><label for="importe">Importe en euros: </label>${param.importe}</li>
		<li><label for="importe">Importe a pagar en TPV: </label>${datosPago.importeCentimos}</li>
<li>
	<h2 class="rojo">Recuerde que esta forma de pago puede suponer el pago de una tasa de descuento ajena a este establecimiento del 0,31%</h2>
</li>
<li>
	<form action="https://sis-t.redsys.es:25443/sis/realizarPago" method="POST" target="_blank">
		<input type="hidden" name="Ds_SignatureVersion" value="${version}"/>
		<input type="hidden" name="Ds_MerchantParameters" value="${parametros}"/>
		<input type="hidden" name="Ds_Signature" value= "${firma}"/>
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