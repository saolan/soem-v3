package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.registroInt.RolPrecRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolPrecRegisImp extends GestorRegisSoem<RolPrec> implements RolPrecRegisInt, Serializable {

	private static final long serialVersionUID = -3780493890373122967L;

	@Override
	public RolPrec buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolPrecGraph = this.entityManager.getEntityGraph("rolPrec.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolPrecGraph);

		return (RolPrec) entityManager.find(entidad, id, hints);
	}
	
}
