package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ServiziInvioPagamentiTelematiciProxy implements it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici {
  private String _endpoint = null;
  private it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici serviziInvioPagamentiTelematici = null;
  
  public ServiziInvioPagamentiTelematiciProxy() {
    _initServiziInvioPagamentiTelematiciProxy();
  }
  
  public ServiziInvioPagamentiTelematiciProxy(String endpoint) {
    _endpoint = endpoint;
    _initServiziInvioPagamentiTelematiciProxy();
  }
  
  private void _initServiziInvioPagamentiTelematiciProxy() {
    try {
      serviziInvioPagamentiTelematici = (new it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematiciBeanServiceLocator()).getServiziInvioPagamentiTelematiciSOAPPort();
      if (serviziInvioPagamentiTelematici != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)serviziInvioPagamentiTelematici)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)serviziInvioPagamentiTelematici)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (serviziInvioPagamentiTelematici != null)
      ((javax.xml.rpc.Stub)serviziInvioPagamentiTelematici)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.ServiziInvioPagamentiTelematici getServiziInvioPagamentiTelematici() {
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici;
  }
  
  public byte[] downloadAvviso(java.lang.String numeroAvviso) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.downloadAvviso(numeroAvviso);
  }
  
  public void eliminaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    serviziInvioPagamentiTelematici.eliminaRichiesta(codiceCRS);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso generaAvviso(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.generaAvviso(richiestaPagamentoTelematico);
  }
  
  public byte[] generaRPT(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.generaRPT(richiestaPagamentoTelematico);
  }
  
  public java.lang.String inviaCarrelloRPT(java.lang.String codiceFiscale, java.lang.String[] crs, boolean areaPubblica) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.inviaCarrelloRPT(codiceFiscale, crs, areaPubblica);
  }
  
  public java.lang.String inviaER(java.lang.String idRevoca, java.util.Calendar dataRevoca) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.inviaER(idRevoca, dataRevoca);
  }
  
  public it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[] listaConfNEP(java.lang.String codiceUfficioNEP, java.lang.String tipologia) throws java.rmi.RemoteException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.listaConfNEP(codiceUfficioNEP, tipologia);
  }
  
  public it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[] listaDatiRiscossione(java.lang.String idtipologia, java.lang.String codice) throws java.rmi.RemoteException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.listaDatiRiscossione(idtipologia, codice);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp registraRispostaWisp(java.lang.String idSession, java.lang.String esito) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.registraRispostaWisp(idSession, esito);
  }
  
  public java.lang.String[] richiediCopiaRT(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.richiediCopiaRT(codiceCRS);
  }
  
  public java.lang.String[] verificaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziInvioPagamentiTelematici == null)
      _initServiziInvioPagamentiTelematiciProxy();
    return serviziInvioPagamentiTelematici.verificaRichiesta(codiceCRS);
  }
  
  
}