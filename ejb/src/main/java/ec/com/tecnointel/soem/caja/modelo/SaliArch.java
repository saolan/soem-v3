package ec.com.tecnointel.soem.caja.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "sali_arch")
@NamedEntityGraph(name = "saliArch.Graph", attributeNodes = {
		@NamedAttributeNode(value = "cajaDocuEgre")
		}
)
public class SaliArch implements java.io.Serializable {

	private Integer saliArchId;
	private String salida;
	private String format;
	private String impres;
	private Boolean predet;
	
	private CajaDocuEgre cajaDocuEgre;
	
	private static final long serialVersionUID = -5492766524957423413L;

	public SaliArch() {
	}
	
	public SaliArch(Integer saliArchId, String salida, String format, String impres, Boolean predet) {
		this.saliArchId = saliArchId;
		this.salida = salida;
		this.format = format;
		this.impres = impres;
		this.predet = predet;
	}

	@Id
	@TableGenerator(name = "idSaliArch", table = "secuencial", pkColumnName = "tabla", pkColumnValue = "sali_arch", valueColumnName = "numero", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idSaliArch")
	@Column(name = "SALI_ARCH_ID", unique = true, nullable = false)	
	public Integer getSaliArchId() {
		return saliArchId;
	}

	public void setSaliArchId(Integer saliArchId) {
		this.saliArchId = saliArchId;
	}

	@Column(name = "SALIDA", nullable = false, length = 32)
	public String getSalida() {
		return salida;
	}

	public void setSalida(String salida) {
		this.salida = salida;
	}

	@Column(name = "FORMAT", nullable = false, length = 32)
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "IMPRES", nullable = false, length = 32)
	public String getImpres() {
		return impres;
	}

	public void setImpres(String impres) {
		this.impres = impres;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAJA_DOCU_EGRE_ID", nullable = false)
	public CajaDocuEgre getCajaDocuEgre() {
		return cajaDocuEgre;
	}

	public void setCajaDocuEgre(CajaDocuEgre cajaDocuEgre) {
		this.cajaDocuEgre = cajaDocuEgre;
	}

	public Boolean getPredet() {
		return predet;
	}

	public void setPredet(Boolean predet) {
		this.predet = predet;
	}
}