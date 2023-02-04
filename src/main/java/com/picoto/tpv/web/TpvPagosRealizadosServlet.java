package com.picoto.tpv.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.picoto.tpv.dao.NRCDao;

@WebServlet("/TpvPagos")
public class TpvPagosRealizadosServlet extends HttpServlet {

	private static final long serialVersionUID = -6186219472085351504L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			NRCDao t = new NRCDao();
			List<String> pagos = t.getPagos("89890001K"); 
			req.setAttribute("pagos", pagos);
			req.getRequestDispatcher("/paginas/pagosRealizados.jsp").forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
