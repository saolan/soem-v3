package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.DocuNomiPlanCuen;
import jakarta.ejb.Local;

@Local
public interface DocuNomiPlanCuenRegisInt {

	public Object insertar(DocuNomiPlanCuen docuNomiPlanCuen) throws Exception;

	public void modificar(DocuNomiPlanCuen docuNomiPlanCuen) throws Exception;

	public void eliminar(DocuNomiPlanCuen docuNomiPlanCuen) throws Exception;

	public DocuNomiPlanCuen buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
