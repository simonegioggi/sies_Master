package siap.sius.depositoordinanzapc.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriFascicoloSiusModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriGProcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.penapecuniaria.model.RichiesteConversioniPerOrdinanzaModel;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: DepositoOrdinanzaPcController
 * </p>
 * <p>
 * Description: Classe Controller per DepositoOrdinanzaPc
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
public interface IDepositoOrdinanzaPc {

	public DepositoOrdinanzaPcModel ExInserisciDepositoOrdinanzaPc(
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc) throws F3BException;

	public OrdinanzaEventoTenoriModel ExInserisciEventoDepositoOrdinanzaPc(OrdinanzaEventoTenoriModel lModel)
			throws F3BException;

	public GPTenoreModel ExInserisciEmissioneOrdinanza(GPTenoreModel aDepositoOrdinanzaPc)
			throws F3BException;

	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanza(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori, BigDecimal IdFascicoloOrigine)
			throws F3BException;

	public Vector ExRicercaDepositoOrdinanzaPc(DepositoOrdinanzaPcModel aDepositoOrdinanzaPc)
			throws F3BException;

	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByKey(BigDecimal aKey) throws F3BException;

	public DepositoOrdinanzaPcModel ExRicercaOrdinanzaRimessioneAttiPcByKeyPerUpdate(BigDecimal aKey)
			throws F3BException;

	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd(BigDecimal aKey,
			String aCodTipoOrd) throws F3BException;

	public DepositoOrdinanzaPcModel ExModificaDepositoOrdinanzaPc(
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc) throws F3BException;

	/*
	 * public void ExCancellaDepositoOrdinanzaPc (DepositoOrdinanzaPcModel aDepositoOrdinanzaPc ) throws
	 * F3BException;
	 * 
	 * public void ExCancellaDepositoOrdinanzaPcByGenProc (BigDecimal aIdGenProc ) throws F3BException;
	 */
	public void ExCancellaDepositoOrdinanza(DepositoOrdinanzaPcModel aDepOrd) throws F3BException;

	public void ExCancellaDepositoOrdinanza(DepositoOrdinanzaPcModel aDepOrd, Connection aConn)
			throws F3BException;

	public void ExCancellaRimessioneAtti(DepositoOrdinanzaPcModel aDepOrd) throws F3BException;

