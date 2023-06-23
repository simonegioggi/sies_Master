/**
 * ServiziInvioPagamentiTelematici.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public interface ServiziInvioPagamentiTelematici extends java.rmi.Remote {
    public byte[] downloadAvviso(java.lang.String numeroAvviso) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public void eliminaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoGeneraAvviso generaAvviso(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public byte[] generaRPT(it.giustizia.www.serviziTelematici.serviziGenerici.RichiestaPagamentoTelematico richiestaPagamentoTelematico) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public java.lang.String inviaCarrelloRPT(java.lang.String codiceFiscale, java.lang.String[] crs, boolean areaPubblica) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public java.lang.String inviaER(java.lang.String idRevoca, java.util.Calendar dataRevoca) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public it.giustizia.www.serviziTelematici.pstbe.gestione.CausalePagamentoNEPConf[] listaConfNEP(java.lang.String codiceUfficioNEP, java.lang.String tipologia) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.pstbe.gestione.DatiRiscossione[] listaDatiRiscossione(java.lang.String idtipologia, java.lang.String codice) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.EsitoRispostaWisp registraRispostaWisp(java.lang.String idSession, java.lang.String esito) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public java.lang.String[] richiediCopiaRT(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public java.lang.String[] verificaRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
}
