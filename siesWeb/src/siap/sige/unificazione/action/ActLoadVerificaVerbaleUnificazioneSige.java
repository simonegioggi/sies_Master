package siap.sige.unificazione.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadVerificaVerbaleUnificazioneSige
 * </p>
 * <p>
 * Description: Classe Action per la load di VerificaVerbaleUnificazioneSige
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
public class ActLoadVerificaVerbaleUnificazioneSige extends ActLoadRicercaFSigePuntuale {
	  public String processRequest() throws Exception {
	      super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
	      super.setLinkRitorno();
	      setRequestAttribute("nextAction", "siap.sige.decretounificazione.action.ActRicercaFSPUnificazioneSige" );
	      setRequestAttribute("functionName", "Ricerca Procedimento Unificante");
	      setRequestAttribute("azioneChiamante", "verbale");
	      setRequestAttribute(IWebConstants.LINK_RITORNO, "20");
	      return PG_LOAD_RICERCAFSIGEPUNTUALE;
	  }
	}

//Vecchia Gestione
/*
public class ActLoadVerificaVerbaleUnificazioneSige extends ActionSiap implements ICostantiVerbaleUnificazioneSige
{
	public String processRequest() throws Exception
	{
		// esegue la query per recuperare l'elenco degli uffici accorpati dall'ufficio dell'utente loggato
		String codUfficioUtente = getCodUfficioUtenteConnesso();
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati(null, codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		return PG_LOAD_VERIFICAVERBALEUNIFICAZIONESIGE; // restituisce la jsp di VIEW
	}
}
*/