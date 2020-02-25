package siap.sius.udienza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;


/**
* <p>Title: ActCacellaRinvioUdienza </p>
* <p>Description: Classe Action per l'annullamento di un Rinvio Udienza.
* </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActCancellaRinvioUdienza extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: inizio" );

    String lRectPage = ""; String nextAct = null; BigDecimal lIdFasSius = null;

    // Preleva dalla sessione il model fascicoloSiusGP
    FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
    if(lFascicoloGPModel == null || lFascicoloGPModel.getFascicoloSiusModel() == null || lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius() == null)
      throw new SIUSException(SIUSException.USER_MESSAGE,"Fascicolo SIUS non in sessione");

    lIdFasSius = lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius();
    
    // ID UDIENZA_PROCEDIMENTO da annullare
    BigDecimal lIdUdiPro = getRequestBigDecimalParameter(ICostantiUdienzaProcedimento.CAMPO_ID_UDIENZA_PROCEDIMENTO);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "id udienza_procedimento -> "+ lIdUdiPro );

    // Ricerca di UDIENZA_PROCEDIMENTO da aggiornare
    IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
    UdienzaProcedimentoModel lUdiProcModel = lCtrl.ExRicercaUdienzaProcedimentoByKey(lIdUdiPro);
    
    // Viene valorizzato il model da aggiornare
    //lUdiProcModel.setIdUdienzaProcedimento(lIdUdiPro);
    lUdiProcModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lUdiProcModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
    lUdiProcModel.setDataAggiornamento(DateUtils.getSysDate());
    //lUdiProcModel.setFlagRinviata(ICostantiUdienzaProcedimento.UDIENZA_ANNULLATA);
    
    lCtrl.ExCancellaRinvioUdienza(lUdiProcModel, lIdFasSius );

    if (!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE))
    {
      nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
      if (!isRequestParameterNullObj("noQuery"))
      {
        nextAct += "&noQuery=";
        nextAct += "OK";
      }
    }
    
    lRectPage = ritornoDopoCancellazione( "Cancellazione Avvenuta Correttamente!", nextAct);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( getClass().getName() + ".processRequest: fine" );
    
    return lRectPage;
  }
}