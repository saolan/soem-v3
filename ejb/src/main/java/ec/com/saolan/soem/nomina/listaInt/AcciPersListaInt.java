package ec.com.saolan.soem.nomina.listaInt;

import java.util.List;

import ec.com.saolan.soem.nomina.modelo.AcciPers;
import jakarta.ejb.Local;

@Local
public interface AcciPersListaInt {

	public List<AcciPers> buscar(AcciPers acciPers, Integer pagina) throws Exception;

	public long contarRegistros(AcciPers acciPers) throws Exception;

	public void filasPagina(int filasPagina);
	
}
