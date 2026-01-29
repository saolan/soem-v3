package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.caja.registroInt.SaliArchRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class SaliArchRegisImp extends GestorRegisSoem<SaliArch> implements SaliArchRegisInt, Serializable {

	private static final long serialVersionUID = -5972995989656573410L;

	@Override
	public SaliArch buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> saliArchGraph = this.entityManager.getEntityGraph("saliArch.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", saliArchGraph);

		return (SaliArch) entityManager.find(entidad, id, hints);
	}
}