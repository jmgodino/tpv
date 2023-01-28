<%@page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="es"/>

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
	font-size: 10px;
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
  	checkBoxField.disabled = true;
  	if (esVacio("#versionTpv") || esVacio("#parametrosTpv") || esVacio("#firmaTpv")) { 
  		cargarDatosPagoTpv();
  	}
  }
}

function esVacio() {
	return !$("#versionTpv").val();
}

function cargarDatosPagoTpv() {
	var info = { "modelo":"${param.modelo}", "ejercicio":"${param.ejercicio}", "periodo":"${param.periodo}", "nif":"${param.nif}", "importe":"${param.importe}", "idioma":"${param.idioma}", "mediopago":"${param.mediopago}", "pagoinseguro":"${param.pagoinseguro}", "pagodirecto":"${param.pagodirecto}", "operacion":"${param.operacion}", "hash":"${datosPago.hash}" };

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
           	  	toggle("formularioPago");
            },
			statusCode: {
        		500: function() {
          			alert( 'Error al preparar llamada al TPV' );
        		}
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
		<li>Modelo: ${param.modelo}</li>
		<li>Ejercicio: ${param.ejercicio}</li>
		<li>Periodo: ${param.periodo}</li>
		<li>NIF: ${param.nif}</li>
		<li>Importe en comercio: <fmt:formatNumber type="number" pattern="0.00" value="${param.importe}"/>€</li>
		<li>Importe a pagar en TPV: <fmt:formatNumber type="number" pattern="0.00" value="${datosPago.importeIngresar}"/>€</li>
<li>
&nbsp;
</li>
<li>
	<input type="checkbox" id="condiciones" name="condiciones" onChange="aceptaCondiciones(this)"> Acepta las condiciones del servicio incluido un recargo del 0,31% ajeno al comercio
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