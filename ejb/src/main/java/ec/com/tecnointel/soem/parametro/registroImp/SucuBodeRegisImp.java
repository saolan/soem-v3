package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.SucuBode;
import ec.com.tecnointel.soem.parametro.registroInt.SucuBodeRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class SucuBodeRegisImp extends GestorRegisSoem<SucuBode> implements SucuBodeRegisInt, Serializable {

	private static final long serialVersionUID = 7292171729432757134L;
	
	@Override
	public SucuBode buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> sucuBodeGraph = this.entityManager.getEntityGraph("sucuBode.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", sucuBodeGraph);

		return (SucuBode) entityManager.find(entidad, id, hints);
	}

}
