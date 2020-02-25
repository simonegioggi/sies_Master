package siap.sige.tenore.action;

import java.math.BigDecimal;

import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
* <p>Title: ActCancellaOggettoRichiesta</p>
* <p>Description: Classe Action per la Gestire la Cancellazione di un Oggetto.</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActCancellaOggettoRichiesta extends ActionSige implements ICostantiTenoreSige
{
	public String processRequest() throws Exception 
	{
		
		// ID Richiesta Sige parametro di ingresso
		// se valorizzato bisogna procedere alla cancellazione generale,
		// ossia tutti gli oggetti/titoli esecutivi/reati legati alla richiesta sige
		BigDecimal lIdRichiestaSige = null;
		if(!isRequestParameterNullObj(CAMPO_RIC_SIG_ID_RICHIESTA_SIGE)){
			lIdRichiestaSige = getRequestBigDecimalParameter(CAMPO_RIC_SIG_ID_RICHIESTA_SIGE);
		}
		
		// ID Tenore da cancellare parametro di ingresso
		BigDecimal lIdTenore = null;
		if(!isRequestParameterNullObj(CAMPO_ID_TENORE_SIGE)){
			lIdTenore = getRequestBigDecimalParameter(CAMPO_ID_TENORE_SIGE);
		}
	    
		// ID Sentenza da cancellare parametro di ingresso
		BigDecimal lIdSentenza = null;
		if(!isRequestParameterNullObj(CAMPO_SEN_ID_SENTENZA)){
			lIdSentenza = getRequestBigDecimalParameter(CAMPO_SEN_ID_SENTENZA);
		}
		
		// Cancellazione
        ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
		lCtrl.ExCancellaTenoreSige(lIdRichiestaSige, lIdTenore, lIdSentenza);

	    // Ritorno al punto di partenza
	    String lRetPage = ritornoDopoCancellazione("Cancellazione effettuata con successo.", null);
	    return lRetPage;
	 }
}
