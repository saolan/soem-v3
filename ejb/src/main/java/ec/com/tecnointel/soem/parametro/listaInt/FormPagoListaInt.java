package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Local;

@Local
public interface FormPagoListaInt {

	public List<FormPago> buscarTodo(String columna) throws Exception;

	public List<FormPago> buscar(FormPago formPago, Integer pagina) throws Exception;

	public long contarRegistros(FormPago formPago) throws Exception;

	public void imprimir(FormPago formPago) throws Exception;
	
	public List<FormPago> filtrarFormPagos(List<FormPago> formPagos, PersUsua persUsuaSesion, List<RolFormPago> rolFormPagos) throws Exception;

	public void filasPagina(int filasPagina);

	public List<FormPago> filtrarFormPagoVentas(FormPagoMoviEgre formPagoMoviEgre, List<FormPago> formPagos,
			PersUsua persUsuaSesion, List<RolFormPago> rolFormPagos) throws Exception;

	public List<FormPago> filtrarFormPagoCompras(FormPagoMoviIngr formPagoMoviIngr, List<FormPago> formPagos,
			PersUsua persUsuaSesion, List<RolFormPago> rolFormPagos);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
