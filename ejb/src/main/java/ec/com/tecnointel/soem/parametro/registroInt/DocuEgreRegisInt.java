package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import jakarta.ejb.Local;

@Local
public interface DocuEgreRegisInt {

	public Object insertar(DocuEgre docuEgre) throws Exception;

	public void modificar(DocuEgre docuEgre) throws Exception;

	public void eliminar(DocuEgre docuEgre) throws Exception;

	public DocuEgre buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
