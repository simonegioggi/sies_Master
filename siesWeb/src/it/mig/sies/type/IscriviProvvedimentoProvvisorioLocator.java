/**
 * IscriviProvvedimentoProvvisorioLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.mig.sies.type;

public class IscriviProvvedimentoProvvisorioLocator extends org.apache.axis.client.Service implements it.mig.sies.type.IscriviProvvedimentoProvvisorio {
    
    public IscriviProvvedimentoProvvisorioLocator() {
    }

    // Aggiunto metodo per parametrizzare l'indirizzo del WS NSC
    public IscriviProvvedimentoProvvisorioLocator(String NscWsAddress) {
          IscriviProvvedimentoProvvisorioPort_address=NscWsAddress;
    }
    
    public IscriviProvvedimentoProvvisorioLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IscriviProvvedimentoProvvisorioLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IscriviProvvedimentoProvvisorioPort
    private java.lang.String IscriviProvvedimentoProvvisorioPort_address = "";

    //---> Indirizzo WS NSC
    //private java.lang.String IscriviProvvedimentoProvvisorioPort_address = "http://10.5.206.149:9001/sies/iscriviProvvedimentoProvvisorio";
    //-----> Solo Per Test in Eutelia
   // private java.lang.String IscriviProvvedimentoProvvisorioPort_address = "http://localhost:9000/axis/IscriviProvvedimentoProvvisorio.jws";

    
    public java.lang.String getIscriviProvvedimentoProvvisorioPortAddress() {
        return IscriviProvvedimentoProvvisorioPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IscriviProvvedimentoProvvisorioPortWSDDServiceName = "IscriviProvvedimentoProvvisorioPort";

    public java.lang.String getIscriviProvvedimentoProvvisorioPortWSDDServiceName() {
        return IscriviProvvedimentoProvvisorioPortWSDDServiceName;
    }

    public void setIscriviProvvedimentoProvvisorioPortWSDDServiceName(java.lang.String name) {
        IscriviProvvedimentoProvvisorioPortWSDDServiceName = name;
    }

    public it.mig.sies.type.IscriviProvvedimentoProvvisorioPort getIscriviProvvedimentoProvvisorioPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IscriviProvvedimentoProvvisorioPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIscriviProvvedimentoProvvisorioPort(endpoint);
    }

    public it.mig.sies.type.IscriviProvvedimentoProvvisorioPort getIscriviProvvedimentoProvvisorioPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            it.mig.sies.type.IscriviProvvedimentoProvvisorioPortStub _stub = new it.mig.sies.type.IscriviProvvedimentoProvvisorioPortStub(portAddress, this);
            _stub.setPortName(getIscriviProvvedimentoProvvisorioPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIscriviProvvedimentoProvvisorioPortEndpointAddress(java.lang.String address) {
        IscriviProvvedimentoProvvisorioPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (it.mig.sies.type.IscriviProvvedimentoProvvisorioPort.class.isAssignableFrom(serviceEndpointInterface)) {
                it.mig.sies.type.IscriviProvvedimentoProvvisorioPortStub _stub = new it.mig.sies.type.IscriviProvvedimentoProvvisorioPortStub(new java.net.URL(IscriviProvvedimentoProvvisorioPort_address), this);
                _stub.setPortName(getIscriviProvvedimentoProvvisorioPortWSDDServiceName());
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
        if ("IscriviProvvedimentoProvvisorioPort".equals(inputPortName)) {
            return getIscriviProvvedimentoProvvisorioPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://it/mig/sies/type", "IscriviProvvedimentoProvvisorio");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://it/mig/sies/type", "IscriviProvvedimentoProvvisorioPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IscriviProvvedimentoProvvisorioPort".equals(portName)) {
            setIscriviProvvedimentoProvvisorioPortEndpointAddress(address);
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
