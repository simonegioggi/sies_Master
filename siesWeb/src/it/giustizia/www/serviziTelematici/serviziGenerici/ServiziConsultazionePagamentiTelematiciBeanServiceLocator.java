/**
 * ServiziConsultazionePagamentiTelematiciBeanServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ServiziConsultazionePagamentiTelematiciBeanServiceLocator extends org.apache.axis.client.Service implements it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBeanService {

    public ServiziConsultazionePagamentiTelematiciBeanServiceLocator() {
    }


    public ServiziConsultazionePagamentiTelematiciBeanServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServiziConsultazionePagamentiTelematiciBeanServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ServiziConsultazionePagamentiTelematiciSOAPPort
    private java.lang.String ServiziConsultazionePagamentiTelematiciSOAPPort_address = "http://test-pst/servizi/ServiziConsultazionePagamentiTelematici";

    public java.lang.String getServiziConsultazionePagamentiTelematiciSOAPPortAddress() {
        return ServiziConsultazionePagamentiTelematiciSOAPPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ServiziConsultazionePagamentiTelematiciSOAPPortWSDDServiceName = "ServiziConsultazionePagamentiTelematiciSOAPPort";

    public java.lang.String getServiziConsultazionePagamentiTelematiciSOAPPortWSDDServiceName() {
        return ServiziConsultazionePagamentiTelematiciSOAPPortWSDDServiceName;
    }

    public void setServiziConsultazionePagamentiTelematiciSOAPPortWSDDServiceName(java.lang.String name) {
        ServiziConsultazionePagamentiTelematiciSOAPPortWSDDServiceName = name;
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici getServiziConsultazionePagamentiTelematiciSOAPPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServiziConsultazionePagamentiTelematiciSOAPPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServiziConsultazionePagamentiTelematiciSOAPPort(endpoint);
    }

    public it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici getServiziConsultazionePagamentiTelematiciSOAPPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBindingStub _stub = new it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBindingStub(portAddress, this);
            _stub.setPortName(getServiziConsultazionePagamentiTelematiciSOAPPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(java.lang.String address) {
        ServiziConsultazionePagamentiTelematiciSOAPPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici.class.isAssignableFrom(serviceEndpointInterface)) {
                it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBindingStub _stub = new it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBindingStub(new java.net.URL(ServiziConsultazionePagamentiTelematiciSOAPPort_address), this);
                _stub.setPortName(getServiziConsultazionePagamentiTelematiciSOAPPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ServiziConsultazionePagamentiTelematiciSOAPPort".equals(inputPortName)) {
            return getServiziConsultazionePagamentiTelematiciSOAPPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziConsultazionePagamentiTelematiciBeanService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/serviziGenerici", "ServiziConsultazionePagamentiTelematiciSOAPPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServiziConsultazionePagamentiTelematiciSOAPPort".equals(portName)) {
            setServiziConsultazionePagamentiTelematiciSOAPPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
