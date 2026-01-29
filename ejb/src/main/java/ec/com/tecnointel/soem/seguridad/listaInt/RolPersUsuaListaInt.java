package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import jakarta.ejb.Local;

@Local
public interface RolPersUsuaListaInt {

	public List<RolPersUsua> buscarTodo(String columna) throws Exception;

	public List<RolPersUsua> buscar(RolPersUsua rolPersUsua, Integer pagina) throws Exception;

	public long contarRegistros(RolPersUsua rolPersUsua) throws Exception;

	public void imprimir(RolPersUsua rolPersUsua) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
