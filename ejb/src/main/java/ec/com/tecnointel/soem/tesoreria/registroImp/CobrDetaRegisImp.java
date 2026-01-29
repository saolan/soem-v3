package ec.com.tecnointel.soem.tesoreria.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.registroInt.CobrDetaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CobrDetaRegisImp extends GestorRegisSoem<CobrDeta> implements CobrDetaRegisInt, Serializable {

	private static final long serialVersionUID = -6363115891259494253L;
	
	@Override
	public CobrDeta buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cobrDetaGraph = this.entityManager.getEntityGraph("cobrDeta.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cobrDetaGraph);

		return (CobrDeta) entityManager.find(entidad, id, hints);

	}

}
