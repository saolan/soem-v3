package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import jakarta.ejb.Local;

@Local
public interface DimmListaInt {

	public List<Dimm> buscarTodo(String columna) throws Exception;

	public List<Dimm> buscar(Dimm dimmDesde, Dimm dimmHasta, Integer pagina) throws Exception;

	public long contarRegistros(Dimm dimmDesde, Dimm dimmHasta) throws Exception;

	public void imprimir(Dimm dimm) throws Exception;

	public void filasPagina(int filasPagina);

}
