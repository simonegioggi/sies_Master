package siap.sige.unificazione.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sige.decretounificazione.controller.IDecretoUnificazioneSige;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.util.SIGELookupRemote;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActCancellaVerbaleUnificazioneSige </p>
 * <p>Description: Classe Azione per richiesta cancellazione del Verbale di Unificazione di 2 Procedimenti SIGE
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 */
public class ActCancellaVerbaleUnificazioneSige extends ActionSiap implements ICostantiVerbaleUnificazioneSige
{
  public String processRequest() throws Exception
  {
    // Preleva dalla sessione i dati dell'utente connesso.
    String lCodiceOperatore   = getCodUtenteConnesso();
    String lCodiceUfficio     = getCodUfficioUtenteConnesso();
    String lDescrComune       = getUfficioUtenteConnesso().getDescrComune();

    String nextAct = null;
    String retPage = "";

    // Preleva dalla request la chiave dell'evento come parametro
    //BigDecimal lKeyEvento = super.getRequestBigDecimalParameter(ICostantiVerbaleUnificazioneSige.CAMPO_ID_EVENTO_UNIFICAZIONE );
    BigDecimal lKeyProvvedimento = super.getRequestBigDecimalParameter(ICostantiVerbaleUnificazioneSige.CAMPO_ID_VERBALE_UNIFICAZIONE );
    
    BigDecimal idFascicoloSigeUnificato = null;
    if(!isRequestParameterNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE)) {
    	idFascicoloSigeUnificato = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);
    }
    
    IDecretoUnificazioneSige lCtrl = SIGELookupRemote.getDecretoUnificazioneSigeRemote();
    lCtrl.ExCancellaVerbaleUnificazioneSige( lKeyProvvedimento, lCodiceUfficio, lCodiceOperatore, lDescrComune );
    // Azione dopo la cancellazione
    if(!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE))
    {
      nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
      if (!isRequestParameterNullObj("noQuery"))
      {
        nextAct += "&noQuery=";
        nextAct += "OK";
      }
    } else {
    	if(idFascicoloSigeUnificato != null){
    		nextAct = "siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&IdFascicoloSige=" + idFascicoloSigeUnificato;
    	} else {
    		nextAct = "siap.sige.unificazione.action.ActLoadVerificaVerbaleUnificazioneSige";
    	}
    }
    
    retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);

    return retPage; //restituisce la jsp di VIEW
  }
}