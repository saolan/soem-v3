package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.CentCost;
import jakarta.ejb.Local;

@Local
public interface CentCostListaInt {

	public List<CentCost> buscar(CentCost centCost, Integer pagina) throws Exception;

	public long contarRegistros(CentCost centCost) throws Exception;

	public void filasPagina(int filasPagina);
	
}
