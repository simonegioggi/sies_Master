package siap.siep.misurasicurezza.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

/**
 * MEV_39
 * <p>
 * Title: ActLoadListaArchiviazioniProvvedimentiSIUS
 * </p>
 * <p>
 * Description: Classe la load della POPUP contenebte la lista dei provvedimenti delle
 * </p>
 * <p>
 * varie tipologie di Differimenti della Sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author SGIOGGI
 * @version 1.0
 */
public class ActLoadListaDifferimentiProvvedimentiSIUS extends ActionSiap implements ICostantiMisuraSicurezza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo") || isSessionAttributeNullObj("soggetto"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("idfascicolo", lFascMod.getIdFascicoloSiep() + "");

		// Dati ORDINANZA SIUS
		IDepositoOrdinanzaPc lDepoCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
		Vector depoOrdinanze = lDepoCtrl.ExRicercaEventoProvvedimentiDifferimentoSIUSByFascicoloSiep(lFascMod
				.getIdFascicoloSiep());

		setRequestAttribute("listaOrd", depoOrdinanze);

		// valore di ritorno
		return PG_LOAD_LISTA_DIFFERIMENTO_PROVV_SORVE; // restituisce la jsp di VIEW
	}

}