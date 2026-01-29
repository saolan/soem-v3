package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.AuseAuto;
import jakarta.ejb.Local;

@Local
public interface AuseAutoListaInt {

	public List<AuseAuto> buscar(AuseAuto auseAuto, Integer pagina) throws Exception;

	public long contarRegistros(AuseAuto auseAuto) throws Exception;

	public void filasPagina(int filasPagina);
	
}
