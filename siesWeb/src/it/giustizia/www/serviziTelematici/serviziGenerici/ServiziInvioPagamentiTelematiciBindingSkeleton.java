/**
 * ServiziInvioPagamentiTelematiciBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ServiziInvioPagamentiTelematiciBindingSkeleton implements it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici, org.apache.axis.wsdl.Skeleton {
    private it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici impl;
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
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroAvviso"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("downloadAvviso", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadAvviso"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("downloadAvviso") == null) {
            _myOperations.put("downloadAvviso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("downloadAvviso")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("eliminaRichiesta", _params, null);
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "eliminaRichiesta"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("eliminaRichiesta") == null) {
            _myOperations.put("eliminaRichiesta", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("eliminaRichiesta")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "richiestaPagamentoTelematico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiestaPagamentoTelematico"), it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("generaAvviso", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoGeneraAvviso"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "generaAvviso"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("generaAvviso") == null) {
            _myOperations.put("generaAvviso", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("generaAvviso")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "richiestaPagamentoTelematico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiestaPagamentoTelematico"), it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("generaRPT", _params, new javax.xml.namespace.QName("", "xmlRPT"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "generaRPT"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("generaRPT") == null) {
            _myOperations.put("generaRPT", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("generaRPT")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "crs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String[].class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "areaPubblica"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("inviaCarrelloRPT", _params, new javax.xml.namespace.QName("", "urlWISP"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "inviaCarrelloRPT"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("inviaCarrelloRPT") == null) {
            _myOperations.put("inviaCarrelloRPT", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("inviaCarrelloRPT")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idRevoca"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRevoca"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("inviaER", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "inviaER"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("inviaER") == null) {
            _myOperations.put("inviaER", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("inviaER")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceUfficioNEP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipologia"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listaConfNEP", _params, new javax.xml.namespace.QName("", "causalePagamentoNEPConf"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "causalePagamentoNEPConf"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "listaConfNEP"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listaConfNEP") == null) {
            _myOperations.put("listaConfNEP", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listaConfNEP")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idtipologia"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codice"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("listaDatiRiscossione", _params, new javax.xml.namespace.QName("", "datiRiscossione"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "datiRiscossione"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "listaDatiRiscossione"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("listaDatiRiscossione") == null) {
            _myOperations.put("listaDatiRiscossione", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("listaDatiRiscossione")).add(_oper);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idSession"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "esito"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("registraRispostaWisp", _params, new javax.xml.namespace.QName("", "esitoRispostaWisp"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoRispostaWisp"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "registraRispostaWisp"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("registraRispostaWisp") == null) {
            _myOperations.put("registraRispostaWisp", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("registraRispostaWisp")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("richiediCopiaRT", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiediCopiaRT"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("richiediCopiaRT") == null) {
            _myOperations.put("richiediCopiaRT", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("richiediCopiaRT")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("verificaRichiesta", _params, new javax.xml.namespace.QName("", "return"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        _oper.setElementQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "verificaRichiesta"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("verificaRichiesta") == null) {
            _myOperations.put("verificaRichiesta", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("verificaRichiesta")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("ServiziPagamentiException");
        _fault.setQName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _fault.setClassName("it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException");
        _fault.setXmlType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"));
        _oper.addFault(_fault);
    }

    public ServiziInvioPagamentiTelematiciBindingSkeleton() {
        this.impl = new it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematiciBindingImpl();
    }

    public ServiziInvioPagamentiTelematiciBindingSkeleton(it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici impl) {
        this.impl = impl;
    }
    public byte[] downloadAvviso(java.lang.String numeroAvviso) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        byte[] ret = impl.downloadAvviso(numeroAvviso);
        return ret;
    }

    public void eliminaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        impl.eliminaRichiesta(codiceCRS);
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso generaAvviso(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso ret = impl.generaAvviso(richiestaPagamentoTelematico);
        return ret;
    }

    public byte[] generaRPT(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        byte[] ret = impl.generaRPT(richiestaPagamentoTelematico);
        return ret;
    }

    public java.lang.String inviaCarrelloRPT(java.lang.String codiceFiscale, java.lang.String[] crs, boolean areaPubblica) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        java.lang.String ret = impl.inviaCarrelloRPT(codiceFiscale, crs, areaPubblica);
        return ret;
    }

    public java.lang.String inviaER(java.lang.String idRevoca, java.util.Calendar dataRevoca) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        java.lang.String ret = impl.inviaER(idRevoca, dataRevoca);
        return ret;
    }

    public it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[] listaConfNEP(java.lang.String codiceUfficioNEP, java.lang.String tipologia) throws java.rmi.RemoteException
    {
        it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[] ret = impl.listaConfNEP(codiceUfficioNEP, tipologia);
        return ret;
    }

    public it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[] listaDatiRiscossione(java.lang.String idtipologia, java.lang.String codice) throws java.rmi.RemoteException
    {
        it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[] ret = impl.listaDatiRiscossione(idtipologia, codice);
        return ret;
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp registraRispostaWisp(java.lang.String idSession, java.lang.String esito) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp ret = impl.registraRispostaWisp(idSession, esito);
        return ret;
    }

    public java.lang.String[] richiediCopiaRT(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        java.lang.String[] ret = impl.richiediCopiaRT(codiceCRS);
        return ret;
    }

    public java.lang.String[] verificaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException
    {
        java.lang.String[] ret = impl.verificaRichiesta(codiceCRS);
        return ret;
    }

}
