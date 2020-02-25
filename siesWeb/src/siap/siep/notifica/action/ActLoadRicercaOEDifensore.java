package siap.siep.notifica.action;

import java.util.ArrayList;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaOEDifensore
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Scadenzario Evento
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadRicercaOEDifensore extends ActionSiap implements ICostantiNotifica {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		if (isFascicoloNonValidato())
			return IWebConstants.PG_MESSAGE;

		isFascicoloSiepDiCompetenza();

		if (isFascicoloArchiviatoDefinito())
			return IWebConstants.PG_MESSAGE;

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(), "LS");

		if (lEveNot == null || lEveNot.getEvento() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun OE con Sospensione.");

		/*
		 * if(lEveNot.getNotifiche() == null || lEveNot.getNotifiche().length == 0) throw new
		 * SIEPException(SIEPException.USER_MESSAGE,
		 * "Non Esiste Nessuna Notifica Associata all'OE con Sospensione.");
		 */
		/******************************* Posizione Giuridica **********************************/
		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascMod.getIdFascicoloSiep());
		if (lPosizione == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		if ("02".equals(lPosizione.getCodPosizioneGiuridica())
				|| "04".equals(lPosizione.getCodPosizioneGiuridica()))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La Posizione Giuridica Detenuto per questa causa in regime di arresti domiciliari non permette di annotare la notifica al Difensore.");

		Vector lNotVect = new Vector();

		int lContaAvvocatiBuoni = 0;
		// String lEsisteNotifica="N";
		for (int i = 0; i < lEveNot.getNotifiche().length; i++) {
			NotificaModel lNotMod = new NotificaModel();
			lNotMod = lEveNot.getNotifiche()[i];
			if (lNotMod.getCodTipoNotifica().equals("N")) {
				lContaAvvocatiBuoni++;
				if (lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) {
					lNotVect.add(lNotMod);
				}
			}
			// lEsisteNotifica ="S";
		}
		/*
		 * if("N".equals(lEsisteNotifica)) { throw new SIEPException(SIEPException.USER_MESSAGE,
		 * "Non Esiste Nessuna Notifica Associata all'OE con Sospensione."); }
		 */
		// se non esistono le notifiche al difensore le inserisco paolo 19 Marzo 2009
		Vector lAvvVect = null;

		if (lContaAvvocatiBuoni == 0) {
			// Controllo esistenza almeno un avvocato per fascicolo.
			IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
			try {
				lAvvVect = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			} catch (SIEPException e) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT,
						e.getMessage() + " Impossibile annotare la Notifica.");
				lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
						+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				return IWebConstants.PG_MESSAGE;
			}
			// se esiste almeno un avvocato procedo a inserire le notifiche
			// naturalmente senza autorita incaricata della notifica
			int lIndNotifiche = 0;
			ArrayList lNotificheArray = new ArrayList();
			INotifica lCtrlNot = SIEPLookupRemote.getNotificaRemote();
			while (lIndNotifiche < lAvvVect.size()) {
				NotificaModel lNot = new NotificaModel();
				lNot.setCodTipoNotifica("N");
				AvvocatoSiepModel lAvvFasMod = (AvvocatoSiepModel) lAvvVect.get(lIndNotifiche);
				lNot.setAvvIdAvvocatoFascicoloSiep(
						lAvvFasMod.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep());
				// lNot.setNote(lArrayNote[lIndNotifiche]);
				lNot.setDataInvio(lEveNot.getEvento().getDataEmissione());
				lNot.setEveIdEvento(lEveNot.getEvento().getIdEvento());
				lNot.setCodEsito("-");
				lNot.setCodOperatoreInserimento(lEveNot.getEvento().getCodOperatoreInserimento());
				lNot.setDataInserimento(DateUtils.getSysDate());
				lNot.setCodUfficioInserimento(lEveNot.getEvento().getCodUfficioInserimento());
				lNot.setIstDetIdIstitutoDetenzione("");
				lNot.setAutoritaEsterna(null);
				lNotificheArray.add(lNot);
				lIndNotifiche++;
			}
			// ricerco le notifiche per caricarmo gli ID inseriti
			// e trasforma il risultato che è un array in un vector da passare alla maschera successiva
			// ArrayList lNotificheArrayRet = new ArrayList();
			if (lNotificheArray.size() > 0) {
				/* lNotificheArrayRet = */lCtrlNot.ExInserisciNotifiche(lNotificheArray);
			}

			lEveNot = new EventoNotificaModel();
			lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(),
					"LS");
			for (int i = 0; i < lEveNot.getNotifiche().length; i++) {
				NotificaModel lNotMod = new NotificaModel();
				lNotMod = lEveNot.getNotifiche()[i];
				if (lNotMod.getCodTipoNotifica().equals("N")) {

					if (lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) {
						lNotVect.add(lNotMod);
					}
				}
				// lEsisteNotifica ="S";
			}
		}

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaDelegata", "" + lOption);
		setRequestAttribute("eventonotifica", lEveNot);
		setRequestAttribute("notifica", lNotVect);
		// setRequestAttribute("scadenzari", lScadenzari);

		return PG_LOAD_DETTAGLIO_NOTIFICA_DIFENSORE; // restituisce la jsp di VIEW
	}

}