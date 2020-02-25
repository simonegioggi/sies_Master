package siap.sige.aula.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sige.aula.controller.IAula;
import siap.sige.aula.model.AulaUdienzaModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaAula</p>
* <p>Description: Classe Action per la load modifica dell'Aula</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering</p>
* @version 1.0
*/
public class ActLoadModificaAula extends ActionSiap implements ICostantiAula
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Carica la form di modifica di un'Aula.
  * <p>
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione.
  * <p>
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    
    String lPage = PG_LOAD_INSERISCI_AULA;
    
    BigDecimal lIdAula = getRequestBigDecimalParameter(CAMPO_ID_AULA);
    BigDecimal lIdSezione = getRequestBigDecimalParameter(CAMPO_ID_SEZIONE);

    // chiama il controller
    IAula lCtrl = SIGELookupRemote.getAulaRemote();
    AulaUdienzaModel lAulaMod = lCtrl.ExRicercaAulaByKey(lIdAula, lIdSezione);

    Option lOption = new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    
    if( lAulaMod.getIdSezione()!= null ){
      lOption.setSelected(lAulaMod.getIdSezione().toString());
    }
    setRequestAttribute("elencoSezioni", "" + lOption );

    lOption = new Option( DecodificheManager.getInstance().getFlagSN(), Option.BLANK_ITEM);
    lOption.setSelected(lAulaMod.getFlagPredefinita());
    setRequestAttribute("aulaPredefinita", "" + lOption );    

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("aulaUdienza", lAulaMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");

    return lPage;  //restituisce la jsp di VIEW  
  }

}