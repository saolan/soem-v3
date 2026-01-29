package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
import ec.com.tecnointel.soem.contabilidad.registroInt.TransaccionRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuIngrListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Se utiliza solamente para la lista de ingresos Los otros procesos de compra
 * se los realiza CompraControl
 */

@Named
@ViewScoped
public class IngresoControl extends PaginaControl implements Serializable {

	private String orden;

	private List<String> ordens = new ArrayList<>();

	private Ingreso ingreso;

	private List<Ingreso> ingresos;
	private List<PersProv> persProvs;
	private List<DocuIngr> docuIngrs;
	private List<Dimm> dimms;

	private List<IngrDeta> ingrDetas;

	@Inject
	IngresoRegisInt ingresoRegis;

	@Inject
	IngresoListaInt ingresoLista;

	@Inject
	DocuIngrListaInt docuIngrLista;

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	IngrDetaListaInt ingrDetaLista;

	@Inject
	TransaccionRegisInt transaccionRegis;

	private static final long serialVersionUID = 6225117022316173486L;

	@PostConstruct
	public void cargar() {

		this.ordens.add("Asc");
		this.ordens.add("Desc");

		this.orden = "Asc";

		PersProv persProv = new PersProv();
		persProv.setPersona(new Persona());

		DocuIngr docuIngr = new DocuIngr();
		docuIngr.setDocumento(new Documento());

		ingreso = new Ingreso();
		ingreso.setSucursal(variablesSesion.getSucursal());
		ingreso.setPersProv(persProv);
		ingreso.setDocuIngr(docuIngr);
//		Date fecha = Util.cambiarFormatoFecha(new Date(), "dd-MM-yyyy");
//		ingreso.setFechaEmis(fecha);
		ingreso.setFechaEmis(LocalDate.now());

		try {
			buscarFormatoReportes();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar formato reportes"));
			e.printStackTrace();
		}

	}

	public void buscar() {

		try {

			ingresoLista.filasPagina(variablesSesion.getFilasPagina());
			ingresoLista.orden(this.orden);

			this.ingresos = ingresoLista.buscar(this.getIngreso(), this.pagina);
			this.numeroReg = ingresos.size();
			this.contadorReg = ingresoLista.contarRegistros(ingreso);
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

		if (this.id == null) {
			this.ingreso = new Ingreso();
		} else {

			try {
				this.ingreso = ingresoRegis.buscarPorId(Ingreso.class, this.getId());

//				Buscar y asignar transaccion si no tiene asigna una vacia
				if (this.ingreso.getTransaccion() != null) {

					Transaccion transaccion = this.buscarTransaccionPorId(this.ingreso.getTransaccion().getTransaccionId());
					this.ingreso.setTransaccion(transaccion);
					
				} else {
					this.ingreso.setTransaccion(new Transaccion());
				}
				
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {
		
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Sucursal sucursal = new Sucursal();
				sucursal.setSucursalId(1);
				ingreso.setSucursal(sucursal);

				Bodega bodega = new Bodega();
				bodega.setBodegaId(1);
				ingreso.setBodega(bodega);
				ingreso.setFechaRegi(LocalDate.now());
				ingreso.setFechaHoraRegi(LocalDateTime.now());

				Object id = ingresoRegis.insertar(ingreso);
				this.id = (Integer) id;
			} else {
				ingresoRegis.modificar(ingreso);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "lista?faces-redirect=true";
	}

	public String nuevoIngrDeta() {
		return "/ppsj/ingreso/ingrDeta/registra?faces-redirect=true&ingresoId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&ingresoId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&ingresoId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Ingreso ingreso = ingresoRegis.buscarPorId(Ingreso.class, this.getId());
			ingresoRegis.eliminar(ingreso);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public List<Ingreso> buscarTodo() {

		List<Ingreso> ingresos = new ArrayList<>();

		try {
			ingresos = ingresoLista.buscarTodo("ingresoId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return ingresos;
	}

	public List<DocuIngr> docuIngresos() {

		List<DocuIngr> docuIngresos = new ArrayList<DocuIngr>();

		try {
			docuIngresos = docuIngrLista.buscarTodo("documentoId");
			this.docuIngrs.addAll(docuIngresos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docuIngresos;
	}

	// <<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<< DETALLE DOCUMENTO >>>>>>>>>>>>>>>>>>

	public String modificarIngrDeta() {
		return "/ppsj/ingreso/ingrDeta/registra?faces-redirect=true&ingrDetaId=" + this.getId();
	}

	public String explorarIngrDeta() {
		return "/ppsj/ingreso/ingrDeta/explora?faces-redirect=true&ingrDetaId=" + this.getId();
	}

	public void buscarIngrDetas() {

		IngrDeta ingrDeta = new IngrDeta();
		ingrDeta.setIngreso(this.ingreso);

		try {
			this.ingrDetas = this.ingrDetaLista.buscar(ingrDeta, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar productos de documento"));
			e.printStackTrace();
		}
	}

	public Transaccion buscarTransaccionPorId(Integer transaccionId) {

		Transaccion transaccion =  new Transaccion();
		
		try {
			transaccion = transaccionRegis.buscarPorId(Transaccion.class, transaccionId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar transacción"));
			e.printStackTrace();
		}
		
		return transaccion;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Ingreso getIngreso() {
		return ingreso;
	}

	public void setIngreso(Ingreso ingreso) {
		this.ingreso = ingreso;
	}

	public List<Ingreso> getIngresos() {
		return ingresos;
	}

	public void setIngresos(List<Ingreso> ingresos) {
		this.ingresos = ingresos;
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

	public List<DocuIngr> getDocuIngrs() {
		return docuIngrs;
	}

	public void setDocuIngrs(List<DocuIngr> docuIngrs) {
		this.docuIngrs = docuIngrs;
	}

	public List<IngrDeta> getIngrDetas() {
		return ingrDetas;
	}

	public void setIngrDetas(List<IngrDeta> ingrDetas) {
		this.ingrDetas = ingrDetas;
	}

	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public List<String> getOrdens() {
		return ordens;
	}

	public void setOrdens(List<String> ordens) {
		this.ordens = ordens;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}