/**
 * AttiDepositabiliUfficio.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class AttiDepositabiliUfficio  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String codiceUfficio;

    private java.util.Calendar dtAttivazione;

    private java.lang.String nomeFile;

    private byte[] normativa;

    private java.lang.String note;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile tipoAttoDep;

    private java.lang.String ufficioInterno;

    public AttiDepositabiliUfficio() {
    }

    public AttiDepositabiliUfficio(
           java.lang.String id,
           java.lang.String codiceUfficio,
           java.util.Calendar dtAttivazione,
           java.lang.String nomeFile,
           byte[] normativa,
           java.lang.String note,
           it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile tipoAttoDep,
           java.lang.String ufficioInterno) {
        this.id = id;
        this.codiceUfficio = codiceUfficio;
        this.dtAttivazione = dtAttivazione;
        this.nomeFile = nomeFile;
        this.normativa = normativa;
        this.note = note;
        this.tipoAttoDep = tipoAttoDep;
        this.ufficioInterno = ufficioInterno;
    }


    /**
     * Gets the id value for this AttiDepositabiliUfficio.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this AttiDepositabiliUfficio.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the codiceUfficio value for this AttiDepositabiliUfficio.
     * 
     * @return codiceUfficio
     */
    public java.lang.String getCodiceUfficio() {
        return codiceUfficio;
    }


    /**
     * Sets the codiceUfficio value for this AttiDepositabiliUfficio.
     * 
     * @param codiceUfficio
     */
    public void setCodiceUfficio(java.lang.String codiceUfficio) {
        this.codiceUfficio = codiceUfficio;
    }


    /**
     * Gets the dtAttivazione value for this AttiDepositabiliUfficio.
     * 
     * @return dtAttivazione
     */
    public java.util.Calendar getDtAttivazione() {
        return dtAttivazione;
    }


    /**
     * Sets the dtAttivazione value for this AttiDepositabiliUfficio.
     * 
     * @param dtAttivazione
     */
    public void setDtAttivazione(java.util.Calendar dtAttivazione) {
        this.dtAttivazione = dtAttivazione;
    }


    /**
     * Gets the nomeFile value for this AttiDepositabiliUfficio.
     * 
     * @return nomeFile
     */
    public java.lang.String getNomeFile() {
        return nomeFile;
    }


    /**
     * Sets the nomeFile value for this AttiDepositabiliUfficio.
     * 
     * @param nomeFile
     */
    public void setNomeFile(java.lang.String nomeFile) {
        this.nomeFile = nomeFile;
    }


    /**
     * Gets the normativa value for this AttiDepositabiliUfficio.
     * 
     * @return normativa
     */
    public byte[] getNormativa() {
        return normativa;
    }


    /**
     * Sets the normativa value for this AttiDepositabiliUfficio.
     * 
     * @param normativa
     */
    public void setNormativa(byte[] normativa) {
        this.normativa = normativa;
    }


    /**
     * Gets the note value for this AttiDepositabiliUfficio.
     * 
     * @return note
     */
    public java.lang.String getNote() {
        return note;
    }


    /**
     * Sets the note value for this AttiDepositabiliUfficio.
     * 
     * @param note
     */
    public void setNote(java.lang.String note) {
        this.note = note;
    }


    /**
     * Gets the tipoAttoDep value for this AttiDepositabiliUfficio.
     * 
     * @return tipoAttoDep
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile getTipoAttoDep() {
        return tipoAttoDep;
    }


    /**
     * Sets the tipoAttoDep value for this AttiDepositabiliUfficio.
     * 
     * @param tipoAttoDep
     */
    public void setTipoAttoDep(it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile tipoAttoDep) {
        this.tipoAttoDep = tipoAttoDep;
    }


    /**
     * Gets the ufficioInterno value for this AttiDepositabiliUfficio.
     * 
     * @return ufficioInterno
     */
    public java.lang.String getUfficioInterno() {
        return ufficioInterno;
    }


    /**
     * Sets the ufficioInterno value for this AttiDepositabiliUfficio.
     * 
     * @param ufficioInterno
     */
    public void setUfficioInterno(java.lang.String ufficioInterno) {
        this.ufficioInterno = ufficioInterno;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttiDepositabiliUfficio)) return false;
        AttiDepositabiliUfficio other = (AttiDepositabiliUfficio) obj;
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
            ((this.codiceUfficio==null && other.getCodiceUfficio()==null) || 
             (this.codiceUfficio!=null &&
              this.codiceUfficio.equals(other.getCodiceUfficio()))) &&
            ((this.dtAttivazione==null && other.getDtAttivazione()==null) || 
             (this.dtAttivazione!=null &&
              this.dtAttivazione.equals(other.getDtAttivazione()))) &&
            ((this.nomeFile==null && other.getNomeFile()==null) || 
             (this.nomeFile!=null &&
              this.nomeFile.equals(other.getNomeFile()))) &&
            ((this.normativa==null && other.getNormativa()==null) || 
             (this.normativa!=null &&
              java.util.Arrays.equals(this.normativa, other.getNormativa()))) &&
            ((this.note==null && other.getNote()==null) || 
             (this.note!=null &&
              this.note.equals(other.getNote()))) &&
            ((this.tipoAttoDep==null && other.getTipoAttoDep()==null) || 
             (this.tipoAttoDep!=null &&
              this.tipoAttoDep.equals(other.getTipoAttoDep()))) &&
            ((this.ufficioInterno==null && other.getUfficioInterno()==null) || 
             (this.ufficioInterno!=null &&
              this.ufficioInterno.equals(other.getUfficioInterno())));
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
        if (getCodiceUfficio() != null) {
            _hashCode += getCodiceUfficio().hashCode();
        }
        if (getDtAttivazione() != null) {
            _hashCode += getDtAttivazione().hashCode();
        }
        if (getNomeFile() != null) {
            _hashCode += getNomeFile().hashCode();
        }
        if (getNormativa() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNormativa());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNormativa(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNote() != null) {
            _hashCode += getNote().hashCode();
        }
        if (getTipoAttoDep() != null) {
            _hashCode += getTipoAttoDep().hashCode();
        }
        if (getUfficioInterno() != null) {
            _hashCode += getUfficioInterno().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AttiDepositabiliUfficio.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "attiDepositabiliUfficio"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceUfficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dtAttivazione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dtAttivazione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeFile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("normativa");
        elemField.setXmlName(new javax.xml.namespace.QName("", "normativa"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
        elemField.setFieldName("tipoAttoDep");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoAttoDep"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipiAttoDepositabile"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ufficioInterno");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ufficioInterno"));
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
