package ec.com.tecnointel.soem.caja.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import jakarta.ejb.Local;

@Local
public interface PersCajeListaInt {

	public List<PersCaje> buscarTodo(String columna) throws Exception;

	public List<PersCaje> buscar(PersCaje persCaje, Integer pagina) throws Exception;

	public long contarRegistros(PersCaje persCaje) throws Exception;

	public void imprimir(PersCaje persCaje) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
