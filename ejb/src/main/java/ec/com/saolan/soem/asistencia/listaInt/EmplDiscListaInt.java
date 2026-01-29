package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.EmplDisc;
import jakarta.ejb.Local;

@Local
public interface EmplDiscListaInt {

	public List<EmplDisc> buscar(EmplDisc emplDisc, Integer pagina) throws Exception;

	public long contarRegistros(EmplDisc emplDisc) throws Exception;

	public void filasPagina(int filasPagina);
	
}
