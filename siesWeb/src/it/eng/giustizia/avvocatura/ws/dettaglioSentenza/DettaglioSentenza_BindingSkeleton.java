/**
 * DettaglioSentenza_BindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.dettaglioSentenza;

public class DettaglioSentenza_BindingSkeleton implements it.eng.giustizia.avvocatura.ws.dettaglioSentenza.DettaglioSentenza_PortType, org.apache.axis.wsdl.Skeleton {
    private it.eng.giustizia.avvocatura.ws.dettaglioSentenza.DettaglioSentenza_PortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiSentenzaInput"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("dettaglioSentenza", _params, new javax.xml.namespace.QName("", "datiSentenzaOutput"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://it/eng/giustizia/avvocatura/ws/dettaglioSentenza/", "DettaglioSentenza"));
        _oper.setSoapAction("http://it/eng/giustizia/avvocatura/ws/dettaglioSentenza/DettaglioSentenza");
        _myOperationsList.add(_oper);
        if (_myOperations.get("dettaglioSentenza") == null) {
            _myOperations.put("dettaglioSentenza", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("dettaglioSentenza")).add(_oper);
    }

    public DettaglioSentenza_BindingSkeleton() {
        this.impl = new it.eng.giustizia.avvocatura.ws.dettaglioSentenza.DettaglioSentenza_BindingImpl();
    }

    public DettaglioSentenza_BindingSkeleton(it.eng.giustizia.avvocatura.ws.dettaglioSentenza.DettaglioSentenza_PortType impl) {
        this.impl = impl;
    }
    public java.lang.String dettaglioSentenza(java.lang.String datiSentenzaInput) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.dettaglioSentenza(datiSentenzaInput);
        return ret;
    }

}
