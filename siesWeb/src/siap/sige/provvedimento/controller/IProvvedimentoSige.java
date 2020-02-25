package siap.sige.provvedimento.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.fogliocomplementare.model.FoglioComplementareModel;
import siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: ProvvedimentoSigeController
 * </p>
 * <p>
 * Description: Classe Controller per ProvvedimentoSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IProvvedimentoSige {

	public void ExInserisciProvvedimentoSige(ProvvedimentoSigeModel aProvvedimentoSige) throws F3BException;

	public void ExModificaProvvedimentoSige(ProvvedimentoSigeModel aProvvedimentoSige,
			MotivazioneProvvedimentoSigeModel[] lMotivazioni) throws F3BException;

	public ProvvedimentoSigeEventoModel ExRicercaProvvedimentoDefinitorioByIdFascicolo(BigDecimal aKey)
			throws F3BException;

	public ProvvedimentoSigeEventoModel ExInserisciProvvedimento(ProvvedimentoSigeEventoModel lProvEveModel,
			Vector lTenori, String aTipoGiudizio) throws Exception;

	public ProvvedimentoSigeEventoModel ExInserisciProvvEveNotifica(
			ProvvedimentoSigeEventoModel lProvEveModel, EventoNotificaModel aEventoNotifiche, Vector lTenori,
			String aCodTipoGiudizio) throws Exception;

	public ProvvedimentoSigeModel ExRicercaProvDefinitorioByFasc(BigDecimal aIdFasSige) throws F3BException;

	public ProvvedimentoSigeEventoModel ExRicercaProvvedimentoById(BigDecimal aIdProv) throws F3BException;

	public ByteArrayOutputStream ExStampaProvvedimento(EventoModel lEvento, BigDecimal aIdFasSige,
			String lTipoUfficio, UtenteModel aUtenteModel) throws F3BException;

	public void ExCancellaProvvedimentoSige(ProvvedimentoSigeModel aProvModel) throws F3BException;

	public Vector ExRicercaProvvedimentoDaDepositare(ProvvedimentoSigeModel aProvvedimentoSige)
			throws F3BException;

	public DocumentoAllegatoModel ExInserisciDataDeposito(FascicoloSigeModel aFasSige,
			ProvvedimentoSigeEventoModel aProvvedimento, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException;

	public ProvvedimentoSigeEventoModel ExRicercaProvvedimentoByIdEvento(BigDecimal aIdEvento)
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumentoAllegato(BigDecimal aIdFascicolo,
			DocumentoAllegatoModel aDAMod, String aCodUff, UtenteModel aUtenteModel, BigDecimal idEvento)
			throws F3BException;

	public void ExCancellaProvvedimentoSige(ProvvedimentoSigeModel aProvvedimento, Connection aConn)
			throws Exception;

	public ProvvedimentoSigeModel ExInserisciEventoNotificaProv(EventoNotificaModel aEventoNotifiche,
			ProvvedimentoSigeModel aProvvedimento) throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiSigePerIdFasSige(BigDecimal aIdFasSige)
			throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiSigePerIdFasSigePerIdSoggetto(
			BigDecimal aIdFasSige, String codTipoProvvedimento) throws F3BException;

	public Vector ExRicercaOrdinanzaSospensioneSigeByProvvId(BigDecimal aProvvId) throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvSigePerIdFasSigeTipiProvv(
			BigDecimal aIdFasSige, String aTipiProvv) throws F3BException;

	public Vector ExRicercaProvvSigePerIdFasSigeTipiProvvProvvSige(BigDecimal aIdFasSige, String aTipiProvv,
			String aCodTipoProvvSige) throws F3BException;

//	public void ExAnnullaProvvedimento(CampoNotaModel aCampoNota, BigDecimal aIdFascicoloSige,
//			ProvvedimentoSigeModel aProvvSige) throws F3BException;
	
	public ProvvedimentoSigeModel ExRicercaProvedimentoByIdTenore(BigDecimal aIdTenoreSige)
			throws F3BException;

	public ProvvedimentoSigeEventoModel ExInserisciProvvedimento(ProvvedimentoSigeEventoModel lProvEveModel,
			Vector lTenori, String aCodTipoGiudizio, MotivazioneProvvedimentoSigeModel[] lMotivazioni)
			throws Exception;

	public ProvvedimentoSigeEventoModel ExInserisciProvvSospensione(
			ProvvedimentoSigeEventoModel lProvEveModel, Vector lTenori, String aCodTipoGiudizio,
			MotivazioneProvvedimentoSigeModel[] lMotivazioni) throws Exception;

	public DocumentoAllegatoModel ExModificaDataDeposito(ProvvedimentoSigeModel aProvvedimento,
			EventoNotificaModel aEveNot, String[] lCheck, FascicoloSigeModel aFasSige) throws F3BException;

	public void ExModificaProvvSigeEveNotifica(ProvvedimentoSigeModel aProvvedimento,
			EventoNotificaModel aEveNot, String[] lCheck) throws F3BException;

	public Vector ExRicercaProvvSigeXCFC(BigDecimal aIdFasSige, String aTipiProvv, String aTipoEvento)
			throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaAltriProvvByFascicoloSige(BigDecimal aFascKey,
			String aTipoEvento) throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaDecretiDaDepositare(
			ProvvedimentoSigeModel aProvvedimentoSige) throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaOrdinanzeDaDepositare(
			ProvvedimentoSigeModel aProvvedimentoSige) throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvSigeFCByIdFascicolo(BigDecimal idFascicolo)
			throws F3BException;

	public Vector<FoglioComplementareModel> ExRicercaFogliComplementariByFascicolo(BigDecimal idFascicolo,
			String lTipiProvv) throws F3BException;

	public BigDecimal ExCountOpposizioniAccolteByIdProvvedimento(BigDecimal idProvedimento)
			throws F3BException;

	public DocumentoAllegatoModel ExInserisciDataDepositoFissazioneUdienza(FascicoloSigeModel aFasSige,
			ProvvedimentoSigeEventoModel aProvvedimento, EventoNotificaModel aEveNot, String[] lCheck)
			throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiUdienzeByIdFascicolo(
			BigDecimal idFascicolo) throws F3BException;

	public ProvvedimentoSigeModel ExRicercaOrdinanzaRinvioUdienzaDaValidareByFascicolo(BigDecimal idFascicolo)
			throws F3BException;

	public Vector<ProvvedimentoSigeEventoModel> ExRicercaProvvedimentiSigePerOpposizioni(BigDecimal aIdFasSige)
			throws F3BException;

	public ProvvedimentoSigeEventoModel ExModificaDecretoInamissibilita(
			ProvvedimentoSigeEventoModel lProvEveModel, Vector<TenoreSigeModel> lTenori,
			String aCodTipoGiudizio, MotivazioneProvvedimentoSigeModel[] lMotivazioni) throws Exception;
	
	// @emma (aggiungo questo  metodo per gestire  la modifica dello stato del fascicolo in caso di annullamento
	// di un provvedimento con impugnazioni)
	public void ExAnnullaProvvedimento(CampoNotaModel aCampoNota, BigDecimal aIdFascicoloSige,
			ProvvedimentoSigeEventoModel aProvvSige, String codiceStatoFascicolo) throws F3BException;

}