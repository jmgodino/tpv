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
		<li><label for="importe">Importe a pagar en TPV (en centimos de euro): </label>${datosPago.importeCentimos}</li>
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

</body>
</html>