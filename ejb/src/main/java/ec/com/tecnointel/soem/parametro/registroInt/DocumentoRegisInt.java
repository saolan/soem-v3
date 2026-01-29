package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Documento;
import jakarta.ejb.Local;

@Local
public interface DocumentoRegisInt {

	public Object insertar(Documento documento) throws Exception;

	public void modificar(Documento documento) throws Exception;

	public void eliminar(Documento documento) throws Exception;

	public Documento buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
