package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.RolPerm;
import ec.com.tecnointel.soem.seguridad.registroInt.RolPermRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolPermRegisImp extends GestorRegisSoem<RolPerm> implements RolPermRegisInt, Serializable {

	private static final long serialVersionUID = 3521469790494172051L;
	
	@Override
	public RolPerm buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolPermGraph = this.entityManager.getEntityGraph("rolPerm.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolPermGraph);

		return (RolPerm) entityManager.find(entidad, id, hints);
	}

}
