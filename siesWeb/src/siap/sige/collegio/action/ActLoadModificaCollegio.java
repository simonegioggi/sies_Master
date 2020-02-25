package siap.sige.collegio.action;

//import f3b.util.F3BException;
import org.apache.log4j.Logger;

import siap.sico.lock.controller.LockController;
//import siap.sico.decodifiche.controller.DecodificheManager;
//import siap.sico.web.ActionSiap;
import siap.sico.lock.model.LockModel;
import siap.sige.collegio.controller.ICollegio;
import siap.sige.collegio.model.CollegioModel;
import siap.sige.collegio.util.CollegioUtils;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
//import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaCollegio</p>
* <p>Description: Classe Action per la load modifica del Collegio</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActLoadModificaCollegio extends ActionCollegio 
implements ICostantiCollegio
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Carica la form di modifica di un Collegio.
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
    
    String lIdCollegio = 
      getRequestStringParameter(CAMPO_ID_COLLEGIO);

    // Lock
    LockModel lck = 
      LockController.lockIfNotLocked(getServletContext() ,"Collegio" ,lIdCollegio ,getCodUtenteConnesso() ,getSession().getId());
    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Il " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }

    // chiama il controller
    ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
    CollegioModel lColMod = 
      lCtrl.ExRicercaCollegioByKey(getRequestBigDecimalParameter(CAMPO_ID_COLLEGIO));

    Option lOption = 
      new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    
    if( lColMod.getSezIdSezione()!= null )
      lOption.setSelected(lColMod.getSezIdSezione().toString());
    setRequestAttribute("elencoSezioni", "" + lOption );
    
    lOption = 
      new Option(CollegioUtils.getElencoCodiciCollegi(), Option.BLANK_ITEM);
    lOption.setSelected(lColMod.getCodCollegio());
    setRequestAttribute("elencoCodiciCollegi", "" + lOption );
    
    // Imposta Modalità.
    setRequestAttribute("modalita", "M");
    setRequestAttribute("collegio", lColMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");

    return getInsViewJSP();  //Restituisce la jsp di VIEW form inserimento.
  }
  
  //
  // 
  //
  /*
  public String getInsViewJSP() throws Exception
  {
    String lPage = new String("");
    
    if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("CAP") )
      lPage = PG_LOAD_INSERISCICOLLEGIOCAP;
    else if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("CAS") )
      lPage = PG_LOAD_INSERISCICOLLEGIOCAS;
    else if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("CASAP") )
      lPage = PG_LOAD_INSERISCICOLLEGIOCASAP;
    else if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("DIB") )
      lPage = PG_LOAD_INSERISCICOLLEGIODIB;
    else if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("DIBM") )
      lPage = PG_LOAD_INSERISCICOLLEGIODIBM;  
    else if( getUfficioUtenteConnesso().getCodTipoUfficio().equalsIgnoreCase("CAPSM") )
      lPage = PG_LOAD_INSERISCICOLLEGIOCAPSM;
    else
      throw new F3BException(F3BException.USER_MESSAGE, "Funzione non gestita dal tipo ufficio di competenza.");

    return lPage;
  }
  */
}