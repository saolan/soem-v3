package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.TomaFisi;
import ec.com.tecnointel.soem.inventario.registroInt.TomaFisiRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class TomaFisiRegisImp extends GestorRegisSoem<TomaFisi> implements TomaFisiRegisInt, Serializable {

	private static final long serialVersionUID = 6142923179197833887L;

	@Override
	public TomaFisi buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> tomaFisiGraph = this.entityManager.getEntityGraph("tomaFisi.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", tomaFisiGraph);

		return (TomaFisi) entityManager.find(entidad, id, hints);

	}
}
