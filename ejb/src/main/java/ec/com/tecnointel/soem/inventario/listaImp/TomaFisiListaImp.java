package ec.com.tecnointel.soem.inventario.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.inventario.listaInt.TomaFisiListaInt;
import ec.com.tecnointel.soem.inventario.modelo.TomaFisi;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TomaFisiListaImp extends GestorListaSoem<TomaFisi> implements TomaFisiListaInt, Serializable {
		
	private static final long serialVersionUID = 1266405227111530574L;

	// Busca con paginacion
	@Override
	public List<TomaFisi> buscar(TomaFisi tomaFisi, Integer pagina) {
		
		EntityGraph<?> tomaFisiGraph = this.entityManager.getEntityGraph("tomaFisi.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<TomaFisi> query = builder.createQuery(TomaFisi.class);
		Root<TomaFisi> tomaFisiRoot = query.from(TomaFisi.class);
		
		query.orderBy(builder.asc(tomaFisiRoot.get("tomaFisiId")));
		TypedQuery<TomaFisi> consulta = this.entityManager
				.createQuery(query.select(tomaFisiRoot).where(getSearchPredicates(tomaFisiRoot, tomaFisi)));
		consulta.setHint("jakarta.persistence.loadgraph", tomaFisiGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a parametros
		// Es decir no realiza paginacion
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(TomaFisi tomaFisi) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<TomaFisi> tomaFisiRoot = countQuery.from(TomaFisi.class);

		countQuery = countQuery.select(builder.count(tomaFisiRoot)).where(getSearchPredicates(tomaFisiRoot, tomaFisi));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<TomaFisi> tomaFisiRoot, TomaFisi tomaFisi) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer tomaFisiId = tomaFisi.getTomaFisiId();
		if (tomaFisiId != null) {
			predicates.add(builder.equal(tomaFisiRoot.<Integer> get("tomaFisiId"), tomaFisiId));
		}
		
		Integer numero = tomaFisi.getNumero();
		if (numero != null) {
			predicates.add(builder.equal(tomaFisiRoot.<Integer> get("numero"), numero));
		}
		
		LocalDate fechaRegi = tomaFisi.getFechaRegi();
		if (fechaRegi != null) {
			predicates.add(builder.greaterThanOrEqualTo(tomaFisiRoot.<LocalDate> get("fechaRegi"), fechaRegi));
		}

//		String estado = tomaFisi.getEstado();
//		if (estado != null) {
//			predicates.add(builder.equal(builder.lower(tomaFisiRoot.<String> get("estado")), estado.toLowerCase()));
//		}
//		
//		String codigoBarra = tomaFisi.getCodigoBarra();
//		if (codigoBarra != null && !"".equals(codigoBarra)) {
//			predicates.add(builder.equal(builder.lower(tomaFisiRoot.<String> get("codigoBarra")), codigoBarra.toLowerCase() ));
//		}
//		
//		String codigo = tomaFisi.getCodigo();
//		if (codigo != null && !"".equals(codigo)) {
//			predicates.add(
//					builder.like(builder.lower(tomaFisiRoot.<String>get("codigo")), '%' + codigo.toLowerCase() + '%'));
//		}
//
//		String descri = tomaFisi.getDescri();
//		if (descri != null && !"".equals(descri)) {
//			predicates.add(
//					builder.like(builder.lower(tomaFisiRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
//		}
//		
//		String prodGrupTipo = tomaFisi.getProdGrup().getTipo();
//		if (prodGrupTipo != null && !"".equals(prodGrupTipo) && !prodGrupTipo.equals("Todo")) {
//			predicates.add(
//					builder.equal(builder.lower(tomaFisiRoot.get("prodGrup").<String>get("tipo")), prodGrupTipo.toLowerCase()));
//		}
//		

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}