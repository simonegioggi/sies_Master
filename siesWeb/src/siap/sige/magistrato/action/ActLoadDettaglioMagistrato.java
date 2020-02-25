package siap.sige.magistrato.action;

import org.apache.log4j.Logger;
//import f3b.util.F3BException;

import siap.sico.web.ActionSiap;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;

/**
 * <p>
 * Title: ActLoadDettaglioMagistrato
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Magistrato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 */
public class ActLoadDettaglioMagistrato extends ActionSiap implements ICostantiMagistrato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		super.gestioneRitorno();

		String lId = getRequestStringParameter(CAMPO_COD_MAGISTRATO);

		IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
		
		// [EC] 20171013 RICARICO LE INFORMAZIONI DEL MAGISTRTO COMPRESE LE SUE SEZIONI
	    MagistratoModel lMagModCompleto = lCtrl.ExRicercaMagistratoByCodEdUfficioAppartenenza(lId,
	    		getCodUfficioUtenteConnesso());

		setRequestAttribute("magistrato", lMagModCompleto);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return PG_LOAD_DETTAGLIOMAGISTRATO;
	}

}