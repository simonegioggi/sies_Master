package siap.sius.impugnazione.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

/**
* <p>Title: ActLoadAnnullaOpposizione</p>
* <p>Description: Classe Action per la load Annullamento Opposizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadAnnullaOpposizione extends ActionSius implements ICostantiImpugnazione
{

  public String processRequest() throws Exception
  {
    if (   isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO) 
        || isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE)
       )
      throw new SIUSException(SIUSException.USER_MESSAGE,"Errore nei dati !");
    
    
    BigDecimal lIdEvento = getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO);

    IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
    ImpugnazioneModel lImpMod = null;
    lImpMod = lCtrl.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));

    // Lock
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"Impugnazione",lImpMod.getIdImpugnazione().toString(),getCodUtenteConnesso(),getSession().getId());
    if (lck!=null)
    {
     setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il/la  "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
     return IWebConstants.PG_MESSAGE;
    }

    setRequestAttribute("nextAction", "siap.sius.impugnazione.action.ActAnnullaOpposizione" );
    setRequestAttribute(CAMPO_ID_IMPUGNAZIONE, lImpMod.getIdImpugnazione().toString());
    setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString());
    return PG_LOAD_ANNULLAMENTO_IMPUGNAZIONE;
  }



}