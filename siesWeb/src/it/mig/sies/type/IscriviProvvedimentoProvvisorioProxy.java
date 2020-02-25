package it.mig.sies.type;

public class IscriviProvvedimentoProvvisorioProxy implements it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType {
  private String _endpoint = null;
  private it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType iscriviProvvedimentoProvvisorio_PortType = null;
  
  public IscriviProvvedimentoProvvisorioProxy() {
    _initIscriviProvvedimentoProvvisorioProxy();
  }
  
  public IscriviProvvedimentoProvvisorioProxy(String endpoint) {
    _endpoint = endpoint;
    _initIscriviProvvedimentoProvvisorioProxy();
  }
  
  private void _initIscriviProvvedimentoProvvisorioProxy() {
    try {
      iscriviProvvedimentoProvvisorio_PortType = (new it.mig.sies.type.IscriviProvvedimentoProvvisorio_ServiceLocator()).getiscriviProvvedimentoProvvisorio();
      if (iscriviProvvedimentoProvvisorio_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iscriviProvvedimentoProvvisorio_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iscriviProvvedimentoProvvisorio_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iscriviProvvedimentoProvvisorio_PortType != null)
      ((javax.xml.rpc.Stub)iscriviProvvedimentoProvvisorio_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.mig.sies.type.IscriviProvvedimentoProvvisorio_PortType getIscriviProvvedimentoProvvisorio_PortType() {
    if (iscriviProvvedimentoProvvisorio_PortType == null)
      _initIscriviProvvedimentoProvvisorioProxy();
    return iscriviProvvedimentoProvvisorio_PortType;
  }
  
  public java.lang.String iscriviProvvedimentoProvvisorio(java.lang.String strutturaXML) throws java.rmi.RemoteException{
    if (iscriviProvvedimentoProvvisorio_PortType == null)
      _initIscriviProvvedimentoProvvisorioProxy();
    return iscriviProvvedimentoProvvisorio_PortType.iscriviProvvedimentoProvvisorio(strutturaXML);
  }
  
  
}