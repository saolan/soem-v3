package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import jakarta.ejb.Local;

@Local
public interface FormPagoPlanCuenListaInt {

	public List<FormPagoPlanCuen> buscarTodo(String columna) throws Exception;

	public List<FormPagoPlanCuen> buscar(FormPagoPlanCuen formPagoPlanCuen, Integer pagina) throws Exception;

	public long contarRegistros(FormPagoPlanCuen formPagoPlanCuen) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
