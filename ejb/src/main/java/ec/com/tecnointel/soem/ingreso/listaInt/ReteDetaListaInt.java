package ec.com.tecnointel.soem.ingreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import jakarta.ejb.Local;

@Local
public interface ReteDetaListaInt {

	public List<ReteDeta> buscarTodo(String columna) throws Exception;

	public List<ReteDeta> buscar(ReteDeta reteDeta, Integer pagina) throws Exception;

	public long contarRegistros(ReteDeta reteDeta) throws Exception;

	public void imprimir(ReteDeta reteDeta) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
