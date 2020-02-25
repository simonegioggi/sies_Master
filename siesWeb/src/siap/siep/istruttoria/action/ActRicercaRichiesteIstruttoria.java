package siap.siep.istruttoria.action;

import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaRichiesteIstruttoria
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Evento
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
public class ActRicercaRichiesteIstruttoria extends ActionSiap implements ICostantiIstruttoria {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");

     IEvento lCtrl = SICOLookupRemote.getEventoRemote();
     String[] lTipoEvento = {"05"};
		// 20170831: [SG] nuova gestione ricerca se catturo eccezione
		Vector lVect = null;
		try {
			lVect = lCtrl.ExRicercaEventoNotificaByFascicoloSiepChiaveUfficio(
					lFascicoloModel.getIdFascicoloSiep(), lFascicoloModel.getChiaveUfficio(), lTipoEvento);
		} catch (Exception e) {
			if ("Nessun Elemento trovato".equals(e.getMessage())) {
				RedirectTo lRedirigi = new RedirectTo();
				lRedirigi.setPage(IWebConstants.PG_MAIN);
				lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
						+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
						+ lFascicoloModel.getIdFascicoloSiep());
				setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, e.getMessage());
				return IWebConstants.PG_MESSAGE;
			} else
				throw new F3BException (e);
		}
     setRequestAttribute("eventi", lVect);

     return PG_LOAD_RICERCAEVENTO;
   }
}
