package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import ec.com.tecnointel.soem.seguridad.registroInt.RolBodeRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolBodeRegisImp extends GestorRegisSoem<RolBode> implements RolBodeRegisInt, Serializable {

	private static final long serialVersionUID = -9146363225131347105L;

	@Override
	public RolBode buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolBodeGraph = this.entityManager.getEntityGraph("rolBode.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolBodeGraph);

		return (RolBode) entityManager.find(entidad, id, hints);
	}
}
