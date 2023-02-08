package com.picoto.tpv.dto.ext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

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
	public static final String OPERACION_PAYPAL = "p";

	public static final BigDecimal LIMITE_PAGO_INSEGURO = new BigDecimal("5.0");
	public static final BigDecimal CIEN = new BigDecimal("100");
	public static final BigDecimal UNIDAD = new BigDecimal("1");
	public static final BigDecimal CERO = new BigDecimal("0");

	
	private String nrc;
	private BigDecimal importeNrc;
	private String importeCentimos;
	private BigDecimal importeIngresar;
	private boolean pagoDirecto;
	private String concepto;
	private String nif;
	private String titular;
	private String direccionIp;
	private String idioma;

	private String operacion;
	private String mediopago;
	private String pagoinseguro;

	private String token;
	private boolean redireccion;

	private DatosTarjeta datosTarjeta;

	private String hash;

	public DatosPagoTpvRedsys() {
	}

	public DatosPagoTpvRedsys(String modelo, String ejercicio, String periodo, String nif, String importe,
			String idioma, String pagodirecto, String mediopago, String pagoinseguro, String ip) {

		concepto = String.format("PAGO DE IMPUESTOS. AUTOLIQUIDACION MODELO %s EJERCICIO %s PERIODO %s", modelo,
				ejercicio, periodo);
		this.nif = nif;
		this.titular = "89890001K-JOSE MIGUEL GOD. MUN. (SEDE AEAT)";
		this.idioma = idioma;
		direccionIp = ip;
		this.mediopago = mediopago;
		this.pagoinseguro = pagoinseguro;
		this.importeNrc = new BigDecimal(importe);

		if (esAfirmativo(pagodirecto)) {
			this.pagoDirecto = true;
			this.token = recuperaToken();
		} else {
			this.pagoDirecto = false;
		}

		String valorRnd = "" + (new Random().nextInt(10000000) + 100000000);
		nrc = modelo+ejercicio+"123456" + valorRnd;

		// Ojo los calculos tras haber seteado todo bien
		calcularImporteCentimosTpv(importe);
		calcularHash(modelo, ejercicio, periodo, nif, importe);
	}

	private void calcularImporteCentimosTpv(String importe) {
		// ImporteTPV = 100 * (Resultado/ (1 - TasaDescuento)) salvo en BIZUM.

		BigDecimal importeComision = null;
		if (isPagoBizum()) {
			importeComision = CERO;
		} else {
			importeComision = new BigDecimal(TASA_DESCUENTO);			
		}

		BigDecimal divisor = UNIDAD.subtract(importeComision);

		BigDecimal dividendo = new BigDecimal(importe);

		importeIngresar = dividendo.divide(divisor, 2, RoundingMode.CEILING);

		importeCentimos = "" + importeIngresar.multiply(CIEN).setScale(0, RoundingMode.CEILING);
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
	public BigDecimal getImporteIngresar() {
		return importeIngresar;
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
			return td.getToken(nif);
		} catch (DAOException e) {
			Utils.debug("No se ha encontrado el token para: " + nif);
			return null;
		}
	}

	@Override
	public String getTitular() {
		if (Utils.esVacio(titular)) { 
			return "";
		} else {
			return (titular.length() < 60) ? titular : titular.substring(0,60);
		}
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
				+ pagoDirecto + ", concepto=" + concepto + ", nif=" + nif + ", titular=" + titular + ", direccionIp=" + direccionIp
				+ ", idioma=" + idioma + ", operacion=" + operacion + ", mediopago=" + mediopago + ", pagoinseguro="
				+ pagoinseguro + ", token=" + token + ", redireccion=" + redireccion + ", datosTarjeta=" + datosTarjeta
				+ "]";
	}

	@Override
	public String getHash() {
		return hash;
	}

	private void calcularHash(String modelo, String ejercicio, String periodo, String nif, String importe) {
		StringBuilder sb = new StringBuilder();
		sb.append(modelo).append(ejercicio).append(periodo).append(nif).append(importe).append(Utils.getDia());
		
		hash = new Base64().encodeAsString(DigestUtils.sha256(sb.toString().getBytes()));
	}

	@Override
	public boolean hashValido(String hashParam) {
		return !Utils.esVacio(hashParam) && hashParam.equals(hash);
	}

	@Override
	public boolean noSuperaLimiteMaximo() {
		return importeIngresar.compareTo(LIMITE_PAGO_INSEGURO) < 0;
	}

	@Override
	public String getNif() {
		return this.nif;
	}
	
	@Override
	public BigDecimal getImporteNrc() {
		return importeNrc;
	}
	
}
