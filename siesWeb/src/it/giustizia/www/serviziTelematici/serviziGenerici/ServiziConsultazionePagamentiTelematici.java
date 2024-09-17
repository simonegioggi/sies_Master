/**
 * ServiziConsultazionePagamentiTelematici.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.serviziGenerici;

public interface ServiziConsultazionePagamentiTelematici extends java.rmi.Remote {
    public java.lang.String downloadPDFRicevuta(java.lang.String idPagamento, boolean bollo) throws java.rmi.RemoteException;
    public byte[] downloadRicevuta(java.lang.String codiceCRS, boolean originale) throws java.rmi.RemoteException;
    public byte[] downloadRichiesta(java.lang.String codiceCRS) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca elencoPagamenti(java.lang.String codiceCRS, java.lang.String tipologia, java.lang.String codiceFiscale, java.lang.String codiceDistretto, java.lang.String causale, java.lang.String stato, java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, int dimensionePagina, int numeroPagina, java.util.Calendar dataRicevutaDa, java.util.Calendar dataRicevutaA) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicerca elencoPagamentiRevocati(java.util.Calendar dataRicevutaRevocataDa, java.util.Calendar dataRicevutaRevocataA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti getAPAinKO(java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, java.lang.String codiceDistretto, java.lang.String codiceUfficio, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.InfoPagamentoRendicontato getInfoPagamentoRendicontato(java.lang.String iuv, java.lang.String codiceUfficio) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRiversamenti getPagamentiByFlusso(java.lang.String codUffNep, java.lang.String idFlusso, java.util.Calendar dataOraFlusso, java.lang.String idPSP) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.serviziGenerici.ServiziPagamentiException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRevocati getPagamentiRevocati(java.util.Calendar dataRicevutaRevocataDa, java.util.Calendar dataRicevutaRevocataA, java.util.Calendar dataRevocataDa, java.util.Calendar dataRevocataA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.StatoRichiestaPagamento getPagamentoByCRS(java.lang.String codiceCRS) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagamenti getPagamentoByDate(java.util.Calendar dataRichiestaDa, java.util.Calendar dataRichiestaA, boolean senzaRicevuta, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaRendicontazione ricercaFlussiRendicontazioneScaricati(java.util.Calendar dataScaricoDa, java.util.Calendar dataScaricoA, boolean crsErrore, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException;
    public it.giustizia.www.serviziTelematici.serviziGenerici.RisultatoRicercaPagaNoVerify ricercaPagamentiNonVerificati(java.util.Calendar dataControlloDa, java.util.Calendar dataControlloA, java.lang.String idFlusso, java.util.Calendar dataRicevutaDa, java.util.Calendar dataRicevutaA, int dimensionePagina, int numeroPagina) throws java.rmi.RemoteException;

    public org.apache.axis.client.Call getLastCall();
}
