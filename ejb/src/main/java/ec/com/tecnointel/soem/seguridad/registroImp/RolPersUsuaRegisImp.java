package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.registroInt.RolPersUsuaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolPersUsuaRegisImp extends GestorRegisSoem<RolPersUsua> implements RolPersUsuaRegisInt, Serializable {

	private static final long serialVersionUID = 4863327241104199480L;
	
	@Override
	public RolPersUsua buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolPersUsua = this.entityManager.getEntityGraph("rolPersUsua.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolPersUsua);

		return (RolPersUsua) entityManager.find(entidad, id, hints);
	}

}
