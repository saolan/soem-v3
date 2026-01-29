package ec.com.tecnointel.soem.tesoreria.listaInt;

import java.math.BigDecimal;
import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import jakarta.ejb.Local;

@Local
public interface PagoDetaListaInt {

	public List<PagoDeta> buscarTodo(String columna) throws Exception;

	public List<PagoDeta> buscar(PagoDeta pagoDeta, Integer pagina) throws Exception;

	public long contarRegistros(PagoDeta pagoDeta) throws Exception;

	public void filasPagina(int filasPagina);

	public BigDecimal sumarPagoDeta(PagoDeta pagoDeta) throws Exception;

	List<Object[]> buscarPorIngresoId(PagoDeta pagoDeta) throws Exception;

	List<Object[]> buscarPorFpmeId(List<Object[]> objs, Ingreso ingreso) throws Exception;
}
