package ec.com.tecnointel.soem.documeElec.modelo.liquiCompra;

import java.math.BigDecimal;
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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "infoTributaria",
    "infoLiquidacionCompra",
    "detalles",
    "reembolsos",
    "tipoNegociable",
    "infoAdicional"
})
@XmlRootElement(name = "liquidacionCompra")
public class LiquidacionCompra {

    @XmlElement(required = true)
    protected InfoTributaria infoTributaria;
    @XmlElement(required = true)
    protected LiquidacionCompra.InfoLiquidacionCompra infoLiquidacionCompra;
    @XmlElement(required = true)
    protected LiquidacionCompra.Detalles detalles;
    protected Reembolsos reembolsos;
    protected TipoNegociable tipoNegociable;
    protected LiquidacionCompra.InfoAdicional infoAdicional;
    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "version")
    @XmlSchemaType(name = "anySimpleType")
    protected String version;

    public InfoTributaria getInfoTributaria() {
        return infoTributaria;
    }

    public void setInfoTributaria(InfoTributaria value) {
        this.infoTributaria = value;
    }

    public LiquidacionCompra.InfoLiquidacionCompra getInfoLiquidacionCompra() {
        return infoLiquidacionCompra;
    }

    public void setInfoLiquidacionCompra(LiquidacionCompra.InfoLiquidacionCompra value) {
        this.infoLiquidacionCompra = value;
    }

    public LiquidacionCompra.Detalles getDetalles() {
        return detalles;
    }

    public void setDetalles(LiquidacionCompra.Detalles value) {
        this.detalles = value;
    }

    public Reembolsos getReembolsos() {
        return reembolsos;
    }

    public void setReembolsos(Reembolsos value) {
        this.reembolsos = value;
    }

    public TipoNegociable getTipoNegociable() {
        return tipoNegociable;
    }

    public void setTipoNegociable(TipoNegociable value) {
        this.tipoNegociable = value;
    }

    public LiquidacionCompra.InfoAdicional getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(LiquidacionCompra.InfoAdicional value) {
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
    @XmlType(name = "", propOrder = {
        "detalle"
    })
    public static class Detalles {

        @XmlElement(required = true)
        protected List<LiquidacionCompra.Detalles.Detalle> detalle;

        public List<LiquidacionCompra.Detalles.Detalle> getDetalle() {
            if (detalle == null) {
                detalle = new ArrayList<LiquidacionCompra.Detalles.Detalle>();
            }
            return this.detalle;
        }
        
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "codigoPrincipal",
            "codigoAuxiliar",
            "descripcion",
            "unidadMedida",
            "cantidad",
            "precioUnitario",
            "precioSinSubsidio",
            "descuento",
            "precioTotalSinImpuesto",
            "detallesAdicionales",
            "impuestos"
        })
        public static class Detalle {

            protected String codigoPrincipal;
            protected String codigoAuxiliar;
            @XmlElement(required = true)
            protected String descripcion;
            protected String unidadMedida;
            @XmlElement(required = true)
            protected BigDecimal cantidad;
            @XmlElement(required = true)
            protected BigDecimal precioUnitario;
            protected BigDecimal precioSinSubsidio;
            @XmlElement(required = true)
            protected BigDecimal descuento;
            @XmlElement(required = true)
            protected BigDecimal precioTotalSinImpuesto;
            protected LiquidacionCompra.Detalles.Detalle.DetallesAdicionales detallesAdicionales;
            @XmlElement(required = true)
            protected LiquidacionCompra.Detalles.Detalle.Impuestos impuestos;

            public String getCodigoPrincipal() {
                return codigoPrincipal;
            }

            public void setCodigoPrincipal(String value) {
                this.codigoPrincipal = value;
            }

            public String getCodigoAuxiliar() {
                return codigoAuxiliar;
            }

            public void setCodigoAuxiliar(String value) {
                this.codigoAuxiliar = value;
            }

            public String getDescripcion() {
                return descripcion;
            }

            public void setDescripcion(String value) {
                this.descripcion = value;
            }

            public String getUnidadMedida() {
                return unidadMedida;
            }

            public void setUnidadMedida(String value) {
                this.unidadMedida = value;
            }

            public BigDecimal getCantidad() {
                return cantidad;
            }

            public void setCantidad(BigDecimal value) {
                this.cantidad = value;
            }

            public BigDecimal getPrecioUnitario() {
                return precioUnitario;
            }

            public void setPrecioUnitario(BigDecimal value) {
                this.precioUnitario = value;
            }

            public BigDecimal getPrecioSinSubsidio() {
                return precioSinSubsidio;
            }

            public void setPrecioSinSubsidio(BigDecimal value) {
                this.precioSinSubsidio = value;
            }

            public BigDecimal getDescuento() {
                return descuento;
            }

            public void setDescuento(BigDecimal value) {
                this.descuento = value;
            }

            public BigDecimal getPrecioTotalSinImpuesto() {
                return precioTotalSinImpuesto;
            }

            public void setPrecioTotalSinImpuesto(BigDecimal value) {
                this.precioTotalSinImpuesto = value;
            }

            public LiquidacionCompra.Detalles.Detalle.DetallesAdicionales getDetallesAdicionales() {
                return detallesAdicionales;
            }

            public void setDetallesAdicionales(LiquidacionCompra.Detalles.Detalle.DetallesAdicionales value) {
                this.detallesAdicionales = value;
            }

            public LiquidacionCompra.Detalles.Detalle.Impuestos getImpuestos() {
                return impuestos;
            }

            public void setImpuestos(LiquidacionCompra.Detalles.Detalle.Impuestos value) {
                this.impuestos = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "detAdicional"
            })
            public static class DetallesAdicionales {

                @XmlElement(required = true)
                protected List<LiquidacionCompra.Detalles.Detalle.DetallesAdicionales.DetAdicional> detAdicional;

                public List<LiquidacionCompra.Detalles.Detalle.DetallesAdicionales.DetAdicional> getDetAdicional() {
                    if (detAdicional == null) {
                        detAdicional = new ArrayList<LiquidacionCompra.Detalles.Detalle.DetallesAdicionales.DetAdicional>();
                    }
                    return this.detAdicional;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class DetAdicional {

                    @XmlAttribute(name = "nombre", required = true)
                    protected String nombre;
                    @XmlAttribute(name = "valor", required = true)
                    protected String valor;

                    public String getNombre() {
                        return nombre;
                    }

                    public void setNombre(String value) {
                        this.nombre = value;
                    }

                    public String getValor() {
                        return valor;
                    }

                    public void setValor(String value) {
                        this.valor = value;
                    }
                }
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "impuesto"
            })
            public static class Impuestos {

                @XmlElement(required = true)
                protected List<Impuesto> impuesto;

                public List<Impuesto> getImpuesto() {
                    if (impuesto == null) {
                        impuesto = new ArrayList<Impuesto>();
                    }
                    return this.impuesto;
                }
            }
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "campoAdicional"
    })
    public static class InfoAdicional {

        @XmlElement(required = true)
        protected List<LiquidacionCompra.InfoAdicional.CampoAdicional> campoAdicional;

        public List<LiquidacionCompra.InfoAdicional.CampoAdicional> getCampoAdicional() {
            if (campoAdicional == null) {
                campoAdicional = new ArrayList<LiquidacionCompra.InfoAdicional.CampoAdicional>();
            }
            return this.campoAdicional;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "value"
        })
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
    @XmlType(name = "", propOrder = {
        "fechaEmision",
        "dirEstablecimiento",
        "contribuyenteEspecial",
        "obligadoContabilidad",
        "tipoIdentificacionProveedor",
        "razonSocialProveedor",
        "identificacionProveedor",
        "direccionProveedor",
        "totalSinImpuestos",
        "totalDescuento",
        "codDocReembolso",
        "totalComprobantesReembolso",
        "totalBaseImponibleReembolso",
        "totalImpuestoReembolso",
        "totalConImpuestos",
        "importeTotal",
        "moneda",
        "pagos"
    })
    public static class InfoLiquidacionCompra {

        @XmlElement(required = true)
        protected String fechaEmision;
        protected String dirEstablecimiento;
        protected String contribuyenteEspecial;
        @XmlSchemaType(name = "string")
        protected String obligadoContabilidad;
        @XmlElement(required = true)
        protected String tipoIdentificacionProveedor;
        @XmlElement(required = true)
        protected String razonSocialProveedor;
        @XmlElement(required = true)
        protected String identificacionProveedor;
        protected String direccionProveedor;
        @XmlElement(required = true)
        protected BigDecimal totalSinImpuestos;
        @XmlElement(required = true)
        protected BigDecimal totalDescuento;
        protected String codDocReembolso;
        protected BigDecimal totalComprobantesReembolso;
        protected BigDecimal totalBaseImponibleReembolso;
        protected BigDecimal totalImpuestoReembolso;
        @XmlElement(required = true)
        protected LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos totalConImpuestos;
        @XmlElement(required = true)
        protected BigDecimal importeTotal;
        protected String moneda;
        protected Pagos pagos;

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

        public String getTipoIdentificacionProveedor() {
            return tipoIdentificacionProveedor;
        }

        public void setTipoIdentificacionProveedor(String value) {
            this.tipoIdentificacionProveedor = value;
        }

        public String getRazonSocialProveedor() {
            return razonSocialProveedor;
        }

        public void setRazonSocialProveedor(String value) {
            this.razonSocialProveedor = value;
        }

        public String getIdentificacionProveedor() {
            return identificacionProveedor;
        }

        public void setIdentificacionProveedor(String value) {
            this.identificacionProveedor = value;
        }

        public String getDireccionProveedor() {
            return direccionProveedor;
        }

        public void setDireccionProveedor(String value) {
            this.direccionProveedor = value;
        }

        public BigDecimal getTotalSinImpuestos() {
            return totalSinImpuestos;
        }

        public void setTotalSinImpuestos(BigDecimal value) {
            this.totalSinImpuestos = value;
        }

        public BigDecimal getTotalDescuento() {
            return totalDescuento;
        }

        public void setTotalDescuento(BigDecimal value) {
            this.totalDescuento = value;
        }

        public String getCodDocReembolso() {
            return codDocReembolso;
        }

        public void setCodDocReembolso(String value) {
            this.codDocReembolso = value;
        }

        public BigDecimal getTotalComprobantesReembolso() {
            return totalComprobantesReembolso;
        }

        public void setTotalComprobantesReembolso(BigDecimal value) {
            this.totalComprobantesReembolso = value;
        }

        public BigDecimal getTotalBaseImponibleReembolso() {
            return totalBaseImponibleReembolso;
        }

        public void setTotalBaseImponibleReembolso(BigDecimal value) {
            this.totalBaseImponibleReembolso = value;
        }

        public BigDecimal getTotalImpuestoReembolso() {
            return totalImpuestoReembolso;
        }

        public void setTotalImpuestoReembolso(BigDecimal value) {
            this.totalImpuestoReembolso = value;
        }

        public LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos getTotalConImpuestos() {
            return totalConImpuestos;
        }

        public void setTotalConImpuestos(LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos value) {
            this.totalConImpuestos = value;
        }

        public BigDecimal getImporteTotal() {
            return importeTotal;
        }

        public void setImporteTotal(BigDecimal value) {
            this.importeTotal = value;
        }

        public String getMoneda() {
            return moneda;
        }

        public void setMoneda(String value) {
            this.moneda = value;
        }

        public Pagos getPagos() {
            return pagos;
        }

        public void setPagos(Pagos value) {
            this.pagos = value;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "totalImpuesto"
        })
        public static class TotalConImpuestos {

            @XmlElement(required = true)
            protected List<LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos.TotalImpuesto> totalImpuesto;

            public List<LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos.TotalImpuesto> getTotalImpuesto() {
                if (totalImpuesto == null) {
                    totalImpuesto = new ArrayList<LiquidacionCompra.InfoLiquidacionCompra.TotalConImpuestos.TotalImpuesto>();
                }
                return this.totalImpuesto;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "codigo",
                "codigoPorcentaje",
                "descuentoAdicional",
                "baseImponible",
                "tarifa",
                "valor"
            })
            public static class TotalImpuesto {

                @XmlElement(required = true)
                protected String codigo;
                @XmlElement(required = true)
                protected String codigoPorcentaje;
                protected BigDecimal descuentoAdicional;
                @XmlElement(required = true)
                protected BigDecimal baseImponible;
                protected BigDecimal tarifa;
                @XmlElement(required = true)
                protected BigDecimal valor;

                public String getCodigo() {
                    return codigo;
                }

                public void setCodigo(String value) {
                    this.codigo = value;
                }

                public String getCodigoPorcentaje() {
                    return codigoPorcentaje;
                }

                public void setCodigoPorcentaje(String value) {
                    this.codigoPorcentaje = value;
                }

                public BigDecimal getDescuentoAdicional() {
                    return descuentoAdicional;
                }

                public void setDescuentoAdicional(BigDecimal value) {
                    this.descuentoAdicional = value;
                }

                public BigDecimal getBaseImponible() {
                    return baseImponible;
                }

                public void setBaseImponible(BigDecimal value) {
                    this.baseImponible = value;
                }

                public BigDecimal getTarifa() {
                    return tarifa;
                }

                public void setTarifa(BigDecimal value) {
                    this.tarifa = value;
                }

                public BigDecimal getValor() {
                    return valor;
                }

                public void setValor(BigDecimal value) {
                    this.valor = value;
                }
            }
        }
    }
}
