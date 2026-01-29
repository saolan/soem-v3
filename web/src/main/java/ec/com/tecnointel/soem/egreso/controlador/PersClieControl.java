package ec.com.tecnointel.soem.egreso.controlador;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersClieListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.registroInt.PersClieRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.controlador.PersonaException;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.registroInt.PersonaRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersClieControl extends PaginaControl implements Serializable {

	private Integer personaId;
	private String paginaRuta;

	private PersClie persClie;

	List<PersClie> persClies;
	List<ClieGrup> clieGrups;
	List<Dimm> dimms;
	List<Dimm> dimmTipoIdenClies;

	@Inject
	PersClieRegisInt persClieRegis;

	@Inject
	PersClieListaInt persClieLista;

	@Inject
	ClieGrupListaInt clieGrupLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	PersonaRegisInt personaRegis;

	private static final long serialVersionUID = 9086272167030401072L;

	@PostConstruct
	public void cargar() {

		persClie = new PersClie();
		persClie.setPersona(new Persona());
		persClie.setEstado(true);

		this.rolPermiso = variablesSesion.getRolPermiso();

	}

	public void buscar() {

		try {

			persClieLista.filasPagina(variablesSesion.getFilasPagina());

			this.persClies = persClieLista.buscar(persClie, this.pagina, variablesSesion.getFilasPagina());
			this.numeroReg = persClies.size();
			this.contadorReg = persClieLista.contarRegistros(persClie);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.buscarDimm();
		this.buscarDimmTipoIdenClies();
		this.buscarClieGrups();

		if (this.id == null) {

			persClie.setExonerIva(false);
			persClie.setRetienRent(true);
			persClie.setRetienIva(true);
			persClie.setCorteCred((short) 0);
			persClie.setDescueMaxi(new BigDecimal(0));
			persClie.setCupo(new BigDecimal(0));
			persClie.setEstado(true);

			if (this.personaId != null) {

				Persona persona = new Persona();
				try {
					persona = personaRegis.buscarPorId(Persona.class, this.getPersonaId());
					this.persClie.setPersona(persona);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al persona"));
					e.printStackTrace();
				}
			}

		} else {

			try {
				this.persClie = persClieRegis.buscarPorId(PersClie.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		String campoTexto;

		campoTexto = this.persClie.getPersona().getCedulaRuc().trim();
		this.persClie.getPersona().setCedulaRuc(campoTexto);

		campoTexto = this.persClie.getPersona().getApelli().trim();
		this.persClie.getPersona().setApelli(campoTexto);

		if (this.persClie.getPersona().getNombre() != null) {
			campoTexto = this.persClie.getPersona().getNombre().trim();
			this.persClie.getPersona().setNombre(campoTexto);
		}

		try {
			
			if (persClie.getDimm().getDimmId() == 2040 && persClie.getPersona().getCedulaRuc().length() != 10) {
				throw new PersonaException("Revisar número de cédula o tipo de identificación");
			}

			if (persClie.getDimm().getDimmId() == 2030 && persClie.getPersona().getCedulaRuc().length() != 13) {
				throw new PersonaException("Revisar número de ruc o tipo de identificación");
			}

			if (this.id == null) {
				this.persClie.getPersona().setEstado(true);
				Object id = persClieRegis.insertar(persClie);
				this.id = (Integer) id;
			} else {
				persClieRegis.modificar(persClie);
			}
			
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

			if (this.getPersonaId() != null) {
				return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();
			} else {
				return paginaRuta + "?faces-redirect=true";
			}

		} catch (RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error cédula o Ruc ya existe"));
			e.printStackTrace();
		} catch (PersonaException pe) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, pe.getMessage()));
			pe.printStackTrace();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
		}
		
		return null;
	}

	public String nuevo() {

		if (this.getPersonaId() != null) {
			return "/ppsj/egreso/persClie/registra?faces-redirect=true&personaId=" + this.getPersonaId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/egreso/persClie/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta();
		}

	}

	public String modificar() {
		if (this.getPersonaId() != null) {
			return "/ppsj/egreso/persClie/registra?faces-redirect=true&persClieId=" + this.getId() + "&personaId="
					+ this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/egreso/persClie/registra?faces-redirect=true&persClieId=" + this.getId() + "&paginaRuta="
					+ this.getPaginaRuta();
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&persClieId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			PersClie persClie = persClieRegis.buscarPorId(PersClie.class, this.getId());
			persClieRegis.eliminar(persClie);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getPersonaId() != null) {
			return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public String cancelar() {

		if (this.getPersonaId() != null) {
			return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();
		} else {
			return paginaRuta + "?faces-redirect=true";
		}

	}

	public List<PersClie> buscarTodo() {

		List<PersClie> persClies = new ArrayList<>();

		try {
			persClies = persClieLista.buscarTodo("PersClieId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return persClies;
	}

	public void buscarDimm() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla2c");
		dimmDesde.setEstado(true);
		try {
			dimms = dimmLista.buscar(dimmDesde, dimmHasta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo de identificación"));
			e.printStackTrace();
		}
	}

	public void buscarDimmTipoIdenClies() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla14");
		dimmDesde.setEstado(true);
		try {
			dimmTipoIdenClies = dimmLista.buscar(dimmDesde, dimmHasta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo identificación cliente"));
			e.printStackTrace();
		}
	}

	public void buscarClieGrups() {

		ClieGrup clieGrup = new ClieGrup();
		clieGrup.setEstado(true);

		try {
			clieGrups = clieGrupLista.buscar(clieGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al buscar grupo clientes"));
			e.printStackTrace();
		}
	}

	public void seleccionarDimm() {

//		Dimm dimmCedula = new Dimm();
//		Dimm dimmRuc = new Dimm();

		for (Dimm dimm : this.dimms) {

			if (dimm.getDimmId().equals(2030) && this.getPersClie().getPersona().getCedulaRuc().length() == 13) {
//				dimmRuc = dimm;
				this.getPersClie().setDimm(dimm);
			} else if (dimm.getDimmId().equals(2040) && this.getPersClie().getPersona().getCedulaRuc().length() == 10) {
//				dimmCedula = dimm;
				this.getPersClie().setDimm(dimm);
			}
		}

//		if (this.getPersClie().getPersona().getCedulaRuc().length() == 10) {
//			this.getPersClie().setDimm(dimmCedula);
//		} else if (this.getPersClie().getPersona().getCedulaRuc().length() == 13) {
//			this.getPersClie().setDimm(dimmRuc);
//		} 

	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PersClie getPersClie() {
		return persClie;
	}

	public void setPersClie(PersClie persClie) {
		this.persClie = persClie;
	}

	public List<PersClie> getPersClies() {
		return persClies;
	}

	public void setPersClies(List<PersClie> persClies) {
		this.persClies = persClies;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public List<ClieGrup> getClieGrups() {
		return clieGrups;
	}

	public void setClieGrups(List<ClieGrup> clieGrups) {
		this.clieGrups = clieGrups;
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	public List<Dimm> getDimmTipoIdenClies() {
		return dimmTipoIdenClies;
	}

	public void setDimmTipoIdenClies(List<Dimm> dimmTipoIdenClies) {
		this.dimmTipoIdenClies = dimmTipoIdenClies;
	}

	// Java 8
	// Java 8
	// Java 8
	public void testStream() {

		Stream<PersClie> streamPersClies = this.persClies.stream();

//		Estas Hacen lo mismo
		streamPersClies.forEach(p -> System.out.println(p));
		streamPersClies.forEach(System.out::println);

//		Estas hacen lo mismo pero en la segunda se declara el predicado aparte
		Stream<PersClie> filtered = streamPersClies
				.filter(persClie -> persClie.getCupo().compareTo(new BigDecimal(20)) > 0);
		filtered.forEach(p -> System.out.println(p));

		Predicate<PersClie> predicatePersClie = persClie -> persClie.getCupo().compareTo(new BigDecimal(20)) > 0;
		Stream<PersClie> filtered1 = streamPersClies.filter(predicatePersClie);
		filtered1.forEach(p -> System.out.println(p));

		Predicate<PersClie> predicatePersClie1 = persClie -> persClie.getCupo().compareTo(new BigDecimal(20)) > 0;
		Stream<PersClie> filtered2 = streamPersClies.filter(predicatePersClie1);
		filtered2.forEach(p -> System.out.println(p));

		Predicate<PersClie> predicatePersClie2 = persClie -> persClie.getCupo().compareTo(new BigDecimal(40)) > 0;
		Stream<PersClie> filtered3 = streamPersClies.filter(predicatePersClie2.or(predicatePersClie1));
		filtered3.forEach(p -> System.out.println(p));

//		Añade Strem a la una lista
		List<PersClie> persClies = new ArrayList<>();
		streamPersClies.peek(p -> System.out.println(p))
				.filter(predicatePersClie2.or(predicatePersClie1))
				.forEach(p -> persClies.add(p));
		
//		Suma
		BinaryOperator<Integer> sum = (i1, i2) -> i1 + i2;
		Integer id = 0;
		
		Stream<Integer> enteros =  Stream.empty();
		int total = enteros.reduce(id, sum);
//		Imprime 0
		System.out.println(total);

		enteros =  Stream.of(1);
		total = enteros.reduce(id, sum);
//		Imprime 1
		System.out.println(total);

		enteros =  Stream.of(1,2,3,4);
		total = enteros.reduce(id, sum);
//		Imprime 10
		System.out.println(total);
		
//		Es lo mismo que el anterior
		total = enteros.reduce(id, Integer::sum);		
//		Fin Suma
		
//		Max
		enteros =  Stream.of(1,2,3,4);
		Optional<Integer> suma = enteros.reduce(Integer::max);
//		Imprime 4
		System.out.println(suma);
		
		enteros =  Stream.of();
		suma = enteros.reduce(Integer::max);
//		Imprime Optional.empty
		System.out.println(suma);
//		Fin Max
		
		Optional<PersClie> persClieMin = 
				streamPersClies.filter(p -> p.getCupo().compareTo(new BigDecimal(20)) > 20 )
				.min(Comparator.comparing(PersClie::getCupo));
		System.out.println(persClieMin);

//		Es lo mismo que el anterior pero se declara el stream en la misma sentencia this.persClies.stream()
		Optional<PersClie> persClieMin1 = 
				this.persClies.stream().
				filter(p -> p.getCupo().compareTo(new BigDecimal(20)) > 20 )
				.min(Comparator.comparing(PersClie::getCupo));
		System.out.println(persClieMin1);

		
//		this.persClies.stream() : Hay que poner esto sino da error no se puede usar un stream para dos operaciones
		Optional<PersClie> persClieMax = 
				this.persClies.stream()
				.max(Comparator.comparing(PersClie::getCupo));
		System.out.println(persClieMax);
		
//		Crea map agrupado por persClie.getCupo()
		Map<BigDecimal, List<PersClie>> mapPersList = 
				this.persClies.stream()
				.collect(Collectors.groupingBy(PersClie::getCupo));
		System.out.println(mapPersList);
	}
}