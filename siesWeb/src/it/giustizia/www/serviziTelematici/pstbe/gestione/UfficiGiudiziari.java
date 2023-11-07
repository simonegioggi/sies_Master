/**
 * UfficiGiudiziari.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class UfficiGiudiziari  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private byte[] certificatoCifra;

    private java.lang.String certificatoMimetype;

    private java.lang.String codiceUfficioMaster;

    private java.lang.String comune;

    private java.lang.String descMaster;

    private java.lang.String descrizione;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale gestoreLocale;

    private java.lang.String indirizzoPec;

    private java.lang.String nomeCertificatoCifra;

    private java.lang.String provincia;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni regione;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.Servizi[] servizi;

    private it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio tipoUfficio;

    public UfficiGiudiziari() {
    }

    public UfficiGiudiziari(
           java.lang.String id,
           byte[] certificatoCifra,
           java.lang.String certificatoMimetype,
           java.lang.String codiceUfficioMaster,
           java.lang.String comune,
           java.lang.String descMaster,
           java.lang.String descrizione,
           it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale gestoreLocale,
           java.lang.String indirizzoPec,
           java.lang.String nomeCertificatoCifra,
           java.lang.String provincia,
           it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni regione,
           it.giustizia.www.serviziTelematici.pstbe.gestione.Servizi[] servizi,
           it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio tipoUfficio) {
        this.id = id;
        this.certificatoCifra = certificatoCifra;
        this.certificatoMimetype = certificatoMimetype;
        this.codiceUfficioMaster = codiceUfficioMaster;
        this.comune = comune;
        this.descMaster = descMaster;
        this.descrizione = descrizione;
        this.gestoreLocale = gestoreLocale;
        this.indirizzoPec = indirizzoPec;
        this.nomeCertificatoCifra = nomeCertificatoCifra;
        this.provincia = provincia;
        this.regione = regione;
        this.servizi = servizi;
        this.tipoUfficio = tipoUfficio;
    }


    /**
     * Gets the id value for this UfficiGiudiziari.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this UfficiGiudiziari.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the certificatoCifra value for this UfficiGiudiziari.
     * 
     * @return certificatoCifra
     */
    public byte[] getCertificatoCifra() {
        return certificatoCifra;
    }


    /**
     * Sets the certificatoCifra value for this UfficiGiudiziari.
     * 
     * @param certificatoCifra
     */
    public void setCertificatoCifra(byte[] certificatoCifra) {
        this.certificatoCifra = certificatoCifra;
    }


    /**
     * Gets the certificatoMimetype value for this UfficiGiudiziari.
     * 
     * @return certificatoMimetype
     */
    public java.lang.String getCertificatoMimetype() {
        return certificatoMimetype;
    }


    /**
     * Sets the certificatoMimetype value for this UfficiGiudiziari.
     * 
     * @param certificatoMimetype
     */
    public void setCertificatoMimetype(java.lang.String certificatoMimetype) {
        this.certificatoMimetype = certificatoMimetype;
    }


    /**
     * Gets the codiceUfficioMaster value for this UfficiGiudiziari.
     * 
     * @return codiceUfficioMaster
     */
    public java.lang.String getCodiceUfficioMaster() {
        return codiceUfficioMaster;
    }


    /**
     * Sets the codiceUfficioMaster value for this UfficiGiudiziari.
     * 
     * @param codiceUfficioMaster
     */
    public void setCodiceUfficioMaster(java.lang.String codiceUfficioMaster) {
        this.codiceUfficioMaster = codiceUfficioMaster;
    }


    /**
     * Gets the comune value for this UfficiGiudiziari.
     * 
     * @return comune
     */
    public java.lang.String getComune() {
        return comune;
    }


    /**
     * Sets the comune value for this UfficiGiudiziari.
     * 
     * @param comune
     */
    public void setComune(java.lang.String comune) {
        this.comune = comune;
    }


    /**
     * Gets the descMaster value for this UfficiGiudiziari.
     * 
     * @return descMaster
     */
    public java.lang.String getDescMaster() {
        return descMaster;
    }


    /**
     * Sets the descMaster value for this UfficiGiudiziari.
     * 
     * @param descMaster
     */
    public void setDescMaster(java.lang.String descMaster) {
        this.descMaster = descMaster;
    }


    /**
     * Gets the descrizione value for this UfficiGiudiziari.
     * 
     * @return descrizione
     */
    public java.lang.String getDescrizione() {
        return descrizione;
    }


    /**
     * Sets the descrizione value for this UfficiGiudiziari.
     * 
     * @param descrizione
     */
    public void setDescrizione(java.lang.String descrizione) {
        this.descrizione = descrizione;
    }


    /**
     * Gets the gestoreLocale value for this UfficiGiudiziari.
     * 
     * @return gestoreLocale
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale getGestoreLocale() {
        return gestoreLocale;
    }


    /**
     * Sets the gestoreLocale value for this UfficiGiudiziari.
     * 
     * @param gestoreLocale
     */
    public void setGestoreLocale(it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale gestoreLocale) {
        this.gestoreLocale = gestoreLocale;
    }


    /**
     * Gets the indirizzoPec value for this UfficiGiudiziari.
     * 
     * @return indirizzoPec
     */
    public java.lang.String getIndirizzoPec() {
        return indirizzoPec;
    }


    /**
     * Sets the indirizzoPec value for this UfficiGiudiziari.
     * 
     * @param indirizzoPec
     */
    public void setIndirizzoPec(java.lang.String indirizzoPec) {
        this.indirizzoPec = indirizzoPec;
    }


    /**
     * Gets the nomeCertificatoCifra value for this UfficiGiudiziari.
     * 
     * @return nomeCertificatoCifra
     */
    public java.lang.String getNomeCertificatoCifra() {
        return nomeCertificatoCifra;
    }


    /**
     * Sets the nomeCertificatoCifra value for this UfficiGiudiziari.
     * 
     * @param nomeCertificatoCifra
     */
    public void setNomeCertificatoCifra(java.lang.String nomeCertificatoCifra) {
        this.nomeCertificatoCifra = nomeCertificatoCifra;
    }


    /**
     * Gets the provincia value for this UfficiGiudiziari.
     * 
     * @return provincia
     */
    public java.lang.String getProvincia() {
        return provincia;
    }


    /**
     * Sets the provincia value for this UfficiGiudiziari.
     * 
     * @param provincia
     */
    public void setProvincia(java.lang.String provincia) {
        this.provincia = provincia;
    }


    /**
     * Gets the regione value for this UfficiGiudiziari.
     * 
     * @return regione
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni getRegione() {
        return regione;
    }


    /**
     * Sets the regione value for this UfficiGiudiziari.
     * 
     * @param regione
     */
    public void setRegione(it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni regione) {
        this.regione = regione;
    }


    /**
     * Gets the servizi value for this UfficiGiudiziari.
     * 
     * @return servizi
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.Servizi[] getServizi() {
        return servizi;
    }


    /**
     * Sets the servizi value for this UfficiGiudiziari.
     * 
     * @param servizi
     */
    public void setServizi(it.giustizia.www.serviziTelematici.pstbe.gestione.Servizi[] servizi) {
        this.servizi = servizi;
    }

    public it.giustizia.www.serviziTelematici.pstbe.gestione.Servizi getServizi(int i) {
        return this.servizi[i];
    }

    public void setServizi(int i, it.giustizia.www.serviziTelematici.pstbe.gestione.Servizi _value) {
        this.servizi[i] = _value;
    }


    /**
     * Gets the tipoUfficio value for this UfficiGiudiziari.
     * 
     * @return tipoUfficio
     */
    public it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio getTipoUfficio() {
        return tipoUfficio;
    }


    /**
     * Sets the tipoUfficio value for this UfficiGiudiziari.
     * 
     * @param tipoUfficio
     */
    public void setTipoUfficio(it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio tipoUfficio) {
        this.tipoUfficio = tipoUfficio;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UfficiGiudiziari)) return false;
        UfficiGiudiziari other = (UfficiGiudiziari) obj;
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
            ((this.codiceUfficioMaster==null && other.getCodiceUfficioMaster()==null) || 
             (this.codiceUfficioMaster!=null &&
              this.codiceUfficioMaster.equals(other.getCodiceUfficioMaster()))) &&
            ((this.comune==null && other.getComune()==null) || 
             (this.comune!=null &&
              this.comune.equals(other.getComune()))) &&
            ((this.descMaster==null && other.getDescMaster()==null) || 
             (this.descMaster!=null &&
              this.descMaster.equals(other.getDescMaster()))) &&
            ((this.descrizione==null && other.getDescrizione()==null) || 
             (this.descrizione!=null &&
              this.descrizione.equals(other.getDescrizione()))) &&
            ((this.gestoreLocale==null && other.getGestoreLocale()==null) || 
             (this.gestoreLocale!=null &&
              this.gestoreLocale.equals(other.getGestoreLocale()))) &&
            ((this.indirizzoPec==null && other.getIndirizzoPec()==null) || 
             (this.indirizzoPec!=null &&
              this.indirizzoPec.equals(other.getIndirizzoPec()))) &&
            ((this.nomeCertificatoCifra==null && other.getNomeCertificatoCifra()==null) || 
             (this.nomeCertificatoCifra!=null &&
              this.nomeCertificatoCifra.equals(other.getNomeCertificatoCifra()))) &&
            ((this.provincia==null && other.getProvincia()==null) || 
             (this.provincia!=null &&
              this.provincia.equals(other.getProvincia()))) &&
            ((this.regione==null && other.getRegione()==null) || 
             (this.regione!=null &&
              this.regione.equals(other.getRegione()))) &&
            ((this.servizi==null && other.getServizi()==null) || 
             (this.servizi!=null &&
              java.util.Arrays.equals(this.servizi, other.getServizi()))) &&
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
        if (getCodiceUfficioMaster() != null) {
            _hashCode += getCodiceUfficioMaster().hashCode();
        }
        if (getComune() != null) {
            _hashCode += getComune().hashCode();
        }
        if (getDescMaster() != null) {
            _hashCode += getDescMaster().hashCode();
        }
        if (getDescrizione() != null) {
            _hashCode += getDescrizione().hashCode();
        }
        if (getGestoreLocale() != null) {
            _hashCode += getGestoreLocale().hashCode();
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
        if (getRegione() != null) {
            _hashCode += getRegione().hashCode();
        }
        if (getServizi() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getServizi());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getServizi(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipoUfficio() != null) {
            _hashCode += getTipoUfficio().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UfficiGiudiziari.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "ufficiGiudiziari"));
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
        elemField.setFieldName("descMaster");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descMaster"));
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
        elemField.setFieldName("gestoreLocale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gestoreLocale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "gestoreLocale"));
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
        elemField.setFieldName("regione");
        elemField.setXmlName(new javax.xml.namespace.QName("", "regione"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "regioni"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("servizi");
        elemField.setXmlName(new javax.xml.namespace.QName("", "servizi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "servizi"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoUfficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipiUfficio"));
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
