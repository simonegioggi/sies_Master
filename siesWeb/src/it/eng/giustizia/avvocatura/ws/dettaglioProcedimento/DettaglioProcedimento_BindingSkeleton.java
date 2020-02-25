/**
 * DettaglioProcedimento_BindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.dettaglioProcedimento;

public class DettaglioProcedimento_BindingSkeleton implements it.eng.giustizia.avvocatura.ws.dettaglioProcedimento.DettaglioProcedimento_PortType, org.apache.axis.wsdl.Skeleton {
    private it.eng.giustizia.avvocatura.ws.dettaglioProcedimento.DettaglioProcedimento_PortType impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiProcedimentoInput"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("dettaglioProcedimento", _params, new javax.xml.namespace.QName("", "datiProcedimentoOutput"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://it/eng/giustizia/avvocatura/ws/dettaglioProcedimento/", "DettaglioProcedimento"));
        _oper.setSoapAction("http://it/eng/giustizia/avvocatura/ws/dettaglioProcedimento/DettaglioProcedimento");
        _myOperationsList.add(_oper);
        if (_myOperations.get("dettaglioProcedimento") == null) {
            _myOperations.put("dettaglioProcedimento", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("dettaglioProcedimento")).add(_oper);
    }

    public DettaglioProcedimento_BindingSkeleton() {
        this.impl = new it.eng.giustizia.avvocatura.ws.dettaglioProcedimento.DettaglioProcedimento_BindingImpl();
    }

    public DettaglioProcedimento_BindingSkeleton(it.eng.giustizia.avvocatura.ws.dettaglioProcedimento.DettaglioProcedimento_PortType impl) {
        this.impl = impl;
    }
    public java.lang.String dettaglioProcedimento(java.lang.String datiProcedimentoInput) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.dettaglioProcedimento(datiProcedimentoInput);
        return ret;
    }

}
