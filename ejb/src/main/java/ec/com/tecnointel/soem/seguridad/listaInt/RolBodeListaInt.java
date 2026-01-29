package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import jakarta.ejb.Local;

@Local
public interface RolBodeListaInt {

	public List<RolBode> buscarTodo(String columna) throws Exception;

	public List<RolBode> buscar(RolBode rolBode, Integer pagina) throws Exception;
	
	public List<RolBode> buscar(Sucursal sucursal, List<Integer> rols, RolBode rolBode) throws Exception;

	public long contarRegistros(RolBode rolBode) throws Exception;

	public void imprimir(RolBode rolBode) throws Exception;

	public void filasPagina(int filasPagina);

	

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
