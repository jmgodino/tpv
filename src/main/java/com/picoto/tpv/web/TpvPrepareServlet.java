package com.picoto.tpv.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.picoto.tpv.dao.NRCDao;
import com.picoto.tpv.dto.ext.DatosPagoTpvRedsys;
import com.picoto.tpv.exceptions.TPVException;
import com.picoto.tpv.service.ext.RedirectTpvRedsysImpl;
import com.picoto.tpv.util.Utils;

@WebServlet("/TpvDetalle")
public class TpvPrepareServlet extends HttpServlet {

	private static final long serialVersionUID = -6186219472085351504L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {

			Utils.debug("Tratando peticion");
			JSONTokener tokener = new JSONTokener(req.getInputStream());
			JSONObject root = new JSONObject(tokener);
			Utils.debug("JSON: "+root.toString());
			String modelo = root.getString("modelo");
			String ejercicio = root.getString("ejercicio");
			String periodo = root.getString("periodo");
			String nif = root.getString("nif");
			String importe = root.getString("importe");
			String idioma = root.getString("idioma");
			String ip = req.getRemoteAddr();
			String operacion = root.getString("operacion");
			String mediopago = root.getString("mediopago");
			String pagodirecto = root.getString("pagodirecto");
			String pagoinseguro = root.getString("pagoinseguro");
			String hash = root.getString("hash");

			RedirectTpvRedsysImpl client = new RedirectTpvRedsysImpl();
			DatosPagoTpvRedsys dp = new DatosPagoTpvRedsys(modelo, ejercicio, periodo, nif, importe, idioma,
					pagodirecto, mediopago, pagoinseguro, ip);
			dp.setOperacion(operacion);
			dp.setRedireccion(true);
			
			if (!dp.hashValido(hash)) {
				throw new TPVException("Error de seguridad. Datos modificados");
			}
			
			client.procesarPeticionTPV(dp, true);
			
			JSONObject salida = new JSONObject();
			salida.put("version", client.getVersion());
			salida.put("parametros", client.getPayload());
			salida.put("firma", client.getSignature());
			salida.put("error", "");
			
			resp.setContentType("application/json");
			resp.getWriter().println(salida.toString());
			resp.getWriter().close();
			

		} catch (Exception e) {
			e.printStackTrace();
			JSONObject salida = new JSONObject();
			salida.put("version", "");
			salida.put("parametros", "");
			salida.put("firma", "");
			salida.put("error", e.getMessage());
			
			resp.setStatus(500);
			resp.setContentType("application/json");
			resp.getWriter().println(salida.toString());
			resp.getWriter().close();

		}
	}

}
