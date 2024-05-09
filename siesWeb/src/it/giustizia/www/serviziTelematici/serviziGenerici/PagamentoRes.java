/**
 * PagamentoRes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class PagamentoRes  implements java.io.Serializable {
    private java.lang.String codice;

    private java.lang.String crs;

    private java.lang.String idUnivocoRiscossione;

    private float importo;

    private java.lang.String iuv;

    public PagamentoRes() {
    }

    public PagamentoRes(
           java.lang.String codice,
           java.lang.String crs,
           java.lang.String idUnivocoRiscossione,
           float importo,
           java.lang.String iuv) {
           this.codice = codice;
           this.crs = crs;
           this.idUnivocoRiscossione = idUnivocoRiscossione;
           this.importo = importo;
           this.iuv = iuv;
    }


    /**
     * Gets the codice value for this PagamentoRes.
     * 
     * @return codice
     */
    public java.lang.String getCodice() {
        return codice;
    }


    /**
     * Sets the codice value for this PagamentoRes.
     * 
     * @param codice
     */
    public void setCodice(java.lang.String codice) {
        this.codice = codice;
    }


    /**
     * Gets the crs value for this PagamentoRes.
     * 
     * @return crs
     */
    public java.lang.String getCrs() {
        return crs;
    }


    /**
     * Sets the crs value for this PagamentoRes.
     * 
     * @param crs
     */
    public void setCrs(java.lang.String crs) {
        this.crs = crs;
    }


    /**
     * Gets the idUnivocoRiscossione value for this PagamentoRes.
     * 
     * @return idUnivocoRiscossione
     */
    public java.lang.String getIdUnivocoRiscossione() {
        return idUnivocoRiscossione;
    }


    /**
     * Sets the idUnivocoRiscossione value for this PagamentoRes.
     * 
     * @param idUnivocoRiscossione
     */
    public void setIdUnivocoRiscossione(java.lang.String idUnivocoRiscossione) {
        this.idUnivocoRiscossione = idUnivocoRiscossione;
    }


    /**
     * Gets the importo value for this PagamentoRes.
     * 
     * @return importo
     */
    public float getImporto() {
        return importo;
    }


    /**
     * Sets the importo value for this PagamentoRes.
     * 
     * @param importo
     */
    public void setImporto(float importo) {
        this.importo = importo;
    }


    /**
     * Gets the iuv value for this PagamentoRes.
     * 
     * @return iuv
     */
    public java.lang.String getIuv() {
        return iuv;
    }


    /**
     * Sets the iuv value for this PagamentoRes.
     * 
     * @param iuv
     */
    public void setIuv(java.lang.String iuv) {
        this.iuv = iuv;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PagamentoRes)) return false;
        PagamentoRes other = (PagamentoRes) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codice==null && other.getCodice()==null) || 
             (this.codice!=null &&
              this.codice.equals(other.getCodice()))) &&
            ((this.crs==null && other.getCrs()==null) || 
             (this.crs!=null &&
              this.crs.equals(other.getCrs()))) &&
            ((this.idUnivocoRiscossione==null && other.getIdUnivocoRiscossione()==null) || 
             (this.idUnivocoRiscossione!=null &&
              this.idUnivocoRiscossione.equals(other.getIdUnivocoRiscossione()))) &&
            this.importo == other.getImporto() &&
            ((this.iuv==null && other.getIuv()==null) || 
             (this.iuv!=null &&
              this.iuv.equals(other.getIuv())));
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
        if (getCodice() != null) {
            _hashCode += getCodice().hashCode();
        }
        if (getCrs() != null) {
            _hashCode += getCrs().hashCode();
        }
        if (getIdUnivocoRiscossione() != null) {
            _hashCode += getIdUnivocoRiscossione().hashCode();
        }
        _hashCode += new Float(getImporto()).hashCode();
        if (getIuv() != null) {
            _hashCode += getIuv().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PagamentoRes.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "pagamentoRes"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("crs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "crs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUnivocoRiscossione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUnivocoRiscossione"));
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
        elemField.setFieldName("iuv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "iuv"));
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
