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
	var info = { "modelo": "${param.modelo}", "ejercicio": "${param.ejercicio}", "periodo": "${param.periodo}", "nif": "${param.nif}", "importe": "${param.importe}", "idioma": "${param.idioma}", "mediopago": "${param.mediopago}", "pagoinseguro": "${param.pagoinseguro}", "pagodirecto": "${param.pagodirecto}", "operacion": "${param.operacion}", "hash": "${datosPago.hash}" };

	$.ajax({
		type: 'post',
		url: '/tpv/TpvDetalle',
		data: JSON.stringify(info),
		contentType: "application/json; charset=utf-8",
		traditional: true,
		dataType: "json",
		success: function(data) {
			$("#versionTpv").val(data.version);
			$("#parametrosTpv").val(data.parametros);
			$("#firmaTpv").val(data.firma);
			toggle("formularioPago");
		},
		statusCode: {
			500: function() {
				alert('Error al preparar llamada al TPV');
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

function cambioMedioPago(medioPago) {
	//toogleList("listaOperacion");
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

