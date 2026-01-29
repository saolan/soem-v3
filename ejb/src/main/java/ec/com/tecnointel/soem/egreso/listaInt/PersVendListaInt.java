package ec.com.tecnointel.soem.egreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import jakarta.ejb.Local;

@Local
public interface PersVendListaInt {

	public List<PersVend> buscarTodo(String columna) throws Exception;

	public List<PersVend> buscar(PersVend persVend, Integer pagina) throws Exception;

	public long contarRegistros(PersVend persVend) throws Exception;

	public void imprimir(PersVend persVend) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
