package ec.com.tecnointel.soem.reportAtsSri.controlador;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

import ec.com.tecnointel.soem.atsSri.registroInt.GenerarAtsSriInt;
import ec.com.tecnointel.soem.general.controlador.GeneraJasperReportes;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class AtsSriControl implements Serializable {

	private static final long serialVersionUID = -6008212939202399422L;

	@Inject
	GenerarAtsSriInt generarAtsSri;

	@Inject
	GeneraJasperReportes generaJasperReportes;

	LocalDate fechaDesd;
	LocalDate fechaHast;

	public void generarXmlAtsSri() {

		String nombreReporte = "anexoSri.xml";
		String tipoArchivo = "text/xml";
		
		try {
			ByteArrayOutputStream byteArrayOutputStream = generarAtsSri.generarAtsSriXml(new Sucursal(), fechaDesd, fechaHast);
			byteArrayOutputStream.close();
			
			generaJasperReportes.abrirReporte(byteArrayOutputStream.toByteArray(), nombreReporte, tipoArchivo);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

//	public ByteArrayOutputStream marshalIvaType(IvaType ivaType, String rutaGenerados) throws Exception {
//
//		String estado = null;
//		String nombreArchivo = "anexoTra.xml";
//
//		JAXBContext context = JAXBContext.newInstance(new Class[] { IvaType.class });
//
//		Marshaller marshaller = context.createMarshaller();
//		marshaller.setProperty("jaxb.encoding", "UTF-8");
//		marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
//		
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		OutputStreamWriter out = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
//		
//		marshaller.marshal(ivaType, out);
//		
//		Estas Lineas crean un archivo en la ruta especificada
//		OutputStream outputStream = new FileOutputStream(
//				rutaGenerados + "anexoSri" + ".xml");
//		byteArrayOutputStream.writeTo(outputStream);
//		byteArrayOutputStream.close();
//		outputStream.close();
//		return byteArrayOutputStream;
		
//		generaJasperReportes.abrirReporte(byteArrayOutputStream.toByteArray(), nombreArchivo, "text/xml");

//		return estado;
//	}
	
	public LocalDate getFechaDesd() {
		return fechaDesd;
	}

	public void setFechaDesd(LocalDate fechaDesd) {
		this.fechaDesd = fechaDesd;
	}

	public LocalDate getFechaHast() {
		return fechaHast;
	}

	public void setFechaHast(LocalDate fechaHast) {
		this.fechaHast = fechaHast;
	}

}
