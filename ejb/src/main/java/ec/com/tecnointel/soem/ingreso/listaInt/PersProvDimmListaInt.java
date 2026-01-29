package ec.com.tecnointel.soem.ingreso.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import jakarta.ejb.Local;


@Local
public interface PersProvDimmListaInt {

	public List<PersProvDimm> buscar(PersProvDimm persProvDimm, Integer pagina) throws Exception;

	public long contarRegistros(PersProvDimm persProvDimm) throws Exception;

	public void filasPagina(int filasPagina);

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
