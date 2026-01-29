package ec.com.tecnointel.soem.tesoreria.listaInt;

import java.math.BigDecimal;
import java.util.List;

import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import jakarta.ejb.Local;

@Local
public interface CxcListaInt {

	public List<Cxc> buscarTodo(String columna) throws Exception;

	public List<Cxc> buscar(Cxc cxc, Integer pagina) throws Exception;

	public long contarRegistros(Cxc cxc) throws Exception;

	public void filasPagina(int filasPagina);

	public BigDecimal sumarCxc(Cxc cxc) throws Exception;

}
