package siap.sige.decretounificazione.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadVerificaDecretoUnificazioneSige
 * </p>
 * <p>
 * Description: Classe Action per la load di VerificaDecretoUnificazioneSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadVerificaDecretoUnificazioneSige extends ActLoadRicercaFSigePuntuale {
  public String processRequest() throws Exception {
      super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
      super.setLinkRitorno();
      setRequestAttribute("nextAction", "siap.sige.decretounificazione.action.ActRicercaFSPUnificazioneSige" );
      setRequestAttribute("functionName", "Ricerca Procedimento Unificante");
      setRequestAttribute("azioneChiamante", "decreto");
      setRequestAttribute(IWebConstants.LINK_RITORNO, "20");
      return PG_LOAD_RICERCAFSIGEPUNTUALE;
  }
}


// Vecchia Gestione
/*public class ActLoadVerificaDecretoUnificazioneSige extends ActionSiap implements ICostantiDecretoUnificazioneSige
{
	
	
	
	// Vecchia Gestione
	public String processRequest() throws Exception
	{
		// esegue la query per recuperare l'elenco degli uffici accorpati dall'ufficio dell'utente loggato
		String codUfficioUtente = getCodUfficioUtenteConnesso();
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati(null, codUfficioUtente);

		// imposta sulla request la lista degli uffici accorpati
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);

		return PG_LOAD_VERIFICADECRETOUNIFICAZIONESIGE; // restituisce la jsp di VIEW
	}
	
	
}

*/