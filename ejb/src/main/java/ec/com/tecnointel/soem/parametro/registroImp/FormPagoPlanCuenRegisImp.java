package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.FormPagoPlanCuen;
import ec.com.tecnointel.soem.parametro.registroInt.FormPagoPlanCuenRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class FormPagoPlanCuenRegisImp extends GestorRegisSoem<FormPagoPlanCuen>
		implements FormPagoPlanCuenRegisInt, Serializable {

	private static final long serialVersionUID = -5998316110828766079L;

	@Override
	public FormPagoPlanCuen buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> fppcGraph = this.entityManager.getEntityGraph("fppc.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", fppcGraph);

		return (FormPagoPlanCuen) entityManager.find(entidad, id, hints);
	}
}
