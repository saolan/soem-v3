package ec.com.saolan.soem.nomina.listaInt;

import java.util.List;

import ec.com.saolan.soem.nomina.modelo.AcciPersTipo;
import jakarta.ejb.Local;

@Local
public interface AcciPersTipoListaInt {

	public List<AcciPersTipo> buscar(AcciPersTipo acciPersTipo, Integer pagina) throws Exception;

	public long contarRegistros(AcciPersTipo acciPersTipo) throws Exception;

	public void filasPagina(int filasPagina);
	
}
