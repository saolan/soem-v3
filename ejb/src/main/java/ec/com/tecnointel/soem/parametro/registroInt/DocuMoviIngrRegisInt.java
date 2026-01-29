package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import jakarta.ejb.Local;

@Local
public interface DocuMoviIngrRegisInt {

	public Object insertar(DocuMoviIngr docuFormPago) throws Exception;

	public void modificar(DocuMoviIngr docuFormPago) throws Exception;

	public void eliminar(DocuMoviIngr docuFormPago) throws Exception;

	public DocuMoviIngr buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
