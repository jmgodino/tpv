package com.picoto.tpv.service.ext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.picoto.tpv.dto.DetallesPagoIntf;
import com.picoto.tpv.dto.ext.DatosPagoTpvRedsys;
import com.picoto.tpv.exceptions.TPVException;
import com.picoto.tpv.service.intf.PostTpvIntf;
import com.picoto.tpv.util.Utils;

public class PostTpvRedsysImpl extends RedirectTpvRedsysImpl implements PostTpvIntf {

	private static String URL_PAGO = "https://sis-t.redsys.es:25443/sis/rest/trataPeticionREST";
	
	private CloseableHttpClient httpClient;
	
	@Override
	public synchronized void open() {
		BasicHttpClientConnectionManager cm = new BasicHttpClientConnectionManager();
		// Sin Proxy
		DefaultProxyRoutePlanner router = null;

		httpClient = HttpClients.custom().setRoutePlanner(router).setConnectionManager(cm).build();
	}

	@Override
	public void close() throws TPVException {
		try {
			httpClient.close();
		} catch (Exception e) {
			throw new TPVException("Error cerrando conexion",e);
		}
	}
	
	@Override
	public DetallesPagoIntf post(DatosPagoTpvRedsys datosPago) throws TPVException {

		HttpPost postRequest = new HttpPost(URL_PAGO);

		try {

			procesarPeticionTPV(datosPago);

			String respuesta = doPost(postRequest, payload, signature);

			return tratarRespuestaTpv(respuesta);

		} finally {
			postRequest.releaseConnection();
		}

	}

	private String doPost(HttpPost postRequest, String params, String firma)
			throws TPVException {
		try {
			String respuesta = null;
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair("Ds_MerchantParameters", params));
			postParams.add(new BasicNameValuePair("Ds_SignatureVersion", "HMAC_SHA256_V1"));
			postParams.add(new BasicNameValuePair("Ds_Signature", firma));
			postRequest.setEntity(new UrlEncodedFormEntity(postParams));
			CloseableHttpResponse response = httpClient.execute(postRequest, HttpClientContext.create());
			Utils.debug(response.getStatusLine().toString());
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new IllegalArgumentException(String.format("El TPV no esta disponible %s", statusCode));
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				respuesta = EntityUtils.toString(entity);
			}
			response.close();
			return respuesta;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TPVException("Error procensado peticion de pago interna: "+e.getMessage());
		}
	}

	private DetallesPagoIntf tratarRespuestaTpv(String res)
			throws TPVException {
		Map<String, String> salida = getMap(res);
		
		
		if (res.indexOf("errorCode") > 0) {
			Utils.debug(res);
			throw new TPVException("Error al confirmar el pago");
		}
		
		String datos = salida.get("Ds_MerchantParameters");
		String firmaSalida = salida.get("Ds_Signature");
		
		return super.procesarRespuestaTPV(datos, firmaSalida);
	}

	private Map<String, String> getMap(String valor) {
		return Arrays.stream(valor.replace("{", "").replace("}", "").replace("\"", "").split(","))
				.map(arr -> arr.split(":")).collect(Collectors.toMap(x -> x[0], x -> x[1]));
	}


}