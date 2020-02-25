package siap.sius.rifasiep.action;

import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.rifasiep.controller.IRiferimentoFascicoloSiep;
import siap.sius.rifasiep.model.RiferimentoFascicoloSiepModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaRifFascicoloSiep
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di Riferimenti altri titoli.
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaRifFascicoloSiep extends ActionSiap
		implements ICostantiRifFascicoloSiep, ICostantiFascicoloSius {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Prepara il link di Ritorno.
		this.setLinkRitorno();

		// Popola il model RiferimentoFascicoloSiepModel con l'Id_Fascicolo_Sius prelevato dalla request.
		RiferimentoFascicoloSiepModel lRFSMod = new RiferimentoFascicoloSiepModel();
		lRFSMod.setFasSiuIdFascicoloSius(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIUS));

		// Chiama la RemoteInterfacce del controller udienza.
		IRiferimentoFascicoloSiep lCtrlRFS = SIUSLookupRemote.getRiferimentoFascicoloSiepRemote();

		// Invoca il metodo della ricerca.
		Vector lRiferimenti = new Vector();
		lRiferimenti = lCtrlRFS.ExRicercaRiferimentoFascicoloSiep(lRFSMod);
		if (lRiferimenti.size() == 0)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun Riferimento Fascicolo Sius trovato");

		// Imposta l'elenco delle udienze nella request.
		setRequestAttribute("riferimenti", lRiferimenti);

		// Ritorna la View Jsp
		return PG_ELENCO_RIFERIMENTI;
	}

}