package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.HoraDeta;
import jakarta.ejb.Local;

@Local
public interface HoraDetaListaInt {

	public List<HoraDeta> buscar(HoraDeta horaDeta, Integer pagina) throws Exception;

	public long contarRegistros(HoraDeta horaDeta) throws Exception;

	public void filasPagina(int filasPagina);
	
}
