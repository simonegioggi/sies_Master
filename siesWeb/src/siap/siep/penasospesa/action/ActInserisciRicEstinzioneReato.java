package siap.siep.penasospesa.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;

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
public class ActInserisciRicEstinzioneReato extends ActionSiap implements ICostantiPenaSospesa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRicEstinzioneReato: inizio");

		// Inizializzazioni
		EventoNotificaModel lEve = new EventoNotificaModel();
		EventoNotificaModel lRetModel;
		// String lPage = null;

		// VALORIZZAZIONE EVENTO
		EventoModel lEveModel = letturaEvento();
		lEve.setEvento(lEveModel);

		// VALORIZZAZIONE NOTIFICA
		NotificaModel lNotifiche[] = new NotificaModel[1];
		lNotifiche[0] = letturaNotifica();
		lEve.setNotifiche(lNotifiche);

		// VALORIZZAZIONE CAMPO NOTE
		CampoNotaModel[] lNote = null;
		CampoNotaModel lCampoNote = letturaCampoNote();
		if (lCampoNote != null) {
			lNote = new CampoNotaModel[1];
			lNote[0] = lCampoNote;

		}
		lEve.setCampoNote(lNote);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		// Costruzione della pagina di redirect
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.siep.penasospesa.action.ActDettaglioRicEstinzioneReato");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
				lRetModel.getEvento().getIdEvento().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRicEstinzioneReato: fine");

		return lRedirectTo.toString();
	}

	protected EventoModel letturaEvento() throws Exception {
		EventoModel lEveModel = new EventoModel();

		// ID Fascicolo SIEP dalla sessione
		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		lEveModel.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		lEveModel.setCodTipoEvento("02");
		lEveModel.setCodTipoProvvedimento("26"); // Richiesta
		lEveModel.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));

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
		lEveModel.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveModel.setDataTrasmissioneAtti(DateUtils.getSysDate());

		return lEveModel;

	}

	protected NotificaModel letturaNotifica() throws Exception {
		// Lettura dati del destinatario
		String lDescrComune = getRequestStringParameter(CAMPO_SEDE_UFFICIO_GE);
		String lCodTipoUfficioDest = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_GE);
		String lCodUfficioDest = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioDest, lDescrComune);

		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lNot.setUffCodUfficio(lCodUfficioDest);
		if (!isRequestParameterNullObj(CAMPO_SEZIONE_UFFICIO_GE)
				&& !("-").equals(getRequestStringParameter(CAMPO_SEZIONE_UFFICIO_GE)))
			lNot.setNote("Sezione " + getRequestStringParameter(CAMPO_SEZIONE_UFFICIO_GE));
		return lNot;
	}

	protected CampoNotaModel letturaCampoNote() throws Exception {
		String lNote = getRequestStringParameter(CAMPO_NOTE);
		CampoNotaModel lCampoNotaMod = null;

		if (lNote.trim().length() > 0) {
			lCampoNotaMod = new CampoNotaModel();
			lCampoNotaMod.setDescr(lNote);
			lCampoNotaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lCampoNotaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());
		}
		return lCampoNotaMod;

	}

}