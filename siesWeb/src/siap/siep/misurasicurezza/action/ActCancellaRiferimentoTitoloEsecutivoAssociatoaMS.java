package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActCancellaRiferimentoTitoloEsecutivoAssociatoaMS				</p>
 * <p>Description: Nell'ambito della gestione Misure Sicurezza, 			</p>
 * <p> 		questa Action cancella il Riferiemnto a un Titolo esecutivo 	<p>
 * <p> 		di altro Procedimento, anche esterno; 									</p>
 * <p>		(ES. nel caso in cui la M.S. sia arrivata nel fascicolo da un CUMULO)	</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Intersistemi Italia spa</p>
 * @version 8.2
 */

public class ActCancellaRiferimentoTitoloEsecutivoAssociatoaMS 
	extends ActionSiap 
	implements ICostantiMisuraSicurezza  
{
	public String processRequest() throws Exception
	{
		
		// Ricerca Titolo Esecutivo per associarlo alla M.S.
		
		String lId = "";
		IMisuraSicurezza lCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
	    MisuraSicurezzaModel lMisMod = new MisuraSicurezzaModel ();
	    
		if( !isRequestParameterNullObj(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA) &&
			getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA) != null  )
		{
			lId = getRequestStringParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA);

		    lMisMod = lCtrl.ExRicercaMisuraSicurezzaByKey(new BigDecimal(lId));
		    if(lMisMod == null || lMisMod.getIdMisuraSicurezza() == null)
		    {
		    	throw new F3BException(F3BException.USER_MESSAGE, "Misura di Sicurezza Non Trovata in archivio");
		    }
			
		}
		
    	IRiferimentoFascicoloSiep lCtrlS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();
	  
    	if(lMisMod.getFasSieIdFascicoloSiepRif()!=null)
		{		
    		lMisMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    		lMisMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    		lMisMod.setDataAggiornamento(DateUtils.getSysDate());
    		
    		lCtrlS.ExCancellaRiferimentoFascicoloSiepUpdateMisuraSic(lMisMod);
    		
		}	

    	BigDecimal lidMis= new BigDecimal(lId);	
	    String lPage="";
	    lPage = IWebConstants.PG_MAIN + "?" + 
	    	IWebConstants.ACTION_FIELD + "=siap.siep.misurasicurezza.action.ActLoadDettaglioMisuraSicurezza";
	    lPage += "&" + "IdMisuraSicurezza" + "=" + lidMis;

	    return lPage;

	}	// Chiude processReq()

}	// Chide Classe 
