package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * MEV_2023-13: aggiunta classe per caricamento griglia
 * 
 * @author sgioggi
 * @version 1.0
 */
public class ActGrigliaBollettiniPagoPA extends ActionSiap implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

		if (isSessionAttributeNullObj("fascicolo"))
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();

		setRequestAttribute("strFunzione", "Gestione Bollettini PagoPA");

		return ICostantiSanzioneSostitutiva.PG_GRIGLIA_BOLLETTINI_PAGOPA;
	}

}