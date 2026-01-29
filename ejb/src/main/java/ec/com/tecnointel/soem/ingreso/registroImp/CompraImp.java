package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ec.com.tecnointel.soem.documeElec.modelo.InfoTributaria;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura;
import ec.com.tecnointel.soem.documeElec.modelo.factura.Factura.Detalles.Detalle;
import ec.com.tecnointel.soem.general.excepcion.ExceptArchivoNoExiste;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaImpuListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaPrecListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDimmListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.IngresoListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvDimmListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.PersProvListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ProvGrupListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.ReteDetaListaInt;
import ec.com.tecnointel.soem.ingreso.listaInt.RetencionListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.modelo.ReteDeta;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.ingreso.registroInt.CompraInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaImpuRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaPrecRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDimmRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.IngresoRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.PersProvRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.ReteDetaRegisInt;
import ec.com.tecnointel.soem.ingreso.registroInt.RetencionRegisInt;
import ec.com.tecnointel.soem.inventario.listaInt.KardTotaViewListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.KardexListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdBodeListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.KardexRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdBodeRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdCostRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProductoRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuIngrListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviIngrListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;
import ec.com.tecnointel.soem.serWebClientSri.general.AutorizacionDTO;
import ec.com.tecnointel.soem.serWebSri.registroInt.AutorizacionComprobantesWsInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxpListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviIngrListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.PagoDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxpRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviIngrRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.PagoDetaRegisInt;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

@Stateful
public class CompraImp implements CompraInt {

	private List<Producto> productos;
	private List<ProdPrec> prodPrecs;

	@Inject
	PersProvListaInt persProvLista;

	@Inject
	PersProvDimmListaInt persProvDimmLista;

	@Inject
	PersProvRegisInt persProvRegis;

	@Inject
	IngresoListaInt ingresoLista;

	@Inject
	IngresoRegisInt ingresoRegis;

	@Inject
	IngrDetaRegisInt ingrDetaRegis;

	@Inject
	IngrDetaListaInt ingrDetaLista;

	@Inject
	IngrDetaImpuListaInt ingrDetaImpuLista;

	@Inject
	IngrDetaImpuRegisInt ingrDetaImpuRegis;

	@Inject
	ProductoRegisInt productoRegis;

	@Inject
	ProductoListaInt productoLista;

	@Inject
	ProdCostListaInt prodCostLista;

	@Inject
	ProdCostRegisInt prodCostRegis;

	@Inject
	ProdBodeListaInt prodBodeLista;

	@Inject
	ProdBodeRegisInt prodBodeRegis;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	ProdPrecRegisInt prodPrecRegis;

	@Inject
	IngrDetaPrecListaInt ingrDetaPrecLista;

	@Inject
	IngrDetaPrecRegisInt ingrDetaPrecRegis;

	@Inject
	ProdDimmListaInt prodDimmLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	DocuIngrListaInt docuIngrLista;

	@Inject
	DocumentoRegisInt documentoRegis;

	@Inject
	CxpRegisInt cxpRegis;

	@Inject
	CxpListaInt cxpLista;

	@Inject
	RolPrecListaInt rolPrecLista;

	@Inject
	KardexRegisInt kardexRegis;

	@Inject
	KardexListaInt kardexLista;

	@Inject
	KardTotaViewListaInt kardTotaViewLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	RetencionRegisInt retencionRegis;

	@Inject
	RetencionListaInt retencionLista;

	@Inject
	ReteDetaListaInt reteDetaLista;

	@Inject
	ReteDetaRegisInt reteDetaRegis;

	@Inject
	ParametroRegisInt parametroRegis;

	@Inject
	DocuMoviIngrListaInt docuMoviIngrLista;

	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	FormPagoMoviIngrListaInt formPagoMoviIngrLista;

	@Inject
	FormPagoMoviIngrRegisInt formPagoMoviIngrRegis;

	@Inject
	PagoDetaRegisInt pagoDetaRegis;

	@Inject
	PagoDetaListaInt pagoDetaLista;

	@Inject
	ProvGrupListaInt provGrupLista;

	@Inject
	IngrDimmRegisInt ingrDimmRegis;

	@Inject
	IngrDimmListaInt ingrDimmLista;

	@Inject
	AutorizacionComprobantesWsInt autorizacionComprobantes;

	@Override
	public Integer insertarPersProv(PersProv persProv) throws Exception {
		Object id = null;
		try {
			id = persProvRegis.insertar(persProv);
		} catch (ConstraintViolationException cve) {
			cve.printStackTrace();
			throw new Exception("Error al grabar proveedor");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al grabar proveedor");
		}

		return (Integer) id;
	}

	@Override
	public void modificarPersProv(PersProv persProv) throws Exception {
		persProvRegis.modificar(persProv);
	}

	@Override
	public List<ProvGrup> buscarProvGrups(ProvGrup provGrup, Integer pagina) throws Exception {
		return provGrupLista.buscar(provGrup, pagina);
	}

	@Override
	public List<PersProv> buscarPersProvs(PersProv persProv, Integer pagina, int filasPagina) throws Exception {
		persProvLista.filasPagina(filasPagina);
		return persProvLista.buscar(persProv, pagina);
	}

	@Override
	public long contarRegistrosProv(PersProv persProv) throws Exception {
		return persProvLista.contarRegistros(persProv);
	}

	@Override
	public Ingreso buscarIngresoPorId(Integer id) throws Exception {
		return ingresoRegis.buscarPorId(Ingreso.class, id);
	}

	@Override
	public List<Ingreso> buscarIngresos(Ingreso ingreso) throws Exception {
		return ingresoLista.buscar(ingreso, null);
	}

	@Override
	public Long contarIngresos(Ingreso ingreso) throws Exception {
		return ingresoLista.contarRegistros(ingreso);
	}

	@Override
	public void grabar(Ingreso ingreso) throws Exception {
		ingresoRegis.insertar(ingreso);
	}

	@Override
	public void modificar(Ingreso ingreso) throws Exception {
		ingresoRegis.modificar(ingreso);
	}

	@Override
	public void modificarFpmi(FormPagoMoviIngr fpmi) throws Exception {
		formPagoMoviIngrRegis.modificar(fpmi);
	}

	@Override
	public void eliminar(Ingreso ingreso) throws Exception {
		ingresoRegis.eliminar(ingreso);
	}

