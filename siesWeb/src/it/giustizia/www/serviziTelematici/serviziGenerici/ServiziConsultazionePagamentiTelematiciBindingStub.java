/**
 * ServiziConsultazionePagamentiTelematiciBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ServiziConsultazionePagamentiTelematiciBindingStub extends org.apache.axis.client.Stub implements it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[10];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("downloadRicevuta");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "originale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "content"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("downloadRichiesta");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        oper.setReturnClass(byte[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "content"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("elencoPagamenti");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipologia"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceFiscale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceDistretto"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "causale"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "stato"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRichiestaDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRichiestaA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dimensionePagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroPagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicerca"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("elencoPagamentiRevocati");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRicevutaRevocataDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRicevutaRevocataA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dimensionePagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroPagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicerca"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAPAinKO");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRichiestaDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRichiestaA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceDistretto"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceUfficio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dimensionePagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroPagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaPagamenti"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPagamentiRevocati");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRicevutaRevocataDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRicevutaRevocataA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRevocataDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRevocataA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dimensionePagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroPagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaRevocati"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPagamentoByCRS");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codiceCRS"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "statoRichiestaPagamento"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getPagamentoByDate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRichiestaDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRichiestaA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "senzaRicevuta"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dimensionePagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroPagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaPagamenti"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ricercaFlussiRendicontazioneScaricati");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataScaricoDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataScaricoA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "crsErrore"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dimensionePagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroPagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaRendicontazione"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ricercaPagamentiNonVerificati");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataControlloDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataControlloA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "idFlusso"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRicevutaDa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dataRicevutaA"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "dimensionePagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "numeroPagina"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaPagaNoVerify"));
        oper.setReturnClass(it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    public ServiziConsultazionePagamentiTelematiciBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public ServiziConsultazionePagamentiTelematiciBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public ServiziConsultazionePagamentiTelematiciBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/Pagamenti", "dettaglioCRS");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.Pagamenti.DettaglioCRS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "datiContesto");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DatiContesto.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadRicevuta");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DownloadRicevuta.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadRicevutaResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DownloadRicevutaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadRichiesta");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DownloadRichiesta.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadRichiestaResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.DownloadRichiestaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamenti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.ElencoPagamenti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamentiResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.ElencoPagamentiResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamentiRevocati");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.ElencoPagamentiRevocati.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamentiRevocatiResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.ElencoPagamentiRevocatiResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "flussoRendicontazione");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.FlussoRendicontazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getAPAinKO");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetAPAinKO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getAPAinKOResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetAPAinKOResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentiRevocati");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetPagamentiRevocati.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentiRevocatiResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetPagamentiRevocatiResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentoByCRS");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetPagamentoByCRS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentoByCRSResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetPagamentoByCRSResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentoByDate");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetPagamentoByDate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentoByDateResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.GetPagamentoByDateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "pagamentoNonVerificato");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.PagamentoNonVerificato.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "pagamentoRevocato");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.PagamentoRevocato.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "revocaPagamento");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RevocaPagamento.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaFlussiRendicontazioneScaricati");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RicercaFlussiRendicontazioneScaricati.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaFlussiRendicontazioneScaricatiResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RicercaFlussiRendicontazioneScaricatiResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaPagamentiNonVerificati");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RicercaPagamentiNonVerificati.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaPagamentiNonVerificatiResponse");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RicercaPagamentiNonVerificatiResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicerca");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaPagamenti");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaPagaNoVerify");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaRendicontazione");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "risultatoRicercaRevocati");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "statoRichiestaPagamento");
            cachedSerQNames.add(qName);
            cls = it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento.class;
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

    public byte[] downloadRicevuta(java.lang.String codiceCRS, boolean originale) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadRicevuta"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceCRS, new java.lang.Boolean(originale)});

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
  throw axisFaultException;
}
    }

    public byte[] downloadRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "downloadRichiesta"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceCRS});

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
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca elencoPagamenti(java.lang.String codiceCRS, java.lang.String tipologia, java.lang.String codiceFiscale, java.lang.String codiceDistretto, java.lang.String causale, java.lang.String stato, java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamenti"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceCRS, tipologia, codiceFiscale, codiceDistretto, causale, stato, dataRichiestaDa, dataRichiestaA, new java.lang.Integer(dimensionePagina), new java.lang.Integer(numeroPagina)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca elencoPagamentiRevocati(java.util.Calendar dataRicevutaRevocataDa, java.util.Calendar dataRicevutaRevocataA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "elencoPagamentiRevocati"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {dataRicevutaRevocataDa, dataRicevutaRevocataA, new java.lang.Integer(dimensionePagina), new java.lang.Integer(numeroPagina)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti getAPAinKO(java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, java.lang.String codiceDistretto, java.lang.String codiceUfficio, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getAPAinKO"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {dataRichiestaDa, dataRichiestaA, codiceDistretto, codiceUfficio, new java.lang.Integer(dimensionePagina), new java.lang.Integer(numeroPagina)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati getPagamentiRevocati(java.util.Calendar dataRicevutaRevocataDa, java.util.Calendar dataRicevutaRevocataA, java.util.Calendar dataRevocataDa, java.util.Calendar dataRevocataA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentiRevocati"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {dataRicevutaRevocataDa, dataRicevutaRevocataA, dataRevocataDa, dataRevocataA, new java.lang.Integer(dimensionePagina), new java.lang.Integer(numeroPagina)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento getPagamentoByCRS(java.lang.String codiceCRS) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentoByCRS"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {codiceCRS});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti getPagamentoByDate(java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, boolean senzaRicevuta, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "getPagamentoByDate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {dataRichiestaDa, dataRichiestaA, new java.lang.Boolean(senzaRicevuta), new java.lang.Integer(dimensionePagina), new java.lang.Integer(numeroPagina)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione ricercaFlussiRendicontazioneScaricati(java.util.Calendar dataScaricoDa, java.util.Calendar dataScaricoA, boolean crsErrore, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaFlussiRendicontazioneScaricati"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {dataScaricoDa, dataScaricoA, new java.lang.Boolean(crsErrore), new java.lang.Integer(dimensionePagina), new java.lang.Integer(numeroPagina)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify ricercaPagamentiNonVerificati(java.util.Calendar dataControlloDa, java.util.Calendar dataControlloA, java.lang.String idFlusso, java.util.Calendar dataRicevutaDa, java.util.Calendar dataRicevutaA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ricercaPagamentiNonVerificati"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {dataControlloDa, dataControlloA, idFlusso, dataRicevutaDa, dataRicevutaA, new java.lang.Integer(dimensionePagina), new java.lang.Integer(numeroPagina)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify) _resp;
            } catch (java.lang.Exception _exception) {
                return (it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify) org.apache.axis.utils.JavaUtils.convert(_resp, it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
