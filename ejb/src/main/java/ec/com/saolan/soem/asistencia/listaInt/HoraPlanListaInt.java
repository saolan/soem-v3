package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.HoraPlan;
import jakarta.ejb.Local;

@Local
public interface HoraPlanListaInt {

	public List<HoraPlan> buscar(HoraPlan horaPlan, Integer pagina) throws Exception;

	public long contarRegistros(HoraPlan horaPlan) throws Exception;

	public void filasPagina(int filasPagina);
	
}
