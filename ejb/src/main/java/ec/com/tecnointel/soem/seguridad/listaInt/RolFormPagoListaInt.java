package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import jakarta.ejb.Local;

@Local
public interface RolFormPagoListaInt {

	public List<RolFormPago> buscarTodo(String columna) throws Exception;

	public List<RolFormPago> buscar(RolFormPago rolFormPago, Integer pagina) throws Exception;

	public long contarRegistros(RolFormPago rolFormPago) throws Exception;

	public void imprimir(RolFormPago rolFormPago) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
