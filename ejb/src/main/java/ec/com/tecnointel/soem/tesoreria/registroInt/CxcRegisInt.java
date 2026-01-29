package ec.com.tecnointel.soem.tesoreria.registroInt;

import java.math.BigDecimal;
import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Local;

@Local
public interface CxcRegisInt {

	public Object insertar(Cxc cxc) throws Exception;

	public void modificar(Cxc cxc) throws Exception;

	public void eliminar(Cxc cxc) throws Exception;

	public Cxc buscarPorId(Class<?> entidad, Integer id) throws Exception;
	 
	public List<Cxc> generarCxc(Egreso egreso, BigDecimal total, Boolean estado) throws Exception;

	public Cxc generarCxc(Egreso egreso, FpmeFormPago fpmeFormPago, Boolean estado) throws Exception;

}
