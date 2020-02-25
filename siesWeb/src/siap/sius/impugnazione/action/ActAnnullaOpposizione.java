package siap.sius.impugnazione.action;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;


/**
* <p>Title: ActAnnullaOpposizione</p>
* <p>Description: Classe Action per annullare l'Opposizione</p>
* <p>Copyright: Copyright (c) 2002</p>
* @since 06/2014
*/

public class ActAnnullaOpposizione extends ActionSius implements ICostantiImpugnazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    
    String lRectPage = null;
    
    if (   isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE) 
        || isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO) 
        || isRequestParameterNullObj(CAMPO_MOTIVO_ANNULLAMENTO) 
       )
      throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati");

    gestioneRitorno();

    //=============================================
    ImpugnazioneModel lImpMod = new ImpugnazioneModel();

    lImpMod.setIdImpugnazione  (getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));    
    
    lImpMod.setDataAnnullamento   (DateUtils.getSysDate());
    lImpMod.setFlagAnnullamento   ("S");
    lImpMod.setMotivoAnnullamento (getRequestStringParameter(CAMPO_MOTIVO_ANNULLAMENTO));

    lImpMod.setCodOperatoreAggiornamento (getCodUtenteConnesso());
    lImpMod.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
    lImpMod.setDataAggiornamento         (lImpMod.getDataAggiornamento());
    

    IImpugnazione lImpCtrl = SIUSLookupRemote.getImpugnazioneRemote();
    lImpCtrl.ExAnnullaImpugnazione(lImpMod , getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    
    lRectPage = ritornoDopoCancellazione("Opposizione annullata!", lRectPage);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lRectPage;
  }


}