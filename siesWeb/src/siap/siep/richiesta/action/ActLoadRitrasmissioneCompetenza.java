package siap.siep.richiesta.action;
    
import java.math.BigDecimal;

import siap.jms.JMSLookupRemote;
import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;


/**
 * <p>Title: ActLoadRitrasmissioneCompetenza</p>
 * <p>Description: Classe Action per la load inserisci della Ritrasmissione per Competenza</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 * @version 1.0
 */

public class ActLoadRitrasmissioneCompetenza extends ActionSiap
                                                    implements ICostantiRichiesta
{
  public String processRequest() throws F3BException
  {    
    UtenteModel lUtenteConnesso = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);    
    BigDecimal lIdMessage = this.getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
        
    IMessaggio lCrtl = JMSLookupRemote.getMessaggioRemote();
    MessaggioModel lMess = lCrtl.ExRicercaMessaggioByKey(lIdMessage);
    this.setRequestAttribute("Messaggio", lMess);
        
    setRequestAttribute("descrComune",  lUtenteConnesso.getUfficioUtente().getDescrComune());
    setRequestAttribute("actionPerForm",  "siap.sius.presaincarico.action.ActRitrasmissioneAttiCompetenza");
    
    return PG_LOAD_RITRASMISSIONE_COMP;
  }

}
