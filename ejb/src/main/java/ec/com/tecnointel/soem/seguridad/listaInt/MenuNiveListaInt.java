package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.seguridad.modelo.MenuNive;
import jakarta.ejb.Local;

@Local
public interface MenuNiveListaInt {

	public List<MenuNive> buscarTodo(String columna) throws Exception;

	public List<MenuNive> buscar(MenuNive menuNive, Integer pagina) throws Exception;

	public long contarRegistros(MenuNive menuNive) throws Exception;
	
	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
