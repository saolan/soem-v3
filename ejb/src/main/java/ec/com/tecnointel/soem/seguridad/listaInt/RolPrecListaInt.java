package ec.com.tecnointel.soem.seguridad.listaInt;

import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.ejb.Local;

@Local
public interface RolPrecListaInt {

	public List<RolPrec> buscarTodo(String columna) throws Exception;

	public List<RolPrec> buscar(RolPrec rolPrec, Integer pagina) throws Exception;

	public List<RolPrec> buscar(Sucursal sucursal, List<Integer> rols, RolPrec rolPrec) throws Exception;
	
	public long contarRegistros(RolPrec rolPrec) throws Exception;

	public void filasPagina(int filasPagina);

	public RolPrec buscarPrecioPred(RolPrec rolPrec, Set<RolPersUsua> rolPersUsuas) throws Exception;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
