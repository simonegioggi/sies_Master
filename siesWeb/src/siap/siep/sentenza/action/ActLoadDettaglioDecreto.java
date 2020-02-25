package siap.siep.sentenza.action;

import java.math.BigDecimal;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;


/**
 * <p>Title: ActLoadDettaglioDecreto</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class ActLoadDettaglioDecreto extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    // Parse della request
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SENTENZA);

    // Riempie il model
    SentenzaModel lSmod = new SentenzaModel();
    lSmod.setIdSentenza(lId);
    lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

    //SentenzaController lCtrl = new SentenzaController();
    ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();
    SentenzaModel lSen = lCtrl.ExRicercaSentenzaByKey(lSmod.getIdSentenza());

    // Inserisce in session la sentenza model.
    setSessionAttribute("sentenza",lSen );
    setSessionAttribute("isSentenza", "false");
    setRequestAttribute("sentenza",lSen );

    //Passa la action di destinazione
    if (!isRequestParameterNullObj("TornaQui"))
    { this.setRequestAttribute("TornaQui", this.getRequestStringParameter("TornaQui"));}

    return PG_DETTAGLIODECRETO;
  }
}
