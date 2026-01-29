//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.12.05 a las 03:49:23 PM COT 
//


package ec.com.tecnointel.soem.atsSri.modelo;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleRecapType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleRecapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="establecimientoRecap" type="{}estRecapType"/>
 *         &lt;element name="identificacionRecap" type="{}identificacionRecapType"/>
 *         &lt;element name="parteRelRec" type="{}parteRelType" minOccurs="0"/>
 *         &lt;element name="tipoEst" type="{}tipoProvType" minOccurs="0"/>
 *         &lt;element name="denoCliRecaps" type="{}denoProvType" minOccurs="0"/>
 *         &lt;element name="tipoComprobante" type="{}tipoComprobanteType"/>
 *         &lt;element name="numeroRecap" type="{}numeroRecapType"/>
 *         &lt;element name="fechaPago" type="{}fechaType"/>
 *         &lt;element name="tarjetaCredito" type="{}tarjetaCreditoType"/>
 *         &lt;element name="fechaEmisionRecap" type="{}fechaType"/>
 *         &lt;element name="consumoCero" type="{}consumoCeroType"/>
 *         &lt;element name="consumoGravado" type="{}consumoGravadoType"/>
 *         &lt;element name="totalConsumo" type="{}totalConsumoType"/>
 *         &lt;element name="montoIva" type="{}montoIvaType"/>
 *         &lt;element name="compensaciones" type="{}compensacionesType" minOccurs="0"/>
 *         &lt;element name="comision" type="{}comisionType"/>
 *         &lt;element name="numeroVouchers" type="{}numeroVouchersType"/>
 *         &lt;element name="valRetBien10" type="{}valorRetBienesType" minOccurs="0"/>
 *         &lt;element name="valRetServ20" type="{}valorRetBienesType" minOccurs="0"/>
 *         &lt;element name="valorRetBienes" type="{}valorRetBienesType"/>
 *         &lt;element name="valRetServ50" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="valorRetServicios" type="{}valorRetServiciosType"/>
 *         &lt;element name="valRetServ100" type="{}valorRetServiciosType"/>
 *         &lt;element name="pagoExterior" type="{}pagoExteriorType" minOccurs="0"/>
 *         &lt;element name="air" type="{}airRecapType" minOccurs="0"/>
 *         &lt;element name="establecimiento" type="{}validacionEstablecimientosType"/>
 *         &lt;element name="puntoEmision" type="{}validacionEstablecimientosType"/>
 *         &lt;element name="secuencial" type="{}secRetencionType"/>
 *         &lt;element name="autorizacion" type="{}validacionAutRetencionType"/>
 *         &lt;element name="fechaEmision" type="{}fechaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleRecapType", propOrder = {
    "establecimientoRecap",
    "identificacionRecap",
    "parteRelRec",
    "tipoEst",
    "denoCliRecaps",
    "tipoComprobante",
    "numeroRecap",
    "fechaPago",
    "tarjetaCredito",
    "fechaEmisionRecap",
    "consumoCero",
    "consumoGravado",
    "totalConsumo",
    "montoIva",
    "compensaciones",
    "comision",
    "numeroVouchers",
    "valRetBien10",
    "valRetServ20",
    "valorRetBienes",
    "valRetServ50",
    "valorRetServicios",
    "valRetServ100",
    "pagoExterior",
    "air",
    "establecimiento",
    "puntoEmision",
    "secuencial",
    "autorizacion",
    "fechaEmision"
})
public class DetalleRecapType {

