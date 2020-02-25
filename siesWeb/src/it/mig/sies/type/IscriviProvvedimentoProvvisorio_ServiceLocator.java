/**
 * IscriviProvvedimentoProvvisorio_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.mig.sies.type;

public class IscriviProvvedimentoProvvisorio_ServiceLocator extends org.apache.axis.client.Service implements it.mig.sies.type.IscriviProvvedimentoProvvisorio_Service {

    public IscriviProvvedimentoProvvisorio_ServiceLocator() {
    }


    public IscriviProvvedimentoProvvisorio_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IscriviProvvedimentoProvvisorio_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for iscriviProvvedimentoProvvisorio
    private java.lang.String iscriviProvvedimentoProvvisorio_address = "";

    public java.lang.String getiscriviProvvedimentoProvvisorioAddress() {
        return iscriviProvvedimentoProvvisorio_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String iscriviProvvedimentoProvvisorioWSDDServiceName = "iscriviProvvedimentoProvvisorio";

    public java.lang.String getiscriviProvvedimentoProvvisorioWSDDServiceName() {
        return iscriviProvvedimentoProvvisorioWSDDServiceName;
    }

    public void setiscriviProvvedimentoProvvisorioWSDDServiceName(java.lang.String name) {
        iscriviProvvedimentoProvvisorioWSDDServiceName = name;
    }

    public it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType getiscriviProvvedimentoProvvisorio() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(iscriviProvvedimentoProvvisorio_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getiscriviProvvedimentoProvvisorio(endpoint);
    }

    public it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType getiscriviProvvedimentoProvvisorio(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.mig.sies.type.IscriviProvvedimentoProvvisorioSoapBindingStub _stub = new it.mig.sies.type.IscriviProvvedimentoProvvisorioSoapBindingStub(portAddress, this);
            _stub.setPortName(getiscriviProvvedimentoProvvisorioWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setiscriviProvvedimentoProvvisorioEndpointAddress(java.lang.String address) {
        iscriviProvvedimentoProvvisorio_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                it.mig.sies.type.IscriviProvvedimentoProvvisorioSoapBindingStub _stub = new it.mig.sies.type.IscriviProvvedimentoProvvisorioSoapBindingStub(new java.net.URL(iscriviProvvedimentoProvvisorio_address), this);
                _stub.setPortName(getiscriviProvvedimentoProvvisorioWSDDServiceName());
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
        if ("iscriviProvvedimentoProvvisorio".equals(inputPortName)) {
            return getiscriviProvvedimentoProvvisorio();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://it/mig/sies/type", "iscriviProvvedimentoProvvisorio");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://it/mig/sies/type", "iscriviProvvedimentoProvvisorio"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("iscriviProvvedimentoProvvisorio".equals(portName)) {
            setiscriviProvvedimentoProvvisorioEndpointAddress(address);
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
