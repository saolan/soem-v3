package ec.com.tecnointel.soem.reportInve.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

public class InventarioModuloControl extends PaginaControl implements Serializable {

	protected Integer precioId;

	protected Double ktvCantidMini;
	protected Double ktvCantidMaxi;
	protected String rutaJrxml;
	protected String rutaReporteCompilado = "\\jasperReportes\\inventario\\";

	// Valoracion inventario costo promedio o costo ultimo
	protected String valoraInve;

	protected Boolean prodEstado;

	protected ProdGrup prodGrup;

	protected ArrayList<String> sucursalIds;
	protected ArrayList<String> precioIds;
	protected ArrayList<String> prodGrupIds;

	protected Sucursal sucursal = new Sucursal();

	protected Producto productoDesd;
	protected Producto productoHast;
	protected ProdGrup prodGrupDesde;
	protected ProdGrup prodGrupHasta;

	protected List<ProdGrup> prodGrups;
	protected List<RolSucu> rolSucus;

	protected Set<Sucursal> sucursals;
	protected Set<Precio> precios;

	@Inject
	ProdGrupListaInt prodGrupLista;

	@Inject
	PrecioListaInt precioLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	RolPrecListaInt rolPrecLista;

	private static final long serialVersionUID = -2969176177697648971L;

	@PostConstruct
	public void cargar() {

		this.prodGrupIds = new ArrayList<>();
		
		this.prodGrup = new ProdGrup();

		this.valoraInve = "promedio";
		this.prodEstado = true;

		this.ktvCantidMini = 0.01d;
		this.ktvCantidMaxi = 50000d;

		productoDesd = new Producto();
		productoHast = new Producto();

		prodGrupDesde = new ProdGrup();
		prodGrupHasta = new ProdGrup();

		sucursals = new HashSet<>();
		precios = new HashSet<>();

		// Se cambio a cada reporte para llenar la lista de acuerdo al tipo de grupo
		// this.buscarProdGrupos();

	}

	public void descargar() throws Exception {
		// Nombre que esta en archivo fuente en jasperReport
		// String nombreReporte = "productos";
		// Se coloca aqui la ruta para poder tener los reportes en diferentes y varias
		// carpetas
		// Sino se podria poner en la clase GenerarJasperReport
		// String rutaJrxml; // "C:\\tecnoIntel\\soem\\jasperReportes\\ingreso\\";
		// En esta ruta se compila el reporte dentro del servidor de aplicaciones dentro
		// del archivo .EAR .WEB
		// String rutaReporteCompilado = "\\jasperReportes\\inventario\\";
		// Map<String, Object> parametrosJasper = new HashMap<String, Object>();
		// Parametro parametro = new Parametro();
		// try {
		// parametrosJasper.put("productoCodigoDesde",
		// productoDesde.getCodigo().toLowerCase());
		// parametro = parametroRegis.buscarPorId(5000);
		// rutaJrxml = parametro.getDescri();
		// generaJasperReportes.crearArchivo(nombreReporte, parametrosJasper,
		// rutaJrxml, rutaReporteCompilado, formatoReporte);
		// } catch (Exception e) {
		// FacesContext.getCurrentInstance().addMessage(null,
		// new FacesMessage(FacesMessage.SEVERITY_FATAL, null,"Excepcion - Error al
		// descargar reporte productos"));
		// e.printStackTrace();
		// }

		this.verificarParametros();

		Parametro parametro = new Parametro();
		parametro = parametroRegis.buscarPorId(Parametro.class, 5000);
		rutaJrxml = parametro.getDescri();

	}

	public void verificarParametros() {

		if (productoDesd.getCodigo() == null) {
			productoDesd.setCodigo(" ");
		}

		if (productoHast.getCodigo() == null) {
			productoHast.setCodigo("zz");
		}

		if (productoDesd.getDescri() == null) {
			productoDesd.setDescri(" ");
		}

		if (productoHast.getDescri() == null) {
			productoHast.setDescri("zz");
		}

		if (productoDesd.getCodigoBarra() == null) {
			productoDesd.setCodigoBarra(" ");
		}

		if (productoHast.getCodigoBarra() == null) {
			productoHast.setCodigoBarra("zz");
		}

		// Esto no se impplento porque el reporte sale aun cuando este parametro pasa
		// sin datos
		// Para llenar la lista de grupos automaticamente
		// en caso que el usuario no seleccione nada
		// if (this.prodGrupIds == null){
		// for (ProdGrup prodGrup : prodGrups) {
		// this.prodGrupIds.add(prodGrup.getProdGrupId().toString());
		// }
		// }

		// En java 8 Probar
		// if (this.prodGrupIds == null){
		// Java 8
		// this.prodGrupIds = prodGrups.stream()
		// .map( ProdGrup::toString )
		// .collect( Collectors.toList() );

		// prodGrups.forEach((xx) -> list.add(String.valueOf(xx)));
		// }

	}

