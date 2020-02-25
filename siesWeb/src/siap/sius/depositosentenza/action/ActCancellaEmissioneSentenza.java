package siap.sius.depositosentenza.action;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActCancellaEmissioneSentenza</p>
* <p>Description: Classe Action per cancellare l'emissione
* di una Sentenza</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActCancellaEmissioneSentenza extends ActionSiap implements ICostantiDepositoSentenza
{
  public String processRequest() throws Exception
  {

    // chiama il controller
    IDepositoSentenza lCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();

    // ricerca del record da cancellare
    DepositoSentenzaModel lDepSen = null;
    lDepSen = lCtrl.ExRicercaDepositoSentenzaByEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));
    if (lDepSen == null)
       throw new F3BException("record DepositoSentenza inesistente");

    // Nel Model del DepositoSentenza vengono valorizzati i dati necessari agli aggiornamenti
    lDepSen.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
    lDepSen.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lDepSen.setDataAggiornamento(DateUtils.getSysDate());

    // Cancellazione
    lCtrl.ExCancellaDepositoSentenza(lDepSen);

    String retPage = null;
    String nextAct = null;

   if(!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE))
   {
      nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
      if (!isRequestParameterNullObj("noQuery"))
      {
        nextAct += "&noQuery=";
        nextAct += "OK";
      }
   }
   
   nextAct = "siap.sius.depositosentenza.action.ActLoadFSPEmissioneSentenzaTDS";
   retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);

   return retPage;
  }
}