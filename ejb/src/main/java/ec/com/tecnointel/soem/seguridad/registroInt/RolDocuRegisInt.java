package ec.com.tecnointel.soem.seguridad.registroInt;

import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import jakarta.ejb.Local;

@Local
public interface RolDocuRegisInt {

	public Object insertar(RolDocu rolDocu) throws Exception;

	public void modificar(RolDocu rolDocu) throws Exception;

	public void eliminar(RolDocu rolDocu) throws Exception;

	public RolDocu buscarPorId(Class<?> entidad, Integer id) throws Exception;
	
	public RolDocu buscarRolDocuPermisos(List<RolDocu> rolDocus, Documento documento, PersUsua persUsua) throws Exception;

}
