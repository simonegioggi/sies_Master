/**
 * WsServiziInterrogazioneInterni_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniInt;

public class WsServiziInterrogazioneInterni_ServiceLocator extends org.apache.axis.client.Service implements it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_Service {

    public WsServiziInterrogazioneInterni_ServiceLocator() {
    }


    public WsServiziInterrogazioneInterni_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsServiziInterrogazioneInterni_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ServiziInterrogazioneInterniBeanPort
    private java.lang.String ServiziInterrogazioneInterniBeanPort_address = "https://89.119.251.203/ServiziInterrogazioneRegindeExt/ServiziInterrogazioneInterni";

    public java.lang.String getServiziInterrogazioneInterniBeanPortAddress() {
        return ServiziInterrogazioneInterniBeanPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ServiziInterrogazioneInterniBeanPortWSDDServiceName = "ServiziInterrogazioneInterniBeanPort";

    public java.lang.String getServiziInterrogazioneInterniBeanPortWSDDServiceName() {
        return ServiziInterrogazioneInterniBeanPortWSDDServiceName;
    }

    public void setServiziInterrogazioneInterniBeanPortWSDDServiceName(java.lang.String name) {
        ServiziInterrogazioneInterniBeanPortWSDDServiceName = name;
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType getServiziInterrogazioneInterniBeanPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServiziInterrogazioneInterniBeanPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServiziInterrogazioneInterniBeanPort(endpoint);
    }

    public it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType getServiziInterrogazioneInterniBeanPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterniBindingStub _stub = new it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterniBindingStub(portAddress, this);
            _stub.setPortName(getServiziInterrogazioneInterniBeanPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServiziInterrogazioneInterniBeanPortEndpointAddress(java.lang.String address) {
        ServiziInterrogazioneInterniBeanPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterniBindingStub _stub = new it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterniBindingStub(new java.net.URL(ServiziInterrogazioneInterniBeanPort_address), this);
                _stub.setPortName(getServiziInterrogazioneInterniBeanPortWSDDServiceName());
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
        if ("ServiziInterrogazioneInterniBeanPort".equals(inputPortName)) {
            return getServiziInterrogazioneInterniBeanPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "WsServiziInterrogazioneInterni");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.giustizia.it/serviziTelematici/reginde/interrogazioniInt", "ServiziInterrogazioneInterniBeanPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServiziInterrogazioneInterniBeanPort".equals(portName)) {
            setServiziInterrogazioneInterniBeanPortEndpointAddress(address);
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
