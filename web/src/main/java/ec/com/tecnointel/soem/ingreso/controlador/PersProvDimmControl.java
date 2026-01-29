package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvDimmListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvDimmRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PersProvDimmControl extends PaginaControl implements Serializable {

	private Integer personaId;

	private PersProvDimm persProvDimm;

	private List<PersProvDimm> persProvDimms;
	private List<PersProv> persProvs;
	private List<Dimm> dimms;

	@Inject
	PersProvDimmRegisInt persProvDimmRegis;

	@Inject
	PersProvDimmListaInt persProvDimmLista;

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	DimmListaInt dimmLista;

	private static final long serialVersionUID = -8737356662670737789L;
	
	@PostConstruct
	public void cargar() {
		persProvDimm = new PersProvDimm();
		persProvDimm.setPersProv(new PersProv());
		persProvDimm.setDimm(new Dimm());

		dimms = new ArrayList<Dimm>();
	}

	public void buscar() {

		try {

			persProvDimmLista.filasPagina(variablesSesion.getFilasPagina());

			this.persProvDimms = persProvDimmLista.buscar(persProvDimm, this.pagina);
			this.numeroReg = persProvDimms.size();
			this.contadorReg = persProvDimmLista.contarRegistros(persProvDimm);
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

		this.getPersProvDimm().getPersProv().setPersonaId(personaId);

		PersProv persProv =  new PersProv();
		persProv.setPersona(new Persona());
		persProv.setPersonaId(personaId);
		persProv.setEstado(true);
		this.persProvs = this.buscarPersProvs(persProv);

		this.dimms.addAll(this.buscarDimms("Tabla3"));
		this.dimms.addAll(this.buscarDimms("Tabla11"));

		if (this.id == null) {
			this.persProvDimm = new PersProvDimm();
		} else {

			try {
				this.persProvDimm = persProvDimmRegis.buscarPorId(PersProvDimm.class, this.getId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {

				Object id = persProvDimmRegis.insertar(persProvDimm);
				this.id = (Integer) id;
			} else {
				persProvDimmRegis.modificar(persProvDimm);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "/ppsj/ingreso/persProv/explora?faces-redirect=true&persProvId=" + this.getPersonaId();
	}

	public String nuevo() {

		return "/ppsj/ingreso/persProvDimm/registra?faces-redirect=true&personaId=" + this.getPersonaId();

	}

	public String modificar() {
		
		return "/ppsj/ingreso/persProvDimm/registra?faces-redirect=true&personaId=" + this.getPersonaId() + "&persProvDimmId=" + this.getId();
		
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			PersProvDimm persProvDimm = persProvDimmRegis.buscarPorId(PersProvDimm.class, this.getId());
			persProvDimmRegis.eliminar(persProvDimm);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "/ppsj/ingreso/persProv/explora?faces-redirect=true&persProvId=" + this.getPersonaId();

	}

	public String cancelar() {

		return "/ppsj/ingreso/persProv/explora?faces-redirect=true&persProvId=" + this.getPersonaId();

	}

	public List<PersProv> buscarPersProvs(PersProv persProv) {

		List<PersProv> persProvs =  new ArrayList<>();
		
		try {
			persProvs = persProvLista.buscar(persProv, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar persProv - Error al buscar proveedores"));
			e.printStackTrace();
		}
		
		return persProvs;
	}

	public List<Dimm> buscarDimms(String tablaRefe) {

		Dimm dimmDesde = new Dimm();

		dimmDesde.setTablaRefe(tablaRefe);
		dimmDesde.setEstado(true);

		List<Dimm> dimms = new ArrayList<>();

		try {
			dimms = dimmLista.buscar(dimmDesde, dimmDesde, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Dimm - Error al buscar dimm"));
			e.printStackTrace();
		}

		return dimms;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public PersProvDimm getPersProvDimm() {
		return persProvDimm;
	}

	public void setPersProvDimm(PersProvDimm persProvDimm) {
		this.persProvDimm = persProvDimm;
	}

	public List<PersProvDimm> getPersProvDimms() {
		return persProvDimms;
	}

	public void setPersProvDimms(List<PersProvDimm> persProvDimms) {
		this.persProvDimms = persProvDimms;
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

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}
}
