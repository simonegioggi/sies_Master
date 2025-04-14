package siap.sius.depositodecreto.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;
import siap.sius.depositodecreto.model.DecretoEventoTenoriFascicoloSiusModel;
import siap.sius.depositodecreto.model.DepositoDecretoEventoModel;
import siap.sius.depositodecreto.model.DepositoDecretoEventoMotivazioniModel;
import siap.sius.depositodecreto.model.DepositoDecretoFascicoloModel;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.generaleprocedimento.model.GPTenoreModel;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;

/**
 * DepositoDecretoController - Classe Controller per DepositoDecreto
 *
 * @version 1.0
 */
public interface IDepositoDecreto {

	public DepositoDecretoModel ExInserisciDepositoDecreto(DepositoDecretoModel aDepositoDecreto)
			throws F3BException;

	public DepositoDecretoModel ExRicercaDepositoDecretoByKey(BigDecimal aKey) throws F3BException;

	public DepositoDecretoModel ExRicercaDepositoDecretoByIdEvento(BigDecimal aIdEvento) throws F3BException;

	public DepositoDecretoEventoMotivazioniModel ExRicercaDecretoEventoMotivazioniInammissibilitaByIdEvento(
			BigDecimal aIdEvento) throws F3BException;

	public DepositoDecretoEventoMotivazioniModel ExRicercaDecretoEventoMotivazioniIncompetenzaByIdEvento(
			BigDecimal aIdEvento) throws F3BException;

	public DepositoDecretoModel ExModificaDepositoDecreto(DepositoDecretoModel aDepositoDecreto)
			throws F3BException;

	public void ExCancellaDepositoDecreto(DepositoDecretoModel aDepDec) throws F3BException;

	public void ExCancellaDepositoDecreto(DepositoDecretoModel aDepDec, Connection aConn) throws Exception;

	public DepositoDecretoEventoModel ExInserisciDecreto(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, String aCodEsitoEvento) throws F3BException;

	public DepositoDecretoEventoModel ExInserisciDecreto(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel) throws F3BException;

	/*
	 * public DepositoDecretoEventoModel ExInserisciDecretoIncompetenza( GPTenoreModel aGpTenoreModel,
	 * DepositoDecretoEventoModel aDepDecrEveModel) throws F3BException;
	 */
	public DepositoDecretoEventoModel ExInserisciDecretoIrreperibilità(GPTenoreModel aGpTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel) throws F3BException;

	public DepositoDecretoEventoModel ExInserisciDecretoRevocaPermesso(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, LicenzaLibAnticipataModel aLicenzaRevocata)
			throws F3BException;

	public ByteArrayOutputStream ExStampaDecreto(FascicoloGPModel aFasc, EventoNotificaModel aEvento,
			UtenteModel aUtenteModel) throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoAllegato(BigDecimal aIdFascicoloSius,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel) throws F3BException;

	public DepositoDecretoModel ExRicercaDepositoDecretoByGenProc(BigDecimal aGenProcKey, String aCodTipoDec)
			throws F3BException;

	public DocumentoAllegatoModel ExInserisciDataDepositoDecreto(FascicoloGPModel aFasGPMod,
			DepositoDecretoModel aDepositoDecreto, EventoNotificaModel aEveNot, String[] lCheck,
			ScadenzarioSiusModel lScadenzarioSiusModPrincipal, ScadenzarioSiusModel lScadenzarioSiusModSecond)
			throws F3BException;

	public boolean ExVerificaEsistenzaDepositoDecretoByIdGenProc(BigDecimal aKey) throws F3BException;

	public boolean ExEsisteDepositoDecretoByGenProcCodEsito(BigDecimal aKey, String aCod) throws F3BException;

	public boolean ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(BigDecimal aKey,
			String aCodTipoDecreto) throws F3BException;

	public boolean ExVerificaEsistenzaDepositoDecretoByIdGenProcCodTipoDec(BigDecimal aKey,
			String[] aCodTipiDecreto) throws F3BException;

