package siap.siep.penasospesa.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penasospesa.controller.IPenaSospesa;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActInserisciRicEstinzioneReato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * @author Luigi
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciAnnotazioneTermini extends ActionSiap implements ICostantiPenaSospesa {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciAnnotazioneTermini: inizio");

		// Inizializzazioni
		// EventoNotificaModel lEve = new EventoNotificaModel();

		// Data Irrevocabilità dal Fascicolo SIEP in sessione
		Date ldataIrr = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getDataIrrevocabilita();

		// VALORIZZAZIONE EVENTO
		EventoModel lEveModel = letturaEvento();

		// VALORIZZAZIONE ANNOTAZIONE MANUALE
		AnnotazioneManualeModel lAnnMod = letturaAnnotazione();

		// Memorizzazione
		IPenaSospesa lPenaSospCtrl = SIEPLookupRemote.getPenaSospesaRemote();
		lAnnMod = lPenaSospCtrl.ExInserisciAnnotazioneEventoScadenzario(lAnnMod, lEveModel, ldataIrr, false);

		// Costruzione della pagina di redirect
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.siep.penasospesa.action.ActDettaglioAnnotazioneTermini");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lAnnMod.getEveIdEvento().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciAnnotazioneTermini: fine");

		return lRedirectTo.toString();
	}

	protected EventoModel letturaEvento() throws Exception {
		EventoModel lEveModel = new EventoModel();

		// ID Fascicolo SIEP dalla sessione
		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		lEveModel.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		lEveModel.setCodTipoEvento("01"); // ?
		lEveModel.setCodTipoProvvedimento("57"); // Annotazione GE
		lEveModel.setCodMotivo("1113"); // Determinazione Termini

		lEveModel.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveModel.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEveModel.setCodUfficioDestinatario("-");
		lEveModel.setCodTipoUfficioDestinatario("-");

		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodEsito("-");
		// 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
		// lEveModel.setDataEmissione(DateUtils.getSysDate());
		lEveModel.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));

		// lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModel.setCodOperatoreInserimento(getCodUfficioUtenteConnesso());
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveModel.setFlagStampaSiep("S");
		lEveModel.setFlagVideoSiep("S");
		lEveModel.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		// lEveModel.setDataTrasmissioneAtti(DateUtils.getSysDate());

		return lEveModel;

	}

	/**
	 * Crea l'Annotazione Manuale che individua la sentenza di revoca
	 */

	private AnnotazioneManualeModel letturaAnnotazione() throws Exception {
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

		// ID Fascicolo SIEP dalla sessione
		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		// Valorizzazione campi specifici
		lAnnMod.setCodTipoAnnotazione("016"); // Gestione Pene Sospese
		lAnnMod.setFlagAppProvvisoria("-");
		lAnnMod.setFlagValidato("S");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");
		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());
		lAnnMod.setCodDpr("-");
		lAnnMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		// I dati relativi alla Ordinanza del GE vengono letti dalla request
		lAnnMod.setAnnoGe(getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_ANNO_GE));
		lAnnMod.setNumeroGe(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_NUMERO_GE));

		lAnnMod.setDataGE(getRequestDateParameter(ICostantiAnnotazioneManuale.CAMPO_ANNO_DATA_GE,
				ICostantiAnnotazioneManuale.CAMPO_MESE_DATA_GE,
				ICostantiAnnotazioneManuale.CAMPO_GIORNO_DATA_GE));

		// Lettura dati dell'Autorità Emittente
		String lDescrComune = getRequestStringParameter(CAMPO_SEDE_UFFICIO_GE);
		String lCodTipoUfficioEmi = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_GE);
		lAnnMod.setCodTipoUfficioSiep(lCodTipoUfficioEmi);
		lAnnMod.setCodLuogoUfficioSiep(getCodComuneByDescr(lDescrComune).getCodComune());

		// Questo solo per controllo validità dati
		getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioEmi, lDescrComune);

		// Motivazioni
		lAnnMod.setMotivazioni(getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_MOTIVAZIONI));

		// Termini
		if (getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_NUM_ANNI_RECLUSIONE).trim()
				.length() > 0)
			lAnnMod.setNumAnniReclusione(
					getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_NUM_ANNI_RECLUSIONE));
		else
			lAnnMod.setNumAnniReclusione(new BigDecimal("0"));
		if (getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_NUM_MESI_RECLUSIONE).trim()
				.length() > 0)
			lAnnMod.setNumMesiReclusione(this
					.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_NUM_MESI_RECLUSIONE));
		else
			lAnnMod.setNumMesiReclusione(new BigDecimal("0"));
		if (getRequestStringParameter(ICostantiAnnotazioneManuale.CAMPO_NUM_GIORNI_RECLUSIONE).trim()
				.length() > 0)
			lAnnMod.setNumGiorniReclusione(this
					.getRequestBigDecimalParameter(ICostantiAnnotazioneManuale.CAMPO_NUM_GIORNI_RECLUSIONE));
		else
			lAnnMod.setNumGiorniReclusione(new BigDecimal("0"));
		return lAnnMod;
	}

}