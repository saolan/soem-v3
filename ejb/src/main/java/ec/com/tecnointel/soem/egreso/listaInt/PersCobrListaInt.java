package ec.com.tecnointel.soem.egreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import jakarta.ejb.Local;

@Local
public interface PersCobrListaInt {

	public List<PersCobr> buscarTodo(String columna) throws Exception;

	public List<PersCobr> buscar(PersCobr persCobr, Integer pagina) throws Exception;

	public long contarRegistros(PersCobr persCobr) throws Exception;

	public void imprimir(PersCobr persCobr) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
