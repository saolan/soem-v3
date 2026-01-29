package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrupPlanCuen;
import ec.com.tecnointel.soem.ingreso.registroInt.ProvGrupPlanCuenRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProvGrupPlanCuenRegisImp extends GestorRegisSoem<ProvGrupPlanCuen>
		implements ProvGrupPlanCuenRegisInt, Serializable {

	private static final long serialVersionUID = -1004622270255979597L;
	
	@Override
	public ProvGrupPlanCuen buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> pgpcGraph = this.entityManager.getEntityGraph("pgpc.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", pgpcGraph);

		return (ProvGrupPlanCuen) entityManager.find(entidad, id, hints);
	}

}
