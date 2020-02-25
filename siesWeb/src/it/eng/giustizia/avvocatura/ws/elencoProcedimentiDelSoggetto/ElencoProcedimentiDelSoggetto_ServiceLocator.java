/**
 * ElencoProcedimentiDelSoggetto_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto;

public class ElencoProcedimentiDelSoggetto_ServiceLocator extends org.apache.axis.client.Service implements it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_Service {

    public ElencoProcedimentiDelSoggetto_ServiceLocator() {
    }


    public ElencoProcedimentiDelSoggetto_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ElencoProcedimentiDelSoggetto_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for elencoProcedimentiDelSoggetto
    private java.lang.String elencoProcedimentiDelSoggetto_address = "http://localhost:8080/services/elencoProcedimentiDelSoggetto";

    public java.lang.String getelencoProcedimentiDelSoggettoAddress() {
        return elencoProcedimentiDelSoggetto_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String elencoProcedimentiDelSoggettoWSDDServiceName = "elencoProcedimentiDelSoggetto";

    public java.lang.String getelencoProcedimentiDelSoggettoWSDDServiceName() {
        return elencoProcedimentiDelSoggettoWSDDServiceName;
    }

    public void setelencoProcedimentiDelSoggettoWSDDServiceName(java.lang.String name) {
        elencoProcedimentiDelSoggettoWSDDServiceName = name;
    }

    public it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_PortType getelencoProcedimentiDelSoggetto() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(elencoProcedimentiDelSoggetto_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getelencoProcedimentiDelSoggetto(endpoint);
    }

    public it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_PortType getelencoProcedimentiDelSoggetto(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_BindingStub _stub = new it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_BindingStub(portAddress, this);
            _stub.setPortName(getelencoProcedimentiDelSoggettoWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setelencoProcedimentiDelSoggettoEndpointAddress(java.lang.String address) {
        elencoProcedimentiDelSoggetto_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_BindingStub _stub = new it.eng.giustizia.avvocatura.ws.elencoProcedimentiDelSoggetto.ElencoProcedimentiDelSoggetto_BindingStub(new java.net.URL(elencoProcedimentiDelSoggetto_address), this);
                _stub.setPortName(getelencoProcedimentiDelSoggettoWSDDServiceName());
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
        if ("elencoProcedimentiDelSoggetto".equals(inputPortName)) {
            return getelencoProcedimentiDelSoggetto();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://it/eng/giustizia/avvocatura/ws/elencoProcedimentiDelSoggetto/", "elencoProcedimentiDelSoggetto");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://it/eng/giustizia/avvocatura/ws/elencoProcedimentiDelSoggetto/", "elencoProcedimentiDelSoggetto"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("elencoProcedimentiDelSoggetto".equals(portName)) {
            setelencoProcedimentiDelSoggettoEndpointAddress(address);
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
