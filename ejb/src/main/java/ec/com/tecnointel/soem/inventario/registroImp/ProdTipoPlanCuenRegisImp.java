package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdTipoPlanCuen;
import ec.com.tecnointel.soem.inventario.registroInt.ProdTipoPlanCuenRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdTipoPlanCuenRegisImp extends GestorRegisSoem<ProdTipoPlanCuen>
		implements ProdTipoPlanCuenRegisInt, Serializable {

	private static final long serialVersionUID = 8489166755480731026L;

	@Override
	public ProdTipoPlanCuen buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> ptpcGraph = this.entityManager.getEntityGraph("ptpc.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", ptpcGraph);

		return (ProdTipoPlanCuen) entityManager.find(entidad, id, hints);
	}
}
