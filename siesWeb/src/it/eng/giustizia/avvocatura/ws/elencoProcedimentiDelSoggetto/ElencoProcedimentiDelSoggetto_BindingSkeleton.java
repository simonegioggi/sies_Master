/**
 * ElencoProcedimentiDelSoggetto_BindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto;

public class ElencoProcedimentiDelSoggetto_BindingSkeleton implements it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_PortType, org.apache.axis.wsdl.Skeleton {
    private it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_PortType impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "datiSoggettoInput"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("elencoProcedimentiDelSoggetto", _params, new javax.xml.namespace.QName("", "elencoProcedimentiOutput"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://it/eng/giustizia/avvocatura/ws/elencoProcedimentiDelSoggetto/", "ElencoProcedimentiDelSoggetto"));
        _oper.setSoapAction("http://it/eng/giustizia/avvocatura/ws/elencoProcedimentiDelSoggetto/ElencoProcedimentiDelSoggetto");
        _myOperationsList.add(_oper);
        if (_myOperations.get("elencoProcedimentiDelSoggetto") == null) {
            _myOperations.put("elencoProcedimentiDelSoggetto", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("elencoProcedimentiDelSoggetto")).add(_oper);
    }

    public ElencoProcedimentiDelSoggetto_BindingSkeleton() {
        this.impl = new it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_BindingImpl();
    }

    public ElencoProcedimentiDelSoggetto_BindingSkeleton(it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_PortType impl) {
        this.impl = impl;
    }
    public java.lang.String elencoProcedimentiDelSoggetto(java.lang.String datiSoggettoInput) throws java.rmi.RemoteException
    {
        java.lang.String ret = impl.elencoProcedimentiDelSoggetto(datiSoggettoInput);
        return ret;
    }

}
