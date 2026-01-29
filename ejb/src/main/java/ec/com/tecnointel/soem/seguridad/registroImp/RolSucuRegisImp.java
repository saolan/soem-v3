package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolSucuRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolSucuRegisImp extends GestorRegisSoem<RolSucu> implements RolSucuRegisInt, Serializable {

	private static final long serialVersionUID = -5372152284470993463L;

	@Override
	public RolSucu buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolSucuGraph = this.entityManager.getEntityGraph("rolSucu.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolSucuGraph);

		return (RolSucu) entityManager.find(entidad, id, hints);
	}
}
