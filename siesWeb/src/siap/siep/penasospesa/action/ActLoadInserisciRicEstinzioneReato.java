package siap.siep.penasospesa.action;

import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRicEstinzioneReato
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di una Richiesta Estinzione Reato.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * <p>
 * @author: Luigi
 * </p>
 * 
 * @version 1.0
 */

public class ActLoadInserisciRicEstinzioneReato extends ActLoadInserisciRichiesta {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		if (mNomeAction == null || mNomeAction.length() == 0) {
			mNomeAction = "siap.siep.penasospesa.action.ActLoadInserisciRicEstinzioneReato";
			mNomeJsp = PG_LOAD_INSERISCIRICHIESTAESTINZIONEREATO;
		}

		return super.processRequest();

	}

	@SuppressWarnings("rawtypes")
	protected String preparazioneForm() throws F3BException {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciRicEstinzioneReato: inizio");

		setRequestAttribute("modalita", "I");

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setCodiceAlternativo("ESTINZIONE_REATO");
		Collection lColMotivo = lDecodifiche.ExRicercaDecodificheOrdinatePerCodice(lModel);
		setRequestAttribute("oggetto", lColMotivo);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("ActLoadInserisciRicEstinzioneReato: fine");

		return mNomeJsp; // restituisce la jsp di VIEW
	}

}