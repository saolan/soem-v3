package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.inventario.modelo.ProdTipoPlanCuen;
import jakarta.ejb.Local;

@Local
public interface ProdTipoPlanCuenListaInt {

	public List<ProdTipoPlanCuen> buscarTodo(String columna) throws Exception;

	public List<ProdTipoPlanCuen> buscar(ProdTipoPlanCuen prodTipoPlancuen, Integer pagina) throws Exception;

	public long contarRegistros(ProdTipoPlanCuen prodTipoPlancuen) throws Exception;

	public void imprimir(ProdTipoPlanCuen prodTipoPlancuen) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
