package ec.com.tecnointel.soem.serWebClientSri.general;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class RespuestaDateConverter implements Converter {
	
	public boolean canConvert(Class clazz) {
		return clazz.equals(XMLGregorianCalendar.class);
	}

	public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
		
		XMLGregorianCalendar i = (XMLGregorianCalendar) o;
		writer.setValue(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(i.toGregorianCalendar().getTime()));
		
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
		
		Date date = null;
		
		try {
			date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").parse(reader.getValue());
		} catch (ParseException ex) {
			Logger.getLogger(RespuestaDateConverter.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		
//		XMLGregorianCalendarImpl item = new XMLGregorianCalendarImpl(cal);
//		Esto reemplaza la linea de arriba
//		TODO: REvisar
		XMLGregorianCalendar item = null;
		try {
			item = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return item;
	}
}
