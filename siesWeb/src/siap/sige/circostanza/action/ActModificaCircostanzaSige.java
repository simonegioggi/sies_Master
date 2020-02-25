package siap.sige.circostanza.action;


/**
* <p>Title: ActModificaCircostanza</p>
* <p>Description: Classe Action per la modifica di Circostanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import siap.siep.circostanza.action.ActModificaCircostanza;
import siap.siep.circostanza.model.CircostanzaModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActModificaCircostanzaSige extends ActModificaCircostanza 
{
	
  /**
  * Azione di Modifica del Circostanza Sige
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws Exception
  {
	// Lettura dalla session dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);

    letturaDati(null);
     
    CircostanzaModel llCirModRet = lCtrl.ExModificaCircostanza(lCirMod, flagAgg, flagGiudizio, 
  		flagSentenza, codBil, noteBil, null ,lIdFasSigeSen);
  
    setRequestAttribute("modalita", "M");
    setRequestAttribute("circostanza", llCirModRet);

    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.circostanza.action.ActRicercaCircostanzaSige";
    return lPage;
  }
 
  
}