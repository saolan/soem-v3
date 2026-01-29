package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.contabilidad.registroInt.PlanCuenRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class PlanCuenRegisImp extends GestorRegisSoem<PlanCuen> implements PlanCuenRegisInt, Serializable {

	private static final long serialVersionUID = -2130498751329955375L;

	@Override
	public PlanCuen buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> planCuenGraph = this.entityManager.getEntityGraph("planCuen.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph",planCuenGraph);

		return (PlanCuen) entityManager.find(entidad, id, hints);

	}
}
