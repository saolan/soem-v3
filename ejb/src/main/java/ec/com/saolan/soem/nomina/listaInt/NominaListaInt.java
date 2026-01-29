package ec.com.saolan.soem.nomina.listaInt;

import java.util.List;

import ec.com.saolan.soem.nomina.modelo.Nomina;
import jakarta.ejb.Local;

@Local
public interface NominaListaInt {

	public List<Nomina> buscar(Nomina nomina, Integer pagina) throws Exception;

	public long contarRegistros(Nomina nomina) throws Exception;

	public void filasPagina(int filasPagina);
	
}
