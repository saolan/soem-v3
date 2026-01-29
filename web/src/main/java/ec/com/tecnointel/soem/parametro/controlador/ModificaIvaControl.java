package ec.com.tecnointel.soem.parametro.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.ProdDimmRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.seguridad.registroInt.RespaldoBaseInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ModificaIvaControl implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String campoCalculo = "PRECIO_CON_IVA";

	int redondeo = 4;

	Dimm dimmActual;
	Dimm dimmNuevo;

	List<Dimm> dimmActuals;
	List<Dimm> dimmNuevos;
	List<Integer> redondeos;

//	Parametros para respaldar base de datos
	String host;
	String puerto;
	String usuario;
	String clave;
	String formato;
	String base;
	String rutaRespaldo;
	String rutaPgDump;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	ProdDimmListaInt prodDimmLista;

	@Inject
	ProdDimmRegisInt prodDimmRegis;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	ProdPrecRegisInt prodPrecRegis;

	@Inject
	RespaldoBaseInt respaldoBase;

	@Inject
	ParametroRegisInt parametroRegis;

	@PostConstruct
	public void cargar() {

		Dimm dimm = new Dimm();

		dimm.setTablaRefe("Tabla12");
		dimm.setEstado(true);

		dimmActuals = buscarDimms(dimm);
		dimmNuevos = buscarDimms(dimm);

		redondeos = new ArrayList<>();
		redondeos.add(2);
		redondeos.add(3);
		redondeos.add(4);

		this.cargarParametros();
	}

	public List<Dimm> buscarDimms(Dimm dimm) {

		List<Dimm> dimms = new ArrayList<>();

		try {
			dimms = dimmLista.buscar(dimm, dimm, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al buscar y cargar lista de impuestos - dimms"));
			e.printStackTrace();
		}

		return dimms;
	}

	private List<ProdDimm> buscarProdDimm(ProdDimm prodDimm) {

		List<ProdDimm> prodDimms = new ArrayList<>();

		try {
			prodDimms = prodDimmLista.buscar(prodDimm, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al buscar impuestos del producto - prodDimm"));
			e.printStackTrace();
		}

		return prodDimms;
	}

	private List<ProdPrec> buscarProdPrec(ProdPrec prodPrec) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {
			prodPrecs = prodPrecLista.buscar(prodPrec, null, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al buscar precios"));
			e.printStackTrace();
		}
		return prodPrecs;
	}

	public void modificarIva() {

		respaldar();

		try {

			cambiarParametroIvaPredeterminado(dimmNuevo);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Se cambio el Impuesto predeterminado..."));

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al cambiar parametro (5061) Iva Actual... Intente nuevamenete"));
			e.printStackTrace();
			return;
		}

//		Cambia el Iva de cada producto - prodDimm
		ProdDimm prodDimm = new ProdDimm(new Producto(), dimmActual);
		List<ProdDimm> prodDimms = buscarProdDimm(prodDimm);

		try {

			modificarProdDimm(prodDimms);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Se cambio el Impuesto a los productos..."));
		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Error al cambiar el Impuesto a los productos."));
			e.printStackTrace();

		}

//		Cambia el precio de cada producto - prodprec
//		Aqui tendria una solo lista de todos los productos y mandaria a grabar
//		List<ProdPrec> prodPrecs = buscarProdPrec(prodDimms);
//		modificarProdPrec(prodPrecs);

//		Aqui se crea una lista por cada producto y graba producto por producto
		try {

			for (ProdDimm prodDimmRecorrer : prodDimms) {

				ProdPrec prodPrec = new ProdPrec(new Sucursal(), new Precio(), new Producto());
				prodPrec.getProducto().setProdGrup(new ProdGrup());
				prodPrec.getProducto().setProductoId(prodDimmRecorrer.getProducto().getProductoId());
				prodPrec.getProducto().setEstado(true);

				List<ProdPrec> prodPrecs = buscarProdPrec(prodPrec);
				modificarProdPrec(prodPrecs);
			}
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
					"Se recalculo los precios de los productos..."));

		} catch (Exception e) {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Error al recalcular los precios de los productos"));
			e.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
				"Se han realizado todos los cambios. Proceso Terminado"));
		
		cerrarSesion();

	}

	public void cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
				"Reinicie el sistema para aplicar los cambios"));

	}

	private void cambiarParametroIvaPredeterminado(Dimm dimm) throws Exception {

//		Parametro para saber que tarifa de iva diferente de 0% se esta utilizando 12%, 8%, 13%, 15% en todo el sistema
//		Se usa para el calculo de iva de las fundas plasticas ICE
		Parametro parametro = parametroRegis.buscarPorId(Parametro.class, 5061);
		parametro.setDescri(dimm.getDimmId().toString());
		parametroRegis.modificar(parametro);

//		Cambia el parametro predeterminado que se usa al momento de crear productos
		parametro = parametroRegis.buscarPorId(Parametro.class, 5050);
		parametro.setDescri(dimm.getDimmId().toString());
		parametroRegis.modificar(parametro);

	}

	private void modificarProdDimm(List<ProdDimm> prodDimms) throws Exception {

		for (ProdDimm prodDimm : prodDimms) {

			prodDimm.setDimm(dimmNuevo);

			prodDimmRegis.modificar(prodDimm);
		}
	}

	private void modificarProdPrec(List<ProdPrec> prodPrecs) throws Exception {

		for (ProdPrec prodPrec : prodPrecs) {

			prodPrecRegis.calcularPrecio(campoCalculo, prodPrec, dimmNuevo, redondeo);
			prodPrecRegis.modificar(prodPrec);

		}
	}

	public void respaldar() {

		boolean respaldo;

		try {
			respaldo = respaldoBase.respaldar(host, puerto, usuario, clave, formato, base, rutaRespaldo, rutaPgDump);

			if (respaldo) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"Se realizo respaldo de datos..."));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, null,
						"No se realizo respaldo de datos..."));
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Error al respaldar base de datos, Intente nuevamente"));
			e.printStackTrace();
		}
	}

	public void cargarParametros() {

		Parametro parametro = new Parametro();
		;

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7001);
			this.host = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar host (7001)"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7002);
			this.puerto = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar puerto (7002)"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7003);
			this.usuario = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar usuario (7003)"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7004);
			this.clave = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar clave (7004)"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7005);
			this.formato = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar formato (7005)"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7006);
			this.base = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar base (7006)"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7007);
			this.rutaRespaldo = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar rutaRespaldo (7007)"));
			e.printStackTrace();
		}

		try {

			parametro = parametroRegis.buscarPorId(Parametro.class, 7008);
			this.rutaPgDump = parametro.getDescri();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar rutaPgDump (7008)"));
			e.printStackTrace();
		}
	}

