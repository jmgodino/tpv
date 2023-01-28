<%@page language="java" %>

<html>
<head>
<style>
.no-bullets {
    list-style-type: none;
}

li {
	margin-bottom: 5px;
}

label {
    width:200px;
    display:inline-block
}

.textoAyuda {
	padding-left:20px;
	color: blue;
	font-style: italic;
	font-size: 12px;
}
</style>
<script>
function cambioMedioPago(medioPago) {
	toogleList("listaOperacion");
	toogleList("listaPagoSeguro");
	toogleList("listaPagoDirecto");
	toogleList("listaPagoRest");
}

function toogleList(nombre) {
	var lista = document.getElementById(nombre);
	if (!lista.style.visibility || lista.style.visibility == 'visible') {
		lista.style.visibility = 'hidden';
	} else {
		lista.style.visibility = 'visible';
	}	
}

function cambioPagoRest() {
	toogleList("datosTarjeta");
}

</script>
</head>
<body>
<ul class="no-bullets">
<li>
	<h1>Datos del pago</h1>
</li>
<li>
	<form name="formu" action="/tpv/TpvPago" method="POST">
		<ul class="no-bullets">
		
		<li><h2>Datos de la autoliquidaci&oacute;n</h2></li>
		
		<li><label for="modelo">Modelo:</label><input type="text" name="modelo" value="100"/></li>
		<li><label for="ejercicio">Ejercicio:</label><input type="text" name="ejercicio" value="2022"/></li>
		<li><label for="periodo">Periodo:</label><input type="text" name="periodo" value="0A"/></li>
		<li><label for="nif">NIF Ordenante:</label><input type="text" name="nif" value="89890001K"/></li>
		<li><label for="importe">Importe en euros:</label><input type="text" name="importe" value="1.00"/></li>

		<li><h2>Opciones de idioma</h2></li>

		<li><label for="idioma">Idioma:</label><select name="idioma"><option value="1">Castellano</option><option value="1">Castellano</option><option value="2">Ingles</option><option value="3">Catalan</option></select></li>

		<li><h2>Opciones de pago</h2></li>

		<li><label for="mediopago">Medio de Pago:</label><select name="mediopago" onChange="cambioMedioPago(this)"><option value="C">Tarjeta</option><option value="z">Bizum</option></select></li>
		<li id="listaOperacion"><label for="operacion">Operacion:</label><select name="operacion"><option value="0">Autorizacion</option><option value="<%=com.picoto.tpv.dto.ext.DatosPagoTpvRedsys.OPERACION_PREAUTORIZACION%>">Preautorizacion</option></select></li>

		<li id="listaPagoSeguro"><label for="pagoseguro">Admitir Pago No Seguro:</label>
		<input type="radio" name="pagoinseguro" value="S">Si</input>
		<input type="radio" name="pagoinseguro" value="N" checked>No</input>
		<span class="textoAyuda">Esta opci&oacute;n le permitir&aacute; realizar pagos usando tan solo datos de su tarjeta, sin necesidad de datos adicionales</span>
		</li>

		<li id="listaPagoDirecto"><label for="pagodirecto">Realizar/Solicitar Pago Directo:</label>
		<input type="radio" name="pagodirecto" value="S">Si</input>
		<input type="radio" name="pagodirecto" value="N" checked>No</input>
		<span class="textoAyuda">Esta opci&oacute;n le permitir&aacute; realizar pr&oacute;ximos pagos sin tener que introducir los datos de su tarjeta</span>
		</li>

		<li id="listaPagoRest"><label for="pagorest">Realizar Pago desde el comercio</label>
		<input type="radio" name="pagorest" value="S" onChange="cambioPagoRest()">Si</input>
		<input type="radio" name="pagorest" value="N" onChange="cambioPagoRest()" checked>No</input>
		<span class="textoAyuda">Esta opci&oacute;n le permitira pagar desde el comercio con datos de tarjeta sin tener que acceder a una web externa</span>
		</li>

		<li>
		<ul id="datosTarjeta" class="no-bullets" style="visibility:hidden;">
			<li><label for="pan">N. Tarjeta:</label><input type="text" name="pan" value="4548810000000003"/></li>
			<li><label for="cad">F. Caducidad:</label><input type="text" name="cad" value="4912"/></li>
			<li><label for="cvv">CVV2:</label><input type="text" name="cvv" value="123"/></li>
		</ul>
		</li>

		<li><input type="submit" value= "Iniciar Pago"/></li>

		<ul>
	</form>

</li>
</ul>

</body>
</html>