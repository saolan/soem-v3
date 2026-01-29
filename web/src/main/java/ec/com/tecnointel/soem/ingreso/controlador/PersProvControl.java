package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvDimmListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvRegisInt;
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
public class PersProvControl extends PaginaControl implements Serializable {

	private Integer personaId;
	private String paginaRuta;

	private PersProv persProv;

	List<PersProv> persProvs;
	List<ProvGrup> ProvGrups;
	List<Dimm> dimms;
	List<Dimm> dimmTipoIdenProvs;
	List<PersProvDimm> persProvDimms;

	@Inject
	PersProvRegisInt persProvRegis;

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	ProvGrupListaInt provGrupLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	PersonaRegisInt personaRegis;

	@Inject
	PersProvDimmListaInt persProvDimmLista;

	private static final long serialVersionUID = 4499558327362278465L;

	@PostConstruct
	public void cargar() {

		persProv = new PersProv();
		persProv.setPersona(new Persona());
		persProv.setEstado(true);

		persProvDimms = new ArrayList<>();

		this.rolPermiso = variablesSesion.getRolPermiso();

	}

	public void buscar() {

		try {

			persProvLista.filasPagina(variablesSesion.getFilasPagina());

			this.persProvs = persProvLista.buscar(persProv, this.pagina);
			this.numeroReg = persProvs.size();
			this.contadorReg = persProvLista.contarRegistros(persProv);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public List<PersProvDimm> buscarPersProvDimms(PersProvDimm persProvDimm) {

		List<PersProvDimm> persProvDimms = new ArrayList<>();

		try {
			persProvDimms = persProvDimmLista.buscar(persProvDimm, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar retenciones del proveedor"));
			e.printStackTrace();
		}

		return persProvDimms;
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.buscarDimm();
		this.buscarDimmTipoIdenProvs();
		this.buscarProvGrups();

		if (this.id == null) {

			persProv.setAutori("0");
			persProv.setFechaAuto(LocalDate.now());
			persProv.setExonerIva(false);
			persProv.setRetienRent(false);
			persProv.setRetienIva(false);
			persProv.setParteRela(false);
			persProv.setEstado(true);

			if (this.personaId != null) {

				Persona persona = new Persona();
				try {
					persona = personaRegis.buscarPorId(Persona.class, this.getPersonaId());
					this.persProv.setPersona(persona);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al persona"));
					e.printStackTrace();
				}
			}

		} else {

			try {
				this.persProv = persProvRegis.buscarPorId(PersProv.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}

			PersProvDimm persProvDimm = new PersProvDimm(this.persProv, new Dimm());
			this.persProvDimms = this.buscarPersProvDimms(persProvDimm);
		}
	}

	public String grabar() {

		String campoTexto;

		campoTexto = this.persProv.getPersona().getCedulaRuc().trim();
		this.persProv.getPersona().setCedulaRuc(campoTexto);

		campoTexto = this.persProv.getPersona().getApelli().trim();
		this.persProv.getPersona().setApelli(campoTexto);

		if (this.persProv.getPersona().getNombre() != null) {
			campoTexto = this.persProv.getPersona().getNombre().trim();
			this.persProv.getPersona().setNombre(campoTexto);
		}

		try {
			
			if (persProv.getDimm().getDimmId() == 2010 && persProv.getPersona().getCedulaRuc().length() != 10) {
				throw new PersonaException("Revisar número de cédula o tipo de identificación");
			}

			if (persProv.getDimm().getDimmId() == 2000 && persProv.getPersona().getCedulaRuc().length() != 13) {
				throw new PersonaException("Revisar número de ruc o tipo de identificación");
			}

			if (this.id == null) {
				this.persProv.getPersona().setEstado(true);
				Object id = persProvRegis.insertar(persProv);
				this.id = (Integer) id;
			} else {
				persProvRegis.modificar(persProv);
			}

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

			if (this.getPersonaId() != null) {
				return paginaRuta + "?faces-redirect=true&personaId=" + this.getPersonaId();
			} else if (this.getId() != null) {
				return "/ppsj/ingreso/persProv/explora.xhtml?faces-redirect=true&persProvId=" + this.getId();
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
			return "/ppsj/ingreso/persProv/registra?faces-redirect=true&personaId=" + this.getPersonaId()
					+ "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/ingreso/persProv/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta();
		}

	}

	public String modificar() {
		if (this.getPersonaId() != null) {
			return "/ppsj/ingreso/persProv/registra?faces-redirect=true&persProvId=" + this.getId() + "&personaId="
					+ this.getPersonaId() + "&paginaRuta=" + this.getPaginaRuta();
		} else {
			return "/ppsj/ingreso/persProv/registra?faces-redirect=true&persProvId=" + this.getId() + "&paginaRuta="
					+ this.getPaginaRuta();
		}
	}

//	Se llama solo desde personaExplora para traer parametro personaId
//	public String nuevo() {
//		return "/ppsj/ingreso/persProv/registra?faces-redirect=true&personaId=" + this.getPersonaId();
//	}
//
//	public String modificar() {
//		return "/ppsj/ingreso/persProv/registra?faces-redirect=true&persProvId=" + this.getId();
//	}

	public String explorar() {
		return "explora?faces-redirect=true&persProvId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			
			PersProv persProv = persProvRegis.buscarPorId(PersProv.class, this.getId());
			persProvRegis.eliminar(persProv);
			
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

	public List<PersProv> buscarTodo() {

		List<PersProv> persProvs = new ArrayList<>();

		try {
			persProvs = persProvLista.buscarTodo("persProvId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return persProvs;
	}

//	Se carga los codigo para facturacion electronica 04, 05, 06 tabla 6
//	Para anexo seria 01, 02, 03 entonces tocaria poner otro combo con estos valores
//	cuando se este implementando el anexo y aumentar los datos de la tabla 2
//	Tabla 2 Anexo; Tabla 6 Facturación Electrónica
	public void buscarDimm() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla2p");
		dimmDesde.setEstado(true);
		try {
			dimms = dimmLista.buscar(dimmDesde, dimmHasta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo de identificación"));
			e.printStackTrace();
		}
	}

	public void buscarDimmTipoIdenProvs() {
		Dimm dimmDesde = new Dimm();
		Dimm dimmHasta = new Dimm();

		dimmDesde.setTablaRefe("Tabla14");
		dimmDesde.setEstado(true);
		try {
			dimmTipoIdenProvs = dimmLista.buscar(dimmDesde, dimmHasta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar tipo identificación proveedor"));
			e.printStackTrace();
		}
	}

	public void buscarProvGrups() {

		ProvGrup provGrup = new ProvGrup();
		provGrup.setEstado(true);

		try {
			ProvGrups = provGrupLista.buscar(provGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar grupo proveedores"));
			e.printStackTrace();
		}
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PersProv getPersProv() {
		return persProv;
	}

	public void setPersProv(PersProv persProv) {
		this.persProv = persProv;
	}

	public List<PersProv> getPersProvs() {
		return persProvs;
	}

	public void setPersProvs(List<PersProv> persProvs) {
		this.persProvs = persProvs;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public List<ProvGrup> getProvGrups() {
		return ProvGrups;
	}

	public void setProvGrups(List<ProvGrup> provGrups) {
		ProvGrups = provGrups;
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

	public List<Dimm> getDimmTipoIdenProvs() {
		return dimmTipoIdenProvs;
	}

	public void setDimmTipoIdenProvs(List<Dimm> dimmTipoIdenProvs) {
		this.dimmTipoIdenProvs = dimmTipoIdenProvs;
	}

	public List<PersProvDimm> getPersProvDimms() {
		return persProvDimms;
	}

	public void setPersProvDimms(List<PersProvDimm> persProvDimms) {
		this.persProvDimms = persProvDimms;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}