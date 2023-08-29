package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
* <p>Title: ActLoadDettaglioRinnovoRicercheOIPP</p>
* <p>Description: Classe Action per il caricamento del dettaglio del RINNOVO per omesse notifiche 
* per gli ordini di ingiunzione al pagamento</p>
* @version 1.0
* @since MEV_2023-33
*/
public class ActLoadDettaglioRinnovoRicercheOIPP extends ActionSiap implements ICostantiSanzioneSostitutiva {
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	 public String processRequest() throws F3BException
	  {
	    String lIdRinnovo = this.getRequestStringParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);

	    RinnovoModel lRinMod = new RinnovoModel();
	    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
	    lRinMod = lCtrl.ExRicercaRinnovoByKey(new BigDecimal(lIdRinnovo));

	    VerbaleModel lVerMod = new VerbaleModel();
	    IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();

	    if(lRinMod != null && lRinMod.getIdRinnovo() != null && lRinMod.getVerIdVerbale()!= null)
	    {
	      lVerMod = lCtrlVer.ExRicercaVerbaleByKey(lRinMod.getVerIdVerbale());
	    }

	    setRequestAttribute("verbale",lVerMod);
	    setRequestAttribute("rinnovo",lRinMod);

	    return PG_DETTAGLIO_OMESSA_NOTIFICA_OIPP;
	 }	
	
}
