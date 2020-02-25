package siap.sige.giudicepopolare.action;

//Import per le combo.
import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.sige.giudicepopolare.controller.IGiudicePopolare;
import siap.sige.giudicepopolare.model.GiudicePopolareModel;
import siap.sige.sezione.util.SezioneUtils;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaGiudicePopolare</p>
* <p>Description: Classe Action per la load inserisci di GiudicePopolare</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia S.p.A.</p>
* @version 1.0
*/
public class ActLoadModificaGiudicePopolare extends ActionSiap 
implements ICostantiGiudicePopolare
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
  * Carica la form di modifica di un GiudicePopolare.
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
    
    String lIdGiudicePopolare = getRequestStringParameter(CAMPO_ID_GIUDICE_POPOLARE);

    // Lock
    LockModel lck = 
      LockController.lockIfNotLocked(getServletContext() ,"GiudicePopolare" ,lIdGiudicePopolare ,getCodUtenteConnesso() ,getSession().getId());
    if (lck!=null)
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "L'" + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi !");
      return IWebConstants.PG_MESSAGE;
    }

    if (lIdGiudicePopolare.equals("0"))
      throw new F3BException(F3BException.USER_MESSAGE,"Non è consentita la modifica di questo record: GiudicePopolare di default.");

    // Chiama il controller.
    IGiudicePopolare lCtrl = SIGELookupRemote.getGiudicePopolareRemote();
    GiudicePopolareModel lGiuPopMod = 
      lCtrl.ExRicercaGiudicePopolareByKey(getRequestBigDecimalParameter(CAMPO_ID_GIUDICE_POPOLARE));

    // Inserire ComboBOX.
    Option lOption;
   
    // Elenco delle Nazioni.
    lOption = new Option(DecodificheManager.getInstance().getNazioni(), lGiuPopMod.getCodStatoNascita());    
    setRequestAttribute("elencoNazioni", "" + lOption );
    
    // Elenco dei Ruoli.
    lOption = 
      new Option( DecodificheManager.getInstance().getRuoloGiudicePopolare(),lGiuPopMod.getCodRuolo());
    setRequestAttribute( "elencoRuoli", "" + lOption );
    
    // Elenco delle Sezioni. 
    lOption = 
      new Option(SezioneUtils.getElencoSezioni(getCodUfficioUtenteConnesso()), Option.BLANK_ITEM);
    if( lGiuPopMod.getSezIdSezione() != null )
      lOption.setSelected(lGiuPopMod.getSezIdSezione().toString());
    
    setRequestAttribute("elencoSezioni", lOption.toString());

    // Elenco dei Sessi.
    lOption = 
      new Option(DecodificheManager.getInstance().getSesso(), Option.BLANK_ITEM);
    lOption.setSelected(lGiuPopMod.getCodSesso());
    setRequestAttribute("elencoSessi", "" + lOption );

    // Imposta Modalità di modifica.
    setRequestAttribute("modalita", "M");    
    setRequestAttribute("giudicepopolare", lGiuPopMod);
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
    
    return PG_LOAD_INSERISCIGIUDICE_POPOLARE;  //restituisce la jsp di VIEW
  }
}