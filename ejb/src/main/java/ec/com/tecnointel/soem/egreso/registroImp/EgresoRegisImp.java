package ec.com.tecnointel.soem.egreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class EgresoRegisImp extends GestorRegisSoem<Egreso> implements EgresoRegisInt, Serializable {

	private static final long serialVersionUID = 1654009923625360836L;

	@Override
	public Egreso buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> egresoGraph = this.entityManager.getEntityGraph("egreso.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", egresoGraph);

		return (Egreso) entityManager.find(entidad, id, hints);

	}
}
