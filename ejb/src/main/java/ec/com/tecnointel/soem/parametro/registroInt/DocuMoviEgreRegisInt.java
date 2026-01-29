package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import jakarta.ejb.Local;

@Local
public interface DocuMoviEgreRegisInt {

	public Object insertar(DocuMoviEgre docuFormPago) throws Exception;

	public void modificar(DocuMoviEgre docuFormPago) throws Exception;

	public void eliminar(DocuMoviEgre docuFormPago) throws Exception;

	public DocuMoviEgre buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
