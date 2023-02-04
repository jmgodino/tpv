package com.picoto.tpv.service.ext;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.io.IOUtils;

import com.picoto.tpv.dao.NRCDao;
import com.picoto.tpv.dao.TokenDao;
import com.picoto.tpv.dto.DatosPagoTpvIntf;
import com.picoto.tpv.dto.DatosTarjeta;
import com.picoto.tpv.dto.DetallesPagoIntf;
import com.picoto.tpv.dto.ext.DatosPagoTpvRedsys;
import com.picoto.tpv.dto.ext.DetallesPagoRedsys;
import com.picoto.tpv.exceptions.TPVException;
import com.picoto.tpv.service.intf.PostTpvIntf;
import com.picoto.tpv.service.intf.RedirectTpvIntf;
import com.picoto.tpv.util.Utils;

import sis.redsys.api.ApiMacSha256;

public class RedirectTpvRedsysImpl implements RedirectTpvIntf {

	protected static final String COMERCIO_NOMBRE = "Comercio";

	protected static final String DOMINIO_URL_RETORNO = "http://localhost:8080";

	protected static final String URL_RETORNO_PAGO_KO = "/tpv/TpvRetorno";

	protected static final String URL_RETORNO_PAGO_OK = "/tpv/TpvRetorno";

	protected static final String TERMINAL_COMERCIO = "048";

	protected static final String MONEDA_EURO = "978";

	protected static final String CODIGO_COMERCIO = "999008881";

	protected static final String CLAVE_COMERCIO = "sq7HjrUOBfKmC576ILgskD5srU870gJ7";

	protected String payload;

	protected String signature;

	protected ApiMacSha256 apiMacSha256 = new ApiMacSha256();

	public String getPayload() {
		return payload;
	}

	public String getSignature() {
		return signature;
	}

	public String getVersion() {
		return "HMAC_SHA256_V1";
	}

	@Override
	public void procesarPeticionTPV(DatosPagoTpvIntf datosPago) throws TPVException {		

		try {
			Utils.debug(datosPago.toString());
			prepareRequest(datosPago);
			payload = apiMacSha256.createMerchantParameters();
			Utils.debug("Peticion:" + apiMacSha256.decodeMerchantParameters(payload));
			signature = apiMacSha256.createMerchantSignature(CLAVE_COMERCIO);
		} catch (Exception e) {
			throw new TPVException("Error procensado peticion de pago: "+e.getMessage());
		}
	}

	protected void prepareRequest(DatosPagoTpvIntf datosPago) {
		// Datos fijos
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTCODE", CODIGO_COMERCIO);
		apiMacSha256.setParameter("DS_MERCHANT_CURRENCY", MONEDA_EURO);
		apiMacSha256.setParameter("DS_MERCHANT_TRANSACTIONTYPE", datosPago.getOperacion());

		apiMacSha256.setParameter("DS_MERCHANT_TERMINAL", TERMINAL_COMERCIO);

		apiMacSha256.setParameter("DS_MERCHANT_PAYMETHODS", datosPago.getMediopago());

		// Ojo, esto es lo contrario al EMV3DS. Pago sin autenticación adicional, solo
		// con datos de tarjeta
		if (datosPago.isPagoInseguro()) {
			//apiMacSha256.setParameter("DS_MERCHANT_DIRECTPAYMENT", "true");
			apiMacSha256.setParameter("DS_MERCHANT_EXCEP_SCA", "COR");
		}

		// Solicitar Pago One Click
		if (datosPago.isPagoTarjeta() && datosPago.isPagoDirecto()) {
			String token = datosPago.getToken();
			// Si no hay token, solo solicitamos el pago directo, si lo hay lo mandamos
			if (Utils.esVacio(token)) {
				apiMacSha256.setParameter("DS_MERCHANT_IDENTIFIER", "REQUIRED");
			} else {
				// Si hay token registrado lo usamos
				apiMacSha256.setParameter("DS_MERCHANT_IDENTIFIER", token);
			}
		}

		// Datos del titular:
		apiMacSha256.setParameter("DS_MERCHANT_PRODUCTDESCRIPTION", datosPago.getConcepto());
		apiMacSha256.setParameter("DS_MERCHANT_TITULAR", datosPago.getTitular());

		// Datos del pedido:
		apiMacSha256.setParameter("DS_MERCHANT_AMOUNT", datosPago.getImporteCentimos()); // En céntimos de euro

		apiMacSha256.setParameter("DS_MERCHANT_ORDER", datosPago.getNrc()); // NRC
		apiMacSha256.setParameter("DS_MERCHANT_TAX_REFERENCE", ""); // Aqui podría ir el NRC también si se habilita

		// Datos para el retorno, solo si Redirección
		// apiMacSha256.setParameter("DS_MERCHANT_MERCHANTURL",
		// "https://www1.agenciatributaria.gob.es/pago/rest");
		if (datosPago.isRedireccion()) {
			apiMacSha256.setParameter("DS_MERCHANT_URLOK", DOMINIO_URL_RETORNO + URL_RETORNO_PAGO_OK);
			apiMacSha256.setParameter("DS_MERCHANT_URLKO", DOMINIO_URL_RETORNO + URL_RETORNO_PAGO_KO);

			//String datosSeguridad = getDatoSeguridad();
			apiMacSha256.setParameter("DS_MERCHANT_EMV3DS","");
			
			// Forzamos no uso de EMV3DS si el importe es bajo.
			if (datosPago.noSuperaLimiteMaximo()) {
				apiMacSha256.setParameter("DS_MERCHANT_EXCEP_SCA", "LWV");
			}
		}

		apiMacSha256.setParameter("DS_MERCHANT_CLIENTIP", datosPago.getDireccionIp());
		apiMacSha256.setParameter("DS_MERCHANT_CONSUMERLANGUAGE", datosPago.getIdioma());
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTDATA",
				(datosPago.isPreautorizacion() ? DetallesPagoRedsys.TEXTO_CONFIRMAR_PREAUTORIZACION : DetallesPagoRedsys.TEXTO_AUTORIZADO_PREAUTORIZACION) + datosPago.getTitular());
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTNAME", COMERCIO_NOMBRE);
		
