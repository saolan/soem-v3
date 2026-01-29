package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDimmRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class IngrDimmRegisImp extends GestorRegisSoem<IngrDimm> implements IngrDimmRegisInt, Serializable {

	private static final long serialVersionUID = -958837722947480104L;

	@Override
	public IngrDimm buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> ingrDimmGraph = this.entityManager.getEntityGraph("ingrDimm.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", ingrDimmGraph);

		return (IngrDimm) entityManager.find(entidad, id, hints);
	}
}
