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
* <p>Title: ActAnnullaImpugnazione</p>
* <p>Description: Classe Action per annullare l'Impugnazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActAnnullaImpugnazione extends ActionSius
 implements ICostantiImpugnazione
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );
    String lRectPage = null;
    if (isRequestParameterNullObj(CAMPO_ID_IMPUGNAZIONE) || isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO) || isRequestParameterNullObj(CAMPO_MOTIVO_ANNULLAMENTO) )
      throw new SIUSException(SIUSException.USER_MESSAGE, "Errore nei dati");

    gestioneRitorno();

    annulla();
    lRectPage = ritornoDopoCancellazione("Impugnazione annullata!", lRectPage);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );

    return lRectPage;
  }

  private void annulla() throws Exception
  {
    //Istanzio il Model e lo carico con quello posto nella request.
    ImpugnazioneModel LimpMod = new ImpugnazioneModel();

   //Dati da aggiornare in  Impugnazione
    LimpMod.setIdImpugnazione(getRequestBigDecimalParameter(CAMPO_ID_IMPUGNAZIONE));
    LimpMod.setCodOperatoreAggiornamento(getCodUtenteConnesso()); //Codice dell'operatore che annulla
    LimpMod.setCodUfficioAggiornamento( getCodUfficioUtenteConnesso()); //Codice dell'operatore che annulla
    LimpMod.setDataAggiornamento(DateUtils.getSysDate());
    LimpMod.setDataAnnullamento(LimpMod.getDataAggiornamento());
    LimpMod.setFlagAnnullamento("S");
    LimpMod.setMotivoAnnullamento(getRequestStringParameter(CAMPO_MOTIVO_ANNULLAMENTO));

   // Viene richiamato il Controller per eseguire l'Update
   IImpugnazione lImpCtrl = SIUSLookupRemote.getImpugnazioneRemote();
   lImpCtrl.ExAnnullaImpugnazione(LimpMod,getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug( "ID Impugnazione Aggiornato: " + LimpMod.getIdImpugnazione() );
   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug( "ID Evento Aggiornato: " + getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO) );

  }

}