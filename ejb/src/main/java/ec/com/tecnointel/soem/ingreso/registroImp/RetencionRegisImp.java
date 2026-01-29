package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RetencionRegisImp extends GestorRegisSoem<Retencion> implements RetencionRegisInt, Serializable {

	private static final long serialVersionUID = -7825411996002170188L;

	@Override
	public Retencion buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> retencionGraph = this.entityManager.getEntityGraph("retencion.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", retencionGraph);

		return (Retencion) entityManager.find(entidad, id, hints);

	}
}
