/**
 * WsServiziInterrogazioneInterniBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniInt;

public class WsServiziInterrogazioneInterniBindingStub extends org.apache.axis.client.Stub implements it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[2];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ricercaEnteComplete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "descrizione"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceEnte"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "indirizzoPec"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "ruoloente"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"),
                      "it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ricercaSoggettoComplete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "cognome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "nome"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "indirizzo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceEnte"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "orderBy"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "asc"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), java.lang.Boolean.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "soggetto"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"),
                      "it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException"), 
                      true
                     ));
        _operations[1] = oper;

    }

    public WsServiziInterrogazioneInterniBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public WsServiziInterrogazioneInterniBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public WsServiziInterrogazioneInterniBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "indirizzo");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Indirizzo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "ruoloente");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "soggetti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniExt", "soggetto");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "indAbilitato");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.IndAbilitato.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "referente");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.Referente.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "SearchLimitException");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "tipologieEnti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.TipologieEnti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ricercaEnteComplete(java.lang.String tipo, java.lang.String descrizione, java.lang.String codiceEnte, java.lang.String codiceFiscale, java.lang.String indirizzoPec) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaEnteComplete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {tipo, descrizione, codiceEnte, codiceFiscale, indirizzoPec});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException) {
              throw (it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[] ricercaSoggettoComplete(java.lang.String cognome, java.lang.String nome, java.lang.String codiceFiscale, java.lang.String indirizzo, java.lang.String codiceEnte, java.lang.String orderBy, java.lang.Boolean asc) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ricercaSoggettoComplete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {cognome, nome, codiceFiscale, indirizzo, codiceEnte, orderBy, asc});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException) {
              throw (it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
