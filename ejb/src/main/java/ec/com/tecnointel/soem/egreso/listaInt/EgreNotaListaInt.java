package ec.com.tecnointel.soem.egreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import jakarta.ejb.Local;

@Local
public interface EgreNotaListaInt {

	public List<EgreNota> buscar(EgreNota egreDeta, Integer pagina) throws Exception;

	public long contarRegistros(EgreNota egreDeta) throws Exception;
	
	public void filasPagina(int filasPagina);

}
