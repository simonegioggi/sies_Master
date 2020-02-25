package siap.sige.avvocato.action;

import java.math.BigDecimal;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
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
		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		AvvocatoFascicoloSigeModel lAvvFasc = new AvvocatoFascicoloSigeModel();
		lAvvFasc.setAvvIdAvvocato(lId);
		lAvvFasc.setFasSigeIdFascicoloSige(((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
				.getFascicoloSige().getIdFascicoloSige());
		lAvvFasc.setDataFineValidita(DateUtils.getSysDate());
		lAvvFasc.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFasc.setDataAggiornamento(DateUtils.getSysDate());
//		Vector lVect = null;

		// chiama il controller
//		AvvocatoFascicoloSigeModel lAvvModificato = new AvvocatoFascicoloSigeModel();

		/*lAvvModificato = */lCtrl.ExDeassegnaAvvocato(lAvvFasc);
		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod
				.setFasSigeIdFascicoloSige(((FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso"))
						.getFascicoloSige().getIdFascicoloSige());

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		try {
//			lVect = new Vector();
			/*lVect = */lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Modificato il ritorno per gestire eventuale bottone di ritorno Luigi 9-7-04
		String nextAct = "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&"
				+ ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE + "="
				+ lAvvFascMod.getFasSigeIdFascicoloSige();
		String retPage = ritornoDopoCancellazione("Deassegnazione  Difensore Avvenuta Correttamente!",
				nextAct);
		return retPage;
	}

}