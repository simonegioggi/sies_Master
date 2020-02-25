package siap.sige.fascicolo.action;

import java.util.Vector;

import siap.siep.sentenza.model.SentenzaModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Ricerca dei Procedimenti che afferiscono alla sentenza individuata
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company :
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaProcSigePerSentenza extends ActionSige implements ICostantiFascicoloSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		SentenzaModel lSenMod = new SentenzaModel();

		// riempie il model
		lSenMod.setIdSentenza(getRequestBigDecimalParameter("IdSentenza"));

		// Recupero del parametro di ricerca (Ufficio/Distretto).
		if (getRequestStringParameter(ICostantiFasSigeSentenza.CAMPO_AMBITO_RICERCA).compareTo("U") == 0)
			lSenMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Si chiama il FascicoloSigeController.
		IFascicoloSige lFasSigeCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		Vector lFascicoliSentenza = lFasSigeCtrl.ExRicercaFasSigePerLaSentenza(lSenMod, checkMinori());

		String lReturnPage = "";

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("IdSentenza", lSenMod.getIdSentenza());
		if (lSenMod.getCodUfficioInserimento() != null)
			setRequestAttribute("ambitoRicerca", "U");
		else
			setRequestAttribute("ambitoRicerca", "D");

		// Setta la risposta nella request
		setRequestAttribute("fascicoli", lFascicoliSentenza);

		lReturnPage = ICostantiFascicoloSige.PG_RICERCA_FASCICOLIPERSENTENZA;

		// Bottone di ritorno
		setLinkRitorno();

		return lReturnPage; // restituisce la jsp di VIEW
	}

}