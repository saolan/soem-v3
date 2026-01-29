package ec.com.tecnointel.soem.caja.modelo;

import java.io.Serializable;

import ec.com.tecnointel.soem.contabilidad.modelo.Transaccion;
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
@Table(name = "caja_movi_tran")
@NamedEntityGraph(name = "cajaMoviTran.Graph", attributeNodes = { 
@NamedAttributeNode(value = "cajaMovi"),
@NamedAttributeNode(value = "transaccion") 
})
public class CajaMoviTran implements Serializable {

	private Integer cajaMoviTranId;
	private CajaMovi cajaMovi;
	private Transaccion transaccion;
	private String descri;

	private static final long serialVersionUID = 8398636802016348084L;

	public CajaMoviTran() {
	}

	public CajaMoviTran(CajaMovi cajaMovi, Transaccion transaccion, String descri) {
		this.cajaMovi = cajaMovi;
		this.transaccion = transaccion;
		this.descri = descri;
	}

	public CajaMoviTran(CajaMovi cajaMovi, Transaccion transaccion) {
		this.cajaMovi = cajaMovi;
		this.transaccion = transaccion;
	}

	@Id
	@TableGenerator(name = "idCajaMoviTran", table = "secuencial", pkColumnName = "tabla", pkColumnValue = "caja_movi_tran", valueColumnName = "numero", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idCajaMoviTran")
	@Column(name = "CAJA_MOVI_TRAN_ID", unique = true, nullable = false)
	public Integer getCajaMoviTranId() {
		return cajaMoviTranId;
	}

	public void setCajaMoviTranId(Integer cajaMoviTranId) {
		this.cajaMoviTranId = cajaMoviTranId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAJA_MOVI_ID", nullable = false)
	public CajaMovi getCajaMovi() {
		return cajaMovi;
	}

	public void setCajaMovi(CajaMovi cajaMovi) {
		this.cajaMovi = cajaMovi;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSACCION_ID", nullable = false)	
	public Transaccion getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}

	@Column(name = "descri")
	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}
}