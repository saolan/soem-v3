//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.12.05 a las 03:49:23 PM COT 
//


package ec.com.tecnointel.soem.atsSri.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleRendFinancierosType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleRendFinancierosType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="retenido" type="{}tpIdProvType"/>
 *         &lt;element name="idRetenido" type="{}idProvType"/>
 *         &lt;element name="parteRelFid" type="{}aplicConvDobTribType" minOccurs="0"/>
 *         &lt;element name="tipoRete" type="{}tipoProvType" minOccurs="0"/>
 *         &lt;element name="denoBenefi" type="{}denoProvType" minOccurs="0"/>
 *         &lt;element name="ahorroPN" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="totalDep" type="{}comisionType"/>
 *                   &lt;element name="rendGen" type="{}comisionType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ctaExenta" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="totalDep" type="{}comisionType"/>
 *                   &lt;element name="rendGen" type="{}comisionType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="retenciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="detRet" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="pagoExterior" type="{}pagoExteriorType" minOccurs="0"/>
 *                             &lt;element name="estabRetencion" type="{}validacionEstablecimientosType"/>
 *                             &lt;element name="ptoEmiRetencion" type="{}validacionEstablecimientosType"/>
 *                             &lt;element name="secRetencion" type="{}secRetencionType"/>
 *                             &lt;element name="autRetencion" type="{}autRetencionType"/>
 *                             &lt;element name="fechaEmiRet" type="{}fechaType"/>
 *                             &lt;element name="airRend" type="{}airRendType"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleRendFinancierosType", propOrder = {
    "retenido",
    "idRetenido",
    "parteRelFid",
    "tipoRete",
    "denoBenefi",
    "ahorroPN",
    "ctaExenta",
    "retenciones"
})
public class DetalleRendFinancierosType {

    @XmlElement(required = true)
    protected String retenido;
    @XmlElement(required = true)
    protected String idRetenido;
    @XmlSchemaType(name = "string")
    protected AplicConvDobTribType parteRelFid;
    protected String tipoRete;
    protected String denoBenefi;
    protected DetalleRendFinancierosType.AhorroPN ahorroPN;
    protected DetalleRendFinancierosType.CtaExenta ctaExenta;
    protected DetalleRendFinancierosType.Retenciones retenciones;

    /**
     * Obtiene el valor de la propiedad retenido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetenido() {
        return retenido;
    }

    /**
     * Define el valor de la propiedad retenido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetenido(String value) {
        this.retenido = value;
    }

    /**
     * Obtiene el valor de la propiedad idRetenido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdRetenido() {
        return idRetenido;
    }

    /**
     * Define el valor de la propiedad idRetenido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdRetenido(String value) {
        this.idRetenido = value;
    }

    /**
     * Obtiene el valor de la propiedad parteRelFid.
     * 
     * @return
     *     possible object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public AplicConvDobTribType getParteRelFid() {
        return parteRelFid;
    }

    /**
     * Define el valor de la propiedad parteRelFid.
     * 
     * @param value
     *     allowed object is
     *     {@link AplicConvDobTribType }
     *     
     */
    public void setParteRelFid(AplicConvDobTribType value) {
        this.parteRelFid = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoRete.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRete() {
        return tipoRete;
    }

    /**
     * Define el valor de la propiedad tipoRete.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRete(String value) {
        this.tipoRete = value;
    }

    /**
     * Obtiene el valor de la propiedad denoBenefi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenoBenefi() {
        return denoBenefi;
    }

    /**
     * Define el valor de la propiedad denoBenefi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenoBenefi(String value) {
        this.denoBenefi = value;
    }

    /**
     * Obtiene el valor de la propiedad ahorroPN.
     * 
     * @return
     *     possible object is
     *     {@link DetalleRendFinancierosType.AhorroPN }
     *     
     */
    public DetalleRendFinancierosType.AhorroPN getAhorroPN() {
        return ahorroPN;
    }

    /**
     * Define el valor de la propiedad ahorroPN.
     * 
     * @param value
     *     allowed object is
     *     {@link DetalleRendFinancierosType.AhorroPN }
     *     
     */
    public void setAhorroPN(DetalleRendFinancierosType.AhorroPN value) {
        this.ahorroPN = value;
    }

    /**
     * Obtiene el valor de la propiedad ctaExenta.
     * 
     * @return
     *     possible object is
     *     {@link DetalleRendFinancierosType.CtaExenta }
     *     
     */
    public DetalleRendFinancierosType.CtaExenta getCtaExenta() {
        return ctaExenta;
    }

    /**
     * Define el valor de la propiedad ctaExenta.
     * 
     * @param value
     *     allowed object is
     *     {@link DetalleRendFinancierosType.CtaExenta }
     *     
     */
    public void setCtaExenta(DetalleRendFinancierosType.CtaExenta value) {
        this.ctaExenta = value;
    }

    /**
     * Obtiene el valor de la propiedad retenciones.
     * 
     * @return
     *     possible object is
     *     {@link DetalleRendFinancierosType.Retenciones }
     *     
     */
    public DetalleRendFinancierosType.Retenciones getRetenciones() {
        return retenciones;
    }

    /**
     * Define el valor de la propiedad retenciones.
     * 
     * @param value
     *     allowed object is
     *     {@link DetalleRendFinancierosType.Retenciones }
     *     
     */
    public void setRetenciones(DetalleRendFinancierosType.Retenciones value) {
        this.retenciones = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="totalDep" type="{}comisionType"/>
     *         &lt;element name="rendGen" type="{}comisionType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "totalDep",
        "rendGen"
    })
    public static class AhorroPN {

        @XmlElement(required = true)
        protected BigDecimal totalDep;
        @XmlElement(required = true)
        protected BigDecimal rendGen;

