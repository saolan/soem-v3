package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.Horario;
import jakarta.ejb.Local;

@Local
public interface HorarioListaInt {

	public List<Horario> buscar(Horario horario, Integer pagina) throws Exception;

	public long contarRegistros(Horario horario) throws Exception;

	public void filasPagina(int filasPagina);
	
}
