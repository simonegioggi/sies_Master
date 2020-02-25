package siap.siep.notifica.action;

import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
//import siap.siep.scadenzario.controller.IScadenzario;
//import siap.siep.scadenzario.model.ScadenzarioModel;
//import siap.siep.util.SIEPLookupRemote;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;

/**
 * <p>
 * Title: ActLoadRicercaNotificaDecretoIrreperibilita
 * </p>
 * <p>
 * Description: Classe Action per la ricerca
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
public class ActLoadRicercaNotificaDecretoIrreperibilita extends ActionSiap implements ICostantiNotifica {

	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " non è stato Validato. Impossibile inserire un ordine d'esecuzione!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		this.isFascicoloSiepDiCompetenza();

		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il Procedimento N." + lFascMod.getChiaveAnno() + "/" + lFascMod.getChiaveProgr()
							+ " Il fascicolo risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE;
		}

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
		lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(),
				"8BIS");

		if (lEveNot == null || lEveNot.getEvento() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non Esiste Alcun Decreto di Irreperibilità.");

		if (lEveNot.getNotifiche() == null || lEveNot.getNotifiche().length == 0)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non Esiste Nessuna Notifica Associata al Decreto di Irreperibilità.");

		setRequestAttribute("eventonotifica", lEveNot);

		Vector lNotVect = new Vector();
		// IScadenzario lCtrlSca = SIEPLookupRemote.getScadenzarioRemote();
		// ScadenzarioModel lScaMod = new ScadenzarioModel();
		// Vector lScadenzari = new Vector();

		for (int i = 0; i < lEveNot.getNotifiche().length; i++) {
			NotificaModel lNotMod = new NotificaModel();
			lNotMod = lEveNot.getNotifiche()[i];
			/*
			 * if(lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAvvIdAvvocatoFascicoloSiep()!= null
			 * && lNotMod.getAvvSiep() != null && lNotMod.getAvvSiep().getAvvocatoFascicoloSiepModel() != null
			 * && lNotMod.getAvvSiep().getAvvocatoFascicoloSiepModel().getDataFineValidita() == null) {
			 */
			if (lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAvvIdAvvocatoFascicoloSiep() != null) {
				// lScaMod =
				// lCtrlSca.ExRicercaScadenzarioCorrenteByIdFascicoloIdNotifica(lFascMod.getIdFascicoloSiep(),lNotMod.getIdNotifica(),"01");
				// lScadenzari.addElement(lScaMod);

				lNotVect.add(lNotMod);
			}
		}

		/*
		 * if(lNotVect == null || lNotVect.size() == 0 ) { throw new SIEPException(SIEPException.USER_MESSAGE,
		 * "L'Avvocato non è più associato al fascicolo."); }
		 */
		setRequestAttribute("notifica", lNotVect);
		// setRequestAttribute("scadenzari", lScadenzari);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "-");
		setRequestAttribute("autoritaEsternaDelegata", "" + lOption);

		return PG_LOAD_DECRETO_IRREPERIBILITA; // restituisce la jsp di VIEW
	}

}