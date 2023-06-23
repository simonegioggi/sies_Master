/**
 * RicercaFlussiRendicontazioneScaricati.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class RicercaFlussiRendicontazioneScaricati  implements java.io.Serializable {
    private java.util.Calendar dataScaricoDa;

    private java.util.Calendar dataScaricoA;

    private boolean crsErrore;

    private int dimensionePagina;

    private int numeroPagina;

    public RicercaFlussiRendicontazioneScaricati() {
    }

    public RicercaFlussiRendicontazioneScaricati(
           java.util.Calendar dataScaricoDa,
           java.util.Calendar dataScaricoA,
           boolean crsErrore,
           int dimensionePagina,
           int numeroPagina) {
           this.dataScaricoDa = dataScaricoDa;
           this.dataScaricoA = dataScaricoA;
           this.crsErrore = crsErrore;
           this.dimensionePagina = dimensionePagina;
           this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the dataScaricoDa value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @return dataScaricoDa
     */
    public java.util.Calendar getDataScaricoDa() {
        return dataScaricoDa;
    }


    /**
     * Sets the dataScaricoDa value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @param dataScaricoDa
     */
    public void setDataScaricoDa(java.util.Calendar dataScaricoDa) {
        this.dataScaricoDa = dataScaricoDa;
    }


    /**
     * Gets the dataScaricoA value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @return dataScaricoA
     */
    public java.util.Calendar getDataScaricoA() {
        return dataScaricoA;
    }


    /**
     * Sets the dataScaricoA value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @param dataScaricoA
     */
    public void setDataScaricoA(java.util.Calendar dataScaricoA) {
        this.dataScaricoA = dataScaricoA;
    }


    /**
     * Gets the crsErrore value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @return crsErrore
     */
    public boolean isCrsErrore() {
        return crsErrore;
    }


    /**
     * Sets the crsErrore value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @param crsErrore
     */
    public void setCrsErrore(boolean crsErrore) {
        this.crsErrore = crsErrore;
    }


    /**
     * Gets the dimensionePagina value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the numeroPagina value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this RicercaFlussiRendicontazioneScaricati.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RicercaFlussiRendicontazioneScaricati)) return false;
        RicercaFlussiRendicontazioneScaricati other = (RicercaFlussiRendicontazioneScaricati) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataScaricoDa==null && other.getDataScaricoDa()==null) || 
             (this.dataScaricoDa!=null &&
              this.dataScaricoDa.equals(other.getDataScaricoDa()))) &&
            ((this.dataScaricoA==null && other.getDataScaricoA()==null) || 
             (this.dataScaricoA!=null &&
              this.dataScaricoA.equals(other.getDataScaricoA()))) &&
            this.crsErrore == other.isCrsErrore() &&
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
        if (getDataScaricoDa() != null) {
            _hashCode += getDataScaricoDa().hashCode();
        }
        if (getDataScaricoA() != null) {
            _hashCode += getDataScaricoA().hashCode();
        }
        _hashCode += (isCrsErrore() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getDimensionePagina();
        _hashCode += getNumeroPagina();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RicercaFlussiRendicontazioneScaricati.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaFlussiRendicontazioneScaricati"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataScaricoDa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataScaricoDa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataScaricoA");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataScaricoA"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("crsErrore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "crsErrore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
