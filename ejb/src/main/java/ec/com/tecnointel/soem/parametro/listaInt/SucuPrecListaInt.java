package ec.com.tecnointel.soem.parametro.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import jakarta.ejb.Local;

@Local
public interface SucuPrecListaInt {

	public List<SucuPrec> buscarTodo(String columna) throws Exception;

	public List<SucuPrec> buscar(SucuPrec sucuPrec, Integer pagina) throws Exception;

	public long contarRegistros(SucuPrec sucuPrec) throws Exception;

	public void imprimir(SucuPrec sucuPrec) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
