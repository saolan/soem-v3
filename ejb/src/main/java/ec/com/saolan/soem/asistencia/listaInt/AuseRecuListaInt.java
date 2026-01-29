package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.AuseRecu;
import jakarta.ejb.Local;

@Local
public interface AuseRecuListaInt {

	public List<AuseRecu> buscar(AuseRecu auseRecu, Integer pagina) throws Exception;

	public long contarRegistros(AuseRecu auseRecu) throws Exception;

	public void filasPagina(int filasPagina);
	
}
