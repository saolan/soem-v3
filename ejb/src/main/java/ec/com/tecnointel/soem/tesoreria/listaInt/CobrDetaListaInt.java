package ec.com.tecnointel.soem.tesoreria.listaInt;

import java.math.BigDecimal;
import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import jakarta.ejb.Local;

@Local
public interface CobrDetaListaInt {

	public List<CobrDeta> buscarTodo(String columna) throws Exception;

	public List<CobrDeta> buscar(CobrDeta cobrDeta, Integer pagina) throws Exception;

	public long contarRegistros(CobrDeta cobrDeta) throws Exception;

	public void filasPagina(int filasPagina);

	public BigDecimal sumarCobrDeta(CobrDeta cobrDeta) throws Exception;


	List<Object[]> buscarPorEgresoId(CobrDeta cobrDeta) throws Exception;

	List<Object[]> buscarPorFpmeFpId(List<Object[]> objs, Egreso egreso) throws Exception;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
