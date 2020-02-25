package siap.sige.circostanza.action;

/**
 * <p>Title: ActInserisciCircostanza<Sige/p>
 * <p>Description: Classe Action per l'inserimento di Circostanza Sige</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: Agile</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.siep.circostanza.action.ActInserisciCircostanza;
import siap.siep.circostanza.controller.ICircostanza;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import f3b.util.F3BException;
import f3b.web.IWebConstants;


public class ActInserisciCircostanzaSige extends ActInserisciCircostanza 
{

	/**
   * Azione di Inserimento del Circostanza
   * @return Nome della pagina JSP da visualizzare
   * al termine dell'elaborazione
   * @throws F3BException
   */
  public String processRequest() throws Exception
  {
	// Lettura dalla session dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);

    // funzione di lettura dati dalla form ereditata dall'ancestor
    letturaDati(null);

    ICircostanza lCtrl = SIEPLookupRemote.getCircostanzaRemote();
    lCtrl.ExInserisciCircostanze(VCRModel, flagAgg, flagGiudizio, flagSentenza, codBil, noteBil, null, lIdFasSigeSen);
   
    // setta la risposta nella request
   // setRequestAttribute("ComingFromInsert", "YES");

    //Prepara la pagina di destinazione
    String lPage = "";
    lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.circostanza.action.ActRicercaCircostanzaSige";

    return lPage;
  }
  
  
}