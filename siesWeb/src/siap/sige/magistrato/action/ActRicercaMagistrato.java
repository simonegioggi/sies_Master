package siap.sige.magistrato.action;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.StringUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActRicercaMagistrato</p>
* <p>Description: Classe Action per la ricerca di Magistrato</p>
* La ricerca è ristretta ai magistrati impiegati nello stesso ufficio dell'utente collegato</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/

public class ActRicercaMagistrato extends ActionSiap implements ICostantiMagistrato
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    super.setLinkRitorno();
    
    String lReturnPage = ""; String lPagina = "1";
    
    if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
      lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

    MagistratoModel lMagMod = new MagistratoModel() ;
    lMagMod.setCodMagistrato(StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COD_MAGISTRATO)).toUpperCase()) );
    lMagMod.setCognome(StringUtils.convertSqlString((getRequestStringParameter(CAMPO_COGNOME)).toUpperCase()));
    lMagMod.setNome(StringUtils.convertSqlString((getRequestStringParameter(CAMPO_NOME)).toUpperCase()));
    lMagMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

    IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
    ArrayList <MagistratoModel>lArrayList = lCtrl.ExRicercaMagistrato(lMagMod, Integer.parseInt(lPagina));
	

    if (lArrayList.size() == 1)
    { 
      // unico Magistrato
      MagistratoModel lMagistrato =  new MagistratoModel((MagistratoModel)lArrayList.get(0));
      // [EC] 20171013 RICARICO LE INFORMAZIONI DEL MAGISTRTO COMPRESE LE SUE SEZIONI
      MagistratoModel lMagModCompleto = lCtrl.ExRicercaMagistratoByCodEdUfficioAppartenenza(lMagistrato.getCodMagistrato(), getCodUfficioUtenteConnesso());
      
      //Inserisce il model Curatore nella request
      setRequestAttribute("magistrato", lMagModCompleto);
      setFunctionsAvailableToRequest("siap.sige.magistrato.action.ActLoadDettaglioMagistrato");
      lReturnPage = PG_LOAD_DETTAGLIOMAGISTRATO;
    }
    else
    {  
      // Paginazione
      int CountRisultati;
      if (isRequestParameterNullObj("CountRisultati"))
      {
        CountRisultati = lCtrl.ExGetNumRicercaMagistrato(lMagMod);
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.warn("CountRisultati : " + CountRisultati);
      }
      else
        CountRisultati=getRequestIntParameter("CountRisultati");

      setRequestAttribute("CountRisultati",new BigDecimal("" + CountRisultati));
      setRequestAttribute(IWebConstants.NUM_PAGE,lPagina);
      setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
      
      // Lista dei Magistrati
      setRequestAttribute("magistrati", lArrayList);
      lReturnPage = PG_RICERCAMAGISTRATO;
    }
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return lReturnPage;
  }
}