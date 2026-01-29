package ec.com.tecnointel.soem.serWebClientSri.general;

import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import ec.com.tecnointel.soem.serWebClientSri.autorizacion.Autorizacion;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.Mensaje;
import ec.com.tecnointel.soem.serWebClientSri.autorizacion.RespuestaComprobante;

public class XStreamUtil {
	
//	  public static XStream getLoteXStream()
//	  {
//	    XStream xstream = new XStream(new XppDriver()
//	    {
//	      public HierarchicalStreamWriter createWriter(Writer out)
//	      {
//	        new PrettyPrintWriter(out)
//	        {
//	          protected void writeText(QuickWriter writer, String text)
//	          {
//	            writer.write(text);
//	          }
//	        };
//	      }
//	    });
//	    
//	    xstream.alias("lote", LoteXml.class);
//	    xstream.alias("comprobante", ComprobanteXml.class);
//	    
//	    xstream.registerConverter(new ComprobanteXmlConverter());
//	    
//	    return xstream;
//	  }

//	public HierarchicalStreamWriter createWriter(Writer out) {
//
//		PrettyPrintWriter prettyPrintWriter = new PrettyPrintWriter(out) 
//
//		{
//			protected void writeText(QuickWriter writer, String text) {
//				writer.write(text);
//			}
//		};
//		return prettyPrintWriter;
//	}
	
	// el return no estaba
	public static XStream getRespuestaXStream() {

		XStream xstream = new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					protected void writeText(QuickWriter writer, String text) {
						writer.write(text);
					}
				};
			}
		});

		xstream.alias("respuesta", RespuestaComprobante.class);
		xstream.alias("autorizacion", Autorizacion.class);
		
//		xstream.alias("fechaAutorizacion", XMLGregorianCalendarImpl.class);
//		xstream.alias("fechaAutorizacion", XMLGregorianCalendar.class);
		xstream.alias("mensaje", Mensaje.class);
		xstream.registerConverter(new RespuestaDateConverter());

		return xstream;
	}
	  
//	  public static XStream getRespuestaLoteXStream()
//	  {
//	    XStream xstream = new XStream(new XppDriver()
//	    {
//	      public HierarchicalStreamWriter createWriter(Writer out)
//	      {
//	        new PrettyPrintWriter(out)
//	        {
//	          protected void writeText(QuickWriter writer, String text)
//	          {
//	            writer.write(text);
//	          }
//	        };
//	      }
//	    });
//	    xstream.alias("respuesta", RespuestaLote.class);
//	    xstream.alias("autorizacion", Autorizacion.class);
//	    xstream.alias("fechaAutorizacion", XMLGregorianCalendarImpl.class);
//	    xstream.alias("mensaje", Mensaje.class);
//	    xstream.registerConverter(new RespuestaDateConverter());
//	    
//	    return xstream;
//	  }
}
