package ec.com.saolan.soem.nomina.listaInt;

import java.util.List;

import ec.com.saolan.soem.nomina.modelo.AcciPersProp;
import jakarta.ejb.Local;

@Local
public interface AcciPersPropListaInt {

	public List<AcciPersProp> buscar(AcciPersProp acciPersProp, Integer pagina) throws Exception;

	public long contarRegistros(AcciPersProp acciPersProp) throws Exception;

	public void filasPagina(int filasPagina);
	
}
