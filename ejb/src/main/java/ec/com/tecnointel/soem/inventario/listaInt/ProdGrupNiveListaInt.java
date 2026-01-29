package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.inventario.modelo.ProdGrupNive;
import jakarta.ejb.Local;

@Local
public interface ProdGrupNiveListaInt {

	public List<ProdGrupNive> buscarTodo(String columna) throws Exception;

	public List<ProdGrupNive> buscar(ProdGrupNive prodGrupNive, Integer pagina) throws Exception;

	public long contarRegistros(ProdGrupNive prodGrupNive) throws Exception;

	public void imprimir(ProdGrupNive prodGrupNive) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
