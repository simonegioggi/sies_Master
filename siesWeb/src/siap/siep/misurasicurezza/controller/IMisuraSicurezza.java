package siap.siep.misurasicurezza.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sollecitoesitotrasmissione.model.SollecitoEsitoTrasmissioneModel;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: MisuraSicurezzaController
 * </p>
 * <p>
 * Description: Classe Controller per MisuraSicurezza
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
public interface IMisuraSicurezza {

	public MisuraSicurezzaModel ExInserisciMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza)
			throws F3BException;

	public Vector ExRicercaMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza) throws F3BException;

	public Vector ExRicercaMisuraSicurezzaEstesa(MisuraSicurezzaModel aMisuraSicurezza) throws F3BException;

	public List ExRicercaFascicoliMisuraSicurezza(String aCodUfficioUtenteConnesso, int aPage)
			throws F3BException;

	public BigDecimal ExGetCountFascicoliMisuraSicurezza(String aCodUfficioUtenteConnesso)
			throws F3BException;

	public MisuraSicurezzaModel ExRicercaMisuraSicurezzaByKey(BigDecimal aKey) throws F3BException;

	// 06-03-2015 - Ritorna un Vector e non più il Model
	// public MisuraSicurezzaModel ExRicercaMisuraSicurezzaByEventoKey (BigDecimal aEveKey)
	public Vector ExRicercaMisuraSicurezzaByEventoKey(BigDecimal aEveKey) throws F3BException;

	// 06-03-2015

	public MisuraSicurezzaModel ExModificaMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza)
			throws F3BException;

	public void ExCancellaMisuraSicurezza(MisuraSicurezzaModel aMisuraSicurezza) throws F3BException;

	public List ExRicercaMisuraSicurezzaByIdFascicolo(BigDecimal aKey) throws F3BException;

	// 10-11-2015 - Ricerca che NON Rialncia l'eccezione
	public List ExRicercaMisuraSicurezzaByIdFascicoloNONRilancia(BigDecimal aKey) throws F3BException;

	public String ExInserisciMisuraSicurezzaWithoutSequence(ArrayList aMisure, Connection lConn)
			throws F3BException;

	public String ExInserisciFascMStoFascSIEPWithoutSequence(ArrayList aRelazioneMisure, Connection lConn)
			throws F3BException;

	public EventoModel ExUpdateValidaTrasmissioneCompetenza(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public Vector<FascMsToFascSiepModel> ExRicercaFascMsToFascSiepByFascSiep(
			FascMsToFascSiepModel aFascMsToFascSiepModel) throws F3BException;

	public BigDecimal ExCountRicercaMessaggi(
			String aDeliveryMode,
			String aCodTipoMessaggio
			// , String aCodTipoOperazione
			,
			Vector<String> aListaTipoOperazione
			// , String aCodEsito
			, Vector<String> aListaEsiti, String aFlagVisto, BigDecimal aChiaveAnnoSiep,
			BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep, String aCodUfficioMitt,
			String aCodUfficioDest, Date aDataTrasmissioneDal, Date aDataTrasmissioneAl) throws Exception;

	public Vector<MessaggioModel> ExRicercaMessaggi(
			String aDeliveryMode,
			String aCodTipoMessaggio
			// , String aCodTipoOperazione
			,
			Vector<String> aListaTipoOperazione
			// , String aCodEsito
			, Vector<String> aListaEsiti, String aFlagVisto, BigDecimal aChiaveAnnoSiep,
			BigDecimal aChiaveProgrSiep, String aChiaveUfficioSiep, String aCodUfficioMitt,
			String aCodUfficioDest, Date aDataTrasmissioneDal, Date aDataTrasmissioneAl, int aPage)
			throws F3BException;

	public Vector<MessaggioModel> ExRicercaSollecitiByIdRich(String aDeliveryMode, String aCodTipoMessaggio,
			String aCodTipoOperazione, BigDecimal aIdMessRichiesta, String aFlagVisto, String aCodUfficioDest)
			throws F3BException;

	public void ExInserisciFascicoloClasseIV(FascicoloSiepModel aFascicolo,
			DettaglioFascicoloModel aDettaglioFasOrig, BigDecimal aIdMessaggio, Date aDataCumulo)
			throws F3BException;

	public BigDecimal ExInserisciAnnotazioneEsito(EventoNotificaModel aEventoNot,
			AnnotazioneEsitoTrasmissioneModel aAnnotaModel) throws F3BException;

	public EventoModel ExUpdateValidaAnnotazioneEsito(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public EventoModel ExUpdateValidaSollecitoEsito(EventoModel aEvento, FascicoloSiepModel aFascicolo)
			throws F3BException;

	public BigDecimal ExInserisciSollecitoEsito(EventoNotificaModel aEventoNot,
			SollecitoEsitoTrasmissioneModel aSollecitoModel) throws F3BException;

	public List ExRicercaMisuraSicurezzaByIdFascicoloSIUS(BigDecimal aKey) throws F3BException;

	// 10-02-2014
	public List ExRicercaMisuraSicurezzaByIdFascicoloOrd(BigDecimal aKey) throws F3BException;

	// 17-12-2014
	public List ExRicercaTutteMisureSicurezzaByIdFascicoloOrd(BigDecimal aKey) throws F3BException;

	public EventoModel ExUpdateValidaAnnotazioneDesignazioneIst(EventoModel aEvento,
			FascicoloSiepModel aFascicolo) throws F3BException;

	public BigDecimal ExInserisciProcedimentoMisuraProvvisoriaeoFuoriSenteneza(SentenzaModel aSentenza,
			SoggettoModel aSoggetto, MisuraSicurezzaModel aMisura, FascicoloSiepModel aFascicolo,
			String aTipo, BigDecimal aIdOrd, BigDecimal aIdFascSius) throws F3BException;

	public Vector<FascMsToFascSiepModel> ExRicercaFascicoliCollegati(BigDecimal aIdFascicoloSiep)
			throws F3BException;

	public Vector ExRicercaMisuraSicurezzaByMisIdMisuraSicurezza(BigDecimal aKey) throws F3BException;

	public String ExRicercaCodTipoMisuraByIdEvento(BigDecimal aIdEvento) throws F3BException;

	public List<MisuraSicurezzaModel> ExRicercaMisuraSicurezzaByIdEvento(BigDecimal aKey) throws F3BException;

	public boolean ExEsistonoFascicoliClasseIVAnno(UfficioModel aUfficio, BigDecimal aAnno)
			throws F3BException;

	//
	/**
	 * Maggio 2015 Metodo per l'aggiornamento dell'evento e delle notifiche
	 * 
	 * @param EventoNotificaModel
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoNotificaModel ExAggiornaEventoNotificheXComunicazioneMS(EventoNotificaModel aEvento)
			throws F3BException;

	/**
	 * Giugno 2015 Metodo per l'aggiornamento dell'evento, di Archiviazione e delle notifiche
	 * 
	 * @param EventoNotificaModel
	 * @param ArchiviazioneModel
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoNotificaModel ExAggiornaEventoNotificheXArchiviazioneMS(EventoNotificaModel aEvento,
			ArchiviazioneModel lArcMod) throws F3BException;

	/**
	 * Giugno 2015 Metodo per l'aggiornamento dell'evento, CampoNota
	 * 
	 * @param EventoNotificaModel
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoNotificaModel ExAggiornaEventoCampoNotaXRichiestaDapMS(EventoNotificaModel aEvento,
			String aDescr) throws F3BException;

	/**
	 * Giugno 2015 Metodo per l'aggiornamento dell'evento e Verbale
	 * 
	 * @param EventoModel
	 * @param VerbaleDataInizioModel
	 * @return EventoNotificaModel
	 * @throws F3BException
	 */
	public EventoModel ExAggiornaEventoVerbaleXDesignazioneIstitutoMS(EventoModel aEvento,
			VerbaleModel aVerbale) throws F3BException;

	// MEV_39: aggiunti metodi di ricerca
	public FascMsToFascSiepModel ExRicercaDatiFascColl(String codUfficioUtenteConnesso, String lId,
			BigDecimal keyFas) throws F3BException;

	public List ExRicercaMSNotificateByIdFasc(BigDecimal idFascicoloSiep, String tipoRicerca, BigDecimal idEventoRestituzione) throws F3BException;

} // Chiude Interfaccia