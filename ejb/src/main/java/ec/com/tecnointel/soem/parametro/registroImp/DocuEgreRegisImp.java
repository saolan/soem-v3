package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.registroInt.DocuEgreRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class DocuEgreRegisImp extends GestorRegisSoem<DocuEgre> implements DocuEgreRegisInt, Serializable {

	private static final long serialVersionUID = 5847494188544016535L;

	@Override
	public DocuEgre buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> egreDetaGraph = this.entityManager.getEntityGraph("docuEgre.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", egreDetaGraph);

		return (DocuEgre) entityManager.find(entidad, id, hints);
	}
}
