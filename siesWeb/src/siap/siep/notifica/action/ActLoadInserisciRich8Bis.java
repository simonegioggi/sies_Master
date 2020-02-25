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
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;

/**
 * <p>
 * Title: ActLoadInserisciRich8Bis
 * </p>
 * <p>
 * Description:
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

public class ActLoadInserisciRich8Bis extends ActionSiap implements ICostantiNotifica {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
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
		lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(), "LS");

		if (lEveNot == null || lEveNot.getEvento() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE, "Non Esiste Nessun OE con Sospensione.");

		if (lEveNot.getNotifiche() == null || lEveNot.getNotifiche().length == 0)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Non Esiste Nessuna Notifica Associata all'OE con Sospensione.");

		// avvocati del fascicolo
		Vector lNotVect = new Vector();
		for (int i = 0; i < lEveNot.getNotifiche().length; i++) {
			NotificaModel lNotMod = new NotificaModel();
			lNotMod = lEveNot.getNotifiche()[i];

			if (lNotMod.getCodTipoNotifica().equals("N") && lNotMod.getAvvIdAvvocatoFascicoloSiep() != null
					&& lNotMod.getAvvSiep() != null
					&& lNotMod.getAvvSiep().getAvvocatoFascicoloSiepModel() != null
					&& lNotMod.getAvvSiep().getAvvocatoFascicoloSiepModel().getDataFineValidita() == null) {
				lNotVect.add(lNotMod);
			}
		}

		if (lNotVect == null || lNotVect.size() == 0) {
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"L'Avvocato non è più associato al fascicolo.");
		}

		setRequestAttribute("avvocati", lEveNot.getAvvocati());
		setRequestAttribute("notifica", lNotVect);
		setRequestAttribute("evento", lEveNot.getEvento());
		setRequestAttribute("eventonotifica", lEveNot);

		Option lOptionAutoritaAltra = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("tipoAutorita", "" + lOptionAutoritaAltra);

		return PG_LOAD_RICH_INFO_8_BIS; // restituisce la jsp di VIEW
	}

}