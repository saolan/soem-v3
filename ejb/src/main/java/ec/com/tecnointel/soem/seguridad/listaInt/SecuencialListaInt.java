package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.seguridad.modelo.Secuencial;
import jakarta.ejb.Local;

@Local
public interface SecuencialListaInt {

	public List<Secuencial> buscarTodo(String columna) throws Exception;

	public List<Secuencial> buscar(Secuencial secuencial, Integer pagina) throws Exception;

	public long contarRegistros(Secuencial secuencial) throws Exception;

	public void imprimir(Secuencial secuencial) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