	@Override
	public void anularIngreso(Ingreso ingreso) throws Exception {

		IngrDeta ingrDeta = new IngrDeta();

		List<IngrDeta> ingrDetas = new ArrayList<>();

		ingrDeta.setIngreso(ingreso);

		ingrDetas = this.buscarIngrDetas(ingrDeta, null);
		for (IngrDeta ingrDeta2 : ingrDetas) {
			this.eliminarIngrDeta(ingrDeta2);
		}

	}

	@Deprecated
	@Override
	public List<Producto> buscarProductos(Producto producto, Integer pagina, Integer filas, String ordenColumna)
			throws Exception {
		productoLista.ordenar(ordenColumna);
		productos = productoLista.buscar(producto, pagina, filas);
		return productos;
	}

	@Override
	public Producto buscarProductoPorId(Integer productoId) throws Exception {
		return productoRegis.buscarPorId(Producto.class, productoId);
	}

	@Override
	public int tamanioProductos() throws Exception {
		return productos.size();
	}

	@Override
	public long contarProductos(Producto producto) throws Exception {
		return productoLista.contarRegistros(producto);
	}

	@Override
	public List<RolPrec> buscarRolPrecios(RolPrec rolPrec) throws Exception {
		return this.rolPrecLista.buscar(rolPrec, null);
	}

	@Override
	public List<IngrDeta> buscarIngrDetas(IngrDeta ingrDeta, Integer pagina) throws Exception {
		return this.ingrDetaLista.buscar(ingrDeta, null);
	}

	@Override
	public void eliminarIngrDeta(IngrDeta ingrDeta) throws Exception {
		this.ingrDetaRegis.eliminar(ingrDeta);
	}

	@Override
	public IngrDeta buscarIngrDetaPorId(IngrDeta ingrDeta) throws Exception {
		return this.ingrDetaRegis.buscarPorId(IngrDeta.class, ingrDeta.getIngrDetaId());
	}

	@Override
	public void grabarIngrDeta(IngrDeta ingrDeta) throws Exception {
		this.ingrDetaRegis.insertar(ingrDeta);
	}

	@Override
	public void modificarIngrDeta(IngrDeta ingrDeta) throws Exception {
		this.ingrDetaRegis.modificar(ingrDeta);
	}

	@Override
	public List<IngrDetaImpu> buscarIngrDetaImpus(IngrDetaImpu ingrDetaImpu) throws Exception {
		return this.ingrDetaImpuLista.buscar(ingrDetaImpu, null);
	}

	@Override
	public void insertarIngrDetaImpu(IngrDetaImpu ingrDetaImpu) throws Exception {
		this.ingrDetaImpuRegis.insertar(ingrDetaImpu);
	}

	@Override
	public void eliminarIngrDetaImpu(IngrDetaImpu ingrDetaImpu) throws Exception {
		this.ingrDetaImpuRegis.eliminar(ingrDetaImpu);
	}

	@Override
	public List<ProdDimm> buscarProdDimms(ProdDimm prodDimm) throws Exception {
		return this.prodDimmLista.buscar(prodDimm, null);
	}

	@Override
	public Dimm buscarDimmPorId(Integer dimmId) throws Exception {
		return this.dimmRegis.buscarPorId(Dimm.class, dimmId);
	}

	@Override
	public List<Dimm> buscarSustentos(Dimm dimmDesde, Dimm dimmHasta) throws Exception {
		return this.dimmLista.buscar(dimmDesde, dimmHasta, null);
	}

	@Override
	public List<Dimm> buscarDimms(Dimm dimmDesde, Dimm dimmHasta) throws Exception {
		return this.dimmLista.buscar(dimmDesde, dimmHasta, null);
	}

	@Override
	public void modificarDocumento(Documento documento) throws Exception {
		this.documentoRegis.modificar(documento);
	}

	@Override
	public List<DocuIngr> buscarDocuIngrs(DocuIngr docuIngr) throws Exception {
		return this.docuIngrLista.buscar(docuIngr, null);
	}

	@Override
	public List<DocuIngr> filtrarDocuIngrs(List<DocuIngr> docuIngrs, PersUsua persUsua, List<RolDocu> rolDocus)
			throws Exception {
		return this.docuIngrLista.filtrarDocuIngrs(docuIngrs, persUsua, rolDocus);
	}

	@Override
	public List<Cxp> buscarCxps(Cxp cxp, Integer pagina, int filasPagina) throws Exception {

		cxpLista.filasPagina(filasPagina);

		return this.cxpLista.buscar(cxp, pagina);
	}

	@Override
	public long contarCxps(Cxp cxp) throws Exception {
		return this.cxpLista.contarRegistros(cxp);
	}

	@Override
	public List<Cxp> generarCxp(Ingreso ingreso, BigDecimal total, Boolean estado) throws Exception {
		return this.cxpRegis.generarCxp(ingreso, total, estado);
	}

	@Override
	public Integer insertarCxp(List<Cxp> cxps) throws Exception {

		Object id = null;
		for (Cxp cxp : cxps) {
			id = cxpRegis.insertar(cxp);
		}
		return (Integer) id;
	}

	@Override
	public void modificarCxp(Cxp cxp) throws Exception {

		cxpRegis.modificar(cxp);
	}

	@Override
	public void anularCxp(Cxp cxp) throws Exception {

		List<Cxp> cxps = new ArrayList<>();
//		Se envia directamente el 10 porque en segundo parametro es null, entonces no se necesita paginación
		cxps = this.buscarCxps(cxp, null, 10);
		for (Cxp cxp2 : cxps) {
			this.eliminarCxp(cxp2);
		}
	}

	@Override
	public Retencion buscarRetencionPorId(Integer retencionId) throws Exception {

		return retencionRegis.buscarPorId(Retencion.class, retencionId);

	}

	@Override
	public Integer insertarRetencion(Retencion retencion) throws Exception {

		Object id = retencionRegis.insertar(retencion);
		return (Integer) id;

	}

	@Override
	public void modificarRetencion(Retencion retencion) throws Exception {

		retencionRegis.modificar(retencion);

	}

	@Override
	public void anularRetencion(List<Retencion> retencions) throws Exception {

		for (Retencion retencionEliminar : retencions) {

			this.eliminarReteDetas(retencionEliminar);
//			No eliminar Cabecera para que quede constancia del documento anulado
//			this.eliminarRetencion(retencionEliminar);
		}
	}

