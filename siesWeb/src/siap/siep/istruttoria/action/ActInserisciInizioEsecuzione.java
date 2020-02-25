package siap.siep.istruttoria.action;

//import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.sentenza.model.SentenzaModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * Azione di caricamento dell form di Inizio Esecuzione
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class ActInserisciInizioEsecuzione extends ActionSiap implements ICostantiIstruttoria {

	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		SentenzaModel sentenza = (SentenzaModel) getSessionAttribute("sentenza");
		String tipo = (String) getRequestStringParameter("tipo");
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		EventoNotificaModel lRetModel = new EventoNotificaModel();
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();

		EventoModel lEveMod = new EventoModel();
		lEveMod.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		// Paolo Cherubini 11/01/2011
		// cerco la Comunicazione inizio esecuzione perchè ne può esistere una sola
		if (tipo.equals("IE")) {
			lEveMod.setCodMotivo("0047"); // Comunicazione inizio esecuzione ne esiste una sola
			lEveMod.setCodTipoEvento("05");
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaEvento(lEveMod);
			} catch (F3BException ex) { // Non è stato trovato nessun elemento
			}

			if (lVect != null && lVect.size() > 0) {
				EventoModel lRetEveMod = new EventoModel();
				lRetEveMod = (EventoModel) lVect.get(0);
				String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.istruttoria.action.ActDettaglioInizioEsecuzione&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetEveMod.getIdEvento();
				return lPage;
			}
		}

		// --- Inserisci Evento
		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("05"); // Tipo Evento = Richiesta Istruttoria
		lEve.getEvento().setCodTipoProvvedimento("-");
		String lCodTipoIstitutoDetenzione = "";
		if (tipo.equals("IE"))
			lEve.getEvento().setCodMotivo("0047"); // Comunicazione inizio esecuzione
		else {
			lEve.getEvento().setCodMotivo("1048"); // Comunicazione esecutività sentenza
			lCodTipoIstitutoDetenzione = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
		}
		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		// 08/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
		// lEve.getEvento().setDataEmissione(DateUtils.getSysDate());
		lEve.getEvento()
				.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lEve.getEvento().setCodOperatoreInserimento(getCodUtenteConnesso());
		lEve.getEvento().setCodLuogoEmittente(lUff.getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lUff.getCodUfficio());
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodUfficioInserimento(lUff.getCodUfficio());
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		NotificaModel lNotifiche[] = new NotificaModel[1];
		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");
		lNot.setDataInvio(DateUtils.getSysDate());
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(getCodUtenteConnesso());
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lUff.getCodUfficio());

		if (tipo.equals("IE"))
			lNot.setUffCodUfficio(lUff.getCodUfficio()); // Comunicazione inizio esecuzione
		else {
			lNot.setIstDetIdIstitutoDetenzione(lCodTipoIstitutoDetenzione);
			lNot.setCodTipoNotifica("E");
			; // Comunicazione esecutività sentenza
		}

		lNotifiche[0] = lNot;

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche(lNotifiche);
		lEve.getEvento().setFlagDocumentoRegistrato("N");

		// IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		EventoNotificaModel lEve1 = new EventoNotificaModel();
		lEve1 = lCtrl.ExRicercaEventoNotificaByKey(lRetModel.getEvento().getIdEvento());

		setRequestAttribute("eventonotifica", lEve1);
		setRequestAttribute("evento", lRetModel.getEvento());
		setRequestAttribute("fascicolo", lFascicoloModel);
		setRequestAttribute("sentenza", sentenza);

		return ICostantiIstruttoria.PG_DETTAGLIO_INIZIO_ESECUZIONE;
	}

}