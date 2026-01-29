package ec.com.tecnointel.soem.tesoreria.listaInt;

import java.math.BigDecimal;
import java.util.List;

import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import jakarta.ejb.Local;

@Local
public interface CxpListaInt {
	
	public List<Cxp> buscarTodo(String columna) throws Exception;

	public List<Cxp> buscar(Cxp cxp, Integer pagina) throws Exception;

	public long contarRegistros(Cxp cxp) throws Exception;

	public void filasPagina(int filasPagina);

	public BigDecimal sumarCxp(Cxp cxp) throws Exception;

}