	@Override
	public List<Retencion> buscarRetencions(Retencion retencion) throws Exception {
		return retencionLista.buscar(retencion, null);
	}

	public void eliminarReteDetas(Retencion retencion) throws Exception {

		ReteDeta reteDeta = new ReteDeta();
		reteDeta.setRetencion(retencion);

		List<ReteDeta> reteDetas = new ArrayList<>();
		reteDetas = reteDetaLista.buscar(reteDeta, null);

		for (ReteDeta reteDetaEliminar : reteDetas) {
			reteDetaRegis.eliminar(reteDetaEliminar);
		}
	}

//	public void eliminarRetencion(Retencion retencion) throws Exception {
//		retencionRegis.eliminar(retencion);
//	}

	@Override
	public List<ProdCost> buscarProdCosts(ProdCost prodCost) throws Exception {
		return prodCostLista.buscar(prodCost, null);
	}

	@Override
	public List<ProdBode> buscarProdBodes(ProdBode prodBode) throws Exception {
		return prodBodeLista.buscar(prodBode, null);
	}

	@Override
	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec, Integer pagina, Integer filas, String ordenColumna)
			throws Exception {
		prodPrecLista.ordenar(ordenColumna);
		this.prodPrecs = prodPrecLista.buscar(prodPrec, pagina, filas);
		return this.prodPrecs;
	}

	@Override
	public List<ProdPrec> buscarProdPrecs(Set<Sucursal> sucursals, Set<Precio> preciosSet, ProdPrec prodPrec,
			Integer filas, String ordenColumna) throws Exception {
		prodPrecLista.ordenar(ordenColumna);
		this.prodPrecs = prodPrecLista.buscar(sucursals, preciosSet, prodPrec, null, filas);
		return this.prodPrecs;
	}

	@Override
	public int tamanioProdPrecs() throws Exception {
		return this.prodPrecs.size();
	}

	@Override
	public long contarProdPrecs(ProdPrec prodPrec) throws Exception {
		return prodPrecLista.contarRegistros(prodPrec);
	}

	@Override
	public List<ProdPrec> filtrarProdPrecs(List<ProdPrec> prodPrecs, PersUsua persUsua, List<RolPrec> rolPrecs,
			Sucursal sucursal) throws Exception {
		return this.prodPrecLista.filtrarProdPrec(prodPrecs, persUsua, rolPrecs, sucursal);
	}

	@Override
	public List<IngrDetaPrec> buscarIngrDetaPrecs(IngrDetaPrec ingrDetaPrec) throws Exception {
		return ingrDetaPrecLista.buscar(ingrDetaPrec, null);
	}

	@Override
	public List<IngrDetaPrec> filtrarIngrDetaPrecs(List<IngrDetaPrec> ingrDetaPrecs, PersUsua persUsua,
			List<RolPrec> rolPrecs, Sucursal sucursal) throws Exception {
		return this.ingrDetaPrecLista.filtrarIngrDetaPrec(ingrDetaPrecs, persUsua, rolPrecs, sucursal);
	}

	@Override
	public void actualizarProdCost(List<ProdCost> prodCosts) throws Exception {
		for (ProdCost prodCost : prodCosts) {
			prodCostRegis.modificar(prodCost);
		}
	}

	@Override
	public void actualizarProdPrec(List<ProdPrec> prodPrecs) throws Exception {
		for (ProdPrec prodPrec : prodPrecs) {
			prodPrecRegis.modificar(prodPrec);
		}

	}

