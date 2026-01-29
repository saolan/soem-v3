package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.DocuAuse;
import jakarta.ejb.Local;

@Local
public interface DocuAuseRegisInt {

	public Object insertar(DocuAuse docuAuse) throws Exception;

	public void modificar(DocuAuse docuAuse) throws Exception;

	public void eliminar(DocuAuse docuAuse) throws Exception;

	public DocuAuse buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
