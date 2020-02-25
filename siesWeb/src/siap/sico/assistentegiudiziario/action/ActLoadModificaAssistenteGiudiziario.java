package siap.sico.assistentegiudiziario.action;

import siap.sico.assistentegiudiziario.controller.IAssistenteGiudiziario;
import siap.sico.assistentegiudiziario.model.AssistenteGiudiziarioModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import per le combo
import f3b.web.html.Option;


/**
* <p>Title: ActLoadModificaAssistenteGiudiziario</p>
* <p>Description: Classe Action per la load inserisci di Esperto</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadModificaAssistenteGiudiziario extends ActionSiap implements ICostantiAssistenteGiudiziario
{
  public String processRequest() throws F3BException
  {
    Option lOption;

    // Lock
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"Assistente",getRequestStringParameter(CAMPO_ID_ASSISTENTE_GIUDIZIARIO),getCodUtenteConnesso(),getSession().getId());
    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L' "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }


      // chiama il controller
    IAssistenteGiudiziario lCtrl = SICOLookupRemote.getAssistenteGiudiziarioRemote();
    AssistenteGiudiziarioModel lAssMod = lCtrl.ExRicercaAssistenteGiudiziarioByKey(getRequestBigDecimalParameter(CAMPO_ID_ASSISTENTE_GIUDIZIARIO));

    // Inserire  ComboBOX
    if(lAssMod.getFlagStato() != null)
      lOption = new Option( DecodificheManager.getInstance().getFlagStato(),lAssMod.getFlagStato());
    else
      lOption = new Option( DecodificheManager.getInstance().getFlagStato());
    setRequestAttribute( "elencoFlagStato", "" + lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("assistentegiudiziario", lAssMod);

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Modifica di un Assistente Udienza da Funzioni Amministrative.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
    String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);
	  
    return PG_LOAD_INSERISCIASSISTENTEGIUDIZIARIO;  //restituisce la jsp di VIEW
  }
}