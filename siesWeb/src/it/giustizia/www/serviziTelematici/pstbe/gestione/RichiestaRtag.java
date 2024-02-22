/**
 * RichiestaRtag.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class RichiestaRtag  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String idRepertorio;

    private java.lang.String idUnivoco;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.LookupUfficio ufficio;

    public RichiestaRtag() {
    }

    public RichiestaRtag(
           java.lang.String id,
           java.lang.String idRepertorio,
           java.lang.String idUnivoco,
           it.giustizia.www.serviziTelematici.pstbe.gestione.LookupUfficio ufficio) {
        this.id = id;
        this.idRepertorio = idRepertorio;
        this.idUnivoco = idUnivoco;
        this.ufficio = ufficio;
    }


    /**
     * Gets the id value for this RichiestaRtag.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this RichiestaRtag.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the idRepertorio value for this RichiestaRtag.
     * 
     * @return idRepertorio
     */
    public java.lang.String getIdRepertorio() {
        return idRepertorio;
    }


    /**
     * Sets the idRepertorio value for this RichiestaRtag.
     * 
     * @param idRepertorio
     */
    public void setIdRepertorio(java.lang.String idRepertorio) {
        this.idRepertorio = idRepertorio;
    }


    /**
     * Gets the idUnivoco value for this RichiestaRtag.
     * 
     * @return idUnivoco
     */
    public java.lang.String getIdUnivoco() {
        return idUnivoco;
    }


    /**
     * Sets the idUnivoco value for this RichiestaRtag.
     * 
     * @param idUnivoco
     */
    public void setIdUnivoco(java.lang.String idUnivoco) {
        this.idUnivoco = idUnivoco;
    }


    /**
     * Gets the ufficio value for this RichiestaRtag.
     * 
     * @return ufficio
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.LookupUfficio getUfficio() {
        return ufficio;
    }


    /**
     * Sets the ufficio value for this RichiestaRtag.
     * 
     * @param ufficio
     */
    public void setUfficio(it.giustizia.www.serviziTelematici.pstbe.gestione.LookupUfficio ufficio) {
        this.ufficio = ufficio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RichiestaRtag)) return false;
        RichiestaRtag other = (RichiestaRtag) obj;
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
            ((this.idRepertorio==null && other.getIdRepertorio()==null) || 
             (this.idRepertorio!=null &&
              this.idRepertorio.equals(other.getIdRepertorio()))) &&
            ((this.idUnivoco==null && other.getIdUnivoco()==null) || 
             (this.idUnivoco!=null &&
              this.idUnivoco.equals(other.getIdUnivoco()))) &&
            ((this.ufficio==null && other.getUfficio()==null) || 
             (this.ufficio!=null &&
              this.ufficio.equals(other.getUfficio())));
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
        if (getIdRepertorio() != null) {
            _hashCode += getIdRepertorio().hashCode();
        }
        if (getIdUnivoco() != null) {
            _hashCode += getIdUnivoco().hashCode();
        }
        if (getUfficio() != null) {
            _hashCode += getUfficio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RichiestaRtag.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "richiestaRtag"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idRepertorio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idRepertorio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUnivoco");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idUnivoco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ufficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ufficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "lookupUfficio"));
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