        /**
         * Obtiene el valor de la propiedad totalDep.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotalDep() {
            return totalDep;
        }

        /**
         * Define el valor de la propiedad totalDep.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotalDep(BigDecimal value) {
            this.totalDep = value;
        }

        /**
         * Obtiene el valor de la propiedad rendGen.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRendGen() {
            return rendGen;
        }

        /**
         * Define el valor de la propiedad rendGen.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRendGen(BigDecimal value) {
            this.rendGen = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="totalDep" type="{}comisionType"/>
     *         &lt;element name="rendGen" type="{}comisionType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "totalDep",
        "rendGen"
    })
    public static class CtaExenta {

        @XmlElement(required = true)
        protected BigDecimal totalDep;
        @XmlElement(required = true)
        protected BigDecimal rendGen;

        /**
         * Obtiene el valor de la propiedad totalDep.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getTotalDep() {
            return totalDep;
        }

        /**
         * Define el valor de la propiedad totalDep.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setTotalDep(BigDecimal value) {
            this.totalDep = value;
        }

        /**
         * Obtiene el valor de la propiedad rendGen.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getRendGen() {
            return rendGen;
        }

        /**
         * Define el valor de la propiedad rendGen.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setRendGen(BigDecimal value) {
            this.rendGen = value;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="detRet" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="pagoExterior" type="{}pagoExteriorType" minOccurs="0"/>
     *                   &lt;element name="estabRetencion" type="{}validacionEstablecimientosType"/>
     *                   &lt;element name="ptoEmiRetencion" type="{}validacionEstablecimientosType"/>
     *                   &lt;element name="secRetencion" type="{}secRetencionType"/>
     *                   &lt;element name="autRetencion" type="{}autRetencionType"/>
     *                   &lt;element name="fechaEmiRet" type="{}fechaType"/>
     *                   &lt;element name="airRend" type="{}airRendType"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "detRet"
    })
    public static class Retenciones {

        @XmlElement(required = true)
        protected List<DetalleRendFinancierosType.Retenciones.DetRet> detRet;

        /**
         * Gets the value of the detRet property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the detRet property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDetRet().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DetalleRendFinancierosType.Retenciones.DetRet }
         * 
         * 
         */
        public List<DetalleRendFinancierosType.Retenciones.DetRet> getDetRet() {
            if (detRet == null) {
                detRet = new ArrayList<DetalleRendFinancierosType.Retenciones.DetRet>();
            }
            return this.detRet;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="pagoExterior" type="{}pagoExteriorType" minOccurs="0"/>
         *         &lt;element name="estabRetencion" type="{}validacionEstablecimientosType"/>
         *         &lt;element name="ptoEmiRetencion" type="{}validacionEstablecimientosType"/>
         *         &lt;element name="secRetencion" type="{}secRetencionType"/>
         *         &lt;element name="autRetencion" type="{}autRetencionType"/>
         *         &lt;element name="fechaEmiRet" type="{}fechaType"/>
         *         &lt;element name="airRend" type="{}airRendType"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "pagoExterior",
            "estabRetencion",
            "ptoEmiRetencion",
            "secRetencion",
            "autRetencion",
            "fechaEmiRet",
            "airRend"
        })
        public static class DetRet {

            protected PagoExteriorType pagoExterior;
            @XmlSchemaType(name = "integer")
            protected int estabRetencion;
            @XmlSchemaType(name = "integer")
            protected int ptoEmiRetencion;
            @XmlSchemaType(name = "integer")
            protected int secRetencion;
            @XmlElement(required = true)
            protected String autRetencion;
            @XmlElement(required = true)
            protected String fechaEmiRet;
            @XmlElement(required = true)
            protected AirRendType airRend;

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
             * Obtiene el valor de la propiedad estabRetencion.
             * 
             */
            public int getEstabRetencion() {
                return estabRetencion;
            }

            /**
             * Define el valor de la propiedad estabRetencion.
             * 
             */
            public void setEstabRetencion(int value) {
                this.estabRetencion = value;
            }

            /**
             * Obtiene el valor de la propiedad ptoEmiRetencion.
             * 
             */
            public int getPtoEmiRetencion() {
                return ptoEmiRetencion;
            }

            /**
             * Define el valor de la propiedad ptoEmiRetencion.
             * 
             */
            public void setPtoEmiRetencion(int value) {
                this.ptoEmiRetencion = value;
            }

            /**
             * Obtiene el valor de la propiedad secRetencion.
             * 
             */
            public int getSecRetencion() {
                return secRetencion;
            }

            /**
             * Define el valor de la propiedad secRetencion.
             * 
             */
            public void setSecRetencion(int value) {
                this.secRetencion = value;
            }

            /**
             * Obtiene el valor de la propiedad autRetencion.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAutRetencion() {
                return autRetencion;
            }

            /**
             * Define el valor de la propiedad autRetencion.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAutRetencion(String value) {
                this.autRetencion = value;
            }

            /**
             * Obtiene el valor de la propiedad fechaEmiRet.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFechaEmiRet() {
                return fechaEmiRet;
            }

            /**
             * Define el valor de la propiedad fechaEmiRet.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFechaEmiRet(String value) {
                this.fechaEmiRet = value;
            }

            /**
             * Obtiene el valor de la propiedad airRend.
             * 
             * @return
             *     possible object is
             *     {@link AirRendType }
             *     
             */
            public AirRendType getAirRend() {
                return airRend;
            }

            /**
             * Define el valor de la propiedad airRend.
             * 
             * @param value
             *     allowed object is
             *     {@link AirRendType }
             *     
             */
            public void setAirRend(AirRendType value) {
                this.airRend = value;
            }

        }

    }

}
