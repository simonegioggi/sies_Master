/**
 * IndirizziAbilitati.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class IndirizziAbilitati  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String codiceFiscaleFirma;

    private java.lang.String pec;

    public IndirizziAbilitati() {
    }

    public IndirizziAbilitati(
           java.lang.String id,
           java.lang.String codiceFiscaleFirma,
           java.lang.String pec) {
        this.id = id;
        this.codiceFiscaleFirma = codiceFiscaleFirma;
        this.pec = pec;
    }


    /**
     * Gets the id value for this IndirizziAbilitati.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this IndirizziAbilitati.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the codiceFiscaleFirma value for this IndirizziAbilitati.
     * 
     * @return codiceFiscaleFirma
     */
    public java.lang.String getCodiceFiscaleFirma() {
        return codiceFiscaleFirma;
    }


    /**
     * Sets the codiceFiscaleFirma value for this IndirizziAbilitati.
     * 
     * @param codiceFiscaleFirma
     */
    public void setCodiceFiscaleFirma(java.lang.String codiceFiscaleFirma) {
        this.codiceFiscaleFirma = codiceFiscaleFirma;
    }


    /**
     * Gets the pec value for this IndirizziAbilitati.
     * 
     * @return pec
     */
    public java.lang.String getPec() {
        return pec;
    }


    /**
     * Sets the pec value for this IndirizziAbilitati.
     * 
     * @param pec
     */
    public void setPec(java.lang.String pec) {
        this.pec = pec;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IndirizziAbilitati)) return false;
        IndirizziAbilitati other = (IndirizziAbilitati) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.codiceFiscaleFirma==null && other.getCodiceFiscaleFirma()==null) || 
             (this.codiceFiscaleFirma!=null &&
              this.codiceFiscaleFirma.equals(other.getCodiceFiscaleFirma()))) &&
            ((this.pec==null && other.getPec()==null) || 
             (this.pec!=null &&
              this.pec.equals(other.getPec())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getCodiceFiscaleFirma() != null) {
            _hashCode += getCodiceFiscaleFirma().hashCode();
        }
        if (getPec() != null) {
            _hashCode += getPec().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IndirizziAbilitati.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "indirizziAbilitati"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceFiscaleFirma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceFiscaleFirma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pec");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pec"));
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
