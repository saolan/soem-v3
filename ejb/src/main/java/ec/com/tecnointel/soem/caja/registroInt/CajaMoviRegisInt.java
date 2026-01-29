package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import jakarta.ejb.Local;

@Local
public interface CajaMoviRegisInt {

	public Object insertar(CajaMovi cajaMovi) throws Exception;

	public void modificar(CajaMovi cajaMovi) throws Exception;

	public void eliminar(CajaMovi cajaMovi) throws Exception;

	public CajaMovi buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
