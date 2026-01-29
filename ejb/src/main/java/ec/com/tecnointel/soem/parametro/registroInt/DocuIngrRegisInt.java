package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import jakarta.ejb.Local;

@Local
public interface DocuIngrRegisInt {

	public Object insertar(DocuIngr docuIngr) throws Exception;

	public void modificar(DocuIngr docuIngr) throws Exception;

	public void eliminar(DocuIngr docuIngr) throws Exception;

	public DocuIngr buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
