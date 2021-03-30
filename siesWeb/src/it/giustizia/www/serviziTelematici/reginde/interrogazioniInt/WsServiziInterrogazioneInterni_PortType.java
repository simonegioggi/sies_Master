/**
 * WsServiziInterrogazioneInterni_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.giustizia.www.serviziTelematici.reginde.interrogazioniInt;

public interface WsServiziInterrogazioneInterni_PortType extends java.rmi.Remote {
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Ruoloente[] ricercaEnteComplete(java.lang.String tipo, java.lang.String descrizione, java.lang.String codiceEnte, java.lang.String codiceFiscale, java.lang.String indirizzoPec) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException;
    public it.giustizia.www.serviziTelematici.reginde.interrogazioniExt.Soggetto[] ricercaSoggettoComplete(java.lang.String cognome, java.lang.String nome, java.lang.String codiceFiscale, java.lang.String indirizzo, java.lang.String codiceEnte, java.lang.String orderBy, java.lang.Boolean asc) throws java.rmi.RemoteException, it.giustizia.www.serviziTelematici.reginde.interrogazioniInt.SearchLimitException;
}
