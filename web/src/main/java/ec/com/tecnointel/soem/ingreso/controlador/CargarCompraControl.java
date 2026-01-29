package ec.com.tecnointel.soem.ingreso.controlador;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import ec.com.tecnointel.soem.documeElec.modelo.InfoTributaria;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura.Detalles.Detalle;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaPrecListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaPrecRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvRegisInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuIngrListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/*
 * Esta Clase es de prueba no se utiliza
 * 
 */

@Named
@ViewScoped
public class CargarCompraControl extends PaginaControl implements Serializable {

	private static final long serialVersionUID = 6874047264294167717L;

	private String claveAcce;

	private DocuIngr docuIngr;
	private List<DocuIngr> docuIngrs;

	private Dimm dimm;
	private List<Dimm> dimms;
	
//	Lista temporal de productos que no existen
	private List<IngrDeta> ingrDetas;

	@Inject
	IngresoRegisInt ingresoRegis;

	@Inject
	IngrDetaRegisInt ingrDetaRegis;

	@Inject
	IngrDetaPrecRegisInt ingrDetaPrecRegis;

	@Inject
	IngrDetaPrecListaInt ingrDetaPrecLista;

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	PersProvRegisInt persProvRegis;

	@Inject
	ProvGrupListaInt provGrupLista;

	@Inject
	ProductoListaInt productoLista;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	DocuIngrListaInt docuIngrLista;

	@Inject
	IngresoListaInt ingresoLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	RolPrecListaInt rolPrecLista;

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@PostConstruct
	public void postConstructor() {

		DocuIngr docuIngr = new DocuIngr(new Documento(true));
		docuIngrs = buscarDocuIngrs(docuIngr);

		Dimm dimm = new Dimm("Tabla5", null, null, true);
		dimms = buscarDimms(dimm);
	}

//	RespuestaComprobante respuestaComprobante;
//	AutorizacionDTO autorizacionDTO;

