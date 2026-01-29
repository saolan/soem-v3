package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.seguridad.modelo.Auditoria;
import jakarta.ejb.Local;

@Local
public interface AuditoriaListaInt {

	public List<Auditoria> buscarTodo(String columna) throws Exception;

	public List<Auditoria> buscar(Auditoria auditoria, Integer pagina) throws Exception;

	public long contarRegistros(Auditoria auditoria) throws Exception;

	public void imprimir(Auditoria auditoria) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
