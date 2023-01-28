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

.rojo {
	color: red;
}

.textoTarjetas {
	color: red;
	font-style: italic;
	font-size: 12px;
}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
<script>

function aceptaCondiciones(checkBoxField) {
  if (checkBoxField.checked) {
  	cargarDatosPagoTpv();
  	}
  toggle("formularioPago");
}

function cargarDatosPagoTpv() {
	var info = { "modelo":"${param.modelo}", "ejercicio":"${param.ejercicio}", "periodo":"${param.periodo}", "nif":"${param.nif}", "importe":"${param.importe}", "idioma":"${param.idioma}", "mediopago":"${param.mediopago}", "pagoinseguro":"${param.pagoinseguro}", "pagodirecto":"${param.pagodirecto}", "operacion":"${param.operacion}" };

        $.ajax({
            type: 'post',
            url: '/tpv/TpvDetalle',
            data: JSON.stringify(info),
            contentType: "application/json; charset=utf-8",
            traditional: true,
            dataType: "json",
            success: function (data) {
             	$("#versionTpv").val(data.version);
             	$("#parametrosTpv").val(data.parametros);
             	$("#firmaTpv").val(data.firma);
            }
        });
}


function toggle(nombre) {
	var lista = document.getElementById(nombre);
	if (!lista.style.visibility || lista.style.visibility == 'visible') {
		lista.style.visibility = 'hidden';
	} else {
		lista.style.visibility = 'visible';
	}	
}
</script>
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
	<input type="checkbox" id="condiciones" name="condiciones" onChange="aceptaCondiciones(this)"> Acepta las condiciones del servicio
</li>
<li>
&nbsp;
</li>

<li id="formularioPago"  style="visibility:hidden;">
	<form action="https://sis-t.redsys.es:25443/sis/realizarPago" method="POST" target="_blank">
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