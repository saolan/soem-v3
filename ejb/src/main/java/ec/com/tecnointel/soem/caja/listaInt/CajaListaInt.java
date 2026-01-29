package ec.com.tecnointel.soem.caja.listaInt;

import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Local;

@Local
public interface CajaListaInt {

	public List<Caja> buscarTodo(String columna) throws Exception;
	
	public List<Caja> buscar(Caja caja, Integer pagina, Set<Sucursal> sucursals) throws Exception;

	public long contarRegistros(Caja caja, Set<Sucursal> sucursals) throws Exception;

	public void filasPagina(int filasPagina);	

}