	public void ExCancellaRimessioneAtti(DepositoOrdinanzaPcModel aDepOrd, Connection aConn)
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumento(FascicoloGPModel aFasc, EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoModello(FascicoloGPModel aFasc,
			EventoNotificaModel aEvento, UtenteModel aUtenteModel) throws F3BException;

	public DocumentoAllegatoModel ExInserisciDataDepositoOrdinanza(FascicoloGPModel aFasGPMod,
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc, EventoNotificaModel aEveNot, String[] lCheck,
			ScadenzarioSiusModel lScadenzarioSiusModPrincipal, ScadenzarioSiusModel lScadenzarioSiusModSecond)
			throws F3BException;

	/*
	 * public OrdinanzaEventoTenoriModel ExRicercaDepositoOrdinanzaPcTenoriByGenProc(BigDecimal aKey) throws
	 * F3BException;
	 */
	public boolean ExVerificaEsistenzaDepositoOrdinanzaByIdGenProc(BigDecimal aKey, Date aData)
			throws F3BException;

	public boolean ExEsisteDepositoOrdinanzaByGenProcEccettoTipi(BigDecimal aKey, String[] aTipiDaEscludere)
			throws F3BException;

	public OrdinanzaEventoTenoriPrescrizioniModel ExRicercaOrdinanzaEventoTenoriPrescrizioniByIdEvento(
			BigDecimal aIdEvento) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoAllegato(BigDecimal aIdFascicoloSius,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel) throws F3BException;

	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByEvento(BigDecimal aEveKey)
			throws F3BException;

	// AMBROS MIS SIC
	public Vector ExRicercaDepositoOrdinanzaPcEventoByFascicoloSiep(BigDecimal aFascSiepKey)
			throws F3BException;

	public Vector ExRicercaEventoProvvediementiArchiviazioneSIUSByFascicoloSiep(BigDecimal aFascSiepKey)
			throws F3BException;

	// >> End MIS SIC <<

	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanza(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori) throws F3BException;

	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaLibAnt(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori, LicenzaPeriodiLibAnticipataModel[] aLicenze)
			throws F3BException;

	// 10-03-2014 Nuova Ordinanza L.A. - Decreto legge 146/2013
	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaLibAnt(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori, LicenzaPeriodiLibAnticipataModel[] aLicenze,
			LicenzaPeriodiLibAnticipataModel[] aLicenze_spe, LicenzaPeriodiLibAnticipataModel[] aLicenze_int,
			LicenzaLibAnticipataModel aLicenzaC, LicenzaLibAnticipataModel aLicenzaC_SPE,
			LicenzaLibAnticipataModel aLicenzaC_INT) throws F3BException;

	public DocumentoAllegatoModel ExModificaDataDepositoOrdinanza(FascicoloGPModel aFasGPMod,
			DepositoOrdinanzaPcModel aDepositoOrdinanza, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException;

	public ByteArrayOutputStream ExStampEmissioneOrdinanza(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExStampaFoglioComp(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public void ExAggiornaTenoriEvento(TenoreModel[] aTenori, EventoModel aEvento, BigDecimal aIdOrdinanza,
			BigDecimal aIdDecreto) throws F3BException;

	// 10102014 - DL 92 2014 Violazione CEDU
	public void ExAggiornaTenoriEventoPeriodiLA(TenoreModel[] aTenori, EventoModel aEvento,
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc, DepositoDecretoModel aDepositoDecreto,
			LicenzaPeriodiLibAnticipataModel[] aLicenze) throws F3BException;

	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByAnnoNumUfficio(
			DepositoOrdinanzaPcModel aDepositoOrdinanzaPc) throws F3BException;

	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaPeriodoAltraSanzioneModificaESS(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori,
			PeriodoAltraSanzioneModel aPeriodoAltraSanzioneModel,
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutivaModel) throws F3BException;

	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaPeriodoAltraMisuraModificaEMS(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori,
			PeriodoAltraMisuraModel aPeriodoAltraMisuraModel,
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezzaModel) throws F3BException;

	public OrdinanzaEventoTenoriGProcModel ExInserisciOrdinanzaConversioneRateizzazionePP(
			OrdinanzaEventoTenoriGProcModel aGProcOrdEveTenori,
			RichiesteConversioniPerOrdinanzaModel aRicConvMod) throws F3BException;

	// Definizione del metodo afferente alla modifica del Magistrato all'ordinanza.
	public DepositoOrdinanzaPcModel ExModificaMagistratoOrdinanza(DepositoOrdinanzaPcModel aDepOrdPcMod)
			throws F3BException;

	public void ExAggiornaTenoriEventoDOS(TenoreModel[] aTenori, EventoModel aEvento,
			BigDecimal aIdOrdinanza, BigDecimal aIdDecreto, BigDecimal aIdSentenza) throws F3BException;

	/*
	 * 13-11-2014 Misure Sicurezza Fuori Sentenza Query usata nella Gestione MISURE SICUREZZA dalla parte SIEP
	 * Esegue la CONTA/RICERCA dei provvedimenti SIUS ordinanza per DATA
	 */
	// MEV_39: aggiunto parametro di passaggio
	public BigDecimal ExCountProvvedimentiSoggettoPerMisuraFuoriSentenza(Date aData_inizio, Date aData_fine,
			Boolean aElaborati, String codUfficio) throws F3BException;

	// MEV_39: aggiunto parametro di passaggio
	public Vector ExRicercaProvvedimentiSoggettoPerMisuraFuoriSentenza(Date aData_inizio, Date aData_fine,
			Boolean aElaborati, String codUfficio, int aPage) throws F3BException;

	/*
	 * ISSUE MEV : aggiunti metodi di ricerca provvedimenti differimento SIUS
	 * Numero MEV : 39
	 * Autore : Gioggi
	 * Data : 24/feb/2017
	 * Branch : MEV_39
	 */
	public Vector ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(BigDecimal idFascicoloSiep)
			throws F3BException;

	public OrdinanzaEventoTenoriFascicoloSiusModel ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(
			BigDecimal idFascicoloSiep, BigDecimal idFascSius, BigDecimal idEveFascSius, BigDecimal idEvento)
			throws F3BException;

	public DepositoOrdinanzaPcModel ExRicercaDepositoOrdinanzaPcByGenProc(BigDecimal idGeneraleProcedimento)
			throws F3BException;
	// ***** FINE INTERVENTO MEV_39 *****//

}