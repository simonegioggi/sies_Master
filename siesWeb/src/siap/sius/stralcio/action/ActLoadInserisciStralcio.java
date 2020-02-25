package siap.sius.stralcio.action;

/**
 * <p>Title: ActLoadInserisciStralcio</p>
 * <p>Description: Classe Action per la load inserisci dello stralcio oggetti SIUS</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;

public class ActLoadInserisciStralcio extends ActionSius implements ICostantiStralcio {

	public String processRequest() throws Exception {

		// Attivazione punto di Ritorno
		setLinkRitorno();

		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		// BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

		if (lFasGPMod.getTenori().length < 2)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Stralcio Impossibile: Il procedimento è riferito ad un solo Oggetto!");

		// Preleva l'id di generale procedimento.
		BigDecimal lIdGenProc = lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento();
		if (lIdGenProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Id Generale Procedimento assente");

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento declaratorio
		// già emesso per il Fascicolo SIUS.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso un altro.
		RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lIdGenProc);
		boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
		if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

		return PG_LOAD_INSERISCISTRALCIO; // restituisce la jsp di VIEW
	}

}