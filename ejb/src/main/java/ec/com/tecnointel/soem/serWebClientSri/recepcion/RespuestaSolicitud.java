package ec.com.tecnointel.soem.serWebClientSri.recepcion;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="respuestaSolicitud", propOrder={"estado", "comprobantes"})
public class RespuestaSolicitud {
	
	  protected String estado;
	  protected Comprobantes comprobantes;
	  
	  public String getEstado()
	  {
	    return this.estado;
	  }
	  
	  public void setEstado(String value)
	  {
	    this.estado = value;
	  }
	  
	  public Comprobantes getComprobantes()
	  {
	    return this.comprobantes;
	  }
	  
	  public void setComprobantes(Comprobantes value)
	  {
	    this.comprobantes = value;
	  }
	  
	  @XmlAccessorType(XmlAccessType.FIELD)
	  @XmlType(name="", propOrder={"comprobante"})
	  public static class Comprobantes
	  {
	    protected List<Comprobante> comprobante;
	    
	    public List<Comprobante> getComprobante()
	    {
	      if (this.comprobante == null) {
	        this.comprobante = new ArrayList();
	      }
	      return this.comprobante;
	    }
	  }
	  
}
