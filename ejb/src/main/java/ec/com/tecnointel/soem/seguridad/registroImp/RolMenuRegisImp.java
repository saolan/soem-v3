package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.RolMenu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolMenuRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolMenuRegisImp extends GestorRegisSoem<RolMenu> implements RolMenuRegisInt, Serializable {

	private static final long serialVersionUID = -1357502943763959564L;

	@Override
	public RolMenu buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolMenuGraph = this.entityManager.getEntityGraph("rolMenu.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolMenuGraph);

		return (RolMenu) entityManager.find(entidad, id, hints);
	}
}
