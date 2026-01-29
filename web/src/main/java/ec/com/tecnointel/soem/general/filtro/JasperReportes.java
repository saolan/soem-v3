package ec.com.tecnointel.soem.general.filtro;

import java.io.IOException;

import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/jasperReportes/*")
public class JasperReportes implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		PersUsua persUsua = (PersUsua) ((HttpServletRequest) request).getSession().getAttribute("persUsua");

		if (persUsua == null) {

			((HttpServletResponse) response).sendRedirect("../accesoRestringido.xhtml");
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
