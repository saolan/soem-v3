package ec.com.tecnointel.soem.ingreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import jakarta.ejb.Local;

@Local
public interface IngrDetaListaInt {

	public List<IngrDeta> buscarTodo(String columna) throws Exception;

	public List<IngrDeta> buscar(IngrDeta ingrDeta, Integer pagina) throws Exception;

	public long contarRegistros(IngrDeta ingrDeta) throws Exception;

	public void imprimir(IngrDeta ingrDeta) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
