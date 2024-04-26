/**
 * DatiRiscossione.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class DatiRiscossione  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String codCausale;

    private java.lang.String descCausale;

    private java.lang.String codiceUtilizzatore;

    private java.lang.String codiceGeneratore;

    private java.lang.String codiceVersamento;

    private java.lang.String tassonomia;

    private java.lang.Boolean distrettoUfficio;

    private java.lang.Boolean visibile;

    private boolean beneficiarioSettore;

    public DatiRiscossione() {
    }

    public DatiRiscossione(
           java.lang.String id,
           java.lang.String codCausale,
           java.lang.String descCausale,
           java.lang.String codiceUtilizzatore,
           java.lang.String codiceGeneratore,
           java.lang.String codiceVersamento,
           java.lang.String tassonomia,
           java.lang.Boolean distrettoUfficio,
           java.lang.Boolean visibile,
           boolean beneficiarioSettore) {
        this.id = id;
        this.codCausale = codCausale;
        this.descCausale = descCausale;
        this.codiceUtilizzatore = codiceUtilizzatore;
        this.codiceGeneratore = codiceGeneratore;
        this.codiceVersamento = codiceVersamento;
        this.tassonomia = tassonomia;
        this.distrettoUfficio = distrettoUfficio;
        this.visibile = visibile;
        this.beneficiarioSettore = beneficiarioSettore;
    }


    /**
     * Gets the id value for this DatiRiscossione.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this DatiRiscossione.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the codCausale value for this DatiRiscossione.
     * 
     * @return codCausale
     */
    public java.lang.String getCodCausale() {
        return codCausale;
    }


    /**
     * Sets the codCausale value for this DatiRiscossione.
     * 
     * @param codCausale
     */
    public void setCodCausale(java.lang.String codCausale) {
        this.codCausale = codCausale;
    }


    /**
     * Gets the descCausale value for this DatiRiscossione.
     * 
     * @return descCausale
     */
    public java.lang.String getDescCausale() {
        return descCausale;
    }


    /**
     * Sets the descCausale value for this DatiRiscossione.
     * 
     * @param descCausale
     */
    public void setDescCausale(java.lang.String descCausale) {
        this.descCausale = descCausale;
    }


    /**
     * Gets the codiceUtilizzatore value for this DatiRiscossione.
     * 
     * @return codiceUtilizzatore
     */
    public java.lang.String getCodiceUtilizzatore() {
        return codiceUtilizzatore;
    }


    /**
     * Sets the codiceUtilizzatore value for this DatiRiscossione.
     * 
     * @param codiceUtilizzatore
     */
    public void setCodiceUtilizzatore(java.lang.String codiceUtilizzatore) {
        this.codiceUtilizzatore = codiceUtilizzatore;
    }


    /**
     * Gets the codiceGeneratore value for this DatiRiscossione.
     * 
     * @return codiceGeneratore
     */
    public java.lang.String getCodiceGeneratore() {
        return codiceGeneratore;
    }


    /**
     * Sets the codiceGeneratore value for this DatiRiscossione.
     * 
     * @param codiceGeneratore
     */
    public void setCodiceGeneratore(java.lang.String codiceGeneratore) {
        this.codiceGeneratore = codiceGeneratore;
    }


    /**
     * Gets the codiceVersamento value for this DatiRiscossione.
     * 
     * @return codiceVersamento
     */
    public java.lang.String getCodiceVersamento() {
        return codiceVersamento;
    }


    /**
     * Sets the codiceVersamento value for this DatiRiscossione.
     * 
     * @param codiceVersamento
     */
    public void setCodiceVersamento(java.lang.String codiceVersamento) {
        this.codiceVersamento = codiceVersamento;
    }


    /**
     * Gets the tassonomia value for this DatiRiscossione.
     * 
     * @return tassonomia
     */
    public java.lang.String getTassonomia() {
        return tassonomia;
    }


    /**
     * Sets the tassonomia value for this DatiRiscossione.
     * 
     * @param tassonomia
     */
    public void setTassonomia(java.lang.String tassonomia) {
        this.tassonomia = tassonomia;
    }


    /**
     * Gets the distrettoUfficio value for this DatiRiscossione.
     * 
     * @return distrettoUfficio
     */
    public java.lang.Boolean getDistrettoUfficio() {
        return distrettoUfficio;
    }


    /**
     * Sets the distrettoUfficio value for this DatiRiscossione.
     * 
     * @param distrettoUfficio
     */
    public void setDistrettoUfficio(java.lang.Boolean distrettoUfficio) {
        this.distrettoUfficio = distrettoUfficio;
    }


    /**
     * Gets the visibile value for this DatiRiscossione.
     * 
     * @return visibile
     */
    public java.lang.Boolean getVisibile() {
        return visibile;
    }


    /**
     * Sets the visibile value for this DatiRiscossione.
     * 
     * @param visibile
     */
    public void setVisibile(java.lang.Boolean visibile) {
        this.visibile = visibile;
    }


    /**
     * Gets the beneficiarioSettore value for this DatiRiscossione.
     * 
     * @return beneficiarioSettore
     */
    public boolean isBeneficiarioSettore() {
        return beneficiarioSettore;
    }


    /**
     * Sets the beneficiarioSettore value for this DatiRiscossione.
     * 
     * @param beneficiarioSettore
     */
    public void setBeneficiarioSettore(boolean beneficiarioSettore) {
        this.beneficiarioSettore = beneficiarioSettore;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatiRiscossione)) return false;
        DatiRiscossione other = (DatiRiscossione) obj;
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
            ((this.codCausale==null && other.getCodCausale()==null) || 
             (this.codCausale!=null &&
              this.codCausale.equals(other.getCodCausale()))) &&
            ((this.descCausale==null && other.getDescCausale()==null) || 
             (this.descCausale!=null &&
              this.descCausale.equals(other.getDescCausale()))) &&
            ((this.codiceUtilizzatore==null && other.getCodiceUtilizzatore()==null) || 
             (this.codiceUtilizzatore!=null &&
              this.codiceUtilizzatore.equals(other.getCodiceUtilizzatore()))) &&
            ((this.codiceGeneratore==null && other.getCodiceGeneratore()==null) || 
             (this.codiceGeneratore!=null &&
              this.codiceGeneratore.equals(other.getCodiceGeneratore()))) &&
            ((this.codiceVersamento==null && other.getCodiceVersamento()==null) || 
             (this.codiceVersamento!=null &&
              this.codiceVersamento.equals(other.getCodiceVersamento()))) &&
            ((this.tassonomia==null && other.getTassonomia()==null) || 
             (this.tassonomia!=null &&
              this.tassonomia.equals(other.getTassonomia()))) &&
            ((this.distrettoUfficio==null && other.getDistrettoUfficio()==null) || 
             (this.distrettoUfficio!=null &&
              this.distrettoUfficio.equals(other.getDistrettoUfficio()))) &&
            ((this.visibile==null && other.getVisibile()==null) || 
             (this.visibile!=null &&
              this.visibile.equals(other.getVisibile()))) &&
            this.beneficiarioSettore == other.isBeneficiarioSettore();
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
        if (getCodCausale() != null) {
            _hashCode += getCodCausale().hashCode();
        }
        if (getDescCausale() != null) {
            _hashCode += getDescCausale().hashCode();
        }
        if (getCodiceUtilizzatore() != null) {
            _hashCode += getCodiceUtilizzatore().hashCode();
        }
        if (getCodiceGeneratore() != null) {
            _hashCode += getCodiceGeneratore().hashCode();
        }
        if (getCodiceVersamento() != null) {
            _hashCode += getCodiceVersamento().hashCode();
        }
        if (getTassonomia() != null) {
            _hashCode += getTassonomia().hashCode();
        }
        if (getDistrettoUfficio() != null) {
            _hashCode += getDistrettoUfficio().hashCode();
        }
        if (getVisibile() != null) {
            _hashCode += getVisibile().hashCode();
        }
        _hashCode += (isBeneficiarioSettore() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatiRiscossione.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "datiRiscossione"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codCausale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codCausale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descCausale");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descCausale"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceUtilizzatore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceUtilizzatore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceGeneratore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceGeneratore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codiceVersamento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codiceVersamento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tassonomia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tassonomia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distrettoUfficio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "distrettoUfficio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("visibile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "visibile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beneficiarioSettore");
        elemField.setXmlName(new javax.xml.namespace.QName("", "beneficiarioSettore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