//	@Override
//	public List<Kardex> buscarKardexs(IngrDeta ingrDeta) throws Exception {
//		List<Kardex> kardexs = new ArrayList<>();
//		return kardexs;
//	}

	@Override
	public List<Kardex> buscarKardexs(Kardex kardex) throws Exception {
		List<Kardex> kardexs = new ArrayList<>();
		kardexs = kardexLista.buscar(kardex, null);
		return kardexs;
	}

	@Override
	public void grabarKardex(Kardex kardex) throws Exception {
		kardexRegis.insertar(kardex);
	}

	@Override
	public void eliminarKardex(Kardex kardex) throws Exception {
		kardexRegis.eliminar(kardex);
	}

	@Override
	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView) throws Exception {
		return kardTotaViewLista.buscar(kardTotaView, null);
	}

	@Override
	public List<KardTotaView> buscarKardTotaViews(List<Integer> sucursals, KardTotaView kardTotaView) throws Exception {
		return kardTotaViewLista.buscar(sucursals, kardTotaView);
	}

	@Override
	public void modificarKardex(Kardex kardex) throws Exception {
		kardexRegis.modificar(kardex);
	}

	@Override
	public List<RolSucu> buscarRolSucus(Set<RolPersUsua> rolPersUsuas) throws Exception {
		return rolSucuLista.buscar(rolPersUsuas);
	}

	@Override
	public void eliminarCxp(Cxp cxp) throws Exception {
		cxpRegis.eliminar(cxp);
	}

	@Override
	public List<DocuMoviIngr> buscarDocuMoviIngrs() throws Exception {

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuMoviIngr docuMoviIngr = new DocuMoviIngr();
		docuMoviIngr.setDocumento(documento);

		List<DocuMoviIngr> docuMoviIngrs = new ArrayList<>();

		docuMoviIngrs = docuMoviIngrLista.buscar(docuMoviIngr, null);

		return docuMoviIngrs;
	}

	@Override
	public List<DocuMoviIngr> filtrarDocuMoviIngrs(List<DocuMoviIngr> docuMoviIngrs, PersUsua persUsuaSesion,
			List<RolDocu> rolDocus) throws Exception {
		return docuMoviIngrLista.filtrarDocuMoviIngrs(docuMoviIngrs, persUsuaSesion, rolDocus);
	}

	@Override
	public DocuMoviIngr buscarDocuMoviIngrPred(List<DocuMoviIngr> docuMoviIngrs, List<RolDocu> rolDocus)
			throws Exception {
		return docuMoviIngrLista.buscarDocuMoviIngrPred(docuMoviIngrs, rolDocus);
	}

	@Override
	public List<FormPago> buscarFormPago() throws Exception {

		FormPago formPago = new FormPago();
		Parametro parametro = new Parametro();

		parametro = parametroRegis.buscarPorId(Parametro.class, 6050);

		formPago.setModulo(parametro.getDescri());
		formPago.setEstado(true);

		return this.formPagoLista.buscar(formPago, null);
	}

	@Override
	public List<FormPago> filtrarFormPagos(List<FormPago> formPagos, PersUsua persUsuaSesion,
			List<RolFormPago> rolFormPagos) throws Exception {
		return formPagoLista.filtrarFormPagos(formPagos, persUsuaSesion, rolFormPagos);
	}

	@Override
	public FormPagoMoviIngr buscarFpmisPorId(Integer fpmiId) throws Exception {
		return formPagoMoviIngrRegis.buscarPorId(FormPagoMoviIngr.class, fpmiId);
	}

	@Override
	public List<FormPagoMoviIngr> buscarFpmis(FormPagoMoviIngr fpmi) throws Exception {
		return formPagoMoviIngrLista.buscar(fpmi, null);
	}

	@Override
	public Integer insertarFpmi(FormPagoMoviIngr formPagoMoviIngr) throws Exception {
		Object id = formPagoMoviIngrRegis.insertar(formPagoMoviIngr);
		return (Integer) id;
	}

	@Override
	public void eliminarFpmi(FormPagoMoviIngr formPagoMoviIngr) throws Exception {
		formPagoMoviIngrRegis.eliminar(formPagoMoviIngr);
	}

	@Override
	public List<PagoDeta> buscarPagoDetas(PagoDeta pagoDeta) throws Exception {
		return pagoDetaLista.buscar(pagoDeta, null);
	}

	@Override
	public void insertarPagoDeta(PagoDeta pagoDeta) throws Exception {
		pagoDetaRegis.insertar(pagoDeta);
	}

	@Override
	public void eliminarPagoDeta(PagoDeta pagoDeta) throws Exception {
		pagoDetaRegis.eliminar(pagoDeta);
	}

	@Override
	public BigDecimal sumarCxp(Cxp cxp) throws Exception {
		return this.cxpLista.sumarCxp(cxp);
	}

	@Override
	public BigDecimal sumarPagoDeta(PagoDeta pagoDeta) throws Exception {
		return this.pagoDetaLista.sumarPagoDeta(pagoDeta);
	}

	@Override
	public PersProv buscarPersProvPorId(PersProv persProv) throws Exception {
		return this.persProvRegis.buscarPorId(PersProv.class, persProv.getPersonaId());
	}

	@Override
	public List<PersProvDimm> buscarPersProvDimms(PersProvDimm persProvDimm, Integer pagina) throws Exception {
		return this.persProvDimmLista.buscar(persProvDimm, pagina);
	}

	@Override
	public void insertarIngrDimm(IngrDimm ingrDimm) throws Exception {
		ingrDimmRegis.insertar(ingrDimm);
	}

	@Override
	public void eliminarIngrDimm(IngrDimm ingrDimm) throws Exception {
		ingrDimmRegis.eliminar(ingrDimm);
	}

	@Override
	public List<IngrDimm> buscarIngrDimms(IngrDimm ingrDimm, Integer pagina) throws Exception {
		return this.ingrDimmLista.buscar(ingrDimm, pagina);
	}

	@Override
	public List<Object[]> buscarPagosPorIngresoId(PagoDeta pagoDeta) throws Exception {
		return pagoDetaLista.buscarPorIngresoId(pagoDeta);
	}

	@Override
	public List<Object[]> buscarPagosPorFpmeId(List<Object[]> objs, Ingreso ingreso) throws Exception {
		return pagoDetaLista.buscarPorFpmeId(objs, ingreso);
	}

	/*
	 * 
	 * Desde aqui se esta haciendo los cambios Desde aqui se esta haciendo los
	 * cambios Desde aqui se esta haciendo los cambios Desde aqui se esta haciendo
	 * los cambios Desde aqui se esta haciendo los cambios
	 * 
	 */

	@Override
	public void cargarXmlDesdeArchivo(Ingreso ingreso, PersUsua persUsua, String claveAcceso, String correo)
			throws ExceptArchivoNoExiste, Exception {

		cargarParametros();

		Path path = Path.of(parametroRutaDescargados.getDescri() + claveAcceso + ".xml");
		File file = new File(parametroRutaDescargados.getDescri() + claveAcceso + ".xml");

		if (!file.exists()) {
			throw new ExceptArchivoNoExiste("Documento " + claveAcceso + " no Existe");
		}

		// Separa en comprobante de la autorizacion
		String comprobante = separarComprobante(parametroRutaDescargados.getDescri() + claveAcceso + ".xml");

		// Sobreescribe el comprobante sin autorizacion
		// del comprobante seleccionado, sino no reconoce al hace marshall
		crearComprobanteXml(comprobante, path);

		cargarDatosDeXml(file, ingreso, persUsua, claveAcceso, correo);
	}

	/**
	 * Develve en un string solo el contenido del comprobante separando de la
	 * autorizacion
	 */
	public String separarComprobante(String rutaDescargados) {

		String cdataNode = "comprobante";

		String comprobante = "";

		try (InputStream in = new BufferedInputStream(new FileInputStream(rutaDescargados))) {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			NodeList elements = doc.getElementsByTagName(cdataNode);

			for (int i = 0; i < elements.getLength(); i++) {
				Node e = elements.item(i);
				comprobante = e.getTextContent();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return comprobante;
	}
	
	/**
	 * Sobreescribe en comprobante seleccionado sin la autorizacion
	 * 
	 */
	public void crearComprobanteXml(String Comprobante, Path path) {

		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {

			bw.write(Comprobante);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cargarXmlDesdeSri(Ingreso ingreso, PersUsua persUsua, String claveAcceso, String correo)
			throws ExceptArchivoNoExiste, Exception {

		cargarParametros();

		try {
			this.descargarXmlSri(claveAcceso);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al descargar documento " + claveAcceso);
		}

		File file = new File(parametroRutaDescargados.getDescri() + claveAcceso + ".xml");

		if (!file.exists()) {
			throw new ExceptArchivoNoExiste("Documento " + claveAcceso + " no Existe");
		}

		cargarDatosDeXml(file, ingreso, persUsua, claveAcceso, correo);
	}

	public void cargarDatosDeXml(File file, Ingreso ingreso, PersUsua persUsua, String claveAcceso, String correo)
			throws ExceptArchivoNoExiste, Exception {

		Object object = new Object();

		try {
			object = unMarshalFactura(file);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new Exception(
					"Error al procesar archivo xml, Verifique que el documento tenga autorización - unMarshall");
		}

		if (object instanceof Factura) {

			RolPrec rolPrecPredet = new RolPrec();

			Factura factura = (Factura) object;

//				Buscar Ingreso
//				Con estos datos se buscar el ingreso para determinar si se carga o no
			PersProv persProvBuscar = new PersProv(new Persona(factura.getInfoTributaria().getRuc(), null, null, true));

			Ingreso ingresoBuscar = new Ingreso(new Sucursal(), new DocuIngr(new Documento()), persProvBuscar);
			ingresoBuscar.setSerie1(factura.getInfoTributaria().getEstab());
			ingresoBuscar.setSerie2(factura.getInfoTributaria().getPtoEmi());
			ingresoBuscar.setNumero(Integer.valueOf(factura.getInfoTributaria().getSecuencial()));

			List<Ingreso> ingresos = new ArrayList<>();

			try {

				ingresoLista.filasPagina(Integer.parseInt(parametroFilasPagina.getDescri()));
				ingresos = ingresoLista.buscar(ingresoBuscar, null);

			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error al buscar ingreso");
			}

			Optional<Ingreso> streamIngreso = ingresos.stream().filter(i -> !i.getEstado().equals("AN")).findAny();

			if (streamIngreso.isPresent()) {
				throw new Exception("Documento ya esta cargado en el sistema");
			}
//				Fin Buscar Ingreso

//				this.persProv = cargarPersProv(factura.getInfoTributaria());
			PersProv persProv = cargarPersProv(factura.getInfoTributaria(), correo);
			ingreso.setPersProv(persProv);

//				ingresoTemporal = cargarIngreso(ingreso.getSucursal(), persProv, factura);
			cargarIngreso(ingreso, factura);
//				ingresoTemporal.setDocuIngr(ingreso.getDocuIngr());
//				ingresoTemporal.setDimm(ingreso.getDimm());
//				this.ingreso = ingresoTemporal;

//				Copia los porcentajes de retencion
//				Se coloca justo aqui luego de haber registrado en ingreso
			List<PersProvDimm> persProvDimms = buscarPersProvDimm(ingreso);
			copiarPersProvDimmAIngrDimm(persProvDimms, ingreso);
			
			RolPrec rolPrec = new RolPrec(new Precio(), null, ingreso.getSucursal(), null, null);
//				Set<RolPersUsua> rolPersUsuas = variablesSesion.getPersUsua().getRolPersUsuas();
			Set<RolPersUsua> rolPersUsuas = persUsua.getRolPersUsuas();

//				Buscar en RolPrec el precio predeterminado que tiene el rol para crear ingrDeta
			try {
				rolPrecPredet = rolPrecLista.buscarPrecioPred(rolPrec, rolPersUsuas);
			} catch (Exception e) {
//					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
//							null, "Error al buscar precio predeterminado"));
				e.printStackTrace();
				throw new Exception("Error al buscar precio predeterminado");
			}

//				if (cargarDeta) {

//					this.ingrDetaDataTable = revisarIngrDetas(ingresoTemporal, rolPrecPredet, factura);
			List<IngrDeta> ingrDetas = revisarIngrDetas(ingreso, rolPrecPredet, factura);

//					if (!ingrDetaDataTable.isEmpty()) {
			if (!ingrDetas.isEmpty()) {
//						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
//								null,
//								"El documento tiene productos no registrados, Cree los productos nuevos luego cargue nuevamente el documento"));
				throw new Exception(
						"Existe productos no registrados, Cree los productos nuevos luego cargue nuevamente el documento");
			}

//					this.ingrDetaDataTable = cargarIngrDetas(ingresoTemporal, rolPrecPredet, factura);
			ingrDetas = cargarIngrDetas(ingreso, rolPrecPredet, factura);

			Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<>();
//					for (IngrDeta ingrDeta : this.ingrDetaDataTable) {
			for (IngrDeta ingrDeta : ingrDetas) {

				ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
				Producto producto = new Producto(prodGrup, null, ingrDeta.getProducto().getCodigo(), null, true);

//				ProdPrec prodPrec = new ProdPrec(this.getVariablesSesion().getSucursal(), new Precio(), producto);
				ProdPrec prodPrec = new ProdPrec(ingreso.getSucursal(), new Precio(), producto);

				ingrDetaPrecs = this.crearIngrDetaPrec(ingrDeta, prodPrec, ingreso, persUsua);
				ingrDeta.setIngrDetaPrecs(ingrDetaPrecs);

//				Se crea impuestos y retenciones del producto que se esta agregando 
				this.crearIngrDetaImpuRete(ingrDeta);

//				Asignar el costo a la lista de productos
				ProdCost prodCostBuscar = new ProdCost();
				List<ProdCost> prodCosts = new ArrayList<ProdCost>();

//				prodCostBuscar.setSucursal(this.variablesSesion.getSucursal());
				prodCostBuscar.setSucursal(ingreso.getSucursal());
				prodCostBuscar.setProducto(ingrDeta.getProducto());

//				Asigna el costo a cada elemento de la lista de prodPrec
//				prodCosts = this.buscarProdCosts(prodCostBuscar);
				try {
					prodCosts = buscarProdCosts(prodCostBuscar);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("Error al buscar costos");
				}
				for (ProdCost prodCost : prodCosts) {
					ingrDeta.getProducto().setProdCost(prodCost);
				}

				ingrDeta.setIngreso(ingreso);
			}
//				}
			Set<IngrDeta> ingrDetasFinal = new HashSet<IngrDeta>(ingrDetas);
			ingreso.setIngrDetas(ingrDetasFinal);

			if (ingreso.getPersProv().getPersonaId() == null) {
				insertarPersProv(persProv);
			}
		}
	}

	private void crearIngrDetaImpuRete(IngrDeta ingrDeta) throws Exception {

		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<>();
		ingrDetaImpus = this.crearImpuestos("IMPUE", ingrDeta);

//		Buscar Iva del producto, si tiene iva se crea ingrDetaImpu sino no se crea ingrDetaImpu
		ProdDimm prodDimmBuscar = new ProdDimm();
		prodDimmBuscar.setDimm(new Dimm());
		prodDimmBuscar.setProducto(ingrDeta.getProducto());

		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();
		try {
			prodDimms = this.buscarProdDimms(prodDimmBuscar);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al crear impuestos de productos");
		}

		ProdDimm prodDimmIva = new ProdDimm();

		for (ProdDimm prodDimmRecorrer : prodDimms) {
			if (prodDimmRecorrer.getDimm().getTablaRefe().equals("Tabla12")) {

				prodDimmIva = prodDimmRecorrer;
			}
		}

//		TODO: arreglar esto no graba IngrDimm pero si ingrDetaImpu y calcula retenciones
		try {

//			Buscar el Producto para relacionar con prodGrup y poder saber si es Mercaderia o Gasto-Servicio
//			Producto producto = this.compra.buscarProductoPorId(ingrDeta.getProducto().getProductoId());
			Producto producto = buscarProductoPorId(ingrDeta.getProducto().getProductoId());

//			Crea Retenciones dependiendo de si los productos son Mercaderia o Gasto-Servicio
//			y de acuerdo a eso selecciona la lista de impuesto que va a crear
			if (producto.getProdGrup().getTipo().equals("Mercaderia")) {

//				for (IngrDimm ingrDimm : this.ingrDimms) {
				for (IngrDimm ingrDimm : ingrDeta.getIngreso().getIngrDimms()) {

					if (ingrDimm.getTipo().equals("RENTA_BIEN")) {
						ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
					} else if (ingrDimm.getTipo().equals("IVA_BIEN")) {
//						if (prodDimmIva.getDimm().getDimmId().equals(13030)) {
						if (prodDimmIva.getDimm().getPorcen().compareTo(BigDecimal.ZERO) > 0) {
							ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
						}
					}
				}

			} else {
//				El producto es un gasto
//				for (IngrDimm ingrDimm : this.ingrDimms) {
				for (IngrDimm ingrDimm : ingrDeta.getIngreso().getIngrDimms()) {

					if (ingrDimm.getTipo().equals("RENTA_SERV")) {
						ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
					} else if (ingrDimm.getTipo().equals("IVA_SERV")) {
//						if (prodDimmIva.getDimm().getDimmId().equals(13030)) {
						if (prodDimmIva.getDimm().getPorcen().compareTo(BigDecimal.ZERO) > 0) {
							ingrDetaImpus.add(this.crearRetenciones(ingrDeta, ingrDimm));
						}
					}
				}
			}

			ingrDeta.setIngrDetaImpus(ingrDetaImpus);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar ID del producto");
		}
	}

	// Crear retenciones de acuerdo a ingrDimm
	// El iva se genera al agregar el producto las retenciones se genera al
	// selecionar el proveedor
//	public Set<IngrDetaImpu> crearIngrDetaImpu(IngrDeta ingrDeta, List<IngrDimm> ingrDimms) {
	public IngrDetaImpu crearRetenciones(IngrDeta ingrDeta, IngrDimm ingrDimm) {

		IngrDetaImpu ingrDetaImpu = this.crearIngrDetaImpu(ingrDeta, ingrDimm.getDimm());

		if (ingrDimm.getTipo().equals("RENTA_BIEN") || ingrDimm.getTipo().equals("RENTA_SERV")) {
			ingrDetaImpu.setTipo("RR");
		} else if (ingrDimm.getTipo().equals("IVA_BIEN") || ingrDimm.getTipo().equals("IVA_SERV")) {
			ingrDetaImpu.setTipo("RI");
		}

		return ingrDetaImpu;
	}

	public IngrDetaImpu crearIngrDetaImpu(IngrDeta ingrDeta, Dimm dimm) {

		IngrDetaImpu ingrDetaImpu = new IngrDetaImpu();
		ingrDetaImpu.setIngrDeta(ingrDeta);

		ingrDetaImpu.setDimm(dimm);
		ingrDetaImpu.setDescri(dimm.getDescri());
		ingrDetaImpu.setCodigo(dimm.getCodigo());
		ingrDetaImpu.setPorcen(dimm.getPorcen());
		ingrDetaImpu.setFactor(dimm.getFactor());
		ingrDetaImpu.setTipo(null);

		return ingrDetaImpu;
	}

	public Set<IngrDetaImpu> crearImpuestos(String ingrDetaTipoImpu, IngrDeta ingrDeta) {

		ProdDimm prodDimm = new ProdDimm();
		prodDimm.setDimm(new Dimm());

		List<ProdDimm> prodDimms = new ArrayList<ProdDimm>();
		Set<IngrDetaImpu> ingrDetaImpus = new HashSet<IngrDetaImpu>();

		prodDimm.setProducto(ingrDeta.getProducto());

		try {

//			prodDimms = compra.buscarProdDimms(prodDimm);
			prodDimms = buscarProdDimms(prodDimm);

			for (ProdDimm prodDimm2 : prodDimms) {

				IngrDetaImpu ingrDetaImpu = this.crearIngrDetaImpu(ingrDeta, prodDimm2.getDimm());

				if (ingrDetaTipoImpu.equals("IMPUE")) {

					if (prodDimm2.getDimm().getDimmId() >= 13000 && prodDimm2.getDimm().getDimmId() <= 13099) {
						ingrDetaImpu.setTipo("IVA");
					} else if (prodDimm2.getDimm().getDimmId() >= 13100 && prodDimm2.getDimm().getDimmId() <= 13199) {
						ingrDetaImpu.setTipo("OTR");
					} else if (prodDimm2.getDimm().getDimmId() >= 11000 && prodDimm2.getDimm().getDimmId() <= 11100) {
//						El ultimo código 11100 es el ice de fundas plasticas
						ingrDetaImpu.setTipo("ICE");
					} else if (prodDimm2.getDimm().getDimmId() >= 11800 && prodDimm2.getDimm().getDimmId() <= 11900) {
						ingrDetaImpu.setTipo("IRBPNR");
					}

				}

				if (ingrDetaImpu.getTipo() != null) {
					ingrDetaImpus.add(ingrDetaImpu);
				}
			}

		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
//					"Excepcion - Error al crear impuestos y retenciones"));
			e.printStackTrace();
		}

		return ingrDetaImpus;
	}

	public Set<IngrDetaPrec> crearIngrDetaPrec(IngrDeta ingrDeta, ProdPrec prodPrec, Ingreso ingreso, PersUsua persUsua)
			throws Exception {

		List<ProdPrec> prodPrecs = new ArrayList<ProdPrec>();
		Set<IngrDetaPrec> ingrDetaPrecs = new HashSet<IngrDetaPrec>();

		prodPrecs = buscarProdPrecs(prodPrec);

		RolPrec rolPrec = new RolPrec(new Precio(), new Rol(), new Sucursal(), null, true);
		// Cargar precios

//		rolPrec.setRol(new Rol());
//		rolPrec.setPrecio(new Precio());
//		rolPrec.setSucursal(new Sucursal());
//		rolPrec.setAcceso(true);

		List<RolPrec> rolPrecs;
		try {
			rolPrecs = buscarRolPrec(rolPrec);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar precios del rol");
		}
		try {

//			prodPrecs = prodPrecLista.filtrarProdPrec(prodPrecs, variablesSesion.getPersUsua(),
//					variablesSesion.getRolPrecs(), variablesSesion.getSucursal());

			prodPrecs = prodPrecLista.filtrarProdPrec(prodPrecs, persUsua, rolPrecs, ingreso.getSucursal());

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al filtrar precios de productos");
		}

		prodPrecs.forEach((ProdPrec prodPrecCopiar) -> {

			IngrDetaPrec ingrDetaPrec = new IngrDetaPrec();
			ingrDetaPrec.setIngrDeta(ingrDeta);
			ingrDetaPrec.setPrecio(prodPrecCopiar.getPrecio());
			ingrDetaPrec.setFactor(prodPrecCopiar.getFactor());
			ingrDetaPrec.setPrecioConImpu(prodPrecCopiar.getPrecioConImpu());
			ingrDetaPrec.setPrecioSinImpu(prodPrecCopiar.getPrecioSinImpu());
			ingrDetaPrec.setUtilid(prodPrecCopiar.getUtilid());

			ingrDetaPrecs.add(ingrDetaPrec);

		});

		return ingrDetaPrecs;
	}

	public List<RolPrec> buscarRolPrec(RolPrec rolPrec) throws Exception {

		List<RolPrec> rolPrecs = new ArrayList<RolPrec>();

		try {
			rolPrecs = rolPrecLista.buscar(rolPrec, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar precios del rol");
		}
		return rolPrecs;
	}

	public List<IngrDeta> cargarIngrDetas(Ingreso ingreso, RolPrec rolPrecPred, Factura factura) {

		List<IngrDeta> ingrDetas = new ArrayList<>();

		for (Detalle detalle : factura.getDetalles().getDetalle()) {

			ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
			Producto producto = new Producto(prodGrup, null, detalle.getCodigoPrincipal(), null, true);

//			ProdPrec prodPrecBuscar = new ProdPrec(this.getVariablesSesion().getSucursal(), rolPrecPred.getPrecio(), producto);
			ProdPrec prodPrecBuscar = new ProdPrec(ingreso.getSucursal(), rolPrecPred.getPrecio(), producto);

			List<ProdPrec> prodPrecs = buscarProdPrecs(prodPrecBuscar);

			if (!prodPrecs.isEmpty()) {
//				Cuando se busca el código del producto puede ser que haya mas de uno, 
//				ya que la busqueda se hace por codigo no por codigo de barra y el codigo admite duplicados
//				entonces varios productos pueden tener el mismo codigo.  
//				Se recorre la lista de productos y se añaden todos a la lista, el usuario tendra que eliminar
//				los productos que no pertenezcan al documento
				for (ProdPrec prodPrec : prodPrecs) {

//					Se pregunta si el codigo esta registrado para dos productos, se le pasa un mensaje al usuario
					if (prodPrecs.size() > 1) {

//						FacesContext.getCurrentInstance().addMessage(null,
//								new FacesMessage(FacesMessage.SEVERITY_INFO, null,
//										"El Código " + prodPrec.getProducto().getCodigo() + ", con código de barra "
//												+ prodPrec.getProducto().getCodigoBarra()
//												+ " esta registrado en dos productos, revisar detalle del documento"));
					}

					IngrDeta ingrDeta = new IngrDeta(ingreso, prodPrec.getProducto(), prodPrec.getPrecio(),
							ingreso.getFechaRegi(), ingreso.getFechaEmis().atTime(LocalTime.now()),
							detalle.getCantidad(), prodPrec.getFactor(), detalle.getPrecioUnitario(),
							detalle.getPrecioUnitario(), BigDecimal.ZERO, detalle.getDescuento(), BigDecimal.ZERO,
							prodPrec.getPrecioConImpu());

					ingrDetas.add(ingrDeta);
				}
			}
		}

		return ingrDetas;
	}
	
	public List<PersProvDimm> buscarPersProvDimm(Ingreso ingreso) throws Exception {

		PersProvDimm persProvDimm = new PersProvDimm();
		persProvDimm.setPersProv(ingreso.getPersProv());
		persProvDimm.setDimm(new Dimm());

		List<PersProvDimm> persProvDimms = new ArrayList<>();
		try {
			persProvDimms = this.buscarPersProvDimms(persProvDimm, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar retenciones del proveedor");
		}
		
		return persProvDimms;
	}

	public void copiarPersProvDimmAIngrDimm(List<PersProvDimm> persProvDimms, Ingreso ingreso) {

		var ingrDimms = new HashSet<IngrDimm>();

		for (PersProvDimm persProvDimm : persProvDimms) {

			IngrDimm ingrDimm = new IngrDimm();

			ingrDimm.setIngreso(ingreso);
			ingrDimm.setDimm(persProvDimm.getDimm());
			ingrDimm.setTipo(persProvDimm.getTipo());

			ingrDimms.add(ingrDimm);

		}
		
		ingreso.setIngrDimms(ingrDimms);
	}

	public void cargarIngreso(Ingreso ingreso, Factura factura) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//		Ingreso ingreso = new Ingreso();
//		ingreso.setSucursal(sucursal);
		ingreso.setBodega(new Bodega(1));
//		ingreso.setDocuIngr(getDocuIngr());
//		ingreso.setPersProv(persProv);
//		ingreso.setDimm(getDimm());
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

	}

	public Factura unMarshalFactura(File file) throws JAXBException {

		JAXBContext context = JAXBContext.newInstance(Factura.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();

		return (Factura) unmarshaller.unmarshal(file);
	}

//	Revisa si todos los productos del documento consten en el sistema
//	La lista que devuelve es la de los productos que no existen en el sistema
	public List<IngrDeta> revisarIngrDetas(Ingreso ingreso, RolPrec rolPrecPred, Factura factura) {

		List<IngrDeta> ingrDetas = new ArrayList<>();

		for (Detalle detalle : factura.getDetalles().getDetalle()) {

			ProdGrup prodGrup = new ProdGrup(null, "Todo", true, false, false, false, true);
			Producto producto = new Producto(prodGrup, null, detalle.getCodigoPrincipal(), null, true);

//			ProdPrec prodPrec = new ProdPrec(this.getVariablesSesion().getSucursal(), rolPrecPred.getPrecio(), producto);
			ProdPrec prodPrec = new ProdPrec(ingreso.getSucursal(), rolPrecPred.getPrecio(), producto);

			List<ProdPrec> prodPrecs = buscarProdPrecs(prodPrec);

			if (prodPrecs.isEmpty()) {

				ProdPrec prodPrecNuevo = new ProdPrec(null, null, producto);
//				Se coloca el codigo de barra ya que la busqueda se hace por codigo
//				y por lo tanto como se esta creando un nuevo prodPrec no tiene esta valor
				prodPrecNuevo.getProducto().setCodigoBarra(detalle.getCodigoPrincipal());

				IngrDeta ingrDeta = new IngrDeta(ingreso, prodPrecNuevo.getProducto(), prodPrecNuevo.getPrecio(),
						ingreso.getFechaRegi(), ingreso.getFechaEmis().atTime(LocalTime.now()), detalle.getCantidad(),
						prodPrecNuevo.getFactor(), detalle.getPrecioUnitario(), detalle.getPrecioUnitario(),
						BigDecimal.ZERO, detalle.getDescuento(), BigDecimal.ZERO, prodPrecNuevo.getPrecioConImpu());
				ingrDetas.add(ingrDeta);
			}
		}

		return ingrDetas;
	}

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec) {

		List<ProdPrec> prodPrecs = new ArrayList<>();

		try {

			prodPrecs = prodPrecLista.buscar(prodPrec, null, null);

		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null,
//					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar productos"));
//			e.printStackTrace();
		}

		return prodPrecs;
	}

	public PersProv cargarPersProv(InfoTributaria infoTributaria, String correo) throws Exception {

		PersProv persProvBuscar = new PersProv(new Persona(infoTributaria.getRuc(), null, null, true), true);
		PersProv persProv = new PersProv(new Persona(), true);

		List<PersProv> persProvs = new ArrayList<PersProv>();

		persProvLista.filasPagina(Integer.parseInt(parametroFilasPagina.getDescri()));
		try {
			persProvs = persProvLista.buscar(persProvBuscar, null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al buscar proveedores");
		}

		if (persProvs.isEmpty()) {
			persProv = crearPersProv(infoTributaria, correo);
		} else {
			persProv = persProvs.get(0);
		}

		return persProv;
	}

	public PersProv crearPersProv(InfoTributaria infoTributaria, String correo) {

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

//		Graba el correo del proveedor ingresado en la pagina 
//		persProv.getPersona().setCorreo(this.persProvCorreo);
		persProv.getPersona().setCorreo(correo);

		return persProv;
	}

	public List<ProvGrup> buscarProvGrups(ProvGrup provGrup) {

		List<ProvGrup> provGrups = new ArrayList<>();

		try {
			provGrups = provGrupLista.buscar(provGrup, null);
		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null,
//					"Excepcion - Error al buscar grupo de proveedores"));
			e.printStackTrace();
		}

		return provGrups;
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

	Parametro parametroFilasPagina = new Parametro();
	Parametro parametroProxyIp = new Parametro();
	Parametro parametroProxyPuerto = new Parametro();
	Parametro parametroUrlProduccion = new Parametro();
	Parametro parametroUrlPruebas = new Parametro();
	Parametro parametroRutaDescargados = new Parametro();

	public void cargarParametros() throws Exception {
		parametroFilasPagina = parametroRegis.buscarPorId(Parametro.class, 6100);
		parametroProxyIp = parametroRegis.buscarPorId(Parametro.class, 3211);
		parametroProxyPuerto = parametroRegis.buscarPorId(Parametro.class, 3212);
		parametroUrlProduccion = parametroRegis.buscarPorId(Parametro.class, 3220);
		parametroUrlPruebas = parametroRegis.buscarPorId(Parametro.class, 3221);
		parametroRutaDescargados = parametroRegis.buscarPorId(Parametro.class, 4251);
	}

	public void descargarXmlSri(String claveAcceso) throws Exception {

		RespuestaComprobante respuestaComprobante;
		AutorizacionDTO autorizacionDTO;

		respuestaComprobante = this.verificarAutorizacion(claveAcceso);

		if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {

			autorizacionDTO = this.obtenerEstadoAutorizacion(respuestaComprobante);

			// Crea el archivo con la autorizacion y copia a la carpeta autorizados
			// autorizacionComprobantes.validarRespuestaAutorizacion(autorizacionDTO,
			// claveAcce, "C:\\saolan\\soem\\comprobantes\\generados\\");
			autorizacionComprobantes.crearArchivoXml(autorizacionDTO, claveAcceso + ".xml",
					parametroRutaDescargados.getDescri());

		}
	}

	public RespuestaComprobante verificarAutorizacion(String claveAcceso) throws Exception {

		RespuestaComprobante respuestaComprobante = new RespuestaComprobante();

		respuestaComprobante = autorizacionComprobantes.autorizarComprobante(parametroProxyIp.getDescri(),
				parametroProxyPuerto.getDescri(), "2", parametroUrlProduccion.getDescri(),
				parametroUrlPruebas.getDescri(), "AutorizacionComprobantesOffline", claveAcceso);

		return respuestaComprobante;
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

	public Parametro buscarParametroPorId(Parametro parametroBuscar) throws Exception {

		return parametroRegis.buscarPorId(Parametro.class, parametroBuscar.getParametroId());

	}

}