package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import ec.com.tecnointel.soem.caja.registroInt.CajaPeriRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class CajaPeriRegisImp extends GestorRegisSoem<CajaPeri> implements CajaPeriRegisInt, Serializable {

	private static final long serialVersionUID = 3947064962590979331L;
	
	@Override
	public CajaPeri buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> cajaPeriGraph = this.entityManager.getEntityGraph("cajaPeri.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", cajaPeriGraph);

		return (CajaPeri) entityManager.find(entidad, id, hints);
	}

}
