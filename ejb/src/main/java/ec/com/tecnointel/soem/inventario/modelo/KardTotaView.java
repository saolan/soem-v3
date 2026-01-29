package ec.com.tecnointel.soem.inventario.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "kard_tota_view")
public class KardTotaView implements Serializable {

	private Integer sucursalId;
	private Integer bodegaId;
	private Integer productoId;
	
	private String sucuDescri;
	private String bodeDescri;
	private String prodDescri;
	private BigDecimal cantidIngr;
	private BigDecimal totalIngr;
	private BigDecimal cantidEgre;
	private BigDecimal totalEgre;
	private BigDecimal cantidSald;
	private BigDecimal totalSald;
	private BigDecimal costo;

//	@EmbeddedId
//	KardTotaViewId kardTotaViewId;

	private static final long serialVersionUID = -685718204976547422L;

	public KardTotaView() {
	}

	public KardTotaView(Integer sucursalId, Integer bodegaId,
			Integer productoId, String sucuDescri, String bodeDescri,
			String prodDescri, BigDecimal cantidIngr, BigDecimal totalIngr,
			BigDecimal cantidEgre, BigDecimal totalEgre,
			BigDecimal cantidSald, BigDecimal totalSald, BigDecimal costo) {

		this.sucursalId = sucursalId;
		this.bodegaId = bodegaId;
		this.productoId = productoId;
		this.sucuDescri = sucuDescri;
		this.bodeDescri = bodeDescri;
		this.prodDescri = prodDescri;
		this.cantidIngr = cantidIngr;
		this.totalIngr = totalIngr;
		this.cantidEgre = cantidEgre;
		this.totalEgre = totalEgre;
		this.cantidSald = cantidSald;
		this.totalSald = totalSald;
		this.costo = costo;
		
//		Delegación
//		kardTotaViewId = new KardTotaViewId(sucursalId, bodegaId, productoId);

	}
	
//	public KardTotaView(Integer sucursalId, Integer bodegaId,Integer productoId) {
//		
////		Delegación
//		kardTotaViewId = new KardTotaViewId(sucursalId, bodegaId, productoId);
//	}

	@Id
	@Column(name = "SUCURSAL_ID")
	public Integer getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}

	@Id
	@Column(name = "BODEGA_ID")
	public Integer getBodegaId(){
		return bodegaId;
	}

	public void setBodegaId(Integer bodegaId) {
		this.bodegaId = bodegaId;
	}

	@Id
	@Column(name = "PRODUCTO_ID")
	public Integer getProductoId() {
		return productoId;
	}

	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}

	@Column(name = "SUCU_DESCRI")
	public String getSucuDescri() {
		return sucuDescri;
	}

	public void setSucuDescri(String sucuDescri) {
		this.sucuDescri = sucuDescri;
	}

	@Column(name = "BODE_DESCRI")
	public String getBodeDescri() {
		return bodeDescri;
	}

	public void setBodeDescri(String bodeDescri) {
		this.bodeDescri = bodeDescri;
	}

	@Column(name = "PROD_DESCRI")
	public String getProdDescri() {
		return prodDescri;
	}

	public void setProdDescri(String prodDescri) {
		this.prodDescri = prodDescri;
	}

	@Column(name = "CANTID_INGR")
	public BigDecimal getCantidIngr() {
		return cantidIngr;
	}

	public void setCantidIngr(BigDecimal cantidIngr) {
		this.cantidIngr = cantidIngr;
	}

	@Column(name = "TOTAL_INGR")
	public BigDecimal getTotalIngr() {
		return totalIngr;
	}

	public void setTotalIngr(BigDecimal totalIngr) {
		this.totalIngr = totalIngr;
	}

	@Column(name = "CANTID_EGRE")
	public BigDecimal getCantidEgre() {
		return cantidEgre;
	}

	public void setCantidEgre(BigDecimal cantidEgre) {
		this.cantidEgre = cantidEgre;
	}

	@Column(name = "TOTAL_EGRE")
	public BigDecimal getTotalEgre() {
		return totalEgre;
	}

	public void setTotalEgre(BigDecimal totalEgre) {
		this.totalEgre = totalEgre;
	}

	@Column(name = "CANTID_SALD")
	public BigDecimal getCantidSald() {
		return cantidSald;
	}

	public void setCantidSald(BigDecimal cantidSald) {
		this.cantidSald = cantidSald;
	}

	@Column(name = "TOTAL_SALD")
	public BigDecimal getTotalSald() {
		return totalSald;
	}

	public void setTotalSald(BigDecimal totalSald) {
		this.totalSald = totalSald;
	}

	@Column(name = "COSTO")
	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

//	public KardTotaViewId getKardTotaViewId() {
//		return kardTotaViewId;
//	}
//
//	public void setKardTotaViewId(KardTotaViewId kardTotaViewId) {
//		this.kardTotaViewId = kardTotaViewId;
//	}
	
}
