package ec.com.tecnointel.soem.tesoreria.registroInt;

import java.math.BigDecimal;
import java.util.List;

import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import jakarta.ejb.Local;

@Local
public interface CxpRegisInt {

	public Object insertar(Cxp cxp) throws Exception;

	public void modificar(Cxp cxp) throws Exception;

	public void eliminar(Cxp cxp) throws Exception;

	public Cxp buscarPorId(Class<?> entidad, Integer id) throws Exception;

	public List<Cxp> generarCxp(Ingreso ingreso, BigDecimal total, Boolean estado) throws Exception;

}
