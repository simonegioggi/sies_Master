/**
 * DefinizioneTemplate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.pstbe.gestione;

public class DefinizioneTemplate  extends it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String pspCode;

    private java.lang.String templateContent;

    private java.lang.String templateName;

    public DefinizioneTemplate() {
    }

    public DefinizioneTemplate(
           java.lang.String id,
           java.lang.String pspCode,
           java.lang.String templateContent,
           java.lang.String templateName) {
        this.id = id;
        this.pspCode = pspCode;
        this.templateContent = templateContent;
        this.templateName = templateName;
    }


    /**
     * Gets the id value for this DefinizioneTemplate.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this DefinizioneTemplate.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the pspCode value for this DefinizioneTemplate.
     * 
     * @return pspCode
     */
    public java.lang.String getPspCode() {
        return pspCode;
    }


    /**
     * Sets the pspCode value for this DefinizioneTemplate.
     * 
     * @param pspCode
     */
    public void setPspCode(java.lang.String pspCode) {
        this.pspCode = pspCode;
    }


    /**
     * Gets the templateContent value for this DefinizioneTemplate.
     * 
     * @return templateContent
     */
    public java.lang.String getTemplateContent() {
        return templateContent;
    }


    /**
     * Sets the templateContent value for this DefinizioneTemplate.
     * 
     * @param templateContent
     */
    public void setTemplateContent(java.lang.String templateContent) {
        this.templateContent = templateContent;
    }


    /**
     * Gets the templateName value for this DefinizioneTemplate.
     * 
     * @return templateName
     */
    public java.lang.String getTemplateName() {
        return templateName;
    }


    /**
     * Sets the templateName value for this DefinizioneTemplate.
     * 
     * @param templateName
     */
    public void setTemplateName(java.lang.String templateName) {
        this.templateName = templateName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DefinizioneTemplate)) return false;
        DefinizioneTemplate other = (DefinizioneTemplate) obj;
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
            ((this.pspCode==null && other.getPspCode()==null) || 
             (this.pspCode!=null &&
              this.pspCode.equals(other.getPspCode()))) &&
            ((this.templateContent==null && other.getTemplateContent()==null) || 
             (this.templateContent!=null &&
              this.templateContent.equals(other.getTemplateContent()))) &&
            ((this.templateName==null && other.getTemplateName()==null) || 
             (this.templateName!=null &&
              this.templateName.equals(other.getTemplateName())));
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
        if (getPspCode() != null) {
            _hashCode += getPspCode().hashCode();
        }
        if (getTemplateContent() != null) {
            _hashCode += getTemplateContent().hashCode();
        }
        if (getTemplateName() != null) {
            _hashCode += getTemplateName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DefinizioneTemplate.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "definizioneTemplate"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pspCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pspCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("templateContent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "templateContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("templateName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "templateName"));
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
