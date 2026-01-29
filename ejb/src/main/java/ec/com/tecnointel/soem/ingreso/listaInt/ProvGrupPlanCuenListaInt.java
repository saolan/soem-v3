package ec.com.tecnointel.soem.ingreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.ProvGrupPlanCuen;
import jakarta.ejb.Local;

@Local
public interface ProvGrupPlanCuenListaInt {

	public List<ProvGrupPlanCuen> buscarTodo(String columna) throws Exception;

	public List<ProvGrupPlanCuen> buscar(ProvGrupPlanCuen provGrupPlanCuen, Integer pagina) throws Exception;

	public long contarRegistros(ProvGrupPlanCuen provGrupPlanCuen) throws Exception;

	public void imprimir(ProvGrupPlanCuen provGrupPlanCuen) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