//	<<<<<<<<<<<<<< getter - Setter >>>>>>>>>>>>>>>>
//	<<<<<<<<<<<<<< getter - Setter >>>>>>>>>>>>>>>>
//	<<<<<<<<<<<<<< getter - Setter >>>>>>>>>>>>>>>>

	public int getRedondeo() {
		return redondeo;
	}

	public void setRedondeo(int redondeo) {
		this.redondeo = redondeo;
	}

	public Dimm getDimmActual() {
		return dimmActual;
	}

	public void setDimmActual(Dimm dimmActual) {
		this.dimmActual = dimmActual;
	}

	public Dimm getDimmNuevo() {
		return dimmNuevo;
	}

	public void setDimmNuevo(Dimm dimmNuevo) {
		this.dimmNuevo = dimmNuevo;
	}

	public List<Dimm> getDimmActuals() {
		return dimmActuals;
	}

	public void setDimmActuals(List<Dimm> dimmActuals) {
		this.dimmActuals = dimmActuals;
	}
	
	public List<Integer> getRedondeos() {
		return redondeos;
	}

	public void setRedondeos(List<Integer> redondeos) {
		this.redondeos = redondeos;
	}

	public List<Dimm> getDimmNuevos() {
		return dimmNuevos;
	}

	public void setDimmNuevos(List<Dimm> dimmNuevos) {
		this.dimmNuevos = dimmNuevos;
	}

	public String getCampoCalculo() {
		return campoCalculo;
	}

	public void setCampoCalculo(String campoCalculo) {
		this.campoCalculo = campoCalculo;
	}

}