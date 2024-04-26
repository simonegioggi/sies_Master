/**
 * DownloadPDFRicevuta.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class DownloadPDFRicevuta  implements java.io.Serializable {
    private java.lang.String idPagamento;

    private boolean bollo;

    public DownloadPDFRicevuta() {
    }

    public DownloadPDFRicevuta(
           java.lang.String idPagamento,
           boolean bollo) {
           this.idPagamento = idPagamento;
           this.bollo = bollo;
    }


    /**
     * Gets the idPagamento value for this DownloadPDFRicevuta.
     * 
     * @return idPagamento
     */
    public java.lang.String getIdPagamento() {
        return idPagamento;
    }


    /**
     * Sets the idPagamento value for this DownloadPDFRicevuta.
     * 
     * @param idPagamento
     */
    public void setIdPagamento(java.lang.String idPagamento) {
        this.idPagamento = idPagamento;
    }


    /**
     * Gets the bollo value for this DownloadPDFRicevuta.
     * 
     * @return bollo
     */
    public boolean isBollo() {
        return bollo;
    }


    /**
     * Sets the bollo value for this DownloadPDFRicevuta.
     * 
     * @param bollo
     */
    public void setBollo(boolean bollo) {
        this.bollo = bollo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DownloadPDFRicevuta)) return false;
        DownloadPDFRicevuta other = (DownloadPDFRicevuta) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idPagamento==null && other.getIdPagamento()==null) || 
             (this.idPagamento!=null &&
              this.idPagamento.equals(other.getIdPagamento()))) &&
            this.bollo == other.isBollo();
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
        if (getIdPagamento() != null) {
            _hashCode += getIdPagamento().hashCode();
        }
        _hashCode += (isBollo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DownloadPDFRicevuta.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadPDFRicevuta"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPagamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idPagamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bollo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bollo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
