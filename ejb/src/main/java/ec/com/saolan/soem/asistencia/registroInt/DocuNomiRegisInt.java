package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.nomina.modelo.DocuNomi;
import jakarta.ejb.Local;

@Local
public interface DocuNomiRegisInt {

	public Object insertar(DocuNomi docuNomi) throws Exception;

	public void modificar(DocuNomi docuNomi) throws Exception;

	public void eliminar(DocuNomi docuNomi) throws Exception;

	public DocuNomi buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
