package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.ejb.Local;

@Local
public interface ProdBodeListaInt {

	public List<ProdBode> buscarTodo(String columna) throws Exception;

	public List<ProdBode> buscar(ProdBode prodBode, Integer pagina) throws Exception;
	
	public List<ProdBode> buscar(Sucursal sucursal, List<Integer> bodegas, Producto producto) throws Exception;

	public long contarRegistros(ProdBode prodBode) throws Exception;

	public void imprimir(ProdBode prodBode) throws Exception;

	public void filasPagina(int filasPagina);

	

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