	public void buscarProdGrupos() {

		// Se coloca este parametro para que se listen solo los grupo que son de detalle
		// En futuras actualizaziones hay que quitar para poder sacer el reporte de
		// manera recursiva
		// es decir seleccionar un grupo que no sea de detalle y que salgan todos los
		// que pertenecen a ese grupo
		this.prodGrup.setDetall(true);
		this.prodGrup.setEstado(true);

		try {
			
			this.prodGrups = prodGrupLista.buscar(prodGrup, null);
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar grupos de productos"));

			e.printStackTrace();
		}

	}

	public List<RolSucu> buscarRolSucus() {

		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");

		try {
			this.rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}

		for (RolSucu rolSucu : this.rolSucus) {
			sucursals.add(rolSucu.getSucursal());
		}

		return this.rolSucus;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public String getValoraInve() {
		return valoraInve;
	}

	public void setValoraInve(String valoraInve) {
		this.valoraInve = valoraInve;
	}

	public Boolean getProdEstado() {
		return prodEstado;
	}

	public void setProdEstado(Boolean prodEstado) {
		this.prodEstado = prodEstado;
	}

	public Producto getProductoDesd() {
		return productoDesd;
	}

	public void setProductoDesd(Producto productoDesd) {
		this.productoDesd = productoDesd;
	}

	public Producto getProductoHast() {
		return productoHast;
	}

	public void setProductoHast(Producto productoHast) {
		this.productoHast = productoHast;
	}

	public List<ProdGrup> getProdGrups() {
		return prodGrups;
	}

	public void setProdGrups(List<ProdGrup> prodGrups) {
		this.prodGrups = prodGrups;
	}

	public ArrayList<String> getProdGrupIds() {
		return prodGrupIds;
	}

	public void setProdGrupIds(ArrayList<String> prodGrupIds) {
		this.prodGrupIds = prodGrupIds;
	}

	public ArrayList<String> getPrecioIds() {
		return precioIds;
	}

	public void setPrecioIds(ArrayList<String> precioIds) {
		this.precioIds = precioIds;
	}

	public ProdGrup getProdGrupDesde() {
		return prodGrupDesde;
	}

	public void setProdGrupDesde(ProdGrup prodGrupDesde) {
		this.prodGrupDesde = prodGrupDesde;
	}

	public ProdGrup getProdGrupHasta() {
		return prodGrupHasta;
	}

	public void setProdGrupHasta(ProdGrup prodGrupHasta) {
		this.prodGrupHasta = prodGrupHasta;
	}

	public Set<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(Set<Precio> precios) {
		this.precios = precios;
	}

	public ArrayList<String> getSucursalIds() {
		return sucursalIds;
	}

	public void setSucursalIds(ArrayList<String> sucursalIds) {
		this.sucursalIds = sucursalIds;
	}

	public Set<Sucursal> getSucursals() {
		return sucursals;
	}

	public void setSucursals(Set<Sucursal> sucursals) {
		this.sucursals = sucursals;
	}

	public Integer getPrecioId() {
		return precioId;
	}

	public void setPrecioId(Integer precioId) {
		this.precioId = precioId;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Double getKtvCantidMini() {
		return ktvCantidMini;
	}

	public void setKtvCantidMini(Double ktvCantidMini) {
		this.ktvCantidMini = ktvCantidMini;
	}

	public Double getKtvCantidMaxi() {
		return ktvCantidMaxi;
	}

	public void setKtvCantidMaxi(Double ktvCantidMaxi) {
		this.ktvCantidMaxi = ktvCantidMaxi;
	}

}
