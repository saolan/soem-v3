package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.SucuPara;
import ec.com.tecnointel.soem.parametro.registroInt.SucuParaRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class SucuParaRegisImp extends GestorRegisSoem<SucuPara> implements SucuParaRegisInt, Serializable {

	private static final long serialVersionUID = 3590168411353683210L;

	@Override
	public SucuPara buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> sucuParaGraph = this.entityManager.getEntityGraph("sucuPara.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", sucuParaGraph);

		return (SucuPara) entityManager.find(entidad, id, hints);
	}

}
