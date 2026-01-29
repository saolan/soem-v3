package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.Persona;
import jakarta.ejb.Local;

@Local
public interface PersonaRegisInt {

	public Object insertar(Persona persona) throws Exception;

	public void modificar(Persona persona) throws Exception;

	public void eliminar(Persona persona) throws Exception;

	public Persona buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
