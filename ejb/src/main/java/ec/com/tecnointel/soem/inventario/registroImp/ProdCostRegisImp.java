package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.registroInt.ProdCostRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdCostRegisImp extends GestorRegisSoem<ProdCost> implements ProdCostRegisInt, Serializable {

	private static final long serialVersionUID = 6463835989947526616L;

	@Override
	public ProdCost buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> prodCostGraph = this.entityManager.getEntityGraph("prodCost.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", prodCostGraph);

		return (ProdCost) entityManager.find(entidad, id, hints);
	}
}
