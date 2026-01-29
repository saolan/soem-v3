package ec.com.tecnointel.soem.contabilidad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.TranDeta;
import jakarta.ejb.Local;

@Local
public interface TranDetaListaInt {

	public List<TranDeta> buscarTodo(String columna) throws Exception;

	public List<TranDeta> buscar(TranDeta tranDeta, Integer pagina) throws Exception;

	public long contarRegistros(TranDeta tranDeta) throws Exception;

	public void filasPagina(int filasPagina);

}
