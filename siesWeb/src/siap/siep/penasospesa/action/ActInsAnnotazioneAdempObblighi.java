package siap.siep.penasospesa.action;

import java.math.BigDecimal;

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
 * Title: ActInserisciRicAdempObblighi
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
public class ActInsAnnotazioneAdempObblighi extends ActionSiap implements ICostantiPenaSospesa {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	boolean mCancellaScadenzario = false;

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInsAnnotazioneAdempObblighi: inizio");

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		// Inizializzazioni
		// String lPage = null;

		// VALORIZZAZIONE EVENTO
		EventoModel lEveModel = letturaEvento();

		// Annotazione Manuale
		AnnotazioneManualeModel lAnnotazione = new AnnotazioneManualeModel();
		lAnnotazione = creaAnnotazione(lIdFascicolo);

		// Memorizzazione
		IPenaSospesa lPenaSospCtrl = SIEPLookupRemote.getPenaSospesaRemote();

		lAnnotazione = lPenaSospCtrl.ExInserisciAnnotazioneEventoScadenzario(lAnnotazione, lEveModel, null,
				mCancellaScadenzario);

		// Costruzione della pagina di Dettaglio
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.siep.penasospesa.action.ActDettaglioAnnAdempObblighi");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO, lAnnotazione.getEveIdEvento().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInsAnnotazioneAdempObblighi: fine");

		return lRedirectTo.toString();
	}

	/**
	 * Viene letto il codice della Tipologia di Obbligo selezionata nella Form, decodificata e memorizzata in
	 * un CampoNote.
	 * 
	 * @return
	 * @throws Exception
	 */
	private AnnotazioneManualeModel creaAnnotazione(BigDecimal aIdFascicolo) throws Exception {
		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

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

		// I dati relativi alla nuova sentenza inserita vengono letti dalla request
		lAnnMod.setFasSieIdFascicoloSiep(aIdFascicolo);

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
		// getCodUfficioByCodTipoUfficioDescrComune( lCodTipoUfficioEmi, lDescrComune );

		if (getRequestStringParameter("Decisione").equals("0")) {
			lAnnMod.setMotivazioni("Il condannato ha ottemperato gli obblighi");
			mCancellaScadenzario = true;
		} else if (getRequestStringParameter("Decisione").equals("1")) {
			lAnnMod.setMotivazioni("Il condannato non ha ottemperato gli obblighi");
			mCancellaScadenzario = false;
		}

		return lAnnMod;
	}

	protected EventoModel letturaEvento() throws Exception {
		EventoModel lEveModel = new EventoModel();

		// ID Fascicolo SIEP dalla sessione
		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		lEveModel.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		lEveModel.setCodTipoEvento("01");
		// lEveModel.setCodTipoProvvedimento("25"); //Annotazione
		lEveModel.setCodTipoProvvedimento("57"); // Annotazione GE
		lEveModel.setCodMotivo("1112");

		lEveModel.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveModel.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		lEveModel.setCodUfficioDestinatario("-");
		lEveModel.setCodTipoUfficioDestinatario("-");

		lEveModel.setCodLuogoDestinatario("-");
		lEveModel.setCodEsito("-");

		// Data di Emissione e di Trasmissione se non presenti nella FORM valorizzate con quella di sistema
		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE))
			// 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
			// lEveModel.setDataEmissione(DateUtils.getSysDate());
			lEveModel.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		else
			lEveModel.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

		if (isRequestParameterNullObj(ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI))
			lEveModel.setDataTrasmissioneAtti(DateUtils.getSysDate());
		else
			lEveModel.setDataTrasmissioneAtti(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
							ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
							ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		// lEveModel.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		lEveModel.setCodOperatoreInserimento(getCodUfficioUtenteConnesso());
		lEveModel.setDataInserimento(DateUtils.getSysDate());
		lEveModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveModel.setFlagStampaSiep("S");
		lEveModel.setFlagVideoSiep("S");
		lEveModel.setFlagDocumentoRegistrato("S");
		lEveModel.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveModel.setDataTrasmissioneAtti(DateUtils.getSysDate());

		return lEveModel;

	}

}