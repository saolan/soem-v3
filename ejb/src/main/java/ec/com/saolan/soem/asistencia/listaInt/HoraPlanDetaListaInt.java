package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.HoraPlanDeta;
import jakarta.ejb.Local;

@Local
public interface HoraPlanDetaListaInt {

	public List<HoraPlanDeta> buscar(HoraPlanDeta horaPlanDeta, Integer pagina) throws Exception;

	public long contarRegistros(HoraPlanDeta horaPlanDeta) throws Exception;

	public void filasPagina(int filasPagina);
	
}
