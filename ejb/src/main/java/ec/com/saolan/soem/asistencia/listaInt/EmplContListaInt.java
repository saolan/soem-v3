package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.EmplCont;
import jakarta.ejb.Local;

@Local
public interface EmplContListaInt {

	public List<EmplCont> buscar(EmplCont emplCont, Integer pagina) throws Exception;

	public long contarRegistros(EmplCont emplCont) throws Exception;

	public void filasPagina(int filasPagina);
	
}
