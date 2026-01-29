package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import jakarta.ejb.Local;

@Local
public interface ProdSubpListaInt {
	
	public List<ProdSubp> buscar(ProdSubp prodSubp, Integer pagina) throws Exception;

	public long contarRegistros(ProdSubp prodSubp) throws Exception;

	public void filasPagina(int filasPagina);

}
