package siap.sius.udienzaprocedimento.action;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActElencoUdienzeProcedimento
 * </p>
 * <p>
 * Description: Classe Action per la ricerca delle udienze successivamente fissate per un fascicolo.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActElencoUdienzeProcedimento extends ActionSius implements ICostantiUdienzaProcedimento {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		FascicoloGPModel lFasGPMod = null;

		// Gestione bottone di ritorno
		gestioneRitorno();

		// Dati del Fascicolo dalla sessione
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Fascicolo SIUS non presente in sessione!");

		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		if (lFasGPMod == null || lFasGPMod.getGeneraleProcedimentoModel() == null
				|| lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dati Fascicolo non presenti in sessione!");

		// Ricerca Elenco Movimenti Udienza
		Vector lUdiProVect = null;
		IUdienzaProcedimento lUdiProCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		lUdiProVect = lUdiProCtrl.ExRicercaUdienzaProcedimentoUdiByGeneraleProcedimento(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		setRequestAttribute("MovimentiUdienze", lUdiProVect);
		setRequestAttribute("modalita", "action");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LISTAUDIENZEXPROCEDIMENTO;
	}

}