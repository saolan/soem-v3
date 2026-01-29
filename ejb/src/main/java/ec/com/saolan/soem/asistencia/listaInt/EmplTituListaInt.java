package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.EmplTitu;
import jakarta.ejb.Local;

@Local
public interface EmplTituListaInt {

	public List<EmplTitu> buscar(EmplTitu emplTitu, Integer pagina) throws Exception;

	public long contarRegistros(EmplTitu emplTitu) throws Exception;

	public void filasPagina(int filasPagina);
	
}
