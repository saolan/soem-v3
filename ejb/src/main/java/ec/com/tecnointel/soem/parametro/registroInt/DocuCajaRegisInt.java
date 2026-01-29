package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.DocuCaja;
import jakarta.ejb.Local;

@Local
public interface DocuCajaRegisInt {

	public Object insertar(DocuCaja docuCaja) throws Exception;

	public void modificar(DocuCaja docuCaja) throws Exception;

	public void eliminar(DocuCaja docuCaja) throws Exception;

	public DocuCaja buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
