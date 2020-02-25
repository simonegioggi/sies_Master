package siap.siep.sentenza.action;

/**
 * <p>Title : ActLoadModificaDatiProvvedimentoMSFuoriSent </p>
 * <p>Description: Load di Modifica dati del decreto/ordinanza relativo a un fascicolo </p>
 * <p>di Misura Sicurezza disposta Fuori Sentenza o Provvisoria	</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Intersistemi Italia s.p.a. </p>
  * @version 8.3
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.sentenza.controller.ISentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import f3b.util.F3BException;
import f3b.web.html.Option;

public class ActLoadModificaDatiProvvedimentoMSFuoriSent extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
    // Parse della request
    String lId = super.getRequestStringParameter(CAMPO_ID_SENTENZA);
    
	  // Lock per evitare accesso contemporaneo alla funzione chiamante che operi sulla stessa sentenza
	  LockModel lck = LockController.lockIfNotLocked(getServletContext(), "Sentenza", lId, getCodUtenteConnesso(),getSession().getId());
	  if (lck != null)
		  throw new SIGEException (F3BException.USER_MESSAGE, "La gestione della  "+lck.getEntity()+" è in gestione ad un altro utente!");
    
    // Riempie il model
    SentenzaModel lSmod = new SentenzaModel();

    lSmod.setIdSentenza(new BigDecimal(lId));
    UtenteModel lUtenteMod = new UtenteModel((UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

    lSmod.setCodUfficioInserimento(lUtenteMod.getUfficioUtente().getCodUfficio());

    ISentenza lCtrl = SIEPLookupRemote.getSentenzaRemote();

    SentenzaModel lSmodRet = lCtrl.ExRicercaSentenzaByKey(lSmod.getIdSentenza());

    //-----------------------------------------------------------------

    String lPage = "";

    setRequestAttribute( "sentenza", lSmodRet );
    setRequestAttribute( CAMPO_ID_SENTENZA, lId );
    
//Tipo Provvedimento Sorv : Decreto / Ordinanza	
    Option lOptionProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(),"-");
    if(  lSmodRet != null && lSmodRet.getIdSentenza() != null &&
    		lSmodRet.getCodTipoProvvedimento() != null)
    {
    	lOptionProvv.setSelected(lSmodRet.getCodTipoProvvedimento());
    }
    setRequestAttribute("tipoProvvedimenti", ""+ lOptionProvv );
    
    String lTipoFasc="";
    Option lAutoOption = new Option();
// a Seconda delL'AUTORITA' EMITTENTE preparo 2 conbo distinte:	<--	--	--	--	
    if(lSmodRet.getCodTipoAutoritaEmittente().compareTo("UDSM")==0 || 
   		lSmodRet.getCodTipoAutoritaEmittente().compareTo("UDS")==0 ||
   		lSmodRet.getCodTipoAutoritaEmittente().compareTo("TDS")==0 )
    {
    	lTipoFasc="FS";
    	// Autorita Emittente: Uffici della Sorveglianza (per Fasc. di Mis. FUORI SENTENZA)
        lAutoOption = new Option(DecodificheManager.getInstance().getTipoUfficio(),"-");
        lAutoOption.setFilter( new String[] {"TDS", "UDS", "UDSM","-"} );
       	lAutoOption.setSelected(lSmodRet.getCodTipoAutoritaEmittente());
    	
    }
    else
    {
    	lTipoFasc="MP";
    	// Autorita Emittente: Giuduci della Cognizione (per fasc. di Mis. PROVVISORIE)
        lAutoOption = new Option(DecodificheManager.getInstance().getTipoUfficioGE() );
        lAutoOption.setSelected(lSmodRet.getCodTipoAutoritaEmittente());
    }
    
    setRequestAttribute("autoritaEmi", "" + lAutoOption); 
    setRequestAttribute("Fascicolo_di", "" + lTipoFasc); 
// 	--	--	--	-->	

    Option lOptionRt = new Option( DecodificheManager.getInstance().getTipoRitoSentenza(), "-");
    if (lSmodRet.getCodTipoAutoritaEmittente().equals("DIB") || lSmodRet.getCodTipoAutoritaEmittente().equals("TRIBSD"))
    { 
    	lOptionRt.setSelected(lSmodRet.getCodTipoRito());
    }
    setRequestAttribute("tipoRito", "" + lOptionRt );
//
    
    lPage =  PG_LOAD_MODIFICA_DATI_PROVVEDIMENTO_MS_FUORI_SENT;
    return lPage; //restituisce la jsp di VIEW
  }
}