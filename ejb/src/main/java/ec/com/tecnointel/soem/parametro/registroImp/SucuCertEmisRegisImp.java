package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import ec.com.tecnointel.soem.parametro.registroInt.SucuCertEmisRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class SucuCertEmisRegisImp extends GestorRegisSoem<SucuCertEmis> implements SucuCertEmisRegisInt, Serializable {
	
	private static final long serialVersionUID = 1768360387056858169L;

	@Override
	public SucuCertEmis buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> sucuCertEmisGraph = this.entityManager.getEntityGraph("sucuCertEmis.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", sucuCertEmisGraph);

		return (SucuCertEmis) entityManager.find(entidad, id, hints);
	}
}
