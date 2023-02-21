/**
 * Uffici.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class Uffici  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private byte[] certificatoCifra;

    private java.lang.String certificatoMimetype;

    private java.lang.String codiceGL;

    private int codiceRegione;

    private java.lang.String codiceUfficioMaster;

    private java.lang.String comune;

    private java.lang.String descrizione;

    private java.lang.String indirizzoPec;

    private java.lang.String nomeCertificatoCifra;

    private java.lang.String provincia;

    private java.lang.String tipoUfficio;

    public Uffici() {
    }

    public Uffici(
           java.lang.String id,
           byte[] certificatoCifra,
           java.lang.String certificatoMimetype,
           java.lang.String codiceGL,
           int codiceRegione,
           java.lang.String codiceUfficioMaster,
           java.lang.String comune,
           java.lang.String descrizione,
           java.lang.String indirizzoPec,
           java.lang.String nomeCertificatoCifra,
           java.lang.String provincia,
           java.lang.String tipoUfficio) {
        this.id = id;
        this.certificatoCifra = certificatoCifra;
        this.certificatoMimetype = certificatoMimetype;
        this.codiceGL = codiceGL;
        this.codiceRegione = codiceRegione;
        this.codiceUfficioMaster = codiceUfficioMaster;
        this.comune = comune;
        this.descrizione = descrizione;
        this.indirizzoPec = indirizzoPec;
        this.nomeCertificatoCifra = nomeCertificatoCifra;
        this.provincia = provincia;
        this.tipoUfficio = tipoUfficio;
    }


    /**
     * Gets the id value for this Uffici.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this Uffici.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the certificatoCifra value for this Uffici.
     * 
     * @return certificatoCifra
     */
    public byte[] getCertificatoCifra() {
        return certificatoCifra;
    }


    /**
     * Sets the certificatoCifra value for this Uffici.
     * 
     * @param certificatoCifra
     */
    public void setCertificatoCifra(byte[] certificatoCifra) {
        this.certificatoCifra = certificatoCifra;
    }


    /**
     * Gets the certificatoMimetype value for this Uffici.
     * 
     * @return certificatoMimetype
     */
    public java.lang.String getCertificatoMimetype() {
        return certificatoMimetype;
    }


    /**
     * Sets the certificatoMimetype value for this Uffici.
     * 
     * @param certificatoMimetype
     */
    public void setCertificatoMimetype(java.lang.String certificatoMimetype) {
        this.certificatoMimetype = certificatoMimetype;
    }


    /**
     * Gets the codiceGL value for this Uffici.
     * 
     * @return codiceGL
     */
    public java.lang.String getCodiceGL() {
        return codiceGL;
    }


    /**
     * Sets the codiceGL value for this Uffici.
     * 
     * @param codiceGL
     */
    public void setCodiceGL(java.lang.String codiceGL) {
        this.codiceGL = codiceGL;
    }


    /**
     * Gets the codiceRegione value for this Uffici.
     * 
     * @return codiceRegione
     */
    public int getCodiceRegione() {
        return codiceRegione;
    }


    /**
     * Sets the codiceRegione value for this Uffici.
     * 
     * @param codiceRegione
     */
    public void setCodiceRegione(int codiceRegione) {
        this.codiceRegione = codiceRegione;
    }


    /**
     * Gets the codiceUfficioMaster value for this Uffici.
     * 
     * @return codiceUfficioMaster
     */
    public java.lang.String getCodiceUfficioMaster() {
        return codiceUfficioMaster;
    }


    /**
     * Sets the codiceUfficioMaster value for this Uffici.
     * 
     * @param codiceUfficioMaster
     */
    public void setCodiceUfficioMaster(java.lang.String codiceUfficioMaster) {
        this.codiceUfficioMaster = codiceUfficioMaster;
    }


    /**
     * Gets the comune value for this Uffici.
     * 
     * @return comune
     */
    public java.lang.String getComune() {
        return comune;
    }


    /**
     * Sets the comune value for this Uffici.
     * 
     * @param comune
     */
    public void setComune(java.lang.String comune) {
        this.comune = comune;
    }


    /**
     * Gets the descrizione value for this Uffici.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this Uffici.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the indirizzoPec value for this Uffici.
     * 
     * @return indirizzoPec
     */
    public java.lang.String getIndirizzoPec() {
        return indirizzoPec;
    }


    /**
     * Sets the indirizzoPec value for this Uffici.
     * 
     * @param indirizzoPec
     */
    public void setIndirizzoPec(java.lang.String indirizzoPec) {
        this.indirizzoPec = indirizzoPec;
    }


    /**
     * Gets the nomeCertificatoCifra value for this Uffici.
     * 
     * @return nomeCertificatoCifra
     */
    public java.lang.String getNomeCertificatoCifra() {
        return nomeCertificatoCifra;
    }


    /**
     * Sets the nomeCertificatoCifra value for this Uffici.
     * 
     * @param nomeCertificatoCifra
     */
    public void setNomeCertificatoCifra(java.lang.String nomeCertificatoCifra) {
        this.nomeCertificatoCifra = nomeCertificatoCifra;
    }


    /**
     * Gets the provincia value for this Uffici.
     * 
     * @return provincia
     */
    public java.lang.String getProvincia() {
        return provincia;
    }


    /**
     * Sets the provincia value for this Uffici.
     * 
     * @param provincia
     */
    public void setProvincia(java.lang.String provincia) {
        this.provincia = provincia;
    }


    /**
     * Gets the tipoUfficio value for this Uffici.
     * 
     * @return tipoUfficio
     */
    public java.lang.String getTipoUfficio() {
        return tipoUfficio;
    }


    /**
     * Sets the tipoUfficio value for this Uffici.
     * 
     * @param tipoUfficio
     */
    public void setTipoUfficio(java.lang.String tipoUfficio) {
        this.tipoUfficio = tipoUfficio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Uffici)) return false;
        Uffici other = (Uffici) obj;
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
            ((this.certificatoCifra==null && other.getCertificatoCifra()==null) || 
             (this.certificatoCifra!=null &&
              java.util.Arrays.equals(this.certificatoCifra, other.getCertificatoCifra()))) &&
            ((this.certificatoMimetype==null && other.getCertificatoMimetype()==null) || 
             (this.certificatoMimetype!=null &&
              this.certificatoMimetype.equals(other.getCertificatoMimetype()))) &&
            ((this.codiceGL==null && other.getCodiceGL()==null) || 
             (this.codiceGL!=null &&
              this.codiceGL.equals(other.getCodiceGL()))) &&
            this.codiceRegione == other.getCodiceRegione() &&
            ((this.codiceUfficioMaster==null && other.getCodiceUfficioMaster()==null) || 
             (this.codiceUfficioMaster!=null &&
              this.codiceUfficioMaster.equals(other.getCodiceUfficioMaster()))) &&
            ((this.comune==null && other.getComune()==null) || 
             (this.comune!=null &&
              this.comune.equals(other.getComune()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.indirizzoPec==null && other.getIndirizzoPec()==null) || 
             (this.indirizzoPec!=null &&
              this.indirizzoPec.equals(other.getIndirizzoPec()))) &&
            ((this.nomeCertificatoCifra==null && other.getNomeCertificatoCifra()==null) || 
             (this.nomeCertificatoCifra!=null &&
              this.nomeCertificatoCifra.equals(other.getNomeCertificatoCifra()))) &&
            ((this.provincia==null && other.getProvincia()==null) || 
             (this.provincia!=null &&
              this.provincia.equals(other.getProvincia()))) &&
            ((this.tipoUfficio==null && other.getTipoUfficio()==null) || 
             (this.tipoUfficio!=null &&
              this.tipoUfficio.equals(other.getTipoUfficio())));
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
        if (getCertificatoCifra() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCertificatoCifra());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCertificatoCifra(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCertificatoMimetype() != null) {
            _hashCode += getCertificatoMimetype().hashCode();
        }
        if (getCodiceGL() != null) {
            _hashCode += getCodiceGL().hashCode();
        }
        _hashCode += getCodiceRegione();
        if (getCodiceUfficioMaster() != null) {
            _hashCode += getCodiceUfficioMaster().hashCode();
        }
        if (getComune() != null) {
            _hashCode += getComune().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getIndirizzoPec() != null) {
            _hashCode += getIndirizzoPec().hashCode();
        }
        if (getNomeCertificatoCifra() != null) {
            _hashCode += getNomeCertificatoCifra().hashCode();
        }
        if (getProvincia() != null) {
            _hashCode += getProvincia().hashCode();
        }
        if (getTipoUfficio() != null) {
            _hashCode += getTipoUfficio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Uffici.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "uffici"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificatoCifra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificatoCifra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificatoMimetype");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificatoMimetype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceGL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceGL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceRegione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceRegione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceUfficioMaster");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceUfficioMaster"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comune");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comune"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("indirizzoPec");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzoPec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeCertificatoCifra");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomeCertificatoCifra"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provincia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "provincia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoUfficio"));
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
