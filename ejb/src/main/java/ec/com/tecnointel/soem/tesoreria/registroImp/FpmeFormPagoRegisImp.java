package ec.com.tecnointel.soem.tesoreria.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.FpmeFormPagoRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class FpmeFormPagoRegisImp extends GestorRegisSoem<FpmeFormPago> implements FpmeFormPagoRegisInt, Serializable {

	private static final long serialVersionUID = -2673794221732225429L;

	@Override
	public FpmeFormPago buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> fpmeFormPagoGraph = this.entityManager.getEntityGraph("fpmeFormPago.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", fpmeFormPagoGraph);

		return (FpmeFormPago) entityManager.find(entidad, id, hints);

	}

}
