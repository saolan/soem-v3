package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.EmplCargVaca;
import jakarta.ejb.Local;

@Local
public interface EmplCargVacaListaInt {

	public List<EmplCargVaca> buscar(EmplCargVaca emplCargVaca, Integer pagina) throws Exception;

	public long contarRegistros(EmplCargVaca emplCargVaca) throws Exception;

	public void filasPagina(int filasPagina);
	
}
