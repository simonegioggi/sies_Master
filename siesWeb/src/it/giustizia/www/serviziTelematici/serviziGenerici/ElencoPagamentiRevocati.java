/**
 * ElencoPagamentiRevocati.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ElencoPagamentiRevocati  implements java.io.Serializable {
    private java.util.Calendar dataRicevutaRevocataDa;

    private java.util.Calendar dataRicevutaRevocataA;

    private int dimensionePagina;

    private int numeroPagina;

    public ElencoPagamentiRevocati() {
    }

    public ElencoPagamentiRevocati(
           java.util.Calendar dataRicevutaRevocataDa,
           java.util.Calendar dataRicevutaRevocataA,
           int dimensionePagina,
           int numeroPagina) {
           this.dataRicevutaRevocataDa = dataRicevutaRevocataDa;
           this.dataRicevutaRevocataA = dataRicevutaRevocataA;
           this.dimensionePagina = dimensionePagina;
           this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the dataRicevutaRevocataDa value for this ElencoPagamentiRevocati.
     * 
     * @return dataRicevutaRevocataDa
     */
    public java.util.Calendar getDataRicevutaRevocataDa() {
        return dataRicevutaRevocataDa;
    }


    /**
     * Sets the dataRicevutaRevocataDa value for this ElencoPagamentiRevocati.
     * 
     * @param dataRicevutaRevocataDa
     */
    public void setDataRicevutaRevocataDa(java.util.Calendar dataRicevutaRevocataDa) {
        this.dataRicevutaRevocataDa = dataRicevutaRevocataDa;
    }


    /**
     * Gets the dataRicevutaRevocataA value for this ElencoPagamentiRevocati.
     * 
     * @return dataRicevutaRevocataA
     */
    public java.util.Calendar getDataRicevutaRevocataA() {
        return dataRicevutaRevocataA;
    }


    /**
     * Sets the dataRicevutaRevocataA value for this ElencoPagamentiRevocati.
     * 
     * @param dataRicevutaRevocataA
     */
    public void setDataRicevutaRevocataA(java.util.Calendar dataRicevutaRevocataA) {
        this.dataRicevutaRevocataA = dataRicevutaRevocataA;
    }


    /**
     * Gets the dimensionePagina value for this ElencoPagamentiRevocati.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this ElencoPagamentiRevocati.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the numeroPagina value for this ElencoPagamentiRevocati.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this ElencoPagamentiRevocati.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ElencoPagamentiRevocati)) return false;
        ElencoPagamentiRevocati other = (ElencoPagamentiRevocati) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataRicevutaRevocataDa==null && other.getDataRicevutaRevocataDa()==null) || 
             (this.dataRicevutaRevocataDa!=null &&
              this.dataRicevutaRevocataDa.equals(other.getDataRicevutaRevocataDa()))) &&
            ((this.dataRicevutaRevocataA==null && other.getDataRicevutaRevocataA()==null) || 
             (this.dataRicevutaRevocataA!=null &&
              this.dataRicevutaRevocataA.equals(other.getDataRicevutaRevocataA()))) &&
            this.dimensionePagina == other.getDimensionePagina() &&
            this.numeroPagina == other.getNumeroPagina();
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
        if (getDataRicevutaRevocataDa() != null) {
            _hashCode += getDataRicevutaRevocataDa().hashCode();
        }
        if (getDataRicevutaRevocataA() != null) {
            _hashCode += getDataRicevutaRevocataA().hashCode();
        }
        _hashCode += getDimensionePagina();
        _hashCode += getNumeroPagina();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ElencoPagamentiRevocati.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamentiRevocati"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicevutaRevocataDa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicevutaRevocataDa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicevutaRevocataA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicevutaRevocataA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dimensionePagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dimensionePagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroPagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroPagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
