package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import jakarta.ejb.Local;

@Local
public interface BodegaListaInt {

	public List<Bodega> buscarTodo(String columna) throws Exception;

	public List<Bodega> buscar(Bodega bodega, Integer pagina) throws Exception;

	public long contarRegistros(Bodega bodega) throws Exception;

	public void imprimir(Bodega bodega) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
