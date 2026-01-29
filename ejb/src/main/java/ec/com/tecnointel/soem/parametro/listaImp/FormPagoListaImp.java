package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class FormPagoListaImp extends GestorListaSoem<FormPago> implements FormPagoListaInt, Serializable {

	private static final long serialVersionUID = -8578613983022690897L;

	// Busca con paginaci�n
	@Override
	public List<FormPago> buscar(FormPago formPago, Integer pagina) {

		EntityGraph<?> formPagoGrap = this.entityManager.getEntityGraph("formPago.Graph");

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<FormPago> query = builder.createQuery(FormPago.class);
		Root<FormPago> formPagoRoot = query.from(FormPago.class);

		query.orderBy(builder.asc(formPagoRoot.get("descri")));
		TypedQuery<FormPago> consulta = this.entityManager
				.createQuery(query.select(formPagoRoot).where(getSearchPredicates(formPagoRoot, formPago)));
		consulta.setHint("jakarta.persistence.loadgraph", formPagoGrap);

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
	public long contarRegistros(FormPago formPago) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<FormPago> formPagoRoot = countQuery.from(FormPago.class);

		countQuery = countQuery.select(builder.count(formPagoRoot)).where(getSearchPredicates(formPagoRoot, formPago));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	@Override
	public void imprimir(FormPago formPago) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<FormPago> filtrarFormPagos(List<FormPago> formPagos, PersUsua persUsuaSesion,
			List<RolFormPago> rolFormPagos) {

		RolFormPago rolFormPago = new RolFormPago();

		List<Rol> roles = new ArrayList<>();
		// List<RolFormPago> rolformPagos = new ArrayList<>();
		List<FormPago> filtroFormPagos = new ArrayList<>();

		Set<FormPago> filtroFormPagosSet = new HashSet<FormPago>();

		// rolformPagos = variablesSesion.getRolFormPagos();

		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}

		for (Rol rol : roles) {
			for (FormPago formPago : formPagos) {
				rolFormPago.setRol(rol);
				rolFormPago.setFormPago(formPago);
				if (rolFormPagos.contains(rolFormPago)) {
					filtroFormPagosSet.add(formPago);
				}
			}
		}

		filtroFormPagos.addAll(filtroFormPagosSet);

		return filtroFormPagos;
	}
	
//	List<FormPago> buscarFormPagoBancos(FormPago formPago, Integer pagina) {
//		
//		List<FormPago> formPagoTmps = new ArrayList<>();
//		
//		this.buscar(formPago, pagina);
//		return null;
//	}

	@Override
	public List<FormPago> filtrarFormPagoVentas (FormPagoMoviEgre formPagoMoviEgre, List<FormPago> formPagos, PersUsua persUsuaSesion,
			List<RolFormPago> rolFormPagos) {
		
		List<FormPago> formPagoFiltros = new ArrayList<>();
						
		formPagos = this.filtrarFormPagos(formPagos, persUsuaSesion, rolFormPagos);
		
		for (FormPago formPago : formPagos) {

			if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("PAGO-COBRO")) {
				// Quitar Credito de la lista, no aparece la forma de pago NC
				if (formPago.getModulo().equals("Cajas-Ventas") && !formPago.getTipo().equals("CR")) {
					formPagoFiltros.add(formPago);
				}
			} else if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("ANTICIPO")) {
				// Dejar solamente los de tipo FP, o aparece CR, NC, AN
				if (formPago.getModulo().equals("Cajas-Ventas") && formPago.getTipo().equals("FP")) {
					formPagoFiltros.add(formPago);
				}
			} else if (formPagoMoviEgre.getDocuMoviEgre().getTipo().equals("DEPOSITO")) {
				// Dejar solamente los de tipo bancos
				if (formPago.getModulo().equals("Compras") && formPago.getTipo2().equals("BA")) {
					formPagoFiltros.add(formPago);
				}
			}
		}
		return formPagoFiltros;
	}

	@Override
	public List<FormPago> filtrarFormPagoCompras (FormPagoMoviIngr formPagoMoviIngr, List<FormPago> formPagos, PersUsua persUsuaSesion,
			List<RolFormPago> rolFormPagos) {
		
		List<FormPago> formPagoFiltros = new ArrayList<>();
						
		formPagos = this.filtrarFormPagos(formPagos, persUsuaSesion, rolFormPagos);
		
		for (FormPago formPago : formPagos) {

			if (formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("PAGO-COBRO")) {
				// Quitar Credito de la lista, no aparece la forma de pago NC
				if (!formPago.getTipo().equals("CR")) {
					formPagoFiltros.add(formPago);
				}
			} else if (formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("ANTICIPO")) {
				// Dejar solamente los de tipo FP, o aparece CR, NC, AN
				if (formPago.getTipo().equals("FP")) {
					formPagoFiltros.add(formPago);
				}
			} else if (formPagoMoviIngr.getDocuMoviIngr().getTipo().equals("DEPOSITO")) {
				// Dejar solamente la lista de las formas de pago que sean banco
				if (formPago.getTipo2().equals("BA")) {
					formPagoFiltros.add(formPago);
				}
			} 
		}
		
		return formPagoFiltros;
	}

	private Predicate[] getSearchPredicates(Root<FormPago> formPagoRoot, FormPago formPago) {
	
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
	
		Integer formPagoId = formPago.getFormPagoId();
		if (formPagoId != null) {
			predicates.add(builder.equal(formPagoRoot.<Integer> get("formPagoId"), formPagoId));
		}
	
		String descri = formPago.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(
					builder.like(builder.lower(formPagoRoot.<String>get("descri")), '%' + descri.toLowerCase() + '%'));
		}
	
		String modulo = formPago.getModulo();
		if (modulo != null && !"".equals(modulo)) {
			predicates.add(builder.equal(builder.lower(formPagoRoot.<String>get("modulo")), modulo.toLowerCase()));
		}
	
		boolean estado = formPago.isEstado();
		predicates.add(builder.equal(formPagoRoot.get("estado"), estado));
	
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}