		// Solo para el caso de peticion REST de pago, que no vamos a usar casi con total seguridad
		DatosTarjeta datosTarjeta = datosPago.getDatosTarjeta();
		if (datosTarjeta != null) {
			apiMacSha256.setParameter("DS_MERCHANT_DIRECTPAYMENT", "true");
			apiMacSha256.setParameter("DS_MERCHANT_PAN", datosTarjeta.getPan());
			apiMacSha256.setParameter("DS_MERCHANT_EXPIRYDATE", datosTarjeta.getCaducidad());
			apiMacSha256.setParameter("DS_MERCHANT_CVV2", datosTarjeta.getCvv());

		}

	}

	private String getDatoSeguridad() throws TPVException {
		try {
			return String.format(IOUtils.resourceToString("/emv3ds.txt",StandardCharsets.UTF_8),724,"MA","MA","Calle Encanto","5 Portal 1","28100", "Jose Miguel GM","test@gmail.com",034,666666666);
		} catch (Exception e) {
			e.printStackTrace();
			throw new TPVException("Error al recuperar datos de seguridad EMV3DS");
		}
	}

	@Override
	public DetallesPagoIntf procesarRespuestaTPV(String datos, String firmaRespuesta) throws TPVException {

		DetallesPagoIntf detallespago = new DetallesPagoRedsys();
		DetallesPagoIntf detallespagoConfirmacion = null;

		try {

			debugDatosEnClaro(datos, apiMacSha256);

			validarFirma(datos, firmaRespuesta);

			if (isCodigoRespuestaAfirmativo()) {

				Utils.debug("Codigo respuesta afirmativo");
				capturarDatosBasicosPago(detallespago);
				
				if (detallespago.necesitaConfirmacion()) {
					Utils.debug("**************** Confirmando operacion de preautorizacion ****************");
					detallespagoConfirmacion = confirmarOperacion(detallespago.getImporteTpv(), detallespago.getNrc(), detallespago.getNif());
					if (detallespagoConfirmacion.isConfirmacionCorrecta()) {
						// Ver si esta es la forma adecuada
						//capturarDetallesPago(detallespagoConfirmacion);
						capturarDetallesPago(detallespago);
						// validamos y consolidamos el NRC al confirmar el pago
						validarNrc(detallespago.getNrc());
					} else {
						throw new TPVException("Error al confirmar la preautorizacion: "+detallespagoConfirmacion.getError());
					}
				} else {
					capturarDetallesPago(detallespago);
					// validamos y consolidamos el NRC al autorizar el pago					
					validarNrc(detallespago.getNrc());
				}
				Utils.debug(detallespago.toString());
				
				gestionarPagoDirectoRespuesta(detallespago);
				
			} else {
				String codResp = getCodigoRespuesta();
				detallespago.setError("Error en la operación. " + codResp);
				detallespago.setCodigoError(codResp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			detallespago.setError("Error en la operación. " + e.getMessage());
			detallespago.setCodigoError(e.getMessage());
		}

		return detallespago;
	}

	private void validarNrc(String nrc) {
		NRCDao dao = new NRCDao();
		String nrcValidado = null;
		
		try {
			nrcValidado = dao.getNRC(nrc);
		} catch (Exception e) {
			throw new TPVException("El NRC no consta en los sistemas de la AEAT. Operación rechazada");
		}
		boolean consolidado = dao.isConsolidado(nrc);
		if (consolidado) {
			throw new TPVException("El NRC ya consta como ingresado. Operacion denegada");
		} else {
			dao.consolidarNRC(nrcValidado);
			Utils.debug("NRC consolidado");
		}
	}

	private void capturarDatosBasicosPago(DetallesPagoIntf detallesPago) throws UnsupportedEncodingException {
		// Datos basicos
		String nrc = getCampo("Ds_Order");
		String importeTpv = getCampo("Ds_Amount");
		String detalles = URLDecoder.decode(getCampo("Ds_MerchantData"), "UTF-8");
		detallesPago.setNrc(nrc);
		detallesPago.setImporteTpv(importeTpv);
		detallesPago.setDetalles(detalles);
		
	}

	private void capturarDetallesPago(DetallesPagoIntf detallesPago) {
		detallesPago.setPagoSeguro(getCampo("Ds_SecurePayment"));
		// Ojo, en REST "pago seguro" es complicado
		detallesPago.setMetodoPago(getCampo("Ds_ProcessedPayMethod"));
		// 3: Tradicional Mundial, 68: bizum

		// Solo datos de tarjeta, si no se trata de Bizum
		if (detallesPago.isPagoBizum()) {
			detallesPago.setPaisTarjeta("724");
			detallesPago.setMarcaTarjeta("BIZUM");
		} else {
			detallesPago.setPaisTarjeta(getCampo("Ds_Card_Country"));
			detallesPago.setMarcaTarjeta(getCampo("Ds_Card_Brand"));
			
			detallesPago.setTipoTarjeta(getCampo("Ds_Card_Type"));
			detallesPago.setTarjetaPSD2(getCampo("Ds_Card_Psd2"));
		}

		// En este escenario, todo ha ido bien
		detallesPago.setError(null);
		detallesPago.setCodigoError("0000");

	}

	private void validarFirma(String datos, String firmaRespuesta) throws TPVException {
		try {
			String firmaCalculada = apiMacSha256.createMerchantSignatureNotif(CLAVE_COMERCIO, datos);
			if (firmaCalculada.equals(firmaRespuesta)) {
				Utils.debug("FIRMA OK. Realizar tareas en el servidor");
			} else {
				Utils.debug(String.format("FIRMA KO. Error, firma inválida %s vs %s", firmaRespuesta, firmaCalculada));
				throw new TPVException("La firma no seha podido validar. Operación descartada");
			} 
		} catch (Exception e) {
			throw new TPVException("Error al validar la firma. "+e.getMessage());
		}
	}

	private void debugDatosEnClaro(String datos, ApiMacSha256 apiMacSha256) throws UnsupportedEncodingException {
		String decodec = apiMacSha256.decodeMerchantParameters(datos);
		Utils.debug("Datos salida en claro: " + decodec);
	}

	private boolean isCodigoRespuestaAfirmativo() {
		String codResp = getCodigoRespuesta();
		int codRespNumerico = new Integer(codResp);
		Utils.debug("codigo respuesta: " + codRespNumerico);
		// Ojo que no solo 0000 es bueno lo es de 0000 hasta 0099 para autorizaciones

		return 0 <= codRespNumerico && codRespNumerico < 100;
	}

	private String getCodigoRespuesta() {
		return getCampo("Ds_Response");
	}

	private void gestionarPagoDirectoRespuesta(DetallesPagoIntf detallesPago) throws ParseException {
		// Token para pago directo. Vemos si existe y hay que registrarlo
		String nif = detallesPago.getNif();
		Optional<String> optToken = getCampoOpcional("Ds_Merchant_Identifier");
		if (optToken.isPresent()) {
			String token = optToken.get();
			detallesPago.setToken(token);
			Utils.debug("Token y NIF recuperado: " + token + ", " + nif);
			// Si en la respuesta tenemos token y NIF del ordenante
			if (!Utils.esVacio(token) && !Utils.esVacio(nif)) {
				// Solo si hay fecha de expiración, registramos un nuevo token
				Optional<String> optFecha = getCampoOpcional("Ds_ExpiryDate");
				if (optFecha.isPresent()) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					// fecha en formato yyMM -> yyyyMMdd
					String fecha = "20" + optFecha.get() + "01";
					// Ojo nos falta el NIF: Sacar de Ds_MerchantData
					registrarToken(nif, token, sdf.parse(fecha));
				}
			}
		}
	}

	private void registrarToken(String nif, String token, Date fecha) {
		TokenDao t = new TokenDao();
		t.registrarToken(nif, token, fecha);
	}

	protected DetallesPagoIntf confirmarOperacion(String importeTpv, String nrc, String titular) throws Exception {
		PostTpvIntf client = new PostTpvRedsysImpl();
		DatosPagoTpvRedsys datosPago = new DatosPagoTpvRedsys();

		// Preparamos operación de confirmación
		datosPago.setImporteCentimos(importeTpv);
		datosPago.setNrc(nrc);
		datosPago.setTitular(titular);
		datosPago.setOperacion(DatosPagoTpvRedsys.OPERACION_CONFIRMACION);
		datosPago.setRedireccion(false);

		client.open();
		DetallesPagoIntf detallesPago = client.post(datosPago);
		client.close();

		return detallesPago;

	}

	protected Optional<String> getCampoOpcional(String nombre) {
		try {
			return Optional.of(apiMacSha256.getParameter(nombre));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	protected String getCampo(String nombre) {
		Optional<String> opcional = getCampoOpcional(nombre);
		return opcional.orElse(null);
	}
}