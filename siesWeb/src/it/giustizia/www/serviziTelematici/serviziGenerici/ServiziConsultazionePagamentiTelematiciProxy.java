package it.giustizia.www.serviziTelematici.serviziGenerici;

public class ServiziConsultazionePagamentiTelematiciProxy implements it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici {
  private String _endpoint = null;
  private it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici serviziConsultazionePagamentiTelematici = null;
  
  public ServiziConsultazionePagamentiTelematiciProxy() {
    _initServiziConsultazionePagamentiTelematiciProxy();
  }
  
  public ServiziConsultazionePagamentiTelematiciProxy(String endpoint) {
    _endpoint = endpoint;
    _initServiziConsultazionePagamentiTelematiciProxy();
  }
  
  private void _initServiziConsultazionePagamentiTelematiciProxy() {
    try {
      serviziConsultazionePagamentiTelematici = (new it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematiciBeanServiceLocator()).getServiziConsultazionePagamentiTelematiciSOAPPort();
      if (serviziConsultazionePagamentiTelematici != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)serviziConsultazionePagamentiTelematici)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)serviziConsultazionePagamentiTelematici)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (serviziConsultazionePagamentiTelematici != null)
      ((javax.xml.rpc.Stub)serviziConsultazionePagamentiTelematici)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.ServiziConsultazionePagamentiTelematici getServiziConsultazionePagamentiTelematici() {
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici;
  }
  
  public java.lang.String downloadPDFRicevuta(java.lang.String idPagamento, boolean bollo) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.downloadPDFRicevuta(idPagamento, bollo);
  }
  
  public byte[] downloadRicevuta(java.lang.String codiceCRS, boolean originale) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.downloadRicevuta(codiceCRS, originale);
  }
  
  public byte[] downloadRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.downloadRichiesta(codiceCRS);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca elencoPagamenti(java.lang.String codiceCRS, java.lang.String tipologia, java.lang.String codiceFiscale, java.lang.String codiceDistretto, java.lang.String causale, java.lang.String stato, java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, int dimensionePagina, int numeroPagina, java.util.Calendar dataRicevutaDa, java.util.Calendar dataRicevutaA) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.elencoPagamenti(codiceCRS, tipologia, codiceFiscale, codiceDistretto, causale, stato, dataRichiestaDa, dataRichiestaA, dimensionePagina, numeroPagina, dataRicevutaDa, dataRicevutaA);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca elencoPagamentiRevocati(java.util.Calendar dataRicevutaRevocataDa, java.util.Calendar dataRicevutaRevocataA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.elencoPagamentiRevocati(dataRicevutaRevocataDa, dataRicevutaRevocataA, dimensionePagina, numeroPagina);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti getAPAinKO(java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, java.lang.String codiceDistretto, java.lang.String codiceUfficio, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.getAPAinKO(dataRichiestaDa, dataRichiestaA, codiceDistretto, codiceUfficio, dimensionePagina, numeroPagina);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.InfoPagamentoRendicontato getInfoPagamentoRendicontato(java.lang.String iuv, java.lang.String codiceUfficio) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.getInfoPagamentoRendicontato(iuv, codiceUfficio);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRiversamenti getPagamentiByFlusso(java.lang.String codUffNep, java.lang.String idFlusso, java.util.Calendar dataOraFlusso, java.lang.String idPSP) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.getPagamentiByFlusso(codUffNep, idFlusso, dataOraFlusso, idPSP);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati getPagamentiRevocati(java.util.Calendar dataRicevutaRevocataDa, java.util.Calendar dataRicevutaRevocataA, java.util.Calendar dataRevocataDa, java.util.Calendar dataRevocataA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.getPagamentiRevocati(dataRicevutaRevocataDa, dataRicevutaRevocataA, dataRevocataDa, dataRevocataA, dimensionePagina, numeroPagina);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento getPagamentoByCRS(java.lang.String codiceCRS) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.getPagamentoByCRS(codiceCRS);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti getPagamentoByDate(java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, boolean senzaRicevuta, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.getPagamentoByDate(dataRichiestaDa, dataRichiestaA, senzaRicevuta, dimensionePagina, numeroPagina);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione ricercaFlussiRendicontazioneScaricati(java.util.Calendar dataScaricoDa, java.util.Calendar dataScaricoA, boolean crsErrore, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.ricercaFlussiRendicontazioneScaricati(dataScaricoDa, dataScaricoA, crsErrore, dimensionePagina, numeroPagina);
  }
  
  public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify ricercaPagamentiNonVerificati(java.util.Calendar dataControlloDa, java.util.Calendar dataControlloA, java.lang.String idFlusso, java.util.Calendar dataRicevutaDa, java.util.Calendar dataRicevutaA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException{
    if (serviziConsultazionePagamentiTelematici == null)
      _initServiziConsultazionePagamentiTelematiciProxy();
    return serviziConsultazionePagamentiTelematici.ricercaPagamentiNonVerificati(dataControlloDa, dataControlloA, idFlusso, dataRicevutaDa, dataRicevutaA, dimensionePagina, numeroPagina);
  }
  
  /**
   * 2023.03.30 DF Da verificare l'implementazione
   */
  public org.apache.axis.client.Call getLastCall() {
      org.apache.axis.client.Call lastCall = null; //super._getCall();
      return lastCall;
  }
}
