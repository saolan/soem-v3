package ec.com.tecnointel.soem.egreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import jakarta.ejb.Local;

@Local
public interface PersClieListaInt {

	public List<PersClie> buscarTodo(String columna) throws Exception;

	public List<PersClie> buscar(PersClie persClie, Integer pagina, Integer filas) throws Exception;

	public long contarRegistros(PersClie persClie) throws Exception;

	public void imprimir(PersClie persClie) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
