package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.ejb.Local;

@Local
public interface RolSucuListaInt {

	public List<RolSucu> buscarTodo(String columna) throws Exception;

	public List<RolSucu> buscar(RolSucu rolSucu, Integer pagina) throws Exception;
	
	public List<RolSucu> buscar(Set<RolPersUsua> rolPersUsuas) throws Exception;

	public long contarRegistros(RolSucu rolSucu) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
