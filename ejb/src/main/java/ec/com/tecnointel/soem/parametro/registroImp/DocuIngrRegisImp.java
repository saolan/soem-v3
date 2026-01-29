package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.registroInt.DocuIngrRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class DocuIngrRegisImp extends GestorRegisSoem<DocuIngr> implements DocuIngrRegisInt, Serializable {

	private static final long serialVersionUID = 460618948089260432L;

	@Override
	public DocuIngr buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> ingrDetaGraph = this.entityManager.getEntityGraph("docuIngr.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", ingrDetaGraph);

		return (DocuIngr) entityManager.find(entidad, id, hints);
	}

}
