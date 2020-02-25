package siap.siep.penasospesa.action;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;

/**
 * <p>
 * Title: ActInserisciRicDeterminazioneTermini
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
public class ActInserisciRicDeterminazioneTermini extends ActInserisciRicEstinzioneReato {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRicDeterminazioneTermini: inizio");

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
		// Lettura Tipologia Obbligo
		CampoNotaModel lCampoNote1 = letturaTipologiaObbligo();
		// Lettura Note (non obbligatorio)
		CampoNotaModel lCampoNote2 = letturaCampoNote();
		if (lCampoNote2 != null) {
			lNote = new CampoNotaModel[2];
			lNote[0] = lCampoNote1;
			lNote[1] = lCampoNote2;
		} else {
			lNote = new CampoNotaModel[1];
			lNote[0] = lCampoNote1;
		}
		lEve.setCampoNote(lNote);

		// Memorizzazione
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		// Costruzione della pagina di Dettaglio
		RedirectTo lRedirectTo = new RedirectTo();
		lRedirectTo.setPage(IWebConstants.PG_MAIN);
		lRedirectTo.setAction("siap.siep.penasospesa.action.ActDettaglioRicDeterminazioneTermini");
		lRedirectTo.setParameter(ICostantiEvento.CAMPO_ID_EVENTO,
				lRetModel.getEvento().getIdEvento().toString());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActInserisciRicDeterminazioneTermini: fine");

		return lRedirectTo.toString();
	}

	/**
	 * Viene letto il codice della Tipologia di Obbligo selezionata nella Form, decodificata e memorizzata in
	 * un CampoNote.
	 * 
	 * @return
	 * @throws Exception
	 */
	protected CampoNotaModel letturaTipologiaObbligo() throws Exception {
		String lCodTipologia = getRequestStringParameter(CAMPO_COD_ARTICOLO);
		String lDescTipologia = DecodificheUtils
				.getDescbyCode(DecodificheManager.getInstance().getTipoSospSubordinata(), lCodTipologia);

		CampoNotaModel lCampoNotaMod = new CampoNotaModel();
		lCampoNotaMod.setDescr(lDescTipologia);
		lCampoNotaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lCampoNotaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lCampoNotaMod.setDataInserimento(DateUtils.getSysDate());

		return lCampoNotaMod;

	}
}