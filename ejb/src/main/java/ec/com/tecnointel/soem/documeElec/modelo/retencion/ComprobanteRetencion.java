package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.documeElec.modelo.InfoTributaria;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "infoTributaria", "infoCompRetencion", "docsSustento", "infoAdicional" })
@XmlRootElement(name = "comprobanteRetencion")
public class ComprobanteRetencion {

	@XmlElement(required = true)
	protected InfoTributaria infoTributaria;
	@XmlElement(required = true)
	protected InfoCompRetencion infoCompRetencion;
	@XmlElement(required = true)
	protected DocsSustento docsSustento;
	protected InfoAdicional infoAdicional;
	@XmlAttribute(name = "id")
	protected String id;
	@XmlAttribute(name = "version", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NMTOKEN")
	protected String version;

	public InfoTributaria getInfoTributaria() {
		return infoTributaria;
	}

	public void setInfoTributaria(InfoTributaria value) {
		this.infoTributaria = value;
	}

	public InfoCompRetencion getInfoCompRetencion() {
		return infoCompRetencion;
	}

	public void setInfoCompRetencion(InfoCompRetencion value) {
		this.infoCompRetencion = value;
	}

	public DocsSustento getDocsSustento() {
		return docsSustento;
	}

	public void setDocsSustento(DocsSustento value) {
		this.docsSustento = value;
	}

	public InfoAdicional getInfoAdicional() {
		return infoAdicional;
	}

	public void setInfoAdicional(InfoAdicional value) {
		this.infoAdicional = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String value) {
		this.version = value;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "docSustento" })
	public static class DocsSustento {

		@XmlElement(required = true)
		protected List<DocSustento> docSustento;

		public List<DocSustento> getDocSustento() {
			if (docSustento == null) {
				docSustento = new ArrayList<DocSustento>();
			}
			return this.docSustento;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "campoAdicional" })
	public static class InfoAdicional {

		@XmlElement(required = true)
		protected List<ComprobanteRetencion.InfoAdicional.CampoAdicional> campoAdicional;

		public List<ComprobanteRetencion.InfoAdicional.CampoAdicional> getCampoAdicional() {
			if (campoAdicional == null) {
				campoAdicional = new ArrayList<ComprobanteRetencion.InfoAdicional.CampoAdicional>();
			}
			return this.campoAdicional;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "value" })
		public static class CampoAdicional {

			@XmlValue
			protected String value;
			@XmlAttribute(name = "nombre", required = true)
			protected String nombre;

			public String getValue() {
				return value;
			}

			public void setValue(String value) {
				this.value = value;
			}

			public String getNombre() {
				return nombre;
			}

			public void setNombre(String value) {
				this.nombre = value;
			}
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "fechaEmision", "dirEstablecimiento", "contribuyenteEspecial",
			"obligadoContabilidad", "tipoIdentificacionSujetoRetenido", "tipoSujetoRetenido", "parteRel",
			"razonSocialSujetoRetenido", "identificacionSujetoRetenido", "periodoFiscal" })
	public static class InfoCompRetencion {

		@XmlElement(required = true)
		protected String fechaEmision;
		protected String dirEstablecimiento;
		protected String contribuyenteEspecial;
		@XmlSchemaType(name = "string")
		protected String obligadoContabilidad;
		@XmlElement(required = true)
		protected String tipoIdentificacionSujetoRetenido;
		protected String tipoSujetoRetenido;
		@XmlElement(required = true)
		protected String parteRel;
		@XmlElement(required = true)
		protected String razonSocialSujetoRetenido;
		@XmlElement(required = true)
		protected String identificacionSujetoRetenido;
		@XmlElement(required = true)
		protected String periodoFiscal;

		public String getFechaEmision() {
			return fechaEmision;
		}

		public void setFechaEmision(String value) {
			this.fechaEmision = value;
		}

		public String getDirEstablecimiento() {
			return dirEstablecimiento;
		}

		public void setDirEstablecimiento(String value) {
			this.dirEstablecimiento = value;
		}

		public String getContribuyenteEspecial() {
			return contribuyenteEspecial;
		}

		public void setContribuyenteEspecial(String value) {
			this.contribuyenteEspecial = value;
		}

		public String getObligadoContabilidad() {
			return obligadoContabilidad;
		}

		public void setObligadoContabilidad(String value) {
			this.obligadoContabilidad = value;
		}

		public String getTipoIdentificacionSujetoRetenido() {
			return tipoIdentificacionSujetoRetenido;
		}

		public void setTipoIdentificacionSujetoRetenido(String value) {
			this.tipoIdentificacionSujetoRetenido = value;
		}

		public String getTipoSujetoRetenido() {
			return tipoSujetoRetenido;
		}

		public void setTipoSujetoRetenido(String value) {
			this.tipoSujetoRetenido = value;
		}

		public String getParteRel() {
			return parteRel;
		}

		public void setParteRel(String value) {
			this.parteRel = value;
		}

		public String getRazonSocialSujetoRetenido() {
			return razonSocialSujetoRetenido;
		}

		public void setRazonSocialSujetoRetenido(String value) {
			this.razonSocialSujetoRetenido = value;
		}

		public String getIdentificacionSujetoRetenido() {
			return identificacionSujetoRetenido;
		}

		public void setIdentificacionSujetoRetenido(String value) {
			this.identificacionSujetoRetenido = value;
		}

		public String getPeriodoFiscal() {
			return periodoFiscal;
		}

		public void setPeriodoFiscal(String value) {
			this.periodoFiscal = value;
		}
	}
}
