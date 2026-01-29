package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.Menu;
import ec.com.tecnointel.soem.seguridad.registroInt.MenuRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class MenuRegisImp extends GestorRegisSoem<Menu> implements MenuRegisInt, Serializable {

	private static final long serialVersionUID = -2794966176314106626L;

	@Override
	public Menu buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> menuGraph = this.entityManager.getEntityGraph("menu.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", menuGraph);

		return (Menu) entityManager.find(entidad, id, hints);

	}
}
