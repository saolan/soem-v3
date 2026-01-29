package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupPlanCuen;
import ec.com.tecnointel.soem.inventario.registroInt.ProdGrupPlanCuenRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class ProdGrupPlanCuenRegisImp extends GestorRegisSoem<ProdGrupPlanCuen>
		implements ProdGrupPlanCuenRegisInt, Serializable {

	private static final long serialVersionUID = 8834008904451460690L;

	@Override
	public ProdGrupPlanCuen buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> prodgpcGraph = this.entityManager.getEntityGraph("prodgpc.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", prodgpcGraph);

		return (ProdGrupPlanCuen) entityManager.find(entidad, id, hints);
	}
}
