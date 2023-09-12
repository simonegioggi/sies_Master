package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

/**
 * MEV_2023-13: aggiunta classe per caricamento griglia
 * 
 * @author sgioggi
 * @version 1.0
 */
public class ActGrigliaRiscossionePP extends ActionSiap implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

//		if (this.isSessionAttributeNullObj("fascicolo")) {
//			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
//		}
	  
    if (isSessionAttributeNullObj("fascicolo"))
      setRequestAttribute("fascicoloNotInSession", "S");
    
		setRequestAttribute("strFunzione", "Gestione Riscossione Pene Pecuniarie");

		return ICostantiSanzioneSostitutiva.PG_GRIGLIA_RISCOSSIONE_PP;
	}

}