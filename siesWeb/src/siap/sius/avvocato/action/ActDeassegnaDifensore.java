package siap.sius.avvocato.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
//import siap.sius.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActDeassegnaDifensore
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Avvocato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActDeassegnaDifensore extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Modifica del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		BigDecimal lId = getRequestBigDecimalParameter("tipo");
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		AvvocatoFascicoloSiusModel lAvvFasc = new AvvocatoFascicoloSiusModel();
		lAvvFasc.setAvvIdAvvocato(lId);
		// 02/11/2006 MAC x Deassegnazione e Sostituzione Avvocati SIUS.
		lAvvFasc.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		lAvvFasc.setDataFineValidita(DateUtils.getSysDate());
		lAvvFasc.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFasc.setDataAggiornamento(DateUtils.getSysDate());
//		Vector lVect = null;

		// chiama il controller

//		AvvocatoFascicoloSiusModel lAvvModificato = new AvvocatoFascicoloSiusModel();

		/*lAvvModificato = */lCtrl.ExDeassegnaAvvocato(lAvvFasc);
		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvFascMod.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		try {
//			lVect = new Vector();
			/*lVect = */lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);
		} catch (Exception e) {
		}

		// Modificato il ritorno per gestire eventuale bottone di ritorno Luigi 9-7-04
		String nextAct = "siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS + "="
				+ lAvvFascMod.getFasSiuIdFascicoloSius();
		String retPage = ritornoDopoCancellazione("Deassegnazione  Difensore Avvenuta Correttamente!",
				nextAct);
		return retPage;
	}

}