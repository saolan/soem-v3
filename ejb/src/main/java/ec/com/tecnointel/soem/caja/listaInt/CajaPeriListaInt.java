package ec.com.tecnointel.soem.caja.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import jakarta.ejb.Local;

@Local
public interface CajaPeriListaInt {

	public List<CajaPeri> buscarTodo(String columna) throws Exception;

	public List<CajaPeri> buscar(CajaPeri cajaPeri, Integer pagina) throws Exception;

	public long contarRegistros(CajaPeri cajaPeri) throws Exception;

	public void imprimir(CajaPeri cajaPeri) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
