package ec.com.tecnointel.soem.egreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.egreso.modelo.ClieGrupPlanCuen;
import ec.com.tecnointel.soem.egreso.registroInt.ClieGrupPlanCuenRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ClieGrupPlanCuenRegisImp extends GestorRegisSoem<ClieGrupPlanCuen>
		implements ClieGrupPlanCuenRegisInt, Serializable {

	private static final long serialVersionUID = 7762174013820445425L;

	@Override
	public ClieGrupPlanCuen buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cgpcGraph = this.entityManager.getEntityGraph("cgpc.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cgpcGraph);

		return (ClieGrupPlanCuen) entityManager.find(entidad, id, hints);
	}
}
