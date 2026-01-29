package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import ec.com.tecnointel.soem.inventario.registroInt.ProdSubpRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdSubpRegisImp extends GestorRegisSoem<ProdSubp> implements ProdSubpRegisInt, Serializable {

	private static final long serialVersionUID = 4293854122493091015L;

	@Override
	public ProdSubp buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> prodSubpGraph = this.entityManager.getEntityGraph("prodSubp.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", prodSubpGraph);

		return (ProdSubp) entityManager.find(entidad, id, hints);
	}
}
