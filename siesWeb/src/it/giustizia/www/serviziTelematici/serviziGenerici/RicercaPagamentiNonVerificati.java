/**
 * RicercaPagamentiNonVerificati.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class RicercaPagamentiNonVerificati  implements java.io.Serializable {
    private java.util.Calendar dataControlloDa;

    private java.util.Calendar dataControlloA;

    private java.lang.String idFlusso;

    private java.util.Calendar dataRicevutaDa;

    private java.util.Calendar dataRicevutaA;

    private int dimensionePagina;

    private int numeroPagina;

    public RicercaPagamentiNonVerificati() {
    }

    public RicercaPagamentiNonVerificati(
           java.util.Calendar dataControlloDa,
           java.util.Calendar dataControlloA,
           java.lang.String idFlusso,
           java.util.Calendar dataRicevutaDa,
           java.util.Calendar dataRicevutaA,
           int dimensionePagina,
           int numeroPagina) {
           this.dataControlloDa = dataControlloDa;
           this.dataControlloA = dataControlloA;
           this.idFlusso = idFlusso;
           this.dataRicevutaDa = dataRicevutaDa;
           this.dataRicevutaA = dataRicevutaA;
           this.dimensionePagina = dimensionePagina;
           this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the dataControlloDa value for this RicercaPagamentiNonVerificati.
     * 
     * @return dataControlloDa
     */
    public java.util.Calendar getDataControlloDa() {
        return dataControlloDa;
    }


    /**
     * Sets the dataControlloDa value for this RicercaPagamentiNonVerificati.
     * 
     * @param dataControlloDa
     */
    public void setDataControlloDa(java.util.Calendar dataControlloDa) {
        this.dataControlloDa = dataControlloDa;
    }


    /**
     * Gets the dataControlloA value for this RicercaPagamentiNonVerificati.
     * 
     * @return dataControlloA
     */
    public java.util.Calendar getDataControlloA() {
        return dataControlloA;
    }


    /**
     * Sets the dataControlloA value for this RicercaPagamentiNonVerificati.
     * 
     * @param dataControlloA
     */
    public void setDataControlloA(java.util.Calendar dataControlloA) {
        this.dataControlloA = dataControlloA;
    }


    /**
     * Gets the idFlusso value for this RicercaPagamentiNonVerificati.
     * 
     * @return idFlusso
     */
    public java.lang.String getIdFlusso() {
        return idFlusso;
    }


    /**
     * Sets the idFlusso value for this RicercaPagamentiNonVerificati.
     * 
     * @param idFlusso
     */
    public void setIdFlusso(java.lang.String idFlusso) {
        this.idFlusso = idFlusso;
    }


    /**
     * Gets the dataRicevutaDa value for this RicercaPagamentiNonVerificati.
     * 
     * @return dataRicevutaDa
     */
    public java.util.Calendar getDataRicevutaDa() {
        return dataRicevutaDa;
    }


    /**
     * Sets the dataRicevutaDa value for this RicercaPagamentiNonVerificati.
     * 
     * @param dataRicevutaDa
     */
    public void setDataRicevutaDa(java.util.Calendar dataRicevutaDa) {
        this.dataRicevutaDa = dataRicevutaDa;
    }


    /**
     * Gets the dataRicevutaA value for this RicercaPagamentiNonVerificati.
     * 
     * @return dataRicevutaA
     */
    public java.util.Calendar getDataRicevutaA() {
        return dataRicevutaA;
    }


    /**
     * Sets the dataRicevutaA value for this RicercaPagamentiNonVerificati.
     * 
     * @param dataRicevutaA
     */
    public void setDataRicevutaA(java.util.Calendar dataRicevutaA) {
        this.dataRicevutaA = dataRicevutaA;
    }


    /**
     * Gets the dimensionePagina value for this RicercaPagamentiNonVerificati.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this RicercaPagamentiNonVerificati.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the numeroPagina value for this RicercaPagamentiNonVerificati.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this RicercaPagamentiNonVerificati.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RicercaPagamentiNonVerificati)) return false;
        RicercaPagamentiNonVerificati other = (RicercaPagamentiNonVerificati) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataControlloDa==null && other.getDataControlloDa()==null) || 
             (this.dataControlloDa!=null &&
              this.dataControlloDa.equals(other.getDataControlloDa()))) &&
            ((this.dataControlloA==null && other.getDataControlloA()==null) || 
             (this.dataControlloA!=null &&
              this.dataControlloA.equals(other.getDataControlloA()))) &&
            ((this.idFlusso==null && other.getIdFlusso()==null) || 
             (this.idFlusso!=null &&
              this.idFlusso.equals(other.getIdFlusso()))) &&
            ((this.dataRicevutaDa==null && other.getDataRicevutaDa()==null) || 
             (this.dataRicevutaDa!=null &&
              this.dataRicevutaDa.equals(other.getDataRicevutaDa()))) &&
            ((this.dataRicevutaA==null && other.getDataRicevutaA()==null) || 
             (this.dataRicevutaA!=null &&
              this.dataRicevutaA.equals(other.getDataRicevutaA()))) &&
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
        if (getDataControlloDa() != null) {
            _hashCode += getDataControlloDa().hashCode();
        }
        if (getDataControlloA() != null) {
            _hashCode += getDataControlloA().hashCode();
        }
        if (getIdFlusso() != null) {
            _hashCode += getIdFlusso().hashCode();
        }
        if (getDataRicevutaDa() != null) {
            _hashCode += getDataRicevutaDa().hashCode();
        }
        if (getDataRicevutaA() != null) {
            _hashCode += getDataRicevutaA().hashCode();
        }
        _hashCode += getDimensionePagina();
        _hashCode += getNumeroPagina();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RicercaPagamentiNonVerificati.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaPagamentiNonVerificati"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataControlloDa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataControlloDa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataControlloA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataControlloA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("dataRicevutaDa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicevutaDa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataRicevutaA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataRicevutaA"));
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
