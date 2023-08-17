<%@page language="java" contentType="text/html; charset=UTF-8"%>

<html>
<head>
<script src="/tpv/js/tpv.js"></script>
<link rel="stylesheet" href="/tpv/css/tpv.css">
</head>
<body>
	<img src="/tpv/images/aeat.png" style="padding-left: 45px;">
	<ul class="no-bullets">
		<li>
			<h1>Datos del pago</h1>
		</li>
		<li>
			<form name="formu" action="/tpv/TpvInicio" method="POST">
				<ul class="no-bullets">

					<li><h2>Datos de la autoliquidación</h2></li>

					<li><label for="modelo">Modelo:</label><input type="text"
						name="modelo" value="100" maxlength="3" /></li>
					<li><label for="ejercicio">Ejercicio:</label><input
						type="text" name="ejercicio" value="2022" maxlength="4" /></li>
					<li><label for="periodo">Periodo:</label><input type="text"
						name="periodo" value="0A" maxlength="2" /></li>
					<li><label for="nif">NIF Ordenante:</label><input type="text"
						name="nif" value="89890001K" maxlength="9" /></li>
					<li><label for="importe">Importe en euros:</label><input
						type="text" name="importe" value="<%=com.picoto.tpv.util.Utils.getImporteAleatorio()%>" /><span
						class="textoAyuda">Por debajo de 5€ admitimos operaciones
							sin necesidad de EMV 3DS. No disponible en autenticación. Las operaciones de 0,01€ se anulan automáticamente</span></li>

					<li><h2>Opciones de idioma</h2></li>

					<li><label for="idioma">Idioma:</label><select name="idioma"><option
								value="1">Castellano</option>
							<option value="1">Castellano</option>
							<option value="2">Ingles</option>
							<option value="3">Catalan</option></select></li>

					<li><h2>Opciones de pago</h2></li>

					<li class="liModoTabla"><label><input type="radio" name="mediopago" class="modoTabla"
							onChange="cambioMedioPago(this)"
							value="<%=com.picoto.tpv.dto.ext.DatosPagoTpvRedsys.OPERACION_TARJETA%>"
							checked> <img src="/tpv/images/cards.jpeg" alt="Tarjeta" class="modoTabla"
							width="150px" height="30px"> </label></li>
					<li class="liModoTabla"><label><input type="radio" name="mediopago" class="modoTabla"
						onChange="cambioMedioPago(this)"
						value="<%=com.picoto.tpv.dto.ext.DatosPagoTpvRedsys.OPERACION_BIZUM%>">
						<img src="/tpv/images/bizum.png" alt="Bizum" class="modoTabla"
						width="150px" height="40px"> </label> </label> <span class="textoAyuda">El
							pago con tarjeta tiene comisión. El pago por Bizum no tiene comisión</span></li>
					<li id="listaOperacion"><label for="operacion">Operacion:</label><select
						name="operacion"><option value="0">Autorizacion</option>
							<option
								value="<%=com.picoto.tpv.dto.ext.DatosPagoTpvRedsys.OPERACION_PREAUTORIZACION%>">Autenticación</option></select><span
						class="textoAyuda">El uso de autenticación, implica una
							primera operación de autenticación de los datos de pago, y una
							segunda operación de confirmación por parte de la AEAT</span></li>

					<li id="listaPagoSeguro"><label for="pagoinseguro">Admitir
							Pago No Seguro:</label> <input type="radio" name="pagoinseguro" value="S">Si</input>
						<input type="radio" name="pagoinseguro" value="N" checked>No</input>
						<span class="textoAyuda">Esta opción le permitirá realizar
							pagos usando tan solo datos de su tarjeta, sin necesidad de
							comprobaciones adicionales. Menor nivel de seguridad al no usar
							EMV 3DS</span></li>

					<li id="listaPagoDirecto"><label for="pagodirecto">Realizar/Solicitar
							Pago Directo:</label> <input type="radio" name="pagodirecto" value="S">Si</input>
						<input type="radio" name="pagodirecto" value="N" checked>No</input>
						<span class="textoAyuda">Esta opción le permitirá realizar
							próximos pagos sin tener que introducir los datos de su tarjeta.
							Mayor comodidad</span></li>

					<li id="listaPagoRest"><label for="pagorest">Realizar
							Pago desde la Sede AEAT</label> <input type="radio" name="pagorest"
						value="S" onChange="cambioPagoRest()">Si</input> <input
						type="radio" name="pagorest" value="N" onChange="cambioPagoRest()"
						checked>No</input> <span class="textoAyuda">Esta opción le
							permitira pagar desde la AEAT con datos de tarjeta sin tener que
							acceder a una web externa (y gestiones con PCI/DSS)</span></li>

					<li>
						<ul id="datosTarjeta" class="no-bullets"
							style="visibility: hidden;">
							<li><label for="pan">N. Tarjeta:</label><input type="text"
								name="pan" value="4548810000000003" /></li>
							<li><label for="cad">F. Caducidad:</label><input type="text"
								name="cad" value="4912" /></li>
							<li><label for="cvv">CVV2:</label><input type="text"
								name="cvv" value="123" /></li>
							<li><span class="textoAyuda2">ESTA OPCIÓN NO ES
									DESEABLE POR TENER QUE MANEJAR DATOS DE TARJETA. PROBLEMAS
									(PCI/DSS)</span></li>
						</ul>
					</li>

					<li><input type="submit" value="Iniciar Pago" /></li>

					<ul>
			</form>

		</li>
	</ul>

</body>
</html>