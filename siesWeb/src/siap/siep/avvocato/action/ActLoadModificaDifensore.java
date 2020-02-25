package siap.siep.avvocato.action;

/**
* <p>Title: ActLoadModificaDifensore</p>
* <p>Description: Classe Action per la load inserisci di Avvocato</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

public class ActLoadModificaDifensore extends ActionSiap implements ICostantiAvvocato {

	public String processRequest() throws F3BException {

		// String lIdAvvocato;

		/*
		 * if(!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_ID_AVVOCATO)) { Vector avvocati = new
		 * Vector(); AvvocatoModel lModelRic = new AvvocatoModel();
		 * 
		 * AvvocatoModel lmModelAppo = new AvvocatoModel();
		 * lmModelAppo.setIdAvvocato(this.getRequestBigDecimalParameter(ICostantiAvvocato.CAMPO_ID_AVVOCATO));
		 * IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		 * avvocati=lCtrl.ExRicercaAvvocato(lmModelAppo); lModelRic=(AvvocatoModel)avvocati.get(0);
		 * setRequestAttribute("avvocato", lModelRic); }
		 */
		setRequestAttribute("modalita", "M");

		return PG_MODIFICA_DIFENSORE; // restituisce la jsp di VIEW
	}

}