package ec.com.tecnointel.soem.contabilidad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import jakarta.ejb.Local;

@Local
public interface PlanCuenListaInt {

	public List<PlanCuen> buscarTodo(String columna) throws Exception;

	public List<PlanCuen> buscar(PlanCuen planCuen, Integer pagina) throws Exception;

	public long contarRegistros(PlanCuen planCuen) throws Exception;

	public void imprimir(PlanCuen planCuen) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
