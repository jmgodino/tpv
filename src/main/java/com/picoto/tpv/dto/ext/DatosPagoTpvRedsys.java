package com.picoto.tpv.dto.ext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import com.picoto.tpv.dao.TokenDao;
import com.picoto.tpv.dto.DatosPagoTpvIntf;
import com.picoto.tpv.dto.DatosTarjeta;
import com.picoto.tpv.exceptions.DAOException;
import com.picoto.tpv.util.Utils;

public class DatosPagoTpvRedsys implements DatosPagoTpvIntf {

	private static final String TASA_DESCUENTO = "0.0031";

	public static final String OPERACION_AUTORIZACION = "0";
	public static final String OPERACION_PREAUTORIZACION = "7";
	public static final String OPERACION_CONFIRMACION = "8";

	public static final String OPERACION_TARJETA = "C";
	public static final String OPERACION_BIZUM = "z";

	private String nrc;
	private String importeCentimos;
	private boolean pagoDirecto;
	private String concepto;
	private String titular;
	private String direccionIp;
	private String idioma;

	private String operacion;
	private String mediopago;
	private String pagoinseguro;

	private String token;
	private boolean redireccion;

	private DatosTarjeta datosTarjeta;

	public DatosPagoTpvRedsys() {
	}

	public DatosPagoTpvRedsys(String modelo, String ejercicio, String periodo, String nif, String importe,
			String idioma, String pagodirecto, String mediopago, String pagoinseguro, String ip) {

		calcularImporteCentimosTpv(importe);

		concepto = String.format("PAGO DE IMPUESTOS. AUTOLIQUIDACION MODELO %s EJERCICIO %s PERIODO %s", modelo,
				ejercicio, periodo);
		titular = nif;
		this.idioma = idioma;
		direccionIp = ip;
		this.mediopago = mediopago;
		this.pagoinseguro = pagoinseguro;

		if (esAfirmativo(pagodirecto)) {
			this.pagoDirecto = true;
			this.token = recuperaToken();
		} else {
			this.pagoDirecto = false;
		}

		String valorRnd = "" + (new Random().nextInt(10000000) + 100000000);
		nrc = "1001234567890" + valorRnd;

	}

	private void calcularImporteCentimosTpv(String importe) {
		// ImporteTPV = 100 * (Resultado/ (1 - TasaDescuento))

		BigDecimal cien = new BigDecimal("100");
		BigDecimal unidad = new BigDecimal("1");
		BigDecimal importeComision = new BigDecimal(TASA_DESCUENTO);
		BigDecimal divisor = unidad.subtract(importeComision);

		BigDecimal dividendo = new BigDecimal(importe);

		BigDecimal importeIngresar = dividendo.divide(divisor, 2, RoundingMode.CEILING);

		importeCentimos = "" + importeIngresar.multiply(cien).setScale(0, RoundingMode.CEILING);
	}

	private boolean esAfirmativo(String str) {
		return str != null && "S".equalsIgnoreCase(str);
	}

	@Override
	public String getNrc() {
		return nrc;
	}

	@Override
	public String getImporteCentimos() {
		return importeCentimos;
	}

	@Override
	public boolean isPagoDirecto() {
		return pagoDirecto;
	}

	@Override
	public String getConcepto() {
		return concepto;
	}

	public String recuperaToken() {
		try {
			TokenDao td = new TokenDao();
			return td.getToken(titular);
		} catch (DAOException e) {
			Utils.debug("No se ha encontrado el token para: " + titular);
			return null;
		}
	}

	@Override
	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	@Override
	public String getDireccionIp() {
		return direccionIp;
	}

	@Override
	public String getIdioma() {
		return idioma;
	}

	@Override
	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public void setImporteCentimos(String importeCentimos) {
		this.importeCentimos = importeCentimos;
	}

	@Override
	public String getMediopago() {
		return mediopago;
	}

	@Override
	public boolean isPagoBizum() {
		return OPERACION_BIZUM.equals(mediopago);
	}

	@Override
	public boolean isPagoTarjeta() {
		return OPERACION_TARJETA.equals(mediopago);
	}

	@Override
	public boolean isPreautorizacion() {
		return OPERACION_PREAUTORIZACION.equals(operacion);
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public boolean isPagoInseguro() {
		return esAfirmativo(pagoinseguro);
	}

	@Override
	public boolean isRedireccion() {
		return redireccion;
	}

	public void setRedireccion(boolean redireccion) {
		this.redireccion = redireccion;
	}

	public void setDatosTarjeta(DatosTarjeta datosTarjeta) {
		this.datosTarjeta = datosTarjeta;
	}

	@Override
	public DatosTarjeta getDatosTarjeta() {
		return datosTarjeta;
	}

	@Override
	public String toString() {
		return "DatosPagoTpvRedsys [nrc=" + nrc + ", importeCentimos=" + importeCentimos + ", pagoDirecto="
				+ pagoDirecto + ", concepto=" + concepto + ", titular=" + titular + ", direccionIp=" + direccionIp
				+ ", idioma=" + idioma + ", operacion=" + operacion + ", mediopago=" + mediopago + ", pagoinseguro="
				+ pagoinseguro + ", token=" + token + ", redireccion=" + redireccion + ", datosTarjeta=" + datosTarjeta
				+ "]";
	}



	
}
