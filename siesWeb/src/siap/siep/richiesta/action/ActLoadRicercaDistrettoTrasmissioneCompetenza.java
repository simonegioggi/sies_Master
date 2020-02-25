package siap.siep.richiesta.action;
		
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
 * <p>Title: ActLoadRicercaDistrettoTrasmissioneCompetenza</p>
 * <p>Description: Classe Action per la load inserisci della Trasmissione per Competenza</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
 */

public class ActLoadRicercaDistrettoTrasmissioneCompetenza extends ActionSiap
                                                    implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {	  
	  UtenteModel lUtenteConnesso = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	  setRequestAttribute("descrComune",  lUtenteConnesso.getUfficioUtente().getDescrComune());
	  setRequestAttribute("actionPerForm",  "siap.siep.richiesta.action.ActLoadInserisciTrasmissioneCompetenza");

	  return PG_LOAD_RICERCA_TITOLO_TRASMISSIONE_COMP;
  }

}
