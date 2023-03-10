package com.picoto.tpv.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.picoto.tpv.dto.DatosTarjeta;
import com.picoto.tpv.dto.DetallesPagoIntf;
import com.picoto.tpv.dto.ext.DatosPagoTpvRedsys;
import com.picoto.tpv.service.ext.PostTpvRedsysImpl;
import com.picoto.tpv.util.Utils;

// http://localhost:8080/tpv/TpvInicio
@WebServlet("/TpvInicio")
public class TpvRequestServlet extends HttpServlet {

	private static final long serialVersionUID = -6186219472085351504L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/paginas/inicioPago.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String modelo = req.getParameter("modelo");
			String ejercicio = req.getParameter("ejercicio");
			String periodo = req.getParameter("periodo");
			String nif = req.getParameter("nif");
			String importe = req.getParameter("importe");
			String idioma = req.getParameter("idioma");
			String ip = req.getRemoteAddr();
			String operacion = req.getParameter("operacion");
			String mediopago = req.getParameter("mediopago");
			String pagodirecto = req.getParameter("pagodirecto");
			String pagoinseguro = req.getParameter("pagoinseguro");
			String pagorest = req.getParameter("pagorest");
			
			// Faltaría validar si los parametros obligatorios llegan y la calidad de los mismos

			DatosPagoTpvRedsys dp = new DatosPagoTpvRedsys(modelo, ejercicio, periodo, nif, importe, idioma, pagodirecto, mediopago, pagoinseguro, ip);
			req.setAttribute("datosPago",dp);
			
			if (!Utils.opcionActivada(pagorest)) {
				req.getRequestDispatcher("/paginas/detallesPago.jsp").forward(req, resp);
			} else {
				PostTpvRedsysImpl client = new PostTpvRedsysImpl();
				dp.setOperacion(operacion);
				dp.setRedireccion(false);
				
				String pan = req.getParameter("pan");
				String caducidad = req.getParameter("cad");
				String cvv = req.getParameter("cvv");
				dp.setDatosTarjeta(new DatosTarjeta(pan, caducidad, cvv));
				
				client.open();
				DetallesPagoIntf detallesPago = client.post(dp, true);
				client.close();
				req.setAttribute("datosPago", detallesPago);
				if (detallesPago.getError() == null) {
					req.getRequestDispatcher("/paginas/finPago.jsp").forward(req, resp);
				} else {
					req.getRequestDispatcher("/paginas/error.jsp").forward(req, resp);
				}

			}
		} catch (Exception e) {
			req.getRequestDispatcher("/paginas/error.jsp").forward(req, resp);
		}
	}
	
}
