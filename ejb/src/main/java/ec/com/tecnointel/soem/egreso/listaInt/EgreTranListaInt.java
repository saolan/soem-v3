package ec.com.tecnointel.soem.egreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.EgreTran;
import jakarta.ejb.Local;

@Local
public interface EgreTranListaInt {

	public List<EgreTran> buscar(EgreTran egreTran, Integer pagina) throws Exception;

	public long contarRegistros(EgreTran egreTran) throws Exception;

	public void filasPagina(int filasPagina);
}
