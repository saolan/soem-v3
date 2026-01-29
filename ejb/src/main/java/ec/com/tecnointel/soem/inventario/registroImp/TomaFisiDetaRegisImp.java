package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.TomaFisiDeta;
import ec.com.tecnointel.soem.inventario.registroInt.TomaFisiDetaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class TomaFisiDetaRegisImp extends GestorRegisSoem<TomaFisiDeta> implements TomaFisiDetaRegisInt, Serializable {

	private static final long serialVersionUID = 293502834804344918L;

	@Override
	public TomaFisiDeta buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> productoGraph = this.entityManager.getEntityGraph("tomaFisiDeta.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", productoGraph);

		return (TomaFisiDeta) entityManager.find(entidad, id, hints);

	}

}
