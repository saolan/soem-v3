package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.ejb.Local;

@Local
public interface ProdPrecListaInt {

	public List<ProdPrec> buscarTodo(String columna) throws Exception;

	public List<ProdPrec> buscar(ProdPrec prodPrec, Integer pagina, Integer filas) throws Exception;
	
	public List<ProdPrec> buscar(Sucursal sucursal, List<Integer> precios, Producto producto) throws Exception;
	
	public List<ProdPrec> buscar(Set<Sucursal> sucursals, Set<Precio> precios, ProdPrec prodPrec, Integer pagina, Integer filas);

	public long contarRegistros(ProdPrec prodPrec) throws Exception;
	
	public long contarRegistros(Set<Sucursal> sucursals, Set<Precio> precios, ProdPrec prodPrec);
	
	public void ordenar(String columna);

	public void imprimir(ProdPrec prodPrec) throws Exception;

	public List<ProdPrec> filtrarProdPrec(List<ProdPrec> prodPrecs, PersUsua persUsuaSesion, List<RolPrec> rolPrecs, Sucursal sucursal) throws Exception;

	public void filasPagina(int filasPagina);

	public List<Object[]> buscarProdComps(Producto producto, List<RolPrec> rolPrecs) throws Exception;

}
