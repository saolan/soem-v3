package ec.com.tecnointel.soem.contabilidad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class TransaccionRegisImp extends GestorRegisSoem<Transaccion> implements TransaccionRegisInt, Serializable {

	private static final long serialVersionUID = 5160593982555426903L;
	
	@Override
	public Transaccion buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> transaccionGraph = this.entityManager.getEntityGraph("transaccion.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", transaccionGraph);

		return (Transaccion) entityManager.find(entidad, id, hints);
	}
}
