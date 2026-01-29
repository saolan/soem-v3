package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class IngresoRegisImp extends GestorRegisSoem<Ingreso> implements IngresoRegisInt, Serializable {

	private static final long serialVersionUID = 1038514296278574711L;

	@Override
	public Ingreso buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> ingresoGraph = this.entityManager.getEntityGraph("ingreso.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", ingresoGraph);

		return (Ingreso) entityManager.find(entidad, id, hints);

	}

}
