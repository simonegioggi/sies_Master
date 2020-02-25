package siap.sico.decodifiche.action;

import java.util.Vector;

import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

@SuppressWarnings("rawtypes")
public class ActLoadListaTitoliEsecutiviSige extends ActionSige implements ICostantiDecodifiche {

	public String processRequest() throws Exception {

		String strFormName = getRequestStringParameter("formname");
		String strFieldName = getRequestStringParameter("fieldname");

		String strFieldCode = "";
		if (!(isRequestParameterNullObj("fieldcode")))
			strFieldCode = getRequestStringParameter("fieldcode");

		// Ricerca Sentenze assegnate al Fascicolo SIGE
		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		Vector lSentenze = lFasSenCtrl.ExRicercaSentenzeAssegnateFascicolo(
				getFascicoloSigeEstesoInSessione().getFascicoloSige().getIdFascicoloSige());

		// Imposta la risposta nella request.
		setRequestAttribute("ListaTitoliEsecutivi", lSentenze);
		setRequestAttribute("fieldcode", strFieldCode);
		setRequestAttribute("fieldname", strFieldName);
		setRequestAttribute("formname", strFormName);

		return PG_LISTA_TITOLI_ESECUTIVI_SIGE; // restituisce la jsp di VIEW
	}

}