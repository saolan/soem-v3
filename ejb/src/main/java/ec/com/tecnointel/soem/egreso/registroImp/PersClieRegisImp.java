package ec.com.tecnointel.soem.egreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.registroInt.PersClieRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class PersClieRegisImp extends GestorRegisSoem<PersClie> implements PersClieRegisInt, Serializable {

	private static final long serialVersionUID = 5206283204249573359L;

	@Override
	public PersClie buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> persClieGraph = this.entityManager.getEntityGraph("persClie.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", persClieGraph);

		return (PersClie) entityManager.find(entidad, id, hints);
	}
}
