package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.DimmPlanCuen;
import ec.com.tecnointel.soem.parametro.registroInt.DimmPlanCuenRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class DimmPlanCuenRegisImp extends GestorRegisSoem<DimmPlanCuen>
		implements
			DimmPlanCuenRegisInt,
			Serializable {

	private static final long serialVersionUID = 5147600939652791423L;

	@Override
	public DimmPlanCuen buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> dimmPlanCuenGraph = this.entityManager
				.getEntityGraph("dimmPlanCuen.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", dimmPlanCuenGraph);

		return (DimmPlanCuen) entityManager.find(entidad, id, hints);
	}
}
