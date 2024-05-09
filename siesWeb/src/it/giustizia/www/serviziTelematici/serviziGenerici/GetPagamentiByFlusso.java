/**
 * GetPagamentiByFlusso.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class GetPagamentiByFlusso  implements java.io.Serializable {
    private java.lang.String codUffNep;

    private java.lang.String idFlusso;

    private java.util.Calendar dataOraFlusso;

    private java.lang.String idPSP;

    public GetPagamentiByFlusso() {
    }

    public GetPagamentiByFlusso(
           java.lang.String codUffNep,
           java.lang.String idFlusso,
           java.util.Calendar dataOraFlusso,
           java.lang.String idPSP) {
           this.codUffNep = codUffNep;
           this.idFlusso = idFlusso;
           this.dataOraFlusso = dataOraFlusso;
           this.idPSP = idPSP;
    }


    /**
     * Gets the codUffNep value for this GetPagamentiByFlusso.
     * 
     * @return codUffNep
     */
    public java.lang.String getCodUffNep() {
        return codUffNep;
    }


    /**
     * Sets the codUffNep value for this GetPagamentiByFlusso.
     * 
     * @param codUffNep
     */
    public void setCodUffNep(java.lang.String codUffNep) {
        this.codUffNep = codUffNep;
    }


    /**
     * Gets the idFlusso value for this GetPagamentiByFlusso.
     * 
     * @return idFlusso
     */
    public java.lang.String getIdFlusso() {
        return idFlusso;
    }


    /**
     * Sets the idFlusso value for this GetPagamentiByFlusso.
     * 
     * @param idFlusso
     */
    public void setIdFlusso(java.lang.String idFlusso) {
        this.idFlusso = idFlusso;
    }


    /**
     * Gets the dataOraFlusso value for this GetPagamentiByFlusso.
     * 
     * @return dataOraFlusso
     */
    public java.util.Calendar getDataOraFlusso() {
        return dataOraFlusso;
    }


    /**
     * Sets the dataOraFlusso value for this GetPagamentiByFlusso.
     * 
     * @param dataOraFlusso
     */
    public void setDataOraFlusso(java.util.Calendar dataOraFlusso) {
        this.dataOraFlusso = dataOraFlusso;
    }


    /**
     * Gets the idPSP value for this GetPagamentiByFlusso.
     * 
     * @return idPSP
     */
    public java.lang.String getIdPSP() {
        return idPSP;
    }


    /**
     * Sets the idPSP value for this GetPagamentiByFlusso.
     * 
     * @param idPSP
     */
    public void setIdPSP(java.lang.String idPSP) {
        this.idPSP = idPSP;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetPagamentiByFlusso)) return false;
        GetPagamentiByFlusso other = (GetPagamentiByFlusso) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codUffNep==null && other.getCodUffNep()==null) || 
             (this.codUffNep!=null &&
              this.codUffNep.equals(other.getCodUffNep()))) &&
            ((this.idFlusso==null && other.getIdFlusso()==null) || 
             (this.idFlusso!=null &&
              this.idFlusso.equals(other.getIdFlusso()))) &&
            ((this.dataOraFlusso==null && other.getDataOraFlusso()==null) || 
             (this.dataOraFlusso!=null &&
              this.dataOraFlusso.equals(other.getDataOraFlusso()))) &&
            ((this.idPSP==null && other.getIdPSP()==null) || 
             (this.idPSP!=null &&
              this.idPSP.equals(other.getIdPSP())));
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
        if (getCodUffNep() != null) {
            _hashCode += getCodUffNep().hashCode();
        }
        if (getIdFlusso() != null) {
            _hashCode += getIdFlusso().hashCode();
        }
        if (getDataOraFlusso() != null) {
            _hashCode += getDataOraFlusso().hashCode();
        }
        if (getIdPSP() != null) {
            _hashCode += getIdPSP().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetPagamentiByFlusso.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentiByFlusso"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codUffNep");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codUffNep"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idFlusso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idFlusso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataOraFlusso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataOraFlusso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idPSP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idPSP"));
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
