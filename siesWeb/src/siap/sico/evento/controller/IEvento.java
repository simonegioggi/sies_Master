package siap.sico.evento.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.evento.model.EventoVerbaleModel;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sius.avvocatura.model.AvvisiAvvocatoModel;

/**
 * Classe Controller per Evento
 *
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IEvento {

	public EventoNotificaModel ExInserisciEventoNotifica(EventoNotificaModel aEvento) throws F3BException;

	public EventoNotificaModel ExInserisciEventoNotifica(EventoNotificaModel aEvento, Connection aConn)
			throws F3BException;

	// public EventoNotificaModel ExInserisciEventoNotifica(EventoNotificaModel aEvento,
	// PenaResiduaModel aPenaResidua) throws F3BException;

	/**
	 * Ricerca l'evento per Id evento
	 *
	 * @param aKey
	 *            id dell'evento
	 * @return Model dell'evento
	 */
	public EventoModel ExRicercaEventoByKey(BigDecimal aKey) throws F3BException;

	/**
	 * Ricerca l'evento per Id evento X conversione pene pec
	 *
	 * @param aKey
	 *            id dell'evento
	 * @return Model dell'evento
	 */
	public EventoModel ExRicercaEventoByKeyTenore(BigDecimal aKey) throws F3BException;

	public EventoModel ExRicercaEventoEsitoParereInamm(BigDecimal aKey) throws F3BException;

	public EventoModel ExInserisciEvento(EventoModel aEvento) throws F3BException;

	/**
	 * Restituisce l'elenco degli eventi che rispettano le condizioni passate con il model in input ordinati
	 * per data EMISSIONE descrescente. n.b. non tutti i campi del model vengono utilizzati per comporre la
	 * query n.b. se non trava corrispondenza viene rilanciata una eccezione con error code
	 * F3BException.USER_MESSAGE = 'Nessun elemento trovato'
	 *
	 * @param aEvento
	 *            e' il Model di ricerca
	 * @return vettore di EventoModel
	 * @throws F3BException
	 */
	public Vector ExRicercaEvento(EventoModel aEvento) throws F3BException;

	public boolean ExRicercaEventoInoltroDispPM(BigDecimal aIdEvento) throws F3BException;

	public Vector ExRicercaEventoIstanzaRigettata(EventoModel aEvento) throws F3BException;

	public void ExModificaEvento(EventoModel aEvento) throws F3BException;

	public void ExCancellaEvento(EventoModel aEvento) throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaByKey(BigDecimal aEventoKey) throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaByKey(BigDecimal aEventoKey, Connection aConn)
			throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaByKeyForRichiestaAtti(BigDecimal aEventoKey)
			throws F3BException;

	public ByteArrayOutputStream ExStampaDocumento(EventoNotificaModel aEvento, UtenteModel aUtente)
			throws F3BException;

	public EventoModel ExUpdateDocument(EventoModel aEvento) throws F3BException;

	public EventoNotificaModel ExRicercaOENotificheNonRegistratoByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException;

	public Vector ExRicercaEventoNotificaByFascicoloSiep(BigDecimal aFascKey, String[] aTipoEvento)
			throws F3BException;

	public Vector ExRicercaEventoNotificaByFascicoloSiepChiaveUfficio(BigDecimal aFascKey,
			String aChiaveUfficio, String[] aTipoEvento) throws F3BException;

	/*
	 * ExRicercaEventoFascicoloSiepProvvSius Ricerca Lista provvedimenti SIUS per TipoProvv
	 */
	// public Vector ExRicercaEventoFascicoloSiepProvvSius(BigDecimal aFascKey, String[] aTipoProvv) throws
	// F3BException;

	public Vector ExRicercaEventoFascicoloSiepProvvSius(BigDecimal aFascKey) throws F3BException;

	public Vector ExRicercaEventoByFascicoloSius(BigDecimal aFascKey, String aTipoEvento) throws F3BException;

	public Vector ExRicercaEventoByFascEsitoParereInamm(BigDecimal aFascKey, String aTipoEvento)
			throws F3BException;

	public Vector ExRicercaAltroEventoByFascicoloSius(BigDecimal aFascKey, String aTipoEvento)
			throws F3BException;

	public EventoModel ExRicercaEventoTipoProv(EventoModel aEvento) throws F3BException;

	public ByteArrayOutputStream ExGetDocumento(EventoModel aEvento) throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaByKeyForUdienza(BigDecimal aEventoKey)
			throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaByKeyForTrasmAtti(BigDecimal aEventoKey)
			throws F3BException;

	// public InputStream ExGetDocumento2 (EventoModel aEvento ) throws F3BException;
	// public ByteArrayOutputStream ExStampaFissazioneUdienza(EventoNotificaModel aEvento ) throws
	// F3BException;

	// public boolean ExEsisteOrdineEsecuzioneByFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException;

	// public Vector ExRicercaEventoNotificaByFascicoloSiepXStatoEsecuzione(BigDecimal aFascKey )
	// throws F3BException;

	public EventoModel ExRicercaEventoTipoMotProvEveDocReg(EventoModel aModel) throws F3BException;

	public void ExAggiornaDateRicezioneEvento(ArrayList aEventi) throws F3BException;

	public Vector ExRicercaEventoByFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException;

	public EventoModel ExRicercaEventoByFascicoloSiepDecretoSospensione(BigDecimal aIdFascicolo)
			throws F3BException;

	public Vector ExRicercaEventoStatoEsecuzioneByFascicoloSiep(BigDecimal aIdFascicolo) throws F3BException;

	public EventoModel ExRicercaRevocaOrdinanzaAcquisitaDecretoSospensioneByFascicoloSiep(
			BigDecimal aIdFascicolo) throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaCondannatoByKey(BigDecimal aKey) throws F3BException;

	public EventoNotificaModel ExRicercaEventoByKeyPerMotivo(BigDecimal aKey) throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaDifensoreByKey(BigDecimal aKey) throws F3BException;

	public TemplateModel ExRicercaTemplateByCodMotivo(String aCodMotivo) throws F3BException;

	public Vector ExRicercaEventoIstanza(BigDecimal aKey) throws F3BException;

	public EventoNotificaModel ExConfermaTrasferisciIstanza(EventoModel aEvento, NotificaModel aNotifica,
			boolean aStatoProcedimento) throws F3BException;

	public EventoModel ExRicercaEventoIstanzaByKey(BigDecimal aKey) throws F3BException;

	public EventoModel ExRicercaEventoMANonRegistratoByFascicoloSiep(BigDecimal aKey, BigDecimal aEveKey)
			throws F3BException;

	public EventoModel ExRicercaUltimoTipoEventoByIdFascicolo(EventoModel aEvento) throws F3BException;

	/**
	 * Restituisce l'ultimo evento inserito con stato FLAG_REGISTRATO=N o NULL e con le caratteristiche
	 * specificate nel model in input
	 *
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoNonRegistrato(EventoModel aModel) throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaByEveIdEvento(BigDecimal aEventoKey)
			throws F3BException;

	public EventoModel ExRicercaEventoMisuraAlternativaByIdFasSius(BigDecimal aKey) throws F3BException;

	public EventoModel ExRicercaEventoPerMotivo(String[] aMotivo, EventoModel aModel) throws F3BException;

	public EventoModel ExRicercaEventoPerMotivoOrderDesc(String[] aMotivo, EventoModel aModel)
			throws F3BException;

	// STUB 29/09/2005 REWORK STATO ESECUZIONE
	/**
	 * Ricerca l'ultimo evento che ha codice motivo e tipo provvedimento tra quelli passati in input, mentre
	 * le condizione su idFascicolo codTipoEvento e FlagDocumentoRegistrato vengono recuperati del Model.
	 *
	 * @param aMotivo
	 *            vettore contenente l'elenco dei codici motivo su cui effettuare la ricerca
	 * @param aTipoProvv
	 *            vettore contenente l'elenco dei codici tipo provvedimento su cui effettuare la ricerca
	 * @param aModel
	 *            model da cui vengono estratti IdFascicolo, codTipoEvento e flagDocumentoRegistrato
	 * @return
	 * @throws F3BException
	 */
	public EventoModel ExRicercaEventoPerMotivoPerProvv(String[] aMotivo, String[] aTipoProvv,
			EventoModel aModel) throws F3BException;

	public EventoModel ExRicercaUltimoEventoGeneratoByCodUtente(String aCodUtente) throws F3BException;

	public EventoModel ExRicercaUltimoByFasSius(String aKey) throws F3BException;

	// MEV10-s3: aggiunto parametro di passaggio per gestire tipologia ufficio minorenni
	public Vector ExRicercaEventoXCFC(BigDecimal aFascKey, String aTipoEvento, String strCodTipoUfficio)
			throws F3BException;

	public void ExAggiornaValidazione(EventoModel aEvento) throws F3BException;

	public EventoModel ExRicercaEventoByFascicoloSiepOESospensione(BigDecimal aIdFascicolo)
			throws F3BException;

	/**
	 * Ricerca gli eventi eventi in base a idFascicolo, tipoEvento, tipoProvvedimento, motivoProvvedimento
	 * recuperati dal model. Ordinati per data Inserimento decrescente
	 *
	 * @param aModel
	 *            - EventoModel con i parametri per la ricerca
	 * @param lFlagDocReg
	 *            se N o non specificato ricerca gli eventi non validati se S solo quelli non validati
	 * @return Vettore di EventoModel
	 * @throws F3BException
	 *             se errore o nessun elemento trovato
	 */
	public Vector ExRicercaEventoTipoEveTipoProvMot(EventoModel aModel, String lFalgDocReg)
			throws F3BException;

	public EventoModel ExRicercaEventoByDataInserimentoUguale(EventoModel lEveModel) throws F3BException;

	public byte[] ExGetDocPerTrasferimento(BigDecimal aIdEvento) throws F3BException;

	public void ExAnnullaEventoInserisciCampoNota(CampoNotaModel aCampoNota) throws F3BException;

	/**
	 * Ricerca gli Eventi con coppie COD_MOTIVO e COD_TIPO_PROVVEDIMENTO passati attraverso due array di
	 * String. Gli eventi vengono restituiti ordinati per DATA_INSERIMENTO DESC La condzione sullo stato di
	 * validazione viene specificata nel model
	 *
	 * @param aModel
	 * @param aTipoProv
	 * @param aCodMotiv
	 * @return
	 * @throws F3BException
	 */
	public Vector ExRicercaEventoTipoProvTipoMot(EventoModel aModel, String[] aTipoProv, String[] aCodMotiv)
			throws F3BException;

	public EventoModel ExRicercaEventoUnicoTipoProvTipoMot(EventoModel aModel, String[] aTipoProv,
			String[] aCodMotiv) throws F3BException;

	public List ExRicercaEventiNOTAnnullati(BigDecimal aIdFascicolo, String[] aMotivo, String aTipoProvv,
			String aTipoEve) throws F3BException;

	/**
	 * Ricerca gli Ordini di Esecuzione emessi per la richiesta di restituzione. Gli OE sono ordinati per
	 * data_emissione decrescente
	 *
	 * @param aFascKey
	 * @return Vettore di EventoModel
	 * @throws F3BException
	 */
	public Vector ExRicercaOEPerRestituzioneByFascicolo(BigDecimal aFascKey) throws F3BException;

	public Vector ExRicercaProvvedimentiDeposito(EventoModel aEvento) throws F3BException;

	/**
	 * ExRicercaEventiPerMotivoTipoProvv
	 *
	 * @param aMotivo
	 * @param aModel
	 * @return
	 * @throws F3BException
	 */

	public Vector ExRicercaEventiPerMotivoTipoProvv(String[] aMotivo, String[] aTipo, EventoModel aModel)
			throws F3BException;

	/**
	 * ExModificaEventoNotifiche
	 *
	 * @param aEvento
	 * @param aNotMod
	 * @return
	 * @throws F3BException
	 */
	public void ExModificaEventoNotifiche(EventoModel aEvento, NotificaModel aNotMod) throws F3BException;

	/**
	 * ExConfermaTrasmissione
	 *
	 * @param aEvento
	 * @param aNotifica
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoNotificaModel ExConfermaTrasmissione(EventoModel aEvento, NotificaModel aNotifica,
			String aCodStatoProcedimento) throws F3BException;

	public void ExModificaEventoNotificheCampoNote(EventoModel aEvento, NotificaModel aNotMod,
			CampoNotaModel aCampoNota) throws F3BException;

	public void ExModificaEventoTrasmissioneCompetenza(EventoModel aEvento, FascicoloSiepModel aFas,
			String aTipoScadenzario) throws F3BException;

	/**
	 * ExUpdateValidaProvvedimento Valida provvedimento in Conversione Pene Pec
	 *
	 * @param aEvento
	 * @param aPos
	 * @param StatoPro
	 * @return EventoModel
	 * @throws F3BException
	 */
	public EventoModel ExUpdateValidaProvvedimento(EventoModel aEvento, String StatoPro) throws F3BException;

	public boolean ExRicercaFoglioComplementare(BigDecimal aKey) throws F3BException;

	public void ExAnnullaEventoInserisciCampoNotaFC(CampoNotaModel aCampoNota) throws F3BException;

	public boolean ExRicercaFoglioComplementareTrasmesso(BigDecimal aKey) throws F3BException;

	public EventoVerbaleModel ExRicercaEventoVerbaleByIdEve(BigDecimal aEveKey) throws F3BException;

	public Date ExRicercaDataEmissioneCertCasellario(BigDecimal aFascKey, String aChiaveUfficio,
			String aTipoEvento, String codMotivo) throws F3BException;

	// MEV 16: aggiunto metodo di controllo esistenza FC
	public boolean existsReallyFC(BigDecimal idEvento) throws F3BException;

	// 07/2015 - ActStampaComunicazionePoliziaEsecMs - Cerca tutti gli eventi per idFascicoloSiep Order desc
	public Vector ExRicercaTuttiEventiValidatiByFascicoloSiepDesc(BigDecimal aEveKey) throws F3BException;

	public void ExModificaEventoProvvedimentoSIGE(EventoModel aEvento,
			ProvvedimentoSigeEventoModel aProvvSIGEMod) throws F3BException;

	// MEV_AVVOCATURA
	public EventoModel ExUpdateDocument(EventoModel aEvento, Vector<AvvisiAvvocatoModel> lAvvvisiAvvocato)
			throws F3BException;

	// MEV 26 CUMULO
	public Vector ExRicercaEventoByTipoEveKeyIstruttoriaCumulo(BigDecimal aIstruCumKey, String[] aTipoEvento)
			throws F3BException;

	// MEV 16 CUMULO: aggiunto metodo di controllo
	public boolean isCumulo(String lCodMotivo, BigDecimal idEvento) throws F3BException;

	// intervento per MEV 64- AVVOCATURA (anche in stampa devono apparire solo le ordinanze/decreti
	// depositati)
	public Vector ExRicercaProvvedimentiConDataDeposito(EventoModel aEvento) throws F3BException;
	
	// MEV_2023-33: aggiunto metodo di modifica
	public void ExModificaEventoNotifiche(EventoNotificaModel enm) throws F3BException;

}