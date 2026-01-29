package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvDimmRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class PersProvDimmRegisImp extends GestorRegisSoem<PersProvDimm> implements PersProvDimmRegisInt, Serializable {

	private static final long serialVersionUID = -6626078583101526018L;

	@Override
	public PersProvDimm buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> persProvDimmGraph = this.entityManager.getEntityGraph("persProvDimm.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", persProvDimmGraph);

		return (PersProvDimm) entityManager.find(entidad, id, hints);
	}
}
