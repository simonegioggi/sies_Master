/**
 * PagamentoNonVerificato.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class PagamentoNonVerificato  implements java.io.Serializable {
    private java.lang.String crs;

    private java.lang.String identificativoUnivocoRiscossione;

    private float importo;

    private java.lang.String indiceDatSingoloPagamento;

    private java.lang.String numeroAvviso;

    private java.lang.String tipologiaPagamento;

    public PagamentoNonVerificato() {
    }

    public PagamentoNonVerificato(
           java.lang.String crs,
           java.lang.String identificativoUnivocoRiscossione,
           float importo,
           java.lang.String indiceDatSingoloPagamento,
           java.lang.String numeroAvviso,
           java.lang.String tipologiaPagamento) {
           this.crs = crs;
           this.identificativoUnivocoRiscossione = identificativoUnivocoRiscossione;
           this.importo = importo;
           this.indiceDatSingoloPagamento = indiceDatSingoloPagamento;
           this.numeroAvviso = numeroAvviso;
           this.tipologiaPagamento = tipologiaPagamento;
    }


    /**
     * Gets the crs value for this PagamentoNonVerificato.
     * 
     * @return crs
     */
    public java.lang.String getCrs() {
        return crs;
    }


    /**
     * Sets the crs value for this PagamentoNonVerificato.
     * 
     * @param crs
     */
    public void setCrs(java.lang.String crs) {
        this.crs = crs;
    }


    /**
     * Gets the identificativoUnivocoRiscossione value for this PagamentoNonVerificato.
     * 
     * @return identificativoUnivocoRiscossione
     */
    public java.lang.String getIdentificativoUnivocoRiscossione() {
        return identificativoUnivocoRiscossione;
    }


    /**
     * Sets the identificativoUnivocoRiscossione value for this PagamentoNonVerificato.
     * 
     * @param identificativoUnivocoRiscossione
     */
    public void setIdentificativoUnivocoRiscossione(java.lang.String identificativoUnivocoRiscossione) {
        this.identificativoUnivocoRiscossione = identificativoUnivocoRiscossione;
    }


    /**
     * Gets the importo value for this PagamentoNonVerificato.
     * 
     * @return importo
     */
    public float getImporto() {
        return importo;
    }


    /**
     * Sets the importo value for this PagamentoNonVerificato.
     * 
     * @param importo
     */
    public void setImporto(float importo) {
        this.importo = importo;
    }


    /**
     * Gets the indiceDatSingoloPagamento value for this PagamentoNonVerificato.
     * 
     * @return indiceDatSingoloPagamento
     */
    public java.lang.String getIndiceDatSingoloPagamento() {
        return indiceDatSingoloPagamento;
    }


    /**
     * Sets the indiceDatSingoloPagamento value for this PagamentoNonVerificato.
     * 
     * @param indiceDatSingoloPagamento
     */
    public void setIndiceDatSingoloPagamento(java.lang.String indiceDatSingoloPagamento) {
        this.indiceDatSingoloPagamento = indiceDatSingoloPagamento;
    }


    /**
     * Gets the numeroAvviso value for this PagamentoNonVerificato.
     * 
     * @return numeroAvviso
     */
    public java.lang.String getNumeroAvviso() {
        return numeroAvviso;
    }


    /**
     * Sets the numeroAvviso value for this PagamentoNonVerificato.
     * 
     * @param numeroAvviso
     */
    public void setNumeroAvviso(java.lang.String numeroAvviso) {
        this.numeroAvviso = numeroAvviso;
    }


    /**
     * Gets the tipologiaPagamento value for this PagamentoNonVerificato.
     * 
     * @return tipologiaPagamento
     */
    public java.lang.String getTipologiaPagamento() {
        return tipologiaPagamento;
    }


    /**
     * Sets the tipologiaPagamento value for this PagamentoNonVerificato.
     * 
     * @param tipologiaPagamento
     */
    public void setTipologiaPagamento(java.lang.String tipologiaPagamento) {
        this.tipologiaPagamento = tipologiaPagamento;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PagamentoNonVerificato)) return false;
        PagamentoNonVerificato other = (PagamentoNonVerificato) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.crs==null && other.getCrs()==null) || 
             (this.crs!=null &&
              this.crs.equals(other.getCrs()))) &&
            ((this.identificativoUnivocoRiscossione==null && other.getIdentificativoUnivocoRiscossione()==null) || 
             (this.identificativoUnivocoRiscossione!=null &&
              this.identificativoUnivocoRiscossione.equals(other.getIdentificativoUnivocoRiscossione()))) &&
            this.importo == other.getImporto() &&
            ((this.indiceDatSingoloPagamento==null && other.getIndiceDatSingoloPagamento()==null) || 
             (this.indiceDatSingoloPagamento!=null &&
              this.indiceDatSingoloPagamento.equals(other.getIndiceDatSingoloPagamento()))) &&
            ((this.numeroAvviso==null && other.getNumeroAvviso()==null) || 
             (this.numeroAvviso!=null &&
              this.numeroAvviso.equals(other.getNumeroAvviso()))) &&
            ((this.tipologiaPagamento==null && other.getTipologiaPagamento()==null) || 
             (this.tipologiaPagamento!=null &&
              this.tipologiaPagamento.equals(other.getTipologiaPagamento())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCrs() != null) {
            _hashCode += getCrs().hashCode();
        }
        if (getIdentificativoUnivocoRiscossione() != null) {
            _hashCode += getIdentificativoUnivocoRiscossione().hashCode();
        }
        _hashCode += new Float(getImporto()).hashCode();
        if (getIndiceDatSingoloPagamento() != null) {
            _hashCode += getIndiceDatSingoloPagamento().hashCode();
        }
        if (getNumeroAvviso() != null) {
            _hashCode += getNumeroAvviso().hashCode();
        }
        if (getTipologiaPagamento() != null) {
            _hashCode += getTipologiaPagamento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PagamentoNonVerificato.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "pagamentoNonVerificato"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("crs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "crs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificativoUnivocoRiscossione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificativoUnivocoRiscossione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "importo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indiceDatSingoloPagamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indiceDatSingoloPagamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroAvviso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroAvviso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipologiaPagamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipologiaPagamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
