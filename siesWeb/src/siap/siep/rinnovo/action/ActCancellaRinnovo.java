package siap.siep.rinnovo.action;


/**
* <p>Title: ActCancellaRinnovo</p>
* <p>Description: Classe Action per la cancellazione </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 3.0
*/
  
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.scadenzario.action.ICostantiScadenzario;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


public class ActCancellaRinnovo extends ActionSiap implements ICostantiRinnovo
{
 /*****************************************************************************
  * Azione per la cancellazione dei dati. 
  * 
  * @return PG_MESSAGE di avvenuta cancellazione
  * @throws F3BException
  *****************************************************************************/
  public String processRequest() throws F3BException {
    //==========================================
    // Recupera la key del record da Cancellare 
    //==========================================
	BigDecimal lIdRinnovo        = getRequestBigDecimalParameter (CAMPO_ID_RINNOVO) ;
	String lIdScadenzario        = null;
	String lfieldname            = null;
	
	if(!this.isRequestParameterNullObj(ICostantiScadenzario.CAMPO_ID_SCADENZARIO))
		lIdScadenzario = getRequestStringParameter (ICostantiScadenzario.CAMPO_ID_SCADENZARIO) ;

	if(!this.isRequestParameterNullObj("fieldname"))
		lfieldname = getRequestStringParameter ("fieldname") ;
	
    //========================================== 
    // Istanzia il model   
    //========================================== 
	RinnovoModel lRinMod = new RinnovoModel();

	lRinMod.setIdRinnovo(lIdRinnovo);

    //====================================================== 
    // Recupera il Controller ed effettua la cancellazione 
    //====================================================== 
    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    lCtrl.ExCancellaRinnovo(lRinMod);

    //===========================================================
    // Restituisce la pagina di Conferma avvenuta Cancellazione. 
    //===========================================================
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione effettuata correttamente!");
 
    
    if(!this.isRequestParameterNullObj("LinkActionRitorno") && 
       this.getRequestStringParameter( "LinkActionRitorno") != null &&
       !this.getRequestStringParameter( "LinkActionRitorno").equals(""))
    {
	 RedirectTo lRedirigi = new RedirectTo();
     lRedirigi.setPage( IWebConstants.PG_MAIN );
     lRedirigi.setAction(getRequestStringParameter( "LinkActionRitorno"));
     if(lIdScadenzario != null)
      lRedirigi.setParameter(ICostantiScadenzario.CAMPO_ID_SCADENZARIO, lIdScadenzario);
     else if(lfieldname != null)
      lRedirigi.setParameter("fieldname", lfieldname);  
     
     setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );
    }
    
    return IWebConstants.PG_MESSAGE;

  }
}