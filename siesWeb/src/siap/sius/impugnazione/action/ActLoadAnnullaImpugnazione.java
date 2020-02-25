package siap.sius.impugnazione.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadAnnullaImpugnazione</p>
* <p>Description: Classe Action per la load Annullamento Impugnazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadAnnullaImpugnazione extends ActionSius implements ICostantiImpugnazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    if (this.isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO) || isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO))
      throw new SIUSException(SIUSException.USER_MESSAGE,"Errore nei dati !");
    BigDecimal lIdEvento = getRequestBigDecimalParameter( ICostantiEvento.CAMPO_ID_EVENTO);
    String lCodTipoProv =  getRequestStringParameter( ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO);

    IImpugnazione lCtrl = SIUSLookupRemote.getImpugnazioneRemote();
    // 16/11/2007 Prevista la lettura puntuale per chiave "IdImpugnazione".  
    ImpugnazioneModel lImpMod = null;
    if( !isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE))
    {
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( CAMPO_ID_IMPUGNAZIONE + ": " + getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
      lImpMod = lCtrl.ExRicercaImpugnazioneByKey(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
    } else {
      Option lOptTipoRicorso = new Option( DecodificheManager.getInstance().getTipoRicorso());
      String[] lFilterRicorso = {"01","02","03"};
      lOptTipoRicorso.setFilter(lFilterRicorso);      
      lImpMod = lCtrl.ExRicercaImpugnazioneByIdEventoTipoProvv (lIdEvento,lCodTipoProv, lOptTipoRicorso.getCodes(), null);
    }
    
    // Lock
    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"Impugnazione",lImpMod.getIdImpugnazione().toString(),getCodUtenteConnesso(),getSession().getId());
    if (lck!=null)
    {
     setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il/la  "+lck.getEntity()+" è in gestione ad un altro utente!<BR>Riprovare più tardi !");
     return IWebConstants.PG_MESSAGE;
    }

    setRequestAttribute("nextAction", "siap.sius.impugnazione.action.ActAnnullaImpugnazione" );
    setRequestAttribute(CAMPO_ID_IMPUGNAZIONE, lImpMod.getIdImpugnazione().toString());
    setRequestAttribute(ICostantiEvento.CAMPO_ID_EVENTO, lIdEvento.toString());
    return PG_LOAD_ANNULLAMENTO_IMPUGNAZIONE;
  }



}