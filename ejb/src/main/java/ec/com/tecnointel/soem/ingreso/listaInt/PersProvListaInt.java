package ec.com.tecnointel.soem.ingreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import jakarta.ejb.Local;

@Local
public interface PersProvListaInt {

	public List<PersProv> buscarTodo(String columna) throws Exception;

	public List<PersProv> buscar(PersProv persProv, Integer pagina) throws Exception;

	public long contarRegistros(PersProv persProv) throws Exception;

	public void imprimir(PersProv persProv) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
