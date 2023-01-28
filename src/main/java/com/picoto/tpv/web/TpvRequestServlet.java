package com.picoto.tpv.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.picoto.tpv.dto.ext.DatosPagoTpvRedsys;
import com.picoto.tpv.service.ext.RedirectTpvRedsysImpl;

// http://localhost:8080/tpv/TpvPago usar tarjeta:
@WebServlet(name="TpvRequestServlet", urlPatterns = "/TpvPago")
public class TpvRequestServlet extends HttpServlet {

	private static final long serialVersionUID = -6186219472085351504L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/paginas/inicioPago.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			RedirectTpvRedsysImpl client = new RedirectTpvRedsysImpl();
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
			
			// Faltaría validar si los parametros obligatorios llegan y la calidad de los mismos
			
			DatosPagoTpvRedsys dp = new DatosPagoTpvRedsys(modelo, ejercicio, periodo, nif, importe, idioma, pagodirecto, mediopago, pagoinseguro, ip);
			dp.setOperacion(operacion);
			dp.setRedireccion(true);
			client.procesarPeticionTPV(dp);
			req.setAttribute("datosPago", dp);
			req.setAttribute("version", client.getVersion());
			req.setAttribute("parametros", client.getPayload());
			req.setAttribute("firma", client.getSignature());
			req.getRequestDispatcher("/paginas/formularioPeticionTpv.jsp").forward(req, resp);
		} catch (Exception e) {
			req.getRequestDispatcher("/paginas/error.jsp").forward(req, resp);
		}
	}
	
}
