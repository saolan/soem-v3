package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import jakarta.ejb.Local;

@Local
public interface ProdCostListaInt {

	public List<ProdCost> buscarTodo(String columna) throws Exception;

	public List<ProdCost> buscar(ProdCost prodCost, Integer pagina) throws Exception;
	
	public List<ProdCost> buscar(List<Integer> sucursals, Producto producto) throws Exception;	

	public long contarRegistros(ProdCost prodCost) throws Exception;

	public void imprimir(ProdCost prodCost) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