    @XmlElement(required = true)
    protected String establecimientoRecap;
    @XmlElement(required = true)
    protected String identificacionRecap;
    @XmlSchemaType(name = "string")
    protected ParteRelType parteRelRec;
    protected String tipoEst;
    protected String denoCliRecaps;
    @XmlElement(required = true)
    protected String tipoComprobante;
    @XmlElement(required = true)
    protected String numeroRecap;
    @XmlElement(required = true)
    protected String fechaPago;
    @XmlElement(required = true)
    protected String tarjetaCredito;
    @XmlElement(required = true)
    protected String fechaEmisionRecap;
    @XmlElement(required = true)
    protected BigDecimal consumoCero;
    @XmlElement(required = true)
    protected BigDecimal consumoGravado;
    @XmlElement(required = true)
    protected BigDecimal totalConsumo;
    @XmlElement(required = true)
    protected BigDecimal montoIva;
    protected CompensacionesType compensaciones;
    @XmlElement(required = true)
    protected BigDecimal comision;
    @XmlSchemaType(name = "integer")
    protected int numeroVouchers;
    protected BigDecimal valRetBien10;
    protected BigDecimal valRetServ20;
    @XmlElement(required = true)
    protected BigDecimal valorRetBienes;
    protected BigDecimal valRetServ50;
    @XmlElement(required = true)
    protected BigDecimal valorRetServicios;
    @XmlElement(required = true)
    protected BigDecimal valRetServ100;
    protected PagoExteriorType pagoExterior;
    protected AirRecapType air;
    @XmlSchemaType(name = "integer")
    protected int establecimiento;
    @XmlSchemaType(name = "integer")
    protected int puntoEmision;
    @XmlSchemaType(name = "integer")
    protected int secuencial;
    @XmlElement(required = true)
    protected BigInteger autorizacion;
    @XmlElement(required = true)
    protected String fechaEmision;

    /**
     * Obtiene el valor de la propiedad establecimientoRecap.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstablecimientoRecap() {
        return establecimientoRecap;
    }

    /**
     * Define el valor de la propiedad establecimientoRecap.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstablecimientoRecap(String value) {
        this.establecimientoRecap = value;
    }

    /**
     * Obtiene el valor de la propiedad identificacionRecap.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificacionRecap() {
        return identificacionRecap;
    }

    /**
     * Define el valor de la propiedad identificacionRecap.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificacionRecap(String value) {
        this.identificacionRecap = value;
    }

    /**
     * Obtiene el valor de la propiedad parteRelRec.
     * 
     * @return
     *     possible object is
     *     {@link ParteRelType }
     *     
     */
    public ParteRelType getParteRelRec() {
        return parteRelRec;
    }

