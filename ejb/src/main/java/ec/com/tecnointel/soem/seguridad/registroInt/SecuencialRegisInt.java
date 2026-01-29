package ec.com.tecnointel.soem.seguridad.registroInt;

import ec.com.tecnointel.soem.seguridad.modelo.Secuencial;
import jakarta.ejb.Local;

@Local
public interface SecuencialRegisInt {

	public Object insertar(Secuencial secuencial) throws Exception;

	public void modificar(Secuencial secuencial) throws Exception;

	public void eliminar(Secuencial secuencial) throws Exception;

	public Secuencial buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
