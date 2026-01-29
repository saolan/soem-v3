package ec.com.tecnointel.soem.tesoreria.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Local;

@Local
public interface FpmeFormPagoListaInt {

	public List<FpmeFormPago> buscar(FpmeFormPago fpmeFormPago, Integer pagina) throws Exception;

	public long contarRegistros(FpmeFormPago fpmeFormPago) throws Exception;

	public void filasPagina(int filasPagina);

}
