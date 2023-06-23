/**
 * PagamentoRevocato.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class PagamentoRevocato  implements java.io.Serializable {
    private java.util.Calendar data_er;

    private java.util.Calendar data_revoca;

    private java.lang.String esito_er;

    private java.lang.String esito_rr;

    private java.lang.String id_crs;

    private java.lang.String id_er;

    private java.lang.String id_revoca;

    private java.util.Calendar rt_revocata_data;

    private float rt_revocata_importo;

    public PagamentoRevocato() {
    }

    public PagamentoRevocato(
           java.util.Calendar data_er,
           java.util.Calendar data_revoca,
           java.lang.String esito_er,
           java.lang.String esito_rr,
           java.lang.String id_crs,
           java.lang.String id_er,
           java.lang.String id_revoca,
           java.util.Calendar rt_revocata_data,
           float rt_revocata_importo) {
           this.data_er = data_er;
           this.data_revoca = data_revoca;
           this.esito_er = esito_er;
           this.esito_rr = esito_rr;
           this.id_crs = id_crs;
           this.id_er = id_er;
           this.id_revoca = id_revoca;
           this.rt_revocata_data = rt_revocata_data;
           this.rt_revocata_importo = rt_revocata_importo;
    }


    /**
     * Gets the data_er value for this PagamentoRevocato.
     * 
     * @return data_er
     */
    public java.util.Calendar getData_er() {
        return data_er;
    }


    /**
     * Sets the data_er value for this PagamentoRevocato.
     * 
     * @param data_er
     */
    public void setData_er(java.util.Calendar data_er) {
        this.data_er = data_er;
    }


    /**
     * Gets the data_revoca value for this PagamentoRevocato.
     * 
     * @return data_revoca
     */
    public java.util.Calendar getData_revoca() {
        return data_revoca;
    }


    /**
     * Sets the data_revoca value for this PagamentoRevocato.
     * 
     * @param data_revoca
     */
    public void setData_revoca(java.util.Calendar data_revoca) {
        this.data_revoca = data_revoca;
    }


    /**
     * Gets the esito_er value for this PagamentoRevocato.
     * 
     * @return esito_er
     */
    public java.lang.String getEsito_er() {
        return esito_er;
    }


    /**
     * Sets the esito_er value for this PagamentoRevocato.
     * 
     * @param esito_er
     */
    public void setEsito_er(java.lang.String esito_er) {
        this.esito_er = esito_er;
    }


    /**
     * Gets the esito_rr value for this PagamentoRevocato.
     * 
     * @return esito_rr
     */
    public java.lang.String getEsito_rr() {
        return esito_rr;
    }


    /**
     * Sets the esito_rr value for this PagamentoRevocato.
     * 
     * @param esito_rr
     */
    public void setEsito_rr(java.lang.String esito_rr) {
        this.esito_rr = esito_rr;
    }


    /**
     * Gets the id_crs value for this PagamentoRevocato.
     * 
     * @return id_crs
     */
    public java.lang.String getId_crs() {
        return id_crs;
    }


    /**
     * Sets the id_crs value for this PagamentoRevocato.
     * 
     * @param id_crs
     */
    public void setId_crs(java.lang.String id_crs) {
        this.id_crs = id_crs;
    }


    /**
     * Gets the id_er value for this PagamentoRevocato.
     * 
     * @return id_er
     */
    public java.lang.String getId_er() {
        return id_er;
    }


    /**
     * Sets the id_er value for this PagamentoRevocato.
     * 
     * @param id_er
     */
    public void setId_er(java.lang.String id_er) {
        this.id_er = id_er;
    }


    /**
     * Gets the id_revoca value for this PagamentoRevocato.
     * 
     * @return id_revoca
     */
    public java.lang.String getId_revoca() {
        return id_revoca;
    }


    /**
     * Sets the id_revoca value for this PagamentoRevocato.
     * 
     * @param id_revoca
     */
    public void setId_revoca(java.lang.String id_revoca) {
        this.id_revoca = id_revoca;
    }


    /**
     * Gets the rt_revocata_data value for this PagamentoRevocato.
     * 
     * @return rt_revocata_data
     */
    public java.util.Calendar getRt_revocata_data() {
        return rt_revocata_data;
    }


    /**
     * Sets the rt_revocata_data value for this PagamentoRevocato.
     * 
     * @param rt_revocata_data
     */
    public void setRt_revocata_data(java.util.Calendar rt_revocata_data) {
        this.rt_revocata_data = rt_revocata_data;
    }


    /**
     * Gets the rt_revocata_importo value for this PagamentoRevocato.
     * 
     * @return rt_revocata_importo
     */
    public float getRt_revocata_importo() {
        return rt_revocata_importo;
    }


    /**
     * Sets the rt_revocata_importo value for this PagamentoRevocato.
     * 
     * @param rt_revocata_importo
     */
    public void setRt_revocata_importo(float rt_revocata_importo) {
        this.rt_revocata_importo = rt_revocata_importo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PagamentoRevocato)) return false;
        PagamentoRevocato other = (PagamentoRevocato) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.data_er==null && other.getData_er()==null) || 
             (this.data_er!=null &&
              this.data_er.equals(other.getData_er()))) &&
            ((this.data_revoca==null && other.getData_revoca()==null) || 
             (this.data_revoca!=null &&
              this.data_revoca.equals(other.getData_revoca()))) &&
            ((this.esito_er==null && other.getEsito_er()==null) || 
             (this.esito_er!=null &&
              this.esito_er.equals(other.getEsito_er()))) &&
            ((this.esito_rr==null && other.getEsito_rr()==null) || 
             (this.esito_rr!=null &&
              this.esito_rr.equals(other.getEsito_rr()))) &&
            ((this.id_crs==null && other.getId_crs()==null) || 
             (this.id_crs!=null &&
              this.id_crs.equals(other.getId_crs()))) &&
            ((this.id_er==null && other.getId_er()==null) || 
             (this.id_er!=null &&
              this.id_er.equals(other.getId_er()))) &&
            ((this.id_revoca==null && other.getId_revoca()==null) || 
             (this.id_revoca!=null &&
              this.id_revoca.equals(other.getId_revoca()))) &&
            ((this.rt_revocata_data==null && other.getRt_revocata_data()==null) || 
             (this.rt_revocata_data!=null &&
              this.rt_revocata_data.equals(other.getRt_revocata_data()))) &&
            this.rt_revocata_importo == other.getRt_revocata_importo();
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
        if (getData_er() != null) {
            _hashCode += getData_er().hashCode();
        }
        if (getData_revoca() != null) {
            _hashCode += getData_revoca().hashCode();
        }
        if (getEsito_er() != null) {
            _hashCode += getEsito_er().hashCode();
        }
        if (getEsito_rr() != null) {
            _hashCode += getEsito_rr().hashCode();
        }
        if (getId_crs() != null) {
            _hashCode += getId_crs().hashCode();
        }
        if (getId_er() != null) {
            _hashCode += getId_er().hashCode();
        }
        if (getId_revoca() != null) {
            _hashCode += getId_revoca().hashCode();
        }
        if (getRt_revocata_data() != null) {
            _hashCode += getRt_revocata_data().hashCode();
        }
        _hashCode += new Float(getRt_revocata_importo()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PagamentoRevocato.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "pagamentoRevocato"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data_er");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data_er"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data_revoca");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data_revoca"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("esito_er");
        elemField.setXmlName(new javax.xml.namespace.QName("", "esito_er"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("esito_rr");
        elemField.setXmlName(new javax.xml.namespace.QName("", "esito_rr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id_crs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id_crs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id_er");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id_er"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id_revoca");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id_revoca"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rt_revocata_data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rt_revocata_data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rt_revocata_importo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rt_revocata_importo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
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