	public List<Dimm> buscarDimms(Dimm dimmDesde) {

		List<Dimm> dimms = new ArrayList<>();
		try {
			dimms = dimmLista.buscar(dimmDesde, dimmDesde, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error al buscar retenciones"));
			e.printStackTrace();
		}

		return dimms;
	}

	public List<DocuIngr> buscarDocuIngrs(DocuIngr docuIngr) {

		List<DocuIngr> docuIngrs = new ArrayList<>();

		try {
			docuIngrs = docuIngrLista.buscar(docuIngr, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar documentos ingreso"));
			e.printStackTrace();
		}

		return docuIngrs;
	}

	public List<PersProv> buscarPersProvs(PersProv persProv) {

		List<PersProv> persProvs = new ArrayList<>();

		try {

			persProvLista.filasPagina(variablesSesion.getFilasPagina());
			persProvs = persProvLista.buscar(persProv, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error al buscar proveedores"));
			e.printStackTrace();
		}
		return persProvs;
	}

//	public Precio buscarPrecioPred(RolPrec rolPrec, Set<RolPersUsua> rolPersUsuas) {
//	
//		List<RolPrec> rolPrecs = new ArrayList<>();
//		List<Rol> rols = new ArrayList<>();
//
//		Stream<RolPersUsua> streamRolPersUsuas = rolPersUsuas.stream();
//		streamRolPersUsuas.forEach(p -> rols.add(p.getRol()));
//
//		for (Rol rol : rols) {
//
//			rolPrec.setRol(rol);
//
//			try {
//				rolPrecs.addAll(rolPrecLista.buscar(rolPrec, null));
//			} catch (Exception e) {
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//						"Excepcion - Error al buscar precios de roles"));
//				e.printStackTrace();
//			}
//		}
//
////		Hacen lo mismo
////		Optional<RolPrec> rolPrecPredet = rolPrecs.stream().filter(rp -> rp.getPredet()).findAny();
//		Optional<RolPrec> rolPrecPredet = rolPrecs.stream().filter((RolPrec rp) -> rp.getPredet()).findAny();
//
//		return rolPrecPredet.get().getPrecio();
//	}

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {

			prodPrecs = prodPrecLista.buscar(prodPrec, null, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar productos"));
			e.printStackTrace();
		}

		return prodPrecs;
	}

	public List<Producto> buscarProductos(Producto producto) {

		List<Producto> productos = new ArrayList<>();

		try {

			productoLista.ordenar("codigo");
//			productoLista.filasPagina(variablesSesion.getFilasPagina());
			productos = productoLista.buscar(producto, null, null);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Error al buscar productos"));
			e.printStackTrace();
		}

		return productos;
	}

	public List<ProvGrup> buscarProvGrups(ProvGrup provGrup) {

		List<ProvGrup> provGrups = new ArrayList<>();

		try {
			provGrups = provGrupLista.buscar(provGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
					"Excepcion - Error al buscar grupo de proveedores"));
			e.printStackTrace();
		}

		return provGrups;
	}

	public Object cargarDocumento() throws JAXBException, ParseException {

		File file = new File(
				"C:\\saolan\\soem\\comprobantes\\generados\\1703202301179193738400120019010000898540000000115.xml");

		Factura factura = unMarshalFactura(file);

		return factura;
	}

	public Ingreso cargarIngreso(Sucursal sucursal, PersProv persProv, Factura factura) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		Ingreso ingreso = new Ingreso();
		ingreso.setSucursal(sucursal);
		ingreso.setBodega(new Bodega(1));
		ingreso.setDocuIngr(getDocuIngr());
		ingreso.setPersProv(persProv);
		ingreso.setDimm(getDimm());
		ingreso.setFechaEmis(LocalDate.parse(factura.getInfoFactura().getFechaEmision(), dateTimeFormatter));
		ingreso.setFechaRegi(LocalDate.parse(factura.getInfoFactura().getFechaEmision(), dateTimeFormatter));
		ingreso.setFechaHoraEmis(ingreso.getFechaEmis().atTime(LocalTime.now()));
		ingreso.setFechaHoraRegi(ingreso.getFechaRegi().atTime(LocalTime.now()));
		ingreso.setSerie1(factura.getInfoTributaria().getEstab());
		ingreso.setSerie2(factura.getInfoTributaria().getPtoEmi());
		ingreso.setNumero(Integer.valueOf(factura.getInfoTributaria().getSecuencial()));
		ingreso.setClaveAcce(factura.getInfoTributaria().getClaveAcceso());
		ingreso.setAutori(factura.getInfoTributaria().getClaveAcceso());
//		Aqui esta el valor no el porcentaje
		ingreso.setDescue(factura.getInfoFactura().getTotalDescuento());
		ingreso.setNumeroCuot((short) 1);
		ingreso.setDiasPlaz((short) 30);
		ingreso.setNumeroGuia(0);
		ingreso.setNumeroRete(0);
		ingreso.setTotal(BigDecimal.ZERO);
		ingreso.setNota("Compra");
		ingreso.setEstado("GR");
		ingreso.setEstadoDocuElec("NO ENVIADO");

		return ingreso;
	}

//	Revisa si todos los productos del documento consten en el sistema
//	La lista que devuelve es la de los productos que no existen en el sistema
	public List<IngrDeta> revisarIngrDetas(Ingreso ingreso, RolPrec rolPrecPred, Factura factura) {

		List<IngrDeta> ingrDetas = new ArrayList<>();

		for (Detalle detalle : factura.getDetalles().getDetalle()) {

			ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
			Producto producto = new Producto(prodGrup, null, detalle.getCodigoPrincipal(), null, true);

			ProdPrec prodPrec = new ProdPrec(this.getVariablesSesion().getSucursal(), rolPrecPred.getPrecio(),
					producto);

			List<ProdPrec> prodPrecs = buscarProdPrecs(prodPrec);

			if (prodPrecs.isEmpty()) {
				ProdPrec prodPrecNuevo = new ProdPrec(null, null, producto);
//				Se coloca el codigo de barra ya que la busqueda se hace por codigo
//				y por lo tanto como se esta creando un nuevo prodPrec no tiene esta valor
				prodPrecNuevo.getProducto().setCodigoBarra(detalle.getCodigoPrincipal());
				ingrDetas.add(cargarIngrDeta(ingreso, detalle, prodPrecNuevo));
			}
		}
		
		return ingrDetas;
	}

	
	public List<IngrDeta> cargarIngrDetas(Ingreso ingreso, RolPrec rolPrecPred, Factura factura) {

		List<IngrDeta> ingrDetas = new ArrayList<>();

		for (Detalle detalle : factura.getDetalles().getDetalle()) {

			ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
			Producto producto = new Producto(prodGrup, null, detalle.getCodigoPrincipal(), null, true);

			ProdPrec prodPrec = new ProdPrec(this.getVariablesSesion().getSucursal(), rolPrecPred.getPrecio(),
					producto);

			List<ProdPrec> prodPrecs = buscarProdPrecs(prodPrec);

			if (!prodPrecs.isEmpty()) {

				ProdPrec prodPrecEncontrado = prodPrecs.get(0);
				ingrDetas.add(cargarIngrDeta(ingreso, detalle, prodPrecEncontrado));
			}
		}
		
		return ingrDetas;
	}

	public IngrDeta cargarIngrDeta(Ingreso ingreso, Detalle detalle, ProdPrec prodPrec) {

		IngrDeta ingrDeta = new IngrDeta();
		ingrDeta.setIngreso(ingreso);
		ingrDeta.setProducto(prodPrec.getProducto());
		ingrDeta.setPrecio(prodPrec.getPrecio());
		ingrDeta.setFechaRegi(ingreso.getFechaRegi());

		ingrDeta.setCantid(detalle.getCantidad());
		ingrDeta.setFactor(prodPrec.getFactor());
		ingrDeta.setCosto(detalle.getPrecioUnitario());
		ingrDeta.setCostoNeto(detalle.getPrecioUnitario());
		ingrDeta.setPromoc(BigDecimal.ZERO);
		ingrDeta.setDescue(detalle.getDescuento());
		ingrDeta.setDescueValo(BigDecimal.ZERO);
		ingrDeta.setFechaHoraRegi(ingreso.getFechaEmis().atTime(LocalTime.now()));

		return ingrDeta;
	}

	public PersProv cargarPersProv(InfoTributaria infoTributaria) {

		PersProv persProvBuscar = new PersProv(new Persona(infoTributaria.getRuc(), null, null, true), true);
		PersProv persProv = new PersProv(new Persona(), true);

		List<PersProv> persProvs = buscarPersProvs(persProvBuscar);

		if (persProvs.isEmpty()) {

			persProv = crearPersProv(infoTributaria);

		} else {
			persProv = persProvs.get(0);
		}

		return persProv;
	}

//	public IngrDetaImpu crearIngrDetaImpu(IngrDeta ingrDeta, Dimm dimm) {
//
//		IngrDetaImpu ingrDetaImpu = new IngrDetaImpu();
//		ingrDetaImpu.setIngrDeta(ingrDeta);
//
//		ingrDetaImpu.setDescri(dimm.getDescri());
//		ingrDetaImpu.setCodigo(dimm.getCodigo());
//		ingrDetaImpu.setPorcen(dimm.getPorcen());
//		ingrDetaImpu.setFactor(dimm.getFactor());
//		ingrDetaImpu.setTipo(null);
//
//		return ingrDetaImpu;
//	}

	// Se busca Impuestos en el producto
//	public Set<IngrDetaImpu> crearImpuestos(String ingrDetaTipoImpu, IngrDeta ingrDeta) {
//
//		ProdDimm prodDimm = new ProdDimm();
//		prodDimm.setDimm(new Dimm());
//
//		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();
//		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<IngrDetaImpu>();
//
//		prodDimm.setProducto(ingrDeta.getProducto());
//
//		try {
//
//			prodDimms = prodDimmLista.buscar(prodDimm, null);
//
//			for (ProdDimm prodDimm2 : prodDimms) {
//
//				IngrDetaImpu ingrDetaImpu = this.crearIngrDetaImpu(ingrDeta, prodDimm2.getDimm());
//
//				if (ingrDetaTipoImpu.equals("IMPUE")) {
//
//					if (prodDimm2.getDimm().getDimmId() >= 13000 && prodDimm2.getDimm().getDimmId() <= 13099) {
//						ingrDetaImpu.setTipo("IVA");
//					} else if (prodDimm2.getDimm().getDimmId() >= 13100 && prodDimm2.getDimm().getDimmId() <= 13199) {
//						ingrDetaImpu.setTipo("OTR");
//					} else if (prodDimm2.getDimm().getDimmId() >= 11000 && prodDimm2.getDimm().getDimmId() <= 11100) {
////						El ultimo código 11100 es el ice de fundas plasticas
//						ingrDetaImpu.setTipo("ICE");
//					} else if (prodDimm2.getDimm().getDimmId() >= 11800 && prodDimm2.getDimm().getDimmId() <= 11900) {
//						ingrDetaImpu.setTipo("IRBPNR");
//					}
//
//				}
//
//				if (ingrDetaImpu.getTipo() != null) {
//					ingrDetaImpus.add(ingrDetaImpu);
//				}
//			}
//
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error al crear impuestos y retenciones"));
//			e.printStackTrace();
//		}
//
//		return ingrDetaImpus;
//	}

	public Set<IngrDetaPrec> crearIngrDetaPrec(IngrDeta ingrDeta) {

		ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
		Producto producto = new Producto(prodGrup, null, ingrDeta.getProducto().getCodigo(), null, true);

		ProdPrec prodPrec = new ProdPrec(this.getVariablesSesion().getSucursal(), new Precio(), producto);

		// List<ProdPrec> prodPrecsFiltro = new ArrayList<ProdPrec>();
		List<ProdPrec> prodPrecs = new ArrayList<ProdPrec>();
		Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<IngrDetaPrec>();

		prodPrecs = buscarProdPrecs(prodPrec);

		try {

			prodPrecs = prodPrecLista.filtrarProdPrec(prodPrecs, variablesSesion.getPersUsua(),
					variablesSesion.getRolPrecs(), variablesSesion.getSucursal());

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al filtrar precios de productos"));
			e.printStackTrace();
		}

		for (ProdPrec prodPrecCopiar : prodPrecs) {

			IngrDetaPrec ingrDetaPrec = new IngrDetaPrec();
			ingrDetaPrec.setIngrDeta(ingrDeta);
			ingrDetaPrec.setPrecio(prodPrecCopiar.getPrecio());
			ingrDetaPrec.setFactor(prodPrecCopiar.getFactor());
			ingrDetaPrec.setPrecioConImpu(prodPrecCopiar.getPrecioConImpu());
			ingrDetaPrec.setPrecioSinImpu(prodPrecCopiar.getPrecioSinImpu());
			ingrDetaPrec.setUtilid(prodPrecCopiar.getUtilid());

			ingrDetaPrecs.add(ingrDetaPrec);
		}

		return ingrDetaPrecs;
	}

	public PersProv crearPersProv(InfoTributaria infoTributaria) {

		PersProv persProv = new PersProv(
				new Persona(infoTributaria.getRuc(), infoTributaria.getRazonSocial(), null, true), true);

		persProv.getPersona().setDirecc(infoTributaria.getDirMatriz());

		ProvGrup provGrup = new ProvGrup();
		provGrup.setEstado(true);

		List<ProvGrup> provGrups = buscarProvGrups(provGrup);
		persProv.setProvGrup(provGrups.get(0));

		Dimm dimm = seleccionarDimm(infoTributaria.getRuc());

		persProv.setDimm(dimm);

		persProv.setAutori("0");
		persProv.setFechaAuto(LocalDate.now());
		persProv.setExonerIva(false);
		persProv.setRetienRent(false);
		persProv.setRetienIva(false);
		persProv.setNatura("01");
		persProv.setParteRela(false);

		return persProv;
	}

	// Este metodo es parecido el run de tareaAutorizarDocu
	public void descargarXml() {

		RespuestaComprobante respuestaComprobante;
		AutorizacionDTO autorizacionDTO;

		respuestaComprobante = this.verificarAutorizacion();

		if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {

			autorizacionDTO = this.obtenerEstadoAutorizacion(respuestaComprobante);

			try {

				// Crea el archivo con la autorizacion y copia a la carpeta autorizados
				// autorizacionComprobantes.validarRespuestaAutorizacion(autorizacionDTO,
				// claveAcce, "C:\\saolan\\soem\\comprobantes\\generados\\");
				autorizacionComprobantes.crearArchivoXml(autorizacionDTO, claveAcce,
						"C:\\saolan\\soem\\comprobantes\\generados\\");

			} catch (Exception e) {

				// "Error al consultar autorización Documento: " + egreso.getNumero() +
				// autorizacionDTO.getMensaje()));
				e.printStackTrace();
			}
		}
	}

	public void grabarPersProv(PersProv persProv) throws Exception {

		persProvRegis.insertar(persProv);
	}

	public void grabarIngreso(Ingreso ingreso) throws Exception {

		ingresoRegis.insertar(ingreso);
	}

	public void grabarIngrDeta(List<IngrDeta> ingrDetas) {

		ingrDetas.forEach((IngrDeta ingrDeta) -> {

			try {
				ingrDetaRegis.insertar(ingrDeta);
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
						"Error al grabar detalle ingreso " + ingrDeta.getProducto().getDescri()));
			}
		});
	}

	public void generarDocumento() {

		Object object = new Object();

		Ingreso ingreso = new Ingreso();
		PersProv persProv = new PersProv();
		List<IngrDeta> ingrDetas = new ArrayList<>();

		try {
			object = cargarDocumento();

			if (object instanceof Factura) {

				RolPrec rolPrecPredet = new RolPrec();

				Factura factura = (Factura) object;

//				Buscar Ingreso
//				Con estos datos se buscar el ingreso para determinar si se carga o no
				PersProv persProvBuscar = new PersProv(
						new Persona(factura.getInfoTributaria().getRuc(), null, null, true));

				Ingreso ingresoBuscar = new Ingreso(new Sucursal(), new DocuIngr(new Documento()), persProvBuscar);
				ingresoBuscar.setSerie1(factura.getInfoTributaria().getEstab());
				ingresoBuscar.setSerie2(factura.getInfoTributaria().getPtoEmi());
				ingresoBuscar.setNumero(Integer.valueOf(factura.getInfoTributaria().getSecuencial()));

				List<Ingreso> ingresos = new ArrayList<>();
				try {

					ingresoLista.filasPagina(variablesSesion.getFilasPagina());
					ingresos = ingresoLista.buscar(ingresoBuscar, null);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error al buscar ingreso"));
					e.printStackTrace();
					return;
				}

				Optional<Ingreso> streamIngreso = ingresos.stream().filter(i -> !i.getEstado().equals("AN")).findAny();

				if (streamIngreso.isPresent()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							null, "Documento ya esta cargado en el sistema"));
					return;
				}
//				Fin Buscar Ingreso
				
				persProv = cargarPersProv(factura.getInfoTributaria());
				
				ingreso = cargarIngreso(variablesSesion.getSucursal(), persProv, factura);

				RolPrec rolPrec = new RolPrec(new Precio(), null, this.getVariablesSesion().getSucursal(), null, null);
				Set<RolPersUsua> rolPersUsuas = variablesSesion.getPersUsua().getRolPersUsuas();

//				Buscar en RolPrec el precio predeterminado que tiene el rol para crear ingrDeta
				try {
					rolPrecPredet = rolPrecLista.buscarPrecioPred(rolPrec, rolPersUsuas);
				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							null, "Error al buscar precio predeterminado"));
					e.printStackTrace();
					return;
				}

				ingrDetas = revisarIngrDetas(ingreso, rolPrecPredet, factura);

