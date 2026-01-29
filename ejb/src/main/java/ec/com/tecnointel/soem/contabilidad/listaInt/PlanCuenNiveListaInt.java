package ec.com.tecnointel.soem.contabilidad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuenNive;
import jakarta.ejb.Local;

@Local
public interface PlanCuenNiveListaInt {

	public List<PlanCuenNive> buscarTodo(String columna) throws Exception;

	public List<PlanCuenNive> buscar(PlanCuenNive planCuenNive, Integer pagina) throws Exception;

	public long contarRegistros(PlanCuenNive planCuenNive) throws Exception;

	public void imprimir(PlanCuenNive planCuenNive) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
