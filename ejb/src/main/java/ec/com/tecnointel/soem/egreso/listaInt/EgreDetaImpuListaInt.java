package ec.com.tecnointel.soem.egreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import jakarta.ejb.Local;

@Local
public interface EgreDetaImpuListaInt {

	public List<EgreDetaImpu> buscarTodo(String columna) throws Exception;

	public List<EgreDetaImpu> buscar(EgreDetaImpu egreDetaImpu, Integer pagina) throws Exception;

	public long contarRegistros(EgreDetaImpu egreDetaImpu) throws Exception;

	public void imprimir(EgreDetaImpu egreDetaImpu) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
