package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import jakarta.ejb.Local;

@Local
public interface ProdDimmListaInt {

	public List<ProdDimm> buscar(ProdDimm prodDimm, Integer pagina) throws Exception;

	public long contarRegistros(ProdDimm prodDimm) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
