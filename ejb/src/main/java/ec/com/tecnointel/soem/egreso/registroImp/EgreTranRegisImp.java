package ec.com.tecnointel.soem.egreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.egreso.modelo.EgreTran;
import ec.com.tecnointel.soem.egreso.registroInt.EgreTranRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class EgreTranRegisImp extends GestorRegisSoem<EgreTran> implements EgreTranRegisInt, Serializable {

	private static final long serialVersionUID = -3852169885712625730L;

	@Override
	public EgreTran buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> egreTranGraph = this.entityManager.getEntityGraph("egreTran.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", egreTranGraph);

		return (EgreTran) entityManager.find(entidad, id, hints);

	}
}
