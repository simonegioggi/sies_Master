/**
 * RicercaAvvisi_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.ricercaAvvisi;

public class RicercaAvvisi_ServiceLocator extends org.apache.axis.client.Service implements it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_Service {

    public RicercaAvvisi_ServiceLocator() {
    }


    public RicercaAvvisi_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RicercaAvvisi_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ricercaAvvisi
    private java.lang.String ricercaAvvisi_address = "http://localhost:8080/services/ricercaAvvisi";

    public java.lang.String getricercaAvvisiAddress() {
        return ricercaAvvisi_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ricercaAvvisiWSDDServiceName = "ricercaAvvisi";

    public java.lang.String getricercaAvvisiWSDDServiceName() {
        return ricercaAvvisiWSDDServiceName;
    }

    public void setricercaAvvisiWSDDServiceName(java.lang.String name) {
        ricercaAvvisiWSDDServiceName = name;
    }

    public it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_PortType getricercaAvvisi() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ricercaAvvisi_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getricercaAvvisi(endpoint);
    }

    public it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_PortType getricercaAvvisi(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_BindingStub _stub = new it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_BindingStub(portAddress, this);
            _stub.setPortName(getricercaAvvisiWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setricercaAvvisiEndpointAddress(java.lang.String address) {
        ricercaAvvisi_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_BindingStub _stub = new it.eng.giustizia.avvocatura.ws.ricercaAvvisi.RicercaAvvisi_BindingStub(new java.net.URL(ricercaAvvisi_address), this);
                _stub.setPortName(getricercaAvvisiWSDDServiceName());
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
        if ("ricercaAvvisi".equals(inputPortName)) {
            return getricercaAvvisi();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://it/eng/giustizia/avvocatura/ws/ricercaAvvisi/", "ricercaAvvisi");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://it/eng/giustizia/avvocatura/ws/ricercaAvvisi/", "ricercaAvvisi"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ricercaAvvisi".equals(portName)) {
            setricercaAvvisiEndpointAddress(address);
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
