package com.picoto.tpv.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.picoto.tpv.dto.DetallesPagoIntf;
import com.picoto.tpv.service.ext.RedirectTpvRedsysImpl;
import com.picoto.tpv.service.intf.RedirectTpvIntf;

@WebServlet(name="TpvResponseServlet", urlPatterns = "/TpvRetorno")
public class TpvResponseServlet extends HttpServlet {

	private static final long serialVersionUID = -6186219472085351504L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			RedirectTpvIntf client = new RedirectTpvRedsysImpl();
			DetallesPagoIntf detalles = client.procesarRespuestaTPV(req.getParameter("Ds_MerchantParameters"),req.getParameter("Ds_Signature"));
			
			// TODO: En este punto debemos integrar con Pasarela para actualizar tabla ZUJAR y consolidar el NRC, vía el servicio existente
			
			req.setAttribute("datosPago", detalles);
			if (detalles.getError() == null) {
				req.getRequestDispatcher("/paginas/pagoFinalizado.jsp").forward(req, resp);
			} else {
				req.getRequestDispatcher("/paginas/error.jsp").forward(req, resp);
			}
	}

	
	
}
