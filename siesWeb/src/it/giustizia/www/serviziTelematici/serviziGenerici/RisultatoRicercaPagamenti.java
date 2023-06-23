/**
 * RisultatoRicercaPagamenti.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class RisultatoRicercaPagamenti  implements java.io.Serializable {
    private int count;

    private int dimensionePagina;

    private it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento[] items;

    private int numeroPagina;

    public RisultatoRicercaPagamenti() {
    }

    public RisultatoRicercaPagamenti(
           int count,
           int dimensionePagina,
           it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento[] items,
           int numeroPagina) {
           this.count = count;
           this.dimensionePagina = dimensionePagina;
           this.items = items;
           this.numeroPagina = numeroPagina;
    }


    /**
     * Gets the count value for this RisultatoRicercaPagamenti.
     * 
     * @return count
     */
    public int getCount() {
        return count;
    }


    /**
     * Sets the count value for this RisultatoRicercaPagamenti.
     * 
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }


    /**
     * Gets the dimensionePagina value for this RisultatoRicercaPagamenti.
     * 
     * @return dimensionePagina
     */
    public int getDimensionePagina() {
        return dimensionePagina;
    }


    /**
     * Sets the dimensionePagina value for this RisultatoRicercaPagamenti.
     * 
     * @param dimensionePagina
     */
    public void setDimensionePagina(int dimensionePagina) {
        this.dimensionePagina = dimensionePagina;
    }


    /**
     * Gets the items value for this RisultatoRicercaPagamenti.
     * 
     * @return items
     */
    public it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento[] getItems() {
        return items;
    }


    /**
     * Sets the items value for this RisultatoRicercaPagamenti.
     * 
     * @param items
     */
    public void setItems(it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento[] items) {
        this.items = items;
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento getItems(int i) {
        return this.items[i];
    }

    public void setItems(int i, it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento _value) {
        this.items[i] = _value;
    }


    /**
     * Gets the numeroPagina value for this RisultatoRicercaPagamenti.
     * 
     * @return numeroPagina
     */
    public int getNumeroPagina() {
        return numeroPagina;
    }


    /**
     * Sets the numeroPagina value for this RisultatoRicercaPagamenti.
     * 
     * @param numeroPagina
     */
    public void setNumeroPagina(int numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RisultatoRicercaPagamenti)) return false;
        RisultatoRicercaPagamenti other = (RisultatoRicercaPagamenti) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.count == other.getCount() &&
            this.dimensionePagina == other.getDimensionePagina() &&
            ((this.items==null && other.getItems()==null) || 
             (this.items!=null &&
              java.util.Arrays.equals(this.items, other.getItems()))) &&
            this.numeroPagina == other.getNumeroPagina();
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
        _hashCode += getCount();
        _hashCode += getDimensionePagina();
        if (getItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getNumeroPagina();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RisultatoRicercaPagamenti.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaPagamenti"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("", "count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dimensionePagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dimensionePagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("items");
        elemField.setXmlName(new javax.xml.namespace.QName("", "items"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "statoRichiestaPagamento"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroPagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroPagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
