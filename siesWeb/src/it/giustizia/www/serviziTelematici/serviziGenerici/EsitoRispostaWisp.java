/**
 * EsitoRispostaWisp.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class EsitoRispostaWisp  implements java.io.Serializable {
    private java.lang.Boolean areaPubblica;

    private boolean pagamentoBollo;

    private java.lang.String urlRedirect;

    public EsitoRispostaWisp() {
    }

    public EsitoRispostaWisp(
           java.lang.Boolean areaPubblica,
           boolean pagamentoBollo,
           java.lang.String urlRedirect) {
           this.areaPubblica = areaPubblica;
           this.pagamentoBollo = pagamentoBollo;
           this.urlRedirect = urlRedirect;
    }


    /**
     * Gets the areaPubblica value for this EsitoRispostaWisp.
     * 
     * @return areaPubblica
     */
    public java.lang.Boolean getAreaPubblica() {
        return areaPubblica;
    }


    /**
     * Sets the areaPubblica value for this EsitoRispostaWisp.
     * 
     * @param areaPubblica
     */
    public void setAreaPubblica(java.lang.Boolean areaPubblica) {
        this.areaPubblica = areaPubblica;
    }


    /**
     * Gets the pagamentoBollo value for this EsitoRispostaWisp.
     * 
     * @return pagamentoBollo
     */
    public boolean isPagamentoBollo() {
        return pagamentoBollo;
    }


    /**
     * Sets the pagamentoBollo value for this EsitoRispostaWisp.
     * 
     * @param pagamentoBollo
     */
    public void setPagamentoBollo(boolean pagamentoBollo) {
        this.pagamentoBollo = pagamentoBollo;
    }


    /**
     * Gets the urlRedirect value for this EsitoRispostaWisp.
     * 
     * @return urlRedirect
     */
    public java.lang.String getUrlRedirect() {
        return urlRedirect;
    }


    /**
     * Sets the urlRedirect value for this EsitoRispostaWisp.
     * 
     * @param urlRedirect
     */
    public void setUrlRedirect(java.lang.String urlRedirect) {
        this.urlRedirect = urlRedirect;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EsitoRispostaWisp)) return false;
        EsitoRispostaWisp other = (EsitoRispostaWisp) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.areaPubblica==null && other.getAreaPubblica()==null) || 
             (this.areaPubblica!=null &&
              this.areaPubblica.equals(other.getAreaPubblica()))) &&
            this.pagamentoBollo == other.isPagamentoBollo() &&
            ((this.urlRedirect==null && other.getUrlRedirect()==null) || 
             (this.urlRedirect!=null &&
              this.urlRedirect.equals(other.getUrlRedirect())));
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
        if (getAreaPubblica() != null) {
            _hashCode += getAreaPubblica().hashCode();
        }
        _hashCode += (isPagamentoBollo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getUrlRedirect() != null) {
            _hashCode += getUrlRedirect().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EsitoRispostaWisp.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoRispostaWisp"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("areaPubblica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "areaPubblica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pagamentoBollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pagamentoBollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlRedirect");
        elemField.setXmlName(new javax.xml.namespace.QName("", "urlRedirect"));
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
