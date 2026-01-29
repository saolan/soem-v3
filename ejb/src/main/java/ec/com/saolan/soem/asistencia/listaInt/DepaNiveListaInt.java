package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.DepaNive;
import jakarta.ejb.Local;

@Local
public interface DepaNiveListaInt {

	public List<DepaNive> buscar(DepaNive depaNive, Integer pagina) throws Exception;

	public long contarRegistros(DepaNive depaNive) throws Exception;

	public void filasPagina(int filasPagina);
	
}
