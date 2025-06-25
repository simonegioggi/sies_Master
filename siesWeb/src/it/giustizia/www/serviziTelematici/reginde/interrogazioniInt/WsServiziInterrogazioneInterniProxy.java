package it.giustizia.www.serviziTelematici.reginde.interrogazioniInt;

public class WsServiziInterrogazioneInterniProxy implements it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType {
  private String _endpoint = null;
  private it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType wsServiziInterrogazioneInterni_PortType = null;
  
  public WsServiziInterrogazioneInterniProxy() {
    _initWsServiziInterrogazioneInterniProxy();
  }
  
  public WsServiziInterrogazioneInterniProxy(String endpoint) {
    _endpoint = endpoint;
    _initWsServiziInterrogazioneInterniProxy();
  }
  
  private void _initWsServiziInterrogazioneInterniProxy() {
    try {
      wsServiziInterrogazioneInterni_PortType = (new it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_ServiceLocator()).getServiziInterrogazioneInterniBeanPort();
      if (wsServiziInterrogazioneInterni_PortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wsServiziInterrogazioneInterni_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wsServiziInterrogazioneInterni_PortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wsServiziInterrogazioneInterni_PortType != null)
      ((javax.xml.rpc.Stub)wsServiziInterrogazioneInterni_PortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.WsServiziInterrogazioneInterni_PortType getWsServiziInterrogazioneInterni_PortType() {
    if (wsServiziInterrogazioneInterni_PortType == null)
      _initWsServiziInterrogazioneInterniProxy();
    return wsServiziInterrogazioneInterni_PortType;
  }
  
  public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ricercaEnteComplete(java.lang.String tipo, java.lang.String descrizione, java.lang.String codiceEnte, java.lang.String codiceFiscale, java.lang.String indirizzoPec) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException{
    if (wsServiziInterrogazioneInterni_PortType == null)
      _initWsServiziInterrogazioneInterniProxy();
    return wsServiziInterrogazioneInterni_PortType.ricercaEnteComplete(tipo, descrizione, codiceEnte, codiceFiscale, indirizzoPec);
  }
  
  public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[] ricercaSoggettoComplete(java.lang.String cognome, java.lang.String nome, java.lang.String codiceFiscale, java.lang.String indirizzo, java.lang.String codiceEnte, java.lang.String orderBy, java.lang.Boolean asc) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException{
    if (wsServiziInterrogazioneInterni_PortType == null)
      _initWsServiziInterrogazioneInterniProxy();
    return wsServiziInterrogazioneInterni_PortType.ricercaSoggettoComplete(cognome, nome, codiceFiscale, indirizzo, codiceEnte, orderBy, asc);
  }
  
  
}