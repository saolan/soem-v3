package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.Feriado;
import jakarta.ejb.Local;

@Local
public interface FeriadoListaInt {

	public List<Feriado> buscar(Feriado feriado, Integer pagina) throws Exception;

	public long contarRegistros(Feriado feriado) throws Exception;

	public void filasPagina(int filasPagina);
	
}
