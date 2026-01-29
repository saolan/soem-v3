package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class PersProvRegisImp extends GestorRegisSoem<PersProv> implements PersProvRegisInt, Serializable {

	private static final long serialVersionUID = 7578625371113401926L;
	
	@Override
	public PersProv buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> persProvGraph = this.entityManager.getEntityGraph("persProv.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", persProvGraph);

		return (PersProv) entityManager.find(entidad, id, hints);
	}

}
