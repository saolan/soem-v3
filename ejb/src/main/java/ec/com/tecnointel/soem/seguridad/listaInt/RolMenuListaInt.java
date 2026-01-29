package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolMenu;
import jakarta.ejb.Local;

@Local
public interface RolMenuListaInt {

	public List<RolMenu> buscarTodo(String columna) throws Exception;

	public List<RolMenu> buscar(RolMenu rolMenu, Integer pagina) throws Exception;

	public long contarRegistros(RolMenu rolMenu) throws Exception;

	public void imprimir(RolMenu rolMenu) throws Exception;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public List<RolMenu> buscar(RolMenu rolMenu, Rol rol) throws Exception;

	public void filasPagina(int filasPagina);

}
