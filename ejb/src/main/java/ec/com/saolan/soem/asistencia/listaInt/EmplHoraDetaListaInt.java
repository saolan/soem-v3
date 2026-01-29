package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.EmplHoraDeta;
import jakarta.ejb.Local;

@Local
public interface EmplHoraDetaListaInt {

	public List<EmplHoraDeta> buscar(EmplHoraDeta emplHoraDeta, Integer pagina) throws Exception;

	public long contarRegistros(EmplHoraDeta emplHoraDeta) throws Exception;

	public void filasPagina(int filasPagina);
	
}
