package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.DepaCont;
import jakarta.ejb.Local;

@Local
public interface DepaContListaInt {

	public List<DepaCont> buscar(DepaCont depaCont, Integer pagina) throws Exception;

	public long contarRegistros(DepaCont depaCont) throws Exception;

	public void filasPagina(int filasPagina);
	
}