	public boolean ExEsisteDepositoDecretoByGenProcEccettoTipi(BigDecimal aKey, String[] aTipiDaEscludere)
			throws F3BException;

	public boolean ExEsisteDepositoDecretoByGenProcDataEmissione(BigDecimal aKey, Date aData)
			throws F3BException;

	public void ProvaInsertSenzaCatch() throws F3BException;

	public void ProvaInsertConCatch() throws F3BException;

	public ByteArrayOutputStream ExStampEmissioneDecreto(EventoModel lEvento, UfficioModel lUfficio,
			UtenteModel aUtenteModel) throws F3BException;

	public DocumentoAllegatoModel ExModificaDataDepositoDecreto(FascicoloGPModel aFasGPMod,
			DepositoDecretoModel aDepositoDecreto, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException;

	@SuppressWarnings("rawtypes")
	public Vector ExRicercaDecretoFascicoloByIdSoggetto(BigDecimal aIdSoggetto, String aTipoDecreto,
			String aTipoLicenza, String aCodUff) throws F3BException;

	public DepositoDecretoFascicoloModel ExRicercaDecretoFascicoloByIdEvento(BigDecimal aIdEventoGenerato)
			throws F3BException;

	public DepositoDecretoModel ExRicercaDepositoDecretoByEveIdEventoNoDescTipoDecreto(BigDecimal aEveKey)
			throws F3BException;

	public ByteArrayOutputStream ExStampaFoglioComp(EventoModel lEvento, String aCodUff,
			UtenteModel aUtenteModel) throws F3BException;

	public DepositoDecretoModel ExRicercaDepositoDecretoByEvento(BigDecimal aEveKey) throws F3BException;

	public DepositoDecretoModel ExRicercaDepositoDecretoByAnnoNumUfficio(
			DepositoDecretoModel aDepositoDecreto) throws F3BException;

	public DepositoDecretoEventoModel ExInserisciDecretoLicenzaoPermesso(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, LicenzaLibAnticipataModel aLicenzaPermessoModel)
			throws F3BException;

	public DepositoDecretoEventoModel ExInserisciDecretoPeriodoAltraSanzione(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, PeriodoAltraSanzioneModel aPeriodoAltraSanzioneModel,
			EsecuzioneSanzioneSostitutivaModel aEsecuzioneSanzioneSostitutivaModel) throws F3BException;

	public DepositoDecretoEventoModel ExInserisciDecretoPeriodoAltraMisura(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, PeriodoAltraMisuraModel aPeriodoAltraMisuraModel,
			EsecuzioneMisuraSicurezzaModel aEsecuzioneMisuraSicurezzaModel) throws F3BException;

	// modifica magistrato per decreto.
	public DepositoDecretoModel ExModificaMagistratoDecreto(DepositoDecretoModel aDepDecrMod)
			throws F3BException;

	// DL 146/2013 - Decreto Revoca L.A.
	public DepositoDecretoEventoModel ExInserisciDecretoRevocaLiberazAnticipata(GPTenoreModel aGPTenoreModel,
			DepositoDecretoEventoModel aDepDecrEveModel, LicenzaPeriodiLibAnticipataModel[] lLicenze,
			LicenzaPeriodiLibAnticipataModel[] lLicenze_spe, LicenzaPeriodiLibAnticipataModel[] lLicenze_int,
			LicenzaLibAnticipataModel lLicenzaC, LicenzaLibAnticipataModel lLicenzaC_SPE,
			LicenzaLibAnticipataModel lLicenzaC_INT, Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato)
			throws F3BException, Exception;

	public Vector<DecretoEventoTenoriFascicoloSiusModel> ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(
			BigDecimal idFascicoloSiep) throws F3BException;

	public DecretoEventoTenoriFascicoloSiusModel ExRicercaEventoProvvDiffSIUSByFascSiepEFascSius(
			BigDecimal idFascicoloSiep, BigDecimal idFascSius, BigDecimal idEveFascSius, BigDecimal idEvento)
			throws F3BException;

}