    /**
     * Define el valor de la propiedad parteRelRec.
     * 
     * @param value
     *     allowed object is
     *     {@link ParteRelType }
     *     
     */
    public void setParteRelRec(ParteRelType value) {
        this.parteRelRec = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEst.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEst() {
        return tipoEst;
    }

    /**
     * Define el valor de la propiedad tipoEst.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEst(String value) {
        this.tipoEst = value;
    }

    /**
     * Obtiene el valor de la propiedad denoCliRecaps.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenoCliRecaps() {
        return denoCliRecaps;
    }

    /**
     * Define el valor de la propiedad denoCliRecaps.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenoCliRecaps(String value) {
        this.denoCliRecaps = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Define el valor de la propiedad tipoComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComprobante(String value) {
        this.tipoComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroRecap.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRecap() {
        return numeroRecap;
    }

    /**
     * Define el valor de la propiedad numeroRecap.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRecap(String value) {
        this.numeroRecap = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaPago.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaPago() {
        return fechaPago;
    }

    /**
     * Define el valor de la propiedad fechaPago.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaPago(String value) {
        this.fechaPago = value;
    }

    /**
     * Obtiene el valor de la propiedad tarjetaCredito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarjetaCredito() {
        return tarjetaCredito;
    }

    /**
     * Define el valor de la propiedad tarjetaCredito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarjetaCredito(String value) {
        this.tarjetaCredito = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEmisionRecap.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmisionRecap() {
        return fechaEmisionRecap;
    }

    /**
     * Define el valor de la propiedad fechaEmisionRecap.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmisionRecap(String value) {
        this.fechaEmisionRecap = value;
    }

    /**
     * Obtiene el valor de la propiedad consumoCero.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConsumoCero() {
        return consumoCero;
    }

    /**
     * Define el valor de la propiedad consumoCero.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConsumoCero(BigDecimal value) {
        this.consumoCero = value;
    }

    /**
     * Obtiene el valor de la propiedad consumoGravado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConsumoGravado() {
        return consumoGravado;
    }

    /**
     * Define el valor de la propiedad consumoGravado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConsumoGravado(BigDecimal value) {
        this.consumoGravado = value;
    }

    /**
     * Obtiene el valor de la propiedad totalConsumo.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalConsumo() {
        return totalConsumo;
    }

    /**
     * Define el valor de la propiedad totalConsumo.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalConsumo(BigDecimal value) {
        this.totalConsumo = value;
    }

    /**
     * Obtiene el valor de la propiedad montoIva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoIva() {
        return montoIva;
    }

    /**
     * Define el valor de la propiedad montoIva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoIva(BigDecimal value) {
        this.montoIva = value;
    }

    /**
     * Obtiene el valor de la propiedad compensaciones.
     * 
     * @return
     *     possible object is
     *     {@link CompensacionesType }
     *     
     */
    public CompensacionesType getCompensaciones() {
        return compensaciones;
    }

    /**
     * Define el valor de la propiedad compensaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link CompensacionesType }
     *     
     */
    public void setCompensaciones(CompensacionesType value) {
        this.compensaciones = value;
    }

    /**
     * Obtiene el valor de la propiedad comision.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getComision() {
        return comision;
    }

    /**
     * Define el valor de la propiedad comision.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setComision(BigDecimal value) {
        this.comision = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroVouchers.
     * 
     */
    public int getNumeroVouchers() {
        return numeroVouchers;
    }

    /**
     * Define el valor de la propiedad numeroVouchers.
     * 
     */
    public void setNumeroVouchers(int value) {
        this.numeroVouchers = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetBien10.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetBien10() {
        return valRetBien10;
    }

    /**
     * Define el valor de la propiedad valRetBien10.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetBien10(BigDecimal value) {
        this.valRetBien10 = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetServ20.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetServ20() {
        return valRetServ20;
    }

    /**
     * Define el valor de la propiedad valRetServ20.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetServ20(BigDecimal value) {
        this.valRetServ20 = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetBienes.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetBienes() {
        return valorRetBienes;
    }

    /**
     * Define el valor de la propiedad valorRetBienes.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetBienes(BigDecimal value) {
        this.valorRetBienes = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetServ50.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetServ50() {
        return valRetServ50;
    }

    /**
     * Define el valor de la propiedad valRetServ50.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetServ50(BigDecimal value) {
        this.valRetServ50 = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetServicios.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetServicios() {
        return valorRetServicios;
    }

    /**
     * Define el valor de la propiedad valorRetServicios.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetServicios(BigDecimal value) {
        this.valorRetServicios = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetServ100.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetServ100() {
        return valRetServ100;
    }

    /**
     * Define el valor de la propiedad valRetServ100.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetServ100(BigDecimal value) {
        this.valRetServ100 = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoExterior.
     * 
     * @return
     *     possible object is
     *     {@link PagoExteriorType }
     *     
     */
    public PagoExteriorType getPagoExterior() {
        return pagoExterior;
    }

    /**
     * Define el valor de la propiedad pagoExterior.
     * 
     * @param value
     *     allowed object is
     *     {@link PagoExteriorType }
     *     
     */
    public void setPagoExterior(PagoExteriorType value) {
        this.pagoExterior = value;
    }

    /**
     * Obtiene el valor de la propiedad air.
     * 
     * @return
     *     possible object is
     *     {@link AirRecapType }
     *     
     */
    public AirRecapType getAir() {
        return air;
    }

    /**
     * Define el valor de la propiedad air.
     * 
     * @param value
     *     allowed object is
     *     {@link AirRecapType }
     *     
     */
    public void setAir(AirRecapType value) {
        this.air = value;
    }

    /**
     * Obtiene el valor de la propiedad establecimiento.
     * 
     */
    public int getEstablecimiento() {
        return establecimiento;
    }

    /**
     * Define el valor de la propiedad establecimiento.
     * 
     */
    public void setEstablecimiento(int value) {
        this.establecimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad puntoEmision.
     * 
     */
    public int getPuntoEmision() {
        return puntoEmision;
    }

    /**
     * Define el valor de la propiedad puntoEmision.
     * 
     */
    public void setPuntoEmision(int value) {
        this.puntoEmision = value;
    }

    /**
     * Obtiene el valor de la propiedad secuencial.
     * 
     */
    public int getSecuencial() {
        return secuencial;
    }

    /**
     * Define el valor de la propiedad secuencial.
     * 
     */
    public void setSecuencial(int value) {
        this.secuencial = value;
    }

    /**
     * Obtiene el valor de la propiedad autorizacion.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAutorizacion() {
        return autorizacion;
    }

    /**
     * Define el valor de la propiedad autorizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAutorizacion(BigInteger value) {
        this.autorizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEmision.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Define el valor de la propiedad fechaEmision.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmision(String value) {
        this.fechaEmision = value;
    }

}
