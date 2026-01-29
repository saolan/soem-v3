package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.DimmPlanCuen;
import jakarta.ejb.Local;

@Local
public interface DimmPlanCuenListaInt {

	public List<DimmPlanCuen> buscarTodo(String columna) throws Exception;

	public List<DimmPlanCuen> buscar(DimmPlanCuen prodTipoPlancuen, Integer pagina) throws Exception;

	public long contarRegistros(DimmPlanCuen prodTipoPlancuen) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
