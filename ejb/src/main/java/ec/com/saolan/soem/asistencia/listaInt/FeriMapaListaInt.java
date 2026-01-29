package ec.com.saolan.soem.asistencia.listaInt;

import java.util.List;

import ec.com.saolan.soem.asistencia.modelo.FeriMapa;
import jakarta.ejb.Local;

@Local
public interface FeriMapaListaInt {

	public List<FeriMapa> buscar(FeriMapa feriMapa, Integer pagina) throws Exception;

	public long contarRegistros(FeriMapa feriMapa) throws Exception;

	public void filasPagina(int filasPagina);

}
