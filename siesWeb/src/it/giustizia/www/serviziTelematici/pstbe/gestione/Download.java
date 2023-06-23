/**
 * Download.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class Download  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.util.Calendar dataAvviso;

    private java.util.Calendar dataDownload;

    private java.lang.String destinazione;

    private java.lang.String sito;

    public Download() {
    }

    public Download(
           java.lang.String id,
           java.util.Calendar dataAvviso,
           java.util.Calendar dataDownload,
           java.lang.String destinazione,
           java.lang.String sito) {
        this.id = id;
        this.dataAvviso = dataAvviso;
        this.dataDownload = dataDownload;
        this.destinazione = destinazione;
        this.sito = sito;
    }


    /**
     * Gets the id value for this Download.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Download.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the dataAvviso value for this Download.
     * 
     * @return dataAvviso
     */
    public java.util.Calendar getDataAvviso() {
        return dataAvviso;
    }


    /**
     * Sets the dataAvviso value for this Download.
     * 
     * @param dataAvviso
     */
    public void setDataAvviso(java.util.Calendar dataAvviso) {
        this.dataAvviso = dataAvviso;
    }


    /**
     * Gets the dataDownload value for this Download.
     * 
     * @return dataDownload
     */
    public java.util.Calendar getDataDownload() {
        return dataDownload;
    }


    /**
     * Sets the dataDownload value for this Download.
     * 
     * @param dataDownload
     */
    public void setDataDownload(java.util.Calendar dataDownload) {
        this.dataDownload = dataDownload;
    }


    /**
     * Gets the destinazione value for this Download.
     * 
     * @return destinazione
     */
    public java.lang.String getDestinazione() {
        return destinazione;
    }


    /**
     * Sets the destinazione value for this Download.
     * 
     * @param destinazione
     */
    public void setDestinazione(java.lang.String destinazione) {
        this.destinazione = destinazione;
    }


    /**
     * Gets the sito value for this Download.
     * 
     * @return sito
     */
    public java.lang.String getSito() {
        return sito;
    }


    /**
     * Sets the sito value for this Download.
     * 
     * @param sito
     */
    public void setSito(java.lang.String sito) {
        this.sito = sito;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Download)) return false;
        Download other = (Download) obj;
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
            ((this.dataAvviso==null && other.getDataAvviso()==null) || 
             (this.dataAvviso!=null &&
              this.dataAvviso.equals(other.getDataAvviso()))) &&
            ((this.dataDownload==null && other.getDataDownload()==null) || 
             (this.dataDownload!=null &&
              this.dataDownload.equals(other.getDataDownload()))) &&
            ((this.destinazione==null && other.getDestinazione()==null) || 
             (this.destinazione!=null &&
              this.destinazione.equals(other.getDestinazione()))) &&
            ((this.sito==null && other.getSito()==null) || 
             (this.sito!=null &&
              this.sito.equals(other.getSito())));
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
        if (getDataAvviso() != null) {
            _hashCode += getDataAvviso().hashCode();
        }
        if (getDataDownload() != null) {
            _hashCode += getDataDownload().hashCode();
        }
        if (getDestinazione() != null) {
            _hashCode += getDestinazione().hashCode();
        }
        if (getSito() != null) {
            _hashCode += getSito().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Download.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "download"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAvviso");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataAvviso"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDownload");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataDownload"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sito");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sito"));
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
