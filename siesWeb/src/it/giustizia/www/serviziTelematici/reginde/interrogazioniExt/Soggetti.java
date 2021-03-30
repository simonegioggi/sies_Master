/**
 * Soggetti.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniExt;

public class Soggetti  implements java.io.Serializable {
    private java.lang.String codFisc;

    private java.lang.String cognome;

    private java.util.Calendar dataNascita;

    private java.lang.String luogoNascita;

    private java.lang.String nome;

    private java.lang.String pec;

    private java.lang.String provNascita;

    public Soggetti() {
    }

    public Soggetti(
           java.lang.String codFisc,
           java.lang.String cognome,
           java.util.Calendar dataNascita,
           java.lang.String luogoNascita,
           java.lang.String nome,
           java.lang.String pec,
           java.lang.String provNascita) {
           this.codFisc = codFisc;
           this.cognome = cognome;
           this.dataNascita = dataNascita;
           this.luogoNascita = luogoNascita;
           this.nome = nome;
           this.pec = pec;
           this.provNascita = provNascita;
    }


    /**
     * Gets the codFisc value for this Soggetti.
     * 
     * @return codFisc
     */
    public java.lang.String getCodFisc() {
        return codFisc;
    }


    /**
     * Sets the codFisc value for this Soggetti.
     * 
     * @param codFisc
     */
    public void setCodFisc(java.lang.String codFisc) {
        this.codFisc = codFisc;
    }


    /**
     * Gets the cognome value for this Soggetti.
     * 
     * @return cognome
     */
    public java.lang.String getCognome() {
        return cognome;
    }


    /**
     * Sets the cognome value for this Soggetti.
     * 
     * @param cognome
     */
    public void setCognome(java.lang.String cognome) {
        this.cognome = cognome;
    }


    /**
     * Gets the dataNascita value for this Soggetti.
     * 
     * @return dataNascita
     */
    public java.util.Calendar getDataNascita() {
        return dataNascita;
    }


    /**
     * Sets the dataNascita value for this Soggetti.
     * 
     * @param dataNascita
     */
    public void setDataNascita(java.util.Calendar dataNascita) {
        this.dataNascita = dataNascita;
    }


    /**
     * Gets the luogoNascita value for this Soggetti.
     * 
     * @return luogoNascita
     */
    public java.lang.String getLuogoNascita() {
        return luogoNascita;
    }


    /**
     * Sets the luogoNascita value for this Soggetti.
     * 
     * @param luogoNascita
     */
    public void setLuogoNascita(java.lang.String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }


    /**
     * Gets the nome value for this Soggetti.
     * 
     * @return nome
     */
    public java.lang.String getNome() {
        return nome;
    }


    /**
     * Sets the nome value for this Soggetti.
     * 
     * @param nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }


    /**
     * Gets the pec value for this Soggetti.
     * 
     * @return pec
     */
    public java.lang.String getPec() {
        return pec;
    }


    /**
     * Sets the pec value for this Soggetti.
     * 
     * @param pec
     */
    public void setPec(java.lang.String pec) {
        this.pec = pec;
    }


    /**
     * Gets the provNascita value for this Soggetti.
     * 
     * @return provNascita
     */
    public java.lang.String getProvNascita() {
        return provNascita;
    }


    /**
     * Sets the provNascita value for this Soggetti.
     * 
     * @param provNascita
     */
    public void setProvNascita(java.lang.String provNascita) {
        this.provNascita = provNascita;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Soggetti)) return false;
        Soggetti other = (Soggetti) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codFisc==null && other.getCodFisc()==null) || 
             (this.codFisc!=null &&
              this.codFisc.equals(other.getCodFisc()))) &&
            ((this.cognome==null && other.getCognome()==null) || 
             (this.cognome!=null &&
              this.cognome.equals(other.getCognome()))) &&
            ((this.dataNascita==null && other.getDataNascita()==null) || 
             (this.dataNascita!=null &&
              this.dataNascita.equals(other.getDataNascita()))) &&
            ((this.luogoNascita==null && other.getLuogoNascita()==null) || 
             (this.luogoNascita!=null &&
              this.luogoNascita.equals(other.getLuogoNascita()))) &&
            ((this.nome==null && other.getNome()==null) || 
             (this.nome!=null &&
              this.nome.equals(other.getNome()))) &&
            ((this.pec==null && other.getPec()==null) || 
             (this.pec!=null &&
              this.pec.equals(other.getPec()))) &&
            ((this.provNascita==null && other.getProvNascita()==null) || 
             (this.provNascita!=null &&
              this.provNascita.equals(other.getProvNascita())));
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
        if (getCodFisc() != null) {
            _hashCode += getCodFisc().hashCode();
        }
        if (getCognome() != null) {
            _hashCode += getCognome().hashCode();
        }
        if (getDataNascita() != null) {
            _hashCode += getDataNascita().hashCode();
        }
        if (getLuogoNascita() != null) {
            _hashCode += getLuogoNascita().hashCode();
        }
        if (getNome() != null) {
            _hashCode += getNome().hashCode();
        }
        if (getPec() != null) {
            _hashCode += getPec().hashCode();
        }
        if (getProvNascita() != null) {
            _hashCode += getProvNascita().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Soggetti.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "soggetti"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codFisc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codFisc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cognome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cognome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataNascita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataNascita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("luogoNascita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "luogoNascita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nome");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nome"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("provNascita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "provNascita"));
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
