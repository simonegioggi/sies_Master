/**
 * ServiziAtti.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class ServiziAtti  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.util.Calendar dataDecreto;

    private byte[] decreto;

    private java.lang.String fase;

    private java.lang.String grado;

    private java.lang.String mimetype;

    private java.lang.String nomeDecreto;

    private java.lang.String note;

    private java.lang.String rito;

    private java.lang.String servizio;

    private java.lang.String qName;

    public ServiziAtti() {
    }

    public ServiziAtti(
           java.lang.String id,
           java.util.Calendar dataDecreto,
           byte[] decreto,
           java.lang.String fase,
           java.lang.String grado,
           java.lang.String mimetype,
           java.lang.String nomeDecreto,
           java.lang.String note,
           java.lang.String rito,
           java.lang.String servizio,
           java.lang.String qName) {
        this.id = id;
        this.dataDecreto = dataDecreto;
        this.decreto = decreto;
        this.fase = fase;
        this.grado = grado;
        this.mimetype = mimetype;
        this.nomeDecreto = nomeDecreto;
        this.note = note;
        this.rito = rito;
        this.servizio = servizio;
        this.qName = qName;
    }


    /**
     * Gets the id value for this ServiziAtti.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this ServiziAtti.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the dataDecreto value for this ServiziAtti.
     * 
     * @return dataDecreto
     */
    public java.util.Calendar getDataDecreto() {
        return dataDecreto;
    }


    /**
     * Sets the dataDecreto value for this ServiziAtti.
     * 
     * @param dataDecreto
     */
    public void setDataDecreto(java.util.Calendar dataDecreto) {
        this.dataDecreto = dataDecreto;
    }


    /**
     * Gets the decreto value for this ServiziAtti.
     * 
     * @return decreto
     */
    public byte[] getDecreto() {
        return decreto;
    }


    /**
     * Sets the decreto value for this ServiziAtti.
     * 
     * @param decreto
     */
    public void setDecreto(byte[] decreto) {
        this.decreto = decreto;
    }


    /**
     * Gets the fase value for this ServiziAtti.
     * 
     * @return fase
     */
    public java.lang.String getFase() {
        return fase;
    }


    /**
     * Sets the fase value for this ServiziAtti.
     * 
     * @param fase
     */
    public void setFase(java.lang.String fase) {
        this.fase = fase;
    }


    /**
     * Gets the grado value for this ServiziAtti.
     * 
     * @return grado
     */
    public java.lang.String getGrado() {
        return grado;
    }


    /**
     * Sets the grado value for this ServiziAtti.
     * 
     * @param grado
     */
    public void setGrado(java.lang.String grado) {
        this.grado = grado;
    }


    /**
     * Gets the mimetype value for this ServiziAtti.
     * 
     * @return mimetype
     */
    public java.lang.String getMimetype() {
        return mimetype;
    }


    /**
     * Sets the mimetype value for this ServiziAtti.
     * 
     * @param mimetype
     */
    public void setMimetype(java.lang.String mimetype) {
        this.mimetype = mimetype;
    }


    /**
     * Gets the nomeDecreto value for this ServiziAtti.
     * 
     * @return nomeDecreto
     */
    public java.lang.String getNomeDecreto() {
        return nomeDecreto;
    }


    /**
     * Sets the nomeDecreto value for this ServiziAtti.
     * 
     * @param nomeDecreto
     */
    public void setNomeDecreto(java.lang.String nomeDecreto) {
        this.nomeDecreto = nomeDecreto;
    }


    /**
     * Gets the note value for this ServiziAtti.
     * 
     * @return note
     */
    public java.lang.String getNote() {
        return note;
    }


    /**
     * Sets the note value for this ServiziAtti.
     * 
     * @param note
     */
    public void setNote(java.lang.String note) {
        this.note = note;
    }


    /**
     * Gets the rito value for this ServiziAtti.
     * 
     * @return rito
     */
    public java.lang.String getRito() {
        return rito;
    }


    /**
     * Sets the rito value for this ServiziAtti.
     * 
     * @param rito
     */
    public void setRito(java.lang.String rito) {
        this.rito = rito;
    }


    /**
     * Gets the servizio value for this ServiziAtti.
     * 
     * @return servizio
     */
    public java.lang.String getServizio() {
        return servizio;
    }


    /**
     * Sets the servizio value for this ServiziAtti.
     * 
     * @param servizio
     */
    public void setServizio(java.lang.String servizio) {
        this.servizio = servizio;
    }


    /**
     * Gets the qName value for this ServiziAtti.
     * 
     * @return qName
     */
    public java.lang.String getQName() {
        return qName;
    }


    /**
     * Sets the qName value for this ServiziAtti.
     * 
     * @param qName
     */
    public void setQName(java.lang.String qName) {
        this.qName = qName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiziAtti)) return false;
        ServiziAtti other = (ServiziAtti) obj;
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
            ((this.dataDecreto==null && other.getDataDecreto()==null) || 
             (this.dataDecreto!=null &&
              this.dataDecreto.equals(other.getDataDecreto()))) &&
            ((this.decreto==null && other.getDecreto()==null) || 
             (this.decreto!=null &&
              java.util.Arrays.equals(this.decreto, other.getDecreto()))) &&
            ((this.fase==null && other.getFase()==null) || 
             (this.fase!=null &&
              this.fase.equals(other.getFase()))) &&
            ((this.grado==null && other.getGrado()==null) || 
             (this.grado!=null &&
              this.grado.equals(other.getGrado()))) &&
            ((this.mimetype==null && other.getMimetype()==null) || 
             (this.mimetype!=null &&
              this.mimetype.equals(other.getMimetype()))) &&
            ((this.nomeDecreto==null && other.getNomeDecreto()==null) || 
             (this.nomeDecreto!=null &&
              this.nomeDecreto.equals(other.getNomeDecreto()))) &&
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              this.note.equals(other.getNote()))) &&
            ((this.rito==null && other.getRito()==null) || 
             (this.rito!=null &&
              this.rito.equals(other.getRito()))) &&
            ((this.servizio==null && other.getServizio()==null) || 
             (this.servizio!=null &&
              this.servizio.equals(other.getServizio()))) &&
            ((this.qName==null && other.getQName()==null) || 
             (this.qName!=null &&
              this.qName.equals(other.getQName())));
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
        if (getDataDecreto() != null) {
            _hashCode += getDataDecreto().hashCode();
        }
        if (getDecreto() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDecreto());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDecreto(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFase() != null) {
            _hashCode += getFase().hashCode();
        }
        if (getGrado() != null) {
            _hashCode += getGrado().hashCode();
        }
        if (getMimetype() != null) {
            _hashCode += getMimetype().hashCode();
        }
        if (getNomeDecreto() != null) {
            _hashCode += getNomeDecreto().hashCode();
        }
        if (getNote() != null) {
            _hashCode += getNote().hashCode();
        }
        if (getRito() != null) {
            _hashCode += getRito().hashCode();
        }
        if (getServizio() != null) {
            _hashCode += getServizio().hashCode();
        }
        if (getQName() != null) {
            _hashCode += getQName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiziAtti.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "serviziAtti"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDecreto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataDecreto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("decreto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "decreto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fase");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fase"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "grado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mimetype");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mimetype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeDecreto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeDecreto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("note");
        elemField.setXmlName(new javax.xml.namespace.QName("", "note"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rito");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servizio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "servizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "qName"));
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
