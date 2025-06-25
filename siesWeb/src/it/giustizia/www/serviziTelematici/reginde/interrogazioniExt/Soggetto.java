/**
 * Soggetto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniExt;

public class Soggetto  implements java.io.Serializable {
    private it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ruoliente;

    private it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo[] indirizzi;

    private it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetti soggetto;

    public Soggetto() {
    }

    public Soggetto(
           it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ruoliente,
           it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo[] indirizzi,
           it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetti soggetto) {
           this.ruoliente = ruoliente;
           this.indirizzi = indirizzi;
           this.soggetto = soggetto;
    }


    /**
     * Gets the ruoliente value for this Soggetto.
     * 
     * @return ruoliente
     */
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] getRuoliente() {
        return ruoliente;
    }


    /**
     * Sets the ruoliente value for this Soggetto.
     * 
     * @param ruoliente
     */
    public void setRuoliente(it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ruoliente) {
        this.ruoliente = ruoliente;
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente getRuoliente(int i) {
        return this.ruoliente[i];
    }

    public void setRuoliente(int i, it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente _value) {
        this.ruoliente[i] = _value;
    }


    /**
     * Gets the indirizzi value for this Soggetto.
     * 
     * @return indirizzi
     */
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo[] getIndirizzi() {
        return indirizzi;
    }


    /**
     * Sets the indirizzi value for this Soggetto.
     * 
     * @param indirizzi
     */
    public void setIndirizzi(it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo[] indirizzi) {
        this.indirizzi = indirizzi;
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo getIndirizzi(int i) {
        return this.indirizzi[i];
    }

    public void setIndirizzi(int i, it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo _value) {
        this.indirizzi[i] = _value;
    }


    /**
     * Gets the soggetto value for this Soggetto.
     * 
     * @return soggetto
     */
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetti getSoggetto() {
        return soggetto;
    }


    /**
     * Sets the soggetto value for this Soggetto.
     * 
     * @param soggetto
     */
    public void setSoggetto(it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetti soggetto) {
        this.soggetto = soggetto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Soggetto)) return false;
        Soggetto other = (Soggetto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ruoliente==null && other.getRuoliente()==null) || 
             (this.ruoliente!=null &&
              java.util.Arrays.equals(this.ruoliente, other.getRuoliente()))) &&
            ((this.indirizzi==null && other.getIndirizzi()==null) || 
             (this.indirizzi!=null &&
              java.util.Arrays.equals(this.indirizzi, other.getIndirizzi()))) &&
            ((this.soggetto==null && other.getSoggetto()==null) || 
             (this.soggetto!=null &&
              this.soggetto.equals(other.getSoggetto())));
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
        if (getRuoliente() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRuoliente());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRuoliente(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIndirizzi() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIndirizzi());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIndirizzi(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSoggetto() != null) {
            _hashCode += getSoggetto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Soggetto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "soggetto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ruoliente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ruoliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "ruoloente"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indirizzi");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indirizzi"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "indirizzo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("soggetto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "soggetto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "soggetti"));
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
