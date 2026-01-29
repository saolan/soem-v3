package ec.com.tecnointel.soem.contabilidad.listaImp;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.listaInt.TransaccionListaInt;
import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class TransaccionListaImp extends GestorListaSoem<Transaccion> implements TransaccionListaInt, Serializable {

	private static final long serialVersionUID = 2517590733291844470L;

	// Busca con paginaci�n
	@Override
	public List<Transaccion> buscar(Transaccion transaccion, Integer pagina) {

		EntityGraph<?> transaccionGraph = this.entityManager.getEntityGraph("transaccion.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Transaccion> query = builder.createQuery(Transaccion.class);
		Root<Transaccion> transaccionRoot = query.from(Transaccion.class);

		query.orderBy(builder.asc(transaccionRoot.get("docuTran").get("documento").get("descri")), builder.asc(transaccionRoot.get("numero")) );
		TypedQuery<Transaccion> consulta = this.entityManager
				.createQuery(query.select(transaccionRoot).where(getSearchPredicates(transaccionRoot, transaccion)));
		consulta.setHint("jakarta.persistence.loadgraph", transaccionGraph);
		
		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Transaccion transaccion) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Transaccion> transaccionRoot = countQuery.from(Transaccion.class);

		countQuery = countQuery.select(builder.count(transaccionRoot))
				.where(getSearchPredicates(transaccionRoot, transaccion));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Transaccion> transaccionRoot, Transaccion transaccion) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer tranId = transaccion.getTransaccionId();
		if (tranId != null) {
			predicates.add(builder.equal(transaccionRoot.get("tranId"), tranId));
		}

		String docuTranDescri = transaccion.getDocuTran().getDocumento().getDescri();
		if (docuTranDescri != null) {
			predicates.add(builder.like(builder.lower(transaccionRoot.get("docuTran").get("documento").<String> get("descri")), docuTranDescri.toLowerCase()));
		}

		Integer numero = transaccion.getNumero();
		if (numero != null) {
			predicates.add(builder.equal(transaccionRoot.get("numero"), numero));
		}

		LocalDate fechaEmis = transaccion.getFechaEmis();
		if (fechaEmis != null) {
			predicates.add(builder.greaterThanOrEqualTo(transaccionRoot.<LocalDate> get("fechaEmis"), fechaEmis));
		}

		String estado = transaccion.getEstado();
		if (estado != null) {
			predicates.add(builder.equal(builder.lower(transaccionRoot.<String> get("estado")), estado.toLowerCase()));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}