package ec.com.tecnointel.soem.egreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.ClieGrupPlanCuen;
import jakarta.ejb.Local;

@Local
public interface ClieGrupPlanCuenListaInt {

	public List<ClieGrupPlanCuen> buscarTodo(String columna) throws Exception;

	public List<ClieGrupPlanCuen> buscar(ClieGrupPlanCuen clieGrupPlanCuen, Integer pagina) throws Exception;

	public long contarRegistros(ClieGrupPlanCuen clieGrupPlanCuen) throws Exception;

	public void imprimir(ClieGrupPlanCuen clieGrupPlanCuen) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
