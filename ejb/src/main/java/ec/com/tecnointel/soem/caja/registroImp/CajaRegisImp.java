package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.registroInt.CajaRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CajaRegisImp extends GestorRegisSoem<Caja> implements CajaRegisInt, Serializable {

	private static final long serialVersionUID = 7423700058798842471L;

	@Override
	public Caja buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cajaGraph = this.entityManager.getEntityGraph("caja.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cajaGraph);

		return (Caja) entityManager.find(entidad, id, hints);

	}
	
//	@Override
//	@Transactional(dontRollbackOn = IllegalArgumentException.class)
//	@Transactional(rollbackOn = IllegalArgumentException.class)
//	public Object insertar(Caja caja) {
//		return entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(caja);
//	}
}
