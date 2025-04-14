/**
 * WsServiziInterrogazioneInterniBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniInt;

public class WsServiziInterrogazioneInterniBindingSkeleton implements it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType, org.apache.axis.wsdl.Skeleton {
    private it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "descrizione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceEnte"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "indirizzoPec"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("ricercaEnteComplete", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "ruoloente"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaEnteComplete"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("ricercaEnteComplete") == null) {
            _myOperations.put("ricercaEnteComplete", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("ricercaEnteComplete")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SearchLimitException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "cognome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "indirizzo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceEnte"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "orderBy"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "asc"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), java.lang.Boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("ricercaSoggettoComplete", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "soggetto"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaSoggettoComplete"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("ricercaSoggettoComplete") == null) {
            _myOperations.put("ricercaSoggettoComplete", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("ricercaSoggettoComplete")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("SearchLimitException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"));
        _oper.addFault(_fault);
    }

    public WsServiziInterrogazioneInterniBindingSkeleton() {
        this.impl = new it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterniBindingImpl();
    }

    public WsServiziInterrogazioneInterniBindingSkeleton(it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType impl) {
        this.impl = impl;
    }
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ricercaEnteComplete(java.lang.String tipo, java.lang.String descrizione, java.lang.String codiceEnte, java.lang.String codiceFiscale, java.lang.String indirizzoPec) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException
    {
        it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ret = impl.ricercaEnteComplete(tipo, descrizione, codiceEnte, codiceFiscale, indirizzoPec);
        return ret;
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[] ricercaSoggettoComplete(java.lang.String cognome, java.lang.String nome, java.lang.String codiceFiscale, java.lang.String indirizzo, java.lang.String codiceEnte, java.lang.String orderBy, java.lang.Boolean asc) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException
    {
        it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[] ret = impl.ricercaSoggettoComplete(cognome, nome, codiceFiscale, indirizzo, codiceEnte, orderBy, asc);
        return ret;
    }

}
