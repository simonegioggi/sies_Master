package siap.sico.libertaanticipata.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoLicenzePeriodiModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: LicenzaLibanticipataController
 * </p>
 * <p>
 * Description: Classe Controller per LicenzaLibanticipata
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface ILicenzaPeriodiLibAnticipata {

	public LicenzaPeriodiLibAnticipataModel[] ExInserisciLicenzeLibanticipata(
			LicenzaPeriodiLibAnticipataModel[] aLicenze, Connection aConn) throws Exception;

	public LicenzaPeriodiLibAnticipataModel[] ExInserisciLicenzeLibanticipata(
			LicenzaPeriodiLibAnticipataModel[] aLicenze, BigDecimal aIdEvento) throws Exception;

	public LicenzaPeriodiLibAnticipataModel[] ExInserisciLicenzeLibanticipata(
			LicenzaPeriodiLibAnticipataModel[] aLicenze, OrdinanzaEventoTenoriModel aOrdEveTenMod)
			throws Exception;

	// NUOVA ORDINANZA L.A. - Decreto 2013/46
	public void ExInserisciNewLicenzeLibanticipata(LicenzaPeriodiLibAnticipataModel[] aLicenze,
			LicenzaPeriodiLibAnticipataModel[] aLicenze_spe, LicenzaPeriodiLibAnticipataModel[] aLicenze_int,
			LicenzaLibAnticipataModel aLicenzaC, LicenzaLibAnticipataModel aLicenzaC_SPE,
			LicenzaLibAnticipataModel aLicenzaC_INT, BigDecimal aIdEvento) throws Exception;

	public EventoModel ExInserisciLicenzeLibanticipataSIEP(OrdinanzaEventoTenoriModel aOrdEveTenMod,
			LicenzaPeriodiLibAnticipataModel[] aLicenze, LicenzaPeriodiLibAnticipataModel[] aLicenze_spe,
			LicenzaPeriodiLibAnticipataModel[] aLicenze_int) throws Exception;

	// End NUOVA ORDINANZA L.A.

	public Vector ExRicercaLicenzeLibanticipataByEve(BigDecimal aIdEvento) throws F3BException;

	public BigDecimal ExInserisciRidimLibanticipata(EventoModel aEvento, CampoNotaModel aCampoNote,
			LicenzaPeriodiLibAnticipataModel aLicenzaLA, LicenzaPeriodiLibAnticipataModel aLicenzaLAS,
			LicenzaPeriodiLibAnticipataModel aLicenzaLAI) throws Exception;

	/**
	 * Ricerca le Liberazioni Anticipate associate ad un evento
	 * 
	 * @param aIdEvento
	 * @return
	 * @throws F3BException
	 *             se nessun elemento trovato o sql exception
	 */
	public Vector ExRicercaLicenzeByEve(BigDecimal aIdEvento) throws F3BException;

	// 20/05/2014 Nuova L.A. DL 146/2013

	/**
	 * Ricerca un particolare Tipo di Liberazioni Anticipate (L.A., L.A. SPECIALE, o INTEGRAZIONE L.A.)
	 * associate ad un evento; Il tipo L.A. viene passato tra in parametri
	 * 
	 * @param aIdEvento,
	 *            Tipo L.A. ("LA" / "LS" / "LI" )
	 * @return
	 * @throws F3BException
	 *             se nessun elemento trovato o sql exception
	 */
	public Vector ExRicercaLicenzeByEve(BigDecimal aIdEvento, String aTipoLA) throws F3BException;

	// End DL 146

	public void ExCancellaLicenzeLibanticipataByEve(BigDecimal aIdEvento) throws F3BException;

	// 10-03-2014 Nuova Ordinanza L.A. - Decreto legge 146/2013
	public LicenzaLibAnticipataModel ExInserisciLicenzaLibanticipata(
			LicenzaLibAnticipataModel aLicenzaLibanticipata, Connection aConn) throws F3BException;

	public LicenzaLibAnticipataModel ExInserisciLicenzaLibanticipata(
			LicenzaLibAnticipataModel aLicenzaLibanticipata) throws F3BException;

	public LicenzaLibAnticipataModel ExRicercaLicenzaLibanticipataUltimaByIDFascicoloSIEP(BigDecimal aKey)
			throws F3BException;

	public LicenzaLibAnticipataModel ExModificaLicenzaLibanticipata(
			LicenzaLibAnticipataModel aLicenzaLibanticipata) throws F3BException;

	public int ExTotalePeriodiConcessiElaboratiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException;

	public int ExTotalePeriodiConcessiElaboratiByEveIdEvento(BigDecimal aEveIdEvento) throws F3BException;

	public int ExTotalePeriodiConcessiNonElaboratiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException;

	public int ExTotalePeriodiConcessiComputatiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException;

	/*****************************************************************************
	 * Restituisce il totale giorni di licenze di Liberazione Anticipata Concessi e con flag elaborato (null,
	 * E o N) (non computati in una pena residua validata)
	 *
	 * @param aIdFascicoloSiep
	 * @return
	 * @throws F3BException
	 */
	public int ExTotalePeriodiConcessiNonValidatiByIdFascicoloSiep(BigDecimal aIdFascicoloSiep)
			throws F3BException;

	public LicenzaLibAnticipataModel ExRicercaLicenzaLibanticipataConcessayIDFascicoloSIEP(BigDecimal aKey,
			String aFlagElaborato) throws F3BException;

	/**
	 * Aggiorna tutte le LA associate al Fascicolo con FLAG_ELABORATO = aFlagElaboratoVecchio modifcandolo in
	 * aFlagElaboratoNuovo
	 * 
	 * @param aKeyFascicolo
	 * @param aFlagElaboratoVecchio
	 * @param aFlagElaboratoNuovo
	 * @throws F3BException
	 */
	public void ExModificaFlagElaboratoLicenzaLibanticipataByIdFascicoloSiep(BigDecimal aKeyFascicolo,
			String aFlagElaboratoVecchio, String aFlagElaboratoNuovo) throws F3BException;

	public LicenzaLibAnticipataModel ExRicercaLicenzaLibanticipataByKey(BigDecimal aKey) throws F3BException;

	public Vector ExRicercaLicenzaLibanticipataNonConcesseByIDFascicoloSIEP(BigDecimal aKey,
			BigDecimal aKeyEvento) throws F3BException;

	public List ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException;

	public EventoModel ExUpdateValidaComunicazioneLA(EventoModel aEvento) throws F3BException;

	public void ExCancellaFungibilitaLicenzeLibanticipata(PenaResiduaModel aPenaResidua,
			BigDecimal aIdFungibilita) throws F3BException;

	public Vector ExRicercaLicenzeLibanticipataByIdFascicoloSIEP(BigDecimal aIdFascicolo) throws F3BException;

	public String ExInserisciLicenzePeriodiLibAnticipataWithoutSequence(ArrayList aLicenzePeriodi,
			Connection lConn) throws F3BException;

	public List ExRicercaLicenzaScomputiConcessiDepositatiByIdFascicoloSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException;

	public Vector ExRicercaLAPeriodiConcessiDepositatiByIdFascSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException;

	public Vector ExRicercaLAPeriodiRevocatiDepositatiByIdFascSIEP(BigDecimal aIdFascicolo,
			String aFlagElaborato) throws F3BException;

	public ArrayList ExRicercaPeriodiByIdLA(BigDecimal aIdLA) throws F3BException;

	public BigDecimal ExInserisciRidimLibanticipataSorv(EventoModel aEvento, EventoModel aEventoAltroUff,
			CampoNotaModel aCampoNote,
			// 20/05/2014 Nuova L.A. - DL 146/2013 - gestione L.A. SPECIALE e L.A. INTEGRAZIONE
			LicenzaPeriodiLibAnticipataModel aLicenzaLA, LicenzaPeriodiLibAnticipataModel aLicenzaLASPE,
			LicenzaPeriodiLibAnticipataModel aLicenzaLAINT) throws Exception;

	public EventoModel ExInserisciRimediRisarcitoriSIEP(EventoModel aEvento,
			Vector<LicenzaPeriodiLibAnticipataModel> aLicenzeEPeriodi, DepositoDecretoModel aDepDecMod,
			DepositoOrdinanzaPcModel aDepOrdMod, TenoreModel aTenoreModel) throws Exception;

	public Vector<EventoLicenzePeriodiModel> ExRicercaRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP(
			BigDecimal aIdFascicolo) throws F3BException;

	public EventoModel ExUpdateValidaComunicazioneRimediRisarcitori(EventoModel aEvento) throws F3BException;

	public EventoModel ExUpdateValidaComunicazioneReclamoRimediRisarcitori(EventoModel aEvento)
			throws F3BException;

	public EventoModel ExUpdateValidaOSRimediRisarcitori(EventoModel aEvento) throws F3BException;

	public Vector<EventoLicenzePeriodiModel> ExRicercaReclamoRimediRisarcitoriConcessiDepositatiByIdFascicoloSIEP(
			BigDecimal aIdFascicolo) throws F3BException;

}