//				TODO: Revisa
//				Se asigna para ver los valores en pantalla
				if (!ingrDetas.isEmpty()) {
					this.ingrDetas = ingrDetas;
					return;
				}
				
				ingrDetas = cargarIngrDetas(ingreso, rolPrecPredet, factura);

				Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<>();
				for (IngrDeta ingrDeta : ingrDetas) {
					ingrDetaPrecs = this.crearIngrDetaPrec(ingrDeta);
					ingrDeta.setIngrDetaPrecs(ingrDetaPrecs);
				}

//				Graba solo si es un nuevo proveedor
				try {

					if (persProv.getPersonaId() == null) {
						grabarPersProv(persProv);
					}

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error al grabar proveedor"));
					e.printStackTrace();
					return;
				}

				try {

//					grabarIngreso(ingreso);

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error al grabar documento"));
					e.printStackTrace();
					return;
				}

				try {

//					grabarIngrDeta(ingrDetas);

				} catch (Exception e) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							null, "Error al grabar detalle del documento"));
					e.printStackTrace();
				}
			}
		} catch (JAXBException | ParseException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Error al descargar documento XML"));
		}
	}

	public AutorizacionDTO obtenerEstadoAutorizacion(RespuestaComprobante respuestaComprobante) {

		AutorizacionDTO autorizacionDTO = null;

		try {
			autorizacionDTO = autorizacionComprobantes.obtenerEstadoAutorizacion(respuestaComprobante);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return autorizacionDTO;
	}

	public Dimm seleccionarDimm(String cedulaRuc) {

		Dimm dimm = new Dimm();

		if (cedulaRuc.length() == 10) {
			dimm.setDimmId(2010);
		} else {
			dimm.setDimmId(2000);
		}

		return dimm;
	}

	public Factura unMarshalFactura(File file) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Factura.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Factura factura = (Factura) unmarshaller.unmarshal(new File(
				"C:\\saolan\\soem\\comprobantes\\generados\\1703202301179193738400120019010000898540000000115.xml"));
		return factura;

	}

	public RespuestaComprobante verificarAutorizacion() {

		RespuestaComprobante respuestaComprobante = new RespuestaComprobante();

		try {
			respuestaComprobante = autorizacionComprobantes.autorizarComprobante(variablesSesion.getProxyIp(),
					variablesSesion.getProxyPuerto(), "2", variablesSesion.getUrlProduccion(),
					variablesSesion.getUrlPruebas(), "AutorizacionComprobantesOffline", getClaveAcce());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return respuestaComprobante;

	}

	public String getClaveAcce() {
		return claveAcce;
	}

	public void setClaveAcce(String claveAcce) {
		this.claveAcce = claveAcce;
	}

	public List<DocuIngr> getDocuIngrs() {
		return docuIngrs;
	}

	public void setDocuIngrs(List<DocuIngr> docuIngrs) {
		this.docuIngrs = docuIngrs;
	}

	public DocuIngr getDocuIngr() {
		return docuIngr;
	}

	public void setDocuIngr(DocuIngr docuIngr) {
		this.docuIngr = docuIngr;
	}

	public Dimm getDimm() {
		return dimm;
	}

	public void setDimm(Dimm dimm) {
		this.dimm = dimm;
	}

	public List<Dimm> getDimms() {
		return dimms;
	}

	public void setDimms(List<Dimm> dimms) {
		this.dimms = dimms;
	}

	public List<IngrDeta> getIngrDetas() {
		return ingrDetas;
	}

	public void setIngrDetas(List<IngrDeta> ingrDetas) {
		this.ingrDetas = ingrDetas;
	}

}