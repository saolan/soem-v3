package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.registroInt.ProdDimmRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdDimmRegisImp extends GestorRegisSoem<ProdDimm> implements ProdDimmRegisInt, Serializable {

	private static final long serialVersionUID = 760740319031291641L;

	@Override
	public ProdDimm buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> prodDimmGraph = this.entityManager.getEntityGraph("prodDimm.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", prodDimmGraph);

		return (ProdDimm) entityManager.find(entidad, id, hints);
	}
}
