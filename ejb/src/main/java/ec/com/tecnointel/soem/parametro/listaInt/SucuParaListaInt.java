package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.SucuPara;
import jakarta.ejb.Local;

@Local
public interface SucuParaListaInt {

	public List<SucuPara> buscar(SucuPara sucuPara, Integer pagina) throws Exception;

	public long contarRegistros(SucuPara sucuPara) throws Exception;

	public void filasPagina(int filasPagina);
}
