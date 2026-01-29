package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.DocuTran;
import jakarta.ejb.Local;

@Local
public interface DocuTranRegisInt {

	public Object insertar(DocuTran docuTran) throws Exception;

	public void modificar(DocuTran docuTran) throws Exception;

	public void eliminar(DocuTran docuTran) throws Exception;

	public DocuTran buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
