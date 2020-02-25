package siap.siep.richiesta.action;

import siap.jms.ICostantiJMS;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;


/**
 * <p>Title: ActLoadRicercaAttiCompetenzaPresiCarico</p>
 * <p>Description: Classe Action per la ricerca degli atti presi in carico</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
 */

public class ActLoadRicercaAttiCompetenzaPresiCarico extends ActionSiap
                                                    implements ICostantiRichiesta
{  
  public String processRequest() throws F3BException
  {   
    UtenteModel lUtenteConnesso = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
    setRequestAttribute("descrComune",  lUtenteConnesso.getUfficioUtente().getDescrComune());
    
    // Imposta Tipo Ufficio SIEP
    Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioPM());
    setRequestAttribute("tipoUfficioSIEP", "" + lOption );

    setRequestAttribute("function_name", "Ricerca atti per Competenza");
    setRequestAttribute("action_name", "siap.sius.presaincarico.action.ActRicercaProvvedimentiRicevuti");
    setRequestAttribute("tipo_provvedimento", ICostantiJMS.TRASFERIMENTO_COMPETENZA);
    setRequestAttribute("stato_fascicoli", "presincarico");

    return PG_LOAD_RICERCA_ATTI_IN_CARICO_COMPETENZA;
  }

}