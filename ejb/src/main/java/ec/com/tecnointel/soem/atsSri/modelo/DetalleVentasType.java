//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.12.05 a las 03:49:23 PM COT 
//


package ec.com.tecnointel.soem.atsSri.modelo;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleVentasType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleVentasType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tpIdCliente" type="{}tpIdClienteType"/>
 *         &lt;element name="idCliente" type="{}idClienteType"/>
 *         &lt;element name="parteRelVtas" type="{}parteRelType" minOccurs="0"/>
 *         &lt;element name="tipoCliente" type="{}tipoProvType" minOccurs="0"/>
 *         &lt;element name="denoCli" type="{}denoProvType" minOccurs="0"/>
 *         &lt;element name="tipoComprobante" type="{}tipoComprobanteType"/>
 *         &lt;element name="tipoEmision" type="{}tipoEmisionType"/>
 *         &lt;element name="numeroComprobantes" type="{}numeroComprobantesType"/>
 *         &lt;element name="baseNoGraIva" type="{}monedaType"/>
 *         &lt;element name="baseImponible" type="{}monedaType"/>
 *         &lt;element name="baseImpGrav" type="{}monedaType"/>
 *         &lt;element name="montoIva" type="{}monedaType"/>
 *         &lt;element name="compensaciones" type="{}compensacionesType" minOccurs="0"/>
 *         &lt;element name="montoIce" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="valorRetIva" type="{}monedaType"/>
 *         &lt;element name="valorRetRenta" type="{}monedaType"/>
 *         &lt;element name="formasDePago" type="{}formasDePagoType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleVentasType", propOrder = {
    "tpIdCliente",
    "idCliente",
    "parteRelVtas",
    "tipoCliente",
    "denoCli",
    "tipoComprobante",
    "tipoEmision",
    "numeroComprobantes",
    "baseNoGraIva",
    "baseImponible",
    "baseImpGrav",
    "montoIva",
    "compensaciones",
    "montoIce",
    "valorRetIva",
    "valorRetRenta",
    "formasDePago"
})
public class DetalleVentasType {

    @XmlElement(required = true)
    protected String tpIdCliente;
    @XmlElement(required = true)
    protected String idCliente;
    @XmlSchemaType(name = "string")
    protected ParteRelType parteRelVtas;
    protected String tipoCliente;
    protected String denoCli;
    @XmlElement(required = true)
    protected String tipoComprobante;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected TipoEmisionType tipoEmision;
    @XmlSchemaType(name = "integer")
    protected long numeroComprobantes;
    @XmlElement(required = true)
    protected BigDecimal baseNoGraIva;
    @XmlElement(required = true)
    protected BigDecimal baseImponible;
    @XmlElement(required = true)
    protected BigDecimal baseImpGrav;
    @XmlElement(required = true)
    protected BigDecimal montoIva;
    protected CompensacionesType compensaciones;
    protected BigDecimal montoIce;
    @XmlElement(required = true)
    protected BigDecimal valorRetIva;
    @XmlElement(required = true)
    protected BigDecimal valorRetRenta;
    protected FormasDePagoType formasDePago;

    /**
     * Obtiene el valor de la propiedad tpIdCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpIdCliente() {
        return tpIdCliente;
    }

    /**
     * Define el valor de la propiedad tpIdCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpIdCliente(String value) {
        this.tpIdCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad idCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * Define el valor de la propiedad idCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCliente(String value) {
        this.idCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad parteRelVtas.
     * 
     * @return
     *     possible object is
     *     {@link ParteRelType }
     *     
     */
    public ParteRelType getParteRelVtas() {
        return parteRelVtas;
    }

    /**
     * Define el valor de la propiedad parteRelVtas.
     * 
     * @param value
     *     allowed object is
     *     {@link ParteRelType }
     *     
     */
    public void setParteRelVtas(ParteRelType value) {
        this.parteRelVtas = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCliente() {
        return tipoCliente;
    }

    /**
     * Define el valor de la propiedad tipoCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCliente(String value) {
        this.tipoCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad denoCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenoCli() {
        return denoCli;
    }

    /**
     * Define el valor de la propiedad denoCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenoCli(String value) {
        this.denoCli = value;
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
     * Obtiene el valor de la propiedad tipoEmision.
     * 
     * @return
     *     possible object is
     *     {@link TipoEmisionType }
     *     
     */
    public TipoEmisionType getTipoEmision() {
        return tipoEmision;
    }

    /**
     * Define el valor de la propiedad tipoEmision.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoEmisionType }
     *     
     */
    public void setTipoEmision(TipoEmisionType value) {
        this.tipoEmision = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroComprobantes.
     * 
     */
    public long getNumeroComprobantes() {
        return numeroComprobantes;
    }

    /**
     * Define el valor de la propiedad numeroComprobantes.
     * 
     */
    public void setNumeroComprobantes(long value) {
        this.numeroComprobantes = value;
    }

    /**
     * Obtiene el valor de la propiedad baseNoGraIva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseNoGraIva() {
        return baseNoGraIva;
    }

    /**
     * Define el valor de la propiedad baseNoGraIva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseNoGraIva(BigDecimal value) {
        this.baseNoGraIva = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImponible.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    /**
     * Define el valor de la propiedad baseImponible.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImponible(BigDecimal value) {
        this.baseImponible = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImpGrav.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImpGrav() {
        return baseImpGrav;
    }

    /**
     * Define el valor de la propiedad baseImpGrav.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImpGrav(BigDecimal value) {
        this.baseImpGrav = value;
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
     * Obtiene el valor de la propiedad montoIce.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoIce() {
        return montoIce;
    }

    /**
     * Define el valor de la propiedad montoIce.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoIce(BigDecimal value) {
        this.montoIce = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetIva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetIva() {
        return valorRetIva;
    }

    /**
     * Define el valor de la propiedad valorRetIva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetIva(BigDecimal value) {
        this.valorRetIva = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetRenta.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetRenta() {
        return valorRetRenta;
    }

    /**
     * Define el valor de la propiedad valorRetRenta.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetRenta(BigDecimal value) {
        this.valorRetRenta = value;
    }

    /**
     * Obtiene el valor de la propiedad formasDePago.
     * 
     * @return
     *     possible object is
     *     {@link FormasDePagoType }
     *     
     */
    public FormasDePagoType getFormasDePago() {
        return formasDePago;
    }

    /**
     * Define el valor de la propiedad formasDePago.
     * 
     * @param value
     *     allowed object is
     *     {@link FormasDePagoType }
     *     
     */
    public void setFormasDePago(FormasDePagoType value) {
        this.formasDePago = value;
    }

}
