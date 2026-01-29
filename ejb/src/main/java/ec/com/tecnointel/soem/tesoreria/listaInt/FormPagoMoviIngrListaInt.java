package ec.com.tecnointel.soem.tesoreria.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Local;

@Local
public interface FormPagoMoviIngrListaInt {

	public void filasPagina(int filasPagina);
	
	
	public List<FormPagoMoviIngr> buscarTodo(String columna) throws Exception;

	public List<FormPagoMoviIngr> buscar(FormPagoMoviIngr formPagoMoviIngr, Integer pagina) throws Exception;

	public long contarRegistros(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public void imprimir(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public List<FormPagoMoviIngr> buscarTransaccion(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
