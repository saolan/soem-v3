package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.TranPlanDeta;
import ec.com.tecnointel.soem.parametro.registroInt.TranPlanDetaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class TranPlanDetaRegisImp extends GestorRegisSoem<TranPlanDeta> implements TranPlanDetaRegisInt, Serializable {

	private static final long serialVersionUID = -1661953311350803820L;
	
	@Override
	public TranPlanDeta buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> tranPlanDetaGraph = this.entityManager.getEntityGraph("tranPlanDeta.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", tranPlanDetaGraph);

		return (TranPlanDeta) entityManager.find(entidad, id, hints);
	}

}
