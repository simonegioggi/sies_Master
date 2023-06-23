/**
 * Servizi.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class Servizi  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.util.Calendar dataAttivazione;

    private java.lang.String descrizione;

    private java.lang.String note;

    private it.giustizia.www.serviziTelematici.serviziGenerici.Registro[] registri;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.ServiziAtti[] serviziAtti;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.DefinizioneServizio tipoServizio;

    private java.lang.String url;

    public Servizi() {
    }

    public Servizi(
           java.lang.String id,
           java.util.Calendar dataAttivazione,
           java.lang.String descrizione,
           java.lang.String note,
           it.giustizia.www.serviziTelematici.serviziGenerici.Registro[] registri,
           it.giustizia.www.serviziTelematici.pstbe.gestione.ServiziAtti[] serviziAtti,
           it.giustizia.www.serviziTelematici.pstbe.gestione.DefinizioneServizio tipoServizio,
           java.lang.String url) {
        this.id = id;
        this.dataAttivazione = dataAttivazione;
        this.descrizione = descrizione;
        this.note = note;
        this.registri = registri;
        this.serviziAtti = serviziAtti;
        this.tipoServizio = tipoServizio;
        this.url = url;
    }


    /**
     * Gets the id value for this Servizi.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Servizi.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the dataAttivazione value for this Servizi.
     * 
     * @return dataAttivazione
     */
    public java.util.Calendar getDataAttivazione() {
        return dataAttivazione;
    }


    /**
     * Sets the dataAttivazione value for this Servizi.
     * 
     * @param dataAttivazione
     */
    public void setDataAttivazione(java.util.Calendar dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
    }


    /**
     * Gets the descrizione value for this Servizi.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this Servizi.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the note value for this Servizi.
     * 
     * @return note
     */
    public java.lang.String getNote() {
        return note;
    }


    /**
     * Sets the note value for this Servizi.
     * 
     * @param note
     */
    public void setNote(java.lang.String note) {
        this.note = note;
    }


    /**
     * Gets the registri value for this Servizi.
     * 
     * @return registri
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.Registro[] getRegistri() {
        return registri;
    }


    /**
     * Sets the registri value for this Servizi.
     * 
     * @param registri
     */
    public void setRegistri(it.giustizia.www.serviziTelematici.serviziGenerici.Registro[] registri) {
        this.registri = registri;
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.Registro getRegistri(int i) {
        return this.registri[i];
    }

    public void setRegistri(int i, it.giustizia.www.serviziTelematici.serviziGenerici.Registro _value) {
        this.registri[i] = _value;
    }


    /**
     * Gets the serviziAtti value for this Servizi.
     * 
     * @return serviziAtti
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.ServiziAtti[] getServiziAtti() {
        return serviziAtti;
    }


    /**
     * Sets the serviziAtti value for this Servizi.
     * 
     * @param serviziAtti
     */
    public void setServiziAtti(it.giustizia.www.serviziTelematici.pstbe.gestione.ServiziAtti[] serviziAtti) {
        this.serviziAtti = serviziAtti;
    }

    public it.giustizia.www.serviziTelematici.pstbe.gestione.ServiziAtti getServiziAtti(int i) {
        return this.serviziAtti[i];
    }

    public void setServiziAtti(int i, it.giustizia.www.serviziTelematici.pstbe.gestione.ServiziAtti _value) {
        this.serviziAtti[i] = _value;
    }


    /**
     * Gets the tipoServizio value for this Servizi.
     * 
     * @return tipoServizio
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.DefinizioneServizio getTipoServizio() {
        return tipoServizio;
    }


    /**
     * Sets the tipoServizio value for this Servizi.
     * 
     * @param tipoServizio
     */
    public void setTipoServizio(it.giustizia.www.serviziTelematici.pstbe.gestione.DefinizioneServizio tipoServizio) {
        this.tipoServizio = tipoServizio;
    }


    /**
     * Gets the url value for this Servizi.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this Servizi.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Servizi)) return false;
        Servizi other = (Servizi) obj;
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
            ((this.dataAttivazione==null && other.getDataAttivazione()==null) || 
             (this.dataAttivazione!=null &&
              this.dataAttivazione.equals(other.getDataAttivazione()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              this.note.equals(other.getNote()))) &&
            ((this.registri==null && other.getRegistri()==null) || 
             (this.registri!=null &&
              java.util.Arrays.equals(this.registri, other.getRegistri()))) &&
            ((this.serviziAtti==null && other.getServiziAtti()==null) || 
             (this.serviziAtti!=null &&
              java.util.Arrays.equals(this.serviziAtti, other.getServiziAtti()))) &&
            ((this.tipoServizio==null && other.getTipoServizio()==null) || 
             (this.tipoServizio!=null &&
              this.tipoServizio.equals(other.getTipoServizio()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl())));
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
        if (getDataAttivazione() != null) {
            _hashCode += getDataAttivazione().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getNote() != null) {
            _hashCode += getNote().hashCode();
        }
        if (getRegistri() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRegistri());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRegistri(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getServiziAtti() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getServiziAtti());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getServiziAtti(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipoServizio() != null) {
            _hashCode += getTipoServizio().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Servizi.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "servizi"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataAttivazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataAttivazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descrizione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descrizione"));
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
        elemField.setFieldName("registri");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registri"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "registro"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviziAtti");
        elemField.setXmlName(new javax.xml.namespace.QName("", "serviziAtti"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "serviziAtti"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoServizio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoServizio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "definizioneServizio"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
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
