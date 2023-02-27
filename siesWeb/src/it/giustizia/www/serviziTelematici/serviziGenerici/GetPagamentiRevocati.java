/**
 * GetPagamentiRevocati.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class GetPagamentiRevocati  implements java.io.Serializable {
    private java.util.Calendar dataRicevutaRevocataDa;

    private java.util.Calendar dataRicevutaRevocataA;

    private java.util.Calendar dataRevocataDa;

    private java.util.Calendar dataRevocataA;

    private int dimensionePagina;

    private int numeroPagina;

    public GetPagamentiRevocati() {
    }

    public GetPagamentiRevocati(
           java.util.Calendar dataRicevutaRevocataDa,
           java.util.Calendar dataRicevutaRevocataA,
           java.util.Calendar dataRevocataDa,
           java.util.Calendar dataRevocataA,
           int dimensionePagina,
           int numeroPagina) {
           this.dataRicevutaRevocataDa = dataRicevutaRevocataDa;
           this.dataRicevutaRevocataA = dataRicevutaRevocataA;
           this.dataRevocataDa = dataRevocataDa;
           this.dataRevocataA = dataRevocataA;
           this.dimensionePagina = dimensionePagina;
           this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the dataRicevutaRevocataDa value for this GetPagamentiRevocati.
     * 
     * @return dataRicevutaRevocataDa
     */
    public java.util.Calendar getDataRicevutaRevocataDa() {
        return dataRicevutaRevocataDa;
    }


    /**
     * Sets the dataRicevutaRevocataDa value for this GetPagamentiRevocati.
     * 
     * @param dataRicevutaRevocataDa
     */
    public void setDataRicevutaRevocataDa(java.util.Calendar dataRicevutaRevocataDa) {
        this.dataRicevutaRevocataDa = dataRicevutaRevocataDa;
    }


    /**
     * Gets the dataRicevutaRevocataA value for this GetPagamentiRevocati.
     * 
     * @return dataRicevutaRevocataA
     */
    public java.util.Calendar getDataRicevutaRevocataA() {
        return dataRicevutaRevocataA;
    }


    /**
     * Sets the dataRicevutaRevocataA value for this GetPagamentiRevocati.
     * 
     * @param dataRicevutaRevocataA
     */
    public void setDataRicevutaRevocataA(java.util.Calendar dataRicevutaRevocataA) {
        this.dataRicevutaRevocataA = dataRicevutaRevocataA;
    }


    /**
     * Gets the dataRevocataDa value for this GetPagamentiRevocati.
     * 
     * @return dataRevocataDa
     */
    public java.util.Calendar getDataRevocataDa() {
        return dataRevocataDa;
    }


    /**
     * Sets the dataRevocataDa value for this GetPagamentiRevocati.
     * 
     * @param dataRevocataDa
     */
    public void setDataRevocataDa(java.util.Calendar dataRevocataDa) {
        this.dataRevocataDa = dataRevocataDa;
    }


    /**
     * Gets the dataRevocataA value for this GetPagamentiRevocati.
     * 
     * @return dataRevocataA
     */
    public java.util.Calendar getDataRevocataA() {
        return dataRevocataA;
    }


    /**
     * Sets the dataRevocataA value for this GetPagamentiRevocati.
     * 
     * @param dataRevocataA
     */
    public void setDataRevocataA(java.util.Calendar dataRevocataA) {
        this.dataRevocataA = dataRevocataA;
    }


    /**
     * Gets the dimensionePagina value for this GetPagamentiRevocati.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this GetPagamentiRevocati.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the numeroPagina value for this GetPagamentiRevocati.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this GetPagamentiRevocati.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetPagamentiRevocati)) return false;
        GetPagamentiRevocati other = (GetPagamentiRevocati) obj;
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
            ((this.dataRevocataDa==null && other.getDataRevocataDa()==null) || 
             (this.dataRevocataDa!=null &&
              this.dataRevocataDa.equals(other.getDataRevocataDa()))) &&
            ((this.dataRevocataA==null && other.getDataRevocataA()==null) || 
             (this.dataRevocataA!=null &&
              this.dataRevocataA.equals(other.getDataRevocataA()))) &&
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
        if (getDataRevocataDa() != null) {
            _hashCode += getDataRevocataDa().hashCode();
        }
        if (getDataRevocataA() != null) {
            _hashCode += getDataRevocataA().hashCode();
        }
        _hashCode += getDimensionePagina();
        _hashCode += getNumeroPagina();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetPagamentiRevocati.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentiRevocati"));
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
        elemField.setFieldName("dataRevocataDa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRevocataDa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRevocataA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRevocataA"));
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
