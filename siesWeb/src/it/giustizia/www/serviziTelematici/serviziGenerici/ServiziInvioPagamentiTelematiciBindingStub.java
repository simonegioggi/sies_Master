/**
 * ServiziInvioPagamentiTelematiciBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ServiziInvioPagamentiTelematiciBindingStub extends org.apache.axis.client.Stub implements it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[11];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("downloadAvviso");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroAvviso"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("eliminaRichiesta");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("generaAvviso");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "richiestaPagamentoTelematico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiestaPagamentoTelematico"), it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoGeneraAvviso"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("generaRPT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "richiestaPagamentoTelematico"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiestaPagamentoTelematico"), it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "xmlRPT"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("inviaCarrelloRPT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "crs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String[].class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "areaPubblica"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "urlWISP"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("inviaER");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idRevoca"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRevoca"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listaConfNEP");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceUfficioNEP"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipologia"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "causalePagamentoNEPConf"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "causalePagamentoNEPConf"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listaDatiRiscossione");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idtipologia"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codice"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "datiRiscossione"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "datiRiscossione"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("registraRispostaWisp");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idSession"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "esito"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoRispostaWisp"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "esitoRispostaWisp"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("richiediCopiaRT");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("verificaRichiesta");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"),
                      "it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException",
                      new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException"), 
                      true
                     ));
        _operations[10] = oper;

    }

    public ServiziInvioPagamentiTelematiciBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ServiziInvioPagamentiTelematiciBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ServiziInvioPagamentiTelematiciBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "altriSoggetti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.AltriSoggetti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "anagrafica");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Anagrafica.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "applicazioni");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Applicazioni.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "attiDepositabiliUfficio");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.AttiDepositabiliUfficio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "causalePagamentoConf");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoConf.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "causalePagamentoNEPConf");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "cdo");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Cdo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "contenutoRichiestaRtag");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.ContenutoRichiestaRtag.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "datiRiscossione");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "definizioneServizio");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.DefinizioneServizio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "definizioneTemplate");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.DefinizioneTemplate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "distretti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Distretti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "download");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Download.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "enti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Enti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "fornitori");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Fornitori.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "gestoreLocale");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.GestoreLocale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "indirizziAbilitati");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.IndirizziAbilitati.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "lookupRepertorio");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.LookupRepertorio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "lookupUfficio");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.LookupUfficio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "parteCausa");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.ParteCausa.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "pda");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Pda.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "regioni");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Regioni.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "richiestaRtag");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.RichiestaRtag.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "rito");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Rito.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "servizi");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Servizi.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "serviziAtti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.ServiziAtti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipiAttoDepositabile");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.TipiAttoDepositabile.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipiUfficio");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.TipiUfficio.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "tipologiaPda");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.TipologiaPda.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "uffici");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.Uffici.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "ufficiGiudiziari");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.UfficiGiudiziari.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "ufficioInterno");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.UfficioInterno.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/pstbe/gestione", "ufficiPenale");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.pstbe.gestione.UfficiPenale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "anagraficaSoggetto");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.AnagraficaSoggetto.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "dateConstraint");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DateConstraint.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiMarcaBolloDigitale");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DatiMarcaBolloDigitale.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiSingoloVersamento");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DatiSingoloVersamento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiVersamento");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DatiVersamento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoGeneraAvviso");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "esitoRispostaWisp");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "registro");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.Registro.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiestaPagamentoTelematico");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziPagamentiException");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException.class;
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

    public byte[] downloadAvviso(java.lang.String numeroAvviso) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadAvviso"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {numeroAvviso});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (byte[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (byte[]) org.apache.axis.utils.JavaUtils.convert(_resp, byte[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public void eliminaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "eliminaRichiesta"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceCRS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso generaAvviso(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "generaAvviso"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {richiestaPagamentoTelematico});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public byte[] generaRPT(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "generaRPT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {richiestaPagamentoTelematico});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (byte[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (byte[]) org.apache.axis.utils.JavaUtils.convert(_resp, byte[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String inviaCarrelloRPT(java.lang.String codiceFiscale, java.lang.String[] crs, boolean areaPubblica) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "inviaCarrelloRPT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceFiscale, crs, new java.lang.Boolean(areaPubblica)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String inviaER(java.lang.String idRevoca, java.util.Calendar dataRevoca) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "inviaER"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idRevoca, dataRevoca});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[] listaConfNEP(java.lang.String codiceUfficioNEP, java.lang.String tipologia) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "listaConfNEP"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceUfficioNEP, tipologia});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[] listaDatiRiscossione(java.lang.String idtipologia, java.lang.String codice) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "listaDatiRiscossione"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idtipologia, codice});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[]) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp registraRispostaWisp(java.lang.String idSession, java.lang.String esito) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "registraRispostaWisp"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {idSession, esito});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String[] richiediCopiaRT(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "richiediCopiaRT"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceCRS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String[]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String[] verificaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "verificaRichiesta"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceCRS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String[]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) {
              throw (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
