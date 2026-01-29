package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.Mapa;
import ec.com.tecnointel.soem.parametro.registroInt.MapaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class MapaRegisImp extends GestorRegisSoem<Mapa> implements MapaRegisInt, Serializable {

	private static final long serialVersionUID = -2606855759453428356L;
	
	@Override
	public Mapa buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> mapaGraph = this.entityManager.getEntityGraph("mapa.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", mapaGraph);

		return (Mapa) entityManager.find(entidad, id, hints);

	}

}
