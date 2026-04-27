package ec.com.tecnointel.soem.ingreso.registroImp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
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

	public Parametro buscarParametroPorId(Parametro parametroBuscar) throws Exception {
		return parametroRegis.buscarPorId(Parametro.class, parametroBuscar.getParametroId());
	}

	public boolean existeNumeroAutorizacion(String autori) {
		return ingresoLista.existeNumeroAutorizacion(autori);
	}

}